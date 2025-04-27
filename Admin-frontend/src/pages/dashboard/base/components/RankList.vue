<template>
  <t-row :gutter="[16, 16]">
    <t-col :xs="12" :xl="12">
      <t-card title="Frequent Users Ranking" class="dashboard-rank-card" :bordered="false">
        <template #actions>
          <t-radio-group default-value="dateVal">
            <t-radio-button value="dateVal">This Week</t-radio-button>
            <t-radio-button value="monthVal">Last 3 Months</t-radio-button>
          </t-radio-group>
        </template>
        <t-table :data="frequentUsers" :columns="userColumns" rowKey="user_id">
          <template #index="{ rowIndex }">
            <span :class="getRankClass(rowIndex)">
              {{ rowIndex + 1 }}
            </span>
          </template>
          <template #avatar="{ row }">
            <t-avatar :image="row.avatar_path" />
          </template>
          <template #operation="slotProps">
            <a class="link" @click="rehandleClickOp(slotProps)">Details</a>
          </template>
        </t-table>
      </t-card>
    </t-col>
  </t-row>
</template>
<script>
import Trend from '@/components/trend/index.vue';
import { getFrequentUsers } from '@/service/service-user';

export default {
  name: 'RankList',
  components: {
    Trend,
  },
  data() {
    return {
      frequentUsers: [],
      userColumns: [
        {
          colKey: 'index',
          title: 'Rank',
          width: 80,
        },
        {
          colKey: 'avatar_path',
          title: 'Avatar',
          width: 80,
          cell: 'avatar',
        },
        {
          colKey: 'username',
          title: 'Username',
          width: 150,
        },
        {
          colKey: 'totalUsageHours',
          title: 'Usage Hours',
          width: 120,
        },
        {
          colKey: 'totalSpent',
          title: 'Total Spent',
          width: 120,
        },
        {
          colKey: 'operation',
          title: 'Actions',
          width: 80,
        },
      ],
    };
  },
  created() {
    this.fetchFrequentUsers();
  },
  methods: {
    async fetchFrequentUsers() {
      try {
        const result = await getFrequentUsers();
        console.log(result)
        this.frequentUsers = result;
      } catch (error) {
        console.error('Failed to fetch frequent users:', error);
      }
    },
    rehandleClickOp(val) {
      console.log(val);
    },
    getRankClass(index) {
      return ['dashboard-rank__cell', { 'dashboard-rank__cell--top': index < 3 }];
    },
  },
};
</script>

<style lang="less" scoped>
@import '@/style/variables.less';

.dashboard-rank-card {
  padding: 8px;

  /deep/ .t-card__header {
    padding-bottom: 24px;
  }

  /deep/ .t-card__title {
    font-size: 20px;
    font-weight: 500;
  }
}

.dashboard-rank__cell {
  display: inline-flex;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  color: white;
  font-size: 14px;
  background-color: var(--td-gray-color-5);
  align-items: center;
  justify-content: center;
  font-weight: 700;

  &--top {
    background: var(--td-brand-color);
  }
}
</style>
