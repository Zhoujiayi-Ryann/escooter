<template>
  <t-form
    ref="form"
    class="item-container"
    :data="formData"
    :rules="FORM_RULES"
    label-width="0"
    @submit="onSubmit"
  >
    <t-form-item name="account">
      <t-input v-model="formData.account" size="large" placeholder="Enter your account">
        <template #prefix-icon>
          <user-icon />
        </template>
      </t-input>
    </t-form-item>

    <t-form-item name="password">
      <t-input
        v-model="formData.password"
        size="large"
        :type="showPsw ? 'text' : 'password'"
        clearable
        placeholder="Enter your password"
      >
        <template #prefix-icon>
          <lock-on-icon />
        </template>
        <template #suffix-icon>
          <browse-icon v-if="showPsw" @click="showPsw = !showPsw" key="browse" />
          <browse-off-icon v-else @click="showPsw = !showPsw" key="browse-off" />
        </template>
      </t-input>
    </t-form-item>

    <t-form-item class="btn-container">
      <t-button block size="large" type="submit">Login</t-button>
    </t-form-item>
  </t-form>
</template>

<script lang="ts">
import Vue from 'vue';
import { UserIcon, LockOnIcon, BrowseOffIcon, BrowseIcon } from 'tdesign-icons-vue';
import { adminLogin } from '@/service/service-user';

const INITIAL_DATA = {
  account: '',
  password: '',
};

const FORM_RULES = {
  account: [{ required: true, message: 'Account is required', type: 'error' }],
  password: [{ required: true, message: 'Password is required', type: 'error' }],
};

export default Vue.extend({
  name: 'Login',
  components: {
    UserIcon,
    LockOnIcon,
    BrowseOffIcon,
    BrowseIcon,
  },
  data() {
    return {
      FORM_RULES,
      formData: { ...INITIAL_DATA },
      showPsw: false,
    };
  },
  methods: {
    async onSubmit({ validateResult }) {
      if (validateResult === true) {
        try {
          const response = await adminLogin(this.formData.account, this.formData.password);
          
          if (response.code === 1 && response.data) {
            // 保存登录信息
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('userId', response.data.user_id.toString());
            localStorage.setItem('username', response.data.username);
            
            this.$message.success('Login successful');

            // 使用window.location进行页面跳转
            window.location.href = '/dashboard/base';
          } else {
            this.$message.error(response.msg || 'Login failed');
          }
        } catch (error) {
          console.error('Login error:', error);
          this.$message.error(error.response?.data?.msg || 'Login failed');
        }
      }
    },
  },
});
</script>
