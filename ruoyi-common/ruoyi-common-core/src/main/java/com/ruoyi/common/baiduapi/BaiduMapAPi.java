package com.ruoyi.common.baiduapi;

import com.ruoyi.common.utils.http.HttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

public class BaiduMapAPi {

    //// TODO: 2020/3/18  百度api 账号更换
    //Baidu地图api密钥
    private static final String ak = "wVTwKUb9V2bp2MSV35Y33m4GbFWM3vME";

    // 调用百度地图API根据地址，获取坐标
    public static String getCoordinate(String address) throws JSONException {
        if (address != null && !"".equals(address)) {
            address = address.replaceAll("\\s*", "").replace("#", "栋");
            String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=" + ak;
            String json = loadJSON(url);
            if (json != null && !"".equals(json)) {
                JSONObject obj = null;
                obj = new JSONObject(json);
                if ("0".equals(obj.getString("status"))) {
                    double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng"); // 经度
                    double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat"); // 纬度
                    DecimalFormat df = new DecimalFormat("#.#####");
                    return df.format(lat) + "," + df.format(lng);
                }
            }
        }
        return null;
    }


    // 调用百度地图API根据坐标获取地址
    public static String getReverseCoordinate(String location) {
        String city = null;
        if (location != null) {
            String result = HttpUtils.sendGet("http://api.map.baidu.com/geocoder/v2/","ak=" + ak + "&output=json&pois=l&location="
                            + location);
            com.alibaba.fastjson.JSONObject jsonObjectAdds = com.alibaba.fastjson.JSONObject.parseObject(result);
            String province = jsonObjectAdds.getJSONObject("result").getJSONObject("addressComponent")
                    .getString("province");// 省
            city = jsonObjectAdds.getJSONObject("result").getJSONObject("addressComponent").getString("city");// 市

            System.out.println("province:" + province);
            System.out.println("city:" + city);
        }
        return city;
    }

    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }
}
