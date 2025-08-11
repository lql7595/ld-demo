package com.liz.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cus.product")
@Data
public class MyConfig {

    private Long timeout;

    private String githubUserUrl;

    private String githubAuthUrl;

    private String githubClientId;

    private String githubAccTokenUrl;

    @Value("${GIT_SECRET}")
    private String githubClientSecret;

    public static void main(String[] args) {
        System.out.println(System.getenv("GIT_SECRET"));
        System.out.println(System.getenv("JAVA_HOME"));
        System.out.println(System.getenv("HADOOP_HOME"));
    }
}
