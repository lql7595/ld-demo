# Login-demo

## 项目简介

本项目为一个基于 Spring Boot 的用户认证与管理服务，支持：
- 普通用户名密码登录
- LDAP 认证登录
- GitHub OAuth 登录
- 用户角色管理（支持 LDAP 角色组）
- MySQL 持久化
- Redis 缓存
- 支持 RESTful API

## 目录结构

```
Cloud-Demo/
├── Product/                  # 主服务模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/liz/
│   │   │   │   ├── controller/         # 控制器
│   │   │   │   ├── service/            # 业务逻辑
│   │   │   │   ├── bean/               # 实体、VO、请求响应
│   │   │   │   ├── config/             # 配置类
│   │   │   │   ├── constant/           # 常量
│   │   │   │   ├── mapper/             # MyBatis Mapper
│   │   │   │   └── ...                 # 其他
│   │   │   └── resources/
│   │   │       ├── application.yml     # 配置文件
│   │   │       └── mapper/             # MyBatis XML
│   ├── pom.xml
├── DataBase/
│   └── dev_1.0.0/
│       └── product/
│           └── data/
│               ├── ldap_init/
│               │   └── init.ldif       # LDAP 初始化数据
│               └── mysql_init/
│                   └── init.sql        # MySQL 初始化数据
├── README.md
└── pom.xml
```

## 快速开始

### 1. 环境准备

- JDK 17+
- Maven 3.6+
- MySQL 5.7/8.0
- Redis
- LDAP 服务（如 OpenLDAP）
- GitHub OAuth 应用（用于第三方登录）

### 2. 数据库和 LDAP 初始化

- 初始化 MySQL 数据库，执行 `DataBase/dev_1.0.0/product/data/mysql_init/init.sql`
- 初始化 LDAP 目录，导入 `DataBase/dev_1.0.0/product/data/ldap_init/init.ldif`

### 3. 配置文件

编辑 `Product/src/main/resources/application.yml`，根据实际环境修改：

- MySQL、Redis、LDAP 连接信息
- GitHub OAuth 配置（client_id, client_secret）

### 4. 依赖安装与构建

```bash
cd Product
mvn clean package
```

### 5. 启动服务

```bash
java -jar target/login-demo-service.jar
```

服务默认端口：`8000`，上下文路径：`/demo`

### 6. 主要接口

- `/sys/login` 用户名密码登录
- `/sys/login/ldap` LDAP 登录
- `/sys/login/github` 获取 GitHub OAuth 登录地址
- `/sys/oauth/callback` GitHub OAuth 回调
- `/sys/query/userinfo` 查询用户信息
- `/sys/logout` 注销

### 7. 环境变量（可选）

如需安全地传递 GitHub 密钥等敏感信息，可设置环境变量：

- `CLIENT_OAUTH_SECRET` 或 `GITHUB_CLIENT_SECRET`

Windows 设置方法：
```cmd
setx GITHUB_CLIENT_SECRET "your_secret"
```
Linux/Mac:
```bash
export GITHUB_CLIENT_SECRET="your_secret"
```

## 依赖技术

- Spring Boot
- Spring Data JPA / MyBatis-Plus
- Spring Data Redis
- Spring Data LDAP
- Spring Web
- Lombok
- Hutool
- MySQL
- Redis
- LDAP (OpenLDAP)
- GitHub OAuth

## 常见问题

- **LDAP 登录报错 error code 32**：请检查 LDAP 配置、用户 DN 拼接、LDAP 服务是否正常。
- **环境变量在 Java 中获取不到**：请确保变量在启动 Java 进程前已设置，IDE 需在运行配置中设置环境变量。

## 联系方式

如有问题请联系项目维护者。 
