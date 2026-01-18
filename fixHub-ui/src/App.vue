<template>
  <el-container style="min-height:100vh">
    <el-header class="topbar">
      <div class="header-wrap">
        <nav class="main-nav">
          <button
            v-for="item in mainSections"
            :key="item.id"
            class="main-nav-item"
            :class="{ active: item.id === currentSection }"
            @click="goSection(item)"
          >
            {{ item.label }}
          </button>
        </nav>

        <div class="nav-right">
          <el-button type="text" @click="go('/')">首页</el-button>
          <el-button type="text" v-if="isAuthenticated" @click="go('/dashboard')">仪表盘</el-button>
          <template v-if="!isAuthenticated">
            <el-button type="text" @click="go('/login')">登录</el-button>
            <el-button type="text" @click="go('/register')">注册</el-button>
          </template>
          <template v-else>
            <el-dropdown>
              <span class="el-dropdown-trigger">
                <el-button type="text">{{ userDisplay }}</el-button>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="go('/dashboard')">个人中心</el-dropdown-item>
                  <el-dropdown-item divided @click="logout">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </div>
      </div>
    </el-header>

    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import useAuth from './composables/useAuth'

export default {
  setup () {
    const router = useRouter()
    // use centralized auth composable so header responds to login/logout across tabs
    const { isAuthenticated, clearToken, user } = useAuth()

    const role = computed(() => user.value?.role || 'GUEST')

    const mainSections = computed(() => {
      const base = [
        { id: 'home', label: '首页', path: '/' }
      ]
      const reporter = [
        { id: 'report', label: '提交报修', path: '/report' },
        { id: 'tracking', label: '状态跟踪', path: '/tracking' }
      ]
      const technician = [
        { id: 'tasks', label: '维修工单', path: '/tasks' },
        { id: 'tracking', label: '状态跟踪', path: '/tracking' }
      ]
      const admin = [
        { id: 'admin-orders', label: '派单管理', path: '/admin-orders' },
        { id: 'devices', label: '设备管理', path: '/devices' },
        { id: 'staff', label: '人员管理', path: '/staff' },
        { id: 'stats', label: '数据看板', path: '/stats' }
      ]
      if (role.value === 'REPORTER') return [...base, ...reporter]
      if (role.value === 'TECHNICIAN') return [...base, ...technician]
      if (role.value === 'ADMIN') return [...base, ...admin]
      return [...base, ...reporter]
    })

    const currentSection = computed(() => {
      const path = router.currentRoute.value.path
      const matched = mainSections.value.find(item => item.path === path)
      return matched?.id || 'home'
    })

    function go(path) { router.push(path) }
    function logout () { clearToken(); router.push('/login') }

    function goSection (item) {
      router.push(item.path)
    }

    const userDisplay = computed(() => user.value?.displayName || user.value?.username || '用户')

    return { go, logout, isAuthenticated, mainSections, goSection, currentSection, user, userDisplay, role }
  }
}
</script>

<style scoped>
.topbar { position: sticky; top: 0; z-index: 50; background: #fff; border-bottom: 1px solid #eee }
.header-wrap{display:flex;align-items:center;justify-content:space-between;gap:16px;padding:8px 20px}
.main-nav{display:flex;gap:12px;justify-content:center;flex:1}
.main-nav-item{padding:8px 16px;border-radius:8px;border:1px solid transparent;background:transparent;cursor:pointer}
.main-nav-item.active{background:#2c64ff;color:#fff;border-color:#2c64ff}
.nav-right{display:flex;gap:8px;align-items:center}
.el-dropdown-trigger{display:inline-flex;align-items:center}
</style>
