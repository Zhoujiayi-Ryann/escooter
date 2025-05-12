<template>
  <t-config-provider :global-config="globalConfig">
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
            <t-row :gutter="[16, 24]">
              <t-col :flex="1">
                <t-form-item label="Feedback Type" name="type">
                  <t-select
                    v-model="formData.type"
                    class="form-item-content"
                    :options="FEEDBACK_TYPE_OPTIONS"
                    placeholder="Please select feedback type"
                  />
                </t-form-item>
              </t-col>
              <t-col :flex="1">
                <t-form-item label="Status" name="status">
                  <t-select
                    v-model="formData.status"
                    class="form-item-content"
                    :options="FEEDBACK_STATUS_OPTIONS"
                    placeholder="Please select status"
                  />
                </t-form-item>
              </t-col>
            </t-row>
          </t-col>

          <t-col :span="2" class="operation-container">
            <t-button theme="primary" type="submit" :style="{ marginLeft: '8px' }">Search</t-button>
            <t-button type="reset" variant="base" theme="default">Reset</t-button>
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
          @page-change="onPageChange"
          @change="rehandleChange"
          :loading="dataLoading"
          :headerAffixedTop="true"
          :headerAffixProps="{ offsetTop, container: getContainer }"
        >
          <template #status="{ row }">
            <t-tag v-if="row.status === FEEDBACK_STATUS.PENDING" theme="warning" variant="light">Todo</t-tag>
            <t-tag v-if="row.status === FEEDBACK_STATUS.PROCESSING" theme="primary" variant="light">Processing</t-tag>
            <t-tag v-if="row.status === FEEDBACK_STATUS.RESOLVED" theme="success" variant="light">Finished</t-tag>
            <t-tag v-if="row.status === FEEDBACK_STATUS.REJECTED" theme="danger" variant="light">Rejected</t-tag>
          </template>
          <template #priority="{ row }">
            <t-tag v-if="row.priority === 'LOW'" theme="default" variant="light">Low</t-tag>
            <t-tag v-if="row.priority === 'MEDIUM'" theme="primary" variant="light">Medium</t-tag>
            <t-tag v-if="row.priority === 'HIGH'" theme="warning" variant="light">High</t-tag>
            <t-tag v-if="row.priority === 'URGENT'" theme="danger" variant="light">Urgent</t-tag>
          </template>
          <template #feedbackType="{ row }">
            <p v-if="row.feedbackType === FEEDBACK_TYPES.USING">Using Problems</p>
            <p v-if="row.feedbackType === FEEDBACK_TYPES.EXPERIENCE">Experience Feedback</p>
          </template>
          <template #op="slotProps">
            <a class="t-button-link" @click="rehandleClickOp(slotProps)">Details</a>
            <a class="t-button-link" @click="handleClickDelete(slotProps)">Delete</a>
          </template>
          <template #priority-header>
            <div class="priority-header" @click="handlePrioritySort">
              <span>Priority</span>
              <div class="sort-icons">
                <t-icon name="chevron-up" :class="{ 'active': currentSort === 'asc' }" />
                <t-icon name="chevron-down" :class="{ 'active': currentSort === 'desc' }" />
              </div>
            </div>
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

      <!-- 添加反馈详情侧边栏 -->
      <t-drawer
        :visible.sync="drawerVisible"
        :header="selectedFeedback ? `Feedback Details - #${selectedFeedback.id}` : 'Feedback Details'"
        :footer="false"
        :size="'500px'"
        :close-btn="true"
        :on-close="onDrawerClose"
      >
        <div v-if="selectedFeedback" class="feedback-details">
          <t-descriptions :column="1" bordered>
            <t-descriptions-item label="ID">#{{ selectedFeedback.id }}</t-descriptions-item>
            <t-descriptions-item label="User">User {{ selectedFeedback.userId }}</t-descriptions-item>
            <t-descriptions-item label="Type">
              <t-tag v-if="selectedFeedback.type === FEEDBACK_TYPE.USING_PROBLEM" theme="warning" variant="light">Using Problem</t-tag>
              <t-tag v-if="selectedFeedback.type === FEEDBACK_TYPE.EXPERIENCE_FEEDBACK" theme="primary" variant="light">Experience Feedback</t-tag>
            </t-descriptions-item>
            <t-descriptions-item label="Status">
              <div v-if="selectedFeedback.status === FEEDBACK_STATUS.PENDING">
                <t-select
                  v-model="selectedFeedback.status"
                  :options="FEEDBACK_STATUS_OPTIONS"
                  @change="handleStatusChange"
                  style="width: 120px"
                />
              </div>
              <div v-else>
                <t-tag v-if="selectedFeedback.status === FEEDBACK_STATUS.PENDING" theme="warning" variant="light">Todo</t-tag>
                <t-tag v-if="selectedFeedback.status === FEEDBACK_STATUS.PROCESSING" theme="primary" variant="light">Processing</t-tag>
                <t-tag v-if="selectedFeedback.status === FEEDBACK_STATUS.RESOLVED" theme="success" variant="light">Finished</t-tag>
                <t-tag v-if="selectedFeedback.status === FEEDBACK_STATUS.REJECTED" theme="danger" variant="light">Rejected</t-tag>
              </div>
            </t-descriptions-item>
            <t-descriptions-item label="Priority">
              <t-select
                v-model="selectedFeedback.priority"
                :options="priorityOptions"
                @change="handlePriorityChange"
                style="width: 120px"
                :disabled="selectedFeedback.status === FEEDBACK_STATUS.RESOLVED"
              />
            </t-descriptions-item>
            <t-descriptions-item label="Description">{{ selectedFeedback.description }}</t-descriptions-item>
            <t-descriptions-item label="Created At">{{ selectedFeedback.createdAt }}</t-descriptions-item>
            <t-descriptions-item label="Images">
              <div v-if="selectedFeedback.images && selectedFeedback.images.length > 0" class="feedback-images">
                <t-image-viewer
                  v-model:visible="imagePreviewVisible"
                  :images="selectedFeedback.images"
                  :defaultIndex="currentImageIndex"
                  @close="closeImagePreview"
                />
                <div class="image-grid">
                  <t-image
                    v-for="(image, index) in selectedFeedback.images"
                    :key="index"
                    :src="image"
                    :style="{ width: '120px', height: '120px', objectFit: 'cover', margin: '4px', cursor: 'pointer' }"
                    @click="previewImage(index)"
                  />
                </div>
              </div>
              <div v-else>No images</div>
            </t-descriptions-item>
            <t-descriptions-item v-if="selectedFeedback.adminReply" label="Admin Reply">
              <div class="admin-reply">
                <p>{{ selectedFeedback.adminReply }}</p>
                <p class="reply-time" v-if="selectedFeedback.repliedAt">
                  Replied at: {{ new Date(selectedFeedback.repliedAt).toLocaleString() }}
                </p>
              </div>
            </t-descriptions-item>
          </t-descriptions>

          <div class="reply-section">
            <t-button 
              v-if="!showReplyInput && !selectedFeedback.adminReply && selectedFeedback.status !== FEEDBACK_STATUS.RESOLVED && selectedFeedback.status !== FEEDBACK_STATUS.REJECTED" 
              theme="primary" 
              @click="showReplyInput = true"
            >
              Reply
            </t-button>
            
            <div v-if="showReplyInput" class="reply-input-section">
              <t-textarea
                v-model="replyContent"
                placeholder="Enter your reply..."
                :autosize="{ minRows: 3, maxRows: 5 }"
                :disabled="isSubmittingReply"
              />
              <div class="reply-buttons">
                <t-button theme="default" @click="cancelReply" :disabled="isSubmittingReply">Cancel</t-button>
                <t-button theme="primary" @click="submitReply" :loading="isSubmittingReply">Submit</t-button>
              </div>
            </div>
          </div>
        </div>
      </t-drawer>
    </div>
  </t-config-provider>
</template>
<script>
import { prefix } from '@/config/global';
import Trend from '@/components/trend/index.vue';
import { feedbackService, FEEDBACK_STATUS, FEEDBACK_TYPE } from '@/service/service-feedback';
import enConfig from 'tdesign-vue/es/locale/en_US';
import host from '@/config/host';
import { 
  Drawer as TDrawer,
  Descriptions as TDescriptions,
  DescriptionsItem as TDescriptionsItem,
  Tag as TTag,
  Select as TSelect,
  Image as TImage,
  ImageViewer as TImageViewer,
} from 'tdesign-vue';

import {
  FEEDBACK_TYPES,
  FEEDBACK_TYPE_OPTIONS,
} from '@/pages/feedback/list/index.ts';

export default {
  name: 'list-table',
  components: {
    Trend,
    TDrawer,
    TDescriptions,
    TDescriptionsItem,
    TTag,
    TSelect,
    TImage,
    TImageViewer,
  },
  data() {
    return {
      globalConfig: enConfig,
      FEEDBACK_STATUS,
      FEEDBACK_TYPE,
      prefix,
      apiBaseUrl: 'https://khnrsggvzudb.sealoshzh.site',
      formData: {
        type: undefined,
        status: undefined,
      },
      data: [],
      dataLoading: false,
      value: 'first',
      currentSort: 'default', // 'default', 'desc', 'asc'
      sortPriority: {
        order: ['URGENT', 'HIGH', 'MEDIUM', 'LOW']
      },
      columns: [
        {
          title: 'Description',
          fixed: 'left',
          width: 200,
          align: 'left',
          ellipsis: true,
          colKey: 'description',
        },
        { title: 'Status', colKey: 'status', width: 200, cell: { col: 'status' } },
        {
          title: 'Type',
          width: 200,
          ellipsis: true,
          colKey: 'type',
          cell: { col: 'feedbackType' }
        },
        {
          title: 'Priority',
          width: 120,
          colKey: 'priority',
          cell: { col: 'priority' },
          align: 'center',
          title: 'priority-header',
        },
        {
          title: 'Created At',
          width: 200,
          ellipsis: true,
          colKey: 'createdAt',
        },
        {
          align: 'left',
          fixed: 'right',
          width: 200,
          colKey: 'op',
          title: 'Options',
        },
      ],
      rowKey: 'id',
      tableLayout: 'auto',
      verticalAlign: 'top',
      bordered: true,
      hover: true,
      rowClassName: (rowKey) => `${rowKey}-class`,
      pagination: {
        pageSize: 10,
        total: 0,
        current: 1,
        locale: {
          itemsPerPage: 'Items per page',
          jumpTo: 'Jump to',
          page: 'Page',
          total: 'Total {total} items',
        },
      },
      confirmVisible: false,
      deleteIdx: -1,
      allData: [],
      FEEDBACK_TYPE_OPTIONS: [
        { label: 'Using Problems', value: FEEDBACK_TYPE.USING_PROBLEM },
        { label: 'Experience Feedback', value: FEEDBACK_TYPE.EXPERIENCE_FEEDBACK },
      ],
      FEEDBACK_STATUS_OPTIONS: [
        { label: 'Todo', value: FEEDBACK_STATUS.PENDING },
        { label: 'Processing', value: FEEDBACK_STATUS.PROCESSING },
        { label: 'Finished', value: FEEDBACK_STATUS.RESOLVED },
        { label: 'Rejected', value: FEEDBACK_STATUS.REJECTED },
      ],
      drawerVisible: false,
      selectedFeedback: null,
      priorityOptions: [
        { label: 'Low', value: 'LOW' },
        { label: 'Medium', value: 'MEDIUM' },
        { label: 'High', value: 'HIGH' },
        { label: 'Urgent', value: 'URGENT' },
      ],
      showReplyInput: false,
      replyContent: '',
      imagePreviewVisible: false,
      currentImageIndex: 0,
      isSubmittingReply: false,
    };
  },
  computed: {
    confirmBody() {
      if (this.deleteIdx > -1) {
        const { description } = this.data?.[this.deleteIdx];
        return `After deleting，${description}''s all contents will be clear and cannot be restored'`;
      }
      return '';
    },
    offsetTop() {
      return this.$store.state.setting.isUseTabsRouter ? 48 : 0;
    },
  },
  mounted() {
    this.fetchFeedbackData();
  },
  methods: {
    getContainer() {
      return document.querySelector('.tdesign-starter-layout');
    },
    updatePageData() {
      const { current, pageSize } = this.pagination;
      const start = (current - 1) * pageSize;
      const end = start + pageSize;
      this.data = this.allData.slice(start, end);
    },
    onPageChange(pageInfo) {
      console.log('Page number changed:', pageInfo);
      this.pagination = {
        ...this.pagination,
        current: pageInfo.current,
        pageSize: pageInfo.pageSize
      };
      this.updatePageData();
    },
    async fetchFeedbackData() {
      this.dataLoading = true;
      try {
        const feedbackList = await feedbackService.getAllFeedback();
        this.allData = feedbackList;
        
        this.pagination.total = feedbackList.length;
        this.pagination.current = 1;
        this.updatePageData();
      } catch (error) {
        console.error('Failed to fetch feedback data:', error);
        this.$message.error('Failed to fetch feedback data');
      } finally {
        this.dataLoading = false;
      }
    },
    async onSubmit({ validateResult, firstError, e }) {
      if (!validateResult) {
        console.log('Validate Errors:', firstError, e);
        return;
      }
      
      this.dataLoading = true;
      try {
        const feedbackList = await feedbackService.getAllFeedback();
        
        this.allData = feedbackList.filter((item) => {
          let match = true;
          if (this.formData.type) {
            match = match && item.type === this.formData.type;
          }
          if (this.formData.status) {
            match = match && item.status === this.formData.status;
          }
          return match;
        });

        // 重置排序状态
        this.currentSort = 'default';
        // 按创建时间排序
        this.allData.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

        this.pagination.total = this.allData.length;
        this.pagination.current = 1;
        this.updatePageData();
      } catch (error) {
        console.error('Failed to fetch feedback data:', error);
        this.$message.error('Failed to fetch feedback data');
      } finally {
        this.dataLoading = false;
      }
    },
    onReset() {
      this.formData = {
        type: undefined,
        status: undefined,
      };
      // 重置排序状态
      this.currentSort = 'default';
      this.fetchFeedbackData();
    },
    rehandleChange(changeParams, triggerAndData) {
      console.log('统一Change', changeParams, triggerAndData);
    },
    handlePrioritySort() {
      console.log('Current sort:', this.currentSort);
      // 切换排序状态
      if (this.currentSort === 'default') {
        this.currentSort = 'desc';
      } else if (this.currentSort === 'desc') {
        this.currentSort = 'asc';
      } else {
        this.currentSort = 'default';
      }

      // 根据当前排序状态进行排序
      if (this.currentSort === 'desc') {
        this.allData.sort((a, b) => {
          const orderA = this.sortPriority.order.indexOf(a.priority);
          const orderB = this.sortPriority.order.indexOf(b.priority);
          return orderA - orderB;
        });
      } else if (this.currentSort === 'asc') {
        this.allData.sort((a, b) => {
          const orderA = this.sortPriority.order.indexOf(a.priority);
          const orderB = this.sortPriority.order.indexOf(b.priority);
          return orderB - orderA;
        });
      } else {
        // 恢复默认排序（按创建时间）
        this.allData.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      }
      
      // 更新表格数据
      this.updatePageData();
    },
    async rehandleClickOp({ row }) {
      try {
        const feedbackDetail = await feedbackService.getFeedbackDetail(row.id);
        if (feedbackDetail) {
          // 处理图片URL，添加完整的后端URL
          const images = feedbackDetail.images ? feedbackDetail.images.map(url => {
            if (url.startsWith('http://') || url.startsWith('https://')) {
              return url;
            }
            // 使用BASE_URL替代this.apiBaseUrl
            const baseUrl = this.apiBaseUrl;
            return `${baseUrl}${url}`;
          }) : [];

          this.selectedFeedback = {
            id: feedbackDetail.id,
            userId: feedbackDetail.user_id,
            type: feedbackDetail.feedback_type,
            description: feedbackDetail.description,
            status: feedbackDetail.status,
            priority: feedbackDetail.priority,
            createdAt: new Date(feedbackDetail.created_at).toLocaleString(),
            images: images,
            adminReply: feedbackDetail.admin_reply,
            repliedAt: feedbackDetail.replied_at
          };
          this.drawerVisible = true;
        }
      } catch (error) {
        console.error('Failed to get feedback details:', error);
        this.$message.error('Failed to get feedback details');
      }
    },
    async handleClickDelete(row) {
      this.deleteIdx = row.rowIndex;
      this.confirmVisible = true;
    },
    async onConfirmDelete() {
      try {
        const feedbackId = this.data[this.deleteIdx].id;
        const result = await feedbackService.deleteFeedback(feedbackId);
        if (result) {
          // 保存当前页码
          const currentPage = this.pagination.current;
          // 重新获取数据
          await this.fetchFeedbackData();
          // 如果当前页码大于1且该页没有数据了，就跳转到前一页
          if (currentPage > 1 && this.allData.length <= (currentPage - 1) * this.pagination.pageSize) {
            this.pagination.current = currentPage - 1;
          } else {
            // 否则保持在当前页
            this.pagination.current = currentPage;
          }
          this.updatePageData();
          this.$message.success('Delete successfully');
        } else {
          this.$message.error('Failed to delete feedback');
        }
      } catch (error) {
        console.error('删除反馈失败:', error);
        this.$message.error('Failed to delete feedback');
      } finally {
        this.confirmVisible = false;
        this.resetIdx();
      }
    },
    onCancel() {
      this.resetIdx();
    },
    resetIdx() {
      this.deleteIdx = -1;
    },
    onDrawerClose() {
      this.drawerVisible = false;
      this.selectedFeedback = null;
      this.showReplyInput = false;
      this.replyContent = '';
    },
    cancelReply() {
      this.showReplyInput = false;
      this.replyContent = '';
    },
    async submitReply() {
      if (!this.replyContent.trim()) {
        this.$message.warning('Please enter your reply');
        return;
      }

      if (this.isSubmittingReply) {
        return;
      }

      this.isSubmittingReply = true;
      try {
        const replyData = {
          admin_id: 1,
          content: this.replyContent.trim(),
          send_notification: true
        };

        const result = await feedbackService.replyFeedback(this.selectedFeedback.id, replyData);
        if (result) {
          // 更新反馈状态和优先级
          await feedbackService.updateFeedback(this.selectedFeedback.id, {
            description: this.selectedFeedback.description,
            status: FEEDBACK_STATUS.RESOLVED,
            priority: 'LOW'
          });

          this.$message.success('Reply submitted successfully');
          
          // 更新当前显示的反馈数据
          this.selectedFeedback = {
            ...this.selectedFeedback,
            status: FEEDBACK_STATUS.RESOLVED,
            priority: 'LOW',
            adminReply: this.replyContent.trim(),
            repliedAt: new Date().toISOString()
          };
          
          // 关闭回复输入框
          this.showReplyInput = false;
          this.replyContent = '';

          // 刷新列表数据
          await this.fetchFeedbackData();
          this.updatePageData();
        } else {
          throw new Error('Failed to submit reply');
        }
      } catch (error) {
        console.error('Failed to submit reply:', error);
        this.$message.error('Failed to submit reply');
      } finally {
        this.isSubmittingReply = false;
      }
    },
    async handlePriorityChange(value) {
      try {
        const result = await feedbackService.updateFeedback(this.selectedFeedback.id, {
          description: this.selectedFeedback.description,
          priority: value
        });
        
        if (result) {
          this.$message.success('Priority updated successfully');
          // 刷新页面数据
          await this.fetchFeedbackData();
          this.updatePageData();
        } else {
          this.$message.error('Failed to update priority');
          // 恢复原值
          const originalFeedback = this.data.find(item => item.id === this.selectedFeedback.id);
          if (originalFeedback) {
            this.selectedFeedback.priority = originalFeedback.priority;
          }
        }
      } catch (error) {
        console.error('Failed to update priority:', error);
        console.error('Error details:', {
          message: error.message,
          response: error.response?.data,
          status: error.response?.status
        });
        this.$message.error('Failed to update priority');
        // 恢复原值
        const originalFeedback = this.data.find(item => item.id === this.selectedFeedback.id);
        if (originalFeedback) {
          this.selectedFeedback.priority = originalFeedback.priority;
        }
      }
    },
    async handleStatusChange(value) {
      try {
        const result = await feedbackService.updateFeedback(this.selectedFeedback.id, {
          description: this.selectedFeedback.description,
          status: value
        });
        
        if (result) {
          this.$message.success('Status updated successfully');
          // 刷新页面数据
          await this.fetchFeedbackData();
          this.updatePageData();
        } else {
          this.$message.error('Failed to update status');
          // 恢复原值
          const originalFeedback = this.data.find(item => item.id === this.selectedFeedback.id);
          if (originalFeedback) {
            this.selectedFeedback.status = originalFeedback.status;
          }
        }
      } catch (error) {
        console.error('Failed to update status:', error);
        this.$message.error('Failed to update status');
        // 恢复原值
        const originalFeedback = this.data.find(item => item.id === this.selectedFeedback.id);
        if (originalFeedback) {
          this.selectedFeedback.status = originalFeedback.status;
        }
      }
    },
    previewImage(index) {
      this.currentImageIndex = index;
      this.imagePreviewVisible = true;
    },
    closeImagePreview() {
      this.imagePreviewVisible = false;
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

.feedback-details {
  padding: 16px;
}

:deep(.t-descriptions) {
  margin-top: 16px;
}

:deep(.t-descriptions__label) {
  width: 120px;
}

.image-count {
  color: var(--td-text-color-secondary);
}

:deep(.t-select) {
  width: 120px;
}

.priority-header {
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  user-select: none;
  gap: 4px;
  
  span {
    cursor: pointer;
  }
  
  .sort-icons {
    display: flex;
    flex-direction: column;
    font-size: 12px;
    line-height: 1;
    
    .t-icon {
      color: var(--td-text-color-secondary);
      transition: color 0.2s;
      
      &.active {
        color: var(--td-brand-color);
      }
    }
  }
}

.reply-section {
  margin-top: 24px;
  padding: 0 16px;
}

.reply-input-section {
  margin-top: 16px;
  
  .reply-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    margin-top: 16px;
  }
}

.feedback-images {
  .image-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-top: 8px;
  }
}

.admin-reply {
  padding: 12px;
  background-color: var(--td-bg-color-component);
  border-radius: var(--td-radius-medium);

  .reply-time {
    margin-top: 8px;
    font-size: 12px;
    color: var(--td-text-color-secondary);
  }
}
</style>
