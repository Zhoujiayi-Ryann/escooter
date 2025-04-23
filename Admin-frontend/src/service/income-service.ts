import { DashboardPanel } from './service-detail';

export const INCOME_PANE_LIST: Array<DashboardPanel> = [
    {
        title: 'Total Income',
        number: '¥ 28,425.00',
        upTrend: '20.5%',
        leftType: 'echarts-line',
    },
    {
        title: 'Total E-scooters',
        number: '768',
        leftType: 'echarts-bar',
    },
    {
        title: 'Total Users',
        number: '1126',
        downTrend: '20.5%',
        leftType: 'icon-usergroup',
    },
    {
        title: 'Total Orders',
        number: 527,
        downTrend: '20.5%',
        leftType: 'icon-file-paste',
    },
]; 