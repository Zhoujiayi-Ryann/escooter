<template>
  <t-config-provider :global-config="globalConfig">
    <t-card :bordered="false">
      <div class="vehicle-list-container">
        <!-- 顶部操作区 -->
        <t-row justify="space-between">
          <div class="left-operation-container">
            <t-button @click="handleAddVehicle">Add Vehicle</t-button>
            <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length" @click="handleBatchRepair">
              Batch Repair
            </t-button>
            <p v-if="!!selectedRowKeys.length" class="selected-count">Selected {{ selectedRowKeys.length }} items</p>
          </div>
          <div class="search-area">
            <t-select v-model="filterStatus" class="status-filter" placeholder="Status Filter">
              <t-option v-for="status in statusOptions" :key="status.value" :value="status.value"
                :label="status.label" />
            </t-select>
            <t-input v-model="searchValue" class="search-input" placeholder="Search by vehicle.no" clearable>
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
              <t-tag v-if="row.status === VEHICLE_STATUS.free" theme="success" variant="light">Free</t-tag>
              <t-tag v-if="row.status === VEHICLE_STATUS.booked" theme="primary" variant="light">Booked</t-tag>
              <t-tag v-if="row.status === VEHICLE_STATUS.in_use" theme="warning" variant="light">In Use</t-tag>
              <t-tag v-if="row.status === VEHICLE_STATUS.maintenance" theme="danger" variant="light">Maintenance</t-tag>
              <t-tag v-if="row.status === VEHICLE_STATUS.charging" theme="default" variant="light">Charging</t-tag>
            </template>

            <!-- 电量列自定义 -->
            <template #battery="{ row }">
              <t-progress :percentage="row.battery" :color="getBatteryColor(row.battery)" :label="false"
                trackColor="#e8f4ff" />
              <span style="margin-left: 4px">{{ row.battery }}%</span>
            </template>

            <!-- 价格列自定义 -->
            <template #price="{ row }">
              £{{ row.price }} / hour
            </template>

            <!-- 操作列自定义 -->
            <template #op="slotProps">
              <a class="t-button-link" @click="handleEdit(slotProps.row)">Edit</a>
              <a class="t-button-link" @click="handleRepair(slotProps.row)">
                {{ slotProps.row.status === VEHICLE_STATUS.maintenance ? 'Restore' : 'Repair' }}
              </a>
              <a class="t-button-link" @click="handleDetails(slotProps.row)">Details</a>
              <a class="t-button-link" v-if="slotProps.row.status === VEHICLE_STATUS.free" @click="handlePay(slotProps.row)">Pay</a>
            </template>

            <!-- 添加删除操作列 -->
            <template #delete="slotProps">
              <a class="t-button-link delete-icon" @click="handleDelete(slotProps)">
                <t-icon name="delete" />
              </a>
            </template>
          </t-table>
        </div>

        <!-- 添加/编辑车辆弹窗 -->
        <t-drawer :visible.sync="formVisible" :header="formTitle" :footer="false" :size="'500px'" :close-btn="true"
          :on-close="onDialogClose">
          <t-form :data="vehicleForm" :rules="rules" ref="vehicleFormRef" :label-width="100" @submit="onFormSubmit">
            <t-form-item v-if="isEdit" label="Vehicle No.">
              <t-input v-model="vehicleForm.scooterCode" disabled />
            </t-form-item>
            <t-form-item label="Location" name="location">
              <!-- 将输入框改为选择站点 -->
              <t-select v-model="vehicleForm.stationId" :options="STATION_OPTIONS" :disabled="isEdit"
                placeholder="Please select a station" @change="handleStationChange" />
            </t-form-item>
            <t-form-item label="Battery" name="battery">
              <t-input-number v-model="vehicleForm.battery" :min="0" :max="100" :disabled="isEdit" />
              <span style="margin-left: 8px">%</span>
            </t-form-item>
            <t-form-item label="Status" name="status">
              <t-select v-model="vehicleForm.status" :options="VEHICLE_STATUS_OPTIONS"
                placeholder="Please select status" />
            </t-form-item>
            <t-form-item label="Price" name="price">
              <t-input-number v-model="vehicleForm.price" :min="0" :step="0.1" />
              £<span style="margin-left: 8px"> / min</span>
            </t-form-item>
            <t-form-item class="centerSubmit">
              <t-space>
                <t-button theme="primary" type="submit">Submit</t-button>
                <t-button theme="default" variant="base" @click="onDialogClose">Cancel</t-button>
              </t-space>
            </t-form-item>
          </t-form>
        </t-drawer>

        <!-- 删除确认弹窗 -->
        <t-dialog header="Confirm Delete Vehicle?" :body="confirmBody" :visible.sync="confirmVisible"
          @confirm="onConfirmDelete" @cancel="onCancel">
        </t-dialog>

        <!-- 批量报修弹窗 -->
        <t-dialog header="Batch Repair" :visible.sync="batchRepairVisible" @confirm="onBatchRepairConfirm"
          @cancel="batchRepairVisible = false">
          <p>Confirm marking {{ selectedRowKeys.length }} selected vehicles as faulty?</p>
        </t-dialog>

        <!-- 添加支付侧边栏 -->
        <t-drawer :visible.sync="payFormVisible" header="Create Order" :footer="false" :size="'500px'" :close-btn="true"
          :on-close="onPayDialogClose">
          <t-form :data="payForm" :rules="payRules" ref="payFormRef" :label-width="100" @submit="onPayFormSubmit">
            <t-form-item label="Vehicle No.">
              <t-input v-model="payForm.scooterCode" disabled />
            </t-form-item>
            <t-form-item label="Station">
              <t-input v-model="payForm.stationName" disabled />
            </t-form-item>
            <t-form-item label="Location" name="pickupLocation">
              <t-input v-model="payForm.pickupLocation" disabled />
            </t-form-item>
            <t-form-item label="Time Range" name="timeRange">
              <t-date-range-picker v-model="payForm.timeRange" enable-time-picker />
            </t-form-item>
            <t-form-item label="Amount (£)" name="amount">
              <t-input-number v-model="payForm.amount" :min="0" :step="0.1" />
            </t-form-item>
            <t-form-item class="centerSubmit">
              <t-space>
                <t-button theme="primary" type="submit">Submit</t-button>
                <t-button theme="default" variant="base" @click="onPayDialogClose">Cancel</t-button>
              </t-space>
            </t-form-item>
          </t-form>
        </t-drawer>
      </div>
    </t-card>
  </t-config-provider>
</template>

<script lang="ts">
import Vue from 'vue';
import { SearchIcon, DeleteIcon } from 'tdesign-icons-vue';
import { scooterService, SCOOTER_STATUS, TableScooter } from '@/service/service-scooter';
import { getAllNonAdminUsers, IUser } from '@/service/service-user';
import enConfig from 'tdesign-vue/es/locale/en_US'; // 

// 车辆状态常量
const VEHICLE_STATUS = SCOOTER_STATUS;

// 车辆状态选项
const VEHICLE_STATUS_OPTIONS = [
  { label: 'Free', value: VEHICLE_STATUS.free },
  { label: 'Booked', value: VEHICLE_STATUS.booked },
  { label: 'In Use', value: VEHICLE_STATUS.in_use },
  { label: 'Maintenance', value: VEHICLE_STATUS.maintenance },
  { label: 'Charging', value: VEHICLE_STATUS.charging },
];

// 地址缓存
const addressCache = new Map<string, string>();
// 从 localStorage 加载缓存
const loadCacheFromStorage = () => {
  try {
    const cachedData = localStorage.getItem('addressCache');
    if (cachedData) {
      const parsedData = JSON.parse(cachedData);
      Object.entries(parsedData).forEach(([key, value]) => {
        addressCache.set(key, value as string);
      });
    }
  } catch (error) {
    console.error('加载地址缓存失败:', error);
  }
};

// 保存缓存到 localStorage
const saveCacheToStorage = () => {
  try {
    const cacheObj = Object.fromEntries(addressCache);
    localStorage.setItem('addressCache', JSON.stringify(cacheObj));
  } catch (error) {
    console.error('保存地址缓存失败:', error);
  }
};

// 请求队列
const requestQueue: Array<() => Promise<void>> = [];
// 是否正在处理请求
let isProcessing = false;
// 上次请求时间
let lastRequestTime = 0;
// 最小请求间隔（毫秒）
const MIN_REQUEST_INTERVAL = 200; // 修改为 200ms，支持每秒 5 个请求

// 处理请求队列
const processQueue = async () => {
  if (isProcessing || requestQueue.length === 0) return;
  
  isProcessing = true;
  const currentTime = Date.now();
  const timeSinceLastRequest = currentTime - lastRequestTime;
  
  if (timeSinceLastRequest < MIN_REQUEST_INTERVAL) {
    await new Promise(resolve => setTimeout(resolve, MIN_REQUEST_INTERVAL - timeSinceLastRequest));
  }
  
  const request = requestQueue.shift();
  if (request) {
    try {
      await request();
    } finally {
      lastRequestTime = Date.now();
      isProcessing = false;
      processQueue();
    }
  }
};

// 获取城市名称
const getCityFromLocation = async (lat: number, lng: number): Promise<string> => {
  const cacheKey = `${lat},${lng}`;
  
  // 检查缓存
  if (addressCache.has(cacheKey)) {
    return addressCache.get(cacheKey) || '未知城市';
  }

  return new Promise((resolve) => {
    requestQueue.push(async () => {
      try {
        // 使用腾讯地图 WebService API
        const response = await fetch(`https://apis.map.qq.com/ws/geocoder/v1/?location=${lat},${lng}&key=4HZBZ-FLRCL-COFPO-EKA57-6CILQ-OEFTJ`);
        const data = await response.json();
        
        if (data.status === 0) {
          const city = data.result.address_component.city || '未知城市';
          // 存入缓存
          addressCache.set(cacheKey, city);
          // 保存到 localStorage
          saveCacheToStorage();
          resolve(city);
        } else {
          console.error('逆地址解析失败:', data.message);
          resolve('未知城市');
        }
      } catch (error) {
        console.error('逆地址解析请求失败:', error);
        resolve('未知城市');
      }
    });
    
    processQueue();
  });
};

// 加载腾讯地图 SDK
const loadTMapSDK = () => {
  return new Promise<void>((resolve, reject) => {
    if (window.TMap) {
      resolve();
      return;
    }

    const script = document.createElement('script');
    script.src = 'https://map.qq.com/api/gljs?v=1.exp&key=4HZBZ-FLRCL-COFPO-EKA57-6CILQ-OEFTJ';
    script.onload = () => {
      resolve();
    };
    script.onerror = (error) => {
      reject(error);
    };
    document.head.appendChild(script);
  });
};

// 在组件挂载时加载缓存
loadCacheFromStorage();

// 添加利兹市的站点数据（国外）
const LEEDS_STATIONS = [
  { id: 1, name: 'Leeds City Centre', lat: 53.7997, lng: -1.5492, country: 'UK' },
  { id: 2, name: 'University of Leeds', lat: 53.8067, lng: -1.5550, country: 'UK' },
  { id: 3, name: 'Leeds Train Station', lat: 53.7947, lng: -1.5478, country: 'UK' },
  { id: 4, name: 'Headingley', lat: 53.8175, lng: -1.5786, country: 'UK' },
  { id: 5, name: 'Roundhay Park', lat: 53.8382, lng: -1.4990, country: 'UK' },
  { id: 6, name: 'Kirkstall Abbey', lat: 53.8204, lng: -1.6066, country: 'UK' },
  { id: 7, name: 'White Rose Shopping Centre', lat: 53.7583, lng: -1.5775, country: 'UK' },
  { id: 8, name: 'Temple Newsam', lat: 53.7794, lng: -1.4647, country: 'UK' },
  { id: 9, name: 'Elland Road Stadium', lat: 53.7778, lng: -1.5724, country: 'UK' },
  { id: 10, name: 'Leeds Dock', lat: 53.7913, lng: -1.5339, country: 'UK' },
];

// 添加成都市的站点数据（国内）
const CHENGDU_STATIONS = [
  { id: 11, name: 'Tianfu Square', lat: 30.6578, lng: 104.0668, country: 'CN' },
  { id: 12, name: 'Chunxi Road', lat: 30.6537, lng: 104.0865, country: 'CN' },
  { id: 13, name: 'Wide and Narrow Alley', lat: 30.6693, lng: 104.0533, country: 'CN' },
  { id: 14, name: 'Jinli Ancient Street', lat: 30.6427, lng: 104.0428, country: 'CN' },
  { id: 15, name: 'Sichuan University', lat: 30.6307, lng: 104.0848, country: 'CN' },
  { id: 16, name: 'UESTC', lat: 30.7577, lng: 103.9268, country: 'CN' },
  { id: 17, name: 'Chengdu East Railway Station', lat: 30.6199, lng: 104.1487, country: 'CN' },
  { id: 18, name: 'Chengdu South Railway Station', lat: 30.5142, lng: 104.0738, country: 'CN' },
  { id: 19, name: 'Chengdu Panda Base', lat: 30.7348, lng: 104.1444, country: 'CN' },
  { id: 20, name: 'Global Center', lat: 30.5702, lng: 104.0668, country: 'CN' },
];

// 合并所有站点数据
const ALL_STATIONS = [...LEEDS_STATIONS, ...CHENGDU_STATIONS];

// 站点选项
const STATION_OPTIONS = ALL_STATIONS.map(station => ({
  label: `${station.name} (${station.country})`,
  value: station.id
}));

// 根据站点ID获取站点信息
const getStationById = (stationId) => {
  return ALL_STATIONS.find(station => station.id === stationId);
};

// 根据站点ID获取位置字符串
const getLocationStringFromStationId = (stationId) => {
  const station = getStationById(stationId);
  if (station) {
    return `${station.name} (${station.lat}, ${station.lng})`;
  }
  return '';
};

// 从位置字符串中提取站点ID
const getStationIdFromLocationString = (locationString) => {
  for (const station of ALL_STATIONS) {
    if (locationString.includes(`(${station.lat}, ${station.lng})`)) {
      return station.id;
    }
  }
  return null;
};

export default Vue.extend({
  name: 'VehicleManagement',
  components: {
    SearchIcon,
    DeleteIcon,
  },
  data() {
    const defaultVehicleForm = {
      id: null,
      scooterCode: '',
      city: '',
      location: '',
      stationId: null, // 添加站点ID字段
      battery: 100,
      status: VEHICLE_STATUS.free,
      lastRentTime: '',
      price: 0.5,
    };

    return {
      // 添加语言配置
      globalConfig: enConfig,

      VEHICLE_STATUS,
      VEHICLE_STATUS_OPTIONS,
      STATION_OPTIONS, // 添加站点选项

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
        { title: 'Vehicle No.', colKey: 'scooterCode', width: 120, fixed: 'left' },
        { title: 'Station', colKey: 'station', width: 150 },
        { title: 'Location', colKey: 'location', width: 180, ellipsis: true },
        { title: 'Battery', colKey: 'battery', width: 150, cell: { col: 'battery' } },
        { title: 'Status', colKey: 'status', width: 120, cell: { col: 'status' } },
        { title: 'Last Rental Time', colKey: 'lastRentTime', width: 180 },
        { title: 'Price(£/hour)', colKey: 'price', width: 120 },
        { title: 'Actions', colKey: 'op', width: 160 },
        { title: '', colKey: 'delete', width: 60 },
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
      formTitle: 'Add Vehicle',
      isEdit: false,
      vehicleForm: { ...defaultVehicleForm },
      defaultVehicleForm,
      rules: {
        scooterCode: [{ required: true, message: 'Please enter vehicle number', type: 'error' }],
        city: [{ required: true, message: 'Please select city', type: 'error' }],
        battery: [{ required: true, message: 'Please enter battery', type: 'error' }],
        status: [{ required: true, message: 'Please select status', type: 'error' }],
        price: [{ required: true, message: 'Please enter price', type: 'error' }],
      },
      confirmVisible: false,
      deleteIdx: -1,
      batchRepairVisible: false,
      filterStatus: '',
      searchValue: '',
      searchTimer: null,

      // 添加状态选项
      statusOptions: [
        { value: '', label: 'All' },
        { value: VEHICLE_STATUS.free, label: 'Free' },
        { value: VEHICLE_STATUS.booked, label: 'Booked' },
        { value: VEHICLE_STATUS.in_use, label: 'In Use' },
        { value: VEHICLE_STATUS.maintenance, label: 'Maintenance' },
        { value: VEHICLE_STATUS.charging, label: 'Charging' },
      ],

      // 新增的变量
      allData: [] as TableScooter[],

      // 支付相关
      payFormVisible: false,
      payForm: {
        scooterCode: '',
        stationName: '', // 添加站点名称字段
        pickupLocation: '',
        stationId: null,
        timeRange: [],
        amount: 0,
      },
      payRules: {
        scooterCode: [{ required: true, message: 'Please enter vehicle number', type: 'error' }],
        stationId: [{ required: true, message: 'Please select a station', type: 'error' }], // 修改验证规则
        timeRange: [{ required: true, message: 'Please select time range', type: 'error' }],
        amount: [{ required: true, message: 'Please enter amount', type: 'error' }],
      },
      
      // 用户选项
      userOptions: [] as IUser[],
      locationSearching: false,
    };
  },

  computed: {
    confirmBody() {
      if (this.deleteIdx > -1) {
        const vehicle = this.data?.[this.deleteIdx];
        return `After deletion, all information of vehicle ${vehicle.scooterCode} will be cleared and cannot be recovered`;
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
    this.fetchUsers();
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
        // 确保地图 SDK 已加载
        await loadTMapSDK();

        // 从API获取数据
        const scooters = await scooterService.getAllScooters();
        
        // 处理每个滑板车的数据
        const processedData = await Promise.all(scooters.map(async (scooter) => {
          // 提取经纬度
          const locationMatch = scooter.location.match(/\(([^,]+),\s*([^)]+)\)/);
          let city = '未知城市';
          let stationId = null;
          let stationName = '未知站点';
          
          if (locationMatch && locationMatch.length === 3) {
            const lat = parseFloat(locationMatch[1]);
            const lng = parseFloat(locationMatch[2]);
            city = await getCityFromLocation(lat, lng);
            
            // 查找最近的站点
            stationId = getStationIdFromLocationString(scooter.location);
            if (stationId) {
              const station = getStationById(stationId);
              if (station) {
                stationName = station.name;
              }
            }
          }

          return {
            ...scooter,
            city: city,
            station: stationName,
            stationId: stationId
          };
        }));

        // 状态筛选
        let filteredData = processedData;
        if (this.filterStatus !== '') {
          filteredData = filteredData.filter(item => item.status === this.filterStatus);
        }

        // 搜索功能
        if (this.searchValue) {
          const searchLower = this.searchValue.toLowerCase();
          filteredData = filteredData.filter(item => 
            item.scooterCode.toLowerCase().includes(searchLower)
          );
        }

        // 保存完整的筛选后数据
        this.allData = filteredData;

        // 更新分页信息
        this.pagination.total = filteredData.length;
        this.pagination.current = 1;
        this.updatePageData();

        // 清空选中状态
        this.selectedRowKeys = [];

      } catch (error) {
        console.error('获取滑板车数据失败:', error);
        this.$message.error('获取滑板车数据失败，请稍后重试');
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
      console.log('Page number changed:', pageInfo);
      this.pagination = {
        ...this.pagination,
        current: pageInfo.current,
        pageSize: pageInfo.pageSize
      };
      this.updatePageData();
    },

    // 表格变化
    handleChange(changeParams, triggerAndData) {
      console.log('Table changed', changeParams, triggerAndData);
    },

    // 选择行变化
    handleSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },

    // 添加车辆
    handleAddVehicle() {
      this.formTitle = 'Add Vehicle';
      this.vehicleForm = this.getDefaultVehicleForm();
      this.isEdit = false;
      this.formVisible = true;
    },

    // 编辑车辆
    async handleEdit(row) {
      console.log('Edit button clicked', row);
      this.formTitle = 'Edit Vehicle';

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
            stationId: row.stationId || getStationIdFromLocationString(row.location), // 优先使用行数据中的站点ID
            battery: scooterDetail.battery_level,
            status: SCOOTER_STATUS[scooterDetail.status],
            lastRentTime: scooterDetail.last_used_date
              ? new Date(scooterDetail.last_used_date).toLocaleString()
              : '-',
            price: scooterDetail.price,
          };
        } else {
          // 如果获取详情失败，使用表格中的数据
          this.vehicleForm = { 
            ...row,
            stationId: row.stationId || getStationIdFromLocationString(row.location) // 优先使用行数据中的站点ID
          };
        }
      } catch (error) {
        console.error('Failed to get vehicle details:', error);
        this.vehicleForm = { 
          ...row,
          stationId: row.stationId || getStationIdFromLocationString(row.location) // 优先使用行数据中的站点ID
        };
      }

      this.isEdit = true;
      this.formVisible = true;
    },

    // 报修
    async handleRepair(row) {
      // 根据当前状态决定操作类型
      const isMaintenance = row.status === VEHICLE_STATUS.maintenance;
      const actionText = isMaintenance ? 'Restore' : 'Repair';
      const targetStatus = isMaintenance ? 'free' : 'maintenance'; // 确保这是后端期望的字符串格式

      this.$dialog.confirm({
        header: `Confirm ${actionText}`,
        body: `Are you sure to ${isMaintenance ? 'restore vehicle' : 'mark vehicle'} ${row.scooterCode} ${isMaintenance ? 'to free status' : 'as maintenance status'}?`,
        onConfirm: async () => {
          try {
            // 获取当前车辆详情
            const scooterDetail = await scooterService.getScooterById(row.id);
            if (!scooterDetail) {
              this.$message.error('Failed to get vehicle details');
              return;
            }

            // 打印日志，确认状态值
            console.log('Current status:', row.status);
            console.log('Target status:', targetStatus);

            // 更新车辆状态
            const updateResult = await scooterService.updateScooter(row.id, {
              location_lat: scooterDetail.location_lat,
              location_lng: scooterDetail.location_lng,
              battery_level: scooterDetail.battery_level,
              status: targetStatus, // 确保这是正确的状态值
              price: scooterDetail.price,
            });

            console.log('API response:', updateResult);

            if (updateResult) {
              // 更新本地数据
              const idx = this.data.findIndex(item => item.id === row.id);
              if (idx > -1) {
                this.data[idx].status = isMaintenance ? VEHICLE_STATUS.free : VEHICLE_STATUS.maintenance;
                this.$message.success(`Vehicle has been ${isMaintenance ? 'restored to free status' : 'marked as maintenance status'}`);
              }
            } else {
              this.$message.error(`Failed to update vehicle status`);
            }
          } catch (error) {
            console.error(`${actionText} operation failed:`, error);
            this.$message.error(`${actionText} operation failed`);
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
            status: 'maintenance',
            price: scooterDetail.price,
          });

          if (updateResult) {
            successCount++;
            // 更新本地数据
            const idx = this.data.findIndex(item => item.id === id);
            if (idx > -1) {
              this.data[idx].status = VEHICLE_STATUS.maintenance;
            }
          }
        }

        this.batchRepairVisible = false;

        if (successCount > 0) {
          this.$message.success(`${successCount} vehicles have been marked as maintenance status`);
        } else {
          this.$message.warning('No vehicle status was updated');
        }
      } catch (error) {
        console.error('Batch repair operation failed:', error);
        this.$message.error('Batch repair operation failed');
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

            this.$message.success('Delete successful');
          } else {
            this.$message.error('Delete failed');
          }
        } catch (error) {
          console.error('Failed to delete vehicle:', error);
          this.$message.error('Failed to delete vehicle');
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

    // 解析地址为经纬度
    async parseAddress(address: string) {
      try {
        const response = await fetch(`https://apis.map.qq.com/ws/geocoder/v1/?address=${encodeURIComponent(address)}&key=4HZBZ-FLRCL-COFPO-EKA57-6CILQ-OEFTJ`);
        const data = await response.json();
        
        if (data.status === 0) {
          const { lat, lng } = data.result.location;
          return {
            success: true,
            lat,
            lng,
            city: data.result.address_components.city
          };
        } else {
          return {
            success: false,
            error: data.message
          };
        }
      } catch (error) {
        console.error('地址解析请求失败:', error);
        return {
          success: false,
          error: '地址解析请求失败'
        };
      }
    },

    // 修改提交表单方法
    async onFormSubmit({ validateResult, firstError }) {
      if (validateResult === true) {
        try {
          // 获取选中站点的经纬度
          const station = getStationById(this.vehicleForm.stationId);
          const lat = station ? station.lat : 0;
          const lng = station ? station.lng : 0;
          
          // 构建位置字符串，包含站点名称
          const locationString = station ? `${station.name} (${lat}, ${lng})` : `(${lat}, ${lng})`;

          if (this.isEdit) {
            // 编辑现有车辆
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
              // 更新成功后刷新数据
              await this.fetchData();
              this.$message.success('Edit successful');
            } else {
              this.$message.error('Failed to update vehicle');
            }
          } else {
            // 添加新车辆
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
              this.$message.success('Add successful');
            } else {
              this.$message.error('Failed to add vehicle');
            }
          }
          this.formVisible = false;
        } catch (error) {
          console.error('Submit vehicle form failed:', error);
          this.$message.error('Operation failed, please try again');
        }
      } else {
        console.log('Form validation failed:', firstError);
        this.$message.error(firstError);
      }
    },

    // 关闭侧边栏
    onDialogClose() {
      this.formVisible = false;
    },

    // 检查详情
    handleDetails(row) {
      // 从 location 中提取经纬度
      const locationMatch = row.location.match(/\(([^,]+),\s*([^)]+)\)/);
      if (locationMatch && locationMatch.length === 3) {
        const lat = parseFloat(locationMatch[1]);
        const lng = parseFloat(locationMatch[2]);
        
        // 使用 Vue Router 跳转到地图页面
        this.$router.push({
          path: '/vehicle/map',
          query: {
            lat: lat.toString(),
            lng: lng.toString()
          }
        });
      } else {
        this.$message.error('Invalid location data');
      }
    },

    // 获取用户列表
    async fetchUsers() {
      try {
        const users = await getAllNonAdminUsers();
        this.userOptions = users;
      } catch (error) {
        console.error('获取用户列表失败:', error);
        this.$message.error('获取用户列表失败');
      }
    },

    // 添加支付
    handlePay(row) {
      // 获取站点信息
      const stationId = row.stationId || getStationIdFromLocationString(row.location);
      const station = getStationById(stationId);
      const stationName = station ? station.name : '未知站点';
      
      this.payForm = {
        scooterCode: row.scooterCode,
        stationName: stationName, // 添加站点名称
        pickupLocation: row.location, // 直接使用车辆当前位置
        stationId: stationId, // 从位置字符串中提取站点ID
        timeRange: [],
        amount: 0,
      };
      this.payFormVisible = true;
    },

    // 提交支付表单
    async onPayFormSubmit({ validateResult, firstError }) {
      if (validateResult === true) {
        try {
          // 获取当前车辆ID（从scooterCode中提取）
          const scooterIdMatch = this.payForm.scooterCode.match(/SC-(\d+)/);
          if (!scooterIdMatch) {
            this.$message.error('Invalid vehicle code');
            return;
          }
          const scooterId = parseInt(scooterIdMatch[1]) - 1000;
          
          // 获取当前管理员ID
          const currentUser = this.$store.state.user?.userInfo || {};
          const adminId = currentUser.userId || 1; // 默认为1如果没有获取到
          
          // 格式化开始和结束时间
          const startTime = scooterService.formatDateTimeForAPI(this.payForm.timeRange[0]);
          const endTime = scooterService.formatDateTimeForAPI(this.payForm.timeRange[1]);
          
          // 构建订单请求数据
          const orderData = {
            user_id: adminId,
            scooter_id: scooterId,
            pickup_address: this.payForm.pickupLocation, // 使用车辆当前位置
            start_time: startTime,
            end_time: endTime,
            cost: this.payForm.amount
          };
          
          console.log('Creating order with data:', orderData);
          
          // 调用API创建临时用户订单
          const orderResult = await scooterService.createOrderForTempUser(orderData);
          
          if (orderResult) {
            this.$message.success('Order created successfully');
            // 更新车辆状态为已预订
            await this.fetchData();
          } else {
            this.$message.error('Failed to create order');
          }
          this.payFormVisible = false;
        } catch (error) {
          console.error('Submit pay form failed:', error);
          this.$message.error('Operation failed, please try again');
        }
      } else {
        console.log('Form validation failed:', firstError);
        this.$message.error(firstError);
      }
    },

    // 关闭支付侧边栏
    onPayDialogClose() {
      this.payFormVisible = false;
    },

    // 站点选择变化处理
    handleStationChange(stationId) {
      if (stationId) {
        this.vehicleForm.location = getLocationStringFromStationId(stationId);
      } else {
        this.vehicleForm.location = '';
      }
    },

    // 支付表单站点选择变化处理
    handlePayStationChange(stationId) {
      if (stationId) {
        this.payForm.pickupLocation = getLocationStringFromStationId(stationId);
      } else {
        this.payForm.pickupLocation = '';
      }
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

// 添加删除图标样式
.delete-icon {
  color: var(--td-error-color);
  
  &:hover {
    opacity: 0.8;
  }
}
</style>