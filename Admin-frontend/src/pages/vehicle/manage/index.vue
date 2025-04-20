<template>
    <t-card :bordered="false">
      <div class="vehicle-list-container">
        <!-- 顶部操作区 -->
        <t-row justify="space-between">
          <div class="left-operation-container">
            <t-button @click="handleAddVehicle">添加车辆</t-button>
            <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length" @click="handleBatchRepair">
              批量报修
            </t-button>
            <p v-if="!!selectedRowKeys.length" class="selected-count">已选{{ selectedRowKeys.length }}项</p>
          </div>
          <div class="search-area">
            <t-select v-model="filterStatus" class="status-filter" placeholder="状态筛选">
              <t-option v-for="status in statusOptions" :key="status.value" :value="status.value" :label="status.label" />
            </t-select>
            <t-input v-model="searchValue" class="search-input" placeholder="请输入车辆编号搜索" clearable>
              <template #suffix-icon><search-icon size="20px" /></template>
            </t-input>
          </div>
        </t-row>
  
        <!-- 表格组件 -->
        <div class="table-container">
          <t-table
            :data="data"
            :columns="columns"
            :rowKey="rowKey"
            :verticalAlign="verticalAlign"
            :hover="hover"
            :pagination="pagination"
            :loading="dataLoading"
            :selected-row-keys="selectedRowKeys"
            @select-change="handleSelectChange"
            @page-change="handlePageChange"
            @change="handleChange"
            :headerAffixedTop="true"
            :headerAffixProps="{ offsetTop, container: getContainer }"
          >
            <!-- 状态列自定义 -->
            <template #status="{ row }">
              <t-tag v-if="row.status === VEHICLE_STATUS.IDLE" theme="success" variant="light">空闲</t-tag>
              <t-tag v-if="row.status === VEHICLE_STATUS.IN_USE" theme="warning" variant="light">使用中</t-tag>
              <t-tag v-if="row.status === VEHICLE_STATUS.FAULT" theme="danger" variant="light">故障</t-tag>
            </template>
  
            <!-- 电量列自定义 -->
            <template #battery="{ row }">
              <t-progress
                :percentage="row.battery"
                :color="getBatteryColor(row.battery)"
                :label="false"
                trackColor="#e8f4ff"
              />
              <span style="margin-left: 4px">{{ row.battery }}%</span>
            </template>
  
            <!-- 操作列自定义 -->
            <template #op="slotProps">
              <a class="t-button-link" @click="handleEdit(slotProps.row)">编辑</a>
              <a class="t-button-link" @click="handleRepair(slotProps.row)">报修</a>
              <a class="t-button-link" @click="handleDelete(slotProps)">删除</a>
            </template>
          </t-table>
        </div>
  
        <!-- 添加/编辑车辆弹窗 -->
        <t-drawer
          :visible.sync="formVisible"
          :header="formTitle"
          :footer="false"
          :size="'500px'"
          :close-btn="true"
          :on-close="onDialogClose"
        >
          <t-form
            :data="vehicleForm"
            :rules="rules"
            ref="vehicleFormRef"
            :label-width="100"
            @submit="onFormSubmit"
          >
            <t-form-item label="车辆编号" name="scooterCode">
              <t-input v-model="vehicleForm.scooterCode" placeholder="请输入车辆编号" />
            </t-form-item>
            <t-form-item label="所属城市" name="city">
              <t-select v-model="vehicleForm.city" :options="CITY_OPTIONS" placeholder="请选择所属城市" />
            </t-form-item>
            <t-form-item label="具体位置" name="location">
              <t-input v-model="vehicleForm.location" placeholder="请输入具体位置" />
            </t-form-item>
            <t-form-item label="电量" name="battery">
              <t-input-number v-model="vehicleForm.battery" :min="0" :max="100" />
              <span style="margin-left: 8px">%</span>
            </t-form-item>
            <t-form-item label="车辆状态" name="status">
              <t-select v-model="vehicleForm.status" :options="VEHICLE_STATUS_OPTIONS" placeholder="请选择车辆状态" />
            </t-form-item>
            <t-form-item>
              <t-space>
                <t-button theme="primary" type="submit">提交</t-button>
                <t-button theme="default" variant="base" @click="onDialogClose">取消</t-button>
              </t-space>
            </t-form-item>
          </t-form>
        </t-drawer>
  
        <!-- 删除确认弹窗 -->
        <t-dialog
          header="确认删除该车辆？"
          :body="confirmBody"
          :visible.sync="confirmVisible"
          @confirm="onConfirmDelete"
          @cancel="onCancel"
        >
        </t-dialog>
  
        <!-- 批量报修弹窗 -->
        <t-dialog
          header="批量报修"
          :visible.sync="batchRepairVisible"
          @confirm="onBatchRepairConfirm"
          @cancel="batchRepairVisible = false"
        >
          <p>确认将选中的 {{ selectedRowKeys.length }} 辆车标记为故障状态？</p>
        </t-dialog>
      </div>
    </t-card>
  </template>
  
<script lang="ts">
import Vue from 'vue';
import { SearchIcon } from 'tdesign-icons-vue';

// 车辆状态常量
const VEHICLE_STATUS = {
  IDLE: 0,   // 空闲
  IN_USE: 1, // 使用中
  FAULT: 2,  // 故障
};

// 车辆状态选项
const VEHICLE_STATUS_OPTIONS = [
  { label: '空闲', value: VEHICLE_STATUS.IDLE },
  { label: '使用中', value: VEHICLE_STATUS.IN_USE },
  { label: '故障', value: VEHICLE_STATUS.FAULT },
];

// 城市选项（示例数据）
const CITY_OPTIONS = [
  { label: '北京', value: '北京' },
  { label: '上海', value: '上海' },
  { label: '广州', value: '广州' },
  { label: '深圳', value: '深圳' },
  { label: '杭州', value: '杭州' },
];

export default Vue.extend({
  name: 'VehicleManagement',
  components: {
    SearchIcon,
  },
  data() {
    const defaultVehicleForm = {
      id: null,
      scooterCode: '',
      city: '',
      location: '',
      battery: 100,
      status: VEHICLE_STATUS.IDLE,
      lastRentTime: '',
      mileage: 0,
    };

    return {
      VEHICLE_STATUS,
      VEHICLE_STATUS_OPTIONS,
      CITY_OPTIONS,
      
      // 筛选表单数据
      formData: {
        scooterCode: '',
        city: undefined,
        status: undefined,
      },
      
      // 表格相关数据
      dataLoading: false,
      data: [],
      selectedRowKeys: [],
      columns: [
        { colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left' },
        { title: '车辆编号', colKey: 'scooterCode', width: 120, fixed: 'left' },
        { title: '所属城市', colKey: 'city', width: 120 },
        { title: '具体位置', colKey: 'location', width: 180, ellipsis: true },
        { title: '电量', colKey: 'battery', width: 150, cell: { col: 'battery' } },
        { title: '当前状态', colKey: 'status', width: 120, cell: { col: 'status' } },
        { title: '最近租用时间', colKey: 'lastRentTime', width: 180 },
        { title: '操作', colKey: 'op', fixed: 'right', width: 180 },
      ],
      pagination: {
        defaultPageSize: 10,
        total: 0,
        defaultCurrent: 1,
      },
      rowKey: 'id',
      verticalAlign: 'middle',
      hover: true,
      
      // 弹窗相关
      formVisible: false,
      formTitle: '添加车辆',
      isEdit: false,
      vehicleForm: { ...defaultVehicleForm },
      defaultVehicleForm,
      rules: {
        scooterCode: [{ required: true, message: '请输入车辆编号', type: 'error' }],
        city: [{ required: true, message: '请选择所属城市', type: 'error' }],
        battery: [{ required: true, message: '请输入电量', type: 'error' }],
        status: [{ required: true, message: '请选择车辆状态', type: 'error' }],
      },
      confirmVisible: false,
      deleteIdx: -1,
      batchRepairVisible: false,
      filterStatus: '',
      searchValue: '',
      searchTimer: null,
      
      // 添加状态选项
      statusOptions: [
        { value: '', label: '全部' },
        { value: VEHICLE_STATUS.IDLE, label: '空闲' },
        { value: VEHICLE_STATUS.IN_USE, label: '使用中' },
        { value: VEHICLE_STATUS.FAULT, label: '故障' },
      ],
    };
  },
  
  computed: {
    confirmBody() {
      if (this.deleteIdx > -1) {
        const vehicle = this.data?.[this.deleteIdx];
        return `删除后，车辆 ${vehicle.scooterCode} 的所有信息将被清空，且无法恢复`;
      }
      return '';
    },
    offsetTop() {
      return this.$store.state.setting.isUseTabsRouter ? 48 : 0;
    },
  },
  
  watch: {
    filterStatus() {
      this.fetchData();
    },
    searchValue() {
      if (this.searchTimer) {
        clearTimeout(this.searchTimer);
      }
      this.searchTimer = setTimeout(() => {
        this.fetchData();
      }, 300);
    }
  },
  
  mounted() {
    this.fetchData();
  },
  
  beforeDestroy() {
    if (this.searchTimer) {
      clearTimeout(this.searchTimer);
    }
  },
  
  methods: {
    // 获取容器元素
    getContainer() {
      return document.querySelector('.tdesign-starter-layout');
    },
    
    // 获取默认表单数据
    getDefaultVehicleForm() {
      return { ...this.defaultVehicleForm };
    },
    
    // 获取电量颜色
    getBatteryColor(battery) {
      if (battery <= 20) {
        return '#e34d59';
      } 
      if (battery <= 50) {
        return '#ed7b2f';
      }
      return '#00a870';
    },
    
    // 加载表格数据
    fetchData() {
      this.dataLoading = true;
      
      setTimeout(() => {
        let mockData = [];
        
        for (let i = 1; i <= 50; i++) {
          const battery = Math.floor(Math.random() * 100);
          const status = Math.floor(Math.random() * 3);
          const city = CITY_OPTIONS[Math.floor(Math.random() * CITY_OPTIONS.length)].value;
          
          mockData.push({
            id: i,
            scooterCode: `SC-${1000 + i}`,
            city,
            location: `${city}市某区某街道${i}号`,
            battery,
            status,
            lastRentTime: status === VEHICLE_STATUS.IN_USE ? 
              new Date(Date.now() - Math.floor(Math.random() * 10000000)).toLocaleString() : 
              new Date(Date.now() - Math.floor(Math.random() * 100000000)).toLocaleString(),
          });
        }

        // 状态筛选：当选择了特定状态时，只显示该状态的车辆
        if (this.filterStatus !== '') {
          mockData = mockData.filter(item => item.status === this.filterStatus);
        }

        // 搜索功能：根据车辆编号搜索
        if (this.searchValue) {
          mockData = mockData.filter(item => 
            item.scooterCode.toLowerCase().includes(this.searchValue.toLowerCase())
          );
        }
        
        this.data = mockData;
        this.pagination.total = mockData.length;
        this.dataLoading = false;
      }, 1000);
    },
    
    // 表单重置
    onReset() {
      this.formData = {
        scooterCode: '',
        city: undefined,
        status: undefined,
      };
      this.fetchData();
    },
    
    // 表单提交查询
    onSubmit() {
      this.dataLoading = true;
      
      // 这里应当是从API获取筛选后的数据，这里用模拟方式进行筛选
      setTimeout(() => {
        let filteredData = [...this.data];
        
        // 根据条件筛选
        if (this.formData.scooterCode) {
          filteredData = filteredData.filter(item => 
            item.scooterCode.includes(this.formData.scooterCode)
          );
        }
        
        if (this.formData.city !== undefined) {
          filteredData = filteredData.filter(item => 
            item.city === this.formData.city
          );
        }
        
        if (this.formData.status !== undefined) {
          filteredData = filteredData.filter(item => 
            item.status === this.formData.status
          );
        }
        
        this.data = filteredData;
        this.pagination.total = filteredData.length;
        this.dataLoading = false;
      }, 500);
    },
    
    // 分页变化
    handlePageChange(curr, pageInfo) {
      console.log('分页变化', curr, pageInfo);
    },
    
    // 表格变化
    handleChange(changeParams, triggerAndData) {
      console.log('表格变化', changeParams, triggerAndData);
    },
    
    // 选择行变化
    handleSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
    
    // 添加车辆
    handleAddVehicle() {
      this.formTitle = '添加车辆';
      this.vehicleForm = this.getDefaultVehicleForm();
      this.isEdit = false;
      this.formVisible = true;
    },
    
    // 编辑车辆
    handleEdit(row) {
      console.log('编辑按钮被点击', row);
      this.formTitle = '编辑车辆';
      this.vehicleForm = { ...row };
      this.isEdit = true;
      this.formVisible = true;
      console.log('formVisible设置为', this.formVisible);
    },
    
    // 报修
    handleRepair(row) {
      this.$dialog.confirm({
        header: '确认报修',
        body: `确定将车辆 ${row.scooterCode} 标记为故障状态？`,
        onConfirm: () => {
          const idx = this.data.findIndex(item => item.id === row.id);
          if (idx > -1) {
            this.data[idx].status = VEHICLE_STATUS.FAULT;
            this.$message.success('已将车辆标记为故障状态');
          }
        },
      });
    },
    
    // 批量报修
    handleBatchRepair() {
      if (this.selectedRowKeys.length === 0) return;
      this.batchRepairVisible = true;
    },
    
    // 确认批量报修
    onBatchRepairConfirm() {
      this.selectedRowKeys.forEach(id => {
        const idx = this.data.findIndex(item => item.id === id);
        if (idx > -1) {
          this.data[idx].status = VEHICLE_STATUS.FAULT;
        }
      });
      this.batchRepairVisible = false;
      this.$message.success(`已将 ${this.selectedRowKeys.length} 辆车标记为故障状态`);
    },
    
    // 删除车辆
    handleDelete(slotProps) {
      this.deleteIdx = this.data.findIndex(item => item.id === slotProps.row.id);
      this.confirmVisible = true;
    },
    
    // 确认删除
    onConfirmDelete() {
      if (this.deleteIdx > -1) {
        const deletedId = this.data[this.deleteIdx].id;
        this.data.splice(this.deleteIdx, 1);
        this.pagination.total = this.data.length;
        
        // 从选中行中删除
        const selectedIdx = this.selectedRowKeys.indexOf(deletedId);
        if (selectedIdx > -1) {
          this.selectedRowKeys.splice(selectedIdx, 1);
        }
        
        this.$message.success('删除成功');
      }
      this.confirmVisible = false;
      this.deleteIdx = -1;
    },
    
    // 取消删除
    onCancel() {
      this.confirmVisible = false;
      this.deleteIdx = -1;
    },
    
    // 提交车辆表单
    onFormSubmit({ validateResult, firstError }) {
      if (validateResult === true) {
        if (this.isEdit) {
          // 编辑现有车辆
          const idx = this.data.findIndex(item => item.id === this.vehicleForm.id);
          if (idx > -1) {
            this.data[idx] = { ...this.vehicleForm };
            this.$message.success('编辑成功');
          }
        } else {
          // 添加新车辆
          const newVehicle = { 
            ...this.vehicleForm,
            id: this.data.length > 0 ? Math.max(...this.data.map(item => item.id)) + 1 : 1,
            lastRentTime: '-',
            mileage: 0
          };
          this.data.unshift(newVehicle);
          this.pagination.total += 1;
          this.$message.success('添加成功');
        }
        this.formVisible = false;
      } else {
        console.log('表单校验失败:', firstError);
        this.$message.error(firstError);
      }
    },
    
    // 关闭侧边栏
    onDialogClose() {
      this.formVisible = false;
    },
  },
});
</script>

  <style lang="less" scoped>
  .vehicle-list-container {
    .operation-container {
      display: flex;
      align-items: center;
      margin-left: 8px;
    }
    
    .left-operation-container {
      display: flex;
      align-items: center;
      margin-bottom: 16px;
      
      .t-button + .t-button {
        margin-left: 8px;
      }
      
      .selected-count {
        display: inline-block;
        margin: 0 0 0 16px;
        color: var(--td-text-color-secondary);
      }
    }
    
    .table-container {
      margin-top: 16px;
    }
  
    .search-area {
      display: flex;
      align-items: center;
      gap: 8px;
  
      .status-filter {
        width: 120px;
      }
  
      .search-input {
        width: 200px;
      }
    }
  }
  
  // 添加侧边栏样式
  :deep(.t-drawer__body) {
    padding: 24px;
  }
  </style> 