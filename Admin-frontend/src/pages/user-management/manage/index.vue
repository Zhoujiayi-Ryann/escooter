<template>
  <t-config-provider :global-config="globalConfig">
    <t-card class="user-list-container" :bordered="false">
      <t-row justify="space-between">
        <div class="search-area">
          <t-button theme="default" :disabled="!selectedRowKeys.length" @click="batchDisableUsers">
          Disable Selected
        </t-button>
          <t-select v-model="filterType" class="role-filter" placeholder="Filter by User Type">
            <t-option value="" label="All Types" />
            <t-option value="frequent" label="Frequent User" />
            <t-option value="normal" label="Normal User" />
          </t-select>
          <t-input v-model="searchValue" class="search-input" placeholder="Search by Username / Email" clearable>
            <template #suffix-icon><search-icon size="20px" /></template>
          </t-input>
        </div>
      </t-row>

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
              {{ row.status === 'active' ? 'Active' : 'Disabled' }}
            </t-tag>
          </template>

          <template #type="{ row }">
            <t-tag :theme="row.type === 'frequent' ? 'primary' : 'default'" variant="light">
              {{ row.type === 'frequent' ? 'Frequent User' : 'Normal User' }}
            </t-tag>
          </template>

          <template #op="{ row }">
            <a class="t-button-link" @click="handleView(row)">View</a>
            <a class="t-button-link" @click="handleToggleStatus(row)">Toggle</a>
          </template>
        </t-table>
      </div>

      <t-dialog
        header="Confirm Status Change"
        :body="confirmBody"
        :visible.sync="confirmVisible"
        @confirm="confirmStatusChange"
        @cancel="cancelStatusChange"
      />

      <t-drawer
  :visible.sync="drawerVisible"
  :header="`User Info - ${currentUser.name}`"
  size="400px"
  :close-btn="true"
  @close="drawerVisible = false"
  :footer="false"
>
  <t-descriptions :column="1" bordered size="small">
    <t-descriptions-item label="Username">{{ currentUser.name }}</t-descriptions-item>
    <t-descriptions-item label="Email">{{ currentUser.email }}</t-descriptions-item>
    <t-descriptions-item label="Phone Number">{{ currentUser.phone }}</t-descriptions-item>
    <t-descriptions-item label="Type">
      <t-tag :theme="currentUser.type === 'frequent' ? 'primary' : 'default'" variant="light">
        {{ currentUser.type === 'frequent' ? 'Frequent User' : 'Normal User' }}
      </t-tag>
    </t-descriptions-item>
    <t-descriptions-item label="Status">
      <t-tag :theme="currentUser.status === 'active' ? 'success' : 'warning'" variant="light">
        {{ currentUser.status === 'active' ? 'Active' : 'Disabled' }}
      </t-tag>
    </t-descriptions-item>
    <t-descriptions-item label="Registration Date">{{ currentUser.createdAt }}</t-descriptions-item>
    <t-descriptions-item label="Total Usage Hours">{{ currentUser.usageHours }}</t-descriptions-item>
    <t-descriptions-item label="Total Spent (£)">{{ currentUser.spent }}</t-descriptions-item>
  </t-descriptions>
</t-drawer>

    </t-card>
  </t-config-provider>
</template>

<script>
import { SearchIcon } from 'tdesign-icons-vue';
import enConfig from 'tdesign-vue/es/locale/en_US';
import { getAllNonAdminUsers, toggleUserDisabledStatus, getFrequentUsers } from '@/service/service-user';

export default {
  name: 'UserManagePage',
  components: { SearchIcon },
  data() {
    return {
      globalConfig: enConfig,
      dataLoading: false,
      allData: [],
      data: [],
      selectedRowKeys: [],
      searchValue: '',
      filterType: '',
      confirmVisible: false,
      currentUserForToggle: null,
      drawerVisible: false,
      currentUser: {},
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
        { title: 'User Type', colKey: 'type', width: 120, cell: { col: 'type' } },
        { title: 'Total Usage Hours', colKey: 'usageHours', width: 140 },
        { title: 'Total Spent (￡)', colKey: 'spent', width: 140 },
        { title: 'Status', colKey: 'status', width: 100, cell: { col: 'status' } },
        { title: 'Registration Date', colKey: 'createdAt', width: 180 },
        { title: 'Actions', colKey: 'op', fixed: 'right', width: 160 },
      ],
    };
  },
  computed: {
    confirmBody() {
      const user = this.currentUserForToggle;
      return user ? `Are you sure you want to change the status of user ${user.name} to ${user.status === 'active' ? 'disabled' : 'active'}?` : '';
    },
    offsetTop() {
      return this.$store?.state?.setting?.isUseTabsRouter ? 48 : 0;
    },
  },
  watch: {
    filterType: 'fetchData',
    searchValue: 'fetchData'
  },
  mounted() {
    this.fetchUsers();
    this.$TDesignConfig?.(enConfig);
  },
  methods: {
    async batchDisableUsers() {
      try {
        const promises = this.selectedRowKeys.map(id => toggleUserDisabledStatus(id));
        const results = await Promise.allSettled(promises);

        const successCount = results.filter(r => r.status === 'fulfilled').length;
        this.$message.success(`Disabled ${successCount} users successfully`);
        await this.fetchUsers();
      } catch (err) {
        this.$message.error('Batch disable failed');
      }
    },
    getContainer() {
      return document.querySelector('.tdesign-starter-layout');
    },
    async fetchUsers() {
      this.dataLoading = true;
      try {
        const users = await getAllNonAdminUsers();
        const frequentUsers = await getFrequentUsers();
        const frequentIds = frequentUsers.map(user => user.userId);

        if (Array.isArray(users)) {
          this.allData = users.map(user => ({
            id: user.userId,
            name: user.username,
            email: user.email,
            phone: user.phoneNumber,
            type: frequentIds.includes(user.userId) ? 'frequent' : 'normal',
            status: user.isDisabled ? 'disabled' : 'active',
            // createdAt: user.registrationDate,
            usageHours: user.totalUsageHours || 0,
            spent: user.totalSpent?.toFixed(2) || '0.00',
            createdAt: this.formatDate(user.registrationDate),
          }));
          this.pagination.total = this.allData.length;
          this.updatePageData();
        } else {
          this.$message.error('Unexpected response format from server');
        }
      } catch (error) {
        this.$message.error('Failed to fetch users');
        console.error(error);
      } finally {
        this.dataLoading = false;
      }
    },
    formatDate(dateStr) {
        const date = new Date(dateStr);
        const pad = n => (n < 10 ? '0' + n : n);
        return `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
      },
    fetchData() {
      let filtered = [...this.allData];

      if (this.filterType) {
        filtered = filtered.filter(user => user.type === this.filterType);
      }

      const keyword = this.searchValue.trim().toLowerCase();
      if (keyword) {
        filtered = filtered.filter(
          user => user.name.toLowerCase().includes(keyword) || user.email.toLowerCase().includes(keyword)
        );
      }

      this.pagination.total = filtered.length;
      this.pagination.current = 1;
      this.updatePageData(filtered);
    },
    updatePageData(filteredData = this.allData) {
      const { current, pageSize } = this.pagination;
      const start = (current - 1) * pageSize;
      const end = start + pageSize;
      this.data = filteredData.slice(start, end);
    },
    onPageChange(pageInfo) {
      this.pagination.current = pageInfo.current;
      this.pagination.pageSize = pageInfo.pageSize;
      this.updatePageData();
    },
    handleSelectChange(keys) {
      this.selectedRowKeys = keys;
    },
    handleView(user) {
      this.currentUser = user;
      this.drawerVisible = true;
    },
    handleToggleStatus(user) {
      this.currentUserForToggle = user;
      this.confirmVisible = true;
    },
    async confirmStatusChange() {
      const user = this.currentUserForToggle;
      if (!user) return;

      try {
        const updatedUser = await toggleUserDisabledStatus(user.id);
        const index = this.allData.findIndex(u => u.id === updatedUser.userId);
        if (index !== -1) {
          this.allData[index].status = updatedUser.isDisabled ? 'disabled' : 'active';
        }
        this.fetchData();
        this.$message.success(`User ${updatedUser.username} status updated`);
      } catch (err) {
        this.$message.error('Failed to update user status');
      } finally {
        this.confirmVisible = false;
        this.currentUserForToggle = null;
      }
    },
    cancelStatusChange() {
      this.confirmVisible = false;
      this.currentUserForToggle = null;
    },
  },
};
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
:deep(.t-descriptions__body .t-descriptions__item-content) {
  line-height: 24px;
}
:deep(.t-descriptions__body .t-descriptions__item) {
  padding-top: 16px;
  padding-bottom: 16px;
}
</style>