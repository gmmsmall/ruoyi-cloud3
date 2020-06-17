package com.ruoyi.acad.utils;

import com.ruoyi.common.baiduapi.TransApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 百度翻译util
 * @Author guomiaomiao
 * @Date 2020/6/16 15:29
 * @Version 1.0
 */
@Configuration
public class BaiduTranslateUtil {

    @Value("${transAppId}")
    private String APP_ID;
    @Value("${transSecretKey}")
    private String SECURITY_KEY;

    public String getTranslateString(String query){
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String str = api.getTransResult(query, "auto", "zh");    //中文翻译英文
        System.out.println("输出结果"+str);    //输出结果，即json字段
        if(str == null || str.equals("") || str.contains("错误")){
            return "";
        }else{
            JsonObject jsonObj = (JsonObject)new JsonParser().parse(str);    //解析json字段
            String res = jsonObj.get("trans_result").toString();    //获取json字段中的 result字段，因为result字段本身即是一个json数组字段，所以要进一步解析
            JsonArray js = new JsonParser().parse(res).getAsJsonArray();    //解析json数组字段
            jsonObj = (JsonObject)js.get(0);    //result数组中只有一个元素，所以直接取第一个元素
            return jsonObj.get("dst").getAsString();    //得到dst字段，即译文，并输出
        }
    }

}
