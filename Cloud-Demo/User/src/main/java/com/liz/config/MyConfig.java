package com.liz.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cus.user")
@Data
public class MyConfig {

    private Long timeout;

    private String githubUserUrl;

    private String githubAuthUrl;

    private String githubClientId;

    private String githubAccTokenUrl;

    private String githubClientSecret;

}
