<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>工作台</h2>
        <p>基于角色的统一入口，快速进入报修、派单、处理与统计。</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="$router.push('/report')">提交报修</el-button>
        <el-button @click="$router.push('/tracking')">查看状态</el-button>
      </div>
    </div>

    <el-row :gutter="20" class="mb-4">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">128</div>
          <div class="stat-label">本月报修量</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">4.2h</div>
          <div class="stat-label">平均响应时间</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">96%</div>
          <div class="stat-label">满意度</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">12</div>
          <div class="stat-label">待处理工单</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="card">
          <template #header>
            <div class="card-header">
              <span>当前工单进度</span>
              <el-button text @click="$router.push('/tracking')">查看全部</el-button>
            </div>
          </template>
          <el-steps :active="2" align-center>
            <el-step title="待受理" description="已提交报修" />
            <el-step title="处理中" description="维修工处理中" />
            <el-step title="待评价" description="等待用户确认" />
            <el-step title="已完成" description="评价归档" />
          </el-steps>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="card">
          <template #header>
            <span>快捷入口</span>
          </template>
          <div class="quick-list">
            <el-button class="quick-btn" @click="$router.push('/devices')">设备管理</el-button>
            <el-button class="quick-btn" @click="$router.push('/staff')">人员管理</el-button>
            <el-button class="quick-btn" @click="$router.push('/tasks')">维修工单</el-button>
            <el-button class="quick-btn" @click="$router.push('/stats')">数据看板</el-button>
            <el-button class="quick-btn" @click="$router.push('/profile')">个人资料</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.quick-list{display:grid;grid-template-columns:1fr 1fr;gap:10px}
.quick-btn{width:100%}
</style>
