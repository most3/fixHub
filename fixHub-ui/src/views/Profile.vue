<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>个人资料</h2>
        <p>维修工需完善个人信息，便于派单与联系。</p>
      </div>
      <div class="header-actions">
        <el-button type="primary">保存资料</el-button>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="card">
          <template #header><span>头像与技能</span></template>
          <div class="profile-avatar">
            <el-avatar :size="96">维</el-avatar>
            <el-upload action="#" :auto-upload="false">
              <el-button class="mt-2">上传头像</el-button>
            </el-upload>
          </div>
          <el-form label-position="top">
            <el-form-item label="技能标签">
              <el-select multiple placeholder="选择擅长领域">
                <el-option label="硬件维修" value="hardware" />
                <el-option label="网络设备" value="network" />
                <el-option label="软件系统" value="software" />
                <el-option label="多媒体设备" value="media" />
              </el-select>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card class="card">
          <template #header><span>基础信息</span></template>
          <el-form :model="form" label-width="100px">
            <el-form-item label="姓名">
              <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="form.phone" />
            </el-form-item>
            <el-form-item label="工号">
              <el-input v-model="form.employeeId" />
            </el-form-item>
            <el-form-item label="服务区域">
              <el-select v-model="form.area" placeholder="选择服务区域">
                <el-option label="主教学楼" value="a" />
                <el-option label="行政楼" value="b" />
                <el-option label="实验楼" value="c" />
              </el-select>
            </el-form-item>
            <el-form-item label="可接单时间">
              <el-time-select v-model="form.time" start="08:00" step="00:30" end="20:00" />
            </el-form-item>
            <el-form-item label="个人介绍">
              <el-input v-model="form.bio" type="textarea" :rows="4" />
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const form = reactive({
  name: '张维修',
  phone: '138****1122',
  employeeId: 'TECH-1024',
  area: 'a',
  time: '09:00',
  bio: '擅长多媒体设备维修，具备5年校园设备维护经验。'
})
</script>

<style scoped>
.profile-avatar{display:flex;flex-direction:column;align-items:center;gap:12px}
</style>
