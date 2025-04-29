<template>
  <t-config-provider :global-config="globalConfig">
  <t-card class="user-list-container" :bordered="false">
    <!-- Top Action Bar -->
    <t-row justify="space-between">
      <div class="search-area">
        <t-select v-model="filterRole" class="role-filter" placeholder="Filter by Role">
          <t-option value="" label="All Roles" />
          <t-option value="admin" label="Administrator" />
          <t-option value="user" label="Regular User" />
        </t-select>
        <t-input v-model="searchValue" class="search-input" placeholder="Search by Username / Email" clearable>
          <template #suffix-icon><search-icon size="20px" /></template>
        </t-input>
      </div>
    </t-row>

    <!-- Table Display -->
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
        <!-- Status Rendering -->
        <template #status="{ row }">
          <t-tag :theme="row.status === 'active' ? 'success' : 'warning'" variant="light">
            {{ row.status === 'active' ? 'Active' : 'Disabled' }}
          </t-tag>
        </template>
        <template #role="{ row }">
          <t-tag :theme="row.role === 'admin' ? 'primary' : 'default'" variant="light">
            {{ row.role === 'admin' ? 'Administrator' : 'Regular User' }}
          </t-tag>
        </template>

        <!-- Action Column -->
        <template #op="{ row }">
          <a class="t-button-link" @click="handleView(row)">View</a>
          <a class="t-button-link" @click="handleToggleStatus(row)">Toggle</a>
        </template>
      </t-table>
    </div>

    <!-- Delete Confirmation Dialog (Modified for status toggle) -->
    <t-dialog
      header="Confirm Status Change"
      :body="confirmBody"
      :visible.sync="confirmVisible"
      @confirm="confirmStatusChange"
      @cancel="cancelStatusChange"
      
    />

    <!-- Sidebar Drawer for Viewing User Info -->
    <t-drawer
      :visible.sync="drawerVisible"
      :header="`User Info - ${currentUser.name}`"
      :size="'400px'"
      :close-btn="true"
      @close="drawerVisible = false"
      :footer="false"
    >
      <div class="user-info">
        <div><strong>Username: </strong>{{ currentUser.name }}</div>
        <div><strong>Email: </strong>{{ currentUser.email }}</div>
        <div><strong>Phone Number: </strong>{{ currentUser.phone }}</div>
        <div><strong>Role: </strong>{{ currentUser.role === 'admin' ? 'Administrator' : 'Regular User' }}</div>
        <div><strong>Status: </strong>{{ currentUser.status === 'active' ? 'Active' : 'Disabled' }}</div>
        <div><strong>Registration Date: </strong>{{ currentUser.createdAt }}</div>
      </div>
    </t-drawer>
  </t-card>
</t-config-provider>
</template>

<script>
import { SearchIcon } from 'tdesign-icons-vue'
import enConfig from 'tdesign-vue/es/locale/en_US'; 

export default {
  name: 'UserManagePage',
  components: {
    SearchIcon,
  },
  data() {
    return {
      globalConfig: enConfig,
      dataLoading: false,
      allData: [],            // All user data
      data: [],               // Current page user data
      selectedRowKeys: [],    // Selected rows
      searchValue: '',        // Search keyword
      filterRole: '',         // Role filter
      confirmVisible: false,  // Status change confirmation dialog
      currentUserForToggle: null, // Current user for status change
      drawerVisible: false,   // Sidebar visibility for user info
      currentUser: {},        // The user data for the sidebar
      rowKey: 'id',
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
        showJumper: true,
        showTotal: (total) => `Total ${total} items`,
      },
      columns: [
        { colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left' },
        { title: 'Username', colKey: 'name', width: 150 },
        { title: 'Email', colKey: 'email', width: 220 },
        { title: 'Phone Number', colKey: 'phone', width: 160 },
        { title: 'Role', colKey: 'role', width: 120, cell: { col: 'role' } },
        { title: 'Status', colKey: 'status', width: 100, cell: { col: 'status' } },
        { title: 'Registration Date', colKey: 'createdAt', width: 180 },
        { title: 'Actions', colKey: 'op', fixed: 'right', width: 160 },
      ],
    }
  },
  computed: {
    confirmBody() {
      if (this.currentUserForToggle) {
        return `Are you sure you want to change the status of user ${this.currentUserForToggle.name} to ${this.currentUserForToggle.status === 'active' ? 'disabled' : 'active'}?`;
      }
      return '';
    },
    offsetTop() {
      return this.$store.state.setting.isUseTabsRouter ? 48 : 0;
    },
  },
  watch: {
    filterRole() {
      this.fetchData();
    },
    searchValue() {
      this.fetchData();
    },
  },
  mounted() {
    this.fetchUsers()
    this.$TDesignConfig(globalConfig); 
  },
  methods: {
    getContainer() {
      return document.querySelector('.tdesign-starter-layout');
    },
    fetchUsers() {
  this.dataLoading = false;

  setTimeout(() => {
    const roles = ['admin', 'user'];
    const statuses = ['active', 'disabled'];
    const mockData = [];

    for (let i = 1; i <= 250; i++) {
      const role = roles[Math.floor(Math.random() * roles.length)];
      const status = statuses[Math.floor(Math.random() * statuses.length)];

      mockData.push({
        id: i,
        name: `User${i}`,
        email: `user${i}@example.com`,
        phone: `1380000${String(1000 + i).slice(-4)}`,
        role: role,
        status: status,
        createdAt: this.randomDate('2024-01-01', '2024-04-24'),
      });
    }

    this.allData = mockData;
    this.pagination.total = mockData.length;
    this.fetchData(); // This will process and update current page data
  }, 500);
},
    fetchData() {
      let filtered = [...this.allData];

      // Apply role filter
      if (this.filterRole) {
        filtered = filtered.filter(user => user.role === this.filterRole)
      }

      // Apply search filter
      const keyword = this.searchValue.trim().toLowerCase();
      if (keyword) {
          filtered = filtered.filter(
              user =>
                  user.name.toLowerCase().includes(keyword) ||
                  user.email.toLowerCase().includes(keyword)
          );
      }

      // Update pagination total
      this.pagination.total = filtered.length;
      this.pagination.current = 1;

      // Update current page data
      this.updatePageData(filtered);
      },
      randomDate(start, end) {
          const startDate = new Date(start).getTime();
          const endDate = new Date(end).getTime();
          const randomTime = startDate + Math.random() * (endDate - startDate);
          const date = new Date(randomTime);
          return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
      },
    updatePageData(filteredData = this.allData) {
      const { current, pageSize } = this.pagination;
      const start = (current - 1) * pageSize;
      const end = start + pageSize;
      this.data = filteredData.slice(start, end);
    },
    onPageChange(pageInfo) {
      this.pagination = {
        ...this.pagination,
        current: pageInfo.current,
        pageSize: pageInfo.pageSize
      };
      this.updatePageData();
    },
    handleSelectChange(keys) {
      this.selectedRowKeys = keys
    },
    handleView(user) {
      this.currentUser = user;
      this.drawerVisible = true;
    },
    handleToggleStatus(user) {
      this.currentUserForToggle = user;
      this.confirmVisible = true;
    },
    confirmStatusChange() {
      if (this.currentUserForToggle) {
        // Toggle status between active and disabled
        this.currentUserForToggle.status = this.currentUserForToggle.status === 'active' ? 'disabled' : 'active';
        this.$message.success(`User ${this.currentUserForToggle.name} status has been updated`);
        
        // Refresh the data
        this.fetchData();
      }
      this.confirmVisible = false;
      this.currentUserForToggle = null;
    },
    cancelStatusChange() {
      this.confirmVisible = false
      this.currentUserForToggle = null
    },
  },
}
</script>

<style scoped>
  .search-area {
    display: flex;
    align-items: center;
    gap: 50px;
  }

    .role-filter {
      width: 180px;
    }

    .search-input {
      width: 320px;
    }
  

  .table-container {
    margin-top: 16px;
  }

  .user-info {
    padding: 16px;
    line-height: 4;
    font-size: 16px;
    background-color: #dddcf23d;
    border-radius: 8px;
  }

</style>