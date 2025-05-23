<template>
  <div>
    <t-drawer
      size="408px"
      :footer="false"
      :visible.sync="showSettingPanel"
      header="Page Configuration"
      :closeBtn="true"
      :onCloseBtnClick="handleCloseDrawer"
      class="setting-drawer-container"
    >
      <div class="setting-container">
        <t-form :data="formData" size="large" ref="form" labelAlign="left" @reset="onReset" @submit="onSubmit">
          <div class="setting-group-title">Theme Mode</div>
          <t-radio-group v-model="formData.mode">
            <div v-for="(item, index) in MODE_OPTIONS" :key="index" class="setting-layout-drawer">
              <div>
                <t-radio-button :key="index" :value="item.type">
                  <component :is="getModeIcon(item.type)" />
                </t-radio-button>
                <p style="text-align: center; margin-top: 8px">{{ item.text }}</p>
              </div>
            </div>
          </t-radio-group>

          <div class="setting-group-title">Brand Color</div>
          <t-radio-group v-model="formData.brandTheme">
            <div v-for="(item, index) in DEFAULT_COLOR_OPTIONS" :key="index" class="setting-layout-drawer">
              <t-radio-button :key="index" :value="item" class="setting-layout-color-group">
                <color-container :value="item" />
              </t-radio-button>
            </div>
            <div class="setting-layout-drawer">
              <t-popup
                destroy-on-close
                expand-animation
                placement="bottom-right"
                trigger="click"
                :visible="isColoPickerDisplay"
                @visible-change="onPopupVisibleChange"
                :overlayStyle="{ padding: 0 }"
              >
                <template #content>
                  <t-color-picker-panel
                    :on-change="changeColor"
                    :color-modes="['monochrome']"
                    format="HEX"
                    :swatch-colors="[]"
                  />
                </template>
                <t-radio-button :value="dynamicColor" :class="['setting-layout-color-group', 'dynamic-color-btn']">
                  <color-container :value="dynamicColor" />
                </t-radio-button>
              </t-popup>
            </div>
          </t-radio-group>

          <!-- <div class="setting-group-title">Layout</div>
          <t-radio-group v-model="formData.layout">
            <div v-for="(item, index) in LAYOUT_OPTION" :key="index" class="setting-layout-drawer">
              <t-radio-button :key="index" :value="item">
                <thumbnail :src="getThumbnailUrl(item)" />
              </t-radio-button>
            </div>
          </t-radio-group> -->

          <!-- <t-form-item v-show="formData.layout === 'mix'" label="Split Menu (for mix layout)" name="splitMenu">
            <t-switch v-model="formData.splitMenu"></t-switch>
          </t-form-item>

          <t-form-item v-show="formData.layout !== 'side'" label="Fixed Header" name="isHeaderFixed">
            <t-switch v-model="formData.isHeaderFixed"></t-switch>
          </t-form-item>

          <t-form-item v-show="formData.layout !== 'top'" label="Fixed Sidebar" name="isSidebarFixed">
            <t-switch v-model="formData.isSidebarFixed"></t-switch>
          </t-form-item>

          <div class="setting-group-title">Element Display</div>
          <t-form-item v-show="formData.layout === 'side'" label="Show Header" name="showHeader">
            <t-switch v-model="formData.showHeader"></t-switch>
          </t-form-item> -->

          <t-form-item label="Show Breadcrumb" name="showBreadcrumb">
            <t-switch v-model="formData.showBreadcrumb"></t-switch>
          </t-form-item>

          <t-form-item label="Show Footer" name="showFooter">
            <t-switch v-model="formData.showFooter"></t-switch>
          </t-form-item>

          <!-- <t-form-item label="Enable Tab Router" name="isUseTabsRouter">
            <t-switch v-model="formData.isUseTabsRouter"></t-switch>
          </t-form-item> -->

          <t-form-item
            label="Footer Inset"
            name="footerPosition"
            v-show="formData.showFooter && !formData.isSidebarFixed"
          >
            <t-switch v-model="formData.isFooterAside"></t-switch>
          </t-form-item>
        </t-form>

        <div class="setting-info">
          <p>Please copy and manually modify the config file: <code>/src/config/style.ts</code></p>
          <t-button theme="primary" variant="text" @click="handleCopy">Copy Config</t-button>
        </div>
      </div>
    </t-drawer>
  </div>
</template>

<script lang="ts">
import { mapGetters } from 'vuex';
import { Color } from 'tvision-color';
import { PopupVisibleChangeContext } from 'tdesign-vue';

import STYLE_CONFIG from '@/config/style';
import { insertThemeStylesheet, generateColorMap } from '@/utils/color';
import { DEFAULT_COLOR_OPTIONS } from '@/config/color';

import Thumbnail from '@/components/thumbnail/index.vue';
import ColorContainer from '@/components/color/index.vue';

import SettingDarkIcon from '@/assets/assets-setting-dark.svg';
import SettingLightIcon from '@/assets/assets-setting-light.svg';
import SettingAutoIcon from '@/assets/assets-setting-auto.svg';

const LAYOUT_OPTION = ['side', 'top', 'mix'];

const MODE_OPTIONS = [
  { type: 'light', text: 'light' },
  { type: 'dark', text: 'dark' },
  { type: 'auto', text: 'auto' },
];

export default {
  name: 'DefaultLayoutSetting',
  components: { Thumbnail, ColorContainer },
  data() {
    return {
      colors: {
        hex: null,
      },
      MODE_OPTIONS,
      LAYOUT_OPTION,
      DEFAULT_COLOR_OPTIONS,
      visible: false,
      formData: { ...STYLE_CONFIG },
      isColoPickerDisplay: false,
    };
  },
  computed: {
    ...mapGetters('setting', ['showSettingBtn']),
    showSettingPanel: {
      get() {
        return this.$store.state.setting.showSettingPanel;
      },
      set(newVal) {
        this.$store.commit('setting/toggleSettingPanel', newVal);
      },
    },
    iconName() {
      return this.visible ? 'close' : 'setting';
    },
    showOthers() {
      return (this.formData.showFooter && !this.formData.isSidebarFixed) || !this.formData.splitMenu;
    },
    dynamicColor() {
      const isDynamic = DEFAULT_COLOR_OPTIONS.indexOf(this.formData.brandTheme) === -1;
      return isDynamic ? this.formData.brandTheme : '';
    },
  },
  watch: {
    formData: {
      handler(newVal) {
        if (!newVal.brandTheme) return;
        // 没有在formData中 需要从store中同步过来
        const { isSidebarCompact } = this.$store.state.setting;
        this.$store.dispatch('setting/changeTheme', { ...newVal, isSidebarCompact });
      },
      deep: true,
    },
  },
  mounted() {
    document.querySelector('.dynamic-color-btn')?.addEventListener('click', () => {
      this.isColoPickerDisplay = true;
    });
  },
  methods: {
    onPopupVisibleChange(visible: boolean, context: PopupVisibleChangeContext) {
      if (!visible && context.trigger === 'document') this.isColoPickerDisplay = visible;
    },
    onReset(): void {
      this.formData = {
        ...STYLE_CONFIG,
      };
      this.$message.success('已恢复初始设置');
    },
    onSubmit({ result, firstError, e }): void {
      e.preventDefault();
      if (result === true) {
        this.visible = false;
      } else {
        this.$message.warning(firstError);
      }
    },
    getModeIcon(mode: string) {
      if (mode === 'light') {
        return SettingLightIcon;
      }
      if (mode === 'dark') {
        return SettingDarkIcon;
      }
      return SettingAutoIcon;
    },
    getThumbnailUrl(name: string) {
      return `https://tdesign.gtimg.com/starter/setting/${name}.png`;
    },
    handleClick(): void {
      this.$store.commit('setting/toggleSettingPanel', true);
    },
    handleCloseDrawer(): void {
      this.$store.commit('setting/toggleSettingPanel', false);
    },
    handleCopy(): void {
      const text = JSON.stringify(this.formData, null, 4);
      this.$copyText(text).then(() => {
        this.$message.closeAll();
        this.$message.success('复制成功');
      });
    },
    changeColor(hex: string) {
      const { setting } = this.$store.state;

      const { colors: newPalette, primary: brandColorIndex } = Color.getColorGradations({
        colors: [hex],
        step: 10,
        remainInput: false, // 是否保留输入 不保留会矫正不合适的主题色
      })[0];

      const { mode } = this.$store.state.setting;
      const colorMap = generateColorMap(hex, newPalette, mode, brandColorIndex);
      this.formData.brandTheme = hex;

      this.$store.commit('setting/addColor', { [hex]: colorMap });
      this.$store.dispatch('setting/changeTheme', { ...setting, brandTheme: hex });

      insertThemeStylesheet(hex, colorMap, mode);
    },
  },
};
</script>
<style lang="less">
@import '@/style/variables.less';

.tdesign-setting {
  z-index: 100;
  position: fixed;
  bottom: 200px;
  right: 0;
  transition: transform 0.3s cubic-bezier(0.7, 0.3, 0.1, 1), visibility 0.3s cubic-bezier(0.7, 0.3, 0.1, 1);
  height: 40px;
  width: 40px;
  border-radius: 20px 0 0 20px;
  transition: all 0.3s;

  .t-icon {
    margin-left: 8px;
  }

  .tdesign-setting-text {
    font-size: 12px;
    display: none;
  }

  &:hover {
    width: 96px;

    .tdesign-setting-text {
      display: inline-block;
    }
  }
}

.setting-layout-color-group {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  border-radius: 50% !important;
  padding: 6px !important;
  border: 2px solid transparent !important;

  > .t-radio-button__label {
    display: inline-flex;
  }
}

.tdesign-setting-close {
  position: fixed;
  bottom: 200px;
  right: 300px;
}

.setting-group-title {
  font-size: 14px;
  line-height: 22px;
  margin: 32px 0 24px 0;
  text-align: left;
  font-family: PingFang SC;
  font-style: normal;
  font-weight: 500;
  color: var(--td-text-color-primary);
}

.setting-group-color {
  position: relative;

  > div {
    position: absolute;
    z-index: 2;
    right: 0;
  }
}

.setting-link {
  cursor: pointer;
  color: var(--td-brand-color);
  margin-bottom: 8px;
}

.setting-info {
  position: absolute;
  padding: 24px;
  bottom: 0;
  left: 0;
  line-height: 20px;
  font-size: 12px;
  text-align: center;
  color: var(--td-text-color-placeholder);
  width: 100%;
  background: var(--td-bg-color-container);
}

.setting-drawer-container {
  .setting-container {
    padding-bottom: 100px;
  }

  .t-radio-group.t-size-m {
    min-height: 32px;

    width: 100%;
    height: auto;
    justify-content: space-between;
    align-items: center;
  }

  .setting-layout-drawer {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 16px;

    .t-radio-button {
      display: inline-flex;
      height: 100%;
      max-height: 78px;
      padding: 8px;
      border-radius: var(--td-radius-default);
      border: 2px solid var(--td-component-border);
      height: auto;

      > .t-radio-button__label {
        display: inline-flex;
      }
    }

    .t-is-checked {
      border: 2px solid var(--td-brand-color) !important;
    }

    .t-form__controls-content {
      justify-content: end;
    }
  }

  .t-form__controls-content {
    justify-content: end;
  }
}

.setting-route-theme {
  .t-form__label {
    min-width: 310px !important;
    color: var(--td-text-color-secondary);
  }
}

.setting-color-theme {
  .setting-layout-drawer {
    .t-radio-button {
      height: 32px;
    }

    &:last-child {
      margin-right: 0;
    }
  }
}
.setting-drawer-container .t-radio-group.t-radio-group__outline.t-size-m .t-radio-button {
  height: auto;
}
</style>
