<template>
  <t-config-provider :global-config="globalConfig">
    <t-card class="coupon-list-container" :bordered="false">
      <!-- Header Buttons and Search -->
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="openAddDrawer">Add Coupon</t-button>
          <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length" @click="openDistributeDialog">
            Distribute
          </t-button>
          <p v-if="selectedRowKeys.length" class="selected-count">Selected {{ selectedRowKeys.length }} items</p>
        </div>
        <div class="search-area">
          <t-input v-model="searchValue" class="search-input" placeholder="Search by Coupon Name" clearable>
            <template #suffix-icon><search-icon size="20px" /></template>
          </t-input>
        </div>
      </t-row>

      <!-- Table -->
      <div class="table-container">
        <t-table :columns="columns" :data="filteredData" :rowKey="rowKey" :hover="true" :pagination="pagination"
          :selected-row-keys="selectedRowKeys" :loading="loading" @page-change="onPageChange"
          @select-change="handleSelectChange">
          <template #status="{ row }">
            <t-tag :theme="row.is_active ? 'success' : 'warning'" variant="light">
              {{ row.is_active ? 'Active' : 'Inactive' }}
            </t-tag>
          </template>
          <template #op="{ row }">
            <a class="t-button-link" @click="toggleCoupon(row)">
              {{ row.is_active ? 'Deactivate' : 'Activate' }}
            </a>
          </template>
        </t-table>
      </div>

      <!-- Drawer Form -->
      <t-drawer :visible="drawerVisible" @close="drawerVisible = false" header="Add Coupon" size="400px">
        <template #footer>
          <t-button theme="default" @click="drawerVisible = false">Cancel</t-button>
          <t-button theme="primary" @click="handleSubmit">Confirm</t-button>
        </template>
        <t-form layout="vertical">
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
        </t-form>
      </t-drawer>

      <!-- Distribute Dialog -->
      <t-dialog
        header="Distribute Coupons"
        :visible="distributeVisible"
        @close="distributeVisible = false"  
        @confirm="handleDistribute"
        @cancel="() => (distributeVisible = false)"
        closeBtn
      >
        <t-radio-group v-model="distributeType">
          <t-radio-button value="all">All Users</t-radio-button>
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
import { couponService } from '@/service/service-coupon';
import { getFrequentUsers, getAllNonAdminUsers } from '@/service/service-user';

export default {
  name: 'CouponManagePage',
  components: { SearchIcon },
  data() {
    return {
      globalConfig: enConfig,
      loading: false,
      allCoupons: [],
      selectedRowKeys: [],
      searchValue: '',
      drawerVisible: false,
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
        { title: 'Coupon ID', colKey: 'coupon_id' },
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
  computed: {
    filteredData() {
      const keyword = this.searchValue.trim().toLowerCase();
      const filtered = keyword ? this.allCoupons.filter(c => c.coupon_name.toLowerCase().includes(keyword)) : this.allCoupons;
      this.pagination.total = filtered.length;
      const start = (this.pagination.current - 1) * this.pagination.pageSize;
      return filtered.slice(start, start + this.pagination.pageSize);
    },
  },
  mounted() {
    this.fetchCoupons();
  },
  methods: {
    async fetchCoupons() {
      this.loading = true;
      try {
        const data = await couponService.getAllCoupons();
        this.allCoupons = data;
      } catch (err) {
        this.$message.error('API error');
      } finally {
        this.loading = false;
      }
    },
    onPageChange({ current, pageSize }) {
      this.pagination.current = current;
      this.pagination.pageSize = pageSize;
    },
    handleSelectChange(keys) {
      this.selectedRowKeys = keys;
    },
    openAddDrawer() {
      this.drawerVisible = true;
      this.form = {
        coupon_name: '',
        min_spend: 0,
        coupon_amount: 0,
        valid_from: '',
        valid_to: '',
      };
    },
    async handleSubmit() {
        try {
          const payload = {
            ...this.form,
            valid_from: new Date(this.form.valid_from).toISOString(),
            valid_to: new Date(this.form.valid_to).toISOString(),
          };
          console.log('Submitting payload:', payload);
          const res = await couponService.addCoupon(payload);
          console.log('Add Coupon Response:', res);

          if (res === true) {
            this.$message.success('Coupon added');
            this.drawerVisible = false;
            this.fetchCoupons();
          } else {
            this.$message.error('Failed to add coupon');
          }
        } catch (e) {
          console.error('Add Coupon Error:', e);
          this.$message.error('Save error');
        }
      },
    async toggleCoupon(row) {
      try {
        if (row.is_active) {
          await couponService.deactivateCoupon(row.coupon_id);
        } else {
          await couponService.activateCoupon(row.coupon_id);
        }
        this.fetchCoupons();
      } catch {
        this.$message.error('Status change failed');
      }
    },
    async handleDistribute() {
      try {
        let user_ids = [];

        // 获取用户ID列表
        if (this.distributeType === 'frequent') {
          const users = await getFrequentUsers();
          user_ids = users.map(u => u.userId);
          console.log('Frequent users:', user_ids);
        } else if (this.distributeType === 'all') {
          const all = await getAllNonAdminUsers();
          user_ids = all.map(u => u.userId);
          console.log('All users:', user_ids);
        } else if (this.distributeType === 'normal') {
          const all = await getAllNonAdminUsers();
          const frequent = await getFrequentUsers();
          const frequentIds = new Set(frequent.map(u => u.userId));
          user_ids = all.map(u => u.userId).filter(id => !frequentIds.has(id));
          console.log('Normal users:', user_ids);
        }

        if (!user_ids.length) {
          this.$message.warning('No users found for this type');
          return;
        }

        if (!this.selectedRowKeys.length) {
          this.$message.warning('Please select a coupon');
          return;
        }

        const selectedCoupon = this.allCoupons.find(
          c => c.coupon_id === this.selectedRowKeys[0]
        );
        if (!selectedCoupon || !selectedCoupon.is_active) {
          this.$message.warning('Only active coupons can be distributed');
          return;
        }

        const request = {
          coupon_id: this.selectedRowKeys[0],
          user_ids,
        };

        console.log('Distribute payload:', request);

        const res = await couponService.distributeCoupons(request);
        console.log('Distribute response:', res);

        // 判断返回结果
        if (res?.success_count > 0) {
          this.$message.success(`✅ Distributed to ${res.success_count} users`);

          if (res.fail_count > 0) {
            this.$dialog.alert({
              header: 'Partial Failure',
              body: `${res.fail_count} users may have already received this coupon.`,
              confirmBtn: null,
            });
          }
        } else {
          // 所有失败情况
          this.$dialog.alert({
            header: 'Distribution Failed',
            body: res?.msg || `${res.fail_count} users may have already received this coupon.`,
            confirmBtn: null,
          });
        }

        this.distributeVisible = false;
      } catch (e) {
        console.error('Distribute exception:', e);
        this.$message.error('Distribution failed');
      }
    },
    openDistributeDialog() {
      this.distributeVisible = true;
    },
  },
};
</script>

<style scoped>
.left-operation-container {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.selected-count {
  margin-left: 12px;
}

.search-area {
  display: flex;
  align-items: center;
  gap: 12px;
}

.search-input {
  width: 240px;
}

.table-container {
  margin-top: 16px;
}
</style>