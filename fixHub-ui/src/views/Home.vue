<template>
  <div class="home">
    <section class="hero">
      <h1>FixHub 校园设备报修与维护平台</h1>
      <p class="subtitle">覆盖报修、派工、维修、评价全流程，让校园设备维护更高效透明。</p>
      <div class="hero-buttons">
        <el-button type="primary" size="large" @click="go('/login')">立即登录</el-button>
        <el-button size="large" @click="go('/register')">注册账号</el-button>
      </div>
    </section>

    <section class="section" id="roles">
      <h2>角色职能</h2>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" v-for="role in roles" :key="role.title">
          <el-card shadow="hover" class="card">
            <h3>{{ role.title }}</h3>
            <p>{{ role.desc }}</p>
            <el-button type="primary" plain @click="hint(role.hint)">{{ role.button }}</el-button>
          </el-card>
        </el-col>
      </el-row>
    </section>

    <section class="section" id="features">
      <h2>平台特色</h2>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" v-for="feature in features" :key="feature.title">
          <el-card shadow="never" class="card">
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.desc }}</p>
            <el-button type="primary" link @click="hint(feature.hint)">{{ feature.button }}</el-button>
          </el-card>
        </el-col>
      </el-row>
    </section>

    <section class="section" id="workflow">
      <h2>报修流程</h2>
      <div class="steps">
        <div class="step" v-for="(step, index) in steps" :key="step">
          <div class="step-index">{{ index + 1 }}</div>
          <div class="step-text">{{ step }}</div>
        </div>
      </div>
    </section>

    <section class="section cta">
      <h2>马上开启高效的设备维护协作</h2>
      <p>登录后即可体验数据实时同步、流程透明的报修平台。</p>
      <div class="cta-buttons">
        <el-button type="primary" size="large" @click="go('/login')">我要登录</el-button>
        <el-button size="large" plain @click="go('/register')">我是新用户</el-button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

const roles = [
  {
    title: '报修用户',
    desc: '在线提交报修单，记录设备信息、故障描述及现场照片，可随时查看处理进度和历史记录。',
    button: '在线报修',
    hint: '请先登录后在仪表盘提交新的报修单。'
  },
  {
    title: '维修工',
    desc: '接收管理员派发的工单，处理并反馈维修结果，系统自动记录响应时间与处理质量。',
    button: '查看派工',
    hint: '登录后可在任务列表查看派工详情。'
  },
  {
    title: '管理员',
    desc: '维护设备档案，指派维修任务，掌握维修状态与统计数据，确保流程透明。',
    button: '进入管理台',
    hint: '管理员登录后可在后台完成派单和统计分析。'
  }
]

const features = [
  {
    title: '一站式报修',
    desc: '报修单支持文字与图片描述，流程节点自动通知，减少线下沟通成本。',
    button: '了解报修流程',
    hint: '具体流程可在登录后于仪表盘查看。'
  },
  {
    title: '高效派单',
    desc: '管理员按设备类型或区域快速派发任务，维修工实时接收并反馈。',
    button: '查看派单示例',
    hint: '登录管理员账号即可体验派单功能。'
  },
  {
    title: '数据看板',
    desc: '直观呈现维修量、响应时长、用户满意度等指标，辅助优化管理策略。',
    button: '预览数据看板',
    hint: '数据看板将在登录后展示实时统计信息。'
  }
]

const steps = [
  '报修用户提交设备故障信息与图片',
  '管理员审核并指派合适的维修工',
  '维修工处理工单并填写处理结果',
  '用户确认维修效果后进行评价',
  '系统归档数据，纳入看板统计'
]

function go (path) {
  router.push(path)
}

function hint (message) {
  ElMessage.info(message)
}
</script>

<style scoped>
.home { padding: 48px 24px 80px; background: #f7f8fa; }
.hero { text-align: center; max-width: 720px; margin: 0 auto 60px; }
.hero h1 { font-size: 2.8rem; margin-bottom: 16px; }
.subtitle { color: #606266; font-size: 1.1rem; margin-bottom: 28px; line-height: 1.6; }
.hero-buttons { display: flex; justify-content: center; gap: 16px; }
.section { max-width: 1080px; margin: 0 auto 64px; }
.section h2 { text-align: center; margin-bottom: 32px; }
.card { height: 100%; }
.card h3 { margin-bottom: 12px; }
.card p { color: #606266; line-height: 1.6; }
.steps { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; }
.step { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 4px 18px rgba(0,0,0,0.08); display: flex; align-items: center; gap: 16px; }
.step-index { width: 40px; height: 40px; border-radius: 50%; background: #409eff; color: #fff; display: flex; align-items: center; justify-content: center; font-weight: 600; }
.step-text { color: #303133; line-height: 1.5; }
.cta { text-align: center; background: linear-gradient(135deg, #5c86ff, #7fb2ff); color: #fff; border-radius: 20px; padding: 56px 24px; }
.cta p { max-width: 560px; margin: 12px auto 28px; font-size: 1.05rem; line-height: 1.6; }
.cta-buttons { display: flex; justify-content: center; gap: 16px; }
@media (max-width: 768px) {
  .hero { padding: 0 12px; }
  .hero h1 { font-size: 2.2rem; }
  .hero-buttons, .cta-buttons { flex-direction: column; }
  .home { padding: 32px 16px 60px; }
}
</style>
