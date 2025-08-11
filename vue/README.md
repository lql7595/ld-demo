# LD-Demo 前端项目

## 0.写在前面：
    1.项目自带了Dockerfile 和nginx.conf 方便本人编写的的dockerr-compose.yml运行。
    2.项目自带了.env.production。同样是为契合docker-compose.yml 编写的后端地址。如需要，需自行更改。
    3.项目的请求虽然没有在httpheader中填充token和user，但是在request的body中自动填写了 userLogin 和 token。
    3.后续内容由cursor输出，本人目检无误，有疑问可以联系作者lizable_0814@163.com。

## 1. 项目功能

### 核心功能
- **多方式登录系统**
  - 用户名密码登录
  - LDAP 认证登录
  - GitHub OAuth 登录
  - 自动 Token 管理

- **产品管理系统**
  - 产品信息展示
  - 产品增删改查操作
  - 权限控制（需要登录）

## 2. 项目结构
vue/
├── public/ # 静态资源
│ ├── favicon.ico # 网站图标
│ └── index.html # HTML 模板
├── src/ # 源代码
│ ├── api/ # API 接口
│ │ └── index.js # 接口定义和配置
│ ├── assets/ # 资源文件
│ │ └── logo.png # 项目 Logo
│ ├── components/ # 公共组件
│ │ ├── AppLayout.vue # 应用布局组件
│ │ └── AppNavbar.vue # 导航栏组件
│ ├── router/ # 路由配置
│ │ └── index.js # 路由定义和守卫
│ ├── utils/ # 工具函数
│ ├── views/ # 页面组件
│ │ ├── LoginDemo.vue # 登录页面
│ │ ├── ProductList.vue # 产品列表页面
│ │ └── CallbackHandler.vue # OAuth 回调处理
│ ├── App.vue # 根组件
│ └── main.js # 应用入口
├── .env.* # 环境配置文件
├── babel.config.js # Babel 配置
├── jsconfig.json # JavaScript 配置
├── package.json # 项目依赖
├── vue.config.js # Vue CLI 配置
├── Dockerfile # Docker 构建文件
└── nginx.conf # Nginx 配置

## 3. 项目技术栈

### 核心框架
- **Vue 3.2.13** - 渐进式 JavaScript 框架
- **Vue Router 4.5.1** - 官方路由管理器
- **Element Plus 2.10.5** - Vue 3 组件库

### 构建和部署
- **Webpack** - 模块打包工具
- **Nginx** - Web 服务器
- **Docker** - 容器化部署

## 4. 环境配置

### 环境变量
项目支持多环境配置，主要环境变量：

```bash
# 开发环境
VUE_APP_API_BASE_URL=http://localhost:8000/demo
VUE_APP_ENV=development

# 生产环境
VUE_APP_API_BASE_URL=/demo
VUE_APP_ENV=production
```

### 配置文件
- `.env.development` - 开发环境配置
- `.env.production` - 生产环境配置
- `.env.test` - 测试环境配置
PS: 项目自带了.env.production 文件，请关注环境配置是否是自己需要的内容。（如果是开发环境配置，请忽略这条。）

## 5. 快速开始

### 环境要求
- **Node.js 18+** - JavaScript 运行时
- **npm 8+** 或 **yarn 1.22+** - 包管理器

### 安装依赖
```bash
# 进入项目目录
cd vue

# 安装依赖
npm install
# 或
yarn install
```

### 开发环境运行
```bash
# 启动开发服务器
npm run serve

# 开发服务器将在 http://localhost:8080 启动
# 自动代理 /demo 接口到后端服务
```

### 生产环境构建
```bash
# 构建生产版本
npm run build:prod

# 构建测试版本
npm run build:test

# 预览构建结果
npm run preview
```

## 6. 项目配置

### Vue CLI 配置
项目使用 `vue.config.js` 进行配置：

- **开发服务器**: 端口 8080，支持外部访问
- **代理配置**: `/demo` 接口代理到后端服务

### 路由配置
- **登录页面**: `/login` - 支持多种登录方式
- **产品列表**: `/products` - 需要认证访问
- **OAuth 回调**: `/callback` - 处理第三方登录

## 7. 部署说明

### Docker 部署
PS: 项目携带了 Dockerfile 和 nginx.conf。请关注上述两个文件内容。

```bash
# 构建镜像
docker build -t ld-demo-frontend .

# 运行容器
docker run -p 80:80 ld-demo-frontend
```

### Nginx 部署
1. 构建项目: `npm run build:prod`
2. 将 `dist` 目录内容复制到 Nginx 静态文件目录
3. 配置 Nginx 反向代理到后端服务

### 环境配置
部署时需要配置正确的环境变量：
- 后端服务地址
- API 基础路径
- 环境标识
