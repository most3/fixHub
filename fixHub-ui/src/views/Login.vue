<template>
  <el-card class="card">
    <h2>登录</h2>
    <el-form :model="form" ref="formRef" label-position="top">
      <el-form-item label="用户名">
        <el-input v-model="form.username" autocomplete="username" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="form.password" autocomplete="current-password" />
      </el-form-item>
      <el-button type="primary" @click="onSubmit">登录</el-button>
    </el-form>
  </el-card>
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
.card{max-width:420px;margin:40px auto;padding:20px}
</style>
