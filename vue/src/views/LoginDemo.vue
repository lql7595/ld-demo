<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>登录系统</h1>
        <p>请选择登录方式</p>
      </div>
      <!-- GitHub 回调处理区域 -->
      <div v-if="showCallbackProcessing" class="callback-processing">
        <div class="spinner"></div>
        <p>正在处理 GitHub 登录...</p>
      </div>

      <!-- 登录方式切换 -->
      <div class="login-tabs">
        <button :class="['tab-btn', { active: activeTab === 'password' }]" @click="activeTab = 'password'">
          密码登录
        </button>
        <button :class="['tab-btn', { active: activeTab === 'ldap' }]" @click="activeTab = 'ldap'">
          LDAP登录
        </button>
        <button :class="['tab-btn', { active: activeTab === 'github' }]" @click="activeTab = 'github'">
          GitHub登录
        </button>
      </div>

      <!-- 密码登录表单 -->
      <div v-if="activeTab === 'password'" class="login-form">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="passwordForm.username" type="text" placeholder="请输入用户名" :disabled="loading" />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="passwordForm.password" type="password" placeholder="请输入密码" :disabled="loading" />
        </div>
        <button class="login-btn" @click="handlePasswordLogin"
          :disabled="loading || !passwordForm.username || !passwordForm.password">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </div>

      <!-- LDAP登录表单 -->
      <div v-if="activeTab === 'ldap'" class="login-form">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="ldapForm.username" type="text" placeholder="请输入LDAP用户名" :disabled="loading" />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="ldapForm.password" type="password" placeholder="请输入LDAP密码" :disabled="loading" />
        </div>
        <button class="login-btn" @click="handleLdapLogin"
          :disabled="loading || !ldapForm.username || !ldapForm.password">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </div>

      <!-- GitHub登录 -->
      <div v-if="activeTab === 'github'" class="login-form">
        <div class="github-login">
          <p>点击下方按钮使用GitHub账号登录</p>
          <button class="github-btn" @click="handleGithubLogin" :disabled="loading">
            {{ loading ? '跳转中...' : 'GitHub登录' }}
          </button>
        </div>
      </div>

      <!-- 错误提示 -->
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      <div class="login-footer" >
        <a href="https://github.com/lql7595/ld-demo" target="_blank">github仓库</a>
      </div>
    </div>
  </div>
</template>

<script>
import { loginAPI } from '../api'

export default {
  name: 'LoginDemo',
  data() {
    return {
      activeTab: 'password',
      loading: false,
      errorMessage: '',
      showCallbackProcessing: false,
      passwordForm: {
        username: '',
        password: ''
      },
      ldapForm: {
        username: '',
        password: ''
      }
    }
  },
  mounted() {
    // 检查是否有GitHub回调参数
    const urlParams = new URLSearchParams(window.location.search)
    const code = urlParams.get('code')
    if (code) {
      this.handleGithubCallback(code)
    }
  },
  methods: {
    async handlePasswordLogin() {
      this.loading = true
      this.errorMessage = ''

      try {
        const response = await loginAPI.passwordLogin(
          this.passwordForm.username,
          this.passwordForm.password
        )

        if (response.data) {
          localStorage.setItem('token', response.data)
          localStorage.setItem('userLogin', this.passwordForm.username)

          this.$router.push('/products')
        }
      } catch (error) {
        this.errorMessage = error.response?.data?.message || '登录失败，请检查用户名和密码'
      } finally {
        this.loading = false
      }
    },

    async handleLdapLogin() {
      this.loading = true
      this.errorMessage = ''

      try {
        const response = await loginAPI.ldapLogin(
          this.ldapForm.username,
          this.ldapForm.password
        )

        if (response.data) {
          localStorage.setItem('token', response.data)
          localStorage.setItem('userLogin', this.ldapForm.username)
          this.$router.push('/products')
        }
      } catch (error) {
        this.errorMessage = error.response?.data?.message || 'LDAP登录失败，请检查用户名和密码'
      } finally {
        this.loading = false
      }
    },

    async handleGithubLogin() {
      this.loading = true
      this.errorMessage = ''

      try {
        const url = 'https://github.com/login/oauth/authorize?client_id=' + 'Ov23litOUbZgf6NTZC8Y'
        // 重定向到GitHub授权页面
        window.open(url, '_self')
      } catch (error) {
        this.errorMessage = 'GitHub登录失败，请稍后重试'
        this.loading = false
      }
    },

    // 处理GitHub回调
    async handleGithubCallback(code) {
      try {
        console.log('GitHub callback params:', { code })

        this.loading = true
        this.errorMessage = ''

        try {
          const response = await loginAPI.githubCallback(code)

          if (response.errorCode === 200) {
            localStorage.setItem('token', response.data.token)
            localStorage.setItem('userLogin', response.data.userLogin)
            this.$router.push('/products')
          } else {
            this.errorMessage = response.data.message
            this.loading = false
          }
        } catch (error) {
          this.errorMessage = 'GitHub登录失败，请稍后重试'
          this.loading = false
        }
      } catch (error) {
        console.error('GitHub callback error:', error)
        this.errorMessage = error.message || 'GitHub 登录失败，请稍后重试'
      } finally {
        this.showCallbackProcessing = false
      }
    },
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  color: #333;
  margin-bottom: 8px;
  font-size: 28px;
}

.login-header p {
  color: #666;
  margin: 0;
}

.login-tabs {
  display: flex;
  margin-bottom: 30px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e0e0e0;
}

.tab-btn {
  flex: 1;
  padding: 12px;
  border: none;
  background: #f8f9fa;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.tab-btn.active {
  background: #007bff;
  color: white;
}

.tab-btn:hover:not(.active) {
  background: #e9ecef;
}

.login-form {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s ease;
  box-sizing: border-box;
}

.form-group input:focus {
  outline: none;
  border-color: #007bff;
}

.login-btn,
.github-btn {
  width: 100%;
  padding: 12px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.login-btn:hover:not(:disabled),
.github-btn:hover:not(:disabled) {
  background: #0056b3;
}

.login-btn:disabled,
.github-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.github-login {
  text-align: center;
}

.github-login p {
  margin-bottom: 20px;
  color: #666;
}

.github-btn {
  background: #24292e;
}

.github-btn:hover:not(:disabled) {
  background: #1a1e21;
}

.error-message {
  background: #f8d7da;
  color: #721c24;
  padding: 12px;
  border-radius: 6px;
  margin-top: 20px;
  text-align: center;
  border: 1px solid #f5c6cb;
}
.login-footer {
  text-align: center;
  margin-top: 20px;
  color: #666;
}
.login-footer a {
  color: #007bff;
  text-decoration: none;
}
.login-footer a:hover {
  text-decoration: underline;
}
</style>