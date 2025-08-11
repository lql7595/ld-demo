# LD-Demo 后端服务

## 0.写在前面
    1.后端项目核心内容：支持3种登录方式登录系统，并且共用同一套角色权限来管控。
    2.目前没有加密相关内容，注意密码风险。
    3.如果需要使用github的 OAuth认证，务必提供 CLIENT 的 CILENT-ID 和 SECRET。对应变量：GIT_CLIENT_ID 和 GIT_SECRET(注意保护secret，建议配制成密文或者环境变量) 。
    4.项目仅提供了基础的maven构建，并未提供sh脚本以启动jar包外的yml文件来支持灵活配置。但项目支持了nacos作为配置中心的配置加载，nacos的加载组为nacos:p_service.yml?group=demo_p。
    5.关注Dockerfile 和application-prod.yml 。其中提供了外部组件相关的链接配置、用户、密码的变量名称。
    6.DataBase内的文件仅为项目作为demo时的数据用例。 其中DataBase中的所有文件。仅用作保存记录，ldap真正初始化加载的数据在父层目录的ldap-init。
    7.在使用github 的OAuth认证时，可能会出现连接超时的情况，暂未定位到具体原因。
    8.在使用数据库时可能会遇到 无法获取数据库连接的原因。暂未定位到具体原因。
    9.token验证规则：登录后会将用户名作为key ， 生成的UUID + ROLE_INFO 作为 value。存入redis。后续接口访问时仅凭借 user_id + token 即可鉴权。无序再次查找数据库。（考虑到目前项目不涉及变更用户角色的功能，所以使用相对简单的方法鉴权。后续如果增加其他功能，需要考虑功能对redis中存储的roleInfo是有有影响，再决定对于redis缓存的操作。）
    10.token的过期时间为cus.product.timeout: 600000(默认)。 可以通过nacos设置预期的时间（注意nacos的数据，本compose并未包含持久化方案）。
    11.Auth验证规则：在接口上方明确可以访问接口的角色，如果用户的角色或者角色关联表 （表结构详见：DataBase\dev_1.0.0\product\data\mysql_init）包含所需要的角色，即为认证成功。
    12.项目提供了退出接口，一旦用户退出，token将不会再生效。并且同一个用户只会有一个生效的token，后生成的token会替换先生成的token。
    14.部分类型报错会被全局异常处理器拦截，统一返回500。
    13.后续内容由cursor输出（除7.CURL测试用例外），本人目检无误，有疑问可以联系作者lizable_0814@163.com。

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

## 2. 项目结构

```
Cloud-Demo/Product/
├── src/main/java/com/liz/
│   ├── anno/           # 自定义注解
│   │   ├── AuthVerifyAnnotation.java    # 权限验证注解
│   │   └── TokenVerifyAnnotation.java   # Token验证注解
│   ├── aspect/         # 切面实现
│   │   ├── AuthVerifyAspect.java        # 权限验证切面
│   │   └── TokenVerifyAspect.java       # Token验证切面
│   ├── bean/           # 数据对象
│   │   ├── entity/     # 实体类
│   │   ├── request/    # 请求对象
│   │   ├── response/   # 响应对象
│   │   └── vo/         # 视图对象
│   ├── config/         # 配置类
│   ├── constant/       # 常量定义
│   ├── controller/     # 控制器层
│   ├── exception/      # 异常处理
│   ├── interceptor/    # 拦截器
│   ├── mapper/         # 数据访问层
│   ├── service/        # 业务逻辑层
│   └── DProductApplication.java  # 启动类
└── src/main/resources/
    ├── application.yml          # 主配置文件
    ├── application-dev.yml      # 开发环境配置
    ├── application-prod.yml     # 生产环境配置
    └── mapper/                 # MyBatis映射文件
```

## 3. 项目技术栈

### 环境要求
- **JDK 17** - 项目基于 Java 17 开发，使用 Spring Boot 3.x

### 核心框架
- **Spring Boot 3.x** - 主框架
- **Spring Cloud Alibaba** - 微服务框架
  - Nacos Discovery - 服务发现
  - Nacos Config - 配置中心
  - Sentinel - 流量控制

### 数据层
- **MyBatis Plus 3.5.8** - ORM框架
- **MySQL 8.3.0** - 主数据库
- **Redis** - 缓存和Token存储
- **LDAP** - 目录服务认证

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

## 5. Git 拉取推送说明和指令

### 初始拉取
```bash
git clone <repository-url>
cd ld-demo
```

### 日常操作
```bash
# 拉取最新代码
git pull origin main

# 查看状态
git status

# 添加修改
git add .

# 提交修改
git commit -m "feat: 添加新功能"

# 推送到远程
git push origin main
```

### 分支管理
```bash
# 创建新分支
git checkout -b feature/new-feature

# 切换分支
git checkout main

# 合并分支
git merge feature/new-feature
```

## 6. 构建说明和指令

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

### 构建步骤
```bash
# 进入项目目录
cd Cloud-Demo/Product

# 清理并编译
mvn clean compile

# 运行测试
mvn test

# 打包
mvn package

# 运行应用
java -jar target/login-demo-service.jar
```

### Docker 构建
```bash
# 构建镜像
docker build -t ld-demo-service .

# 运行容器
docker run -p 8000:8000 ld-demo-service
```

## 7. Controller 层接口的 CURL 测试用例

### 用户认证接口

#### 本地用户登录
```bash
curl -X POST http://localhost:8000/demo/sys/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### LDAP 登录
```bash
curl -X POST http://localhost:8000/demo/sys/login/ldap \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ldapuser",
    "password": "ldappass"
  }'
```

#### GitHub OAuth 登录
```bash
# 获取授权URL
curl -X GET "http://localhost:8000/demo/sys/login/github"

# OAuth回调
curl -X GET "http://localhost:8000/demo/sys/oauth/callback?code=YOUR_CODE"
```

#### 查询用户信息
```bash
curl -X POST http://localhost:8000/demo/sys/query/userinfo \
  -H "Content-Type: application/json" \
  -d '{
    "token": "YOUR_TOKEN"
  }'
```

#### 用户登出
```bash
curl -X POST http://localhost:8000/demo/sys/logout \
  -H "Content-Type: application/json" \
  -d '{
    "token": "YOUR_TOKEN"
  }'
```

### 产品管理接口

#### 查询产品列表
```bash
curl -X POST http://localhost:8000/demo/bus/query/infolist \
  -H "Content-Type: application/json" \
  -d '{
    "pageNum": 1,
    "pageSize": 10,
    "token": "YOUR_TOKEN"
  }'
```

#### 添加产品信息
```bash
curl -X POST http://localhost:8000/demo/bus/add/info \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "productName": "测试产品",
    "description": "产品描述",
    "token": "YOUR_TOKEN"
  }'
```

#### 更新产品信息
```bash
curl -X POST http://localhost:8000/demo/bus/update/info \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "id": 1,
    "productName": "更新后的产品名",
    "token": "YOUR_TOKEN"
  }'
```

#### 删除产品信息
```bash
curl -X POST http://localhost:8000/demo/bus/del/info \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "id": 1,
    "token": "YOUR_TOKEN"
  }'
```

## 8. 需要注意的地方


### 配置相关
- **Nacos 配置**: 项目依赖 Nacos 配置中心，需要确保 Nacos 服务可用
- **数据库初始化**: 需要先执行 `DataBase/dev_1.0.0/mysql_init/init.sql` 如果使用本项目的compose.yml 中配置的mysql则不需要初始化数据。（但是需要考虑将数据摘出。）
- **LDAP 配置**: 需要配置 LDAP 服务器连接信息 初始化数据在 ./../ldap-init/ 目录下。

### 部署相关
- **端口配置**: 默认端口 8000，可通过配置文件修改
- **上下文路径**: 默认为 `/demo`
- **日志配置**: 日志文件保存在 `logs/app.log`