<template>
    <div class="user-detail">
        <!-- 返回按钮 -->
    <t-button @click="goBack" class="back-btn" variant="base">返回列表</t-button>
      <!-- 用户基本信息 -->
      <t-card title="用户基本信息" :bordered="false" class="info-block">
        <t-descriptions>
          <t-descriptions-item v-for="(item, index) in userInfoData" :key="index" :label="item.label">
            <span>{{ item.value }}</span>
          </t-descriptions-item>
        </t-descriptions>
      </t-card>
  
      <!-- 操作记录 -->
      <t-card title="操作记录" class="container-base-margin-top" :bordered="false">
        <t-steps class="user-detail-steps" layout="vertical" theme="dot" :current="1">
          <t-step-item title="修改密码" content="2025-04-20 14:23:00 管理员操作" />
          <t-step-item title="更新邮箱地址" content="2025-04-18 09:15:30 用户自行操作" />
          <t-step-item title="用户注册成功" content="2025-04-15 08:00:00 系统自动记录" />
        </t-steps>
      </t-card>
    </div>
  </template>
  
  <script>
  export default {
    name: 'UserDetail',
    data() {
      return {
        userInfoData: [
          { label: '用户名', value: '张三' },
          { label: '邮箱', value: 'zhangsan@example.com' },
          { label: '手机号', value: '13800001234' },
          { label: '角色', value: '管理员' },
          { label: '状态', value: '启用' },
          { label: '注册时间', value: '2025-04-15' },
        ],
      };
    },
    mounted() {
  const userStr = sessionStorage.getItem('currentUser');
  if (userStr) {
    const user = JSON.parse(userStr);
    this.userInfoData = [
      { label: '用户名', value: user.name },
      { label: '邮箱', value: user.email },
      { label: '手机号', value: user.phone },
      { label: '角色', value: user.role === 'admin' ? '管理员' : '普通用户' },
      { label: '状态', value: user.status === 'active' ? '启用' : '禁用' },
      { label: '注册时间', value: user.createdAt },
    ];
  } else {
    this.$message.error('用户信息丢失，请返回列表重新选择');
  }
},
    methods: {
        goBack() {
            this.$router.push('/user-management/manage');
        },
    }
  };
  </script>
  
  <style lang="less" scoped>
  .user-detail {
    .back-btn {
      margin-bottom: 16px;
    }
  
    /deep/ .t-card {
      padding: 8px;
    }
  
    /deep/ .t-card__title {
      font-size: 20px;
      font-weight: 500;
    }
  
    .user-detail-steps {
      padding-top: 12px;
    }
  }
  
  .info-block {
    span {
      margin-left: 24px;
    }
  }
  </style>
  