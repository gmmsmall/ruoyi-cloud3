package com.ruoyi.fabric.utils;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.fabric.config.NetworkConfig;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Relic
 * @desc yml工具类
 * @date 2020-02-27 17:19
 */
public class YamlUtils {

    private final static DumperOptions OPTIONS = new DumperOptions();

    private static File file;

    private static InputStream ymlInputSteam;

    private static Object CONFIG_MAP;

    private static Yaml yaml;

    static {
        //将默认读取的方式设置为块状读取
        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }

    /**
     * 使用其他方法之前必须调用一次 设置yml的输出文件,当没有设置输入流时可以不设置输入流,默认以此文件读入
     *
     * @param file 输出的文件
     */
    public static void setYmlFile(File file) throws FileNotFoundException {
        YamlUtils.file = file;
        if (ymlInputSteam == null) {
            setYmlInputSteam(new FileInputStream(file));
        }
    }


    /**
     * 使用其他方法之前必须调用一次 设置yml的输入流
     *
     * @param inputSteam 输入流
     */
    public static void setYmlInputSteam(InputStream inputSteam) {
        ymlInputSteam = inputSteam;
        yaml = new Yaml(OPTIONS);
        CONFIG_MAP = yaml.load(ymlInputSteam);
    }

    /**
     * 根据键获取值
     *
     * @param key 键
     * @return 查询到的值
     */
    @SuppressWarnings("unchecked")
    public static Object getByKey(String key) {
        if (ymlInputSteam == null) {
            return null;
        }
        String[] keys = key.split("\\.");
        Object configMap = CONFIG_MAP;
        for (String s : keys) {
            if (configMap instanceof Map) {
                configMap = ((Map<String, Object>) configMap).get(s);
            } else {
                break;
            }
        }
        return configMap == null ? "" : configMap;
    }

    public static void saveOrUpdateByKey(String key, Object value) throws IOException {
        KeyAndMap keyAndMap = new KeyAndMap(key).invoke();
        key = keyAndMap.getKey();
        Map<String, Object> map = keyAndMap.getMap();
        map.put(key, value);
        //将数据重新写回文件
        yaml.dump(CONFIG_MAP, new FileWriter(file));
    }

    public static void removeByKey(String key) throws Exception {
        KeyAndMap keyAndMap = new KeyAndMap(key).invoke();
        key = keyAndMap.getKey();
        Map<String, Object> map = keyAndMap.getMap();
        Map<String, Object> fatherMap = keyAndMap.getFatherMap();
        map.remove(key);
        if (map.size() == 0) {
            Set<Map.Entry<String, Object>> entries = fatherMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                if (entry.getValue() == map) {
                    fatherMap.remove(entry.getKey());
                }
            }
        }
        yaml.dump(CONFIG_MAP, new FileWriter(file));
    }

    private static class KeyAndMap {
        private String key;
        private Map<String, Object> map;
        private Map<String, Object> fatherMap;

        public KeyAndMap(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public Map<String, Object> getMap() {
            return map;
        }

        public Map<String, Object> getFatherMap() {
            return fatherMap;
        }

        @SuppressWarnings("unchecked")
        public KeyAndMap invoke() {
            if (file == null) {
                System.err.println("请设置文件路径");
            }
            if (null == CONFIG_MAP) {
                CONFIG_MAP = new LinkedHashMap<>();
            }
            String[] keys = key.split("\\.");
            key = keys[keys.length - 1];
            map = (Map<String, Object>) CONFIG_MAP;
            for (int i = 0; i < keys.length - 1; i++) {
                String s = keys[i];
                if (map.get(s) == null || !(map.get(s) instanceof Map)) {
                    map.put(s, new HashMap<>(4));
                }
                fatherMap = map;
                map = (Map<String, Object>) map.get(s);
            }
            return this;
        }
    }
    public static  void  setTLSToConfig(String username) throws IOException {
        //读取key
        File key = new File(NetworkConfig.DATA_PATH+"/resources/tlswallet/"+username+"/"+username+"-priv");//定义一个file对象，用来初始化FileReader
        FileReader reader_k = new FileReader(key);//定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader_k = new BufferedReader(reader_k);//new一个BufferedReader对象，将文件内容读取到缓存
        StringBuilder sb_k = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
        String k = "";
        while ((k =bReader_k.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
            sb_k.append(k + "\n");//将读取的字符串添加换行符后累加存放在缓存中
        }

        //读取cert
        File cert = new File(NetworkConfig.DATA_PATH+"/resources/tlswallet/"+username+"/"+username);//定义一个file对象，用来初始化FileReader
        FileReader reader_c = new FileReader(cert);//定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader_c = new BufferedReader(reader_c);//new一个BufferedReader对象，将文件内容读取到缓存
        StringBuilder sb_c = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
        String c = "";
        while ((c =bReader_c.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
            sb_c.append(c + "\n");//将读取的字符串添加换行符后累加存放在缓存中
        }
        bReader_c.close();
        bReader_k.close();
        String str_c = sb_c.toString();
        String str_k = sb_k.toString();
        JSONObject jsonObject1 =JSONObject.parseObject(str_c);
        JSONObject enrollment = jsonObject1.getJSONObject("enrollment");
        JSONObject identity =enrollment.getJSONObject("identity");

        //读取配置文件
        Path networkConfigFile = Paths.get(NetworkConfig.DATA_PATH+"/resources/connection-org1.yml");
        File yml = networkConfigFile.toFile();
        YamlUtils.setYmlFile(yml);

        //注入密钥和证书
        YamlUtils.saveOrUpdateByKey("client.tlsCerts.client.key.pem", str_k);
        YamlUtils.saveOrUpdateByKey("client.tlsCerts.client.cert.pem",identity.get("certificate").toString());
        YamlUtils.saveOrUpdateByKey("client.tlsCerts.client.cert.pem",identity.get("certificate").toString());
    }
}