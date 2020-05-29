package com.ruoyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启动类
 * @Author guomiaomiao
 * @Date 2020/5/28 10:03
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.ruoyi")
@EnableScheduling
@EnableSwagger2
@MapperScan("com.ruoyi.javamail.dao")
public class RuoYiJavaMailApp {
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiJavaMailApp.class, args);
        System.out.println("\n\t\t\t\t\t\t====================== RuoYiJavaMailApp start success ======================================\n");
    }
}
