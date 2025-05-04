import axios from 'axios';

// API基础URL
export const BASE_URL = 'http://172.20.10.2:8080/api';

// 反馈状态映射
export const FEEDBACK_STATUS = {
  PENDING: 'PENDING',       // 待处理
  PROCESSING: 'PROCESSING', // 处理中
  RESOLVED: 'RESOLVED',     // 已解决
  REJECTED: 'REJECTED',     // 已拒绝
};

// 反馈优先级映射
export const FEEDBACK_PRIORITY = {
  LOW: 'LOW',       // 低
  MEDIUM: 'MEDIUM', // 中
  HIGH: 'HIGH',     // 高
  URGENT: 'URGENT', // 紧急
};

// 反馈类型映射
export const FEEDBACK_TYPE = {
  USING_PROBLEM: 'USING_PROBLEM',           // 使用问题
  EXPERIENCE_FEEDBACK: 'EXPERIENCE_FEEDBACK', // 体验反馈
};

// 响应类型接口
interface ApiResponse<T> {
  code: number;
  data: T;
  msg: string;
}

// 用户信息接口
export interface UserInfo {
  username: string;
  avatar: string;
}

// 反馈图片接口
export interface FeedbackImage {
  url: string;
  upload_time: string;
}

// 反馈详情响应接口
export interface FeedbackDetailResponse {
  id: number;
  user_id: number;
  feedback_type: string;
  description: string;
  status: string;
  priority: string;
  happening_time: string;
  bill_number: string;
  created_at: string;
  images: string[];
  user_info: UserInfo;
}

// 反馈列表项响应接口
export interface FeedbackListResponse {
  id: number;
  user_id: number;
  feedback_type: string;
  description: string;
  status: string;
  priority: string;
  happening_time: string;
  bill_number: string;
  created_at: string;
  images: FeedbackImage[];
}

// 更新反馈请求接口
export interface UpdateFeedbackRequest {
  description: string;
  status?: string;
  priority?: string;
}

// 删除反馈响应接口
export interface DeleteFeedbackResponse {
  deleted_feedback_id: number;
  deleted_images_count: number;
}

// 表格数据适配接口
export interface TableFeedback {
  id: number;
  userId: number;
  userName: string;
  type: string;
  description: string;
  status: string;
  priority: string;
  createdAt: string;
  imagesCount: number;
}

// 将后端反馈数据转换为表格数据
const mapFeedbackToTableData = (feedback: FeedbackListResponse): TableFeedback => {
  return {
    id: feedback.id,
    userId: feedback.user_id,
    userName: `User${feedback.user_id}`, // 实际应该从用户服务获取
    type: feedback.feedback_type,
    description: feedback.description.length > 30
      ? `${feedback.description.substring(0, 30)}...`
      : feedback.description,
    status: feedback.status,
    priority: feedback.priority,
    createdAt: new Date(feedback.created_at).toLocaleString(),
    imagesCount: feedback.images ? feedback.images.length : 0,
  };
};

// 回复反馈请求接口
export interface FeedbackReplyRequest {
  admin_id: number;
  content: string;
  send_notification?: boolean;
}

// 回复反馈响应接口
export interface FeedbackReplyResponse {
  feedback_id: number;
  user_id: number;
  admin_id: number;
  content: string;
  reply_time: string;
  status: string;
  notification_sent: boolean;
}

// 反馈服务类
export const feedbackService = {
  /**
   * 获取所有反馈
   */
  async getAllFeedback(): Promise<TableFeedback[]> {
    try {
      const response = await axios.get<ApiResponse<FeedbackListResponse[]>>(`${BASE_URL}/feedback_list`);

      if (response.data.code === 1 && response.data.data) {
        // 将API响应转换为表格数据
        return response.data.data.map(mapFeedbackToTableData);
      }
      return [];
    } catch (error) {
      console.error('Get feedback list failed:', error);
      return [];
    }
  },

  /**
   * 根据ID获取反馈详情
   */
  async getFeedbackDetail(feedbackId: number): Promise<FeedbackDetailResponse | null> {
    try {
      const response = await axios.get<ApiResponse<FeedbackDetailResponse>>(`${BASE_URL}/feedback/${feedbackId}`);

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error(`Get feedback detail(ID: ${feedbackId}) failed:`, error);
      return null;
    }
  },

  /**
   * 更新反馈
   */
  async updateFeedback(feedbackId: number, feedbackData: UpdateFeedbackRequest): Promise<FeedbackDetailResponse | null> {
    try {
      const response = await axios.put<ApiResponse<FeedbackDetailResponse>>(
        `${BASE_URL}/feedback/${feedbackId}`,
        feedbackData
      );

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error(`Update feedback(ID: ${feedbackId}) failed:`, error);
      return null;
    }
  },

  /**
   * 删除反馈
   */
  async deleteFeedback(feedbackId: number): Promise<DeleteFeedbackResponse | null> {
    try {
      const response = await axios.delete<ApiResponse<DeleteFeedbackResponse>>(`${BASE_URL}/feedback/${feedbackId}`);

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error(`Delete feedback(ID: ${feedbackId}) failed:`, error);
      return null;
    }
  },

  /**
   * 获取反馈状态文本
   */
  getStatusText(status: string): string {
    switch (status) {
      case FEEDBACK_STATUS.PENDING:
        return 'Pending';
      case FEEDBACK_STATUS.PROCESSING:
        return 'Processing';
      case FEEDBACK_STATUS.RESOLVED:
        return 'Resolved';
      case FEEDBACK_STATUS.REJECTED:
        return 'Rejected';
      default:
        return 'Unknown status';
    }
  },

  /**
   * 获取反馈优先级文本
   */
  getPriorityText(priority: string): string {
    switch (priority) {
      case FEEDBACK_PRIORITY.LOW:
        return 'Low';
      case FEEDBACK_PRIORITY.MEDIUM:
        return 'Medium';
      case FEEDBACK_PRIORITY.HIGH:
        return 'High';
      case FEEDBACK_PRIORITY.URGENT:
        return 'Urgent';
      default:
        return 'Unknown priority';
    }
  },

  /**
   * 获取反馈类型文本
   */
  getTypeText(type: string): string {
    switch (type) {
      case FEEDBACK_TYPE.USING_PROBLEM:
        return 'Using problem';
      case FEEDBACK_TYPE.EXPERIENCE_FEEDBACK:
        return 'Experience feedback';
      default:
        return 'Unknown type';
    }
  },

  /**
   * 管理员回复反馈
   * @param feedbackId 反馈ID
   * @param replyData 回复数据
   * @returns 回复结果
   */
  async replyFeedback(feedbackId: number, replyData: FeedbackReplyRequest): Promise<FeedbackReplyResponse | null> {
    try {
      const response = await axios.post<ApiResponse<FeedbackReplyResponse>>(
        `${BASE_URL}/feedback/${feedbackId}/reply`,
        replyData
      );

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error(`Admin reply feedback(ID: ${feedbackId}) failed:`, error);
      return null;
    }
  },
}; 