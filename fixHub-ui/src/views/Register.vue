<template>
  <div class="auth-wrapper">
    <el-card class="auth-card">
      <div class="auth-left">
        <h2>加入 FixHub</h2>
        <p>提交报修更高效，进度透明可追踪。</p>
        <ul>
          <li>一站式设备报修流程</li>
          <li>管理员派单与监督</li>
          <li>维修工快速响应</li>
        </ul>
      </div>
      <div class="auth-right">
        <h3>创建账号</h3>
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
          <el-button type="primary" @click="onSubmit" style="width:100%">注册</el-button>
          <div class="auth-links">
            <el-button link @click="$router.push('/login')">已有账号？去登录</el-button>
          </div>
        </el-form>
      </div>
    </el-card>
  </div>
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
.auth-wrapper{display:flex;justify-content:center;align-items:center;min-height:calc(100vh - 120px)}
.auth-card{display:grid;grid-template-columns:1fr 1fr;gap:24px;max-width:860px;padding:24px}
.auth-left{background:linear-gradient(135deg,#e6f0ff,#f8fbff);padding:24px;border-radius:16px}
.auth-left ul{padding-left:18px;color:#4b5563;line-height:1.8}
.auth-right{padding:8px 12px}
.auth-links{display:flex;justify-content:flex-end;margin-top:12px}
@media (max-width: 900px){
  .auth-card{grid-template-columns:1fr}
}
</style>
