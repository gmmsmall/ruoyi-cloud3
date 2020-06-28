package com.ruoyi.system.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SendSms {

    /**
     * @Author jxd
     * @Description 发送验证码
     * @Date 10:21 2019/7/8
     * @Param [code]
     * @return void
     **/
    public static void SendSms(String mobile, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI6ExP8XsL9h1u", "WI6AnBIeo6lfLoAPFBJ4XQK8ei3aqS");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("TemplateCode", "SMS_169990503");
        request.putQueryParameter("SignName", "青岛国际院士港");
        request.putQueryParameter("TemplateParam", code);
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author gmm
     * @Description 发送新邮件通知
     * @Date 10:21 2020/3/30
     * @Param [code]
     * @return void
     **/
    public static void SendSmsEmail(String mobile, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI6ExP8XsL9h1u", "WI6AnBIeo6lfLoAPFBJ4XQK8ei3aqS");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("TemplateCode", "SMS_186951366");
        request.putQueryParameter("SignName", "青岛国际院士港");
        request.putQueryParameter("TemplateParam", code);
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}