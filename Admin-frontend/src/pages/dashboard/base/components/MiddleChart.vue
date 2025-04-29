<template>
  <t-row :gutter="[16, 16]">
    <t-col :xs="12" :xl="9">
      <t-card title="Weekly Income" class="dashboard-chart-card" :bordered="false">
        <template #actions>
          <t-space>
            <t-date-picker
              v-model="selectedWeek"
              mode="week"
              :max="maxDate"
              format="YYYY-MM-DD"
              :first-day-of-week="0"
              @change="onWeekChange"
            />
          </t-space>
        </template>
        <div
          id="monitorContainer"
          ref="monitorContainer"
          :style="{ width: '100%', height: `${resizeTime * 326}px` }"
        ></div>
      </t-card>
    </t-col>
    <t-col :xs="12" :xl="3">
      <t-card title="Sales Channels" :subtitle="currentMonth" class="dashboard-chart-card" :bordered="false">
        <div
          id="countContainer"
          ref="countContainer"
          :style="{ width: `${resizeTime * 326}px`, height: `${resizeTime * 326}px`, margin: '0 auto' }"
        ></div>
      </t-card>
    </t-col>
  </t-row>
</template>
<script>
import { TooltipComponent, LegendComponent, GridComponent } from 'echarts/components';
import { PieChart, LineChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
import * as echarts from 'echarts/core';
import { mapState } from 'vuex';
import dayjs from 'dayjs';

import { LAST_7_DAYS } from '@/utils/date';

import { getPieChartDataSet, getLineChartDataSet } from '../index';
import { changeChartsTheme } from '@/utils/color';
echarts.use([TooltipComponent, LegendComponent, PieChart, GridComponent, LineChart, CanvasRenderer]);

export default {
  name: 'MiddleChart',
  data() {
    return {
      LAST_7_DAYS,
      resizeTime: 1,
      currentMonth: this.getThisMonth(),
      selectedWeek: dayjs().startOf('week').add(1, 'day').format('YYYY-MM-DD'),
      maxDate: dayjs().format('YYYY-MM-DD'),
    };
  },
  computed: {
    ...mapState('setting', ['brandTheme', 'mode']), // 这里需要用到主题色和主题模式的全局配置
  },
  watch: {
    brandTheme() {
      changeChartsTheme([this.countChart, this.monitorChart]);
    },
    mode() {
      [this.countChart, this.monitorChart].forEach((item) => {
        item.dispose();
      });
      this.renderCharts();
    },
    selectedWeek() {
      this.updateChartData();
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.updateContainer();
    });

    window.addEventListener('resize', this.updateContainer, false);
    this.renderCharts();
  },

  methods: {
    /** 获取当前选中时间的短时间表达法 */
    getThisMonth(checkedValues = '') {
      let date;
      if (!checkedValues || checkedValues.length === 0) {
        date = new Date();
        return `${date.getFullYear()}-${date.getMonth() + 1}`;
      }
      date = new Date(checkedValues[0]);
      const date2 = new Date(checkedValues[1]);
      const startMonth = date.getMonth() + 1 > 9 ? date.getMonth() + 1 : `0${date.getMonth() + 1}`;
      const endMonth = date2.getMonth() + 1 > 9 ? date2.getMonth() + 1 : `0${date2.getMonth() + 1}`;

      return `${date.getFullYear()}-${startMonth} to ${date2.getFullYear()}-${endMonth}`;
    },
    /** 更新图表数据 */
    updateChartData() {
      const { chartColors } = this.$store.state.setting;
      const chartData = getLineChartDataSet({ 
        selectedDate: this.selectedWeek,
        ...chartColors 
      });
      this.monitorChart.setOption(chartData);
    },
    onWeekChange(value) {
      if (value) {
        this.selectedWeek = value;
        // 获取所选周的开始(周日)和结束(周六)日期
        const weekStart = dayjs(value).startOf('week').format('YYYY-MM-DD');
        const weekEnd = dayjs(value).endOf('week').format('YYYY-MM-DD');
        console.log(`所选周的范围: ${weekStart} 至 ${weekEnd}`);
        this.updateChartData();
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

      this.countChart.resize({
        // 根据父容器的大小设置大小
        width: `${this.resizeTime * 326}px`,
        height: `${this.resizeTime * 326}px`,
      });

      this.monitorChart.resize({
        // 根据父容器的大小设置大小
        width: this.monitorContainer.clientWidth,
        height: `${this.resizeTime * 326}px`,
      });
    },
    renderCharts() {
      const { chartColors } = this.$store.state.setting;

      // 资金走势
      if (!this.monitorContainer) {
        this.monitorContainer = document.getElementById('monitorContainer');
      }
      this.monitorChart = echarts.init(this.monitorContainer);
      
      // 使用新的方法更新图表数据
      this.updateChartData();

      // 销售合同占比
      if (!this.countContainer) {
        this.countContainer = document.getElementById('countContainer');
      }
      this.countChart = echarts.init(this.countContainer);

      const option = getPieChartDataSet(chartColors);
      this.countChart.setOption(option);
    },
  },
};
</script>
<style lang="less" scoped>
.dashboard-chart-card {
  padding: 8px;

  /deep/ .t-card__header {
    padding-bottom: 24px;
  }

  /deep/ .t-card__title {
    font-size: 20px;
    font-weight: 500;
  }
}

.dashboard-chart-title-container {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
</style>
