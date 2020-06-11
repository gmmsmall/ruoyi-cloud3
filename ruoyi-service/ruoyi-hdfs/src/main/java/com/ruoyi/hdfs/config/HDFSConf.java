package com.ruoyi.hdfs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：
 * @date ：Created in 2019/11/5 11:32
 * @description：
 * @modified By：
 * @version:
 */

@Configuration
public class HDFSConf {
    @Value("${hdfs.path}")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
