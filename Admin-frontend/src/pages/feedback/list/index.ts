interface IOption {
  value: number | string;
  label: string;
}
// 反馈状态枚举
export const FEEDBACK_STATUS = {
  PENDING: 0,
  EXECUTING: 1,
  FINISH: 2,
};

export const FEEDBACK_STATUS_OPTIONS: Array<IOption> = [
  { value: FEEDBACK_STATUS.PENDING, label: 'Todo' },
  { value: FEEDBACK_STATUS.EXECUTING, label: 'Processing' },
  { value: FEEDBACK_STATUS.FINISH, label: 'Finish' },
];

// 反馈类型枚举
export const FEEDBACK_TYPES = {
  USING: 0,
  EXPERIENCE: 1
};

export const FEEDBACK_TYPE_OPTIONS: Array<IOption> = [
  { value: FEEDBACK_TYPES.USING, label: 'Using Problems' },
  { value: FEEDBACK_TYPES.EXPERIENCE, label: 'Experience Feedback' },
];

// 反馈类型枚举
// export const CONTRACT_PAYMENT_TYPES = {
//   PAYMENT: 0,
//   RECEIPT: 1,
// };

// 通知的优先级对应的TAG类型
export const PRIORITY = {
  low: 'primary',
  high: 'danger',
};