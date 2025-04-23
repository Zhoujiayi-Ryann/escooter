<template>
  <t-card class="user-list-container" :bordered="false">
    <!-- Top Action Bar -->
    <t-row justify="space-between">
      <div class="left-operation-container">
        <t-button @click="handleCreateUser">Add User</t-button>
        <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length">Export Users</t-button>
        <p v-if="selectedRowKeys.length" class="selected-count">Selected {{ selectedRowKeys.length }} items</p>
      </div>
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
        :data="filteredData"
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
        <template #op="{ rowIndex }">
          <a class="t-button-link" @click="handleView(rowIndex)">View</a>
          <a class="t-button-link" @click="handleDelete(rowIndex)">Delete</a>
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
import { SearchIcon } from 'tdesign-icons-vue'

export default {
  name: 'UserManagePage',
  components: {
    SearchIcon,
  },
  data() {
    return {
      dataLoading: false,
      data: [],               // 用户数据源
      selectedRowKeys: [],    // 选中的行
      searchValue: '',        // 搜索关键词
      filterRole: '',         // 角色筛选
      confirmVisible: false,  // 删除确认弹窗
      deleteIdx: -1,          // 当前删除索引
      rowKey: 'id',
      pagination: {
          current: 1,
          pageSize: 10,
          total: 0,
          showJumper: true,   // 可选：显示跳页
          showTotal: true     // 可选：显示总数
      },
      columns: [
      { colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left' },
        { title: 'Username', colKey: 'name', width: 150 },
        { title: 'Email', colKey: 'email', width: 220 },
        { title: 'Phone Number', colKey: 'phone', width: 160 },
        { title: 'Role', colKey: 'role', width: 120 },
        { title: 'Status', colKey: 'status', width: 100, cell: { col: 'status' } },
        { title: 'Registration Date', colKey: 'createdAt', width: 180 },
        { title: 'Actions', colKey: 'op', fixed: 'right', width: 160 },
      ],
    }
  },
  computed: {
  filteredData() {
    let filtered = this.data

    if (this.filterRole) {
      filtered = filtered.filter(user => user.role === this.filterRole)
    }

    const keyword = this.searchValue.trim().toLowerCase()
    if (keyword) {
      filtered = filtered.filter(
        user =>
          user.name.toLowerCase().includes(keyword) ||
          user.email.toLowerCase().includes(keyword)
      )
    }

   // ⭐ 前端分页处理
    const start = (this.pagination.current - 1) * this.pagination.pageSize;
    const end = start + this.pagination.pageSize;

    return filtered.slice(start, end);
  },
},

  mounted() {
    this.fetchUsers()
  },
  methods: {
    // 生成 Mock 用户数据
    fetchUsers() {
      this.dataLoading = true

      setTimeout(() => {
        const roles = ['admin', 'user']
        const statuses = ['active', 'disabled']
        const mockData = []

        for (let i = 1; i <= 50; i++) {
          const role = roles[Math.floor(Math.random() * roles.length)]
          const status = statuses[Math.floor(Math.random() * statuses.length)]

          mockData.push({
            id: i,
            name: `User${i}`,
            email: `user${i}@example.com`,
            phone: `1380000${String(1000 + i).slice(-4)}`,
            role: role,
            status: status,
            createdAt: this.randomDate('2024-01-01', '2024-04-24'),
          })
        }

        this.data = mockData
        this.pagination.total = mockData.length
        this.dataLoading = false
      }, 500)
    },

    // 随机日期生成
    randomDate(start, end) {
      const startDate = new Date(start).getTime()
      const endDate = new Date(end).getTime()
      const randomTime = startDate + Math.random() * (endDate - startDate)
      const date = new Date(randomTime)
      return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
    },

    handlePageChange(current) {
      this.pagination.current = pageInfo.current;
      this.pagination.pageSize = pageInfo.pageSize;
    },
    handleSelectChange(keys) {
      this.selectedRowKeys = keys
    },
    handleView(idx) {
  const user = this.filteredData[idx];
      sessionStorage.setItem('currentUser', JSON.stringify(user));
      this.$router.push(`/user-management/details/${user.id}`);
    },

    handleCreateUser() {
      this.$router.push('/user-management/new')
    },
    handleDelete(idx) {
      this.deleteIdx = idx
      this.confirmVisible = true
    },
    confirmDelete() {
      this.data.splice(this.deleteIdx, 1)
      this.pagination.total = this.data.length
      this.selectedRowKeys = []
      this.confirmVisible = false
      this.$message.success('Delete Success')
    },
    cancelDelete() {
      this.deleteIdx = -1
      this.confirmVisible = false
    },
    getContainer() {
      return document.querySelector('.tdesign-starter-layout')
    },
  },
}
</script>


<style lang="less" scoped>
.user-list-container {
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

    .role-filter {
      width: 140px;
    }

    .search-input {
      width: 220px;
    }
  }

  .table-container {
    margin-top: 16px;
  }
}
</style>
