<template>
  <t-config-provider :global-config="globalConfig">
    <t-card class="order-list-container" :bordered="false">
      <!-- Search & Filter -->
      <t-row justify="space-between">
        <div class="search-area">
          
          <t-select v-model="filterStatus" class="status-filter" placeholder="Filter by Status" clearable>
            <t-option value="" label="All" />
            <t-option value="completed" label="Completed" />
            <t-option value="paid" label="Paid" />
            <t-option value="pending" label="Pending" />
          </t-select>
          <t-input v-model="searchValue" class="search-input" placeholder="Search by Order ID" clearable>
            <template #suffix-icon><search-icon size="20px" /></template>
          </t-input>
        </div>
      </t-row>

      <!-- Table -->
      <div class="table-container">
        <t-table
          :columns="columns"
          :data="paginatedData"
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
  <t-tag :theme="badgeTheme(row.status)" variant="light" size="small">
    {{ row.status }}
  </t-tag>
</template>

          <template #op="{ rowIndex }">
            <a class="t-button-link" @click="handleView(rowIndex)">View</a>
            <a class="t-button-link" @click="promptDelete(rowIndex)">Delete</a>
          </template>
        </t-table>
      </div>

      <!-- Confirm Deletion -->
      <t-dialog
        header="Confirm Deletion"
        :body="`Are you sure you want to delete Order ID: ${orders[deleteIdx]?.order_id}?`"
        :visible.sync="confirmVisible"
        @confirm="confirmDelete"
        @cancel="cancelDelete"
      />

      <!-- Drawer -->
      <t-drawer
        :visible.sync="drawerVisible"
        header="Order Details"
        size="400px"
        :close-btn="true"
        @close="drawerVisible = false"
        :footer="false"
      >
        <div class="order-info">
          <div class="info-row"><span>Order ID:</span> {{ currentOrder.order_id }}</div>
          <div class="info-row"><span>User ID:</span> {{ currentOrder.user_id }}</div>
          <div class="info-row"><span>Scooter ID:</span> {{ currentOrder.scooter_id }}</div>
          <div class="info-row"><span>Start:</span> {{ currentOrder.start_time }}</div>
          <div class="info-row"><span>End:</span> {{ currentOrder.end_time }}</div>
          <!-- <div class="info-row"><span>Duration:</span> {{ currentOrder.duration }} hrs</div> -->
          <div class="info-row"><span>Cost:</span> ${{ currentOrder.cost }}</div>
          <div class="info-row"><span>Status:</span>
            <t-tag :theme="badgeTheme(currentOrder.status)" variant="light" size="small">
  {{ currentOrder.status }}
</t-tag>
          </div>
          <div class="info-row"><span>Address:</span> {{ currentOrder.pickup_address }}</div>
        </div>
      </t-drawer>
    </t-card>
  </t-config-provider>
</template>

<script>
import { SearchIcon } from 'tdesign-icons-vue';
import enConfig from 'tdesign-vue/es/locale/en_US';
import { getAllOrders } from '@/service/service-order'; // 根据实际路径调整

export default {
  name: 'OrderManagePage',
  components: { SearchIcon },
  data() {
    return {
      globalConfig: enConfig,
      dataLoading: false,
      drawerVisible: false,
      currentOrder: {},
      selectedRowKeys: [],
      confirmVisible: false,
      deleteIdx: -1,
      searchValue: '',
      filterStatus: '',
      rowKey: 'order_id',
      orders: [],
      pagination: {
        current: 1,
        pageSize: 5,
        total: 0,
        showJumper: true,
        showTotal: total => `Total ${total} orders`,
      },
      columns: [
        { colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left' },
        { title: 'Order ID', colKey: 'order_id', width: 120 },
        { title: 'User ID', colKey: 'user_id', width: 100 },
        { title: 'Scooter ID', colKey: 'scooter_id', width: 120 },
        { title: 'Start Time', colKey: 'start_time', width: 190 },
        { title: 'End Time', colKey: 'end_time', width: 190 },
        // {
        //     title: 'Duration (hrs)',
        //     colKey: 'duration_custom',
        //     width: 120,
        //     cell: ({ row }) => {
        //       if (!row || !row.start_time || !row.end_time) return '--';
        //       const start = new Date(row.start_time);
        //       const end = new Date(row.end_time);
        //       const duration = (end - start) / 3600000;
        //       return isNaN(duration) ? '--' : duration.toFixed(1);
        //     },
        //   },
        { title: 'Cost', colKey: 'cost', width: 100 },
        { title: 'Status', colKey: 'status', width: 100 },
        { title: 'Address', colKey: 'pickup_address', width: 180 },
        { title: 'Actions', colKey: 'op', fixed: 'right', width: 160 },
      ],
    };
  },

  computed: {
    offsetTop() {
      return this.$store?.state?.setting?.isUseTabsRouter ? 48 : 0;
    },
    paginatedData() {
      const keyword = this.searchValue.trim().toLowerCase();
      let filtered = this.orders;

      if (keyword) {
        filtered = filtered.filter(order =>
          order.order_id.toString().includes(keyword)
        );
      }

      if (this.filterStatus) {
        filtered = filtered.filter(order => order.status === this.filterStatus);
      }

      this.pagination.total = filtered.length;
      const start = (this.pagination.current - 1) * this.pagination.pageSize;
      const end = start + this.pagination.pageSize;
      return filtered.slice(start, end);
    },
  },

  methods: {
    badgeTheme(status) {
      switch (status) {
        case 'completed': return 'success';
        case 'paid': return 'warning';
        case 'pending': return 'danger';
        case 'active': return 'primary';
        case 'cancelled': return 'default';
        default: return 'default';
      }
    },

    async fetchOrders() {
      try {
        this.dataLoading = true;
        const res = await getAllOrders();
        console.log('Fetched orders:', res);

        if (res.code === 1 && Array.isArray(res.data)) {
          this.orders = res.data;
          this.pagination.total = res.data.length;
        } else {
          this.$message.error(res.msg || 'Failed to fetch orders');
        }
      } catch (err) {
        this.$message.error('Network error while fetching orders');
        console.error(err);
      } finally {
        this.dataLoading = false;
      }
    },

    onPageChange(pageInfo) {
      this.pagination.current = pageInfo.current;
      this.pagination.pageSize = pageInfo.pageSize;
    },

    handleSelectChange(keys) {
      this.selectedRowKeys = keys;
    },

    handleView(idx) {
      this.currentOrder = this.paginatedData[idx];
      this.drawerVisible = true;
    },

    promptDelete(idx) {
      this.deleteIdx = idx;
      this.confirmVisible = true;
    },

    confirmDelete() {
      const target = this.paginatedData[this.deleteIdx];
      const globalIdx = this.orders.findIndex(o => o.order_id === target.order_id);
      if (globalIdx !== -1) {
        this.orders.splice(globalIdx, 1);
        this.selectedRowKeys = [];
        this.confirmVisible = false;
        this.pagination.total = this.orders.length;
        this.$message.success('Order deleted');
      }
    },

    cancelDelete() {
      this.deleteIdx = -1;
      this.confirmVisible = false;
    },

    getContainer() {
      return document.querySelector('.tdesign-starter-layout');
    },
  },

  mounted() {
    this.fetchOrders();
  },
};
</script>



<style scoped>
.search-area {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 16px;
}

.search-input {
  width: 260px;
}

.status-filter {
  width: 200px;
}

.table-container {
  margin-top: 16px;
}

.order-info {
  padding: 20px;
  font-size: 14px;
  line-height: 2;
  background-color: #f0f6ff;
  border-radius: 10px;
  /* box-shadow: inset 0 0 5px #d0e7ff; */
}

.order-info {
  padding: 20px;
  font-size: 14px;
  line-height: 2;
  background-color: var(--td-bg-color-container, #f0f6ff);
  color: var(--td-text-color-primary, #000);
  border-radius: 10px;
}

.order-info .info-row {
  display: flex;
  justify-content: space-between;
  padding: 4px 0;
  border-bottom: 1px dashed var(--td-border-color, #cce0ff);
}

.order-info .info-row:last-child {
  border-bottom: none;
}

.order-info .info-row span {
  font-weight: 600;
  color: var(--td-text-color-secondary, #333);
}

</style>
