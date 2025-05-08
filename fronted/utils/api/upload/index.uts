// 文件上传相关API
import { BASE_URL } from '../request';
import { request } from '../request';
import { ApiResponse } from '../types';

/**
 * 上传图片接口返回类型
 */
interface UploadResponse {
    code: number;
    msg: string;
    data: string[];
}

/**
 * 上传API模块
 */
export const uploadApi = {
    /**
     * 上传反馈图片
     * @param filePaths 本地图片路径数组
     * @returns 上传后的URL数组
     */
    async uploadFeedbackImages(filePaths: string[]): Promise<string[]> {
        if (!filePaths || filePaths.length === 0) {
            return [];
        }
        
        // 处理多张图片上传
        const uploadPromises = filePaths.map(filePath => this.uploadSingleImage(filePath));
        return Promise.all(uploadPromises);
    },
    
    /**
     * 上传单张图片
     * @param filePath 本地图片路径
     * @returns 上传后的URL
     */
    async uploadSingleImage(filePath: string): Promise<string> {
        return new Promise((resolve, reject) => {
            uni.uploadFile({
                url: BASE_URL + '/upload/feedback/images',
                filePath: filePath,
                name: 'files',
                success: (res) => {
                    try {
                        const result = JSON.parse(res.data) as UploadResponse;
                        if (result.code === 1 && result.data && result.data.length > 0) {
                            resolve(result.data[0]);
                        } else {
                            reject(new Error(result.msg || '上传失败'));
                        }
                    } catch (err) {
                        reject(new Error('解析响应失败'));
                    }
                },
                fail: (err) => {
                    reject(err);
                }
            });
        });
    },

    /**
     * 上传用户头像
     * @param filePath 本地文件路径
     * @param userId 用户ID
     * @returns 上传结果
     */
    uploadAvatar(filePath: string, userId: number): Promise<Result<string>> {
        return new Promise((resolve, reject) => {
            uni.uploadFile({
                url: `${BASE_URL}/users/avatar/upload/${userId}`,
                filePath: filePath,
                name: 'file',
                success: (res) => {
                    try {
                        const result = JSON.parse(res.data) as Result<string>;
                        if (result.code === 1) {
                            // 确保返回的URL是完整的
                            const avatarUrl = result.data.startsWith('http') ? 
                                result.data : 
                                `${BASE_URL}${result.data}`;
                            result.data = avatarUrl;
                        }
                        resolve(result);
                    } catch (e) {
                        reject(new Error('Response failed'));
                    }
                },
                fail: (err) => {
                    reject(new Error(`Fail to upload file: ${err.errMsg}`));
                }
            });
        });
    }
}

/**
 * 使用示例：
 * 
 * import { uploadApi } from '../../utils/api/upload';
 * 
 * // 上传图片
 * const images = await uni.chooseImage({
 *     count: 3,
 *     sizeType: ['compressed'],
 *     sourceType: ['album', 'camera']
 * });
 * 
 * try {
 *     const imageUrls = await uploadApi.uploadFeedbackImages(images.tempFilePaths);
 *     console.log('上传成功, 图片URL:', imageUrls);
 * } catch (err) {
 *     console.error('上传失败:', err);
 * }
 */ 