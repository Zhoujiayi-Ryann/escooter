// 银行卡相关API
import { request } from '../request';
import { 
    ApiResponse, 
    AddCreditCardRequest, 
    AddCreditCardResponse, 
    CreditCardResponse 
} from '../types';

/**
 * 银行卡API模块
 */
export const creditCardApi = {
    /**
     * 添加银行卡
     * @param data 添加银行卡请求数据
     * @returns 添加的银行卡信息
     */
    addCreditCard(data: AddCreditCardRequest): Promise<ApiResponse<AddCreditCardResponse>> {
        return request<AddCreditCardResponse>({
            url: '/bank-cards',
            method: 'POST',
            data: data,
            needToken: true // 需要登录权限
        });
    },

    /**
     * 获取用户的银行卡列表
     * @param userId 用户ID
     * @returns 银行卡列表
     */
    getUserCreditCards(userId: number): Promise<ApiResponse<CreditCardResponse[]>> {
        return request<CreditCardResponse[]>({
            url: `/bank-cards?userId=${userId}`,
            method: 'GET',
            needToken: true // 需要登录权限
        });
    },

    /**
     * 删除银行卡
     * @param cardId 银行卡ID
     * @param userId 用户ID（用于验证权限）
     * @returns 删除结果
     */
    deleteCreditCard(cardId: number, userId: number): Promise<ApiResponse<string>> {
        return request<string>({
            url: `/bank-cards/${cardId}?userId=${userId}`,
            method: 'DELETE',
            needToken: true // 需要登录权限
        });
    }
}

/**
 * 使用示例：
 * 
 * import { creditCardApi } from '../utils/api/creditCard';
 * 
 * // 添加银行卡
 * const cardData = {
 *     user_id: 1,
 *     card_number: '6222023602480423785',
 *     security_code: '123',
 *     expiry_date: '2026-12-31',
 *     country: '中国'
 * };
 * 
 * creditCardApi.addCreditCard(cardData).then(res => {
 *     if (res.code === 1) { // 注意：银行卡接口成功的code是1
 *         console.log('银行卡添加成功:', res.data);
 *     }
 * });
 * 
 * // 获取用户银行卡列表
 * creditCardApi.getUserCreditCards(1).then(res => {
 *     if (res.code === 1) {
 *         console.log('用户银行卡列表:', res.data);
 *     }
 * });
 * 
 * // 删除银行卡
 * creditCardApi.deleteCreditCard(1, 1).then(res => {
 *     if (res.code === 1) {
 *         console.log('银行卡删除成功');
 *     }
 * });
 */ 