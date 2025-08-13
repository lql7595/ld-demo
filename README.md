## LD-Demo 一键部署指南（顶层）

## -1.写在前面的前面：
    理论启动步骤：
        a. 添加GIT_CLIENT_ID 和 GIT_SECRET 环境变量（回调地址请填写：http://你的vue配置host:端口号/callback）。
        b. 构建后端项目： mvn -f Cloud-Demo/pom.xml clean package -DskipTests
        c. docker编排： docker compose up -d --build
## 0.写在前面：
    0.回答面试老师问题：
        1.实现一个docker-compose.yml： 鉴于您要执行mvn clean package 。我就没有将构建编排到docker-compose中。但是我提供了在同一个目录层级构建项目的指令。
          mvn -f Cloud-Demo/pom.xml clean package -DskipTests
          docker compose up -d --build
        2.实现Uaa用户认证（数据库、GITHUB oauth、 LDAP登录）
        3.使用mysql作为关系型数据库（制作了自己的mysql镜像，上传到docker-hub上，可直接拉取下载，其中有demo需要的用户信息）。
        4.使用oxiasia的 openldap镜像搭建ldap服务。
        5.同时使用了 Getway 和 nginx。Gateway端口号：7573， nginx用来代理 \demo 转发给网关服务。
        6.Configure 和 Discovery使用了 Nacos（未持久化，后文给出配置路径可验证配置中心功能）。
        7.微服务架构：User服务（端口7571）负责用户认证和权限管理，Product服务（端口7570）负责产品管理，两服务通过OpenFeign进行通信。
        8.如面试老师在本地无法正常启动，可以先访问我的服务器来验证功能。www.lizable.cn
        9.感谢老师，希望得到面试老师的指正。谢谢！
        10.curl清单和结果见文末的第11点内容。
    1.项目已部署在 www.lizable.cn 欢迎访问。
    2.项目在一键部署前，需要 手动maven构建！！！ 详见5. 启动步骤
    3.ldap数据需要初始化，一键部署时，会自动加载ldap-init下的文件。
    4.使用Git OAuth 验证时，务必配置对应变量：GIT_CLIENT_ID 和 GIT_SECRET(注意保护secret，建议配成环境变量) 。详见：Cloud-Demo\README.md。
    5.nacos没有做持久化功能，如需验证nacose 是否生效，请在分组demo_p：、文件：p_service.yml、参数：cus.product.timeout 设置你期望的token生效时间来检验。
    6.服务注册使用了nacos，可以访问nacos的控制台观察服务是否注册成功。
    7.后端项目对其他内容有依赖关系。如变更过docker-compose.yml中的内容，请确定各个参数没有问题。
    8.jdk 是17版本。
    9.后续文本仅检视，如有疑问可以联系作者lizable_0814@163.com。

### 1. 项目概述
- 前后端分离的演示项目（后端 Spring Boot 3 + 前端 Vue 3），通过 Docker Compose 一键拉起完整环境。
- 集成 Nacos 配置中心、Redis、MySQL、LDAP、前端 Nginx 反向代理。
- 支持本地用户登录、LDAP 登录、GitHub OAuth 登录；产品的增删改查与基于角色的权限控制。

### 2. 目录结构（顶层）
```
ld-demo/
├── Cloud-Demo/           # 后端工程与数据库脚本
│   ├── Product/          # 产品服务（Spring Boot）
│   ├── User/             # 用户服务（Spring Boot）
│   ├── Gateway/          # 网关服务（Spring Boot）
│   └── DataBase/         # 初始化 SQL（记录）
├── ldap-init/            # LDAP 初始化数据（LDIF）（实际初始化用）
├── vue/                  # 前端应用（Vue 3 + Nginx）
└── docker-compose.yml    # 一键编排入口
```

### 3. 服务清单与端口
- ld-mysql: 自定义镜像 ld-mysql-data（默认 3306:3306）
- ld-redis: bitnami/redis（默认 6379:6379，密码 123456）
- ld-ldap: osixia/openldap（389/636，基于 `ldap-init/` 自动导入初始数据）
- ld-nacos: nacos/nacos-server:v2.4.3（8848/9848，单机模式）
- ld-product: 产品服务（7570，Spring Boot 3，JDK 17）
- ld-user: 用户服务（7571，Spring Boot 3，JDK 17）
- ld-gateway: 网关服务（7573:7573，Spring Boot 3，JDK 17）
- ld-vue: 前端服务（80:80，Nginx，反代后端 `/demo`）

访问入口：
- 前端站点: http://localhost
- 网关服务接口: http://localhost:7573/
- Nacos 控制台: http://localhost:8848/nacos

### 4. 前置要求
- Docker 20+ 与 Docker Compose（或 Docker Desktop）
- 构建镜像时可联网拉取依赖镜像
- 后端需要预先打包可执行 JAR（见下方“启动步骤”）

### 5. 启动步骤
1) 打包后端 JAR
```bash
# 在项目根目录执行
mvn -f Cloud-Demo/pom.xml clean package -DskipTests
```

2) 配置环境变量（GitHub OAuth 可选）
```
GIT_CLIENT_ID=你的GitHub客户端ID
GIT_SECRET=你的GitHub客户端密钥
```

3) 一键启动
```bash
docker compose up -d --build
```

4) 验证服务
- 前端: http://localhost
- Nacos: http://localhost:8848/nacos

### 6. 环境变量与配置对照
- 产品服务容器（ld-product）主要变量（已在编排中设置）：
  - ACTIVE_PROFILE=prod
  - NACOS_URL=ld-nacos:8848
  - MYSQL_URL=ld-mysql:3306
  - MYSQL_NAME=demo_pd
  - MYSQL_USER=root
  - MYSQL_PASSWORD=123456
  - REDIS_HOST=ld-redis
  - REDIS_PASSWORD=123456
- 用户服务容器（ld-user）主要变量（已在编排中设置）：
  - ACTIVE_PROFILE=prod
  - NACOS_URL=ld-nacos:8848
  - MYSQL_URL=ld-mysql:3306
  - MYSQL_NAME=demo_pd
  - MYSQL_USER=root
  - MYSQL_PASSWORD=123456
  - REDIS_HOST=ld-redis
  - REDIS_PASSWORD=123456
  - LDAP_HOST=ld-ldap
  - LDAP_BASE=dc=liz,dc=cn
  - LDAP_USER=cn=admin,dc=liz,dc=cn
  - LDAP_PASSWORD=123456
  - GIT_SECRET=从宿主 `.env` 注入
  - GIT_CLIENT_ID_=从宿主 `.env` 注入

- 前端容器（ld-vue）
  - 构建阶段会将 `./.env.production` 复制为 `.env`，默认将 API 指向 `/demo`，由 Nginx 反向代理到后端。

- Nginx（前端容器内）
  - `nginx.conf` 中将 `/demo` 代理到 `ld-gateway:7573`。

### 7. 初始化数据与账号
- MySQL（演示库 demo_pd）
  - 已准备 `Cloud-Demo/DataBase/dev_1.0.0/product/data/mysql_init/init.sql`
  - 演示用户：
    - 本地账号：`user_1` / `user_1`（USER 角色）
    - 本地账号：`editor_1` / `editor_1`（EDITOR 角色）
    - 本地账号：`adm_1` / `adm_1`（ADMIN 角色）
- LDAP（通过 `ldap-init/init.ldif` 导入）
  - 用户：`ldap_user_1` / `ldap_user_1`（USER）
  - 用户：`ldap_editor_1` / `ldap_editor_1`（EDITOR）
  - 用户：`ldap_adm_1` / `ldap_adm_1`（ADMIN）
  - 管理员 DN：`cn=admin,dc=liz,dc=cn`（密码 123456）

### 8. 常用命令
```bash
# 启动/关闭/重启
docker compose up -d --build
docker compose down

# 查看日志
docker compose logs -f ld-vue

# 重新构建（变更 Dockerfile 或前端/后端代码后）
docker compose build --no-cache

# 进入容器
docker exec -it ld-vue sh
```

### 9. 典型问题与注意事项
- 后端 JAR 未打包：请先执行 Maven 打包。
- GitHub OAuth：需在根目录 `.env` 配置 `GIT_SECRET`；后端还需在 Nacos 或配置中心设置 `githubClientId` 对应值。
- Nacos 准备时间：已配置健康检查，首次启动可能需要数十秒，请耐心等待后端依赖就绪。
- 安全性：默认密码仅用于演示，生产请务必修改 Redis/MySQL/LDAP 密码与外网暴露策略（或移除端口映射）。

### 10. 版本与依赖提示
- 后端运行时基于 **OpenJDK 17**（镜像 `openjdk:17-jdk-slim`）
- 前端构建阶段基于 **Node 22 (alpine)**，运行时基于 **Nginx 1.26**
- Nacos v2.4.3，Redis（bitnami），OpenLDAP（osixia）


## 11. Controller 层接口的 CURL 测试用例

### 测试账号信息

#### 本地用户账号
- **USER角色**: `user_1` / `user_1`
- **EDITOR角色**: `editor_1` / `editor_1`
- **ADMIN角色**: `adm_1` / `adm_1`

#### LDAP用户账号
- **USER角色**: `ldap_user_1` / `ldap_user_1`
- **EDITOR角色**: `ldap_editor_1` / `ldap_editor_1`
- **ADMIN角色**: `ldap_adm_1` / `ldap_adm_1`

### 权限说明

#### 产品管理权限
- **USER角色**: 只能查询产品列表
- **EDITOR角色**: 可以查询、添加、更新、删除产品
- **ADMIN角色**: 拥有所有权限

#### 使用步骤
1. 先使用登录接口获取token
2. 将返回的token替换到后续请求的`YOUR_TOKEN_HERE`位置
3. 根据用户角色测试不同的权限功能

### 示例完整流程

```bash
# 1. 登录获取token
TOKEN=$(curl -s -X POST http://localhost:7573/demo/user/sys/login \
  -H "Content-Type: application/json" \
  -d '{
    "userLogin": "editor_1",
    "pwd": "editor_1",
    "loginType": 1
  }' | jq -r '.data')

echo "获取到的token: $TOKEN"

# 2. 查询产品列表
curl -X POST http://localhost:7573/demo/product/bus/query/infolist \
  -H "Content-Type: application/json" \
  -d "{
    \"userLogin\": \"editor_1\",
    \"token\": \"$TOKEN\",
    \"loginType\": 1,
    \"cur\": 1,
    \"size\": 10
  }"

# 3. 添加产品
curl -X POST http://localhost:7573/demo/product/bus/add/info \
  -H "Content-Type: application/json" \
  -d "{
    \"userLogin\": \"editor_1\",
    \"token\": \"$TOKEN\",
    \"loginType\": 1,
    \"productName\": \"新测试产品\"
  }"
```

### 注意事项

1. **端口说明**: 所有请求都通过网关端口7573访问
2. **路径说明**: 网关会自动路由到对应的微服务
3. **Token格式**: 登录成功后返回的token需要包含在后续请求中
4. **权限验证**: 不同角色的用户只能访问对应的功能
5. **错误处理**: 如果token无效或权限不足，会返回相应的错误信息

#### 本地用户登录
```bash
curl -X POST http://localhost:7573/demo/user/sys/login \
  -H "Content-Type: application/json" \
  -d '{
    "userLogin": "user_1",
    "pwd": "user_1",
    "loginType": 1
  }'
```

#### LDAP 登录
```bash
curl -X POST http://localhost:7573/demo/user/sys/login/ldap \
  -H "Content-Type: application/json" \
  -d '{
    "userLogin": "ldap_user_1",
    "pwd": "ldap_user_1",
    "loginType": 3
  }'
```


#### 查询用户信息
```bash
curl -X POST http://localhost:7573/demo/user/sys/query/userinfo \
  -H "Content-Type: application/json" \
  -d '{
    "userLogin": "user_1",
    "token": "YOUR_TOKEN_HERE",
    "loginType": 1
  }'
```

#### 用户登出
```bash
curl -X POST http://localhost:7573/demo/user/sys/logout \
  -H "Content-Type: application/json" \
  -d '{
    "userLogin": "user_1",
    "token": "YOUR_TOKEN_HERE",
    "loginType": 1
  }'
```

### 产品管理接口

#### 查询产品列表
```bash
curl -X POST http://localhost:7573/demo/product/bus/query/infolist \
  -H "Content-Type: application/json" \
  -d '{
    "userLogin": "user_1",
    "token": "YOUR_TOKEN_HERE",
    "loginType": 1,
    "cur": 1,
    "size": 10,
    "keyFuzzy": "",
    "keyId": 0
  }'
```

#### 添加产品信息
```bash
curl -X POST http://localhost:7573/demo/product/bus/add/info \
  -H "Content-Type: application/json" \
  -d '{
    "userLogin": "editor_1",
    "token": "YOUR_TOKEN_HERE",
    "loginType": 1,
    "productName": "测试产品"
  }'
```

#### 更新产品信息
```bash
curl -X POST http://localhost:7573/demo/product/bus/update/info \
  -H "Content-Type: application/json" \
  -d '{
    "userLogin": "editor_1",
    "token": "YOUR_TOKEN_HERE",
    "loginType": 1,
    "id": 1,
    "newPrdName": "更新后的产品名"
  }'
```

#### 删除产品信息
```bash
curl -X POST http://localhost:7573/demo/product/bus/del/info \
  -H "Content-Type: application/json" \
  -d '{
    "userLogin": "editor_1",
    "token": "YOUR_TOKEN_HERE",
    "loginType": 1,
    "id": 1
  }'
```