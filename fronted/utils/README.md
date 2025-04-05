# API 模块使用说明

本项目的API模块采用了模块化的架构设计，按功能将API划分为不同的模块，提高了代码的可读性和可维护性。

## 目录结构

```
utils/api/
  ├── index.uts          # 统一导出文件
  ├── request.uts        # 请求核心方法
  ├── types.uts          # 类型定义
  ├── user/              # 用户相关API
  │   └── index.uts
  ├── scooter/           # 滑板车相关API
  │   └── index.uts
  ├── order/             # 订单相关API
  │   └── index.uts
  └── creditCard/        # 银行卡相关API
      └── index.uts
```

## 使用方法

### 1. 导入API模块

你可以按需导入所需的API模块：

```typescript
// 导入单个模块
import { userApi } from '../utils/api/user';

// 或者从统一导出文件中导入多个模块
import { userApi, orderApi, creditCardApi } from '../utils/api';
```

### 2. 调用API方法

每个API模块都导出了一个对象，包含了该模块的所有API方法：

```typescript
// 用户登录
userApi.login({
    username: 'user123',
    password: 'password123'
}).then(res => {
    if (res.code === 0) {
        console.log('登录成功:', res.data);
    }
});

// 获取滑板车列表
scooterApi.getScooters().then(res => {
    if (res.code === 0) {
        console.log('可用滑板车:', res.data);
    }
});
```

### 3. 银行卡API使用说明

银行卡API模块提供了添加、查询和删除银行卡的功能：

```typescript
import { creditCardApi } from '../utils/api';

// 添加银行卡
const cardData = {
    user_id: 1,
    card_number: '6222023602480423785',
    security_code: '123',
    expiry_date: '2026-12-31',
    country: '中国'
};

creditCardApi.addCreditCard(cardData).then(res => {
    if (res.code === 1) { // 注意：银行卡接口成功的code是1
        console.log('银行卡添加成功:', res.data);
    }
});

// 获取用户银行卡列表
creditCardApi.getUserCreditCards(1).then(res => {
    if (res.code === 1) {
        console.log('用户银行卡列表:', res.data);
    }
});

// 删除银行卡
creditCardApi.deleteCreditCard(1, 1).then(res => {
    if (res.code === 1) {
        console.log('银行卡删除成功');
    }
});
```

注意：银行卡接口的成功状态码是`1`，这与其他接口可能不同（其他接口通常是`0`）。

## 错误处理

所有API调用都应包含错误处理：

```typescript
creditCardApi.addCreditCard(cardData)
    .then(res => {
        if (res.code === 1) {
            // 处理成功情况
        } else {
            // 处理业务错误
            uni.showToast({
                title: res.msg || '操作失败',
                icon: 'none'
            });
        }
    })
    .catch(err => {
        // 处理网络错误等异常
        console.error('请求异常:', err);
        uni.showToast({
            title: '网络异常，请稍后重试',
            icon: 'none'
        });
    });
``` 