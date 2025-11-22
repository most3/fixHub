<template>
  <el-card class="card">
    <h2>仪表盘</h2>
    <p>欢迎使用 FixHub 管理后台（示例页面）。</p>
    <el-button type="primary" @click="showInfo">查看我的信息</el-button>
  </el-card>
</template>

<script>
import { ElMessage } from 'element-plus'
export default {
  setup () {
    function showInfo () {
      const token = localStorage.getItem('fixhub_token')
      ElMessage.info(token ? '已登录（token 已保存）' : '未登录')
    }
    return { showInfo }
  }
}
</script>

<style scoped>
.card{max-width:720px;margin:40px auto;padding:20px}
</style>
