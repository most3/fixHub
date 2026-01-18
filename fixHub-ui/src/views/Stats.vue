<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>数据看板</h2>
        <p>统计维修量、响应效率、满意度与设备故障类型分布。</p>
      </div>
      <div class="header-actions">
        <el-date-picker v-model="range" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" />
      </div>
    </div>

    <el-row :gutter="20" class="mb-4">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">268</div>
          <div class="stat-label">本月维修量</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">3.8h</div>
          <div class="stat-label">平均响应时间</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">95%</div>
          <div class="stat-label">满意度</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">42</div>
          <div class="stat-label">待处理工单</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="card">
          <template #header>
            <span>维修量趋势</span>
          </template>
          <div class="chart-placeholder">
            <div class="bar" v-for="n in 10" :key="n" :style="{ height: `${20 + n * 6}px` }"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="card">
          <template #header>
            <span>故障类型分布</span>
          </template>
          <div class="pie-placeholder">
            <div class="pie-item" v-for="item in faultTypes" :key="item.name">
              <span class="dot" :style="{ background: item.color }"></span>
              <span>{{ item.name }}</span>
              <strong>{{ item.value }}%</strong>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-4">
      <el-col :span="12">
        <el-card class="card">
          <template #header>
            <span>维修工绩效排行</span>
          </template>
          <el-table :data="ranking" style="width:100%">
            <el-table-column prop="name" label="维修工" />
            <el-table-column prop="count" label="完成量" width="120" />
            <el-table-column prop="rating" label="满意度" width="120" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="card">
          <template #header>
            <span>平均响应时长</span>
          </template>
          <div class="line-placeholder">
            <div class="line"></div>
            <div class="line"></div>
            <div class="line"></div>
          </div>
          <div class="line-labels">
            <span>教学设备 3.2h</span>
            <span>办公设备 4.1h</span>
            <span>网络设备 2.6h</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const range = ref('')
const faultTypes = ref([
  { name: '硬件故障', value: 40, color: '#5b8ff9' },
  { name: '软件故障', value: 28, color: '#61d9a8' },
  { name: '网络问题', value: 18, color: '#f6bd16' },
  { name: '其他', value: 14, color: '#e86452' }
])

const ranking = ref([
  { name: '张维修', count: 36, rating: '4.9' },
  { name: '李维修', count: 32, rating: '4.8' },
  { name: '王维修', count: 29, rating: '4.7' }
])
</script>

<style scoped>
.chart-placeholder{height:200px;display:flex;align-items:flex-end;gap:8px}
.chart-placeholder .bar{flex:1;background:linear-gradient(180deg,#6a9eff,#2c64ff);border-radius:6px}
.pie-placeholder{display:flex;flex-direction:column;gap:12px}
.pie-item{display:flex;align-items:center;justify-content:space-between}
.dot{width:10px;height:10px;border-radius:50%;margin-right:8px;display:inline-block}
.line-placeholder{height:120px;background:#f5f7fb;border-radius:8px;position:relative;overflow:hidden}
.line-placeholder .line{position:absolute;left:0;right:0;height:2px;background:#2c64ff;opacity:0.2}
.line-placeholder .line:nth-child(1){top:20px}
.line-placeholder .line:nth-child(2){top:60px}
.line-placeholder .line:nth-child(3){top:100px}
.line-labels{display:flex;justify-content:space-between;margin-top:8px;color:#6b7280}
</style>
