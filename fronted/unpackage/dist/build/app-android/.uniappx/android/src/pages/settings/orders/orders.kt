@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER")
package uni.UNIE4E24E1;
import io.dcloud.uniapp.*;
import io.dcloud.uniapp.extapi.*;
import io.dcloud.uniapp.framework.*;
import io.dcloud.uniapp.runtime.*;
import io.dcloud.uniapp.vue.*;
import io.dcloud.uniapp.vue.shared.*;
import io.dcloud.uts.*;
import io.dcloud.uts.Map;
import io.dcloud.uts.Set;
import io.dcloud.uts.UTSAndroid;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.async;
import vant/lib/index.css;
import io.dcloud.uniapp.extapi.`$emit` as uni__emit;
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading;
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack;
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo;
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading;
import io.dcloud.uniapp.extapi.showModal as uni_showModal;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesSettingsOrdersOrders : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onCreated(fun() {
            this.selectedStatus = this.currentTab;
            this.fetchOrders();
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        val _component_van_empty = resolveComponent("van-empty");
        val _component_van_button = resolveComponent("van-button");
        val _component_van_card = resolveComponent("van-card");
        val _component_van_pull_refresh = resolveComponent("van-pull-refresh");
        val _component_van_tab = resolveComponent("van-tab");
        val _component_van_tabs = resolveComponent("van-tabs");
        val _component_van_date_picker = resolveComponent("van-date-picker");
        val _component_van_time_picker = resolveComponent("van-time-picker");
        val _component_van_picker_group = resolveComponent("van-picker-group");
        val _component_van_popup = resolveComponent("van-popup");
        return createElementVNode(Fragment, null, utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "orders-container"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "back-btn", "onClick" to _ctx.navigateToHome), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "back-icon"))
                    ), 8, utsArrayOf(
                        "onClick"
                    )),
                    createElementVNode("text", utsMapOf("class" to "header-title"), "My Orders")
                )),
                createVNode(_component_van_tabs, utsMapOf("modelValue" to _ctx.currentTab, "onUpdate:modelValue" to fun(`$event`: Number){
                    _ctx.currentTab = `$event`;
                }
                , "sticky" to "", "onClick" to _ctx.onTabChange), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.tabs, fun(tab, index, __index, _cached): Any {
                            return createVNode(_component_van_tab, utsMapOf("key" to index, "title" to tab, "name" to index), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                return utsArrayOf(
                                    createVNode(_component_van_pull_refresh, utsMapOf("modelValue" to _ctx.refreshing, "onUpdate:modelValue" to fun(`$event`: Boolean){
                                        _ctx.refreshing = `$event`;
                                    }
                                    , "onRefresh" to _ctx.onRefresh), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                        return utsArrayOf(
                                            createElementVNode("view", utsMapOf("class" to "order-list"), utsArrayOf(
                                                if (_ctx.filteredOrders.length === 0) {
                                                    createVNode(_component_van_empty, utsMapOf("key" to 0, "description" to "No Orders"));
                                                } else {
                                                    createCommentVNode("v-if", true);
                                                }
                                                ,
                                                createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.filteredOrders, fun(order, index, __index, _cached): Any {
                                                    return createVNode(_component_van_card, utsMapOf("key" to index, "price" to if (order.statusCode === 1) {
                                                        "";
                                                    } else {
                                                        "£" + order.price;
                                                    }
                                                    , "desc" to order.orderTime, "title" to order.type, "tag" to order.status, "currency" to "", "thumb" to "/static/bikelogo/escooter_car2.png", "class" to "order-card"), utsMapOf("footer" to withSlotCtx(fun(): UTSArray<Any> {
                                                        return utsArrayOf(
                                                            createElementVNode("view", utsMapOf("class" to "card-footer"), utsArrayOf(
                                                                if (order.statusCode === 1) {
                                                                    createElementVNode(Fragment, utsMapOf("key" to 0), utsArrayOf(
                                                                        createVNode(_component_van_button, utsMapOf("size" to "small", "type" to "danger", "plain" to "", "onClick" to fun(){
                                                                            _ctx.showCancelOrderConfirm(order);
                                                                        }, "style" to normalizeStyle(utsMapOf("margin-right" to "auto"))), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                                            return utsArrayOf(
                                                                                " Cancel "
                                                                            );
                                                                        }), "_" to 2), 1032, utsArrayOf(
                                                                            "onClick",
                                                                            "style"
                                                                        )),
                                                                        createVNode(_component_van_button, utsMapOf("size" to "small", "type" to "primary", "plain" to "", "onClick" to fun(){
                                                                            _ctx.goToPayOrder(order.id);
                                                                        }), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                                            return utsArrayOf(
                                                                                " Pay Now "
                                                                            );
                                                                        }), "_" to 2), 1032, utsArrayOf(
                                                                            "onClick"
                                                                        ))
                                                                    ), 64);
                                                                } else {
                                                                    if (order.statusCode === 2) {
                                                                        createElementVNode(Fragment, utsMapOf("key" to 1), utsArrayOf(
                                                                            createVNode(_component_van_button, utsMapOf("size" to "small", "type" to "primary", "plain" to "", "onClick" to fun(){
                                                                                _ctx.goToUseOrder(order.id);
                                                                            }), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                                                return utsArrayOf(
                                                                                    " Use Now "
                                                                                );
                                                                            }), "_" to 2), 1032, utsArrayOf(
                                                                                "onClick"
                                                                            )),
                                                                            createVNode(_component_van_button, utsMapOf("size" to "small", "icon" to "edit", "plain" to "", "onClick" to fun(){
                                                                                _ctx.showExtendRentalDialog(order);
                                                                            }), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                                                return utsArrayOf(
                                                                                    " Extend Rental "
                                                                                );
                                                                            }), "_" to 2), 1032, utsArrayOf(
                                                                                "onClick"
                                                                            ))
                                                                        ), 64);
                                                                    } else {
                                                                        if (order.statusCode === 3) {
                                                                            createElementVNode(Fragment, utsMapOf("key" to 2), utsArrayOf(
                                                                                createVNode(_component_van_button, utsMapOf("size" to "small", "type" to "primary", "plain" to "", "onClick" to fun(){
                                                                                    _ctx.goToReturnBike(order.id);
                                                                                }), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                                                    return utsArrayOf(
                                                                                        " Return Bike "
                                                                                    );
                                                                                }), "_" to 2), 1032, utsArrayOf(
                                                                                    "onClick"
                                                                                )),
                                                                                createVNode(_component_van_button, utsMapOf("size" to "small", "plain" to "", "onClick" to fun(){
                                                                                    _ctx.showExtendRentalDialog(order);
                                                                                }), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                                                    return utsArrayOf(
                                                                                        " Extend Rental "
                                                                                    );
                                                                                }), "_" to 2), 1032, utsArrayOf(
                                                                                    "onClick"
                                                                                ))
                                                                            ), 64);
                                                                        } else {
                                                                            createElementVNode(Fragment, utsMapOf("key" to 3), utsArrayOf(
                                                                                createVNode(_component_van_button, utsMapOf("size" to "small", "icon" to "delete", "plain" to "", "onClick" to fun(){
                                                                                    _ctx.deleteOrder(order.id);
                                                                                }
                                                                                , "style" to normalizeStyle(utsMapOf("margin-right" to "auto"))), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                                                    return utsArrayOf(
                                                                                        " Delete "
                                                                                    );
                                                                                }
                                                                                ), "_" to 2), 1032, utsArrayOf(
                                                                                    "onClick",
                                                                                    "style"
                                                                                )),
                                                                                createVNode(_component_van_button, utsMapOf("size" to "small", "type" to "primary", "plain" to "", "onClick" to fun(){
                                                                                    _ctx.viewOrderDetail(order.id);
                                                                                }
                                                                                ), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                                                                    return utsArrayOf(
                                                                                        " Order Details "
                                                                                    );
                                                                                }
                                                                                ), "_" to 2), 1032, utsArrayOf(
                                                                                    "onClick"
                                                                                ))
                                                                            ), 64);
                                                                        }
                                                                        ;
                                                                    }
                                                                    ;
                                                                }
                                                            ))
                                                        );
                                                    }
                                                    ), "desc" to withSlotCtx(fun(): UTSArray<Any> {
                                                        return utsArrayOf(
                                                            createElementVNode("view", null, utsArrayOf(
                                                                createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                                                    createElementVNode("text", utsMapOf("class" to "info-label"), utsArrayOf(
                                                                        "Rental Duration: ",
                                                                        createElementVNode("text", utsMapOf("class" to "info-value"), toDisplayString(order.duration), 1)
                                                                    ))
                                                                )),
                                                                createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                                                    createElementVNode("text", utsMapOf("class" to "info-label"), utsArrayOf(
                                                                        "Order Time: ",
                                                                        createElementVNode("text", utsMapOf("class" to "info-value"), toDisplayString(order.orderTime), 1)
                                                                    ))
                                                                ))
                                                            ))
                                                        );
                                                    }
                                                    ), "_" to 2), 1032, utsArrayOf(
                                                        "price",
                                                        "desc",
                                                        "title",
                                                        "tag"
                                                    ));
                                                }
                                                ), 128)
                                            ))
                                        );
                                    }
                                    ), "_" to 2), 1032, utsArrayOf(
                                        "modelValue",
                                        "onUpdate:modelValue",
                                        "onRefresh"
                                    ))
                                );
                            }
                            ), "_" to 2), 1032, utsArrayOf(
                                "title",
                                "name"
                            ));
                        }
                        ), 128)
                    );
                }
                ), "_" to 1), 8, utsArrayOf(
                    "modelValue",
                    "onUpdate:modelValue",
                    "onClick"
                ))
            )),
            createVNode(_component_van_popup, utsMapOf("show" to _ctx.showTimePicker, "onUpdate:show" to fun(`$event`: Boolean){
                _ctx.showTimePicker = `$event`;
            }
            , "position" to "bottom", "round" to ""), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                return utsArrayOf(
                    createVNode(_component_van_picker_group, utsMapOf("title" to "Select Extension Time", "tabs" to utsArrayOf(
                        "Select Date",
                        "Select Time"
                    ), "onConfirm" to _ctx.onDateTimePickerConfirm, "onCancel" to _ctx.onDateTimePickerCancel), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            createVNode(_component_van_date_picker, utsMapOf("modelValue" to _ctx.dateColumns, "onUpdate:modelValue" to fun(`$event`: UTSArray<String>){
                                _ctx.dateColumns = `$event`;
                            }
                            , "min-date" to _ctx.minDate, "max-date" to _ctx.maxDate), null, 8, utsArrayOf(
                                "modelValue",
                                "onUpdate:modelValue",
                                "min-date",
                                "max-date"
                            )),
                            createVNode(_component_van_time_picker, utsMapOf("modelValue" to _ctx.timeColumns, "onUpdate:modelValue" to fun(`$event`: UTSArray<String>){
                                _ctx.timeColumns = `$event`;
                            }
                            ), null, 8, utsArrayOf(
                                "modelValue",
                                "onUpdate:modelValue"
                            ))
                        );
                    }
                    ), "_" to 1), 8, utsArrayOf(
                        "onConfirm",
                        "onCancel"
                    ))
                );
            }
            ), "_" to 1), 8, utsArrayOf(
                "show",
                "onUpdate:show"
            ))
        ), 64);
    }
    open var tabs: UTSArray<String> by `$data`;
    open var currentTab: Number by `$data`;
    open var selectedStatus: Number by `$data`;
    open var orders: UTSArray<Any?> by `$data`;
    open var showCancelDialog: Boolean by `$data`;
    open var currentCancelOrder: Any? by `$data`;
    open var showExtendDialog: Boolean by `$data`;
    open var currentExtendOrder: Any? by `$data`;
    open var extendDuration: String by `$data`;
    open var extendOptions: UTSArray<String> by `$data`;
    open var showTimePicker: Boolean by `$data`;
    open var currentDate: Date by `$data`;
    open var minDate: Date by `$data`;
    open var maxDate: Date by `$data`;
    open var selectedExtendDate: String by `$data`;
    open var dateColumns: UTSArray<String> by `$data`;
    open var timeColumns: UTSArray<String> by `$data`;
    open var refreshing: Boolean by `$data`;
    open var loading: Boolean by `$data`;
    open var userId: Any? by `$data`;
    open var filteredOrders: UTSArray<never> by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("tabs" to utsArrayOf(
            "All",
            "Pending",
            "Paid",
            "Active",
            "Completed"
        ), "currentTab" to 0, "selectedStatus" to 0, "orders" to utsArrayOf(), "showCancelDialog" to false, "currentCancelOrder" to null, "showExtendDialog" to false, "currentExtendOrder" to null, "extendDuration" to "1 day", "extendOptions" to utsArrayOf(
            "1 hour",
            "2 hours",
            "4 hours",
            "1 day"
        ), "showTimePicker" to false, "currentDate" to Date(), "minDate" to Date(), "maxDate" to Date(Date().getFullYear() + 1, Date().getMonth(), Date().getDate()), "selectedExtendDate" to "", "dateColumns" to utsArrayOf(
            "",
            "",
            ""
        ), "timeColumns" to utsArrayOf(
            "",
            ""
        ), "refreshing" to false, "loading" to false, "userId" to userApi.getUserId(1), "filteredOrders" to computed<UTSArray<never>>(fun(): UTSArray<never> {
            if (this.selectedStatus === 0) {
                return this.orders;
            } else {
                return this.orders.filter(fun(order): Boolean {
                    return order.statusCode === this.selectedStatus;
                }
                );
            }
        }
        ));
    }
    override fun `$initMethods`() {
        this.navigateToHome = fun() {
            val pages = getCurrentPages();
            if (pages.length > 1) {
                uni_navigateBack(NavigateBackOptions(delta = 1));
            } else {
                uni_navigateTo(NavigateToOptions(url = "/pages/home/home"));
            }
        }
        ;
        this.onTabChange = fun(index) {
            val tabElement = index.target.closest(".van-tab");
            if (tabElement && tabElement.id) {
                val tabId = tabElement.id;
                val tabIndex = tabId.split("-").pop();
                this.selectedStatus = Number(tabIndex);
                console.log("当前状态码:", this.selectedStatus);
            }
        }
        ;
        this.deleteOrder = fun(orderId) {
            uni_showModal(ShowModalOptions(title = "Tip", content = "Are you sure to delete this order?", confirmText = "Confirm", cancelText = "Cancel", success = fun(res){
                if (res.confirm) {
                    orderApi.softDeleteOrder(Number(orderId)).then(fun(res){
                        if (res.code === 1) {
                            this.orders = this.orders.filter(fun(order): Boolean {
                                return order.id !== orderId;
                            });
                            uni__emit("refreshHomePage", null);
                            uni_showToast(ShowToastOptions(title = "Deleted", icon = "success"));
                        } else {
                            uni_showToast(ShowToastOptions(title = res.msg || "Delete failed", icon = "none"));
                        }
                    }
                    ).`catch`(fun(err){
                        console.error("Network error, please try again later:", err);
                        uni_showToast(ShowToastOptions(title = "Network error, please try again later", icon = "none"));
                    }
                    );
                }
            }
            ));
        }
        ;
        this.viewOrderDetail = fun(orderId) {
            console.log(orderId);
            uni_navigateTo(NavigateToOptions(url = "/pages/order_details/order_details?orderId=" + orderId));
        }
        ;
        this.showCancelOrderConfirm = fun(order) {
            this.currentCancelOrder = order;
            uni_showModal(ShowModalOptions(title = "Confirm Order Cancellation?", content = "Rental Duration: " + order.duration + "\nBicycle ID: " + order.id, cancelText = "Cancel", confirmText = "Confirm", success = fun(res){
                if (res.confirm) {
                    orderApi.deleteOrder(Number(order.id)).then(fun(res){
                        if (res.code === 1) {
                            this.orders = this.orders.filter(fun(o): Boolean {
                                return o.id !== order.id;
                            });
                            uni_showToast(ShowToastOptions(title = "Order Cancelled", icon = "success"));
                        } else {
                            uni_showToast(ShowToastOptions(title = res.msg || "Cancel failed", icon = "none"));
                        }
                    }
                    ).`catch`(fun(err){
                        console.error("Cancel order error:", err);
                        uni_showToast(ShowToastOptions(title = "Network error, please try again later", icon = "none"));
                    }
                    );
                }
            }
            ));
        }
        ;
        this.showExtendRentalDialog = fun(order) {
            this.currentExtendOrder = order;
            this.selectedExtendDate = "";
            orderApi.getAvailableTimeSlots(Number(order.id)).then(fun(res){
                console.log(res);
                if (res.code === 1 && res.data) {
                    this.minDate = Date(res.data.current_end_time);
                    if (res.data.next_start_time) {
                        this.maxDate = Date(res.data.next_start_time);
                    } else if (res.data.max_extended_hours > 0) {
                        val maxDate = Date(res.data.current_end_time);
                        maxDate.setHours(maxDate.getHours() + res.data.max_extended_hours);
                        this.maxDate = maxDate;
                    } else {
                        this.maxDate = Date(Date().getFullYear() + 1, Date().getMonth(), Date().getDate());
                    }
                    this.showDateTimePicker();
                } else {
                    uni_showToast(ShowToastOptions(title = "Failed to get available time slots", icon = "none"));
                }
            }
            ).`catch`(fun(err){
                console.error("获取可用时间段失败:", err);
                uni_showToast(ShowToastOptions(title = "Network error, please try again later", icon = "none"));
            }
            );
        }
        ;
        this.showDateTimePicker = fun() {
            this.showTimePicker = true;
            this.initDateTimeColumns();
        }
        ;
        this.initDateTimeColumns = fun() {
            this.setDateTimeValues(this.minDate);
        }
        ;
        this.setDateTimeValues = fun(date) {
            this.dateColumns = utsArrayOf(
                date.getFullYear().toString(),
                (date.getMonth() + 1).toString().padStart(2, "0"),
                date.getDate().toString().padStart(2, "0")
            );
            this.timeColumns = utsArrayOf(
                date.getHours().toString().padStart(2, "0"),
                date.getMinutes().toString().padStart(2, "0")
            );
        }
        ;
        this.onDateTimePickerConfirm = fun() {
            val year = this.dateColumns[0];
            val month = this.dateColumns[1];
            val day = this.dateColumns[2];
            val hour = this.timeColumns[0];
            val minute = this.timeColumns[1];
            this.selectedExtendDate = "" + year + "-" + month + "-" + day + " " + hour + ":" + minute;
            this.showTimePicker = false;
            if (this.currentExtendOrder) {
                val extendOrderRequest: UTSJSONObject = let {
                    object : UTSJSONObject() {
                        var order_id = Number(it.currentExtendOrder.id)
                        var new_end_time = "" + year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":00"
                    }
                };
                console.log(extendOrderRequest);
                orderApi.extendOrder(extendOrderRequest).then(fun(res){
                    console.log(res);
                    if (res.code === 1 && res.data) {
                        val orderData = res.data;
                        val orderInfo: UTSJSONObject = let {
                            object : UTSJSONObject() {
                                var orderId = if (orderData.order_id) {
                                    orderData.order_id.toString();
                                } else {
                                    "";
                                }
                                var scooterCode = if (orderData.scooter_id) {
                                    orderData.scooter_id.toString();
                                } else {
                                    it.currentExtendOrder.type;
                                }
                                var address = orderData.pickup_address || "未知地点"
                                var cost = if (orderData.extended_cost != null && orderData.extended_cost !== undefined) {
                                    orderData.extended_cost.toString();
                                } else {
                                    "0";
                                }
                                var discountAmount = if (orderData.discount_amount != null && orderData.discount_amount !== undefined) {
                                    orderData.discount_amount.toString();
                                } else {
                                    "0";
                                }
                                var startDate = if (orderData.start_time) {
                                    it.formatDatePart(Date(orderData.start_time));
                                } else {
                                    "";
                                }
                                var startTime = if (orderData.start_time) {
                                    it.formatTimePart(Date(orderData.start_time));
                                } else {
                                    "";
                                }
                                var endDate = if (orderData.end_time) {
                                    it.formatDatePart(Date(orderData.end_time));
                                } else {
                                    "";
                                }
                                var endTime = if (orderData.end_time) {
                                    it.formatTimePart(Date(orderData.end_time));
                                } else {
                                    "";
                                }
                                var days = if (orderData.extended_duration) {
                                    orderData.extended_duration / 24;
                                } else {
                                    1;
                                }
                            }
                        };
                        val orderInfoStr = encodeURIComponent(JSON.stringify(orderInfo));
                        uni_navigateTo(NavigateToOptions(url = "/pages/order/payment?orderInfo=" + orderInfoStr));
                    } else {
                        uni_showToast(ShowToastOptions(title = res.msg || "Failed to extend order", icon = "none"));
                    }
                }
                ).`catch`(fun(err){
                    console.error("延长订单出错:", err);
                    uni_showToast(ShowToastOptions(title = "Network error, please try again later", icon = "none"));
                }
                );
            }
        }
        ;
        this.onDateTimePickerCancel = fun() {
            this.showTimePicker = false;
        }
        ;
        this.formatDate = fun(date): String {
            val year = date.getFullYear();
            val month = this.padZero(date.getMonth() + 1);
            val day = this.padZero(date.getDate());
            val hour = this.padZero(date.getHours());
            val minute = this.padZero(date.getMinutes());
            return "" + year + "-" + month + "-" + day + " " + hour + ":" + minute;
        }
        ;
        this.padZero = fun(num): String {
            return String(num).padStart(2, "0");
        }
        ;
        this.formatDatePart = fun(date): String {
            val year = date.getFullYear();
            val month = this.padZero(date.getMonth() + 1);
            val day = this.padZero(date.getDate());
            return "" + year + "-" + month + "-" + day;
        }
        ;
        this.formatTimePart = fun(date): String {
            val hour = this.padZero(date.getHours());
            val minute = this.padZero(date.getMinutes());
            return "" + hour + ":" + minute;
        }
        ;
        this.onRefresh = fun() {
            console.log("开始刷新");
            this.fetchOrders();
        }
        ;
        this.fetchOrders = fun() {
            this.loading = true;
            orderApi.getUserOrders(this.userId).then(fun(res){
                console.log(res);
                if (res.code === 1 && res.data) {
                    this.orders = res.data.map(fun(order){
                        return let {
                            object : UTSJSONObject() {
                                var id = order.order_id.toString()
                                var type = "Bicycle"
                                var status = it.getStatusText(order.status)
                                var duration = it.formatDuration(order.start_time, order.end_time)
                                var orderTime = it.formatTime(order.start_time)
                                var price = order.cost.toString()
                                var statusCode = it.mapStatusToCode(order.status)
                            }
                        };
                    });
                    console.log(this.orders);
                } else {
                    uni_showToast(ShowToastOptions(title = res.msg || "Failed to get orders", icon = "none"));
                }
            }
            ).`catch`(fun(err){
                console.error("获取订单出错:", err);
                uni_showToast(ShowToastOptions(title = "Network error, please try again later", icon = "none"));
            }
            ).`finally`(fun(){
                this.loading = false;
                this.refreshing = false;
            }
            );
        }
        ;
        this.mapStatusToCode = fun(status): Any {
            console.log(status);
            val statusMap: UTSJSONObject = object : UTSJSONObject() {
                var pending: Number = 1
                var paid: Number = 2
                var active: Number = 3
                var completed: Number = 4
            };
            return statusMap[status.toLowerCase()] || 0;
        }
        ;
        this.getStatusText = fun(status): Any {
            if (!status) {
                return "Unknown";
            }
            val formattedStatus = status.charAt(0).toUpperCase() + status.slice(1).toLowerCase();
            return formattedStatus;
        }
        ;
        this.formatDuration = fun(startTime, endTime): String {
            if (!startTime || !endTime) {
                return "N/A";
            }
            val start = Date(startTime);
            val end = Date(endTime);
            val diffMs = end - start;
            val hours = Math.floor(diffMs / 3600000);
            val minutes = Math.floor((diffMs % 3600000) / 60000);
            var result = "";
            if (hours > 0) {
                result += "" + hours + " " + (if (hours === 1) {
                    "hour";
                } else {
                    "hours";
                }
                );
            }
            if (minutes > 0) {
                if (result) {
                    result += " ";
                }
                result += "" + minutes + " " + (if (minutes === 1) {
                    "minute";
                } else {
                    "minutes";
                }
                );
            }
            if (result === "") {
                result = "0 minutes";
            }
            return result;
        }
        ;
        this.formatTime = fun(timestamp): String {
            if (!timestamp) {
                return "N/A";
            }
            val date = Date(timestamp);
            return "" + date.getFullYear() + "-" + this.padZero(date.getMonth() + 1) + "-" + this.padZero(date.getDate()) + " " + this.padZero(date.getHours()) + ":" + this.padZero(date.getMinutes());
        }
        ;
        this.goToUseOrder = fun(orderId) {
            uni_navigateTo(NavigateToOptions(url = "/pages/order_not_used/order_not_used?orderId=" + orderId));
        }
        ;
        this.goToReturnBike = fun(orderId) {
            uni_navigateTo(NavigateToOptions(url = "/pages/order_in_progress/order_in_progress?orderId=" + orderId));
        }
        ;
        this.goToPayOrder = fun(orderId) {
            uni_showLoading(ShowLoadingOptions(title = "loading..."));
            orderApi.getOrderDetail(Number(orderId)).then(fun(res){
                if (res.code === 1 && res.data) {
                    val orderData = res.data;
                    console.log(orderData);
                    val orderInfo: UTSJSONObject = let {
                        object : UTSJSONObject() {
                            var orderId = if (orderData.order_id) {
                                orderData.order_id.toString();
                            } else {
                                "";
                            }
                            var scooterCode = if (orderData.scooter_id) {
                                orderData.scooter_id.toString();
                            } else {
                                "";
                            }
                            var address = orderData.pickup_address || "Unknown"
                            var cost = if (orderData.cost != null && orderData.cost !== undefined) {
                                orderData.cost.toString();
                            } else {
                                "0";
                            }
                            var discountAmount = if (orderData.discount_amount != null && orderData.discount_amount !== undefined) {
                                orderData.discount_amount.toString();
                            } else {
                                "0";
                            }
                            var startDate = if (orderData.start_time) {
                                it.formatDatePart(Date(orderData.start_time));
                            } else {
                                "";
                            }
                            var startTime = if (orderData.start_time) {
                                it.formatTimePart(Date(orderData.start_time));
                            } else {
                                "";
                            }
                            var endDate = if (orderData.end_time) {
                                it.formatDatePart(Date(orderData.end_time));
                            } else {
                                "";
                            }
                            var endTime = if (orderData.end_time) {
                                it.formatTimePart(Date(orderData.end_time));
                            } else {
                                "";
                            }
                            var days = if (orderData.duration) {
                                orderData.duration / 24;
                            } else {
                                1;
                            }
                        }
                    };
                    val orderInfoStr = encodeURIComponent(JSON.stringify(orderInfo));
                    uni_navigateTo(NavigateToOptions(url = "/pages/order/payment?orderInfo=" + orderInfoStr));
                } else {
                    uni_showToast(ShowToastOptions(title = res.msg || "Failed to obtain order details", icon = "none"));
                }
            }
            ).`catch`(fun(err){
                console.error("Failed to obtain order details:", err);
                uni_showToast(ShowToastOptions(title = "Network error, please try again later", icon = "none"));
            }
            ).`finally`(fun(){
                uni_hideLoading();
            }
            );
        }
        ;
    }
    open lateinit var navigateToHome: () -> Unit;
    open lateinit var onTabChange: (index) -> Unit;
    open lateinit var deleteOrder: (orderId) -> Unit;
    open lateinit var viewOrderDetail: (orderId) -> Unit;
    open lateinit var showCancelOrderConfirm: (order) -> Unit;
    open lateinit var showExtendRentalDialog: (order) -> Unit;
    open lateinit var showDateTimePicker: () -> Unit;
    open lateinit var initDateTimeColumns: () -> Unit;
    open lateinit var setDateTimeValues: (date) -> Unit;
    open lateinit var onDateTimePickerConfirm: () -> Unit;
    open lateinit var onDateTimePickerCancel: () -> Unit;
    open lateinit var formatDate: (date) -> String;
    open lateinit var padZero: (num) -> String;
    open lateinit var formatDatePart: (date) -> String;
    open lateinit var formatTimePart: (date) -> String;
    open lateinit var onRefresh: () -> Unit;
    open lateinit var fetchOrders: () -> Unit;
    open lateinit var mapStatusToCode: (status) -> Any;
    open lateinit var getStatusText: (status) -> Any;
    open lateinit var formatDuration: (startTime, endTime) -> String;
    open lateinit var formatTime: (timestamp) -> String;
    open lateinit var goToUseOrder: (orderId) -> Unit;
    open lateinit var goToReturnBike: (orderId) -> Unit;
    open lateinit var goToPayOrder: (orderId) -> Unit;
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(
                styles0
            ), utsArrayOf(
                GenApp.styles
            ))
        };
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return utsMapOf("orders-container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "paddingTop" to "40rpx", "paddingRight" to "0rpx", "paddingBottom" to "0rpx", "paddingLeft" to "0rpx", "backgroundImage" to "linear-gradient(to bottom, #e0f0ff, #ffffff)", "backgroundColor" to "#f5f5f5", "boxSizing" to "border-box")), "header" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "textAlign" to "center", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "position" to "relative", "marginBottom" to "30rpx", "marginLeft" to "20rpx", "marginRight" to "20rpx")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "25rpx", "left" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "60rpx", "color" to "#0084ff")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "46rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center")), "order-list" to padStyleMapOf(utsMapOf("paddingTop" to "35rpx", "paddingRight" to "35rpx", "paddingBottom" to "35rpx", "paddingLeft" to "35rpx")), "order-card" to padStyleMapOf(utsMapOf("marginBottom" to "20rpx", "backgroundColor" to "#ffffff", "borderRadius" to "12rpx", "overflow" to "hidden")), "card-footer" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "flex-end", "alignItems" to "center", "gap" to "16rpx", "marginTop" to "16rpx", "flexDirection" to "row", "flexWrap" to "nowrap")), "info-item" to padStyleMapOf(utsMapOf("marginBottom" to "10rpx", "display" to "flex", "alignItems" to "flex-start", "justifyContent" to "flex-start")), "info-label" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666", "textAlign" to "left")), "info-value" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#999999", "marginLeft" to "10rpx", "textAlign" to "left")), "van-card__price" to padStyleMapOf(utsMapOf("!fontSize" to "42rpx", "!color" to "#ff4444")), "van-card__title" to padStyleMapOf(utsMapOf("!fontSize" to "38rpx", "!fontWeight" to "bold")), "van-card__tag" to padStyleMapOf(utsMapOf("!paddingTop" to "2rpx", "!paddingRight" to "8rpx", "!paddingBottom" to "2rpx", "!paddingLeft" to "8rpx", "!backgroundColor" to "#1989fa", "!color" to "#ffffff", "!borderRadius" to "4rpx", "!fontSize" to "26rpx")), "van-tab--active" to padStyleMapOf(utsMapOf("!color" to "#1989fa", "!fontWeight" to "bold", "!fontSize" to "32rpx")), "van-tab" to padStyleMapOf(utsMapOf("!fontSize" to "30rpx")), "van-button" to padStyleMapOf(utsMapOf("!fontSize" to "28rpx")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
