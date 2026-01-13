<template>
  <div class="home-container">
    <!-- 报修用户（师生）首页 -->
    <div v-if="isReporter" class="role-dashboard reporter-dashboard">
      <!-- 顶部：快速报修 -->
      <el-row :gutter="20" class="mb-4">
        <el-col :span="24">
          <el-card shadow="hover" class="quick-action-card" @click="handleQuickRepair">
            <div class="card-content">
              <el-icon :size="40" color="#409EFF"><Tools /></el-icon>
              <div class="text-info">
                <h3>一键提交报修单</h3>
                <p>设备故障？点击这里快速提交报修申请</p>
              </div>
              <el-icon :size="24" class="arrow-icon"><ArrowRight /></el-icon>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 中部：我的报修单 -->
      <el-card class="box-card mb-4">
        <template #header>
          <div class="card-header">
            <span>我的报修单</span>
            <el-button text @click="fetchMyOrders">刷新</el-button>
          </div>
        </template>
        <el-table :data="myOrders" style="width: 100%" v-loading="loading">
          <el-table-column prop="orderNo" label="报修单号" width="180" />
          <el-table-column prop="deviceName" label="设备名称" />
          <el-table-column prop="location" label="位置" />
          <el-table-column prop="createdAt" label="提交时间" width="180">
            <template #default="scope">{{ formatDate(scope.row.createdAt) }}</template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="viewDetail(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 底部：常见问题 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="box-card">
            <template #header><span>常见问题</span></template>
            <ul class="faq-list">
              <li>报修后多久受理？</li>
              <li>如何查询设备编号？</li>
              <li>维修完成后如何评价？</li>
            </ul>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="box-card" shadow="hover" @click="handleDeviceQuery">
            <div class="simple-card-content">
              <el-icon><Search /></el-icon>
              <span>学校公共设备名录查询</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 维修工首页 -->
    <div v-if="isTechnician" class="role-dashboard technician-dashboard">
      <!-- 顶部：统计看板 -->
      <el-row :gutter="20" class="mb-4">
        <el-col :span="8">
          <el-card shadow="hover" class="stat-card red">
            <div class="stat-value">{{ stats.pendingOrders || 0 }}</div>
            <div class="stat-label">今日待接单</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="stat-card yellow">
            <div class="stat-value">{{ assignedOrders.length }}</div> <!-- 暂用列表长度代替 -->
            <div class="stat-label">处理中工单</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="stat-card green">
            <div class="stat-value">{{ stats.completedOrders || 0 }}</div>
            <div class="stat-label">今日完成数</div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 中部：待接单（模拟数据，因为API没提供专门的待接单接口，暂时用所有Pending状态的） -->
      <el-card class="box-card mb-4">
        <template #header>
          <div class="card-header">
            <span>待接单工单</span>
            <el-button text @click="fetchPendingOrders">刷新</el-button>
          </div>
        </template>
        <el-table :data="pendingOrders" style="width: 100%" v-loading="loading">
          <el-table-column prop="orderNo" label="报修单号" width="150" />
          <el-table-column prop="deviceName" label="设备名称" />
          <el-table-column prop="description" label="故障描述" />
          <el-table-column prop="createdAt" label="提交时间" width="180">
            <template #default="scope">{{ formatDate(scope.row.createdAt) }}</template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleAccept(scope.row)">接单</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 中下部：处理中 -->
      <el-card class="box-card">
        <template #header><span>处理中工单</span></template>
        <el-table :data="assignedOrders" style="width: 100%" v-loading="loading">
          <el-table-column prop="orderNo" label="报修单号" width="150" />
          <el-table-column prop="deviceName" label="设备名称" />
          <el-table-column prop="assignedAt" label="开始处理时间" width="180">
            <template #default="scope">{{ formatDate(scope.row.assignedAt) }}</template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button type="success" size="small" @click="handleComplete(scope.row)">填写处理结果</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 管理员首页 -->
    <div v-if="isAdmin" class="role-dashboard admin-dashboard">
      <!-- 顶部：数据看板 -->
      <el-row :gutter="20" class="mb-4">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-value">{{ stats.totalOrders || 0 }}</div>
            <div class="stat-label">今日报修总量</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-value">{{ stats.pendingOrders || 0 }}</div>
            <div class="stat-label">待派单工单</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-value">{{ stats.completedOrders || 0 }}</div>
            <div class="stat-label">今日完成工单</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-value">{{ stats.averageRating || 0 }}</div>
            <div class="stat-label">平均响应时间(h)</div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 中部：待派单 -->
      <el-card class="box-card mb-4">
        <template #header>
          <div class="card-header">
            <span>待派单工单</span>
            <el-button text @click="fetchPendingOrders">刷新</el-button>
          </div>
        </template>
        <el-table :data="pendingOrders" style="width: 100%" v-loading="loading">
          <el-table-column prop="orderNo" label="报修单号" width="150" />
          <el-table-column prop="deviceName" label="设备名称" />
          <el-table-column prop="reporter.displayName" label="报修人" />
          <el-table-column prop="location" label="位置" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button type="warning" size="small" @click="handleAssign(scope.row)">派单</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 底部：快捷管理 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover" class="manage-card">设备管理</el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="manage-card">人员管理</el-card>
        </el-col>
        <el-col :span="8">
            <el-card shadow="hover" class="manage-card" @click="handleCreateUser">账号生成</el-card>
        </el-col>
      </el-row>
    </div>

      <!-- 访客 / 未登录用户展示（避免空白页面） -->
      <div v-else class="role-dashboard guest-dashboard">
        <el-row :gutter="20" class="mb-4">
          <el-col :span="24">
            <el-card class="card">
              <h3>欢迎使用校园设备报修平台</h3>
              <p>请登录或注册以提交报修或查看工单；管理员 / 维修工请使用分配账号登录。</p>
              <div style="margin-top:12px">
                <el-button type="primary" @click="$router.push('/login')">登录</el-button>
                <el-button type="default" @click="$router.push('/register')" style="margin-left:8px">注册</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="box-card">
              <template #header><span>平台简介</span></template>
              <p>本平台为师生提供便捷的设备报修通道；维修工和管理员账号由学校或系统管理员统一下发与管理。</p>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="box-card">
              <template #header><span>常见问题</span></template>
              <ul class="faq-list">
                <li>如何获取维修工账号？联系管理员由其创建并下发账号。</li>
                <li>忘记密码怎么办？使用找回密码流程。</li>
              </ul>
            </el-card>
          </el-col>
        </el-row>
      </div>

    <!-- 弹窗：快速报修 -->
    <el-dialog v-model="repairDialogVisible" title="提交报修单" width="500px">
      <el-form :model="repairForm" label-width="80px">
        <el-form-item label="设备名称">
          <el-input v-model="repairForm.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备位置">
          <el-input v-model="repairForm.location" placeholder="请输入设备位置" />
        </el-form-item>
        <el-form-item label="故障描述">
          <el-input v-model="repairForm.description" type="textarea" placeholder="请详细描述故障情况" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="repairDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRepair">提交</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 弹窗：处理结果 -->
    <el-dialog v-model="completeDialogVisible" title="填写处理结果" width="500px">
      <el-form :model="completeForm" label-width="80px">
        <el-form-item label="处理结果">
          <el-input v-model="completeForm.resultDesc" type="textarea" placeholder="请描述维修结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="completeDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitComplete">提交</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 弹窗：创建用户 -->
    <el-dialog v-model="createUserDialogVisible" title="创建用户" width="500px">
      <el-form :model="createUserForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="createUserForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="createUserForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="显示名">
          <el-input v-model="createUserForm.displayName" placeholder="请输入显示名" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="createUserForm.role" placeholder="请选择角色">
            <el-option label="维修工" value="TECHNICIAN" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="createUserDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitCreateUser">创建</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Tools, ArrowRight, Search } from '@element-plus/icons-vue'
import useAuth from '../composables/useAuth'
import { createOrder, getMyOrders, getAssignedOrders, getAllOrders, assignOrder, completeOrder } from '../api/repair'
import { getDashboardStats } from '../api/stats'
import { createUser } from '../api/user'

const { user } = useAuth()
const loading = ref(false)

// 角色判断
const isReporter = computed(() => user.value?.role === 'REPORTER')
const isTechnician = computed(() => user.value?.role === 'TECHNICIAN')
const isAdmin = computed(() => user.value?.role === 'ADMIN')

// 数据
const myOrders = ref([])
const pendingOrders = ref([])
const assignedOrders = ref([])
const stats = ref({})

// 表单
const repairDialogVisible = ref(false)
const repairForm = ref({ deviceName: '', location: '', description: '' })

const completeDialogVisible = ref(false)
const currentOrder = ref(null)
const completeForm = ref({ resultDesc: '' })

// 创建用户表单
const createUserDialogVisible = ref(false)
const createUserForm = ref({ username: '', password: '', displayName: '', role: 'TECHNICIAN' })

function handleCreateUser() {
  createUserForm.value = { username: '', password: '', displayName: '', role: 'TECHNICIAN' }
  createUserDialogVisible.value = true
}

async function submitCreateUser() {
  if (!createUserForm.value.username || !createUserForm.value.password) {
    ElMessage.warning('请填写用户名和密码')
    return
  }
  try {
    await createUser(createUserForm.value)
    ElMessage.success('用户创建成功')
    createUserDialogVisible.value = false
  } catch (e) {
    console.error(e)
    ElMessage.error(e.humanMessage || e.response?.data?.message || e.message || '创建失败')
  }
}

// 初始化加载
onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    if (isReporter.value) {
      await fetchMyOrders()
    } else if (isTechnician.value) {
      await fetchTechnicianData()
    } else if (isAdmin.value) {
      await fetchAdminData()
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('数据加载失败')
  } finally {
    loading.value = false
  }
}

async function fetchMyOrders() {
  if (user.value?.id) {
    myOrders.value = await getMyOrders(user.value.id)
  }
}

async function fetchTechnicianData() {
  if (user.value?.id) {
    assignedOrders.value = await getAssignedOrders(user.value.id)
    // 模拟获取待接单（实际应有专门接口，这里取所有Pending的演示）
    const all = await getAllOrders()
    pendingOrders.value = all.filter(o => o.status === 'PENDING')
    
    // 获取统计
    try {
      stats.value = await getDashboardStats()
    } catch (e) { /* ignore */ }
  }
}

async function fetchAdminData() {
  const all = await getAllOrders()
  pendingOrders.value = all.filter(o => o.status === 'PENDING')
  try {
    stats.value = await getDashboardStats()
  } catch (e) { /* ignore */ }
}

// 报修用户操作
function handleQuickRepair() {
  repairDialogVisible.value = true
}

async function submitRepair() {
  try {
    await createOrder(user.value.id, repairForm.value)
    ElMessage.success('报修提交成功')
    repairDialogVisible.value = false
    repairForm.value = { deviceName: '', location: '', description: '' }
    fetchMyOrders()
  } catch (e) {
    ElMessage.error('提交失败')
  }
}

function handleDeviceQuery() {
  ElMessage.info('功能开发中...')
}

// 维修工操作
async function handleAccept(order) {
  try {
    await ElMessageBox.confirm('确认接单吗？', '提示', { type: 'info' })
    await assignOrder(order.id, user.value.id)
    ElMessage.success('接单成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('接单失败')
  }
}

function handleComplete(order) {
  currentOrder.value = order
  completeDialogVisible.value = true
}

async function submitComplete() {
  try {
    await completeOrder(currentOrder.value.id, user.value.id, completeForm.value.resultDesc)
    ElMessage.success('工单已完成')
    completeDialogVisible.value = false
    completeForm.value = { resultDesc: '' }
    loadData()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

// 管理员操作
function handleAssign(order) {
  ElMessage.info('请使用维修工账号登录演示接单，或后续完善派单弹窗')
}

// 工具函数
function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}

function getStatusText(status) {
  const map = {
    'PENDING': '待受理',
    'ASSIGNED': '处理中',
    'REPAIRED': '待确认',
    'CLOSED': '已完成'
  }
  return map[status] || status
}

function getStatusType(status) {
  const map = {
    'PENDING': 'primary',
    'ASSIGNED': 'warning',
    'REPAIRED': 'success',
    'CLOSED': 'info'
  }
  return map[status] || ''
}

function viewDetail(row) {
  ElMessage.info(`查看详情: ${row.orderNo}`)
}

// 刷新待接单
async function fetchPendingOrders() {
  const all = await getAllOrders()
  pendingOrders.value = all.filter(o => o.status === 'PENDING')
}
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.mb-4 {
  margin-bottom: 20px;
}

/* 快速报修卡片 */
.quick-action-card {
  cursor: pointer;
  transition: all 0.3s;
  background: linear-gradient(135deg, #e6f7ff 0%, #ffffff 100%);
}
.quick-action-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.card-content {
  display: flex;
  align-items: center;
  padding: 10px;
}
.text-info {
  flex: 1;
  margin-left: 20px;
}
.text-info h3 {
  margin: 0 0 5px 0;
  font-size: 18px;
  color: #303133;
}
.text-info p {
  margin: 0;
  color: #909399;
}

/* 统计卡片 */
.stat-card {
  text-align: center;
  padding: 10px 0;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 5px;
}
.stat-label {
  color: #909399;
  font-size: 14px;
}
.stat-card.red .stat-value { color: #F56C6C; }
.stat-card.yellow .stat-value { color: #E6A23C; }
.stat-card.green .stat-value { color: #67C23A; }

/* 常见问题 */
.faq-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.faq-list li {
  padding: 8px 0;
  border-bottom: 1px solid #EBEEF5;
  color: #606266;
  cursor: pointer;
}
.faq-list li:hover {
  color: #409EFF;
}

.simple-card-content {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100px;
  font-size: 16px;
  color: #606266;
  gap: 10px;
}

.manage-card {
  text-align: center;
  padding: 20px;
  cursor: pointer;
  font-weight: bold;
  color: #303133;
}
.manage-card:hover {
  color: #409EFF;
  background-color: #f5f7fa;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
