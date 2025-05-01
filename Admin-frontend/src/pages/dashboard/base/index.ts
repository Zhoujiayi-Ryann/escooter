import dayjs from 'dayjs';
import { getChartListColor } from '@/utils/color';
import { getRandomArray } from '@/utils/charts';
import { getRevenueStatistics } from '@/service/service-revenue';

/** 首页 dashboard 折线图 */
export function constructInitDashboardDataset(type: string, chartColors?: any) {
  const dateArray: Array<string> = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
  const datasetAxis = {
    xAxis: {
      type: 'category',
      show: false,
      data: dateArray,
    },
    yAxis: {
      show: false,
      type: 'value',
    },
    grid: {
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
    },
  };

  if (type === 'line') {
    const lineDataset = {
      ...datasetAxis,
      color: ['#fff'],
      series: [
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type,
          showSymbol: true,
          symbol: 'circle',
          symbolSize: 0,
          markPoint: {
            data: [
              { type: 'max', name: '最大值' },
              { type: 'min', name: '最小值' },
            ],
          },
          itemStyle: {
            normal: {
              lineStyle: {
                width: 2,
              },
            },
          },
        },
      ],
    };
    return lineDataset;
  }
  if (type === 'bar') {
    const barDataset = {
      ...datasetAxis,
      color: chartColors || getChartListColor(),
      series: [
        {
          data: [
            100,
            130,
            184,
            218,
            {
              value: 135,
              itemStyle: {
                opacity: 0.2,
              },
            },
            {
              value: 118,
              itemStyle: {
                opacity: 0.2,
              },
            },
            {
              value: 60,
              itemStyle: {
                opacity: 0.2,
              },
            },
          ],
          type,
          barWidth: 9,
        },
      ],
    };
    return barDataset;
  }
}

/** 柱状图数据源 */
export function constructInitDataset({
  dateTime = [],
  placeholderColor,
  borderColor,
}: { dateTime: Array<string> } & Record<string, string>) {
  const divideNum = 10;
  const timeArray = [];
  const inArray = [];
  const outArray = [];
  for (let i = 0; i < divideNum; i++) {
    if (dateTime.length > 0) {
      const dateAbsTime: number = (new Date(dateTime[1]).getTime() - new Date(dateTime[0]).getTime()) / divideNum;
      const enhandTime: number = new Date(dateTime[0]).getTime() + dateAbsTime * i;
      timeArray.push(dayjs(enhandTime).format('MM-DD'));
    } else {
      timeArray.push(
        dayjs()
          .subtract(divideNum - i, 'day')
          .format('MM-DD'),
      );
    }

    inArray.push(getRandomArray().toString());
    outArray.push(getRandomArray().toString());
  }
  const dataset = {
    color: getChartListColor(),
    tooltip: {
      trigger: 'item',
    },
    xAxis: {
      type: 'category',
      data: timeArray,
      axisLabel: {
        color: placeholderColor,
      },
      axisLine: {
        lineStyle: {
          color: borderColor,
          width: 1,
        },
      },
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        color: placeholderColor,
      },
      splitLine: {
        lineStyle: {
          color: borderColor,
        },
      },
    },
    grid: {
      top: '5%',
      left: '25px',
      right: 0,
      bottom: '60px',
    },
    legend: {
      icon: 'rect',
      itemWidth: 12,
      itemHeight: 4,
      itemGap: 48,
      textStyle: {
        fontSize: 12,
        color: placeholderColor,
      },
      left: 'center',
      bottom: '0',
      orient: 'horizontal',
      data: ['本月', '上月'],
    },
    series: [
      {
        name: '本月',
        data: outArray,
        type: 'bar',
      },
      {
        name: '上月',
        data: inArray,
        type: 'bar',
      },
    ],
  };

  return dataset;
}

export function getLineChartDataSet({
  selectedDate,
  placeholderColor,
  borderColor,
}: { selectedDate?: string } & Record<string, string>) {
  const timeArray = [];
  const inArray = [];
  const outArray = [];
  
  // 修改为以周日为一周的开始
  const startOfWeek = dayjs(selectedDate).startOf('week').subtract(1, 'day');
  const endOfWeek = startOfWeek.add(6, 'day');
  
  // 修改星期显示顺序，从周日开始
  const weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  
  // 生成周日到周六的数据
  for (let i = 0; i < 7; i++) {
    const currentDay = startOfWeek.add(i, 'day');
    timeArray.push(currentDay.format('MM-DD') + ` (${weekDays[i]})`);
    
    // 这里将使用API获取的数据替换随机数据
    outArray.push('0'); // 默认值，将在API调用后更新
    inArray.push('0');  // 默认值，将在API调用后更新
  }

  // 不同时段的收入数据也相应调整
  const hourlyRates = {
    '1hr': outArray.map(v => 0),
    '4hr': outArray.map(v => 0),
    '1day': outArray.map(v => 0)
  };

  const dataSet = {
    color: getChartListColor(),
    tooltip: {
      trigger: 'axis',
      formatter: function(params: any) {
        const day = params[0].name;
        let html = `${day}<br/>`;
        params.forEach((param: any) => {
          const series = param.seriesName;
          const value = param.value;
          html += `${series}: £${value}<br/>`;
        });
        html += '<br/>Time Period Distribution:<br/>';
        html += `1 Hour: £${hourlyRates['1hr'][params[0].dataIndex]}<br/>`;
        html += `4 Hours: £${hourlyRates['4hr'][params[0].dataIndex]}<br/>`;
        html += `1 Day: £${hourlyRates['1day'][params[0].dataIndex]}`;
        return html;
      }
    },
    grid: {
      left: '0px',
      right: '0px',
      top: '7px',
      bottom: '30px',
      containLabel: true,
    },
    legend: {
      left: 'center',
      bottom: '0',
      orient: 'horizontal',
      data: ['Current Week', 'Last Week'],
      textStyle: {
        fontSize: 12,
        color: placeholderColor,
      },
    },
    xAxis: {
      type: 'category',
      data: timeArray,
      boundaryGap: true,
      axisLabel: {
        color: placeholderColor,
        align: 'center',
      },
      axisTick: {
        alignWithLabel: true,
      },
      axisLine: {
        lineStyle: {
          width: 1,
        },
      },
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        color: placeholderColor,
        formatter: '${value}'
      },
      splitLine: {
        lineStyle: {
          color: borderColor,
        },
      },
    },
    series: [
      {
        name: 'Current Week',
        data: outArray,
        type: 'line',
        smooth: false,
        showSymbol: true,
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: {
          normal: {
            borderColor,
            borderWidth: 1,
          },
        },
        areaStyle: {
          normal: {
            opacity: 0.1,
          },
        },
      },
      {
        name: 'Last Week',
        data: inArray,
        type: 'line',
        smooth: false,
        showSymbol: true,
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: {
          normal: {
            borderColor,
            borderWidth: 1,
          },
        },
      },
    ],
  };
  return dataSet;
}

// 新增函数：更新图表数据
export async function updateLineChartData(chart: any, selectedDate: string) {
  try {
    // 获取所选周的开始(周日)和结束(周六)日期
    const startOfWeek = dayjs(selectedDate).startOf('week').subtract(1, 'day').format('YYYY-MM-DD');
    const endOfWeek = dayjs(selectedDate).endOf('week').subtract(1, 'day').format('YYYY-MM-DD');
    
    // 获取上周的日期范围
    const lastWeekStart = dayjs(startOfWeek).subtract(7, 'day').format('YYYY-MM-DD');
    const lastWeekEnd = dayjs(endOfWeek).subtract(7, 'day').format('YYYY-MM-DD');
    
    // 调用API获取当前周的收入数据
    const currentWeekData = await getRevenueStatistics(startOfWeek, endOfWeek);
    console.log(currentWeekData);
    // 调用API获取上周的收入数据
    const lastWeekData = await getRevenueStatistics(lastWeekStart, lastWeekEnd);
    
    // 准备数据数组
    const currentWeekValues = [];
    const lastWeekValues = [];
    const hourlyRates = {
      '1hr': [],
      '4hr': [],
      '1day': []
    };
    
    // 遍历日期范围内的每一天
    for (let i = 0; i < 7; i++) {
      const currentDate = dayjs(startOfWeek).add(i, 'day').format('YYYY-MM-DD');
      const lastWeekDate = dayjs(lastWeekStart).add(i, 'day').format('YYYY-MM-DD');
      
      // 获取当前周每天的收入
      const dailyRevenue = currentWeekData.dailyRevenue[currentDate] || 0;
      currentWeekValues.push(dailyRevenue.toString());
      
      // 获取上周每天的收入
      const lastWeekRevenue = lastWeekData.dailyRevenue[lastWeekDate] || 0;
      lastWeekValues.push(lastWeekRevenue.toString());
      
      // 获取不同时段的收入数据
      const durationData = currentWeekData.dailyDurationRevenue[currentDate] || {
        lessThanOneHour: 0,
        oneToFourHours: 0,
        moreThanFourHours: 0
      };
      
      hourlyRates['1hr'].push(durationData.lessThanOneHour);
      hourlyRates['4hr'].push(durationData.oneToFourHours);
      hourlyRates['1day'].push(durationData.moreThanFourHours);
    }
    
    // 更新图表数据
    chart.setOption({
      series: [
        {
          name: 'Current Week',
          data: currentWeekValues
        },
        {
          name: 'Last Week',
          data: lastWeekValues
        }
      ]
    });
    
    // 更新tooltip中的时段数据
    chart.getOption().tooltip.formatter = function(params: any) {
      const day = params[0].name;
      let html = `${day}<br/>`;
      params.forEach((param: any) => {
        const series = param.seriesName;
        const value = param.value;
        html += `${series}: £${value}<br/>`;
      });
      
      const index = params[0].dataIndex;
      html += '<br/>Time Period Distribution:<br/>';
      html += `1 Hour: £${hourlyRates['1hr'][index]}<br/>`;
      html += `4 Hours: £${hourlyRates['4hr'][index]}<br/>`;
      html += `1 Day: £${hourlyRates['1day'][index]}`;
      return html;
    };
    
    return { currentWeekValues, lastWeekValues, hourlyRates };
  } catch (error) {
    console.error('Failed to update chart data:', error);
    return null;
  }
}

// 辅助函数：生成指定范围内的随机数
function getRandomArray(min = 100, max = 1000) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

/**
 * 获取表行数据
 *
 * @export
 * @param {string} productName
 * @param {number} divideNum
 */
export function getSelftItemList(productName: string, divideNum: number): string[] {
  const productArray: string[] = [productName];
  for (let i = 0; i < divideNum; i++) {
    productArray.push(getRandomArray(100 * i).toString());
  }

  return productArray;
}

/**
 * 散点图数据
 *
 * @export
 * @returns {any[]}
 */
export function getScatterDataSet({
  dateTime = [],
  placeholderColor,
  borderColor,
}: { dateTime?: Array<string> } & Record<string, string>): any {
  const divideNum = 40;
  const timeArray = [];
  const inArray = [];
  const outArray = [];
  for (let i = 0; i < divideNum; i++) {
    // const [timeArray, inArray, outArray] = dataset;
    if (dateTime.length > 0) {
      const dateAbsTime: number = (new Date(dateTime[1]).getTime() - new Date(dateTime[0]).getTime()) / divideNum;
      const enhandTime: number = new Date(dateTime[0]).getTime() + dateAbsTime * i;
      timeArray.push(dayjs(enhandTime).format('MM-DD'));
    } else {
      timeArray.push(
        dayjs()
          .subtract(divideNum - i, 'day')
          .format('MM-DD'),
      );
    }

    inArray.push(getRandomArray().toString());
    outArray.push(getRandomArray().toString());
  }

  return {
    color: getChartListColor(),
    xAxis: {
      data: timeArray,
      axisLabel: {
        color: placeholderColor,
      },
      splitLine: { show: false },
      axisLine: {
        lineStyle: {
          color: borderColor,
          width: 1,
        },
      },
    },
    yAxis: {
      type: 'value',
      // splitLine: { show: false},
      axisLabel: {
        color: placeholderColor,
      },
      nameTextStyle: {
        padding: [0, 0, 0, 60],
      },
      axisTick: {
        show: false,
        axisLine: {
          show: false,
        },
      },
      axisLine: {
        show: false,
      },
      splitLine: {
        lineStyle: {
          color: borderColor,
        },
      },
    },
    tooltip: {
      trigger: 'item',
    },
    grid: {
      top: '5px',
      left: '25px',
      right: '5px',
      bottom: '60px',
    },
    legend: {
      left: 'center',
      bottom: '0',
      orient: 'horizontal', // legend 横向布局。
      data: ['按摩仪', '咖啡机'],
      itemHeight: 8,
      itemWidth: 8,
      textStyle: {
        fontSize: 12,
        color: placeholderColor,
      },
    },
    series: [
      {
        name: '按摩仪',
        symbolSize: 10,
        data: outArray.reverse(),
        type: 'scatter',
      },
      {
        name: '咖啡机',
        symbolSize: 10,
        data: inArray.concat(inArray.reverse()),
        type: 'scatter',
      },
    ],
  };
}

/**
 * 获域图数据结构
 *
 * @export
 * @returns {any[]}
 */
export function getAreaChartDataSet(): any {
  const xAxisData = [];
  const data1 = [];
  const data2 = [];
  for (let i = 0; i < 50; i++) {
    xAxisData.push(`${i}`);
    data1.push((getRandomArray() * Math.sin(i / 5) * (i / 5 - 5) + i / 6) * 2);
    data2.push((getRandomArray() * Math.cos(i / 5) * (i / 5 - 5) + i / 6) * 2);
  }

  return {
    color: getChartListColor(),
    // title: {
    //   text: '柱状图动画延迟',
    // },
    legend: {
      left: 'center',
      bottom: '5%',
      orient: 'horizontal',
      data: ['测试', '上线'],
    },
    tooltip: {
      trigger: 'item',
    },
    xAxis: {
      data: xAxisData,
      splitLine: {
        show: false,
      },
    },
    yAxis: {},
    series: [
      {
        name: '测试',
        type: 'bar',
        data: data1,
        emphasis: {
          focus: 'series',
        },
        animationDelay(idx: number) {
          return idx * 10;
        },
      },
      {
        name: '上线',
        type: 'bar',
        data: data2,
        emphasis: {
          focus: 'series',
        },
        animationDelay(idx: number) {
          return idx * 10 + 100;
        },
      },
    ],
    animationEasing: 'elasticOut',
    animationDelayUpdate(idx: number) {
      return idx * 5;
    },
  };
}

/**
 * 柱状图数据结构
 *
 * @export
 * @param {boolean} [isMonth=false]
 * @returns {*}
 */
export function getColumnChartDataSet(isMonth = false) {
  if (isMonth) {
    return {
      color: getChartListColor(),
      legend: {
        left: 'center',
        top: '10%',
        orient: 'horizontal', // legend 横向布局。
        data: ['直接访问'],
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          // 坐标轴指示器，坐标轴触发有效
          type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
        },
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: [
        {
          type: 'category',
          data: ['1', '4', '8', '12', '16', '20', '24'],
          axisTick: {
            alignWithLabel: true,
          },
        },
      ],
      yAxis: [
        {
          type: 'value',
        },
      ],
      series: [
        {
          name: '直接访问',
          type: 'bar',
          barWidth: '60%',
          data: [
            getRandomArray(Math.random() * 100),
            getRandomArray(Math.random() * 200),
            getRandomArray(Math.random() * 300),
            getRandomArray(Math.random() * 400),
            getRandomArray(Math.random() * 500),
            getRandomArray(Math.random() * 600),
            getRandomArray(Math.random() * 700),
          ],
        },
      ],
    };
  }

  return {
    color: getChartListColor(),
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        // 坐标轴指示器，坐标轴触发有效
        type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
      },
    },
    legend: {
      left: 'center',
      bottom: '0%',
      orient: 'horizontal', // legend 横向布局。
      data: ['直接访问'],
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '13%',
      containLabel: true,
    },
    xAxis: [
      {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        axisTick: {
          alignWithLabel: true,
        },
      },
    ],
    yAxis: [
      {
        type: 'value',
      },
    ],
    series: [
      {
        name: '直接访问',
        type: 'bar',
        barWidth: '20%',
        data: [
          getRandomArray(Math.random() * 100),
          getRandomArray(Math.random() * 200),
          getRandomArray(Math.random() * 300),
          getRandomArray(Math.random() * 400),
          getRandomArray(Math.random() * 500),
          getRandomArray(Math.random() * 600),
          getRandomArray(Math.random() * 700),
        ],
      },
    ],
  };
}

export function getPieChartDataSet({
  radius = 42,
  textColor,
  placeholderColor,
  containerColor,
}: { radius: number } & Record<string, string>) {
  return {
    color: getChartListColor(),
    tooltip: {
      show: false,
      trigger: 'axis',
      position: null,
    },
    grid: {
      top: '0',
      right: '0',
    },
    legend: {
      selectedMode: false,
      itemWidth: 12,
      itemHeight: 4,
      textStyle: {
        fontSize: 12,
        color: placeholderColor,
      },
      left: 'center',
      bottom: '0',
      orient: 'horizontal', // legend 横向布局。
    },
    series: [
      {
        name: '销售渠道',
        type: 'pie',
        radius: ['48%', '60%'],
        avoidLabelOverlap: true,
        selectedMode: true,
        hoverAnimation: true,
        silent: true,
        itemStyle: {
          borderColor: containerColor,
          borderWidth: 1,
        },
        label: {
          show: true,
          position: 'center',
          formatter: ['{value|{d}%}', '{name|{b}渠道占比}'].join('\n'),
          rich: {
            value: {
              color: textColor,
              fontSize: 28,
              fontWeight: 'normal',
              lineHeight: 46,
            },
            name: {
              color: '#909399',
              fontSize: 12,
              lineHeight: 14,
            },
          },
        },
        emphasis: {
          label: {
            show: true,
            formatter: ['{value|{d}%}', '{name|{b}渠道占比}'].join('\n'),
            rich: {
              value: {
                color: textColor,
                fontSize: 28,
                fontWeight: 'normal',
                lineHeight: 46,
              },
              name: {
                color: '#909399',
                fontSize: 14,
                lineHeight: 14,
              },
            },
          },
        },
        labelLine: {
          show: false,
        },
        data: [
          {
            value: 1048,
            name: '线上',
          },
          { value: radius * 7, name: '门店' },
        ],
      },
    ],
  };
}
