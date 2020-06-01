package com.ruoyi.javamail.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.common.utils.SpringContextUtil;
import com.ruoyi.javamail.bo.TemplateManagerEditBo;
import com.ruoyi.javamail.config.RabbitConfig;
import com.ruoyi.javamail.domain.MailTemplate;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.domain.UserMail;
import com.ruoyi.javamail.entity.*;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.*;
import com.ruoyi.javamail.util.FebsUtil;
import com.ruoyi.javamail.util.StringUtils;
import com.ruoyi.javamail.web.ApiJsonObject;
import com.ruoyi.javamail.web.ApiJsonProperty;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author gmm
 */
@Slf4j
@RestController
@RequestMapping("send-mail")
@Api(tags = "邮件发送")
public class SendMailController extends BaseController{

    private String message;

    private boolean flag = true;

    @Autowired
    private ISendMailService sendMailService;
    @Autowired
    private ISendMailItemsService sendMailItemsService;
    @Autowired
    private ITemplateManagerService templateManagerService;
    /*@Resource
    private RedisService redisService;*/
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ISendMailStmpService sendMailStmpService;
    @Autowired
    private ISendMailRedisService mailRedisService;
    @Autowired
    private ISendMailReceivedService mailReceivedService;
//    @Autowired
//    private IBaseInfoService baseInfoService;
    @Autowired
    private IUserMailService userMailService;

    /**
     * 新增一次邮件发送
     * @param mailmsg
     * @return
     * @throws FebsException
     */
    @PostMapping("/add")
    @ApiOperation(value="新增一次邮件发送", notes="")
    public ResponseResult addTemplate(@ApiJsonObject(name = "mailmsg", value = {
            @ApiJsonProperty(key = "zhu",description = "{\n" +
                    "\t\"templateid\": \"模板id\",\n" +
                    "\t\"stmp\": \"stmp服务器\",\n" +
                    "\t\"mailtopic\": \"发件主题\",\n" +
                    "\t\"mailname\": \"发件名称\",\n" +
                    "\t\"sendbox\": \"发件箱\",\n" +
                    "\t\"mailpassword\": \"密码\"\n" +
                    "}"),
            @ApiJsonProperty(key = "list",description = "[{\n" +
                    "\t\"id\": \"院士编码1\"\n" +
                    "}, {\n" +
                    "\t\"id\": \"院士编码2\"\n" +
                    "}]")
    })@RequestBody String mailmsg) throws FebsException {
        try {
            JSONObject json = JSONObject.parseObject(mailmsg);
            SendMail sendMail = JSONObject.toJavaObject(json.getJSONObject("zhu"),SendMail.class);
            sendMail.setAddtime(LocalDateTime.now());
            sendMail.setAddperson(FebsUtil.getCurrentUser().getUsername());
            sendMail.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            sendMail.setDeleteflag("1");//未删除

            //调用用户中心提供的接口，根据邮箱，获取该邮箱对应的密码和stmp
            UserMail userMail = userMailService.findUserMail(sendMail.getSendbox());

            if(userMail != null && !userMail.getPassword().equals("")){
                String password = userMail.getPassword();//发件人的邮箱密码不能直接保存到数据库中
                sendMail.setMailpassword(null);
                sendMail.setStmp(userMail.getSmtp());
                sendMail.setSuccessnumber(0);
                sendMail.setFailnumber(0);

                //记录当天使用的stmp服务器数量(条件：服务器名称 + 当天日期)
                LambdaQueryWrapper<SendMailStmp> stmpLambdaQueryWrapper = new LambdaQueryWrapper<>();
                stmpLambdaQueryWrapper.eq(SendMailStmp::getName,sendMail.getStmp()).eq(SendMailStmp::getStmptime,LocalDate.now());
                SendMailStmp sendMailStmp = sendMailStmpService.getOne(stmpLambdaQueryWrapper);

                //如果当天没有记录则新增一条使用数量是0的记录
                if(sendMailStmp == null || sendMailStmp.getId() == null){
                    sendMailStmp = new SendMailStmp();
                    sendMailStmp.setName(sendMail.getStmp());
                    sendMailStmp.setStmptime(LocalDate.now());
                    sendMailStmp.setUsenumber(0);
                    sendMailStmpService.save(sendMailStmp);
                }

                //list.size()为需要发送的邮件总数
                List<Map> list = (List<Map>)JSONObject.parseArray(json.getString("list"), Map.class);
                if(list != null && list.size() > 0){
                    sendMail.setSendnumber(list.size());//本次发件量
                    sendMailService.saveSendMail(sendMail);
                    SendMailReceived mailReceived = new SendMailReceived();
                    mailReceived.setMailtopic(sendMail.getMailtopic());
                    mailReceived.setMailname(sendMail.getMailname());
                    mailReceived.setSendbox(sendMail.getSendbox());
                    mailReceived.setSendnumber(sendMail.getSendnumber());
                    mailReceived.setStmp(sendMail.getStmp());
                    mailReceived.setAddperson(sendMail.getAddperson());
                    mailReceived.setAddpersonid(sendMail.getAddpersonid());
                    mailReceived.setAddtime(LocalDateTime.now());

                    mailReceivedService.saveMailReceived(mailReceived);


                    SendMailRedis sendMailRedis = new SendMailRedis();
                    sendMailRedis.setFid(sendMail.getId());
                    sendMailRedis.setEmail(sendMail.getSendbox());
                    //sendMailRedis.setPassword(password);//不能保存用户的邮箱密码
                    sendMailRedis.setSendtype(sendMail.getStmp());
                    sendMailRedis.setDeleteflag("1");//表示未删除
                    sendMailRedis.setFlag("1");//表示有效
                    sendMailRedis.setAddperson(FebsUtil.getCurrentUser().getUsername());
                    sendMailRedis.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
                    sendMailRedis.setAddtime(LocalDateTime.now());
                    mailRedisService.save(sendMailRedis);

                    //此时需要进行模板使用次数的更新
                    TemplateManager template = templateManagerService.getById(sendMail.getTemplateid());
                    if(template != null){
                        TemplateManagerEditBo templateManagerEditBo = new TemplateManagerEditBo();
                        templateManagerEditBo.setId(sendMail.getTemplateid());
                        templateManagerEditBo.setUsenumber(list.size()+template.getUsenumber());
                        templateManagerEditBo.setEditpersonid(FebsUtil.getCurrentUser().getUserId());
                        templateManagerEditBo.setEditperson(FebsUtil.getCurrentUser().getUsername());
                        templateManagerEditBo.setEdittime(LocalDateTime.now());
                        templateManagerService.updateTById(templateManagerEditBo);
                        RedisUtils redisService = SpringContextUtil.getBean(RedisUtils.class);
                        redisService.set(sendMail.getAddpersonid()+sendMail.getAddperson()+sendMail.getId(),String.valueOf(list.size()),7200000L);//有效时间2小时
                        MailTemplate mailTemplate = new MailTemplate();
                        mailTemplate.setPassword(password);//发件箱密码
                        mailTemplate.setAccount(sendMail.getSendbox());//发件箱
                        mailTemplate.setSubject(sendMail.getMailtopic());
                        mailTemplate.setSendType(sendMail.getStmp());

                        for(Map<String,Object> map : list){
                            //需要取出院士的编码，进行收件箱地址和院士信息的查询，但是落库时需要在消费后
                            String acadeId = String.valueOf(map.get("id"));//院士编码

                            //现在自己开发先写死
                            String msgId = UUID.randomUUID().toString();
                            mailTemplate.setMsgId(msgId);

                            String zhuemail = "88888@qq.com";
                            String fuemail  = "99999@qq.com";
                            //根据院士编码查询院士的邮箱等信息
                            /*ClientBaseInfo clientBaseInfo = baseInfoService.getBaseInfoById(1);
                            if(clientBaseInfo != null){
                                List<Email> emailList = clientBaseInfo.getEmailList();
                                if(emailList != null && emailList.size() > 0){
                                    for(Email email : emailList){
                                        if(email.getIsMainEmail()){//主邮箱
                                            //有可能没有主邮箱只有副邮箱
                                            zhuemail = email.getEmail();
                                        }else if(fuemail != null && !fuemail.equals("")){
                                            fuemail = email.getEmail();
                                        }
                                    }*/
                                    if(StringUtils.isNotBlank(zhuemail) || StringUtils.isNotBlank(fuemail)){//有邮箱才进行发送
                                        SendMailItems mailItems = new SendMailItems();
                                        if(zhuemail != null && !zhuemail.equals("")){
                                            mailTemplate.setTo(zhuemail);
                                            mailItems.setEmail(zhuemail);
                                            mailItems.setShowemail(zhuemail);
                                            mailItems.setNextemail(fuemail);
                                            mailItems.setType("1");//默认是主邮箱
                                            map.put("flag","zhu");//标志位，zhu表示主邮箱发送，next表示副邮箱发送
                                        }else{//主邮箱无值，直接发送给副邮箱
                                            mailTemplate.setTo(fuemail);
                                            mailItems.setEmail(fuemail);
                                            mailItems.setShowemail(fuemail);
                                            mailItems.setNextemail("");
                                            mailItems.setType("2");//默认是主邮箱
                                            map.put("flag","next");//标志位，zhu表示主邮箱发送，next表示副邮箱发送
                                        }
                                        //mailTemplate.setTo("9876543@miao.com");//收件箱，主邮箱如果发送不成功需要发送次邮箱
                                        mailTemplate.setToName("暂无数据");//收件人
                                        //邮件发送子表

                                        mailItems.setFid(sendMail.getId());
                                        //mailItems.setAcadencode(String.valueOf(clientBaseInfo.getAcadId()));//院士编码
                                        mailItems.setAcadencode("1123999");//院士编码

                                        mailItems.setFid(sendMail.getId());
                                        mailItems.setLeader(FebsUtil.getCurrentUser().getUsername());
                                        mailItems.setLeaderid(FebsUtil.getCurrentUser().getUserId());
                                        mailItems.setName("院士姓名");//院士姓名
                                        //mailItems.setNationality(clientBaseInfo.getNativePlace());
                                        mailItems.setNationality("中华人民共和国");

                                        //mailItems.setSign(String.valueOf(clientBaseInfo.getSignType()));//签约情况 1-全职，2-刚性，3-柔性，4-注册，5-其他
                                        mailItems.setSign(String.valueOf(1));//签约情况 1-全职，2-刚性，3-柔性，4-注册，5-其他
                                        mailItems.setOrganization("授衔机构-中国");
                                        //mailItems.setVisit(String.valueOf(clientBaseInfo.getContactStatus()));//到访情况1-已通讯，2-已到访，3-已签约，4-未通讯
                                        mailItems.setVisit(String.valueOf(2));//到访情况1-已通讯，2-已到访，3-已签约，4-未通讯
                                        mailItems.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
                                        mailItems.setAddperson(FebsUtil.getCurrentUser().getUsername());
                                        mailItems.setAddtime(LocalDateTime.now());
                                        mailItems.setSendemail(sendMail.getSendbox());
                                        //发送的正文内容采用的是自定义变量（已统一固定了几个），在发送的时候需要进行内容的替换
                                        //每次都是根据先查出来的信息进行替换的
                                        String mainbody = template.getMainbody();
                                        if(mainbody != null && !mainbody.equals("")){
                                            mainbody = mainbody.replace("${name}","院士~~");
                                            mainbody = mainbody.replace("${leader}"," 小同学 ");
                                            mainbody = mainbody.replace("${time}","2020-03-15");
                                            mainbody = mainbody.replace("${address}","【青岛国际院士港】- 四楼 - 3306");
                                        }
                                        mailTemplate.setContent(mainbody);
                                        map.put("mailTemplate",mailTemplate);
                                        map.put("mailItems",mailItems);
                                        map.put("snumber","first");//表示没有进行二次授权时的发送

                                        CorrelationData correlationData = new CorrelationData(msgId);
                                        rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, JSON.toJSONString(map), correlationData);// 发送消息
                                    }
                                }
                            //}
                        }
                    }
                }


                //生成一个个消息并扔到消息队列中

                //此时有一个模板使用次数的更新

                //Redis总数


                message = "发送成功";
            //}
        } catch (Exception e) {
            flag = false;
            message = "邮件发送失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,null);
    }

    /**
     * 批量邮件发送列表
     * @param request
     * @param sendMail
     * @return
     * @throws FebsException
     */
    @PostMapping("/list")
    @ApiOperation(value="批量邮件发送列表", notes="请求参数：输入邮件的基础信息")
    public ResponseResult templateList(QueryRequest request, @RequestBody SendMail sendMail) throws FebsException{
        Map<String,Object> map = new HashMap<>();
        try{
            sendMail.setAddpersonid(FebsUtil.getCurrentUser().getUserId());//虽然设置值了，但是在数据过滤v中没有使用该条件
            map.put("list",getDataTable(sendMailService.findSendMail(request,sendMail)));
            message = "获取成功";
        }catch(Exception e){
            flag = false;
            message = "获取列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
    }

    /**
     * 根据邮件主列表获取邮件发送详情列表(所有数据，不分页)==》此接口不用
     * @param id
     * @return
     * @throws FebsException
     */
    @GetMapping("/infoByFidaaa")
    //@RequiresPermissions("")
    public ResponseResult infoByFidaaa(@NotBlank(message = "{required}") String id) throws FebsException{
        Map<String,Object> map = new HashMap<>();
        try{
            if(id != null && !id.equals("")){
                LambdaQueryWrapper<SendMailItems> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(SendMailItems::getFid, Long.parseLong(id)).eq(SendMailItems::getAddpersonid,FebsUtil.getCurrentUser().getUserId());
                map.put("list",sendMailItemsService.list(queryWrapper));
                map.put("fid",id);
                message = "获取成功";
            }else{
                flag = false;
                message = "请选择将要获取的数据记录";
            }
        }catch(Exception e){
            flag = false;
            message = "根据邮件主列表获取邮件发送详情列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
    }

    /**
     * 导出邮件发送列表
     * @param request
     * @param sendMail
     * @param response
     * @throws FebsException
     */
    @PostMapping("/export")
    @ApiOperation(value="导出邮件发送列表", notes="请求参数：各查询条件")
    public void export(QueryRequest request, @RequestBody SendMail sendMail, HttpServletResponse response) throws FebsException {
        try {
            request.setPageNum(1);
            request.setPageSize(Integer.MAX_VALUE);
            sendMail.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            List<SendMail> list = sendMailService.findSendMail(request,sendMail).getRecords();
            ExcelKit.$Export(SendMail.class, response).downXlsx(list, false);
            message = "导出成功";
        } catch (Exception e) {
            flag = false;
            message = "导出Excel邮件发送列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        //return new ResponseResult(flag,200,message,null);
    }


}
