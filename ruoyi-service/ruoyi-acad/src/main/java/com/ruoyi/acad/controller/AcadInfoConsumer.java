package com.ruoyi.acad.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rabbitmq.client.Channel;
import com.ruoyi.acad.config.RabbitConfig;
import com.ruoyi.acad.service.IOnlineResumeService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.utils.StringUtils;
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
public class AcadInfoConsumer {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    //简历的生成
    @Autowired
    private IOnlineResumeService resumeService;

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
        Integer acadId = 0;
        if(msg != null && msg.length() > 3){
            String acadIdStr = msg.substring(msg.indexOf(":")+1,msg.length()-2);
            if(StringUtils.isNotEmpty(acadIdStr)){
                acadId = Integer.valueOf(acadIdStr);
            }
        }

        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();

        log.info("收到院士编码消息: {}", acadId);

        //具体的院士生成简历逻辑
        RE re = this.resumeService.generateResume(acadId);

        if (re != null && re.getStatus()) {
            log.info("生成简历成功！");
        } else {
            /*String msgId = UUID.randomUUID().toString();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("acadId",acadId);
            CorrelationData correlationData = new CorrelationData(msgId);
            rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, JSON.toJSONString(map), correlationData);*/
            log.info("投递失败！");
            channel.basicAck(tag, false);// 消费确认
           //channel.basicNack(tag, false, true);
        }
    }
}
