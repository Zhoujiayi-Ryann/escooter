<template>
  <div :class="layoutCls">
    <t-head-menu :class="menuCls" :theme="theme" expandType="popup" :value="active">
      <template #logo>
        <span v-if="showLogo" class="header-logo-container" @click="handleNav('/dashboard/base')">
    <img :src="escooterLogo" alt="Escooter Logo" class="t-logo" />
  </span>
        <div v-else class="header-operate-left">
          <t-button theme="default" shape="square" variant="text" @click="changeCollapsed">
            <view-list-icon class="collapsed-icon" />
          </t-button>
          <search :layout="layout" />
        </div>
      </template>
      <menu-content v-show="layout !== 'side'" class="header-menu" :navData="menu" />
      <template #operations>
        <div class="operations-container">
          <!-- 搜索框 -->
          <!-- <search v-if="layout !== 'side'" :layout="layout" /> -->

          <!-- 全局通知 -->
          <!-- <notice /> -->

          <t-tooltip placement="bottom" content="GitHub">
            <t-button theme="default" shape="square" variant="text" @click="navToGitHub">
              <logo-github-icon />
            </t-button>
          </t-tooltip>
          <!-- <t-tooltip placement="bottom" content="帮助文档">
            <t-button theme="default" shape="square" variant="text" @click="navToHelper">
              <help-circle-icon />
            </t-button>
          </t-tooltip> -->
          <t-dropdown :min-column-width="125" trigger="click">
            <template #dropdown>
              <t-dropdown-menu>
                <t-dropdown-item class="operations-dropdown-container-item" @click="handleNav('/user/index')">
                  <user-circle-icon />Personal
                </t-dropdown-item>
                <t-dropdown-item class="operations-dropdown-container-item" @click="handleLogout">
                  <poweroff-icon />Logout
                </t-dropdown-item>
              </t-dropdown-menu>
            </template>
            <t-button class="header-user-btn" theme="default" variant="text">
              <template #icon>
                <user-circle-icon class="header-user-avatar" />
              </template>
              <div class="header-user-account">Admin</div>
              <template #suffix>
                <chevron-down-icon />
              </template>
            </t-button>
          </t-dropdown>
          <t-tooltip placement="bottom" content="Settings">
            <t-button theme="default" shape="square" variant="text" @click="toggleSettingPanel">
              <setting-icon />
            </t-button>
          </t-tooltip>
        </div>
      </template>
    </t-head-menu>
  </div>
</template>

<script>
import escooterLogo from '@/assets/escooterlogo.png';
import Vue from 'vue';
import {
  ViewListIcon,
  LogoGithubIcon,
  HelpCircleIcon,
  UserCircleIcon,
  PoweroffIcon,
  SettingIcon,
  ChevronDownIcon,
  
} 
from 'tdesign-icons-vue';
import { prefix } from '@/config/global';
// import LogoFull from '@/assets/assets-logo-full.svg';

import Notice from './Notice.vue';
import Search from './Search.vue';
import MenuContent from './MenuContent.vue';

export default Vue.extend({
  components: {
    MenuContent,
    // LogoFull,
    Notice,
    Search,
    ViewListIcon,
    LogoGithubIcon,
    HelpCircleIcon,
    UserCircleIcon,
    PoweroffIcon,
    SettingIcon,
    ChevronDownIcon,
    escooterLogo
  },
  props: {
    theme: String,
    layout: {
      type: String,
      default: 'top',
    },
    showLogo: {
      type: Boolean,
      default: true,
    },
    menu: {
      type: Array,
    },
    isFixed: {
      type: Boolean,
      default: false,
    },
    isCompact: {
      type: Boolean,
      default: false,
    },
    maxLevel: {
      type: Number,
      default: 3,
    },
  },
  data() {
    return {
      prefix,
      visibleNotice: false,
      isSearchFocus: false,
    };
  },
  computed: {
    active() {
      if (!this.$route.path) {
        return '';
      }
      return this.$route.path
        .split('/')
        .filter((item, index) => index <= this.maxLevel && index > 0)
        .map((item) => `/${item}`)
        .join('');
    },
    showMenu() {
      return !(this.layout === 'mix' && this.showLogo === 'side');
    },
    layoutCls() {
      return [`${this.prefix}-header-layout`];
    },
    menuCls() {
      return [
        {
          [`${this.prefix}-header-menu`]: !this.isFixed,
          [`${this.prefix}-header-menu-fixed`]: this.isFixed,
          [`${this.prefix}-header-menu-fixed-side`]: this.layout === 'side' && this.isFixed,
          [`${this.prefix}-header-menu-fixed-side-compact`]: this.layout === 'side' && this.isFixed && this.isCompact,
        },
      ];
    },
  },
  methods: {
    toggleSettingPanel() {
      this.$store.commit('setting/toggleSettingPanel', true);
    },
    handleLogout() {
      this.$router.push(`/login?redirect=${this.$router.history.current.fullPath}`);
    },
    changeCollapsed() {
      this.$store.commit('setting/toggleSidebarCompact');
    },
    handleNav(url) {
      this.$router.push(url);
    },
    navToGitHub() {
      window.open('https://github.com/Zhoujiayi-Ryann/escooter');
    },
    navToHelper() {
      window.open('http://tdesign.tencent.com/starter/docs/get-started');
    },
  },
});
</script>
<style lang="less">
@import '@/style/variables.less';
.logo-header {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  background-color: white;
}

.logo-img {
  height: 100%; /* 根据你想显示的完整高度设置，比如原图高100px */
  width: auto;   /* 让宽度自适应，避免拉伸或压缩 */
  object-fit: contain; /* 保证按原比例缩放 */
}
.header-menu {
  flex: 1 1 1;
  display: inline-flex;
}

.operations-container {
  display: flex;
  align-items: center;
  margin-right: 12px;

  .t-popup__reference {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .t-button {
    margin: 0 8px;

    &.header-user-btn {
      margin: 0;
    }
  }

  .t-icon {
    font-size: 20px;

    &.general {
      margin-right: 16px;
    }
  }
}

.header-operate-left {
  display: flex;
  margin-left: 20px;
  align-items: normal;
  line-height: 0;

  .collapsed-icon {
    font-size: 20px;
  }
}

.header-logo-container {
  width: 184px;
  height: 26px;
  display: flex;
  margin-left: 24px;
  color: var(--td-text-color-primary);

  .t-logo {
    width: 100%;
    height: 100%;

    &:hover {
      cursor: pointer;
    }
  }

  &:hover {
    cursor: pointer;
  }
}

.header-user-account {
  display: inline-flex;
  align-items: center;
  color: var(--td-text-color-primary);

  .t-icon {
    margin-left: 4px;
    font-size: 16px;
  }
}

.t-head-menu__inner {
  border-bottom: 1px solid var(--td-border-level-1-color);
}

.t-menu--light {
  .header-user-account {
    color: var(--td-text-color-primary);
  }
}

.t-menu--dark {
  .t-head-menu__inner {
    border-bottom: 1px solid var(--td-gray-color-10);
  }

  .header-user-account {
    color: rgba(255, 255, 255, 0.55);
  }

  .t-button {
    --ripple-color: var(--td-gray-color-10) !important;

    &:hover {
      background: var(--td-gray-color-12) !important;
    }
  }
}

.operations-dropdown-container-item {
  width: 100%;
  display: flex;
  align-items: center;

  .t-icon {
    margin-right: 8px;
  }

  .t-dropdown__item {
    .t-dropdown__item__content {
      display: flex;
      justify-content: center;
    }

    .t-dropdown__item__content__text {
      display: flex;
      align-items: center;
      font-size: 14px;
    }
  }

  .t-dropdown__item {
    width: 100%;
    margin-bottom: 0px;
  }

  &:last-child {
    .t-dropdown__item {
      margin-bottom: 8px;
    }
  }
  
}
</style>
