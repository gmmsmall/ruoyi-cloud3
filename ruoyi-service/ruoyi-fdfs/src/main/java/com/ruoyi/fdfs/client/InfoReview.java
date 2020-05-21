package com.ruoyi.fdfs.client;


import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.ruoyi.common.baiduapi.AuthService;
import com.ruoyi.common.baiduapi.Base64Util;
import com.ruoyi.common.baiduapi.HttpUtil;
import com.ruoyi.common.utils.SpringContextUtil;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.redis.util.RedisUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证识别
 */
public class InfoReview {

    private static final String idcardIdentificate = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
    private static final String businessIdentificate = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license";
    private static final String APP_ID = "17600381";
    private static final String API_KEY = "Scaf4tt9xUdSTi1s8xaaGRvD";
    private static final String SECRET_KEY = "TqUFqFoBn9v2bG7oIRb3ytnocBcV9OBE";

    public static AipContentCensor getClient() {
        return client;
    }

    private static final AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);

    public static Map<String, Object> idcard(MultipartFile file, String id_card_side){
        try {
            String imgStr = Base64Util.encode(file.getBytes());
            String params = "id_card_side=" + id_card_side + "&" + "detect_risk=true&" + URLEncoder.encode("image", "UTF-8") + "="
                    + URLEncoder.encode(imgStr, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            RedisUtils redisService = SpringContextUtil.getBean(RedisUtils.class);
            String accessToken;
            if (StringUtils.isEmpty(redisService.get(Constants.BAIDU_TOKEN_PREFIX))){
                accessToken = AuthService.getAuth();
                redisService.set(Constants.BAIDU_TOKEN_PREFIX,accessToken, 2591000L);
            }else {
                accessToken = redisService.get(Constants.BAIDU_TOKEN_PREFIX);
            }

            JSONObject o = JSONObject.parseObject(HttpUtil.post(idcardIdentificate, accessToken, params));
            Map<String,Object> map = new HashMap<>();
            map.put("risk_type",o.getString("risk_type"));
            map.put("image_status",o.getString("image_status"));
//            map.put("name",o.getJSONObject("words_result").getJSONObject("姓名").getString("words"));
            map.put("status",true);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String,Object> map = new HashMap<>();
            map.put("status",false);
            return map;
        }
    }

    public static JSONObject business(MultipartFile file){
        try {
            String imgStr = Base64Util.encode(file.getBytes());
            String params = URLEncoder.encode("image", "UTF-8") + "="
                    + URLEncoder.encode(imgStr, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            RedisUtils redisService = SpringContextUtil.getBean(RedisUtils.class);
            String accessToken;
            if (StringUtils.isEmpty(redisService.get(Constants.BAIDU_TOKEN_PREFIX))){
                accessToken = AuthService.getAuth();
                redisService.set(Constants.BAIDU_TOKEN_PREFIX,accessToken, 2591000L);
            }else {
                accessToken = redisService.get(Constants.BAIDU_TOKEN_PREFIX);
            }
            return JSONObject.parseObject(HttpUtil.post(businessIdentificate, accessToken, params));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
