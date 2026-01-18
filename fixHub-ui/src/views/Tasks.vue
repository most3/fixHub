<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>维修工单</h2>
        <p>查看待接单、处理中与待评价工单，快速填写处理结果。</p>
      </div>
      <div class="header-actions">
        <el-button @click="$router.push('/profile')">完善资料</el-button>
      </div>
    </div>

    <el-row :gutter="20" class="mb-4">
      <el-col :span="8">
        <el-card class="stat-card red">
          <div class="stat-value">6</div>
          <div class="stat-label">待接单</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card yellow">
          <div class="stat-value">4</div>
          <div class="stat-label">处理中</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card green">
          <div class="stat-value">12</div>
          <div class="stat-label">本周完成</div>
        </el-card>
      </el-col>
    </el-row>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="待接单" name="pending">
        <el-table :data="pending" style="width:100%">
          <el-table-column prop="orderNo" label="报修单号" width="160" />
          <el-table-column prop="deviceName" label="设备" />
          <el-table-column prop="location" label="位置" />
          <el-table-column prop="createdAt" label="提交时间" width="160" />
          <el-table-column label="操作" width="140">
            <template #default>
              <el-button type="primary" size="small">接单</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="处理中" name="processing">
        <el-table :data="processing" style="width:100%">
          <el-table-column prop="orderNo" label="报修单号" width="160" />
          <el-table-column prop="deviceName" label="设备" />
          <el-table-column prop="assignedAt" label="开始时间" width="160" />
          <el-table-column label="操作" width="160">
            <template #default="scope">
              <el-button type="success" size="small" @click="openComplete(scope.row)">填写结果</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="待评价" name="review">
        <el-table :data="review" style="width:100%">
          <el-table-column prop="orderNo" label="报修单号" width="160" />
          <el-table-column prop="deviceName" label="设备" />
          <el-table-column prop="rating" label="用户评分" width="120" />
          <el-table-column prop="comment" label="评价内容" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="completeVisible" title="填写处理结果" width="520px">
      <el-form :model="completeForm" label-width="90px">
        <el-form-item label="处理结果">
          <el-input v-model="completeForm.result" type="textarea" placeholder="请描述维修处理情况" />
        </el-form-item>
        <el-form-item label="更换备件">
          <el-input v-model="completeForm.parts" placeholder="如：电源模块" />
        </el-form-item>
        <el-form-item label="处理耗时">
          <el-input v-model="completeForm.duration" placeholder="例如：1.5 小时" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completeVisible = false">取消</el-button>
        <el-button type="primary">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const activeTab = ref('pending')
const completeVisible = ref(false)
const completeForm = reactive({ result: '', parts: '', duration: '' })

const pending = ref([
  { orderNo: 'RH20260116003', deviceName: '会议室音响', location: 'A栋 2F', createdAt: '2026-01-16 10:00' },
  { orderNo: 'RH20260116004', deviceName: '实验室电脑', location: 'B栋 301', createdAt: '2026-01-16 10:20' }
])

const processing = ref([
  { orderNo: 'RH20260115009', deviceName: '投影仪', assignedAt: '2026-01-15 14:20' }
])

const review = ref([
  { orderNo: 'RH20260114003', deviceName: '空调', rating: '5分', comment: '响应及时，处理到位。' }
])

function openComplete () {
  completeVisible.value = true
}
</script>
