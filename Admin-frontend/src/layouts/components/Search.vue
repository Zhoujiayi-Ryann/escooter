<template>
  <div>
    <!-- Sidebar Layout -->
    <div v-if="layout === 'side'" class="header-menu-search">
      <t-input
        v-model="searchText"
        placeholder="Enter keyword to search"
        @focus="handleFocus(true)"
        @blur="handleFocus(false)"
        clearable
        @enter="emitSearch"
        :class="{ 'hover-active': isFocused }"
      >
        <template #prefix-icon>
          <search-icon size="16" />
        </template>
      </t-input>
    </div>

    <!-- Top Header Layout -->
    <div v-else class="header-menu-search-left">
      <t-button
        theme="default"
        shape="square"
        variant="text"
        :class="{ 'search-icon-hide': isFocused }"
        @click="handleFocus(true)"
      >
        <search-icon />
      </t-button>
      <t-input
        ref="inputRef"
        v-model="searchText"
        :class="['header-search', { 'width-zero': !isFocused }]"
        placeholder="Search content"
        clearable
        :autofocus="isFocused"
        @blur="handleFocus(false)"
        @enter="emitSearch"
      >
        <template #prefix-icon>
          <search-icon size="16" />
        </template>
      </t-input>
    </div>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from 'vue';
import { SearchIcon } from 'tdesign-icons-vue';

export default Vue.extend({
  name: 'SearchBar',
  components: {
    SearchIcon,
  },
  props: {
    layout: {
      type: String as PropType<'side' | 'top'>,
      default: 'top',
    },
    value: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      isFocused: false,
      searchText: this.value,
    };
  },
  watch: {
    value(newVal) {
      this.searchText = newVal;
    },
    searchText(newVal) {
      this.$emit('input', newVal); // v-model支持
    },
  },
  methods: {
    handleFocus(status: boolean) {
      this.isFocused = status;
      if (!status) this.searchText = '';
    },
    emitSearch() {
      const keyword = this.searchText.trim();
      if (keyword) {
        this.$emit('search', keyword);
      }
    },
  },
});
</script>

<style lang="less" scoped>
@import '@/style/variables.less';

.header-menu-search {
  display: flex;
  margin-left: 16px;

  .hover-active {
    .t-input {
      background: var(--td-bg-color-secondarycontainer);
    }
    /deep/ .t-icon {
      color: var(--td-brand-color);
    }
  }

  /deep/ .t-icon {
    font-size: 20px;
    color: var(--td-text-color-primary);
  }

  .t-input {
    border: none;
    outline: none;
    box-shadow: none;
    transition: background @anim-duration-base linear;

    &:hover {
      background: var(--td-bg-color-secondarycontainer);
    }
  }
}

.header-menu-search-left {
  display: flex;
  align-items: center;
}

.t-button {
  transition: opacity @anim-duration-base @anim-time-fn-easing;
}

.search-icon-hide {
  opacity: 0;
}

.header-search {
  width: 200px;
  transition: width @anim-duration-base @anim-time-fn-easing;

  .t-input {
    border: 0;
    padding-left: 40px;

    &:focus {
      box-shadow: none;
    }
  }

  &.width-zero {
    width: 0;
    opacity: 0;
  }
}
</style>
