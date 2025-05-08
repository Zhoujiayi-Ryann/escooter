import axios from 'axios';

// API基础URL
const BASE_URL = 'https://khnrsggvzudb.sealoshzh.site/api';

// 订单状态枚举
export enum OrderStatus {
    PENDING = "pending",
    PAID = "paid",
    ACTIVE = "active",
    COMPLETED = "completed",
    CANCELLED = "cancelled"
}

// 订单响应类型
export interface OrderResponse {
    order_id: number;
    user_id: number;
    scooter_id: number;
    start_time: string;
    end_time: string;
    new_end_time: string | null;
    extended_duration: number | null;
    extended_cost: number | null;
    cost: number;
    discount_amount: number;
    pickup_address: string;
    status: OrderStatus;
    created_at: string;
    is_deleted: number;
}

export interface ApiResponse<T> {
    code: number;
    msg: string;
    data: T;
}

// 获取所有订单
export const getAllOrders = async (): Promise<ApiResponse<OrderResponse[]>> => {
    try {
        const response = await axios.get(`${BASE_URL}/orders`);
        return response.data;
    } catch (error) {
        console.error('Obtain order data failed:', error);
        throw error;
    }
};
