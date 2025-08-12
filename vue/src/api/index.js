import axios from 'axios'
// import { Message } from 'element-plus' 


// 创建axios实例
const api = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || '/demo',
  timeout: 60000
})
console.log('API Base URL:', process.env.VUE_APP_API_BASE_URL)
console.log('Environment:', process.env.VUE_APP_ENV)

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    const userLogin = localStorage.getItem('userLogin')
    if (config.method !== 'get') {
      // 如果是POST/PUT/DELETE请求且不是登录接口
      if (config.data) {
        // 如果已有请求体，添加参数
        config.data = {
          ...config.data,
          userLogin: config.data.userLogin ? config.data.userLogin : userLogin,
          token: token
        }
      } else {
        // 如果没有请求体，创建新的请求体
        config.data = {
          userLogin: userLogin,
          token: token
        }
      }
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    if (response.data.errorCode === 604) {
      localStorage.removeItem('token')
      localStorage.removeItem('userLogin')
      window.location.href = '/login'
    }
    if (response.data.errorCode !== 200) {
      // Message.error(response.data.message)
      alert(response.data.message)
    }
    return response.data
  },
  error => {
    if (error.response && (error.response.status === 401) ) {
      localStorage.removeItem('token')
      localStorage.removeItem('userLogin')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 登录相关API
export const loginAPI = {
  // 密码登录
  passwordLogin: (userLogin, pwd) => {
    return api.post('/user/sys/login', { userLogin, pwd, loginType: 1 })
  },
  
  // LDAP登录
  ldapLogin: (userLogin, pwd) => {
    return api.post('/user/sys/login/ldap', { userLogin, pwd, loginType:3 })
  },
  
  // GitHub登录
  githubLogin: () => {
    return api.get('/user/sys/login/github')
  },
  
  // GitHub回调处理
  githubCallback: (code) => {
    return api.get('/user/sys/oauth/callback?code=' + code )
  },

  logout: () => {
    return api.post('/user/sys/logout')
  }
}

// 商品相关API
export const productAPI = {
  // 获取商品列表
  getProducts: () => {
    return api.post('/product/bus/query/infolist')
  },
  
  // 创建商品
  createProduct: (product) => {
    return api.post('/product/bus/add/info', product)
  },
  
  // 更新商品
  updateProduct: (product) => {
    return api.post(`/product/bus/update/info`, product)
  },
  
  // 删除商品
  deleteProduct: (id) => {
    return api.post(`/product/bus/del/info`, id)
  }
}

export default api 