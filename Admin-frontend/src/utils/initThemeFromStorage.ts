// src/utils/initThemeFromStorage.ts
import store from '@/store';
import { Color } from 'tvision-color';
import { insertThemeStylesheet, generateColorMap } from '@/utils/color';

export function initThemeFromLocalStorage() {
  const saved = localStorage.getItem('APP_STYLE_CONFIG');
  if (!saved) return;

  const config = JSON.parse(saved);

  // 1. 恢复 Vuex 状态
  store.commit('setting/update', config);

  // 2. 主动应用主题色样式
  const { brandTheme, mode } = config;

  const { colors: palette, primary: index } = Color.getColorGradations({
    colors: [brandTheme],
    step: 10,
    remainInput: false,
  })[0];

  const resolvedMode = mode === 'auto'
    ? window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
    : mode;

  const colorMap = generateColorMap(brandTheme, palette, resolvedMode, index);
  insertThemeStylesheet(brandTheme, colorMap, resolvedMode);

  document.documentElement.setAttribute('theme-mode', resolvedMode);
  document.documentElement.setAttribute('theme-color', brandTheme);
}
