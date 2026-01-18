<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>人员管理</h2>
        <p>管理员创建并分发账号，维修工完善个人信息。</p>
      </div>
      <div class="header-actions">
        <el-button @click="openCreate">生成随机账号</el-button>
        <el-button type="primary">批量导入</el-button>
      </div>
    </div>

    <el-card class="card mb-4">
      <el-form :inline="true" :model="filters" class="filter-bar">
        <el-form-item label="关键词">
          <el-input v-model="filters.keyword" placeholder="姓名/账号/手机号" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="filters.role" placeholder="全部">
            <el-option label="全部" value="" />
            <el-option label="管理员" value="ADMIN" />
            <el-option label="维修工" value="TECHNICIAN" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filters.status" placeholder="全部">
            <el-option label="全部" value="" />
            <el-option label="待完善" value="pending" />
            <el-option label="正常" value="active" />
            <el-option label="停用" value="inactive" />
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
          <span>人员列表</span>
          <el-button text>导出</el-button>
        </div>
      </template>
      <el-table :data="staffs" style="width:100%">
        <el-table-column prop="username" label="账号" width="160" />
        <el-table-column prop="displayName" label="姓名" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.role === 'ADMIN' ? 'primary' : 'success'">
              {{ scope.row.role === 'ADMIN' ? '管理员' : '维修工' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="160" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="statusMap[scope.row.status].type">{{ statusMap[scope.row.status].label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default>
            <el-button link type="primary">编辑</el-button>
            <el-button link type="warning">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="生成随机账号" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="角色">
          <el-select v-model="form.role">
            <el-option label="维修工" value="TECHNICIAN" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="显示名">
          <el-input v-model="form.displayName" placeholder="例如：张维修" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="账号预览">
          <el-input v-model="form.preview" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary">生成并下发</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const filters = reactive({ keyword: '', role: '', status: '' })
const dialogVisible = ref(false)

const form = reactive({
  role: 'TECHNICIAN',
  displayName: '',
  phone: '',
  preview: 'tech-20260116-AX21'
})

const statusMap = {
  pending: { label: '待完善', type: 'warning' },
  active: { label: '正常', type: 'success' },
  inactive: { label: '停用', type: 'info' }
}

const staffs = ref([
  { username: 'tech-AX21', displayName: '张维修', role: 'TECHNICIAN', phone: '138****1122', status: 'active' },
  { username: 'tech-BK12', displayName: '李维修', role: 'TECHNICIAN', phone: '139****2211', status: 'pending' },
  { username: 'admin-001', displayName: '系统管理员', role: 'ADMIN', phone: '137****0001', status: 'active' }
])

function openCreate () {
  dialogVisible.value = true
}
</script>
