// 反馈相关API
import { request } from '../request';
import { ApiResponse, FeedbackRequest, FeedbackResponse } from '../types';

/**
 * 反馈API模块
 */
export const feedbackApi = {
    /**
     * 提交反馈
     * @param data 反馈信息
     * @returns 提交结果
     */
    submitFeedback(data: FeedbackRequest): Promise<ApiResponse<FeedbackResponse>> {
        return request<FeedbackResponse>({
            url: '/feedback',
            method: 'POST',
            data: data
        });
    }
}

/**
 * 使用示例：
 * 
 * import { feedbackApi, FeedbackType } from '../utils/api/feedback';
 * 
 * // 提交反馈
 * feedbackApi.submitFeedback({
 *     user_id: 123,
 *     feedback_type: FeedbackType.USING_PROBLEM,
 *     description: '滑板车刹车不灵敏',
 *     happening_time: '2023-06-15T14:30:00Z',
 *     bill_number: '456',
 *     image_urls: ['http://example.com/image1.jpg']
 * }).then(res => {
 *     if (res.code === 1) {
 *         console.log('反馈提交成功', res.data);
 *     }
 * });
 */ 