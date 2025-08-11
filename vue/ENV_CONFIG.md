# 环境变量配置说明

## VUE_APP_API_BASE_URL 使用方法

### 1. 创建环境变量文件

在项目根目录创建 `.env` 文件：

```bash
# .env
VUE_APP_API_BASE_URL=http://localhost:8000/api
```

### 2. 不同环境的配置

#### 开发环境 (.env.development)
```bash
VUE_APP_API_BASE_URL=http://localhost:8000/api
VUE_APP_ENV=development
```

#### 生产环境 (.env.production)
```bash
VUE_APP_API_BASE_URL=https://your-api-domain.com/api
VUE_APP_ENV=production
```

#### 测试环境 (.env.test)
```bash
VUE_APP_API_BASE_URL=http://test-api-domain.com/api
VUE_APP_ENV=test
```

### 3. 在代码中使用

```javascript
// 获取环境变量
const apiBaseUrl = process.env.VUE_APP_API_BASE_URL
console.log('API Base URL:', apiBaseUrl)

// 在axios配置中使用
const api = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8000/api',
  timeout: 10000
})
```

### 4. 启动不同环境

```bash
# 开发环境
npm run serve

# 生产环境构建
npm run build

# 测试环境
npm run serve --mode test
```

### 5. 注意事项

- 环境变量必须以 `VUE_APP_` 开头
- 修改环境变量后需要重启开发服务器
- `.env` 文件不应该提交到版本控制（添加到 .gitignore）
- 可以创建 `.env.local` 用于本地开发（不会被提交）

### 6. 当前项目配置

项目已配置为使用 `VUE_APP_API_BASE_URL`，默认值为 `http://localhost:8000/api`。

如果需要修改API地址，只需要：
1. 创建 `.env` 文件
2. 设置 `VUE_APP_API_BASE_URL=你的API地址`
3. 重启开发服务器 