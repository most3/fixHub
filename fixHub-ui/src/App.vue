<template>
  <el-container style="min-height:100vh">
    <el-header>
      <div class="header-wrap">
        <div class="logo">FixHub</div>
        <div class="nav">
          <el-button type="text" @click="go('/')">首页</el-button>
          <el-button type="text" v-if="isAuthenticated" @click="go('/dashboard')">仪表盘</el-button>
          <el-button type="text" v-if="!isAuthenticated" @click="go('/login')">登录</el-button>
          <el-button type="text" v-if="!isAuthenticated" @click="go('/register')">注册</el-button>
          <el-button type="text" v-if="isAuthenticated" @click="logout">退出</el-button>
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

export default {
  setup () {
    const router = useRouter()
    const isAuthenticated = computed(() => !!localStorage.getItem('fixhub_token'))
    function go(path) { router.push(path) }
    function logout () { localStorage.removeItem('fixhub_token'); router.push('/login') }
    return { go, logout, isAuthenticated }
  }
}
</script>

<style scoped>
.header-wrap{display:flex;justify-content:space-between;align-items:center}
.logo{font-weight:700}
.nav{display:flex;gap:8px}
</style>
