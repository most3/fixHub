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

export default {
  setup () {
    const router = useRouter()
    const formRef = ref(null)
    const form = ref({ username: '', password: '' })

    async function onSubmit () {
      try {
        const data = await login(form.value.username, form.value.password)
        // 假定后端返回 token
        if (data.token) localStorage.setItem('fixhub_token', data.token)
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } catch (err) {
        console.error(err)
        ElMessage.error(err.response?.data?.message || err.message || '登录失败')
      }
    }

    return { form, onSubmit, formRef }
  }
}
</script>

<style scoped>
.card{max-width:420px;margin:40px auto;padding:20px}
</style>
