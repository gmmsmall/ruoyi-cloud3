package com.ruoyi.common.redis.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data //使用了lombok的标签 如果未引用lombok需写getter 和 setter方法
public class RedisClusterConfigProperties {
    private List<String> nodes;
    private Integer maxAttempts;
    private Integer connectionTimeout;
    private Integer soTimeout;
    private String password;
}