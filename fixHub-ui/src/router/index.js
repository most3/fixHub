import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import Report from '../views/Report.vue'
import Tracking from '../views/Tracking.vue'
import Devices from '../views/Devices.vue'
import Staff from '../views/Staff.vue'
import Tasks from '../views/Tasks.vue'
import Stats from '../views/Stats.vue'
import Profile from '../views/Profile.vue'
import AdminOrders from '../views/AdminOrders.vue'
import { getToken } from '../composables/useAuth'

const routes = [
  { path: '/', component: Home },
  { path: '/login', component: Login, meta: { guest: true } },
  { path: '/register', component: Register, meta: { guest: true } },
  { path: '/dashboard', component: Dashboard, meta: { requiresAuth: true } },
  { path: '/report', component: Report, meta: { requiresAuth: true } },
  { path: '/tracking', component: Tracking, meta: { requiresAuth: true } },
  { path: '/devices', component: Devices, meta: { requiresAuth: true } },
  { path: '/staff', component: Staff, meta: { requiresAuth: true } },
  { path: '/tasks', component: Tasks, meta: { requiresAuth: true } },
  { path: '/stats', component: Stats, meta: { requiresAuth: true } },
  { path: '/profile', component: Profile, meta: { requiresAuth: true } },
  { path: '/admin-orders', component: AdminOrders, meta: { requiresAuth: true } }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const token = getToken()
  if (to.meta.requiresAuth && !token) return next('/login')
  if (to.meta.guest && token) return next('/dashboard')
  next()
})

export default router
