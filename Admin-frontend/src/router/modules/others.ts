import { CartIcon, DashboardIcon, UserIcon, ShopIcon, PrintIcon } from 'tdesign-icons-vue';
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
    path: '/user-management',
    name: 'user-management',
    component: Layout,
    meta: { title: 'User Management', icon: UserIcon },
    children: [
      {
        path: 'manage',
        name: 'UserManage',
        component: () => import('@/pages/user-management/manage/index.vue'),
        meta: { title: 'User List' },
      },
      {
        path: 'details/:id',
        name: 'UserDetail',
        component: () => import('@/pages/user-management/details/index.vue'),
        meta: { title: 'User Details', hidden: true, activeMenu: '/user-management/manage' },
      },
    ],
  },
  {
    path: '/vehicle',
    name: 'vehicle',
    component: Layout,
    redirect: '/vehicle/manage',
    meta: { title: 'Vehicle Management', icon: CartIcon },
    children: [
      {
        path: 'manage',
        name: 'VehicleManage',
        component: () => import('@/pages/vehicle/manage/index.vue'),
        meta: { title: 'Vehicle List' },
      },
      {
        path: 'map',
        name: 'VehicleMap',
        component: () => import('@/pages/vehicle/map/index.vue'),
        meta: { title: 'Vehicle Map' },
      }
    ],
  },
  {
    path: '/Order',
    name: 'Order',
    component: Layout,
    meta: { title: 'Order Management', icon: ShopIcon },
    children: [
      {
        path: 'manage',
        name: 'Order List',
        component: () => import('@/pages/Order/manage/index.vue'),
        meta: { title: 'Order List' },
      }
    ],
  },
  {
    path: '/coupons',
    name: 'coupons',
    component: Layout,
    meta: { title: 'Coupons Management', icon: PrintIcon },
    children: [
      {
        path: 'coupons_list',
        name: 'Coupon List',
        component: () => import('@/pages/coupons/coupons_list/index.vue'),
        meta: { title: 'Coupon List' },
      }
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
