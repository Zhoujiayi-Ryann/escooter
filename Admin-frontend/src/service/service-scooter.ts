import axios from 'axios';

// API基础URL
const BASE_URL = 'https://khnrsggvzudb.sealoshzh.site/api';

// 滑板车状态映射
export const SCOOTER_STATUS = {
  free: 0,        // 空闲
  booked: 1,      // 已预订
  in_use: 2,      // 使用中
  maintenance: 3, // 维护中
  charging: 4,    // 充电中
};

// 滑板车状态的反向映射
export const SCOOTER_STATUS_REVERSE = {
  0: 'free',
  1: 'booked',
  2: 'in_use',
  3: 'maintenance',
  4: 'charging',
};

// 响应类型接口
interface ApiResponse<T> {
  code: number;
  data: T;
  msg: string;
}

// 滑板车响应接口
export interface ScooterResponse {
  scooter_id: number;
  location_lat: number;
  location_lng: number;
  status: string;
  battery_level: number;
  total_usage_time: number;
  price: number;
  last_maintenance_date?: string;
  last_used_date?: string;
}

// 滑板车请求接口
export interface ScooterRequest {
  location_lat: number;
  location_lng: number;
  status?: string;
  battery_level: number;
  price: number;
}

// 管理员临时用户订单请求接口
export interface AdminTempUserOrderRequest {
  user_id: number;        // 管理员ID
  scooter_id: number;     // 滑板车ID
  pickup_address: string; // 取车地址
  start_time: string;     // 开始时间，ISO格式
  end_time: string;       // 结束时间，ISO格式
  cost: number;           // 订单金额
}

// 订单响应接口
export interface OrderResponse {
  order_id: number;
  user_id: number;
  scooter_id: number;
  start_time: string;
  end_time: string;
  new_end_time: string | null;
  extended_duration: number;
  extended_cost: number | null;
  cost: number;
  discount_amount: number | null;
  pickup_address: string;
  status: string;
  created_at: string;
  is_deleted: number;
}

// 表格数据适配接口
export interface TableScooter {
  id: number;
  scooterCode: string;
  city: string;
  location: string;
  battery: number;
  status: number;
  lastRentTime: string;
  price: number;
}

// 将后端滑板车数据转换为表格数据
const mapScooterToTableData = (scooter: ScooterResponse): TableScooter => {
  // 简单地从经纬度推断城市（实际中这应该由地理编码API完成）
  const city = '上海'; // 仅作为示例，实际应根据经纬度确定

  // 将状态字符串转换为数字
  let statusNumber = SCOOTER_STATUS.free;
  if (scooter.status === 'booked') {
    statusNumber = SCOOTER_STATUS.booked;
  } else if (scooter.status === 'in_use') {
    statusNumber = SCOOTER_STATUS.in_use;
  } else if (scooter.status === 'maintenance') {
    statusNumber = SCOOTER_STATUS.maintenance;
  } else if (scooter.status === 'charging') {
    statusNumber = SCOOTER_STATUS.charging;
  }

  return {
    id: scooter.scooter_id,
    scooterCode: `SC-${1000 + scooter.scooter_id}`, // 构造一个滑板车编号
    city: city,
    location: `(${scooter.location_lat.toFixed(4)}, ${scooter.location_lng.toFixed(4)})`,
    battery: scooter.battery_level,
    status: statusNumber,
    lastRentTime: scooter.last_used_date
      ? new Date(scooter.last_used_date).toLocaleString()
      : '-',
    price: scooter.price,
  };
};

// 滑板车服务类
export const scooterService = {
  /**
   * 获取所有滑板车
   */
  async getAllScooters(): Promise<TableScooter[]> {
    try {
      const response = await axios.get<ApiResponse<ScooterResponse[]>>(`${BASE_URL}/scooters_all`);

      if (response.data.code === 1 && response.data.data) {
        // 将API响应转换为表格数据
        return response.data.data.map(mapScooterToTableData);
      }
      return [];
    } catch (error) {
      console.error('获取滑板车列表失败:', error);
      return [];
    }
  },

  /**
   * 根据ID获取滑板车
   */
  async getScooterById(scooterId: number): Promise<ScooterResponse | null> {
    try {
      const response = await axios.get<ApiResponse<ScooterResponse>>(`${BASE_URL}/scooters/${scooterId}`);

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error(`获取滑板车(ID: ${scooterId})详情失败:`, error);
      return null;
    }
  },

  /**
   * 添加滑板车
   */
  async addScooter(scooterData: ScooterRequest): Promise<number | null> {
    try {
      const response = await axios.post<ApiResponse<number>>(`${BASE_URL}/scooters`, scooterData);

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error('添加滑板车失败:', error);
      return null;
    }
  },

  /**
   * 更新滑板车
   */
  async updateScooter(scooterId: number, scooterData: ScooterRequest): Promise<ScooterResponse | null> {
    try {
      const response = await axios.post<ApiResponse<ScooterResponse>>(
        `${BASE_URL}/scooters/${scooterId}`,
        scooterData
      );

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error(`更新滑板车(ID: ${scooterId})失败:`, error);
      return null;
    }
  },

  /**
   * 删除滑板车
   */
  async deleteScooter(scooterId: number): Promise<boolean> {
    try {
      const response = await axios.delete<ApiResponse<void>>(`${BASE_URL}/scooters/${scooterId}`);

      return response.data.code === 1;
    } catch (error) {
      console.error(`删除滑板车(ID: ${scooterId})失败:`, error);
      return false;
    }
  },

  /**
   * 管理员为临时用户创建订单
   * @param orderData 临时用户订单数据
   * @returns 创建的订单信息，失败返回null
   */
  async createOrderForTempUser(orderData: AdminTempUserOrderRequest): Promise<OrderResponse | null> {
    try {
      console.log('Admin creating order for temp user:', orderData);

      const response = await axios.post<ApiResponse<OrderResponse>>(
        `${BASE_URL}/orders/temp-user`,
        orderData
      );

      if (response.data.code === 1 && response.data.data) {
        console.log('Order created successfully for temp user:', response.data.data);
        return response.data.data;
      } else {
        console.error('Order creation for temp user failed:', response.data.msg);
        return null;
      }
    } catch (error) {
      console.error('Error creating order for temp user:', error);
      return null;
    }
  },

  /**
   * 格式化日期时间为ISO字符串（保留本地时区信息）
   * @param date 日期对象或日期字符串
   * @returns ISO格式的日期时间字符串
   */
  formatDateTimeForAPI(date: Date | string): string {
    const dateObj = typeof date === 'string' ? new Date(date) : date;

    // 创建YYYY-MM-DDTHH:MM:SS格式的字符串
    const year = dateObj.getFullYear();
    const month = String(dateObj.getMonth() + 1).padStart(2, '0');
    const day = String(dateObj.getDate()).padStart(2, '0');
    const hours = String(dateObj.getHours()).padStart(2, '0');
    const minutes = String(dateObj.getMinutes()).padStart(2, '0');
    const seconds = String(dateObj.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  }
}; 