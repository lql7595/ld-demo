# LD-Demo 后端服务

## 0.写在前面
    1.后端项目核心内容：支持3种登录方式登录系统，并且共用同一套角色权限来管控。
    2.目前没有加密相关内容，注意密码风险。
    3.如果需要使用github的 OAuth认证，务必提供 CLIENT 的 CILENT-ID 和 SECRET。对应变量：GIT_CLIENT_ID 和 GIT_SECRET(注意保护secret，建议配制成密文或者环境变量) 。
    4.项目仅提供了基础的maven构建，并未提供sh脚本以启动jar包外的yml文件来支持灵活配置。但项目支持了nacos作为配置中心的配置加载，nacos的加载组为nacos:p_service.yml?group=demo_p。
    5.关注各工程的Dockerfile 和application-prod.yml 。其中提供了外部组件相关的链接配置、用户、密码的变量名称。
    6.DataBase内的文件仅为项目作为demo时的数据用例。 其中DataBase中的所有文件。仅用作保存记录，ldap真正初始化加载的数据在父层目录的ldap-init。
    7.在使用github 的OAuth认证时，可能会出现连接超时的情况，暂未定位到具体原因。
    8.token验证规则：登录后会将用户名作为key ， 生成的UUID + ROLE_INFO 作为 value。存入redis。后续接口访问时仅凭借 user_id + token 即可鉴权。无序再次查找数据库。（考虑到目前项目不涉及变更用户角色的功能，所以使用相对简单的方法鉴权。后续如果增加其他功能，需要考虑功能对redis中存储的roleInfo是有有影响，再决定对于redis缓存的操作。）
    9.token的过期时间为cus.product.timeout: 600000(默认)。 可以通过nacos设置预期的时间（注意nacos的数据，本compose并未包含持久化方案）。
    10.Auth验证规则：在接口上方明确可以访问接口的角色，如果用户的角色或者角色关联表 （表结构详见：DataBase\dev_1.0.0\product\data\mysql_init）包含所需要的角色，即为认证成功。
    11.项目提供了退出接口，一旦用户退出，token将不会再生效。并且同一个用户只会有一个生效的token，后生成的token会替换先生成的token。
    12.部分类型报错会被全局异常处理器拦截，统一返回500。
    13.有疑问可以联系作者lizable_0814@163.com。

## 1. 项目功能

### 核心功能
- **用户认证系统**
  - 本地用户登录
  - LDAP 认证登录
  - GitHub OAuth 登录
  - Token 验证和权限控制
  - 用户角色管理（USER、EDITOR、ADMIN）

- **产品管理系统**
  - 基于角色的权限控制

### 权限体系
- **USER**: 基础用户，可查询产品信息
- **EDITOR**: 编辑者，可增删改产品信息
- **ADMIN**: 管理员，拥有所有权限

## 3. 项目技术栈

### 环境要求
- **JDK 17** - 项目基于 Java 17 开发，使用 Spring Boot 3.x

### 核心框架
- **Spring Boot 3.x** - 主框架
- **Spring Cloud Alibaba** - 微服务框架
  - Nacos Discovery - 服务发现
  - Nacos Config - 配置中心
  - Sentinel - 流量控制

## 4. 可能需要的配置文件

### 必需配置
- `application.yml` - 主配置
- `nacos:p_service.yml` - Nacos配置
- 数据库连接配置
- Redis连接配置
- LDAP连接配置

### 环境配置
- `application-dev.yml` - 开发环境
- `application-prod.yml` - 生产环境

## 5. 构建说明和指令

### 环境要求
- **JDK 17** - 必需，项目基于 Java 17 开发
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### JDK 版本说明
项目使用 **JDK 17**，这是 Spring Boot 3.x 的最低要求。请确保：
- 安装 JDK 17 或更高版本
- 设置 `JAVA_HOME` 环境变量指向 JDK 17 安装目录
- IDE 中配置 JDK 17 作为项目 SDK