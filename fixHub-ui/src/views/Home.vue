<template>
  <div class="home">
    <main class="home-body" v-if="currentSection">
      <section class="content">
        <header class="content-header">
          <h2>{{ currentSection.title }}</h2>
          <p class="description">{{ currentSection.description }}</p>
          <div class="actions">
            <el-button type="primary" size="large" @click="handleAction(currentSection)">
              {{ currentSection.actionText }}
            </el-button>
            <el-button size="large" plain @click="showHint(currentSection)">
              操作指引
            </el-button>
          </div>
        </header>

        <section class="highlights" v-if="currentSection.highlights?.length">
          <article class="highlight-card" v-for="item in currentSection.highlights" :key="item.title">
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </article>
        </section>

        <section class="steps" v-if="currentSection.steps?.length">
          <h3>操作流程</h3>
          <ol>
            <li v-for="(step, index) in currentSection.steps" :key="index">
              <span class="index">{{ index + 1 }}</span>
              <span class="text">{{ step }}</span>
            </li>
          </ol>
        </section>

        <section class="tips" v-if="currentSection.tips?.length">
          <h3>贴心提示</h3>
          <ul>
            <li v-for="tip in currentSection.tips" :key="tip">{{ tip }}</li>
          </ul>
        </section>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import useAuth from '../composables/useAuth'

const router = useRouter()
const route = useRoute()

const sections = [
  {
    id: 'report',
    label: '报修',
    title: '在线报修',
    description: '快速提交设备故障信息，支持地点、描述、图片等多种附加信息，管理员可第一时间接收并派单。',
    highlights: [
      { title: '多渠道采集', desc: '支持手动选择设备或直接填写设备名称，避免信息缺失。' },
      { title: '实时通知', desc: '提交后自动通知管理员和维修工，全程保留消息记录。' }
    ],
    steps: [
      '填写报修信息并上传故障图片',
      '提交后等待管理员审核派单',
      '在仪表盘中实时查看处理进度'
    ],
    tips: [
      '如遇紧急情况，可在备注中说明优先级。',
      '上传清晰照片有助于维修工提前判断故障。'
    ],
    actionText: '立即创建报修单',
    requiresAuth: true,
    target: '/dashboard'
  },
  {
    id: 'tracking',
    label: '状态跟踪',
    title: '工单状态跟踪',
    description: '随时掌握报修单的受理、派工、维修、评价全流程状态，关键时间节点一目了然。',
    highlights: [
      { title: '时间轴视图', desc: '按照时间顺序展示工单进度，追踪节点更简单。' },
      { title: '消息提醒', desc: '状态变更自动提醒，避免错过维修反馈。' }
    ],
    steps: [
      '登录后进入仪表盘工单列表',
      '选择需要查看的工单了解最新状态',
      '对完成的工单进行确认或评价'
    ],
    tips: [
      '工单状态包含待受理、处理中、待评价、已完成。',
      '如长时间未更新，可联系管理员协调。'
    ],
    actionText: '前往查看进度',
    requiresAuth: true,
    target: '/dashboard'
  },
  {
    id: 'device',
    label: '设备',
    title: '设备资产管理',
    description: '集中管理校园公共设备，支持录入设备档案、维护状态和位置，帮助管理员掌握资产健康度。',
    highlights: [
      { title: '完整档案', desc: '记录设备型号、位置、采购时间等关键信息。' },
      { title: '状态更新', desc: '设备状态随报修及时更新，方便制定维护计划。' }
    ],
    steps: [
      '管理员登录后进入设备管理模块',
      '新增或编辑设备信息，维护资产档案',
      '结合报修记录评估设备健康状况'
    ],
    tips: [
      '建议按楼栋或功能分组管理设备，便于快速检索。'
    ],
    actionText: '维护设备档案',
    requiresAuth: true,
    target: '/dashboard'
  },
  {
    id: 'stats',
    label: '数据统计',
    title: '数据看板',
    description: '汇总维修量、响应时长、满意度等关键指标，为管理者提供决策依据，提升维修服务质量。',
    highlights: [
      { title: '多维统计', desc: '按时间、部门、设备类型等维度查看维修数据。' },
      { title: '趋势洞察', desc: '识别高频故障设备和薄弱环节，优化资源投入。' }
    ],
    steps: [
      '登录后打开仪表盘数据看板',
      '选择时间范围或筛选条件查看图表',
      '导出数据或制定维护计划'
    ],
    tips: [
      '定期回顾统计数据，有助于提前规划检修计划。'
    ],
    actionText: '查看统计报表',
    requiresAuth: true,
    target: '/dashboard'
  }
]

const activeSection = ref(sections[0].id)
const { isAuthenticated } = useAuth()

const currentSection = computed(() => sections.find(item => item.id === activeSection.value))

// sync activeSection with route query `section` so top bar can control it
onMounted(() => {
  const q = route.query.section
  if (q) activeSection.value = q
})

watch(
  () => route.query.section,
  (val) => {
    if (val) activeSection.value = val
  }
)

function handleAction (section) {
  if (section.requiresAuth && !isAuthenticated.value) {
    ElMessage.info('请先登录后使用该功能')
    router.push('/login')
    return
  }
  if (section.target) {
    router.push(section.target)
    return
  }
  ElMessage.info('功能即将上线')
}

function showHint (section) {
  const hint = section.tips?.[0] || '请在登录后按照页面指引操作'
  ElMessage.info(hint)
}

</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: #f4f6fb;
}

.nav { display: none }

.nav-item {
  padding: 10px 26px;
  border: 1px solid #d8dae2;
  border-radius: 10px;
  background: #ffffff;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.2s ease;
}

.nav-item:hover {
  border-color: #5c86ff;
  color: #5c86ff;
}

.nav-item.active {
  border-color: #2c64ff;
  background: #2c64ff;
  color: #ffffff;
  box-shadow: 0 6px 18px rgba(44, 100, 255, 0.18);
}

.home-body {
  flex: 1;
  padding: 48px 40px 80px;
  display: flex;
  justify-content: center;
}

.content {
  width: 100%;
  max-width: 1080px;
  background: #ffffff;
  border-radius: 20px;
  padding: 36px 40px;
  box-shadow: 0 16px 60px rgba(31, 47, 86, 0.08);
}

.content-header h2 {
  font-size: 28px;
  margin-bottom: 12px;
  color: #1f2f56;
}

.description {
  color: #61657a;
  line-height: 1.7;
  margin-bottom: 28px;
}

.actions {
  display: flex;
  gap: 12px;
  margin-bottom: 32px;
}

.highlights {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 36px;
}

.highlight-card {
  padding: 20px;
  border-radius: 14px;
  border: 1px solid #e3e6f0;
  background: linear-gradient(180deg, #f8f9ff 0%, #ffffff 100%);
}

.highlight-card h3 {
  margin-bottom: 10px;
  color: #2c64ff;
  font-size: 18px;
}

.highlight-card p {
  color: #555a6e;
  line-height: 1.6;
}

.steps h3,
.tips h3 {
  font-size: 20px;
  margin-bottom: 16px;
  color: #1f2f56;
}

.steps ol {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  gap: 16px;
}

.steps li {
  display: flex;
  align-items: center;
  background: #f4f6fb;
  padding: 14px 18px;
  border-radius: 12px;
  border: 1px solid #e2e6f4;
}

.steps .index {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #2c64ff;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  margin-right: 12px;
}

.steps .text {
  color: #44495c;
}

.tips ul {
  margin: 0;
  padding-left: 20px;
  color: #5a5f74;
  line-height: 1.6;
}

@media (max-width: 960px) {
  .home-header {
    flex-wrap: wrap;
    justify-content: center;
    gap: 12px 24px;
  }

  .nav {
    flex-wrap: wrap;
    justify-content: center;
  }

  .actions {
    flex-direction: column;
    align-items: flex-start;
  }
}

@media (max-width: 600px) {
  .home-header {
    padding: 20px;
  }

  .home-body {
    padding: 20px 16px 60px;
  }

  .content {
    padding: 24px 20px;
  }

  .nav-item {
    padding: 8px 18px;
  }
}
</style>
