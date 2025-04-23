<template>
  <div class="list-feedback-table">
    <t-form
      ref="form"
      :data="formData"
      :label-width="110"
      colon
      @reset="onReset"
      @submit="onSubmit"
      :style="{ marginBottom: '8px' }"
    >
      <t-row>
        <t-col :span="10">
          <!-- <t-row :gutter="[16, 24]"> -->
            <!-- <t-col :flex="1">
              <t-form-item label="合同名称" name="name">
                <t-input
                  v-model="formData.name"
                  class="form-item-content"
                  type="search"
                  placeholder="请输入合同名称"
                  :style="{ minWidth: '134px' }"
                />
              </t-form-item>
            </t-col>
            <t-col :flex="1">
              <t-form-item label="合同状态" name="status">
                <t-select
                  v-model="formData.status"
                  class="form-item-content`"
                  :options="CONTRACT_STATUS_OPTIONS"
                  placeholder="请选择合同状态"
                />
              </t-form-item>
            </t-col>
            <t-col :flex="1">
              <t-form-item label="合同编号" name="no">
                <t-input
                  v-model="formData.no"
                  class="form-item-content"
                  placeholder="请输入合同编号"
                  :style="{ minWidth: '134px' }"
                />
              </t-form-item>
            </t-col> -->
            <t-col :flex="1">
              <t-form-item label="Feedback Type" name="type">
                <t-select
                  v-model="formData.type"
                  class="form-item-content`"
                  :options="FEEDBACK_TYPE_OPTIONS"
                  placeholder="Please choose a type"
                />
              </t-form-item>
            </t-col>
          <!-- </t-row> -->
        </t-col>

        <t-col :span="2" class="operation-container">
          <t-button theme="primary" type="submit" :style="{ marginLeft: '8px' }"> Search </t-button>
          <t-button type="reset" variant="base" theme="default"> Reset </t-button>
        </t-col>
      </t-row>
    </t-form>
    <div class="table-container">
      <t-table
        :data="data"
        :columns="columns"
        :rowKey="rowKey"
        :verticalAlign="verticalAlign"
        :hover="hover"
        :pagination="pagination"
        @page-change="rehandlePageChange"
        @change="rehandleChange"
        :loading="dataLoading"
        :headerAffixedTop="true"
        :headerAffixProps="{ offsetTop, container: getContainer }"
      >
        <template #status="{ row }">
          <!-- <t-tag v-if="row.status === CONTRACT_STATUS.FAIL" theme="danger" variant="light">待处理</t-tag> -->
          <t-tag v-if="row.status === FEEDBACK_STATUS.PENDING" theme="warning" variant="light">Todo</t-tag>
          <!-- <t-tag v-if="row.status === CONTRACT_STATUS.EXEC_PENDING" theme="warning" variant="light">待履行</t-tag> -->
          <t-tag v-if="row.status === FEEDBACK_STATUS.EXECUTING" theme="success" variant="light">Processing</t-tag>
          <t-tag v-if="row.status === FEEDBACK_STATUS.FINISH" theme="success" variant="light">Finish</t-tag>
        </template>
        <template #feedbackType="{ row }">
          <p v-if="row.feedbackType === FEEDBACK_TYPES.USING">Using Problems</p>
          <p v-if="row.feedbackType === FEEDBACK_TYPES.EXPERIENCE">Experience Feedback</p>
          <!-- <p v-if="row.contractType === CONTRACT_TYPES.SUPPLEMENT">待履行</p> -->
        </template>
        <!-- <template #paymentType="{ row }">
          <p v-if="row.paymentType === CONTRACT_PAYMENT_TYPES.PAYMENT" class="payment-col">
            付款
            <trend class="dashboard-item-trend" type="up" />
          </p>
          <p v-if="row.paymentType === CONTRACT_PAYMENT_TYPES.RECEIPT" class="payment-col">
            收款
            <trend class="dashboard-item-trend" type="down" />
          </p>
        </template> -->
        <template #op="slotProps">
          <a class="t-button-link" @click="rehandleClickOp(slotProps)">Details</a>
          <a class="t-button-link" @click="handleClickDelete(slotProps)">Delete</a>
        </template>
      </t-table>
      <t-dialog
        header="Are you sure to delete this feedback?"
        :body="confirmBody"
        :visible.sync="confirmVisible"
        @confirm="onConfirmDelete"
        :onCancel="onCancel"
      >
      </t-dialog>
    </div>
  </div>
</template>
<script>
import { prefix } from '@/config/global';
import Trend from '@/components/trend/index.vue';

import {
  FEEDBACK_STATUS,
  FEEDBACK_STATUS_OPTIONS,
  FEEDBACK_TYPES,
  FEEDBACK_TYPE_OPTIONS,
} from '@/pages/feedback/list/index.ts';

export default {
  name: 'list-table',
  components: {
    Trend,
  },
  data() {
    return {
      FEEDBACK_STATUS,
      FEEDBACK_STATUS_OPTIONS,
      FEEDBACK_TYPES,
      FEEDBACK_TYPE_OPTIONS,
      prefix,
      formData: {
        name: '',
        no: undefined,
        status: undefined,
      },
      data: [],
      dataLoading: false,
      value: 'first',
      columns: [
        {
          title: 'Title',
          fixed: 'left',
          width: 200,
          align: 'left',
          ellipsis: true,
          colKey: 'name',
        },
        { title: 'Status', colKey: 'status', width: 200, cell: { col: 'status' } },
        {
          title: 'Number',
          width: 200,
          ellipsis: true,
          colKey: 'no',
        },
        {
          title: 'Type',
          width: 200,
          ellipsis: true,
          colKey: 'feedbackType',
          cell: { col: 'feedbackType' }
        },
        // {
        //   title: '合同收付类型',
        //   width: 200,
        //   ellipsis: true,
        //   colKey: 'paymentType',
        // },
        // {
        //   title: '合同金额 (元)',
        //   width: 200,
        //   ellipsis: true,
        //   colKey: 'amount',
        // },
        {
          align: 'left',
          fixed: 'right',
          width: 200,
          colKey: 'op',
          title: 'Options',
        },
      ],
      rowKey: 'index',
      tableLayout: 'auto',
      verticalAlign: 'top',
      bordered: true,
      hover: true,
      rowClassName: (rowKey) => `${rowKey}-class`,
      // 与pagination对齐
      pagination: {
        defaultPageSize: 20,
        total: 100,
        defaultCurrent: 1,
      },
      confirmVisible: false,
      deleteIdx: -1,
    };
  },
  computed: {
    confirmBody() {
      if (this.deleteIdx > -1) {
        const { name } = this.data?.[this.deleteIdx];
        return `After deleting，${name}''s all contents will be clear and cannot be restored'`;
      }
      return '';
    },
    offsetTop() {
      return this.$store.state.setting.isUseTabsRouter ? 48 : 0;
    },
  },
  mounted() {
    this.dataLoading = true;
    // 模拟数据
    setTimeout(() => {
      const mockData = [];
      for (let i = 1; i <= 20; i++) {
        const status = Math.floor(Math.random() * 3); // 0, 1, 2
        const type = Math.floor(Math.random() * 2); // 0, 1
        mockData.push({
          id: i,
          name: `Feedback ${i}`,
          status: status,
          no: `FB-${1000 + i}`,
          feedbackType: type,
        });
      }
      this.data = mockData;
      this.pagination.total = mockData.length;
      this.dataLoading = false;
    }, 1000);
  },
  methods: {
    getContainer() {
      return document.querySelector('.tdesign-starter-layout');
    },
    onReset(data) {
      console.log(data);
    },
    onSubmit(data) {
      console.log(data);
    },
    rehandlePageChange(curr, pageInfo) {
      console.log('分页变化', curr, pageInfo);
    },
    rehandleChange(changeParams, triggerAndData) {
      console.log('统一Change', changeParams, triggerAndData);
    },
    rehandleClickOp({ text, row }) {
      console.log(text, row);
    },
    handleClickDelete(row) {
      this.deleteIdx = row.rowIndex;
      this.confirmVisible = true;
    },
    onConfirmDelete() {
      // 真实业务请发起请求
      this.data.splice(this.deleteIdx, 1);
      this.pagination.total = this.data.length;
      this.confirmVisible = false;
      this.$message.success('Delete successfully');
      this.resetIdx();
    },
    onCancel() {
      this.resetIdx();
    },
    resetIdx() {
      this.deleteIdx = -1;
    },
  },
};
</script>

<style lang="less" scoped>
@import '@/style/variables.less';

.list-feedback-table {
  background-color: var(--td-bg-color-container);
  padding: 30px 32px;
  border-radius: var(--td-radius-default);
}

.form-item-content {
  width: 100%;
}

.operation-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.payment-col {
  display: flex;

  .trend-container {
    display: flex;
    align-items: center;
    margin-left: 8px;
  }
}
.t-button + .t-button {
  margin-left: var(--td-comp-margin-s);
}
</style>
