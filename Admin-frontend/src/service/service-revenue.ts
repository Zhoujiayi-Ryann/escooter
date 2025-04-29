import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export interface RevenueResponse {
    totalRevenue: number;
    dailyRevenue: number;
    revenueByDateRange: number;
}

export const getTotalRevenue = async (): Promise<number> => {
    try {
        const response = await axios.get(`${API_BASE_URL}/revenue/total`);
        return response.data;
    } catch (error) {
        console.error('Error fetching total revenue:', error);
        throw error;
    }
};

export const getDailyRevenue = async (): Promise<number> => {
    try {
        const response = await axios.get(`${API_BASE_URL}/revenue/daily`);
        return response.data;
    } catch (error) {
        console.error('Error fetching daily revenue:', error);
        throw error;
    }
};

export const getRevenueByDateRange = async (startDate: string, endDate: string): Promise<number> => {
    try {
        const response = await axios.get(`${API_BASE_URL}/revenue/range`, {
            params: {
                startDate,
                endDate
            }
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching revenue by date range:', error);
        throw error;
    }
}; 