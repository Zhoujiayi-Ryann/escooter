<template>
  <t-row :gutter="[16, 16]">
    <t-col :xs="12" :xl="12">
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
            <div class="weekly-income-summary">
              <span class="income-label">This week's income:  </span>
              <span class="income-value">£{{ totalWeeklyIncome }}</span>
            </div>
          </t-space>
        </template>
        <div
          id="monitorContainer"
          ref="monitorContainer"
          :style="{ width: '100%', height: `${resizeTime * 326}px` }"
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

import { getPieChartDataSet, getLineChartDataSet, updateLineChartData } from '../index';
import { changeChartsTheme } from '@/utils/color';
import { getRevenueStatistics } from '@/service/service-revenue';

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
      chartData: {
        currentWeek: [],
        lastWeek: [],
        hourlyRates: {
          '1hr': [],
          '4hr': [],
          '1day': []
        }
      },
      totalWeeklyIncome: 0
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
    
    // 初始化时获取当前周的收入数据
    this.fetchInitialWeeklyIncome();
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
    /** 初始化时获取当前周的收入数据 */
    async fetchInitialWeeklyIncome() {
      // 修正日期计算，确保正确获取周的开始和结束
      const weekStart = dayjs(this.selectedWeek).startOf('week').subtract(1, 'day').format('YYYY-MM-DD');
      const weekEnd = dayjs(this.selectedWeek).endOf('week').subtract(1, 'day').format('YYYY-MM-DD');
      
      try {
        const revenueData = await getRevenueStatistics(weekStart, weekEnd);
        console.log('初始化时获取的收入数据:', revenueData);
        if (revenueData && revenueData.totalRevenue !== undefined) {
          this.totalWeeklyIncome = revenueData.totalRevenue;
          console.log('初始周收入:', this.totalWeeklyIncome);
        }
      } catch (error) {
        console.error('获取初始收入数据失败:', error);
      }
    },
    /** 更新图表数据 */
    async updateChartData() {
      const { chartColors } = this.$store.state.setting;
      // 首先设置基本图表结构
      const chartData = getLineChartDataSet({ 
        selectedDate: this.selectedWeek,
        ...chartColors 
      });
      this.monitorChart.setOption(chartData);
      
      // 然后使用API数据更新图表
      const result = await updateLineChartData(this.monitorChart, this.selectedWeek);
      if (result) {
        this.chartData = result;
        
        // 更新图表的tooltip格式化函数，使用最新的hourlyRates数据
        this.monitorChart.setOption({
          tooltip: {
            formatter: (params) => {
              const day = params[0].name;
              let html = `${day}`;
              params.forEach((param) => {
                const series = param.seriesName;
                const value = param.value;
                // html += `${series}: £${value}<br/>`;
              });
              
              const index = params[0].dataIndex;
              html += '<br/>Time Period Distribution:<br/>';
              html += `1 Hour: £${this.chartData.hourlyRates['1hr'][index]}<br/>`;
              html += `4 Hours: £${this.chartData.hourlyRates['4hr'][index]}<br/>`;
              html += `1 Day: £${this.chartData.hourlyRates['1day'][index]}`;
              return html;
            }
          },
          yAxis: {
            axisLabel: {
              formatter: (value) => `£${value}`
            }
          }
        });
      }
    },
    async onWeekChange(value) {
      if (value) {
        this.selectedWeek = value;
        // 获取所选周的开始(周日)和结束(周六)日期
        // 修正日期计算，确保正确获取周的开始和结束
        const weekStart = dayjs(value).startOf('week').subtract(1, 'day').format('YYYY-MM-DD');
        const weekEnd = dayjs(value).endOf('week').subtract(1, 'day').format('YYYY-MM-DD');
        console.log(`所选周的范围: ${weekStart} 至 ${weekEnd}`);
        
        // 直接调用API获取周收入数据
        try {
          const revenueData = await getRevenueStatistics(weekStart, weekEnd);
          console.log('周变更 - API返回的收入数据:', revenueData);
          if (revenueData && revenueData.totalRevenue !== undefined) {
            this.totalWeeklyIncome = revenueData.totalRevenue;
            console.log('更新后的周收入:', this.totalWeeklyIncome);
          }
        } catch (error) {
          console.error('获取收入数据失败:', error);
        }
        
        await this.updateChartData();
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

      // 添加检查确保图表已初始化
      if (this.countChart) {
        this.countChart.resize({
          // 根据父容器的大小设置大小
          width: `${this.resizeTime * 326}px`,
          height: `${this.resizeTime * 326}px`,
        });
      }

      if (this.monitorChart) {
        this.monitorChart.resize({
          // 根据父容器的大小设置大小
          width: this.monitorContainer ? this.monitorContainer.clientWidth : 0,
          height: `${this.resizeTime * 326}px`,
        });
      }
    },
    async renderCharts() {
      const { chartColors } = this.$store.state.setting;

      // 资金走势
      if (!this.monitorContainer) {
        this.monitorContainer = document.getElementById('monitorContainer');
      }
      this.monitorChart = echarts.init(this.monitorContainer);
      
      // 使用新的方法更新图表数据
      await this.updateChartData();

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

.weekly-income-summary {
  display: flex;
  align-items: center;
  margin-left: 16px;
  
  .income-label {
    font-size: 14px;
    color: #999;
  }
  
  .income-value {
    font-size: 16px;
    font-weight: 500;
    color: #0052d9;
    margin-left: 4px;
  }
}
</style>
