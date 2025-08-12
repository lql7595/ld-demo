# Gateway 网关服务

## 概述

Gateway服务是基于Spring Cloud Gateway构建的API网关，提供统一的入口点、认证授权、路由转发、限流熔断等功能。

## 功能特性

- **统一入口**: 为所有微服务提供统一的API入口
- **认证授权**: 基于Token的JWT认证机制
- **路由转发**: 智能路由到不同的微服务
- **限流熔断**: 集成Sentinel进行流量控制和熔断
- **日志记录**: 详细的请求日志记录
- **健康检查**: 提供健康检查接口

## 技术栈

- Spring Boot 3.3.4
- Spring Cloud Gateway
- Spring Cloud Alibaba Nacos
- Spring Cloud Alibaba Sentinel
- Redis
- Hutool

## 路由配置

### 用户服务路由
- 路径: `/api/user/**`
- 目标服务: `user-service`
- 认证: 需要Token认证

### 产品服务路由
- 路径: `/api/product/**`
- 目标服务: `product-service`
- 认证: 需要Token认证

### 健康检查路由
- 路径: `/health/**`
- 认证: 无需认证

## 认证机制

### Token格式
```
Authorization: Bearer <token>
```

### 跳过认证的路径
- `/api/user/login` - 用户登录
- `/api/user/register` - 用户注册
- `/health/**` - 健康检查

### 用户信息传递
认证通过后，网关会在请求头中添加用户信息：
- `X-User-Id`: 用户ID
- `X-User-Name`: 用户名

## 配置说明

### 应用配置
```yaml
server:
  port: 8080

spring:
  application:
    name: gateway-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
    gateway:
      discovery:
        locator:
          enabled: true
      routes:
        # 路由配置...
```

### Redis配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 10000ms
```

## 启动方式

### 本地启动
```bash
mvn clean package
java -jar target/gateway-service.jar
```

### Docker启动
```bash
mvn clean package
docker build -t gateway-service .
docker run -p 8080:8080 gateway-service
```

## API接口

### 健康检查
```
GET /health
GET /health/info
```

### 响应格式
```json
{
  "code": "200",
  "message": "操作成功",
  "data": {
    "status": "UP",
    "service": "gateway-service",
    "timestamp": 1640995200000
  },
  "timestamp": 1640995200000
}
```

## 错误处理

### 401 Unauthorized
- Token缺失
- Token无效
- Token已过期

### 403 Forbidden
- 权限不足

### 500 Internal Server Error
- 网关服务异常

## 监控和日志

### 日志级别
- 开发环境: DEBUG
- 生产环境: INFO

### 关键日志
- 请求开始和结束日志
- 认证成功/失败日志
- 路由转发日志

## 注意事项

1. 确保Nacos服务已启动并配置正确
2. 确保Redis服务已启动并配置正确
3. 确保目标微服务已注册到Nacos
4. Token验证依赖Redis存储，请确保Redis连接正常
5. 生产环境建议配置HTTPS

## 扩展功能

### 自定义过滤器
可以通过继承`AbstractGatewayFilterFactory`来创建自定义过滤器。

### 限流配置
可以通过Sentinel Dashboard配置限流规则。

### 熔断配置
可以通过Sentinel Dashboard配置熔断规则。
