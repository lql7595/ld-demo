const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  
  // 开发服务器配置
  devServer: {
    port: process.env.PORT || 8080, // 开发环境默认8080端口
    host: '0.0.0.0', // 允许外部访问
    open: true, // 自动打开浏览器
    https: false, // 是否使用HTTPS
    proxy: {
      '/demo': {
        target: 'http://localhost:7573',
        changeOrigin: true,
        secure: false,
        logLevel: 'debug'
      }
    }
  },
  
  // 生产环境配置
  publicPath: process.env.NODE_ENV === 'production' ? '/' : '/',
  outputDir: 'dist',
  assetsDir: 'static'
})
