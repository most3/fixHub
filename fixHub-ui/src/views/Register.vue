<template>
  <el-card class="card">
    <h2>注册</h2>
    <el-form :model="form" ref="formRef" label-position="top">
      <el-form-item label="用户名">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="form.password" />
      </el-form-item>
      <el-form-item label="显示名">
        <el-input v-model="form.displayName" />
      </el-form-item>
      <el-form-item label="联系电话">
        <el-input v-model="form.phone" />
      </el-form-item>
        <el-button type="primary" @click="onSubmit">注册</el-button>
    </el-form>
  </el-card>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/auth'
import { ElMessage } from 'element-plus'

export default {
  setup () {
    const router = useRouter()
    const formRef = ref(null)
    const form = ref({ username: '', password: '', displayName: '', phone: '' })

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
        // 普通用户注册默认不传 role，后端会处理为 REPORTER
        await register({ username: form.value.username, password: form.value.password, displayName: form.value.displayName, phone: form.value.phone })
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } catch (err) {
        console.error(err)
        ElMessage.error(err.humanMessage || err.response?.data?.message || err.message || '注册失败')
      }
    }

    return { form, onSubmit, formRef }
  }
}
</script>

<style scoped>
.card{max-width:420px;margin:40px auto;padding:20px}
</style>
