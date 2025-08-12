package com.liz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAspectJAutoProxy
public class DUserApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DUserApplication.class, args);
    }
}
