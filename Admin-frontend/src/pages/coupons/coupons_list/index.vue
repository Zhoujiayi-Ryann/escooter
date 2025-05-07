<template>
  <t-config-provider :global-config="globalConfig">
    <t-card class="coupon-list-container" :bordered="false">
      <!-- 顶部操作区 -->
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="openAddDrawer">Add Coupon</t-button>
          <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length" @click="openDistributeDialog">
            Distribute
          </t-button>
          <p v-if="selectedRowKeys.length" class="selected-count">
            Selected {{ selectedRowKeys.length }} items
          </p>
        </div>
        <div class="search-area">
          <t-input v-model="searchValue" class="search-input" placeholder="Search by Coupon Name" clearable>
            <template #suffix-icon><search-icon size="20px" /></template>
          </t-input>
        </div>
      </t-row>

      <!-- 表格展示 -->
      <div class="table-container">
        <t-table
          :columns="columns"
          :data="data"
          :rowKey="rowKey"
          :hover="true"
          :pagination="pagination"
          :selected-row-keys="selectedRowKeys"
          @page-change="onPageChange"
          @select-change="handleSelectChange"
        >
          <template #status="{ row }">
            <t-tag :theme="row.status === 'active' ? 'success' : 'warning'" variant="light">
              {{ row.status === 'active' ? 'Active' : 'Inactive' }}
            </t-tag>
          </template>
          <template #op="{ row }">
            <a class="t-button-link" @click="editCoupon(row)">Edit</a>
            <a class="t-button-link" @click="toggleCoupon(row)">
              {{ row.status === 'active' ? 'Deactivate' : 'Activate' }}
            </a>
          </template>
        </t-table>
      </div>

      <!-- 抽屉表单 -->
      <t-drawer :visible.sync="drawerVisible" :header="editMode ? 'Edit Coupon' : 'Add Coupon'" size="400px">
        <t-form @submit.prevent="handleSubmit">
          <t-form-item label="Coupon Name">
            <t-input v-model="form.coupon_name" required />
          </t-form-item>
          <t-form-item label="Min Spend">
            <t-input-number v-model="form.min_spend" required />
          </t-form-item>
          <t-form-item label="Amount">
            <t-input-number v-model="form.coupon_amount" required />
          </t-form-item>
          <t-form-item label="Valid From">
            <t-date-picker v-model="form.valid_from" />
          </t-form-item>
          <t-form-item label="Valid To">
            <t-date-picker v-model="form.valid_to" />
          </t-form-item>
          <t-button type="submit" theme="primary">{{ editMode ? 'Save' : 'Add' }}</t-button>
        </t-form>
      </t-drawer>

      <!-- 发放弹窗 -->
      <t-dialog
        header="Distribute Coupons"
        :visible="distributeVisible"
        @confirm="handleDistribute"
        @cancel="() => (distributeVisible = false)"
      >
        <t-radio-group v-model="distributeType">
          <t-radio-button value="frequent">Frequent Users</t-radio-button>
          <t-radio-button value="normal">Normal Users</t-radio-button>
        </t-radio-group>
      </t-dialog>
    </t-card>
  </t-config-provider>
</template>

<script>
import { SearchIcon } from 'tdesign-icons-vue';
import enConfig from 'tdesign-vue/es/locale/en_US';

export default {
  name: 'CouponManagePage',
  components: { SearchIcon },
  data() {
    return {
      globalConfig: enConfig,
      allData: [
        {
          coupon_id: 11,
          coupon_name: 'New user exclusive',
          min_spend: 0,
          coupon_amount: 10,
          valid_from: '2023-06-01',
          valid_to: '2025-05-01',
          status: 'active',
        },
        {
          coupon_id: 12,
          coupon_name: 'Get 15 off on order',
          min_spend: 50,
          coupon_amount: 15,
          valid_from: '2023-06-01',
          valid_to: '2025-12-31',
          status: 'active',
        },
        {
          coupon_id: 13,
          coupon_name: 'Weekend discount',
          min_spend: 30,
          coupon_amount: 8,
          valid_from: '2023-06-01',
          valid_to: '2026-06-01',
          status: 'active',
        },
      ],
      data: [],
      selectedRowKeys: [],
      searchValue: '',
      drawerVisible: false,
      editMode: false,
      form: {
        coupon_name: '',
        min_spend: 0,
        coupon_amount: 0,
        valid_from: '',
        valid_to: '',
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
      rowKey: 'coupon_id',
      distributeVisible: false,
      distributeType: 'frequent',
      columns: [
        { colKey: 'row-select', type: 'multiple', width: 60 },
        { title: 'Name', colKey: 'coupon_name' },
        { title: 'Min Spend', colKey: 'min_spend' },
        { title: 'Amount', colKey: 'coupon_amount' },
        { title: 'Valid From', colKey: 'valid_from' },
        { title: 'Valid To', colKey: 'valid_to' },
        { title: 'Status', colKey: 'status', cell: { col: 'status' } },
        { title: 'Actions', colKey: 'op', fixed: 'right' },
      ],
    };
  },
  watch: {
    searchValue() {
      this.fetchData();
    },
  },
  mounted() {
    this.fetchCoupons();
  },
  methods: {
    fetchCoupons() {
      this.pagination.total = this.allData.length;
      this.fetchData();
    },
    fetchData() {
      const keyword = this.searchValue.toLowerCase().trim();
      let filtered = [...this.allData];

      if (keyword) {
        filtered = filtered.filter((c) =>
          c.coupon_name.toLowerCase().includes(keyword)
        );
      }

      this.pagination.total = filtered.length;
      const { current, pageSize } = this.pagination;
      const start = (current - 1) * pageSize;
      this.data = filtered.slice(start, start + pageSize);
    },
    onPageChange({ current, pageSize }) {
      this.pagination.current = current;
      this.pagination.pageSize = pageSize;
      this.fetchData();
    },
    handleSelectChange(keys) {
      this.selectedRowKeys = keys;
    },
    openAddDrawer() {
      this.editMode = false;
      this.form = {
        coupon_name: '',
        min_spend: 0,
        coupon_amount: 0,
        valid_from: '',
        valid_to: '',
      };
      this.drawerVisible = true;
    },
    editCoupon(row) {
      this.editMode = true;
      this.form = { ...row };
      this.drawerVisible = true;
    },
    handleSubmit() {
      if (this.editMode) {
        const index = this.allData.findIndex(
          (c) => c.coupon_id === this.form.coupon_id
        );
        if (index !== -1) {
          this.allData.splice(index, 1, { ...this.form });
        }
      } else {
        const newId = Math.max(...this.allData.map((c) => c.coupon_id)) + 1;
        this.allData.push({
          ...this.form,
          coupon_id: newId,
          status: 'active',
        });
      }
      this.drawerVisible = false;
      this.fetchCoupons();
      this.$message.success(this.editMode ? 'Updated' : 'Added');
    },
    toggleCoupon(row) {
      const index = this.allData.findIndex(
        (c) => c.coupon_id === row.coupon_id
      );
      if (index !== -1) {
        this.allData[index].status =
          this.allData[index].status === 'active' ? 'inactive' : 'active';
        this.fetchCoupons();
        this.$message.success('Status updated');
      }
    },
    openDistributeDialog() {
      this.distributeVisible = true;
    },
    handleDistribute() {
      this.$message.success('Simulated distribution successful!');
      this.distributeVisible = false;
    },
  },
};
</script>

<style scoped>
.left-operation-container {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.selected-count {
  margin-left: 16px;
}
.search-area {
  display: flex;
  align-items: center;
  gap: 12px;
}
.search-input {
  width: 220px;
}
.table-container {
  margin-top: 16px;
}
</style>
