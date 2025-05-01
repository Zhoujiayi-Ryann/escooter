<template>
  <t-card :bordered="false">
    <t-row>
      <t-col :xs="12" :xl="12">
        <t-card
          :bordered="false"
          title="Display at a specific time period"
          :class="{ 'dashboard-overview-card': true, 'overview-panel': true }"
        >
          <template #actions>
            <t-date-picker
              v-model="selectedWeek"
              mode="week"
              :max="maxDate"
              format="YYYY-MM-DD"
              :first-day-of-week="0"
              @change="onWeekChange"
            />
          </template>
          <div
            id="stokeContainer"
            style="width: 100%; height: 351px"
            ref="stokeContainer"
            class="dashboard-chart-container"
          ></div>
        </t-card>
      </t-col>
    </t-row>
  </t-card>
</template>
<script>
import { TooltipComponent, LegendComponent, GridComponent } from 'echarts/components';
import { BarChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
import * as echarts from 'echarts/core';
import { mapState } from 'vuex';
import dayjs from 'dayjs';

import { constructInitDataset } from '../index';
import { changeChartsTheme, getChartListColor } from '@/utils/color';
import { LAST_7_DAYS } from '@/utils/date';
import Trend from '@/components/trend/index.vue';
import { getRevenueStatistics } from '@/service/service-revenue';

import { PANE_LIST, SALE_TEND_LIST, BUY_TEND_LIST, SALE_COLUMNS, BUY_COLUMNS } from '@/service/service-base';

echarts.use([TooltipComponent, LegendComponent, GridComponent, BarChart, CanvasRenderer]);

export default {
  name: 'Overview',
  components: {
    Trend,
  },
  data() {
    return {
      panelList: PANE_LIST,
      buyTendList: BUY_TEND_LIST,
      saleTendList: SALE_TEND_LIST,
      saleColumns: SALE_COLUMNS,
      buyColumns: BUY_COLUMNS,
      LAST_7_DAYS,
      selectedWeek: dayjs().startOf('week').add(1, 'day').format('YYYY-MM-DD'),
      maxDate: dayjs().format('YYYY-MM-DD'),
    };
  },
  computed: {
    ...mapState('setting', ['brandTheme', 'mode']), // 这里需要用到主题色和主题模式的全局配置
  },
  watch: {
    brandTheme() {
      changeChartsTheme([this.stokeChart]);
    },
    mode() {
      [this.stokeChart].forEach((item) => {
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
    /** 出入库概览日期更新 */
    onStokeDataChange(checkedValues) {
      const { chartColors } = this.$store.state.setting;

      this.stokeChart.setOption(constructInitDataset({ dateTime: checkedValues, ...chartColors }));
    },
    
    /** 周选择变更处理 */
    async onWeekChange(value) {
      if (value) {
        this.selectedWeek = value;
        // 获取所选周的开始(周日)和结束(周六)日期
        const weekStart = dayjs(value).startOf('week').format('YYYY-MM-DD');
        const weekEnd = dayjs(value).endOf('week').format('YYYY-MM-DD');
        console.log(`所选周的范围: ${weekStart} 至 ${weekEnd}`);
        await this.updateChartData();
      }
    },
    
    /** 更新图表数据 */
    async updateChartData() {
      const { chartColors } = this.$store.state.setting;
      
      try {
        // 获取所选周的开始(周日)和结束(周六)日期
        const startOfWeek = dayjs(this.selectedWeek).startOf('week').format('YYYY-MM-DD');
        const endOfWeek = dayjs(this.selectedWeek).endOf('week').format('YYYY-MM-DD');
        
        // 调用API获取当前周的数据
        const currentWeekData = await getRevenueStatistics(startOfWeek, endOfWeek);
        
        // 准备数据数组
        const timeArray = [];
        const oneHourData = [];
        const fourHoursData = [];
        const oneDayData = [];
        
        // 遍历日期范围内的每一天
        for (let i = 0; i < 7; i++) {
          const currentDate = dayjs(startOfWeek).add(i, 'day').format('YYYY-MM-DD');
          const weekDay = dayjs(startOfWeek).add(i, 'day').format('ddd');
          timeArray.push(dayjs(startOfWeek).add(i, 'day').format('MM-DD') + ` (${weekDay})`);
          
          // 获取不同时段的数据
          const durationData = currentWeekData.dailyDurationRevenue[currentDate] || {
            lessThanOneHour: 0,
            oneToFourHours: 0,
            moreThanFourHours: 0
          };
          
          oneHourData.push(durationData.lessThanOneHour);
          fourHoursData.push(durationData.oneToFourHours);
          oneDayData.push(durationData.moreThanFourHours);
        }
        
        // 更新图表数据
        this.stokeChart.setOption({
          color: ['#8e8de1', '#c3d7f2', '#5b89d8'],
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            data: ['1 Hour', '4 Hours', '1 Day'],
            left: 'center',
            bottom: '0',
            orient: 'horizontal',
            textStyle: {
              fontSize: 12,
              color: chartColors.placeholderColor,
            },
          },
          xAxis: {
            type: 'category',
            data: timeArray,
            axisLabel: {
              color: chartColors.placeholderColor,
            },
            axisLine: {
              lineStyle: {
                color: chartColors.borderColor,
                width: 1,
              },
            },
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: chartColors.placeholderColor,
              formatter: '£{value}'
            },
            splitLine: {
              lineStyle: {
                color: chartColors.borderColor,
              },
            },
          },
          grid: {
            top: '5%',
            left: '25px',
            right: '25px',
            bottom: '60px',
            containLabel: true
          },
          series: [
            {
              name: '1 Hour',
              type: 'bar',
              emphasis: {
                focus: 'series'
              },
              data: oneHourData,
              barWidth: '25%',
              barGap: '20%',
              itemStyle: {
                borderRadius: [3, 3, 3, 3]
              }
            },
            {
              name: '4 Hours',
              type: 'bar',
              emphasis: {
                focus: 'series'
              },
              data: fourHoursData,
              barWidth: '20%',
              barGap: '10%',
              itemStyle: {
                borderRadius: [3, 3, 3, 3]
              }
            },
            {
              name: '1 Day',
              type: 'bar',
              emphasis: {
                focus: 'series'
              },
              data: oneDayData,
              barWidth: '20%',
              barGap: '10%',
              itemStyle: {
                borderRadius: [3, 3, 3, 3]
              }
            }
          ]
        });
        
      } catch (error) {
        console.error('更新图表数据失败:', error);
        // 如果API调用失败，使用默认数据
        this.stokeChart.setOption(constructInitDataset({ 
          dateTime: [this.selectedWeek, dayjs(this.selectedWeek).endOf('week').format('YYYY-MM-DD')], 
          ...chartColors 
        }));
      }
    },
    
    updateContainer() {
      this.stokeChart.resize({
        // 根据父容器的大小设置大小
        width: this.stokeContainer.clientWidth,
        height: this.stokeContainer.clientHeight,
      });
    },
    renderCharts() {
      const { chartColors } = this.$store.state.setting;
      // 出入库概览
      if (!this.stokeContainer) this.stokeContainer = document.getElementById('stokeContainer');

      this.stokeChart = echarts.init(this.stokeContainer);
      // 初始化图表
      this.updateChartData();
    },
  },
};
</script>

<style lang="less" scoped>
@import '@/style/variables.less';

.dashboard-chart-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
}

.dashboard-overview-card {
  /deep/ .t-card__header {
    padding-bottom: 24px;
  }

  /deep/ .t-card__title {
    font-size: 20px;
    font-weight: 500;
  }

  &.overview-panel {
    border-right: none;
  }

  &.export-panel {
    border-left: none;
  }
}

.inner-card {
  padding: 24px 0;

  /deep/ .t-card__header {
    padding-bottom: 0;
  }

  &__content {
    &-title {
      font-size: 36px;
      line-height: 44px;
    }

    &-footer {
      display: flex;
      align-items: center;
      line-height: 22px;
      color: var(--td-text-color-placeholder);

      .trend-tag {
        margin-left: 4px;
      }
    }
  }
}
</style>
