<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>派单管理</h2>
        <p>管理员对待受理工单进行派单与优先级管理。</p>
      </div>
      <div class="header-actions">
        <el-button type="primary">导出工单</el-button>
      </div>
    </div>

    <el-card class="card mb-4">
      <el-form :inline="true" :model="filters" class="filter-bar">
        <el-form-item label="关键词">
          <el-input v-model="filters.keyword" placeholder="设备/单号/报修人" />
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="filters.priority" placeholder="全部">
            <el-option label="全部" value="" />
            <el-option label="紧急" value="HIGH" />
            <el-option label="普通" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filters.status" placeholder="全部">
            <el-option label="全部" value="" />
            <el-option label="待受理" value="PENDING" />
            <el-option label="处理中" value="ASSIGNED" />
          </el-select>
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
          <span>待派单工单</span>
          <el-button text>批量派单</el-button>
        </div>
      </template>
      <el-table :data="orders" style="width:100%">
        <el-table-column prop="orderNo" label="报修单号" width="160" />
        <el-table-column prop="deviceName" label="设备" />
        <el-table-column prop="reporter" label="报修人" width="120" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="scope">
            <el-tag :type="priorityMap[scope.row.priority]">{{ priorityText[scope.row.priority] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template #default="scope">
            <el-button type="primary" size="small" @click="openAssign(scope.row)">派单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-drawer v-model="assignVisible" size="30%" title="派单设置">
      <el-form :model="assignForm" label-width="90px">
        <el-form-item label="工单号">
          <el-input v-model="assignForm.orderNo" disabled />
        </el-form-item>
        <el-form-item label="维修工">
          <el-select v-model="assignForm.technician">
            <el-option label="张维修" value="zhang" />
            <el-option label="李维修" value="li" />
            <el-option label="王维修" value="wang" />
          </el-select>
        </el-form-item>
        <el-form-item label="到场时限">
          <el-select v-model="assignForm.sla">
            <el-option label="2小时" value="2h" />
            <el-option label="4小时" value="4h" />
            <el-option label="8小时" value="8h" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="assignForm.note" type="textarea" />
        </el-form-item>
      </el-form>
      <div class="drawer-actions">
        <el-button @click="assignVisible = false">取消</el-button>
        <el-button type="primary">确认派单</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const filters = reactive({ keyword: '', priority: '', status: '' })
const assignVisible = ref(false)

const priorityText = { HIGH: '紧急', MEDIUM: '普通', LOW: '低' }
const priorityMap = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }

const orders = ref([
  { orderNo: 'RH20260116005', deviceName: '会议室投影仪', reporter: '王老师', location: '综合楼 402', priority: 'HIGH' },
  { orderNo: 'RH20260116006', deviceName: '实验室打印机', reporter: '李同学', location: '实验楼 102', priority: 'MEDIUM' }
])

const assignForm = reactive({ orderNo: '', technician: '', sla: '4h', note: '' })

function openAssign (row) {
  assignForm.orderNo = row.orderNo
  assignVisible.value = true
}
</script>

<style scoped>
.drawer-actions{display:flex;justify-content:flex-end;gap:12px;margin-top:16px}
</style>
