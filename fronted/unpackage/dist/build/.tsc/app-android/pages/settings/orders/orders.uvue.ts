
import { orderApi } from '@/utils/api/order';
import { userApi } from '@/utils/api/user';

const __sfc__ = defineComponent({
	data() {
		return {
			tabs: ['All', 'Pending', 'Paid', 'Active', 'Completed'],
			currentTab: 0,
			selectedStatus: 0,
			orders: [], // 改为空数组，从API获取数据
			// 添加取消订单的相关数据
			showCancelDialog: false,
			currentCancelOrder: null,
			
			// 添加延长租车的相关数据
			showExtendDialog: false,
			currentExtendOrder: null,
			extendDuration: '1 day',
			extendOptions: ['1 hour', '2 hours', '4 hours', '1 day'],
			
			// 修改日期时间选择器相关数据
			showTimePicker: false,
			currentDate: new Date(),
			minDate: new Date(),  // 这个值会在实际使用时被更新
			maxDate: new Date(new Date().getFullYear() + 1, new Date().getMonth(), new Date().getDate()),
			selectedExtendDate: '',
			
			// 修正为正确的数组格式
			dateColumns: ['', '', ''], // 确保格式为 ['年', '月', '日']
			timeColumns: ['', ''],     // 确保格式为 ['时', '分']
			refreshing: false,
			loading: false, // 添加加载状态
			userId: userApi.getUserId(1), // 从用户API获取当前用户ID，如果未登录则使用默认值1
		}
	},
	computed: {
		filteredOrders() {
			if (this.selectedStatus === 0) {
				return this.orders; // 全部订单
			} else {
				return this.orders.filter(order => order.statusCode === this.selectedStatus);
			}
		}
	},
	methods: {
		navigateToHome() {
			const pages = getCurrentPages();
			if (pages.length > 1) {
				// 如果有上级页面，返回上级页面
				uni.navigateBack({
					delta: 1
				});
			} else {
				// 如果没有上级页面，返回主页
				uni.navigateTo({
					url: '/pages/home/home'
				});
			}
		},
		onTabChange(index) {
			// zjy的超级超级天才想法：解决莫名奇妙的报错
			const tabElement = index.target.closest('.van-tab');
			if (tabElement && tabElement.id) {
				// 从ID "van-tabs-1-1" 中提取末尾的数字作为索引
				const tabId = tabElement.id;
				const tabIndex = tabId.split('-').pop();
				// 更新选中的状态码
				this.selectedStatus = Number(tabIndex);
				console.log('当前状态码:', this.selectedStatus);
			}
		},
		deleteOrder(orderId) {
			uni.showModal({
				title: 'Tip',
				content: 'Are you sure to delete this order?',
				confirmText: 'Confirm',
				cancelText: 'Cancel',
				success: (res) => {
					if (res.confirm) {
						// 调用软删除订单API
						orderApi.softDeleteOrder(Number(orderId))
							.then(res => {
								if (res.code === 1) {
									// API调用成功，更新本地数据
									this.orders = this.orders.filter(order => order.id !== orderId);
									// 使用uni.$emit发送事件
									uni.$emit('refreshHomePage');
									uni.showToast({
										title: 'Deleted',
										icon: 'success'
									});
								} else {
									// API调用失败，显示错误信息
									uni.showToast({
										title: res.msg || 'Delete failed',
										icon: 'none'
									});
								}
							})
							.catch(err => {
								console.error('Network error, please try again later:', err);
								uni.showToast({
									title: 'Network error, please try again later',
									icon: 'none'
								});
							});
					}
				}
			});
		},
		viewOrderDetail(orderId) {
			// 跳转到订单详情页
			console.log(orderId)
			uni.navigateTo({
				url: `/pages/order_details/order_details?orderId=${orderId}`
			});
		},
		
		// 显示取消订单确认弹窗
		showCancelOrderConfirm(order) {
			this.currentCancelOrder = order;
			uni.showModal({
				title: 'Confirm Order Cancellation?',
				content: `Rental Duration: ${order.duration}\nBicycle ID: ${order.id}`,
				cancelText: 'Cancel',
				confirmText: 'Confirm',
				success: (res) => {
					if (res.confirm) {
						// 调用删除订单API
						orderApi.deleteOrder(Number(order.id))
							.then(res => {
								if (res.code === 1) {
									// API调用成功，更新本地数据
									this.orders = this.orders.filter(o => o.id !== order.id);
									uni.showToast({
										title: 'Order Cancelled',
										icon: 'success'
									});
								} else {
									// API调用失败，显示错误信息
									uni.showToast({
										title: res.msg || 'Cancel failed',
										icon: 'none'
									});
								}
							})
							.catch(err => {
								console.error('Cancel order error:', err);
								uni.showToast({
									title: 'Network error, please try again later',
									icon: 'none'
								});
							});
					}
				}
			});
		},
		
		// 显示延长租车对话框
		showExtendRentalDialog(order) {
		  this.currentExtendOrder = order;
		  this.selectedExtendDate = '';
		  
		  // 使用getAvailableTimeSlots接口获取可用时间段
		  orderApi.getAvailableTimeSlots(Number(order.id))
		    .then(res => {
			  console.log(res)
		      if (res.code === 1 && res.data) {
		        // 使用订单的结束时间作为最小可选日期
		        this.minDate = new Date(res.data.current_end_time);
		        
		        // 设置最大可选日期
		        if (res.data.next_start_time) {
		          // 如果有下一个订单，使用下一个订单的开始时间作为最大可选日期
		          this.maxDate = new Date(res.data.next_start_time);
		        } else if (res.data.max_extended_hours > 0) {
		          // 如果没有下一个订单但有最大延长时间，计算最大可选日期
		          const maxDate = new Date(res.data.current_end_time);
		          maxDate.setHours(maxDate.getHours() + res.data.max_extended_hours);
		          this.maxDate = maxDate;
		        } else {
		          // 默认情况下最大可延长一年
		          this.maxDate = new Date(new Date().getFullYear() + 1, new Date().getMonth(), new Date().getDate());
		        }
		        
		        // 显示日期选择器
		        this.showDateTimePicker();
		      } else {
		        uni.showToast({
		          title: 'Failed to get available time slots',
		          icon: 'none'
		        });
		      }
		    })
		    .catch(err => {
		      console.error('获取可用时间段失败:', err);
		      uni.showToast({
		        title: 'Network error, please try again later',
		        icon: 'none'
		      });
		    });
		},
		
		// 显示日期时间选择器
		showDateTimePicker() {
		  this.showTimePicker = true;
		  // 初始化日期和时间列
		  this.initDateTimeColumns();
		},
		
		// 初始化日期和时间列 - 使用订单结束时间
		initDateTimeColumns() {
		  // 使用已经设置好的minDate(当前订单结束时间)
		  // 确保初始值不早于最小日期
		  this.setDateTimeValues(this.minDate);
		},
		
		// 设置日期时间值的辅助方法
		setDateTimeValues(date) {
		  // 设置日期
		  this.dateColumns = [
		    date.getFullYear().toString(),
		    (date.getMonth() + 1).toString().padStart(2, '0'),
		    date.getDate().toString().padStart(2, '0')
		  ];
		  
		  // 设置时间
		  this.timeColumns = [
		    date.getHours().toString().padStart(2, '0'),
		    date.getMinutes().toString().padStart(2, '0')
		  ];
		},
		
		// 日期时间选择器确认 - 修改为直接执行
		onDateTimePickerConfirm() {
		  // 使用正确的日期时间格式
		  const year = this.dateColumns[0];
		  const month = this.dateColumns[1];
		  const day = this.dateColumns[2];
		  const hour = this.timeColumns[0];
		  const minute = this.timeColumns[1];
		  
		  this.selectedExtendDate = `${year}-${month}-${day} ${hour}:${minute}`;
		  this.showTimePicker = false;
		  
		  // 获取当前订单信息，然后调用extendOrder API
		  if (this.currentExtendOrder) {
		    // 构造API请求参数
		    const extendOrderRequest = {
		      order_id: Number(this.currentExtendOrder.id),
		      new_end_time: `${year}-${month}-${day}T${hour}:${minute}:00` // 将空格替换为"T"
		    };
		    console.log(extendOrderRequest)
		    // 调用延长订单API
		    orderApi.extendOrder(extendOrderRequest)
		      .then(res => {
				console.log(res)
		        if (res.code === 1 && res.data) {
		          // API调用成功，直接使用extendOrder的返回数据
		          const orderData = res.data;
		          // 构建订单信息对象，添加空值检查防止toString()错误
		          const orderInfo = {
		            orderId: orderData.order_id ? orderData.order_id.toString() : "",
		            scooterCode: orderData.scooter_id ? orderData.scooter_id.toString() : this.currentExtendOrder.type,
		            address: orderData.pickup_address || "未知地点",
		            cost: orderData.extended_cost !== null && orderData.extended_cost !== undefined ? orderData.extended_cost.toString() : "0",
		            discountAmount: orderData.discount_amount !== null && orderData.discount_amount !== undefined ? orderData.discount_amount.toString() : "0",
		            startDate: orderData.start_time ? this.formatDatePart(new Date(orderData.start_time)) : "",
		            startTime: orderData.start_time ? this.formatTimePart(new Date(orderData.start_time)) : "",
		            endDate: orderData.end_time ? this.formatDatePart(new Date(orderData.end_time)) : "",
		            endTime: orderData.end_time ? this.formatTimePart(new Date(orderData.end_time)) : "",
		            days: orderData.extended_duration ? orderData.extended_duration / 24 : 1
		          };
		          // 编码并跳转到支付页面
		          const orderInfoStr = encodeURIComponent(JSON.stringify(orderInfo));
		          uni.navigateTo({
		            url: `/pages/order/payment?orderInfo=${orderInfoStr}`
		          });
		        } else {
		          uni.showToast({
		            title: res.msg || 'Failed to extend order',
		            icon: 'none'
		          });
		        }
		      })
		      .catch(err => {
		        console.error('延长订单出错:', err);
		        uni.showToast({
		          title: 'Network error, please try again later',
		          icon: 'none'
		        });
		      });
		  }
		},
		
		// 日期时间选择器取消
		onDateTimePickerCancel() {
		  this.showTimePicker = false;
		},
		
		// 格式化日期
		formatDate(date) {
		  const year = date.getFullYear();
		  const month = this.padZero(date.getMonth() + 1);
		  const day = this.padZero(date.getDate());
		  const hour = this.padZero(date.getHours());
		  const minute = this.padZero(date.getMinutes());
		  
		  return `${year}-${month}-${day} ${hour}:${minute}`;
		},
		
		// 补零
		padZero(num) {
		  return String(num).padStart(2, '0');
		},
		
		// 格式化日期部分 (YYYY-MM-DD)
		formatDatePart(date) {
		  const year = date.getFullYear();
		  const month = this.padZero(date.getMonth() + 1);
		  const day = this.padZero(date.getDate());
		  
		  return `${year}-${month}-${day}`;
		},
		
		// 格式化时间部分 (HH:MM)
		formatTimePart(date) {
		  const hour = this.padZero(date.getHours());
		  const minute = this.padZero(date.getMinutes());
		  
		  return `${hour}:${minute}`;
		},
		
		onRefresh() {
			console.log('开始刷新');
			this.fetchOrders(); // 使用API重新获取数据
		},
		
		// 获取用户订单数据
		fetchOrders() {
			this.loading = true;
			
			orderApi.getUserOrders(this.userId)
				.then(res => {
					console.log(res);
					if (res.code === 1 && res.data) {  
						// 将API返回的数据转换为当前使用的格式
						this.orders = res.data.map(order => {
							return {
								id: order.order_id.toString(),
								type: 'Bicycle',
								status: this.getStatusText(order.status),
								duration: this.formatDuration(order.start_time, order.end_time),
								orderTime: this.formatTime(order.start_time), // 使用start_time作为订单时间
								price: order.cost.toString(),  // 使用cost而不是amount
								statusCode: this.mapStatusToCode(order.status) // 使用字符串状态映射
							};
						});
						console.log(this.orders);
					} else {
						uni.showToast({
							title: res.msg || 'Failed to get orders',
							icon: 'none'
						});
					}
				})
				.catch(err => {
					console.error('获取订单出错:', err);
					uni.showToast({
						title: 'Network error, please try again later',
						icon: 'none'
					});
				})
				.finally(() => {
					this.loading = false;
					this.refreshing = false;
				});
		},
		
		// 根据状态字符串映射到状态码
		mapStatusToCode(status) {
			console.log(status)
			const statusMap = {
				'pending': 1,
				'paid': 2,
				'active': 3,
				'completed': 4
			};
			return statusMap[status.toLowerCase()] || 0;
		},
		
		// 根据状态字符串获取显示文本
		getStatusText(status) {
			// 首字母大写其他小写的格式化
			if (!status) return 'Unknown';
			const formattedStatus = status.charAt(0).toUpperCase() + status.slice(1).toLowerCase();
			return formattedStatus;
		},
		
		// 格式化持续时间
		formatDuration(startTime, endTime) {
			if (!startTime || !endTime) return 'N/A';
			
			// 计算时间差并格式化
			const start = new Date(startTime);
			const end = new Date(endTime);
			const diffMs = end - start;
			
			const hours = Math.floor(diffMs / (1000 * 60 * 60));
			const minutes = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60));
			
			let result = '';
			
			// 处理小时部分，使用正确的单复数形式
			if (hours > 0) {
				result += `${hours} ${hours === 1 ? 'hour' : 'hours'}`;
			}
			
			// 只有当分钟数大于0时才添加分钟部分
			if (minutes > 0) {
				// 如果已经有小时部分，添加空格
				if (result) result += ' ';
				result += `${minutes} ${minutes === 1 ? 'minute' : 'minutes'}`;
			}
			
			// 如果时长为0，显示"0 minutes"
			if (result === '') {
				result = '0 minutes';
			}
			
			return result;
		},
		
		// 格式化时间
		formatTime(timestamp) {
			if (!timestamp) return 'N/A';
			
			const date = new Date(timestamp);
			return `${date.getFullYear()}-${this.padZero(date.getMonth() + 1)}-${this.padZero(date.getDate())} ${this.padZero(date.getHours())}:${this.padZero(date.getMinutes())}`;
		},
		
		// 添加去使用订单的导航方法
		goToUseOrder(orderId) {
			uni.navigateTo({
				url: `/pages/order_not_used/order_not_used?orderId=${orderId}`
			});
		},
		
		// 添加去还车的导航方法
		goToReturnBike(orderId) {
			uni.navigateTo({
				url: `/pages/order_in_progress/order_in_progress?orderId=${orderId}`
			});
		},
		
		// 添加去支付订单的导航方法
		goToPayOrder(orderId) {
			// 显示加载提示
			uni.showLoading({
				title: 'loading...'
			});
			
			// 调用获取订单详情API
			orderApi.getOrderDetail(Number(orderId))
				.then(res => {
					if (res.code === 1 && res.data) {
						const orderData = res.data;
						console.log(orderData);
						// 构建订单信息对象
						const orderInfo = {
							orderId: orderData.order_id ? orderData.order_id.toString() : "",
							scooterCode: orderData.scooter_id ? orderData.scooter_id.toString() : "",
							address: orderData.pickup_address || "Unknown",
							cost: orderData.cost !== null && orderData.cost !== undefined ? orderData.cost.toString() : "0",
							discountAmount: orderData.discount_amount !== null && orderData.discount_amount !== undefined ? orderData.discount_amount.toString() : "0",
							startDate: orderData.start_time ? this.formatDatePart(new Date(orderData.start_time)) : "",
							startTime: orderData.start_time ? this.formatTimePart(new Date(orderData.start_time)) : "",
							endDate: orderData.end_time ? this.formatDatePart(new Date(orderData.end_time)) : "",
							endTime: orderData.end_time ? this.formatTimePart(new Date(orderData.end_time)) : "",
							days: orderData.duration ? orderData.duration / 24 : 1
						};
						
						// 编码并跳转到支付页面
						const orderInfoStr = encodeURIComponent(JSON.stringify(orderInfo));
						uni.navigateTo({
							url: `/pages/order/payment?orderInfo=${orderInfoStr}`
						});
					} else {
						uni.showToast({
							title: res.msg || 'Failed to obtain order details',
							icon: 'none'
						});
					}
				})
				.catch(err => {
					console.error('Failed to obtain order details:', err);
					uni.showToast({
						title: 'Network error, please try again later',
						icon: 'none'
					});
				})
				.finally(() => {
					uni.hideLoading();
				});
		}
	},
	// 改为使用API获取数据
	created() {
		this.selectedStatus = this.currentTab;
		this.fetchOrders();
	}
})

export default __sfc__
function GenPagesSettingsOrdersOrdersRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
const _component_van_icon = resolveComponent("van-icon")
const _component_van_empty = resolveComponent("van-empty")
const _component_van_button = resolveComponent("van-button")
const _component_van_card = resolveComponent("van-card")
const _component_van_pull_refresh = resolveComponent("van-pull-refresh")
const _component_van_tab = resolveComponent("van-tab")
const _component_van_tabs = resolveComponent("van-tabs")
const _component_van_date_picker = resolveComponent("van-date-picker")
const _component_van_time_picker = resolveComponent("van-time-picker")
const _component_van_picker_group = resolveComponent("van-picker-group")
const _component_van_popup = resolveComponent("van-popup")

  return createElementVNode(Fragment, null, [
    createElementVNode("view", utsMapOf({ class: "orders-container" }), [
      createElementVNode("view", utsMapOf({ class: "header" }), [
        createElementVNode("view", utsMapOf({
          class: "back-btn",
          onClick: _ctx.navigateToHome
        }), [
          createVNode(_component_van_icon, utsMapOf({
            name: "arrow-left",
            class: "back-icon"
          }))
        ], 8 /* PROPS */, ["onClick"]),
        createElementVNode("text", utsMapOf({ class: "header-title" }), "My Orders")
      ]),
      createVNode(_component_van_tabs, utsMapOf({
        modelValue: _ctx.currentTab,
        "onUpdate:modelValue": $event => {(_ctx.currentTab) = $event},
        sticky: "",
        onClick: _ctx.onTabChange
      }), utsMapOf({
        default: withSlotCtx((): any[] => [
          createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.tabs, (tab, index, __index, _cached): any => {
            return createVNode(_component_van_tab, utsMapOf({
              key: index,
              title: tab,
              name: index
            }), utsMapOf({
              default: withSlotCtx((): any[] => [
                createVNode(_component_van_pull_refresh, utsMapOf({
                  modelValue: _ctx.refreshing,
                  "onUpdate:modelValue": $event => {(_ctx.refreshing) = $event},
                  onRefresh: _ctx.onRefresh
                }), utsMapOf({
                  default: withSlotCtx((): any[] => [
                    createElementVNode("view", utsMapOf({ class: "order-list" }), [
                      _ctx.filteredOrders.length === 0
                        ? createVNode(_component_van_empty, utsMapOf({
                            key: 0,
                            description: "No Orders"
                          }))
                        : createCommentVNode("v-if", true),
                      createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.filteredOrders, (order, index, __index, _cached): any => {
                        return createVNode(_component_van_card, utsMapOf({
                          key: index,
                          price: order.statusCode === 1 ? '' : '£' + order.price,
                          desc: order.orderTime,
                          title: order.type,
                          tag: order.status,
                          currency: "",
                          thumb: "/static/bikelogo/escooter_car2.png",
                          class: "order-card"
                        }), utsMapOf({
                          footer: withSlotCtx((): any[] => [
                            createElementVNode("view", utsMapOf({ class: "card-footer" }), [
                              order.statusCode === 1
                                ? createElementVNode(Fragment, utsMapOf({ key: 0 }), [
                                    createVNode(_component_van_button, utsMapOf({
                                      size: "small",
                                      type: "danger",
                                      plain: "",
                                      onClick: () => {_ctx.showCancelOrderConfirm(order)},
                                      style: normalizeStyle(utsMapOf({"margin-right":"auto"}))
                                    }), utsMapOf({
                                      default: withSlotCtx((): any[] => [" Cancel "]),
                                      _: 2 /* DYNAMIC */
                                    }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["onClick", "style"]),
                                    createVNode(_component_van_button, utsMapOf({
                                      size: "small",
                                      type: "primary",
                                      plain: "",
                                      onClick: () => {_ctx.goToPayOrder(order.id)}
                                    }), utsMapOf({
                                      default: withSlotCtx((): any[] => [" Pay Now "]),
                                      _: 2 /* DYNAMIC */
                                    }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["onClick"])
                                  ], 64 /* STABLE_FRAGMENT */)
                                : order.statusCode === 2
                                  ? createElementVNode(Fragment, utsMapOf({ key: 1 }), [
                                      createVNode(_component_van_button, utsMapOf({
                                        size: "small",
                                        type: "primary",
                                        plain: "",
                                        onClick: () => {_ctx.goToUseOrder(order.id)}
                                      }), utsMapOf({
                                        default: withSlotCtx((): any[] => [" Use Now "]),
                                        _: 2 /* DYNAMIC */
                                      }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["onClick"]),
                                      createVNode(_component_van_button, utsMapOf({
                                        size: "small",
                                        icon: "edit",
                                        plain: "",
                                        onClick: () => {_ctx.showExtendRentalDialog(order)}
                                      }), utsMapOf({
                                        default: withSlotCtx((): any[] => [" Extend Rental "]),
                                        _: 2 /* DYNAMIC */
                                      }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["onClick"])
                                    ], 64 /* STABLE_FRAGMENT */)
                                  : order.statusCode === 3
                                    ? createElementVNode(Fragment, utsMapOf({ key: 2 }), [
                                        createVNode(_component_van_button, utsMapOf({
                                          size: "small",
                                          type: "primary",
                                          plain: "",
                                          onClick: () => {_ctx.goToReturnBike(order.id)}
                                        }), utsMapOf({
                                          default: withSlotCtx((): any[] => [" Return Bike "]),
                                          _: 2 /* DYNAMIC */
                                        }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["onClick"]),
                                        createVNode(_component_van_button, utsMapOf({
                                          size: "small",
                                          plain: "",
                                          onClick: () => {_ctx.showExtendRentalDialog(order)}
                                        }), utsMapOf({
                                          default: withSlotCtx((): any[] => [" Extend Rental "]),
                                          _: 2 /* DYNAMIC */
                                        }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["onClick"])
                                      ], 64 /* STABLE_FRAGMENT */)
                                    : createElementVNode(Fragment, utsMapOf({ key: 3 }), [
                                        createVNode(_component_van_button, utsMapOf({
                                          size: "small",
                                          icon: "delete",
                                          plain: "",
                                          onClick: () => {_ctx.deleteOrder(order.id)},
                                          style: normalizeStyle(utsMapOf({"margin-right":"auto"}))
                                        }), utsMapOf({
                                          default: withSlotCtx((): any[] => [" Delete "]),
                                          _: 2 /* DYNAMIC */
                                        }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["onClick", "style"]),
                                        createVNode(_component_van_button, utsMapOf({
                                          size: "small",
                                          type: "primary",
                                          plain: "",
                                          onClick: () => {_ctx.viewOrderDetail(order.id)}
                                        }), utsMapOf({
                                          default: withSlotCtx((): any[] => [" Order Details "]),
                                          _: 2 /* DYNAMIC */
                                        }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["onClick"])
                                      ], 64 /* STABLE_FRAGMENT */)
                            ])
                          ]),
                          desc: withSlotCtx((): any[] => [
                            createElementVNode("view", null, [
                              createElementVNode("view", utsMapOf({ class: "info-item" }), [
                                createElementVNode("text", utsMapOf({ class: "info-label" }), [
                                  "Rental Duration: ",
                                  createElementVNode("text", utsMapOf({ class: "info-value" }), toDisplayString(order.duration), 1 /* TEXT */)
                                ])
                              ]),
                              createElementVNode("view", utsMapOf({ class: "info-item" }), [
                                createElementVNode("text", utsMapOf({ class: "info-label" }), [
                                  "Order Time: ",
                                  createElementVNode("text", utsMapOf({ class: "info-value" }), toDisplayString(order.orderTime), 1 /* TEXT */)
                                ])
                              ])
                            ])
                          ]),
                          _: 2 /* DYNAMIC */
                        }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["price", "desc", "title", "tag"])
                      }), 128 /* KEYED_FRAGMENT */)
                    ])
                  ]),
                  _: 2 /* DYNAMIC */
                }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["modelValue", "onUpdate:modelValue", "onRefresh"])
              ]),
              _: 2 /* DYNAMIC */
            }), 1032 /* PROPS, DYNAMIC_SLOTS */, ["title", "name"])
          }), 128 /* KEYED_FRAGMENT */)
        ]),
        _: 1 /* STABLE */
      }), 8 /* PROPS */, ["modelValue", "onUpdate:modelValue", "onClick"])
    ]),
    createVNode(_component_van_popup, utsMapOf({
      show: _ctx.showTimePicker,
      "onUpdate:show": $event => {(_ctx.showTimePicker) = $event},
      position: "bottom",
      round: ""
    }), utsMapOf({
      default: withSlotCtx((): any[] => [
        createVNode(_component_van_picker_group, utsMapOf({
          title: "Select Extension Time",
          tabs: ['Select Date', 'Select Time'],
          onConfirm: _ctx.onDateTimePickerConfirm,
          onCancel: _ctx.onDateTimePickerCancel
        }), utsMapOf({
          default: withSlotCtx((): any[] => [
            createVNode(_component_van_date_picker, utsMapOf({
              modelValue: _ctx.dateColumns,
              "onUpdate:modelValue": $event => {(_ctx.dateColumns) = $event},
              "min-date": _ctx.minDate,
              "max-date": _ctx.maxDate
            }), null, 8 /* PROPS */, ["modelValue", "onUpdate:modelValue", "min-date", "max-date"]),
            createVNode(_component_van_time_picker, utsMapOf({
              modelValue: _ctx.timeColumns,
              "onUpdate:modelValue": $event => {(_ctx.timeColumns) = $event}
            }), null, 8 /* PROPS */, ["modelValue", "onUpdate:modelValue"])
          ]),
          _: 1 /* STABLE */
        }), 8 /* PROPS */, ["onConfirm", "onCancel"])
      ]),
      _: 1 /* STABLE */
    }), 8 /* PROPS */, ["show", "onUpdate:show"])
  ], 64 /* STABLE_FRAGMENT */)
}
const GenPagesSettingsOrdersOrdersStyles = [utsMapOf([["orders-container", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["paddingTop", "40rpx"], ["paddingRight", "0rpx"], ["paddingBottom", "0rpx"], ["paddingLeft", "0rpx"], ["backgroundImage", "linear-gradient(to bottom, #e0f0ff, #ffffff)"], ["backgroundColor", "#f5f5f5"], ["boxSizing", "border-box"]]))], ["header", padStyleMapOf(utsMapOf([["backgroundColor", "#ffffff"], ["paddingTop", "40rpx"], ["paddingRight", 0], ["paddingBottom", "40rpx"], ["paddingLeft", 0], ["textAlign", "center"], ["borderRadius", "20rpx"], ["boxShadow", "0 4px 12px rgba(0, 0, 0, 0.05)"], ["position", "relative"], ["marginBottom", "30rpx"], ["marginLeft", "20rpx"], ["marginRight", "20rpx"]]))], ["back-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["top", "25rpx"], ["left", "30rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["zIndex", 10], ["backgroundColor", "#f4f8ff"], ["borderRadius", "25rpx"], ["width", "80rpx"], ["height", "80rpx"]]))], ["back-icon", padStyleMapOf(utsMapOf([["fontSize", "60rpx"], ["color", "#0084ff"]]))], ["header-title", padStyleMapOf(utsMapOf([["fontSize", "46rpx"], ["fontWeight", "bold"], ["color", "#007aff"], ["textAlign", "center"]]))], ["order-list", padStyleMapOf(utsMapOf([["paddingTop", "35rpx"], ["paddingRight", "35rpx"], ["paddingBottom", "35rpx"], ["paddingLeft", "35rpx"]]))], ["order-card", padStyleMapOf(utsMapOf([["marginBottom", "20rpx"], ["backgroundColor", "#ffffff"], ["borderRadius", "12rpx"], ["overflow", "hidden"]]))], ["card-footer", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "flex-end"], ["alignItems", "center"], ["gap", "16rpx"], ["marginTop", "16rpx"], ["flexDirection", "row"], ["flexWrap", "nowrap"]]))], ["info-item", padStyleMapOf(utsMapOf([["marginBottom", "10rpx"], ["display", "flex"], ["alignItems", "flex-start"], ["justifyContent", "flex-start"]]))], ["info-label", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["color", "#666666"], ["textAlign", "left"]]))], ["info-value", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["color", "#999999"], ["marginLeft", "10rpx"], ["textAlign", "left"]]))], ["van-card__price", padStyleMapOf(utsMapOf([["!fontSize", "42rpx"], ["!color", "#ff4444"]]))], ["van-card__title", padStyleMapOf(utsMapOf([["!fontSize", "38rpx"], ["!fontWeight", "bold"]]))], ["van-card__tag", padStyleMapOf(utsMapOf([["!paddingTop", "2rpx"], ["!paddingRight", "8rpx"], ["!paddingBottom", "2rpx"], ["!paddingLeft", "8rpx"], ["!backgroundColor", "#1989fa"], ["!color", "#ffffff"], ["!borderRadius", "4rpx"], ["!fontSize", "26rpx"]]))], ["van-tab--active", padStyleMapOf(utsMapOf([["!color", "#1989fa"], ["!fontWeight", "bold"], ["!fontSize", "32rpx"]]))], ["van-tab", padStyleMapOf(utsMapOf([["!fontSize", "30rpx"]]))], ["van-button", padStyleMapOf(utsMapOf([["!fontSize", "28rpx"]]))]])]
