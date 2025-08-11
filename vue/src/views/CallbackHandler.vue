<template>
  <div class="callback-container">
    <div class="callback-card">
      <div class="callback-header">
        <div class="spinner"></div>
        <h2>正在处理登录...</h2>
        <p>{{ statusMessage }}</p>
      </div>
      
      <!-- 进度条 -->
      <div class="progress-container">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: progressWidth + '%' }"></div>
        </div>
        <div class="progress-text">{{ progressText }}</div>
      </div>
      
      <!-- 错误信息 -->
      <div v-if="errorMessage" class="error-message">
        <div class="error-icon">⚠️</div>
        <p>{{ errorMessage }}</p>
        <button class="back-btn" @click="goBack">
          返回登录页
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { loginAPI } from '../api'

export default {
  name: 'CallbackHandler',
  data() {
    return {
      code: '',
      statusMessage: '正在验证您的身份...',
      progressWidth: 0,
      progressText: '0%',
      errorMessage: '',
      processingSteps: [
        '正在验证您的身份...',
        '正在获取用户信息...',
        '正在完成登录...',
        '登录成功！正在跳转...'
      ],
      currentStep: 0
    }
  },
  mounted() {
    // 获取URL中的code参数
    const urlParams = new URLSearchParams(window.location.search)
    this.code = urlParams.get('code')
    
    if (!this.code) {
      this.handleError('缺少必要的授权码，请重新登录')
      return
    }
    
    // 开始处理回调
    this.processCallback()
  },
  methods: {
    async processCallback() {
      try {
        this.startProgress()
        
        // 调用后端API处理回调
        const response = await loginAPI.githubCallback(this.code)
        
        if (response.errorCode === 200) {
          // 登录成功
          this.updateProgress(100, '登录成功！正在跳转...')
          
          // 保存登录信息
          localStorage.setItem('token', response.data.token)
          localStorage.setItem('userLogin', response.data.userLogin)
          
          // 延迟跳转，让用户看到成功状态
          setTimeout(() => {
            this.redirectToTarget(response.data)
          }, 1000)
          
        } else {
          // 后端返回错误
          this.handleError(response.data?.message || '登录失败，请稍后重试')
        }
        
      } catch (error) {
        console.error('Callback processing error:', error)
        this.handleError('网络错误，请检查网络连接后重试')
      }
    },
    
    startProgress() {
      this.progressWidth = 0
      this.currentStep = 0
      this.updateProgress(10, this.processingSteps[0])
      
      // 模拟进度更新
      this.progressInterval = setInterval(() => {
        if (this.progressWidth < 90) {
          this.progressWidth += Math.random() * 10
          this.currentStep = Math.floor(this.progressWidth / 30)
          this.statusMessage = this.processingSteps[this.currentStep] || this.processingSteps[this.processingSteps.length - 1]
          this.progressText = Math.floor(this.progressWidth) + '%'
        }
      }, 500)
    },
    
    updateProgress(width, text) {
      this.progressWidth = width
      this.progressText = text
      this.statusMessage = text
    },
    
    redirectToTarget(data) {
      // 根据后端返回的数据决定跳转目标
      // 这里可以根据你的业务逻辑进行调整
      const targetPage = data.targetPage || '/products'
      
      // 清除进度定时器
      if (this.progressInterval) {
        clearInterval(this.progressInterval)
      }
      
      // 跳转到目标页面
      this.$router.push(targetPage)
    },
    
    handleError(message) {
      this.errorMessage = message
      this.statusMessage = '处理失败'
      
      // 清除进度定时器
      if (this.progressInterval) {
        clearInterval(this.progressInterval)
      }
      
      this.updateProgress(0, '处理失败')
    },
    
    goBack() {
      this.$router.push('/login')
    }
  },
  beforeUnmount() {
    // 清理定时器
    if (this.progressInterval) {
      clearInterval(this.progressInterval)
    }
  }
}
</script>

<style scoped>
.callback-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.callback-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 400px;
  text-align: center;
}

.callback-header {
  margin-bottom: 30px;
}

.callback-header h2 {
  color: #333;
  margin: 20px 0 10px 0;
  font-size: 24px;
}

.callback-header p {
  color: #666;
  margin: 0;
  font-size: 16px;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.progress-container {
  margin-bottom: 30px;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background-color: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 10px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #007bff, #0056b3);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-text {
  color: #666;
  font-size: 14px;
  font-weight: 500;
}

.error-message {
  background: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 8px;
  padding: 20px;
  margin-top: 20px;
}

.error-icon {
  font-size: 24px;
  margin-bottom: 10px;
}

.error-message p {
  color: #721c24;
  margin: 10px 0 20px 0;
  font-size: 14px;
}

.back-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  background: #6c757d;
  color: white;
}

.back-btn:hover {
  background: #545b62;
}
</style> 