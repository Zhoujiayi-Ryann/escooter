interface IOption {
  value: number | string;
  label: string;
}
// 合同状态枚举
export const CONTRACT_STATUS = {
  FAIL: 0,
  AUDIT_PENDING: 1,
  EXEC_PENDING: 2,
  EXECUTING: 3,
  FINISH: 4,
};

export const CONTRACT_STATUS_OPTIONS: Array<IOption> = [
  { value: CONTRACT_STATUS.FAIL, label: '审核失败' },
  { value: CONTRACT_STATUS.AUDIT_PENDING, label: '待审核' },
  { value: CONTRACT_STATUS.EXEC_PENDING, label: '待履行' },
  { value: CONTRACT_STATUS.EXECUTING, label: '审核成功' },
  { value: CONTRACT_STATUS.FINISH, label: '已完成' },
];

// 合同类型枚举
export const CONTRACT_TYPES = {
  MAIN: 0,
  SUB: 1,
  SUPPLEMENT: 2,
};

export const CONTRACT_TYPE_OPTIONS: Array<IOption> = [
  { value: CONTRACT_TYPES.MAIN, label: '主合同' },
  { value: CONTRACT_TYPES.SUB, label: '子合同' },
  { value: CONTRACT_TYPES.SUPPLEMENT, label: '补充合同' },
];

// 合同收付类型枚举
export const CONTRACT_PAYMENT_TYPES = {
  PAYMENT: 0,
  RECEIPT: 1,
};

// 通知的优先级对应的TAG类型
export const NOTIFICATION_TYPES = {
  low: 'primary',
  middle: 'warning',
  high: 'danger',
};

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
