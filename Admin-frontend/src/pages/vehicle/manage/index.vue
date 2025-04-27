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
        <t-table :data="data" :columns="columns" :rowKey="rowKey" :verticalAlign="verticalAlign" :hover="hover"
          :pagination="pagination" :loading="dataLoading" :selected-row-keys="selectedRowKeys"
          @select-change="handleSelectChange" @page-change="onPageChange" @change="handleChange"
          :headerAffixedTop="true" :headerAffixProps="{ offsetTop, container: getContainer }">
          <!-- 状态列自定义 -->
          <template #status="{ row }">
            <t-tag v-if="row.status === VEHICLE_STATUS.free" theme="success" variant="light">空闲</t-tag>
            <t-tag v-if="row.status === VEHICLE_STATUS.in_use" theme="warning" variant="light">使用中</t-tag>
            <t-tag v-if="row.status === VEHICLE_STATUS.fault" theme="danger" variant="light">故障</t-tag>
          </template>

          <!-- 电量列自定义 -->
          <template #battery="{ row }">
            <t-progress :percentage="row.battery" :color="getBatteryColor(row.battery)" :label="false"
              trackColor="#e8f4ff" />
            <span style="margin-left: 4px">{{ row.battery }}%</span>
          </template>

          <!-- 价格列自定义 -->
          <template #price="{ row }">
            {{ row.price }} 元/分钟
          </template>

          <!-- 操作列自定义 -->
          <template #op="slotProps">
            <a class="t-button-link" @click="handleEdit(slotProps.row)">编辑</a>
            <a class="t-button-link" @click="handleRepair(slotProps.row)">
              {{ slotProps.row.status === VEHICLE_STATUS.fault ? '恢复' : '报修' }}
            </a>
            <a class="t-button-link" @click="handleDelete(slotProps)">删除</a>
          </template>
        </t-table>
      </div>

      <!-- 添加/编辑车辆弹窗 -->
      <t-drawer :visible.sync="formVisible" :header="formTitle" :footer="false" :size="'500px'" :close-btn="true"
        :on-close="onDialogClose">
        <t-form :data="vehicleForm" :rules="rules" ref="vehicleFormRef" :label-width="100" @submit="onFormSubmit">
          <t-form-item v-if="isEdit" label="车辆编号">
            <t-input v-model="vehicleForm.scooterCode" disabled />
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
          <t-form-item label="价格" name="price">
            <t-input-number v-model="vehicleForm.price" :min="0" :step="0.1" />
            <span style="margin-left: 8px">元/分钟</span>
          </t-form-item>
          <t-form-item class="centerSubmit">
            <t-space>
              <t-button theme="primary" type="submit">提交</t-button>
              <t-button theme="default" variant="base" @click="onDialogClose">取消</t-button>
            </t-space>
          </t-form-item>
        </t-form>
      </t-drawer>

      <!-- 删除确认弹窗 -->
      <t-dialog header="确认删除该车辆？" :body="confirmBody" :visible.sync="confirmVisible" @confirm="onConfirmDelete"
        @cancel="onCancel">
      </t-dialog>

      <!-- 批量报修弹窗 -->
      <t-dialog header="批量报修" :visible.sync="batchRepairVisible" @confirm="onBatchRepairConfirm"
        @cancel="batchRepairVisible = false">
        <p>确认将选中的 {{ selectedRowKeys.length }} 辆车标记为故障状态？</p>
      </t-dialog>
    </div>
  </t-card>
</template>

<script lang="ts">
import Vue from 'vue';
import { SearchIcon } from 'tdesign-icons-vue';
import { scooterService, SCOOTER_STATUS, TableScooter } from '@/service/service-scooter';

// 车辆状态常量
const VEHICLE_STATUS = SCOOTER_STATUS;

// 车辆状态选项
const VEHICLE_STATUS_OPTIONS = [
  { label: '空闲', value: VEHICLE_STATUS.free },
  { label: '使用中', value: VEHICLE_STATUS.in_use },
  { label: '故障', value: VEHICLE_STATUS.fault },
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
      status: VEHICLE_STATUS.free,
      lastRentTime: '',
      price: 0.5,
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
      data: [] as TableScooter[],
      selectedRowKeys: [] as number[],
      columns: [
        { colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left' },
        { title: '车辆编号', colKey: 'scooterCode', width: 120, fixed: 'left' },
        { title: '所属城市', colKey: 'city', width: 120 },
        { title: '具体位置', colKey: 'location', width: 180, ellipsis: true },
        { title: '电量', colKey: 'battery', width: 150, cell: { col: 'battery' } },
        { title: '当前状态', colKey: 'status', width: 120, cell: { col: 'status' } },
        { title: '最近租用时间', colKey: 'lastRentTime', width: 180 },
        { title: '价格(元/分钟)', colKey: 'price', width: 120 },
        { title: '操作', colKey: 'op', fixed: 'right', width: 180 },
      ],
      pagination: {
        pageSize: 10,
        total: 0,
        current: 1,
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
        price: [{ required: true, message: '请输入价格', type: 'error' }],
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
        { value: VEHICLE_STATUS.free, label: '空闲' },
        { value: VEHICLE_STATUS.in_use, label: '使用中' },
        { value: VEHICLE_STATUS.fault, label: '故障' },
      ],

      // 新增的变量
      allData: [] as TableScooter[],
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
    async fetchData() {
      this.dataLoading = true;

      try {
        // 从API获取数据
        const scooters = await scooterService.getAllScooters();
        console.log(scooters)

        // 状态筛选：当选择了特定状态时，只显示该状态的车辆
        let filteredData = scooters;
        if (this.filterStatus !== '') {
          filteredData = filteredData.filter(item => item.status === this.filterStatus);
        }

        // 搜索功能：根据车辆编号搜索
        if (this.searchValue) {
          filteredData = filteredData.filter(item =>
            item.scooterCode.toLowerCase().includes(this.searchValue.toLowerCase())
          );
        }

        // 保存完整的筛选后数据
        this.allData = filteredData;
        
        // 分页处理
        this.pagination.total = filteredData.length;
        this.updatePageData();
      } catch (error) {
        console.error('获取车辆数据失败:', error);
        this.$message.error('获取车辆数据失败');
      } finally {
        this.dataLoading = false;
      }
    },

    // 更新当前页数据
    updatePageData() {
      const { current, pageSize } = this.pagination;
      const start = (current - 1) * pageSize;
      const end = start + pageSize;
      this.data = this.allData.slice(start, end);
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
    onPageChange(pageInfo) {
      console.log('页码变化:', pageInfo);
      this.pagination = {
        ...this.pagination,
        current: pageInfo.current,
        pageSize: pageInfo.pageSize
      };
      this.updatePageData();
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
    async handleEdit(row) {
      console.log('编辑按钮被点击', row);
      this.formTitle = '编辑车辆';

      // 获取最新的车辆详情
      try {
        const scooterDetail = await scooterService.getScooterById(row.id);
        if (scooterDetail) {
          // 将API返回的数据映射到表单
          this.vehicleForm = {
            id: scooterDetail.scooter_id,
            scooterCode: `SC-${1000 + scooterDetail.scooter_id}`,
            city: row.city, // 使用表格中的城市数据
            location: row.location,
            battery: scooterDetail.battery_level,
            status: SCOOTER_STATUS[scooterDetail.status],
            lastRentTime: scooterDetail.last_used_date
              ? new Date(scooterDetail.last_used_date).toLocaleString()
              : '-',
            price: scooterDetail.price,
          };
        } else {
          // 如果获取详情失败，使用表格中的数据
          this.vehicleForm = { ...row };
        }
      } catch (error) {
        console.error('获取车辆详情失败:', error);
        this.vehicleForm = { ...row };
      }

      this.isEdit = true;
      this.formVisible = true;
    },

    // 报修
    async handleRepair(row) {
      // 根据当前状态决定操作类型
      const isFault = row.status === VEHICLE_STATUS.fault;
      const actionText = isFault ? '恢复' : '报修';
      const targetStatus = isFault ? 'free' : 'fault'; // 确保这是后端期望的字符串格式
      
      this.$dialog.confirm({
        header: `确认${actionText}`,
        body: `确定将车辆 ${row.scooterCode} ${isFault ? '恢复为空闲状态' : '标记为故障状态'}？`,
        onConfirm: async () => {
          try {
            // 获取当前车辆详情
            const scooterDetail = await scooterService.getScooterById(row.id);
            if (!scooterDetail) {
              this.$message.error('获取车辆详情失败');
              return;
            }

            // 打印日志，确认状态值
            console.log('当前状态:', row.status);
            console.log('目标状态:', targetStatus);

            // 更新车辆状态
            const updateResult = await scooterService.updateScooter(row.id, {
              location_lat: scooterDetail.location_lat,
              location_lng: scooterDetail.location_lng,
              battery_level: scooterDetail.battery_level,
              status: targetStatus, // 确保这是正确的状态值
              price: scooterDetail.price,
            });

            console.log('API 响应:', updateResult);

            if (updateResult) {
              // 更新本地数据
              const idx = this.data.findIndex(item => item.id === row.id);
              if (idx > -1) {
                this.data[idx].status = isFault ? VEHICLE_STATUS.free : VEHICLE_STATUS.fault;
                this.$message.success(`已将车辆${isFault ? '恢复为空闲状态' : '标记为故障状态'}`);
              }
            } else {
              this.$message.error(`更新车辆状态失败`);
            }
          } catch (error) {
            console.error(`${actionText}操作失败:`, error);
            this.$message.error(`${actionText}操作失败`);
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
    async onBatchRepairConfirm() {
      try {
        let successCount = 0;

        // 逐个更新选中的车辆
        for (const id of this.selectedRowKeys) {
          // 获取当前车辆详情
          const scooterDetail = await scooterService.getScooterById(id);
          if (!scooterDetail) continue;

          // 更新车辆状态为故障
          const updateResult = await scooterService.updateScooter(id, {
            location_lat: scooterDetail.location_lat,
            location_lng: scooterDetail.location_lng,
            battery_level: scooterDetail.battery_level,
            status: 'fault',
            price: scooterDetail.price,
          });

          if (updateResult) {
            successCount++;
            // 更新本地数据
            const idx = this.data.findIndex(item => item.id === id);
            if (idx > -1) {
              this.data[idx].status = VEHICLE_STATUS.fault;
            }
          }
        }

        this.batchRepairVisible = false;

        if (successCount > 0) {
          this.$message.success(`已将 ${successCount} 辆车标记为故障状态`);
        } else {
          this.$message.warning('没有车辆状态被更新');
        }
      } catch (error) {
        console.error('批量报修操作失败:', error);
        this.$message.error('批量报修操作失败');
        this.batchRepairVisible = false;
      }
    },

    // 删除车辆
    handleDelete(slotProps) {
      this.deleteIdx = this.data.findIndex(item => item.id === slotProps.row.id);
      this.confirmVisible = true;
    },

    // 确认删除
    async onConfirmDelete() {
      if (this.deleteIdx > -1) {
        const deletedId = this.data[this.deleteIdx].id;

        try {
          // 调用API删除车辆
          const deleteResult = await scooterService.deleteScooter(deletedId);

          if (deleteResult) {
            // 从本地数据中删除
            this.data.splice(this.deleteIdx, 1);
            this.pagination.total = this.data.length;

            // 从选中行中删除
            const selectedIdx = this.selectedRowKeys.indexOf(deletedId);
            if (selectedIdx > -1) {
              this.selectedRowKeys.splice(selectedIdx, 1);
            }

            this.$message.success('删除成功');
          } else {
            this.$message.error('删除失败');
          }
        } catch (error) {
          console.error('删除车辆失败:', error);
          this.$message.error('删除车辆失败');
        }
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
    async onFormSubmit({ validateResult, firstError }) {
      if (validateResult === true) {
        try {
          if (this.isEdit) {
            // 编辑现有车辆
            // 从表单数据中提取经纬度信息
            const locationMatch = this.vehicleForm.location.match(/\(([^,]+),\s*([^)]+)\)/);
            let lat = 0, lng = 0;

            if (locationMatch && locationMatch.length === 3) {
              lat = parseFloat(locationMatch[1]);
              lng = parseFloat(locationMatch[2]);
            }

            // 构建请求数据
            const requestData = {
              location_lat: lat,
              location_lng: lng,
              battery_level: this.vehicleForm.battery,
              status: Object.keys(VEHICLE_STATUS).find(
                key => VEHICLE_STATUS[key] === this.vehicleForm.status
              ) || 'free',
              price: this.vehicleForm.price,
            };

            // 调用API更新车辆
            const updateResult = await scooterService.updateScooter(
              this.vehicleForm.id,
              requestData
            );

            if (updateResult) {
              // 更新本地数据
              const idx = this.data.findIndex(item => item.id === this.vehicleForm.id);
              if (idx > -1) {
                this.data[idx] = { ...this.vehicleForm };
                this.$message.success('编辑成功');
              }
            } else {
              this.$message.error('更新车辆失败');
            }
          } else {
            // 添加新车辆
            // 从表单数据中提取城市信息，并生成随机经纬度（实际应用中应有地图选点）
            const lat = 31.2 + Math.random() * 0.1; // 上海附近的随机经纬度
            const lng = 121.4 + Math.random() * 0.1;

            // 构建请求数据
            const requestData = {
              location_lat: lat,
              location_lng: lng,
              battery_level: this.vehicleForm.battery,
              status: Object.keys(VEHICLE_STATUS).find(
                key => VEHICLE_STATUS[key] === this.vehicleForm.status
              ) || 'free',
              price: this.vehicleForm.price,
            };

            // 调用API添加车辆
            const newScooterId = await scooterService.addScooter(requestData);

            if (newScooterId) {
              // 重新获取数据以确保显示最新状态
              await this.fetchData();
              this.$message.success('添加成功');
            } else {
              this.$message.error('添加车辆失败');
            }
          }
          this.formVisible = false;
        } catch (error) {
          console.error('提交车辆表单失败:', error);
          this.$message.error('操作失败，请重试');
        }
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

    .t-button+.t-button {
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

// 添加提交按钮居中样式
.centerSubmit {
  display: flex;
  justify-content: center;
}
</style>