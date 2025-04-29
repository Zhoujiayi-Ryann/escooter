<template>
  <t-row :gutter="[16, 16]">
    <t-col :xs="6" :xl="3" v-for="(item, index) in panelList" :key="item.title">
      <t-card
        :bordered="false"
        :title="item.title"
        :style="{ height: '168px' }"
        :class="{ 'dashboard-item': true, 'dashboard-item--main-color': index == 0 }"
      >
        <div class="dashboard-item-top">
          <span v-if="!isLoading || index !== 1" :style="{ fontSize: `${resizeTime * 36}px` }">{{ item.number }}</span>
          <t-loading v-else size="small" />
        </div>
        <div class="dashboard-item-left">
          <div
            v-if="index === 0"
            id="moneyContainer"
            class="dashboard-chart-container"
            :style="{ width: `${resizeTime * 120}px`, height: `${resizeTime * 66}px` }"
          ></div>
          <div
            v-else-if="index === 1"
            id="refundContainer"
            class="dashboard-chart-container"
            :style="{ width: `${resizeTime * 120}px`, height: `${resizeTime * 42}px` }"
          ></div>
          <span v-else-if="index === 2" :style="{ marginTop: `-24px` }">
            <usergroup-icon />
          </span>
          <span v-else :style="{ marginTop: '-24px' }">
            <file-icon />
          </span>
        </div>
        <template #footer>
          <div class="dashboard-item-bottom">
            <div class="dashboard-item-block">
              自从上周以来
              <trend
                class="dashboard-item-trend"
                :type="item.upTrend ? 'up' : 'down'"
                :is-reverse-color="index === 0"
                :describe="item.upTrend || item.downTrend"
              />
            </div>
            <chevron-right-icon />
          </div>
        </template>
      </t-card>
    </t-col>
  </t-row>
</template>
<script>
import { LineChart, BarChart } from 'echarts/charts';
import * as echarts from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { UsergroupIcon, FileIcon, ChevronRightIcon } from 'tdesign-icons-vue';
import { mapState } from 'vuex';

import Trend from '@/components/trend/index.vue';

import { constructInitDashboardDataset } from '../index';
import { changeChartsTheme } from '@/utils/color';
import { PANE_LIST } from '@/service/service-base';
import { scooterService } from '@/service/service-scooter';
import { getTotalRevenue } from '@/service/service-revenue';
import { getAllNonAdminUsers } from '@/service/service-user';

echarts.use([LineChart, BarChart, CanvasRenderer]);

export default {
  name: 'TopPanel',
  components: {
    Trend,
    UsergroupIcon,
    FileIcon,
    ChevronRightIcon,
  },
  data() {
    return {
      resizeTime: 1,
      panelList: [...PANE_LIST],
      isLoading: true,
    };
  },
  computed: {
    ...mapState('setting', ['brandTheme', 'mode']), // 这里需要用到主题色和主题模式的全局配置
  },
  watch: {
    brandTheme() {
      changeChartsTheme([this.refundChart]);
    },
    mode() {
      [this.moneyCharts, this.refundChart].forEach((item) => {
        item.dispose();
      });
      this.renderCharts();
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.updateContainer();
    });

    window.addEventListener('resize', this.updateContainer, false);
    this.fetchVehicleCount();
    this.fetchTotalRevenue();
    this.fetchUserCount();
    this.renderCharts();
  },

  methods: {
    async fetchVehicleCount() {
      try {
        const scooters = await scooterService.getAllScooters();
        if (this.panelList[1]) {
          this.panelList[1].number = scooters.length;
          this.panelList[1].upTrend = '5%';
          delete this.panelList[1].downTrend;
        }
        this.isLoading = false;
      } catch (error) {
        console.error('获取车辆数据失败:', error);
        this.isLoading = false;
      }
    },

    async fetchTotalRevenue() {
      try {
        const totalRevenue = await getTotalRevenue();
        if (this.panelList[0]) {
          this.panelList[0].number = `£ ${totalRevenue.data.toFixed(2)}`;
          this.panelList[0].upTrend = '20.5%'; // 可以根据实际情况修改或从API获取
        }
      } catch (error) {
        console.error('获取总收入数据失败:', error);
      }
    },

    async fetchUserCount() {
      try {
        const users = await getAllNonAdminUsers();
        if (this.panelList[2]) {
          this.panelList[2].number = users.length;
          this.panelList[2].downTrend = '5%'; // 可以根据实际情况修改或从API获取
        }
      } catch (error) {
        console.error('获取用户数据失败:', error);
      }
    },

    updateContainer() {
      if (document.documentElement.clientWidth >= 1400 && document.documentElement.clientWidth < 1920) {
        this.resizeTime = (document.documentElement.clientWidth / 2080).toFixed(2);
      } else if (document.documentElement.clientWidth < 1080) {
        this.resizeTime = (document.documentElement.clientWidth / 1080).toFixed(2);
      } else {
        this.resizeTime = 1;
      }
      this.moneyCharts.resize({
        // 根据父容器的大小设置大小
        width: `${this.resizeTime * 120}px`,
        height: `${this.resizeTime * 66}px`,
      });

      this.refundChart.resize({
        // 根据父容器的大小设置大小
        width: `${this.resizeTime * 120}px`,
        height: `${this.resizeTime * 42}px`,
      });
    },

    renderCharts() {
      const { chartColors } = this.$store.state.setting;
      // 收入汇总图
      if (!this.moneyContainer) this.moneyContainer = document.getElementById('moneyContainer');
      this.moneyCharts = echarts.init(this.moneyContainer);
      this.moneyCharts.setOption(constructInitDashboardDataset('line'));

      // 退款图
      if (!this.refundContainer) this.refundContainer = document.getElementById('refundContainer');
      this.refundChart = echarts.init(this.refundContainer);
      this.refundChart.setOption(constructInitDashboardDataset('bar', chartColors));
    },
  },
};
</script>

<style lang="less" scoped>
@import '@/style/variables.less';

.dashboard-item {
  padding: 8px;

  /deep/ .t-card__footer {
    padding-top: 0;
  }

  /deep/ .t-card__title {
    font-size: 14px;
    font-weight: 500;
  }

  /deep/ .t-card__body {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    flex: 1;
    position: relative;
  }

  &:hover {
    cursor: pointer;
  }

  &-top {
    display: flex;
    flex-direction: row;
    align-items: flex-start;

    > span {
      display: inline-block;
      color: var(--td-text-color-primary);
      font-size: 36px;
      line-height: 44px;
    }
  }

  &-bottom {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;

    > .t-icon {
      cursor: pointer;
    }
  }

  &-block {
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 22px;
    color: var(--td-text-color-placeholder);
  }

  &-trend {
    margin-left: 8px;
  }

  &-left {
    position: absolute;
    top: 0;
    right: 32px;

    > span {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 56px;
      height: 56px;
      background: var(--td-brand-color-1);
      border-radius: 50%;

      .t-icon {
        font-size: 24px;
        color: var(--td-brand-color);
      }
    }
  }

  // 针对第一个卡片需要反色处理
  &--main-color {
    background: var(--td-brand-color);
    color: var(--td-text-color-primary);

    /deep/ .t-card__title,
    .dashboard-item-top span,
    .dashboard-item-bottom {
      color: var(--td-text-color-anti);
    }

    .dashboard-item-block {
      color: var(--td-text-color-anti);
      opacity: 0.6;
    }

    .dashboard-item-bottom {
      color: var(--td-text-color-anti);
    }
  }
}
</style>
