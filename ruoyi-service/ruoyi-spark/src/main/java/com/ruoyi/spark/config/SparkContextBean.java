package com.ruoyi.spark.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkContextBean {

    private String sparkHome = ".";

    private String appName = "acadWordCount";

    private String master = "spark://192.168.8.165:7077";

    @Bean
    @ConditionalOnMissingBean(SparkConf.class)
    public SparkConf sparkConf() throws Exception {
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
        return conf;
    }

    @Bean
    @ConditionalOnMissingBean(JavaSparkContext.class)
    public JavaSparkContext javaSparkContext() throws Exception {
        return new JavaSparkContext(sparkConf());
    }

    public String getSparkHome() {
        return sparkHome;
    }

    public void setSparkHome(String sparkHome) {
        this.sparkHome = sparkHome;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }
}