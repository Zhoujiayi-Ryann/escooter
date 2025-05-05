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

      <!-- 表格 -->
      <div class="table-container">
        <t-table
          :columns="columns"
          :data="data"
          :rowKey="rowKey"
          :hover="true"
          :pagination="pagination"
          :selected-row-keys="selectedRowKeys"
          :loading="dataLoading"
          @page-change="onPageChange"
          @select-change="handleSelectChange"
          :headerAffixedTop="true"
          :headerAffixProps="{ offsetTop: offsetTop, container: getContainer }"
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

      <!-- 新增 / 编辑 抽屉 -->
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
            <t-date-picker v-model="form.valid_from" enable-time-picker />
          </t-form-item>
          <t-form-item label="Valid To">
            <t-date-picker v-model="form.valid_to" enable-time-picker />
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
import {
  getAllCoupons,
  addCoupon,
  activateCoupon,
  deactivateCoupon,
  distributeCoupons
} from '@/service/service-coupon';

export default {
  name: 'CouponManagePage',
  components: { SearchIcon },
  data() {
    return {
      globalConfig: enConfig,
      allData: [],
      data: [],
      selectedRowKeys: [],
      searchValue: '',
      dataLoading: false,
      drawerVisible: false,
      editMode: false,
      form: {
        coupon_name: '',
        min_spend: 0,
        coupon_amount: 0,
        valid_from: '',
        valid_to: ''
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
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
        { title: 'Actions', colKey: 'op', fixed: 'right' }
      ]
    };
  },
  computed: {
    offsetTop() {
      return this.$store?.state?.setting?.isUseTabsRouter ? 48 : 0;
    }
  },
  watch: {
    searchValue() {
      this.fetchData();
    }
  },
  mounted() {
    this.fetchCoupons();
  },
  methods: {
    getContainer() {
      return document.querySelector('.tdesign-starter-layout');
    },
    async fetchCoupons() {
  this.dataLoading = true;
  try {
    const res = await getAllCoupons();
    if (res.code === 1 && Array.isArray(res.data)) {
      this.allData = res.data; // ✅ 正确解包
      this.pagination.total = this.allData.length;
      this.fetchData(); // ✅ 触发分页数据更新
    } else {
      this.$message.error(res.msg || 'Failed to fetch coupons');
    }
  } catch (e) {
    this.$message.error('Failed to fetch coupons');
    console.error('Fetch error:', e);
  } finally {
    this.dataLoading = false;
  }
},
    fetchData() {
      const keyword = this.searchValue.toLowerCase().trim();
      let filtered = [...this.allData];

      if (keyword) {
        filtered = filtered.filter(c =>
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
        valid_to: ''
      };
      this.drawerVisible = true;
    },
    editCoupon(row) {
      this.editMode = true;
      this.form = { ...row };
      this.drawerVisible = true;
    },
    async handleSubmit() {
      try {
        await addCoupon(this.form);
        this.$message.success(this.editMode ? 'Updated' : 'Added');
        this.drawerVisible = false;
        this.fetchCoupons();
      } catch {
        this.$message.error('Failed to save coupon');
      }
    },
    async toggleCoupon(row) {
      try {
        if (row.status === 'active') {
          await deactivateCoupon(row.coupon_id);
        } else {
          await activateCoupon(row.coupon_id);
        }
        this.$message.success('Status updated');
        this.fetchCoupons();
      } catch {
        this.$message.error('Failed to update status');
      }
    },
    openDistributeDialog() {
      this.distributeVisible = true;
    },
    async handleDistribute() {
      const user_ids = this.distributeType === 'frequent' ? [1, 2] : [3, 4];
      try {
        const res = await distributeCoupons({
          coupon_id: this.selectedRowKeys[0],
          user_ids
        });
        this.$message.success(`Distributed: ${res.success_count} success`);
        this.distributeVisible = false;
      } catch {
        this.$message.error('Distribute failed');
      }
    }
  }
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
