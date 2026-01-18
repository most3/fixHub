<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>设备管理</h2>
        <p>维护学校公共设备信息，支持导入、分类与维护计划。</p>
      </div>
      <div class="header-actions">
        <el-button @click="dialogVisible = true">新增设备</el-button>
        <el-button type="primary">批量导入</el-button>
      </div>
    </div>

    <el-card class="card mb-4">
      <el-form :inline="true" :model="filters" class="filter-bar">
        <el-form-item label="关键词">
          <el-input v-model="filters.keyword" placeholder="名称/编号/位置" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="filters.category" placeholder="全部">
            <el-option label="全部" value="" />
            <el-option label="教学设备" value="teaching" />
            <el-option label="办公设备" value="office" />
            <el-option label="网络设备" value="network" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filters.status" placeholder="全部">
            <el-option label="全部" value="" />
            <el-option label="正常" value="normal" />
            <el-option label="待维修" value="repair" />
            <el-option label="停用" value="offline" />
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
          <span>设备列表</span>
          <el-button text>导出</el-button>
        </div>
      </template>
      <el-table :data="devices" style="width:100%">
        <el-table-column prop="code" label="设备编号" width="160" />
        <el-table-column prop="name" label="设备名称" />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="statusColor[scope.row.status]">{{ statusText[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="最后维护" width="160" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button link type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button link type="warning">维护计划</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="设备信息" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="设备名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="设备编号">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category">
            <el-option label="教学设备" value="教学设备" />
            <el-option label="办公设备" value="办公设备" />
            <el-option label="网络设备" value="网络设备" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="正常" value="normal" />
            <el-option label="待维修" value="repair" />
            <el-option label="停用" value="offline" />
          </el-select>
        </el-form-item>
        <el-form-item label="维护周期">
          <el-select v-model="form.cycle">
            <el-option label="每月" value="monthly" />
            <el-option label="每季度" value="quarter" />
            <el-option label="每半年" value="half" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const filters = reactive({ keyword: '', category: '', status: '' })
const dialogVisible = ref(false)
const form = reactive({ name: '', code: '', category: '', location: '', status: 'normal', cycle: 'monthly' })

const statusText = { normal: '正常', repair: '待维修', offline: '停用' }
const statusColor = { normal: 'success', repair: 'warning', offline: 'info' }

const devices = ref([
  { code: 'A-3F-PRJ-002', name: '投影仪', category: '教学设备', location: 'A栋 3F 305', status: 'normal', updatedAt: '2026-01-10' },
  { code: 'B-1F-AC-008', name: '空调', category: '教学设备', location: 'B栋 1F 101', status: 'repair', updatedAt: '2026-01-12' },
  { code: 'C-2F-SW-011', name: '交换机', category: '网络设备', location: 'C栋 2F 机房', status: 'normal', updatedAt: '2026-01-08' },
  { code: 'ADMIN-PRT-003', name: '多功能打印机', category: '办公设备', location: '行政楼 2F', status: 'offline', updatedAt: '2025-12-30' }
])

function edit (row) {
  Object.assign(form, row)
  dialogVisible.value = true
}
</script>
