import axios from 'axios';

// API基础URL
const BASE_URL = 'http://localhost:8080/api';

// 响应类型接口
interface ApiResponse<T> {
  code: number;
  data: T;
  msg: string;
}

// 登录响应数据类型
interface LoginResponseData {
  user_id: number;
  username: string;
  token: string;
}

export const USER_INFO_LIST = [
  {
    title: '手机',
    content: '+86 13923734567',
  },
  {
    title: '座机',
    content: '734567',
  },
  {
    title: '办公室邮箱',
    content: 'Account@qq.com',
  },
  {
    title: '座位',
    content: 'T32F 012',
  },
  {
    title: '管理主体',
    content: '腾讯集团',
  },
  {
    title: '直属上级',
    content: 'Michael Wang',
  },
  {
    title: '职位',
    content: '高级 UI 设计师',
  },
  {
    title: '入职时间',
    content: '2021-07-01',
  },
  {
    title: '所属团队',
    content: '腾讯/腾讯公司/某事业群/某产品部/某运营中心/商户服务组',
    span: 6,
  },
];

export const TEAM_MEMBERS = [
  {
    avatar: 'https://avatars.githubusercontent.com/Wen1kang',
    title: 'Lovellzhong 钟某某',
    description: '直客销售 港澳拓展组员工',
  },
  {
    avatar: 'https://avatars.githubusercontent.com/pengYYYYY',
    title: 'Jiajingwang 彭某某',
    description: '前端开发 前台研发组员工',
  },
  {
    avatar: 'https://avatars.githubusercontent.com/u/24469546?s=96&v=4',
    title: 'cruisezhang 林某某',
    description: '技术产品 产品组员工',
  },
  {
    avatar: 'https://avatars.githubusercontent.com/u/88708072?s=96&v=4',
    title: 'Lovellzhang 商某某',
    description: '产品运营 港澳拓展组员工',
  },
];

export const PRODUCT_LIST = ['a', 'b', 'c', 'd'];

export interface IFrequentUser {
  user_id: number;
  username: string;
  total_usage_hours: number;
  total_spent: number;
  user_types: string[];
  avatar_path: string;
}

export interface IUser {
  userId: number;
  username: string;
  email: string;
  phoneNumber: string;
  registrationDate: string;
  totalUsageHours: number;
  totalSpent: number;
  userTypes: string[];
  avatarPath: string;
  isDisabled: boolean;
}

/**
 * 查询常用用户（使用时长超过50小时）
 * @returns 常用用户列表
 */
export const getFrequentUsers = async (): Promise<IFrequentUser[]> => {
  try {
    const response = await axios.get<ApiResponse<IFrequentUser[]>>(`${BASE_URL}/users/frequent`);

    if (response.data.code === 1 && response.data.data) {
      return response.data.data;
    }
    return [];
  } catch (error) {
    console.error('Failed to get frequent users:', error);
    return [];
  }
};

/**
 * 获取所有非管理员用户
 * @returns 非管理员用户列表
 */
export const getAllNonAdminUsers = async (): Promise<IUser[]> => {
  try {
    const response = await axios.get<ApiResponse<IUser[]>>(`${BASE_URL}/users/non-admin`);

    if (response.data.code === 1 && response.data.data) {
      return response.data.data;
    }
    return [];
  } catch (error) {
    console.error('Failed to get non-admin users:', error);
    return [];
  }
};

/**
 * 切换用户禁用状态
 * @param userId 用户ID
 * @returns 更新后的用户信息
 */
export const toggleUserDisabledStatus = async (userId: number): Promise<IUser> => {
  try {
    const response = await axios.patch<ApiResponse<IUser>>(`${BASE_URL}/users/${userId}/toggle-disabled`);

    if (response.data.code === 1 && response.data.data) {
      return response.data.data;
    }
    throw new Error(response.data.msg || 'Failed to toggle user status');
  } catch (error) {
    console.error('Failed to toggle user status:', error);
    throw error;
  }
};

/**
 * 管理员登录
 * @param username 用户名
 * @param password 密码
 * @returns 登录响应
 */
export const adminLogin = async (username: string, password: string): Promise<ApiResponse<LoginResponseData>> => {
  try {
    const response = await axios.post<ApiResponse<LoginResponseData>>(`${BASE_URL}/users/admin/login`, {
      username,
      password
    });
    return response.data;
  } catch (error) {
    console.error('Admin login failed:', error);
    throw error;
  }
};

