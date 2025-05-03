<template>
  <div class="login-wrapper">
    <login-header />

    <div class="login-container">
      <div class="title-container">
        <h1 class="title margin-no">Welcome to</h1>
        <h1 class="title">E-scooter Management System</h1>
      </div>

      <login />
      <tdesign-setting />
    </div>

    <footer class="copyright">Copyright @ 2021-2024 E-scooter. All Rights Reserved</footer>
  </div>
</template>
<script>
import Login from './components/components-login.vue';
import LoginHeader from './components/components-header.vue';
import TdesignSetting from '@/layouts/setting.vue';

export default {
  name: 'LoginIndex',
  components: {
    LoginHeader,
    Login,
    TdesignSetting,
  },
};
</script>
<style lang="less">
@import url('./index.less');
</style>
