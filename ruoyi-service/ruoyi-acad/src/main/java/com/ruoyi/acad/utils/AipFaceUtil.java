package com.ruoyi.acad.utils;

import com.baidu.aip.face.AipFace;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 照片解析男女
 * @Author guomiaomiao
 * @Date 2020/6/15 16:41
 * @Version 1.0
 */
@Configuration
public class AipFaceUtil {

    /**
     * 解决注解取值问题
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    //设置APPID/AK/SK

    @Value("${appId}")
    private String APP_ID;

    @Value("${apiKey}")
    private String API_KEY;

    @Value("${secretKey}")
    private String SECRET_KEY;

    /**
     * 人脸识别
     * @return
     */
   public String getSexByImage(String imageUrl) {

       String sex = "";//存储识别后的性别，male男female女
       // 初始化一个AipFace
       AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

       // 传入可选参数调用接口
       HashMap<String, String> options = new HashMap<String, String>();
       options.put("face_field", "age");
       options.put("face_field", "gender");
       options.put("max_face_num", "1");//识别面积最大的一张人脸
       options.put("face_type", "LIVE");
       options.put("liveness_control", "LOW");

       // 调用接口
       String image = imageUrl;
       String imageType = "BASE64";
       String msg= getImageStr(image);

       try{
           // 人脸检测
           JSONObject res = client.detect(msg, imageType, options);
           System.out.println(res);
           JSONObject jsonObject = res.getJSONObject("result");
           if(jsonObject != null){
               JSONArray jsonArray = jsonObject.getJSONArray("face_list");
               if(jsonArray != null && jsonArray.length() > 0){
                   JSONObject json = jsonArray.getJSONObject(0);
                   sex = json.getJSONObject("gender").getString("type");
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }

       return sex;
   }

   public String getImageStr(String imgFile){
       //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
       InputStream inputStream = null;
       byte[] data = null;
       //读取图片字节数组
       try{
           URL url = new URL(imgFile);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           //默认是get请求   如果想使用post必须指明
           connection.setRequestMethod("GET");
           connection.setReadTimeout(5000);
           connection.setConnectTimeout(5000);
           inputStream = connection.getInputStream();
           data = readInputStream(inputStream);
           inputStream.read();
           inputStream.close();
       }catch (Exception e){
           e.printStackTrace();
       }
       return Base64.encodeBase64String(data);
   }

    private byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

}