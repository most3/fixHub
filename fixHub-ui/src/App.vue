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
            @click="goSection(item.id)"
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

    const mainSections = [
      { id: 'report', label: '报修' },
      { id: 'tracking', label: '状态跟踪' },
      { id: 'device', label: '设备' },
      { id: 'stats', label: '数据统计' }
    ]

    const currentSection = computed(() => {
      // reflect route query so header can highlight active when on home
      const q = router.currentRoute.value.query.section
      return q || ''
    })

    function go(path) { router.push(path) }
    function logout () { clearToken(); router.push('/login') }

    function goSection (id) {
      // navigate to home with section query so Home.vue can pick it up
      router.push({ path: '/', query: { section: id } })
    }

    const userDisplay = computed(() => user.value?.displayName || user.value?.username || '用户')

    return { go, logout, isAuthenticated, mainSections, goSection, currentSection, user, userDisplay }
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
