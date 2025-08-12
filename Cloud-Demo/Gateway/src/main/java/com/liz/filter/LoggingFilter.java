package com.liz.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日志过滤器
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String host = request.getURI().getHost();
        String path = request.getURI().getPath();
        String method = request.getMethod().name();
        String remoteAddress = request.getRemoteAddress() != null ? 
                request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        
        long startTime = System.currentTimeMillis();
        String startTimeStr = LocalDateTime.now().format(FORMATTER);
        
        log.info("请求开始 - 时间: {}, 方法: {}, 路径: {}, 客户端IP: {}", 
                startTimeStr, method, path, remoteAddress);
        
        return chain.filter(exchange)
                .doFinally(signalType -> {
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    String endTimeStr = LocalDateTime.now().format(FORMATTER);
                    
                    log.info("请求结束 - 时间: {}, 方法: {}, 路径: {}, 耗时: {}ms, 状态: {}", 
                            endTimeStr, method, path, duration, 
                            exchange.getResponse().getStatusCode());
                });
    }
    
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
