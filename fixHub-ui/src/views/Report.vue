<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h2>提交报修</h2>
        <p>填写设备信息、故障描述并上传图片，系统将自动流转至管理员派单。</p>
      </div>
      <div class="header-actions">
        <el-button @click="$router.push('/tracking')">查看报修进度</el-button>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="card">
          <template #header>
            <span>报修单信息</span>
          </template>
          <el-form :model="form" label-width="110px" class="form-grid">
            <el-form-item label="报修设备">
              <el-input v-model="form.deviceName" placeholder="如：投影仪 / 空调 / 打印机" />
            </el-form-item>
            <el-form-item label="设备编号">
              <el-input v-model="form.deviceCode" placeholder="例如：A-3F-PRJ-002" />
            </el-form-item>
            <el-form-item label="位置">
              <el-input v-model="form.location" placeholder="楼宇 / 房间 / 工位" />
            </el-form-item>
            <el-form-item label="故障类型">
              <el-select v-model="form.category" placeholder="请选择">
                <el-option label="硬件故障" value="hardware" />
                <el-option label="软件故障" value="software" />
                <el-option label="网络问题" value="network" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
            <el-form-item label="优先级">
              <el-radio-group v-model="form.priority">
                <el-radio-button label="紧急" />
                <el-radio-button label="普通" />
                <el-radio-button label="低" />
              </el-radio-group>
            </el-form-item>
            <el-form-item label="期望上门时间">
              <el-date-picker
                v-model="form.expectTime"
                type="datetime"
                placeholder="选择日期时间"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="故障描述">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="5"
                placeholder="请尽量详细描述故障现象" />
            </el-form-item>
            <el-form-item label="附件图片">
              <el-upload
                action="#"
                list-type="picture-card"
                :auto-upload="false"
              >
                <el-icon><Plus /></el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="联系人">
              <el-input v-model="form.contactName" placeholder="报修人姓名" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="form.contactPhone" placeholder="便于维修工联系" />
            </el-form-item>
            <el-form-item label="可联系时间">
              <el-select v-model="form.contactWindow" placeholder="请选择">
                <el-option label="工作日 9:00-12:00" value="am" />
                <el-option label="工作日 14:00-18:00" value="pm" />
                <el-option label="不限" value="any" />
              </el-select>
            </el-form-item>
          </el-form>
          <div class="form-actions">
            <el-button @click="reset">重置</el-button>
            <el-button type="primary">提交报修</el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="card">
          <template #header>
            <span>报修流程</span>
          </template>
          <el-steps direction="vertical" :active="1">
            <el-step title="提交报修" description="填写设备信息与故障描述" />
            <el-step title="管理员派单" description="自动分配维修工" />
            <el-step title="维修处理" description="维修工上门并填写结果" />
            <el-step title="确认评价" description="用户确认完成并评分" />
          </el-steps>
        </el-card>

        <el-card class="card mt-4">
          <template #header>
            <span>报修提示</span>
          </template>
          <ul class="tips">
            <li>上传图片有助于快速定位故障。</li>
            <li>紧急报修会触发加急派单。</li>
            <li>如设备无法开机，请备注电源状态。</li>
          </ul>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { Plus } from '@element-plus/icons-vue'

const form = reactive({
  deviceName: '',
  deviceCode: '',
  location: '',
  category: '',
  priority: '普通',
  expectTime: '',
  description: '',
  contactName: '',
  contactPhone: '',
  contactWindow: ''
})

function reset () {
  Object.assign(form, {
    deviceName: '',
    deviceCode: '',
    location: '',
    category: '',
    priority: '普通',
    expectTime: '',
    description: '',
    contactName: '',
    contactPhone: '',
    contactWindow: ''
  })
}
</script>

<style scoped>
.form-grid{display:grid;grid-template-columns:repeat(2,1fr);gap:12px 16px}
.form-grid :deep(.el-form-item){margin-bottom:0}
.form-grid :deep(.el-form-item:nth-child(7)),
.form-grid :deep(.el-form-item:nth-child(8)){
  grid-column:1 / -1;
}
.form-actions{display:flex;justify-content:flex-end;gap:12px;margin-top:16px}
.tips{padding-left:16px;color:#5a6572;line-height:1.8;margin:0}
</style>
