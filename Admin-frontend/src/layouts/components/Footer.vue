<template>
  <div :class="prefix + '-footer'">
  © 2021-{{ new Date().getFullYear() }} EScooter Rental System. All rights reserved.
</div>

</template>

<script>
import { prefix } from '@/config/global';
import Vue from 'vue';

export default Vue.extend({
  name: `${prefix}-footer`,
  data() {
    return {
      prefix,
    };
  },
});
</script>

<style lang="less" scoped>
@import '@/style/variables';

.@{starter-prefix}-footer {
  color: var(--td-text-color-placeholder);
  line-height: 20px;
  text-align: center;
}
</style>
