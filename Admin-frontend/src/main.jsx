import Vue from 'vue';
import VueRouter from 'vue-router';
import { sync } from 'vuex-router-sync';
import TDesign from 'tdesign-vue';
import VueClipboard from 'vue-clipboard2';
import axiosInstance from '@/utils/request';
import App from './App.vue';
import router from './router';
// import zhConfig from 'tdesign-vue/es/locale/zh_CN';
import enConfig from 'tdesign-vue/es/locale/en_US'; // 英文多语言配置
import { initThemeFromLocalStorage } from '@/utils/initThemeFromStorage';
import 'tdesign-vue/es/style/index.css';
import '@/style/index.less';

import './permission';
import store from './store';

Vue.use(VueRouter);
Vue.use(TDesign);
Vue.use(VueClipboard);

Vue.component('t-page-header');

Vue.prototype.$request = axiosInstance;

const originPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originPush.call(this, location).catch((err) => err);
};

const originReplace = VueRouter.prototype.replace;
VueRouter.prototype.replace = function replace(location) {
  return originReplace.call(this, location).catch((err) => err);
};

Vue.config.productionTip = false;
sync(store, router);
const savedStyleConfig = localStorage.getItem('APP_STYLE_CONFIG');
if (savedStyleConfig) {
  const parsedConfig = JSON.parse(savedStyleConfig);
  store.commit('setting/update', parsedConfig);           // ✅ 设置 state
  store.dispatch('setting/changeTheme', parsedConfig);    // ✅ 应用颜色和 DOM 样式
}

initThemeFromLocalStorage();
// ✅ 2. 原本的 Vue 实例创建逻辑保持不变
new Vue({
  router,
  store,
  render: (h) => (
    <div>
      <t-config-provider globalConfig={enConfig}>
        <App />
      </t-config-provider>
    </div>
  ),
}).$mount('#app');
