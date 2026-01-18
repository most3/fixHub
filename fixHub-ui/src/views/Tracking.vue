<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>状态跟踪</h2>
        <p>实时查看报修单状态、维修进度与评价结果。</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="$router.push('/report')">新建报修</el-button>
      </div>
    </div>

    <el-card class="card mb-4">
      <el-form :inline="true" :model="filters" class="filter-bar">
        <el-form-item label="关键词">
          <el-input v-model="filters.keyword" placeholder="设备 / 单号 / 位置" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filters.status" placeholder="全部">
            <el-option label="全部" value="" />
            <el-option label="待受理" value="PENDING" />
            <el-option label="处理中" value="ASSIGNED" />
            <el-option label="待评价" value="REPAIRED" />
            <el-option label="已完成" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="filters.date" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button>重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="card">
      <template #header>
        <div class="card-header">
          <span>报修列表</span>
          <el-button text>导出</el-button>
        </div>
      </template>
      <el-table :data="orders" style="width:100%">
        <el-table-column prop="orderNo" label="报修单号" width="160" />
        <el-table-column prop="deviceName" label="设备名称" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="updatedAt" label="更新时间" width="180" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="statusMap[scope.row.status].type">{{ statusMap[scope.row.status].label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button link type="primary" @click="openDetail(scope.row)">详情</el-button>
            <el-button v-if="scope.row.status === 'REPAIRED'" link type="success" @click="openRate(scope.row)">评价</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-drawer v-model="detailVisible" size="40%" title="工单详情">
      <div class="drawer-section">
        <h4>当前进度</h4>
        <el-steps :active="detailStep" align-center>
          <el-step title="待受理" />
          <el-step title="处理中" />
          <el-step title="待评价" />
          <el-step title="已完成" />
        </el-steps>
      </div>
      <div class="drawer-section">
        <h4>工单信息</h4>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="报修单号">{{ active.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="设备名称">{{ active.deviceName }}</el-descriptions-item>
          <el-descriptions-item label="位置">{{ active.location }}</el-descriptions-item>
          <el-descriptions-item label="故障描述">{{ active.description }}</el-descriptions-item>
          <el-descriptions-item label="维修工">{{ active.technician }}</el-descriptions-item>
          <el-descriptions-item label="处理结果">{{ active.result }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>

    <el-dialog v-model="rateVisible" title="服务评价" width="420px">
      <el-form :model="rateForm" label-width="90px">
        <el-form-item label="评分">
          <el-rate v-model="rateForm.score" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="rateForm.comment" type="textarea" placeholder="请填写评价" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rateVisible = false">取消</el-button>
        <el-button type="primary">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'

const filters = reactive({ keyword: '', status: '', date: '' })

const statusMap = {
  PENDING: { label: '待受理', type: 'info' },
  ASSIGNED: { label: '处理中', type: 'warning' },
  REPAIRED: { label: '待评价', type: 'success' },
  CLOSED: { label: '已完成', type: 'primary' }
}

const orders = ref([
  { orderNo: 'RH20260116001', deviceName: '教学楼投影仪', location: 'A栋 201', updatedAt: '2026-01-16 09:30', status: 'PENDING', description: '无法开机', technician: '-', result: '-' },
  { orderNo: 'RH20260116002', deviceName: '会议室空调', location: '行政楼 3F', updatedAt: '2026-01-16 11:20', status: 'ASSIGNED', description: '制冷异常', technician: '张维修', result: '-' },
  { orderNo: 'RH20260115012', deviceName: '实验室打印机', location: 'B栋 105', updatedAt: '2026-01-15 17:10', status: 'REPAIRED', description: '卡纸', technician: '李维修', result: '清理卡纸并更换滚轴' },
  { orderNo: 'RH20260114007', deviceName: '网络交换机', location: 'C栋 1F', updatedAt: '2026-01-14 14:05', status: 'CLOSED', description: '频繁掉线', technician: '王维修', result: '更换交换模块' }
])

const detailVisible = ref(false)
const rateVisible = ref(false)
const active = ref({})

const rateForm = reactive({ score: 5, comment: '' })

function openDetail (row) {
  active.value = row
  detailVisible.value = true
}

function openRate (row) {
  active.value = row
  rateVisible.value = true
}

const detailStep = computed(() => {
  const map = { PENDING: 1, ASSIGNED: 2, REPAIRED: 3, CLOSED: 4 }
  return map[active.value.status] || 1
})
</script>

<style scoped>
.filter-bar{display:flex;flex-wrap:wrap;gap:12px}
.drawer-section{margin-bottom:24px}
.drawer-section h4{margin:0 0 12px 0}
</style>
