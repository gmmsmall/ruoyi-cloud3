package com.ruoyi.javamail.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rabbitmq.client.Channel;
import com.ruoyi.javamail.config.RabbitConfig;
import com.ruoyi.javamail.domain.MailTemplate;
import com.ruoyi.javamail.entity.SendMail;
import com.ruoyi.javamail.entity.SendMailItems;
import com.ruoyi.javamail.entity.SendMailStmp;
import com.ruoyi.javamail.service.ISendMailItemsService;
import com.ruoyi.javamail.service.ISendMailService;
import com.ruoyi.javamail.service.ISendMailStmpService;
import com.ruoyi.javamail.service.RedisService;
import com.ruoyi.javamail.util.SendEmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class MailConsumer {

    /*@Autowired
    private IMsgLogService iMsgLogService;*/

    @Autowired
    private ISendMailService sendMailService;
    @Autowired
    private ISendMailItemsService sendMailItemsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ISendMailStmpService sendMailStmpService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //java判断string字符串是不是json格式
    public static boolean isJson(String content) {
        try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws Exception {

        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        msg = msg.replaceAll("\\\\", "");
        if(msg != null && msg.length() > 2){
            /*if(msg.startsWith("\"") && msg.endsWith("\"")){
                msg = msg.substring(1, msg.length() - 1);
            }*/
            msg = msg.substring(1, msg.length() - 1);
            if(isJson(msg)){
                JSONObject json = JSONObject.parseObject(msg);

                MailTemplate mailTemplate = JSON.parseObject(json.getString("mailTemplate"), MailTemplate.class);
                log.info("收到消息: {}", mailTemplate.toString());

                SendMailItems sendMailItems = JSON.parseObject(json.getString("mailItems"), SendMailItems.class);

                MessageProperties properties = message.getMessageProperties();
                long tag = properties.getDeliveryTag();

                boolean success = new SendEmailUtil(mailTemplate, new ArrayList<>()).send();
                if(json.getString("snumber").equals("first")){
                    if (success) {
                        log.info("消费成功！");
                        sendMailItems.setSendflag("1");//先默认发送成功，当两个小时后进行二次确认的时候如果未发送到邮箱，再更新成失败状态，并返回失败原因，此时主表中发送成功或失败的数量要变
                        sendMailItems.setMajorsendflag("1");
                        //消费成功后需要将邮件发送详情落库
                        sendMailItemsService.save(sendMailItems);
                        channel.basicAck(tag, false);// 消费确认
                        //邮件发送成功，则stmp当前的使用数量加1
                        LambdaQueryWrapper<SendMailStmp> stmpLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        stmpLambdaQueryWrapper.eq(SendMailStmp::getName,mailTemplate.getSendType()).eq(SendMailStmp::getStmptime, LocalDate.now());
                        SendMailStmp sendMailStmp = sendMailStmpService.getOne(stmpLambdaQueryWrapper);
                        if(sendMailStmp == null || sendMailStmp.getId() == null){//为了防止生产时间是今天但是消费是明天
                            sendMailStmp = new SendMailStmp();
                            sendMailStmp.setName(mailTemplate.getSendType());
                            sendMailStmp.setStmptime(LocalDate.now());
                            sendMailStmp.setUsenumber(1);
                            sendMailStmpService.save(sendMailStmp);
                        }else{
                            sendMailStmp.setUsenumber(sendMailStmp.getUsenumber()+1);
                            sendMailStmpService.updateById(sendMailStmp);
                        }
                        //邮件发送成功，则待发送的邮件数量减1
                        Integer num = Integer.valueOf(redisService.get(sendMailItems.getLeaderid()+sendMailItems.getLeader()+sendMailItems.getFid()))-1;
                        redisService.set(sendMailItems.getLeaderid()+sendMailItems.getLeader()+sendMailItems.getFid(),String.valueOf(num));
                        SendMail sendMail = sendMailService.getById(sendMailItems.getFid());
                        sendMail.setSuccessnumber(sendMail.getSuccessnumber() + 1);//本次发送成功数量
                        sendMailService.updateById(sendMail);
                    } else {

                        //主/副邮箱投递都失败才算失败
                        if(json.getString("flag").equals("zhu")){//重新投递副邮箱
                            //此时主邮箱也需要有一条记录
                            sendMailItems.setSendflag("2");
                            sendMailItems.setMajorsendflag("2");
                            sendMailItems.setFailreason("发送到服务器失败");
                            sendMailItems.setShowemail(sendMailItems.getEmail());
                            sendMailItemsService.save(sendMailItems);

                            //还需要判断是否有副邮箱
                            if(sendMailItems.getNextemail() != null && !sendMailItems.getNextemail().equals("")){
                                //有副邮箱才进行二次发送，没有就不发了
                                sendMailItems.setType("2");//副邮箱
                                sendMailItems.setShowemail(sendMailItems.getNextemail());
                                sendMailItems.setNextemail(sendMailItems.getNextemail());
                                Map<String,Object> map = new HashMap<>();
                                String msgId = UUID.randomUUID().toString();
                                mailTemplate.setMsgId(msgId);
                                mailTemplate.setTo(sendMailItems.getNextemail());//发送次邮箱
                                map.put("mailTemplate",mailTemplate);
                                map.put("mailItems",sendMailItems);
                                map.put("flag","next");//标志位，zhu表示主邮箱发送，next表示副邮箱发送
                                CorrelationData correlationData = new CorrelationData(msgId);
                                rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, JSON.toJSONString(map), correlationData);// 发送消息
                            }else{//没有副邮箱，邮件直接算发送失败
                                //主表中数据的操作
                                SendMail sendMail = sendMailService.getById(sendMailItems.getFid());
                                sendMail.setFailnumber(sendMail.getFailnumber() + 1);//本次发送失败数量
                                sendMailService.updateById(sendMail);
                            }
                        }else{//表示副邮箱也投递失败，则此时需要增加发送失败的数量
                            SendMail sendMail = sendMailService.getById(sendMailItems.getFid());
                            sendMail.setFailnumber(sendMail.getFailnumber() + 1);//本次发送失败数量
                            sendMailService.updateById(sendMail);
                            //失败原因也需要更新为：发送到stmp服务器失败
                            sendMailItems.setSendflag("2");
                            sendMailItems.setMajorsendflag("2");
                            sendMailItems.setFailreason("发送到服务器失败");
                            sendMailItemsService.save(sendMailItems);

                        }

                        log.info("投递失败！");
                        channel.basicAck(tag, false);// 消费确认
//            channel.basicNack(tag, false, true);
                    }
                }else if(json.getString("snumber").equals("second")){
                    if (success) {
                        sendMailItems.setSendflag("1");//先默认发送成功，当两个小时后进行二次确认的时候如果未发送到邮箱，再更新成失败状态，并返回失败原因，此时主表中发送成功或失败的数量要变
                        sendMailItems.setMajorsendflag("1");
                        //消费成功后需要将邮件发送详情落库
                        sendMailItemsService.save(sendMailItems);
                        channel.basicAck(tag, false);// 消费确认
                    }else{
                        SendMail sendMail = sendMailService.getById(sendMailItems.getFid());
                        sendMail.setFailnumber(sendMail.getFailnumber() + 1);//本次发送失败数量
                        sendMail.setSuccessnumber(sendMail.getSuccessnumber()-1);
                        sendMailService.updateById(sendMail);
                        //失败原因也需要更新为：发送到stmp服务器失败
                        sendMailItems.setSendflag("2");
                        sendMailItems.setMajorsendflag("2");
                        sendMailItems.setFailreason("发送到服务器失败");
                        sendMailItemsService.save(sendMailItems);
                        SendMailItems sendMailItems1 = new SendMailItems();
                        sendMailItems1.setFid(sendMailItems.getFid());
                        sendMailItems1.setAcadencode(sendMailItems.getAcadencode());
                        sendMailItems1.setMajorsendflag("2");
                        LambdaQueryWrapper<SendMailItems> mailItemsLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        mailItemsLambdaQueryWrapper.eq(SendMailItems::getFid,sendMailItems.getFid()).eq(SendMailItems::getAcadencode,sendMailItems.getAcadencode());
                        sendMailItemsService.update(sendMailItems1,mailItemsLambdaQueryWrapper);
                    }
                }
            }
        }
    }
}