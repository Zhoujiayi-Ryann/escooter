<template>
    <t-card class="coupon-list-container" :bordered="false">
      <!-- Top Action Bar -->
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="handleAddCoupon">Add Coupon</t-button>
          <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length">Export Coupons</t-button>
          <p v-if="selectedRowKeys.length" class="selected-count">Selected {{ selectedRowKeys.length }} items</p>
        </div>
        <div class="search-area">
          <t-input v-model="searchValue" class="search-input" placeholder="Search by Coupon Name" clearable>
            <template #suffix-icon><search-icon size="20px" /></template>
          </t-input>
        </div>
      </t-row>
  
      <!-- Table Display -->
      <div class="table-container">
        <t-table
          :columns="columns"
          :data="filteredCoupons"
          :rowKey="rowKey"
          :hover="true"
          :pagination="pagination"
          :selected-row-keys="selectedRowKeys"
          :loading="dataLoading"
          @page-change="handlePageChange"
          @select-change="handleSelectChange"
          :headerAffixedTop="true"
          :headerAffixProps="{ offsetTop: offsetTop, container: getContainer }"
        >
          <!-- Status Rendering -->
          <template #status="{ row }">
            <t-tag :theme="row.is_active ? 'success' : 'warning'" variant="light">
              {{ row.is_active ? 'Active' : 'Inactive' }}
            </t-tag>
          </template>
  
          <!-- Action Column -->
          <template #op="{ rowIndex }">
            <a class="t-button-link" @click="handleEdit(rowIndex)">Edit</a>
            <a class="t-button-link" @click="handleDeactivate(rowIndex)">Deactivate</a>
            <a class="t-button-link" @click="handleActivate(rowIndex)">Activate</a>
          </template>
        </t-table>
      </div>
  
      <!-- Delete Confirmation Dialog -->
      <t-dialog
        header="Confirm Deletion?"
        :body="confirmBody"
        :visible.sync="confirmVisible"
        @confirm="confirmDelete"
        @cancel="cancelDelete"
      />
    </t-card>
  </template>
  
  <script>
  import { getAllCoupons, deactivateCoupon, activateCoupon } from '@/api/coupons'; // assuming the API is imported here
  import { SearchIcon } from 'tdesign-icons-vue';
  
  export default {
    name: 'CouponManagePage',
    components: {
      SearchIcon,
    },
    data() {
      return {
        dataLoading: false,
        coupons: [],  // List of coupons
        selectedRowKeys: [],  // Selected rows
        searchValue: '',  // Search term
        confirmVisible: false,  // Delete confirmation dialog
        deleteIdx: -1,  // Current delete index
        rowKey: 'coupon_id',
        pagination: {
          current: 1,
          pageSize: 10,
          total: 0,
          showJumper: true,   // Show page jumper
          showTotal: true     // Show total count
        },
        columns: [
          { colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left' },
          { title: 'Coupon Name', colKey: 'coupon_name', width: 200 },
          { title: 'Min Spend', colKey: 'min_spend', width: 100 },
          { title: 'Coupon Amount', colKey: 'coupon_amount', width: 120 },
          { title: 'Valid From', colKey: 'valid_from', width: 180 },
          { title: 'Valid To', colKey: 'valid_to', width: 180 },
          { title: 'Status', colKey: 'status', width: 100, cell: { col: 'status' } },
          { title: 'Actions', colKey: 'op', fixed: 'right', width: 200 },
        ],
      };
    },
    computed: {
      filteredCoupons() {
        let filtered = this.coupons;
  
        if (this.searchValue.trim()) {
          const keyword = this.searchValue.trim().toLowerCase();
          filtered = filtered.filter(coupon =>
            coupon.coupon_name.toLowerCase().includes(keyword)
          );
        }
  
        // Frontend pagination logic
        const start = (this.pagination.current - 1) * this.pagination.pageSize;
        const end = start + this.pagination.pageSize;
  
        return filtered.slice(start, end);
      }
    },
    mounted() {
      this.fetchCoupons();
    },
    methods: {
      // Fetch coupons from backend API
      fetchCoupons() {
        this.dataLoading = true;
  
        getAllCoupons()
          .then(response => {
            this.coupons = response.data;
            this.pagination.total = response.data.length;
            this.dataLoading = false;
          })
          .catch(() => {
            this.dataLoading = false;
          });
      },
  
      handlePageChange(pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      },
  
      handleSelectChange(keys) {
        this.selectedRowKeys = keys;
      },
  
      handleEdit(idx) {
        const coupon = this.filteredCoupons[idx];
        this.$router.push(`/coupon-management/edit/${coupon.coupon_id}`);
      },
  
      handleDeactivate(idx) {
        const coupon = this.filteredCoupons[idx];
        deactivateCoupon(coupon.coupon_id)
          .then(() => {
            this.$message.success('Coupon deactivated');
            this.fetchCoupons();
          })
          .catch(() => {
            this.$message.error('Failed to deactivate coupon');
          });
      },
  
      handleActivate(idx) {
        const coupon = this.filteredCoupons[idx];
        activateCoupon(coupon.coupon_id)
          .then(() => {
            this.$message.success('Coupon activated');
            this.fetchCoupons();
          })
          .catch(() => {
            this.$message.error('Failed to activate coupon');
          });
      },
  
      handleAddCoupon() {
        this.$router.push('/coupon-management/new');
      },
  
      cancelDelete() {
        this.deleteIdx = -1;
        this.confirmVisible = false;
      },
  
      confirmDelete() {
        this.coupons.splice(this.deleteIdx, 1);
        this.pagination.total = this.coupons.length;
        this.selectedRowKeys = [];
        this.confirmVisible = false;
        this.$message.success('Coupon deleted');
      },
  
      getContainer() {
        return document.querySelector('.tdesign-starter-layout');
      }
    }
  };
  </script>
  
  <style lang="less" scoped>
  .coupon-list-container {
    .left-operation-container {
      display: flex;
      align-items: center;
      margin-bottom: 16px;
  
      .t-button + .t-button {
        margin-left: 12px;
      }
  
      .selected-count {
        margin-left: 16px;
        color: var(--td-text-color-secondary);
      }
    }
  
    .search-area {
      display: flex;
      align-items: center;
      gap: 12px;
  
      .search-input {
        width: 220px;
      }
    }
  
    .table-container {
      margin-top: 16px;
    }
  }
  </style>
  