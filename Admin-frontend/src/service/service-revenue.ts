import axios from 'axios';

const BASE_URL = 'https://khnrsggvzudb.sealoshzh.site/api';

export interface RevenueResponse {
    totalRevenue: number;
    dailyRevenue: number;
}

export interface ApiResponse<T> {
    code: number;
    msg: string;
    data: T;
}

export interface DurationRevenue {
    lessThanOneHour: number;
    oneToFourHours: number;
    moreThanFourHours: number;
}

export interface RevenueStatistics {
    totalRevenue: number;
    dailyRevenue: Record<string, number>;
    dailyDurationRevenue: Record<string, DurationRevenue>;
}

export const getTotalRevenue = async (): Promise<number> => {
    try {
        const response = await axios.get(`${BASE_URL}/total-revenue`);
        return response.data;
    } catch (error) {
        console.error('Error fetching total revenue:', error);
        throw error;
    }
};

/**
 * Get the total revenue
 * @returns the total revenue
 */
export const getDailyRevenue = async (): Promise<number> => {
    try {
        const response = await axios.get(`${BASE_URL}/daily-revenue`);
        return response.data;
    } catch (error) {
        console.error('Error fetching daily revenue:', error);
        throw error;
    }
};

/**
 * Get the revenue statistics data of the specified time period
 * @param startDate the start date (yyyy-MM-dd)
 * @param endDate the end date (yyyy-MM-dd)
 * @returns the revenue statistics data
 */
export const getRevenueStatistics = async (startDate: string, endDate: string): Promise<RevenueStatistics> => {
    try {
        const response = await axios.get<ApiResponse<RevenueStatistics>>(`${BASE_URL}/revenue-statistics`, {
            params: {
                start_date: startDate,
                end_date: endDate
            }
        });

        if (response.data.code === 1 && response.data.data) {
            return response.data.data;
        }
        throw new Error(response.data.msg || 'Obtain revenue statistics data failed');
    } catch (error) {
        console.error('Obtain revenue statistics data failed:', error);
        throw error;
    }
};

/**
 * Get the total order count (excluding pending orders)
 * @returns the total order count
 */
export const getTotalOrderCount = async (): Promise<number> => {
    try {
        const response = await axios.get<ApiResponse<number>>(`${BASE_URL}/total-order-count`);
        if (response.data.code === 1 && response.data.data !== undefined) {
            return response.data.data;
        }
        throw new Error(response.data.msg || 'Obtain total order count failed');
    } catch (error) {
        console.error('Obtain total order count failed:', error);
        throw error;
    }
}; 