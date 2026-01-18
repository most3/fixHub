<template>
  <div class="auth-wrapper">
    <el-card class="auth-card">
      <div class="auth-left">
        <h2>欢迎登录 FixHub</h2>
        <p>校园设备报修与维护一体化平台</p>
        <ul>
          <li>在线提交报修单</li>
          <li>维修进度实时可视</li>
          <li>数据看板透明管理</li>
        </ul>
      </div>
      <div class="auth-right">
        <h3>账号登录</h3>
        <el-form :model="form" ref="formRef" label-position="top">
          <el-form-item label="用户名">
            <el-input v-model="form.username" autocomplete="username" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input type="password" v-model="form.password" autocomplete="current-password" />
          </el-form-item>
          <el-button type="primary" @click="onSubmit" style="width:100%">登录</el-button>
          <div class="auth-links">
            <el-button link @click="$router.push('/register')">没有账号？去注册</el-button>
            <el-button link>忘记密码</el-button>
          </div>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/auth'
import { ElMessage } from 'element-plus'
import useAuth from '../composables/useAuth'

export default {
  setup () {
    const router = useRouter()
    const formRef = ref(null)
    const form = ref({ username: '', password: '' })

    const { setSession } = useAuth()

    async function onSubmit () {
      // 前端校验
      if (!form.value.username || !form.value.password) {
        ElMessage.warning('请输入用户名和密码')
        return
      }
      if (form.value.password.length < 6) {
        ElMessage.warning('密码长度至少6位')
        return
      }
      try {
        const data = await login(form.value.username, form.value.password)
        // 后端暂未返回 token，这里直接保存用户信息以保持登录状态
        setSession(data)
        ElMessage.success('登录成功')
        // 登录后跳转到首页（并保留 token）
        router.push({ path: '/', query: { section: 'report' } })
      } catch (err) {
        console.error(err)
        ElMessage.error(err.humanMessage || err.response?.data?.message || err.message || '登录失败')
      }
    }

    return { form, onSubmit, formRef }
  }
}
</script>

<style scoped>
.auth-wrapper{display:flex;justify-content:center;align-items:center;min-height:calc(100vh - 120px)}
.auth-card{display:grid;grid-template-columns:1fr 1fr;gap:24px;max-width:860px;padding:24px}
.auth-left{background:linear-gradient(135deg,#e6f0ff,#f8fbff);padding:24px;border-radius:16px}
.auth-left ul{padding-left:18px;color:#4b5563;line-height:1.8}
.auth-right{padding:8px 12px}
.auth-links{display:flex;justify-content:space-between;margin-top:12px}
@media (max-width: 900px){
  .auth-card{grid-template-columns:1fr}
}
</style>
