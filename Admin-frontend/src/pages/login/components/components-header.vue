<template>
  <header class="login-header">
    <span class="logo-container" @click="navToHome">
      <img :src="escooterLogo" alt="Escooter Logo" class="logo" />
    </span>
    <div class="operations-container">
      <t-button theme="default" shape="square" variant="text" @click="navToGitHub">
        <logo-github-icon class="icon" />
      </t-button>
      <t-button theme="default" shape="square" variant="text" @click="toggleSettingPanel">
        <setting-icon class="icon" />
      </t-button>
    </div>
  </header>
</template>

<script>
import escooterLogo from '@/assets/escooterlogo.png';
import { LogoGithubIcon, SettingIcon } from 'tdesign-icons-vue';

export default {
  components: { LogoGithubIcon, SettingIcon },
  data() {
    return {
      escooterLogo,
    };
  },
  methods: {
    navToGitHub() {
      window.open('https://github.com/Zhoujiayi-Ryann/escooter');
    },
    navToHome() {
      this.$router.push('/dashboard/base');
    },
    toggleSettingPanel() {
      this.$store.commit('setting/toggleSettingPanel', true);
    },
  },
};
</script>

<style lang="less" scoped>
@import '@/style/variables.less';

.login-header {
  height: var(--td-comp-size-xxxl);
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  backdrop-filter: blur(5px);
  color: var(--td-text-color-primary);

  .logo-container {
    cursor: pointer;
  }

  .logo {
    height: 40px;
    width: auto;
    object-fit: contain;
  }

  .operations-container {
    display: flex;
    align-items: center;

    .t-button {
      margin-left: 16px;
    }

    .icon {
      height: 20px;
      width: 20px;
      padding: 6px;
      box-sizing: content-box;

      &:hover {
        cursor: pointer;
      }
    }
  }
}
</style>
