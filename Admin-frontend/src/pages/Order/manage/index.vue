<template>
    <t-card class="order-list-container" :bordered="false">
      <!-- Top Action Bar -->
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="handleAddOrder">Add Order</t-button>
          <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length">Export Orders</t-button>
          <p v-if="selectedRowKeys.length" class="selected-count">Selected {{ selectedRowKeys.length }} items</p>
        </div>
        <div class="search-area">
          <t-input v-model="searchValue" class="search-input" placeholder="Search by Order ID" clearable>
            <template #suffix-icon><search-icon size="20px" /></template>
          </t-input>
        </div>
      </t-row>
  
      <!-- Table Display -->
      <div class="table-container">
        <t-table
          :columns="columns"
          :data="filteredOrders"
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
          <!-- Action Column -->
          <template #op="{ rowIndex }">
            <a class="t-button-link" @click="handleEdit(rowIndex)">View</a>
            <a class="t-button-link" @click="handleEdit(rowIndex)">Edit</a>
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
  import { SearchIcon } from 'tdesign-icons-vue';
  
  export default {
    name: 'OrderManagePage',
    components: {
      SearchIcon,
    },
    data() {
      return {
        dataLoading: false,
        orders: [
          { order_id: 1, user_id: 6, scooter_id: 6, start_time: '2025-03-19 10:00:00', end_time: '2025-03-19 11:00:00', duration: 1, cost: 6.5, status: 'completed', address: '123 Main St' },
          { order_id: 2, user_id: 1, scooter_id: 10, start_time: '2025-03-20 10:00:00', end_time: '2025-03-20 11:00:00', duration: 1, cost: 5, status: 'completed', address: '123 Main St' },
          { order_id: 3, user_id: 4, scooter_id: 6, start_time: '2025-03-21 09:00:00', end_time: '2025-03-21 11:00:00', duration: 2, cost: 6.5, status: 'completed', address: '123 Main St' },
          { order_id: 4, user_id: 5, scooter_id: 8, start_time: '2025-04-13 11:00:00', end_time: '2025-04-13 13:00:00', duration: 2, cost: 19.5, status: 'paid', address: 'qqqqq1111' },
          // Add more simulated order data here as needed
        ],
        selectedRowKeys: [],
        searchValue: '',
        confirmVisible: false,
        deleteIdx: -1,
        rowKey: 'order_id',
        pagination: {
          current: 1,
          pageSize: 10,
          total: 4,
          showJumper: true,
          showTotal: true,
        },
        columns: [
          { colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left' },
          { title: 'Order ID', colKey: 'order_id', width: 120 },
          { title: 'User ID', colKey: 'user_id', width: 100 },
          { title: 'Scooter ID', colKey: 'scooter_id', width: 120 },
          { title: 'Start Time', colKey: 'start_time', width: 180 },
          { title: 'End Time', colKey: 'end_time', width: 180 },
          { title: 'Duration (hrs)', colKey: 'duration', width: 120 },
          { title: 'Cost', colKey: 'cost', width: 120 },
          { title: 'Status', colKey: 'status', width: 100 },
          { title: 'Address', colKey: 'address', width: 180 },
          { title: 'Actions', colKey: 'op', fixed: 'right', width: 200 },
        ],
      };
    },
    computed: {
      filteredOrders() {
        let filtered = this.orders;
  
        if (this.searchValue.trim()) {
          const keyword = this.searchValue.trim().toLowerCase();
          filtered = filtered.filter(order =>
            order.order_id.toString().includes(keyword)  // Searching by order ID
          );
        }
  
        const start = (this.pagination.current - 1) * this.pagination.pageSize;
        const end = start + this.pagination.pageSize;
  
        return filtered.slice(start, end);
      }
    },
    methods: {
      handlePageChange(pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      },
  
      handleSelectChange(keys) {
        this.selectedRowKeys = keys;
      },
  
      handleEdit(idx) {
        const order = this.filteredOrders[idx];
        this.$router.push(`/order-management/edit/${order.order_id}`);
      },
  
      handleAddOrder() {
        this.$router.push('/order-management/new');
      },
  
      cancelDelete() {
        this.deleteIdx = -1;
        this.confirmVisible = false;
      },
  
      confirmDelete() {
        this.orders.splice(this.deleteIdx, 1);
        this.pagination.total = this.orders.length;
        this.selectedRowKeys = [];
        this.confirmVisible = false;
        this.$message.success('Order deleted');
      },
  
      getContainer() {
        return document.querySelector('.tdesign-starter-layout');
      }
    }
  };
  </script>
  
  <style lang="less" scoped>
  .order-list-container {
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
  