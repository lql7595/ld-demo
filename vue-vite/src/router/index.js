import { createRouter, createWebHistory } from 'vue-router'
import LoginDemo from '../views/LoginDemo.vue'
import ProductList from '../views/ProductList.vue'
import CallbackHandler from '../views/CallbackHandler.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'LoginDemo',
    component: LoginDemo
  },
  {
    path: '/callback',
    name: 'CallbackHandler',
    component: CallbackHandler
  },
  {
    path: '/products',
    name: 'ProductList',
    component: ProductList,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL || '/'),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router
