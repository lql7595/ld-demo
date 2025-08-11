# Cloud Demo - Vue Vite

基于 Vue 3 + Vite + Element Plus 的现代化前端项目。

## 技术栈

- **Vue 3** - 渐进式JavaScript框架
- **Vite** - 下一代前端构建工具
- **Vue Router 4** - Vue.js官方路由管理器
- **Element Plus** - 基于Vue 3的组件库
- **Axios** - HTTP客户端

## 项目特性

- 🚀 **极速开发** - Vite提供超快的热更新
- 📱 **响应式设计** - 支持移动端和桌面端
- 🎨 **现代化UI** - 使用Element Plus组件库
- 🔐 **多种登录方式** - 支持密码、LDAP、GitHub登录
- 📦 **代码分割** - 自动优化包大小

## 开发环境要求

- Node.js >= 20.19.0 或 >= 22.12.0
- npm >= 8.0.0

## 项目设置

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 生产环境构建
npm run build:prod

# 预览生产构建
npm run preview
```

## 环境变量

项目支持多环境配置：

- `.env.development` - 开发环境配置
- `.env.production` - 生产环境配置

### 主要环境变量

```bash
VITE_API_BASE_URL=/demo          # API基础URL
VITE_APP_ENV=development         # 应用环境
VITE_APP_TITLE=Cloud Demo        # 应用标题
```

## 项目结构

```
src/
├── api/              # API接口
├── assets/           # 静态资源
├── components/       # 公共组件
├── router/           # 路由配置
├── views/            # 页面组件
├── App.vue           # 根组件
└── main.js           # 入口文件
```

## 构建脚本

- `npm run dev` - 开发服务器
- `npm run dev:prod` - 生产模式开发服务器
- `npm run build` - 构建
- `npm run build:prod` - 生产环境构建
- `npm run build:test` - 测试环境构建
- `npm run preview` - 预览构建结果
- `npm run deploy` - 部署脚本

## 开发指南

### 推荐IDE设置

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar)

### 代码规范

项目使用ESLint进行代码检查，确保代码质量。

## 部署

构建完成后，将 `dist` 目录部署到Web服务器即可。

```bash
npm run build:prod
# 将 dist/ 目录内容部署到服务器
```
