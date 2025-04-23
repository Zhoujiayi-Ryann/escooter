import { CartIcon, DashboardIcon } from 'tdesign-icons-vue';
import Layout from '@/layouts/index.vue';

export default [
  {
    path: '/user',
    name: 'user',
    component: Layout,
    redirect: '/user/index',
    meta: { title: '个人页', icon: 'user-circle' },
    children: [
      {
        path: 'index',
        name: 'UserIndex',
        component: () => import('@/pages/user/index.vue'),
        meta: { title: '个人中心' },
      },
    ],
  },
  {
    path: '/loginRedirect',
    name: 'loginRedirect',
    meta: { title: '登录页', icon: 'logout' },
    component: () => import('@/pages/login/index.vue'),
    children: [
      {
        path: 'index',
        redirect: '/login',
        component: () => import('@/layouts/blank.vue'),
        meta: { title: '登录中心' },
      },
    ],
  },
  {
    path: '/vehicle',
    name: 'vehicle',
    component: Layout,
    redirect: '/vehicle/manage',
    meta: { title: '车辆管理', icon: CartIcon },
    children: [
      {
        path: 'manage',
        name: 'VehicleManage',
        component: () => import('@/pages/vehicle/manage/index.vue'),
        meta: { title: '车辆列表' },
      },
    ],
  },
  {
    path: '/feedback',
    name: 'feedback',
    component: Layout,
    redirect: '/feedback/list',
    meta: { title: 'Feedback', icon: 'chat-double' },
    children: [
      {
        path: 'list',
        name: 'FeedbackList',
        component: () => import('@/pages/feedback/list/index.vue'),
        meta: { title: 'Feedback List' },
      },
    ],
  },
  {
    path: '/income',
    name: 'income',
    component: Layout,
    redirect: '/income/chart',
    meta: { title: 'Income', icon: DashboardIcon },
    children: [
      {
        path: 'chart',
        name: 'IncomeChart',
        component: () => import('@/pages/income/chart/index.vue'),
        meta: { title: 'Income Charts' },
      },
    ],
  },
  // 三级菜单配置
  // {
  //   path: '/menu',
  //   name: 'menu',
  //   component: Layout,
  //   meta: { title: '一级菜单', icon: 'menu-fold' },
  //   children: [
  //     {
  //       path: 'second',
  //       meta: { title: '二级菜单' },
  //       component: () => import('@/layouts/blank.vue'),
  //       children: [
  //         {
  //           path: 'third',
  //           name: 'NestMenu',
  //           component: () => import('@/pages/nest-menu/index.vue'),
  //           meta: { title: '三级菜单' },
  //         },
  //       ],
  //     },
  //   ],
  // },
];
