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
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack;
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo;
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesOrderPayment : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLoad(fun(options: OnLoadOptions) {
            if (options.orderInfo) {
                this.orderInfo = JSON.parse(decodeURIComponent(options.orderInfo));
                this.orderId = this.orderInfo.orderId;
                this.totalAmount = this.orderInfo.cost;
                console.log("订单信息:", this.orderInfo);
                if (this.orderInfo.start_time && this.orderInfo.end_time) {
                    try {
                        val startDateTime = Date(this.orderInfo.start_time);
                        val endDateTime = Date(this.orderInfo.end_time);
                        this.orderInfo.startDate = startDateTime.toLocaleDateString();
                        this.orderInfo.startTime = startDateTime.toLocaleTimeString(utsArrayOf(), object : UTSJSONObject() {
                            var hour = "2-digit"
                            var minute = "2-digit"
                        });
                        this.orderInfo.endDate = endDateTime.toLocaleDateString();
                        this.orderInfo.endTime = endDateTime.toLocaleTimeString(utsArrayOf(), object : UTSJSONObject() {
                            var hour = "2-digit"
                            var minute = "2-digit"
                        });
                        console.log("格式化后的时间:", let {
                            object : UTSJSONObject() {
                                var startDate = it.orderInfo.startDate
                                var startTime = it.orderInfo.startTime
                                var endDate = it.orderInfo.endDate
                                var endTime = it.orderInfo.endTime
                            }
                        });
                    }
                     catch (e: Throwable) {
                        console.error("Error parsing date and time:", e);
                    }
                }
                this.loadAvailableCoupons();
            }
            this.loadUserCreditCards();
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        val _component_van_cell = resolveComponent("van-cell");
        val _component_van_cell_group = resolveComponent("van-cell-group");
        val _component_van_coupon_cell = resolveComponent("van-coupon-cell");
        val _component_van_button = resolveComponent("van-button");
        val _component_van_password_input = resolveComponent("van-password-input");
        val _component_van_number_keyboard = resolveComponent("van-number-keyboard");
        val _component_van_popup = resolveComponent("van-popup");
        val _component_van_coupon_list = resolveComponent("van-coupon-list");
        val _component_van_loading = resolveComponent("van-loading");
        return createElementVNode("view", utsMapOf("class" to "payment-page"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "back-btn", "onClick" to _ctx.handleBack), utsArrayOf(
                    createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "back-icon"))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("text", utsMapOf("class" to "header-title"), " Payment Confirm")
            )),
            createElementVNode("scroll-view", utsMapOf("scroll-y" to "", "class" to "scroll-content", "scroll-with-animation" to true), utsArrayOf(
                createVNode(_component_van_cell_group, utsMapOf("inset" to "", "class" to "order-card"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createVNode(_component_van_cell, utsMapOf("title" to "Scooter Number", "value" to _ctx.orderInfo.scooterCode), null, 8, utsArrayOf(
                            "value"
                        )),
                        createVNode(_component_van_cell, utsMapOf("center" to "", "title" to "Scooter Style"), utsMapOf("value" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                createElementVNode("image", utsMapOf("class" to "scooter-preview", "src" to "/static/bikelogo/escooter_car2.png", "mode" to "aspectFit"))
                            );
                        }
                        ), "_" to 1)),
                        createVNode(_component_van_cell, utsMapOf("title" to "Order Number", "value" to _ctx.orderId), null, 8, utsArrayOf(
                            "value"
                        )),
                        createVNode(_component_van_cell, utsMapOf("title" to "Rental Period"), utsMapOf("value" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                createElementVNode("scroll-view", utsMapOf("scroll-x" to "true", "class" to "scroll-time-line"), utsArrayOf(
                                    createElementVNode("text", null, toDisplayString(_ctx.orderInfo.startDate) + " " + toDisplayString(_ctx.orderInfo.startTime) + " to " + toDisplayString(_ctx.orderInfo.endDate) + " " + toDisplayString(_ctx.orderInfo.endTime), 1)
                                ))
                            );
                        }
                        ), "_" to 1)),
                        createVNode(_component_van_cell, utsMapOf("title" to "Rental Time", "value" to _ctx.calculateRentalTime()), null, 8, utsArrayOf(
                            "value"
                        )),
                        createVNode(_component_van_cell, utsMapOf("title" to "Pickup address", "value" to _ctx.orderInfo.address), null, 8, utsArrayOf(
                            "value"
                        ))
                    );
                }
                ), "_" to 1)),
                createVNode(_component_van_cell_group, utsMapOf("inset" to "", "class" to "coupon-card"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createVNode(_component_van_coupon_cell, utsMapOf("coupons" to _ctx.coupons, "chosen-coupon" to _ctx.chosenCoupon, "onClick" to fun(){
                            _ctx.showList = true;
                        }
                        , "currency" to "£", "enable-amount" to false), utsMapOf("value" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                if (_ctx.chosenCoupon !== -1) {
                                    createElementVNode("text", utsMapOf("key" to 0), " -£" + toDisplayString(_ctx.coupons[_ctx.chosenCoupon].couponAmount), 1);
                                } else {
                                    createElementVNode("text", utsMapOf("key" to 1), "None");
                                }
                            );
                        }
                        ), "_" to 1), 8, utsArrayOf(
                            "coupons",
                            "chosen-coupon",
                            "onClick"
                        ))
                    );
                }
                ), "_" to 1)),
                createVNode(_component_van_cell_group, utsMapOf("inset" to "", "class" to "amount-card"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createVNode(_component_van_cell, utsMapOf("title" to "Base Amount", "class" to "base-amount"), utsMapOf("value" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "currency base-price"), "£" + toDisplayString(_ctx.orderInfo.cost), 1)
                            );
                        }
                        ), "_" to 1)),
                        if (_ctx.calculateCouponDiscount() > 0) {
                            createVNode(_component_van_cell, utsMapOf("key" to 0, "title" to "Coupon Discount", "class" to "coupon-discount"), utsMapOf("value" to withSlotCtx(fun(): UTSArray<Any> {
                                return utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "currency discount-price"), "-£" + toDisplayString(_ctx.calculateCouponDiscount()), 1)
                                );
                            }), "_" to 1));
                        } else {
                            createCommentVNode("v-if", true);
                        }
                        ,
                        createVNode(_component_van_cell, utsMapOf("title" to "Payment Amount", "class" to "amount"), utsMapOf("value" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "currency"), "£"),
                                createElementVNode("text", utsMapOf("class" to "price"), toDisplayString(_ctx.totalAmount), 1)
                            );
                        }
                        ), "_" to 1))
                    );
                }
                ), "_" to 1)),
                createVNode(_component_van_cell_group, utsMapOf("inset" to "", "title" to "Payment Method", "class" to "payment-method"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createVNode(_component_van_cell, utsMapOf("title" to "Credit Card", "clickable" to "", "onClick" to fun(){
                            _ctx.showCardList = true;
                        }
                        ), utsMapOf("icon" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                createElementVNode("image", utsMapOf("src" to "/static/icons/bankcard.svg", "class" to "payment-icon"))
                            );
                        }
                        ), "value" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                if (isTrue(_ctx.selectedCard)) {
                                    createElementVNode("text", utsMapOf("key" to 0), toDisplayString(_ctx.formatCardNumber(_ctx.selectedCard.card_number)), 1);
                                } else {
                                    createElementVNode("text", utsMapOf("key" to 1), "Select a card");
                                }
                            );
                        }
                        ), "right-icon" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                createVNode(_component_van_icon, utsMapOf("name" to "arrow"))
                            );
                        }
                        ), "_" to 1), 8, utsArrayOf(
                            "onClick"
                        ))
                    );
                }
                ), "_" to 1))
            )),
            createElementVNode("view", utsMapOf("class" to "bottom-button"), utsArrayOf(
                createVNode(_component_van_button, utsMapOf("type" to "primary", "block" to "", "round" to "", "onClick" to _ctx.showPasswordInput), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        " Confirm Payment "
                    );
                }
                ), "_" to 1), 8, utsArrayOf(
                    "onClick"
                ))
            )),
            createVNode(_component_van_popup, utsMapOf("show" to _ctx.showPassword, "onUpdate:show" to fun(`$event`: Boolean){
                _ctx.showPassword = `$event`;
            }
            , "position" to "bottom", "round" to "", "style" to normalizeStyle(utsMapOf("padding" to "24px"))), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                return utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "password-popup"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "popup-title"), "Enter Payment Password"),
                        createElementVNode("text", utsMapOf("class" to "popup-amount"), "£" + toDisplayString(_ctx.totalAmount), 1),
                        createVNode(_component_van_password_input, utsMapOf("value" to _ctx.password, "mask" to true, "focused" to _ctx.passwordFocus, "onFocus" to fun(){
                            _ctx.passwordFocus = true;
                        }
                        , "class" to "password-input"), null, 8, utsArrayOf(
                            "value",
                            "focused",
                            "onFocus"
                        )),
                        createVNode(_component_van_number_keyboard, utsMapOf("show" to _ctx.passwordFocus, "onUpdate:show" to fun(`$event`: Boolean){
                            _ctx.passwordFocus = `$event`;
                        }
                        , "value" to _ctx.password, "onUpdate:value" to fun(`$event`: String){
                            _ctx.password = `$event`;
                        }
                        , "maxlength" to 6, "onBlur" to fun(){
                            _ctx.passwordFocus = false;
                        }
                        , "onClose" to _ctx.closePasswordInput, "onInput" to _ctx.onPasswordInput, "onDelete" to _ctx.onPasswordDelete, "safe-area-inset-bottom" to true), null, 8, utsArrayOf(
                            "show",
                            "onUpdate:show",
                            "value",
                            "onUpdate:value",
                            "onBlur",
                            "onClose",
                            "onInput",
                            "onDelete"
                        ))
                    ))
                );
            }
            ), "_" to 1), 8, utsArrayOf(
                "show",
                "onUpdate:show",
                "style"
            )),
            createVNode(_component_van_popup, utsMapOf("show" to _ctx.showList, "onUpdate:show" to fun(`$event`: Boolean){
                _ctx.showList = `$event`;
            }
            , "round" to "", "position" to "bottom", "style" to normalizeStyle(utsMapOf("height" to "90%", "padding-top" to "4px"))), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                return utsArrayOf(
                    createVNode(_component_van_coupon_list, utsMapOf("coupons" to _ctx.coupons, "chosen-coupon" to _ctx.chosenCoupon, "disabled-coupons" to _ctx.disabledCoupons, "onChange" to _ctx.onChange, "enable-amount" to false, "show-exchange-bar" to false), null, 8, utsArrayOf(
                        "coupons",
                        "chosen-coupon",
                        "disabled-coupons",
                        "onChange"
                    ))
                );
            }
            ), "_" to 1), 8, utsArrayOf(
                "show",
                "onUpdate:show",
                "style"
            )),
            createVNode(_component_van_popup, utsMapOf("show" to _ctx.showCardList, "onUpdate:show" to fun(`$event`: Boolean){
                _ctx.showCardList = `$event`;
            }
            , "round" to "", "position" to "bottom", "style" to normalizeStyle(utsMapOf("height" to "70%", "padding-top" to "20rpx"))), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                return utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "card-list-popup"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "popup-header"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "popup-title"), "Select Card"),
                            createVNode(_component_van_icon, utsMapOf("name" to "cross", "class" to "close-icon", "onClick" to fun(){
                                _ctx.showCardList = false;
                            }
                            ), null, 8, utsArrayOf(
                                "onClick"
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "card-list"), utsArrayOf(
                            if (isTrue(_ctx.loading)) {
                                createElementVNode("view", utsMapOf("key" to 0, "class" to "loading-container"), utsArrayOf(
                                    createVNode(_component_van_loading, utsMapOf("type" to "spinner", "color" to "#007aff")),
                                    createElementVNode("text", utsMapOf("class" to "loading-text"), "Loading...")
                                ));
                            } else {
                                if (_ctx.creditCards.length === 0) {
                                    createElementVNode("view", utsMapOf("key" to 1, "class" to "empty-container"), utsArrayOf(
                                        createVNode(_component_van_icon, utsMapOf("name" to "info-o", "size" to "80rpx", "color" to "#999")),
                                        createElementVNode("text", utsMapOf("class" to "empty-text"), "No cards available")
                                    ));
                                } else {
                                    createElementVNode(Fragment, utsMapOf("key" to 2), RenderHelpers.renderList(_ctx.creditCards, fun(card, index, __index, _cached): Any {
                                        return createElementVNode("view", utsMapOf("key" to card.card_id, "class" to normalizeClass(utsArrayOf(
                                            "card-item",
                                            utsMapOf("card-selected" to (_ctx.selectedCardId === card.card_id))
                                        )), "onClick" to fun(){
                                            _ctx.toggleCardSelection(card);
                                        }
                                        ), utsArrayOf(
                                            createElementVNode("view", utsMapOf("class" to "card-left"), utsArrayOf(
                                                createElementVNode("view", utsMapOf("class" to "card-icon"), utsArrayOf(
                                                    createVNode(_component_van_icon, utsMapOf("name" to "credit-pay", "size" to "50rpx", "color" to "#007aff"))
                                                )),
                                                createElementVNode("view", utsMapOf("class" to "card-info"), utsArrayOf(
                                                    createElementVNode("text", utsMapOf("class" to "card-number"), toDisplayString(_ctx.formatCardNumber(card.card_number)), 1),
                                                    createElementVNode("text", utsMapOf("class" to "card-expiry"), "Expires: " + toDisplayString(card.expiry_date), 1)
                                                ))
                                            )),
                                            if (_ctx.selectedCardId === card.card_id) {
                                                createVNode(_component_van_icon, utsMapOf("key" to 0, "name" to "success", "color" to "#007aff", "size" to "40rpx"));
                                            } else {
                                                createCommentVNode("v-if", true);
                                            }
                                        ), 10, utsArrayOf(
                                            "onClick"
                                        ));
                                    }
                                    ), 128);
                                }
                                ;
                            }
                        )),
                        createElementVNode("view", utsMapOf("class" to "card-action"), utsArrayOf(
                            createVNode(_component_van_button, utsMapOf("type" to "primary", "block" to "", "round" to "", "onClick" to _ctx.confirmCardSelection), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                return utsArrayOf(
                                    " Confirm "
                                );
                            }
                            ), "_" to 1), 8, utsArrayOf(
                                "onClick"
                            ))
                        ))
                    ))
                );
            }
            ), "_" to 1), 8, utsArrayOf(
                "show",
                "onUpdate:show",
                "style"
            ))
        ));
    }
    open var orderId: String by `$data`;
    open var orderInfo: UTSJSONObject by `$data`;
    open var totalAmount: Number by `$data`;
    open var showPassword: Boolean by `$data`;
    open var showKeyboard: Boolean by `$data`;
    open var password: String by `$data`;
    open var passwordFocus: Boolean by `$data`;
    open var showList: Boolean by `$data`;
    open var chosenCoupon: Number by `$data`;
    open var coupons: UTSArray<Any?> by `$data`;
    open var disabledCoupons: UTSArray<Any?> by `$data`;
    open var bankCardSelected: Boolean by `$data`;
    open var showCardList: Boolean by `$data`;
    open var creditCards: UTSArray<Any?> by `$data`;
    open var selectedCard: Any? by `$data`;
    open var selectedCardId: Number by `$data`;
    open var loading: Boolean by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("orderId" to "", "orderInfo" to UTSJSONObject(), "totalAmount" to 0, "showPassword" to false, "showKeyboard" to false, "password" to "", "passwordFocus" to false, "showList" to false, "chosenCoupon" to -1, "coupons" to utsArrayOf(), "disabledCoupons" to utsArrayOf(), "bankCardSelected" to false, "showCardList" to false, "creditCards" to utsArrayOf(), "selectedCard" to null, "selectedCardId" to -1, "loading" to false);
    }
    override fun `$initMethods`() {
        this.handleBack = fun() {
            val pages = getCurrentPages();
            if (pages.length > 1) {
                uni_navigateBack(null);
            } else {
                uni_reLaunch(ReLaunchOptions(url = "/pages/home/home"));
            }
        }
        ;
        this.togglePayment = fun(method) {
            this.paymentMethod = method;
        }
        ;
        this.showPasswordInput = fun() {
            if (!this.selectedCard) {
                uni_showToast(ShowToastOptions(title = "Please select a payment card", icon = "none"));
                return;
            }
            this.showPassword = true;
            this.showKeyboard = true;
            this.passwordFocus = true;
        }
        ;
        this.closePasswordInput = fun() {
            this.showPassword = false;
            this.showKeyboard = false;
            this.password = "";
            this.passwordFocus = false;
        }
        ;
        this.onPasswordInput = fun(key) {
            if (this.password.length >= 6) {
                return;
            }
            this.password += key;
            if (this.password.length === 6) {
                this.handlePayment();
            }
        }
        ;
        this.onPasswordDelete = fun() {
            this.password = this.password.slice(0, -1);
        }
        ;
        this.handlePayment = fun() {
            val orderId = this.orderInfo.orderId || 0;
            if (!orderId) {
                uni_showToast(ShowToastOptions(title = "Incomplete order information", icon = "none"));
                this.closePasswordInput();
                return;
            }
            var couponRequest = null;
            if (this.chosenCoupon !== -1 && this.coupons.length > this.chosenCoupon) {
                couponRequest = let {
                    object : UTSJSONObject() {
                        var couponId = it.coupons[it.chosenCoupon].id
                        var orderId = orderId
                    }
                };
                console.log("使用优惠券支付，couponRequest:", couponRequest);
            }
            val paymentPromise = if (couponRequest) {
                orderApi.payOrder(orderId, couponRequest);
            } else {
                orderApi.payOrderWithoutCoupon(orderId);
            }
            ;
            paymentPromise.then(fun(res){
                console.log("Payment API response:", res);
                if (res.code === 1) {
                    this.closePasswordInput();
                    var successMessage = "Payment successful";
                    if (res.data && res.data.coupon_amount) {
                        successMessage += " (Saved \xa3" + res.data.coupon_amount + ")";
                    }
                    uni_showToast(ShowToastOptions(title = successMessage, icon = "success", duration = 2000));
                    console.log("Payment successful, redirecting...");
                    setTimeout(fun(){
                        uni_reLaunch(ReLaunchOptions(url = "/pages/booking_right/booking_right?orderId=" + orderId));
                    }, 2000);
                } else {
                    uni_showToast(ShowToastOptions(title = res.msg || "Payment failed", icon = "none"));
                    this.closePasswordInput();
                }
            }
            ).`catch`(fun(err){
                uni_showToast(ShowToastOptions(title = "Network request exception", icon = "none"));
                console.error("Payment request exception:", err);
                this.closePasswordInput();
            }
            );
        }
        ;
        this.onChange = fun(index) {
            console.log("选择优惠券，索引:", index, "当前选择:", this.chosenCoupon);
            if (this.chosenCoupon === index) {
                this.chosenCoupon = -1;
                console.log("取消选择优惠券");
            } else {
                this.chosenCoupon = index;
                console.log("选择了新的优惠券, ID:", this.coupons[index]?.id);
            }
            this.showList = false;
            this.`$nextTick`(fun(){
                this.calculateTotal();
            }
            );
        }
        ;
        this.onExchange = fun(code) {
            if (code === "123456") {
                this.coupons.push(let {
                    object : UTSJSONObject() {
                        var id = it.coupons.length + 1
                        var condition = "No minimum"
                        var discount: Number = 15
                        var denominations: Number = 1500
                        var originCondition: Number = 0
                        var name = "New £15 Coupon"
                        var startAt = Date.now() / 1000
                        var endAt = Date.now() / 1000 + 2592000
                        var valueDesc = "£15"
                        var unitDesc = ""
                        var description = "Redeemed"
                    }
                });
                uni_showToast(ShowToastOptions(title = "Redemption Successful", icon = "success"));
            } else {
                uni_showToast(ShowToastOptions(title = "Invalid Code", icon = "error"));
            }
        }
        ;
        this.calculateCouponDiscount = fun(): String {
            var couponDiscount: Number = 0;
            if (this.chosenCoupon !== -1 && this.coupons.length > this.chosenCoupon) {
                val coupon = this.coupons[this.chosenCoupon];
                val baseAmount = parseFloat(this.orderInfo.cost || 0);
                if (baseAmount >= (coupon.originCondition || 0)) {
                    couponDiscount = coupon.couponAmount;
                } else {
                    console.log("订单金额不满足优惠券使用条件:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                        utsArrayOf(
                            "baseAmount",
                            baseAmount
                        ),
                        utsArrayOf(
                            "minAmount",
                            (coupon.originCondition || 0)
                        )
                    ))));
                }
            }
            return couponDiscount.toFixed(2);
        }
        ;
        this.calculateTotal = fun() {
            var basePrice = parseFloat(this.orderInfo.cost || 0);
            val couponDiscount = parseFloat(this.calculateCouponDiscount());
            console.log("计算总价:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "basePrice",
                    basePrice
                ),
                utsArrayOf(
                    "couponDiscount",
                    couponDiscount
                )
            ))));
            if (couponDiscount > 0) {
                basePrice = Math.max(0, basePrice - couponDiscount);
            }
            this.totalAmount = basePrice.toFixed(2);
            console.log("最终计算价格:", this.totalAmount);
        }
        ;
        this.loadUserCreditCards = fun() {
            this.loading = true;
            val userId = userApi.getUserId(1);
            if (!userId) {
                uni_showToast(ShowToastOptions(title = "Please login first", icon = "none"));
                this.loading = false;
                return;
            }
            creditCardApi.getUserCreditCards(userId).then(fun(res){
                if (res.code === 1) {
                    this.creditCards = res.data || utsArrayOf();
                } else {
                    uni_showToast(ShowToastOptions(title = res.msg || "Failed to get cards", icon = "none"));
                }
            }
            ).`catch`(fun(err){
                console.error("Card retrieval exception:", err);
                uni_showToast(ShowToastOptions(title = "Network request exception", icon = "none"));
            }
            ).`finally`(fun(){
                this.loading = false;
            }
            );
        }
        ;
        this.formatCardNumber = fun(cardNumber): String {
            if (!cardNumber) {
                return "";
            }
            return "****  ****  ****  " + cardNumber.slice(-4);
        }
        ;
        this.toggleCardSelection = fun(card) {
            if (this.selectedCardId === card.card_id) {
                this.selectedCardId = -1;
            } else {
                this.selectedCardId = card.card_id;
            }
        }
        ;
        this.confirmCardSelection = fun() {
            if (this.selectedCardId === -1) {
                uni_showToast(ShowToastOptions(title = "Please select a card", icon = "none"));
                return;
            }
            val card = this.creditCards.find(fun(c): Boolean {
                return c.card_id === this.selectedCardId;
            }
            );
            if (card) {
                this.selectedCard = card;
                this.bankCardSelected = true;
                this.showCardList = false;
            }
        }
        ;
        this.goToAddCard = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/user/add_card"));
        }
        ;
        this.loadAvailableCoupons = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend {
                    try {
                        val userId = userApi.getUserId(1);
                        console.log("正在获取用户优惠券，用户ID:", userId);
                        val result = await(couponApi.getAvailableCoupons(userId));
                        console.log("优惠券API返回结果:", result);
                        if (result.code === 1 && result.data) {
                            val orderAmount = parseFloat(this.orderInfo.cost || 0);
                            console.log("当前订单金额:", orderAmount);
                            val availableCoupons = utsArrayOf();
                            val unavailableCoupons = utsArrayOf();
                            val coupons = if (UTSArray.isArray(result.data)) {
                                result.data;
                            } else {
                                (result.data.coupons || utsArrayOf());
                            };
                            coupons.forEach(fun(coupon){
                                console.log("处理优惠券:", coupon);
                                val validFrom = if (coupon.valid_from) {
                                    Date(coupon.valid_from);
                                } else {
                                    null;
                                };
                                val validTo = if (coupon.valid_to) {
                                    Date(coupon.valid_to);
                                } else {
                                    null;
                                };
                                val formattedCoupon: UTSJSONObject = object : UTSJSONObject() {
                                    var id = coupon.coupon_id
                                    var name = coupon.coupon_name || "Discount Coupon"
                                    var condition = "Min. spend \xa3" + (coupon.min_spend || 0)
                                    var description = if (validFrom && validTo) {
                                        "Valid from " + validFrom.toLocaleDateString() + " to " + validTo.toLocaleDateString();
                                    } else {
                                        "Valid dates unavailable";
                                    }
                                    var startAt = if (validFrom) {
                                        validFrom.getTime();
                                    } else {
                                        Date.now();
                                    }
                                    var endAt = if (validTo) {
                                        validTo.getTime();
                                    } else {
                                        Date.now() + 2592000000;
                                    }
                                    var valueDesc = "\xa3" + (coupon.coupon_amount || 0)
                                    var unitDesc = "OFF"
                                    var denominations = (coupon.coupon_amount || 0) * 100
                                    var originCondition = coupon.min_spend || 0
                                    var couponAmount = coupon.coupon_amount || 0
                                };
                                console.log("格式化后的优惠券:", formattedCoupon);
                                val now = Date.now();
                                val isValid = now >= formattedCoupon["startAt"] && now <= formattedCoupon["endAt"];
                                val meetsThreshold = orderAmount >= (coupon.min_spend || 0);
                                if (coupon.status === "able" && isValid && meetsThreshold) {
                                    availableCoupons.push(formattedCoupon);
                                    console.log("优惠券可用:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                                        utsArrayOf(
                                            "id",
                                            coupon.coupon_id
                                        ),
                                        utsArrayOf(
                                            "minSpend",
                                            coupon.min_spend
                                        ),
                                        utsArrayOf(
                                            "orderAmount",
                                            orderAmount
                                        )
                                    ))));
                                } else {
                                    unavailableCoupons.push(formattedCoupon);
                                    console.log("优惠券不可用:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                                        utsArrayOf(
                                            "id",
                                            coupon.coupon_id
                                        ),
                                        utsArrayOf(
                                            "status",
                                            coupon.status
                                        ),
                                        utsArrayOf(
                                            "isValid",
                                            isValid
                                        ),
                                        utsArrayOf(
                                            "meetsThreshold",
                                            meetsThreshold
                                        ),
                                        utsArrayOf(
                                            "minSpend",
                                            coupon.min_spend
                                        ),
                                        utsArrayOf(
                                            "orderAmount",
                                            orderAmount
                                        )
                                    ))));
                                }
                            });
                            this.coupons = availableCoupons;
                            this.disabledCoupons = unavailableCoupons;
                            console.log("最终可用优惠券数量:", this.coupons.length);
                            console.log("最终不可用优惠券数量:", this.disabledCoupons.length);
                        } else {
                            console.warn("Failed to obtain coupon:", result.message);
                            this.coupons = utsArrayOf();
                            this.disabledCoupons = utsArrayOf();
                        }
                    }
                     catch (error: Throwable) {
                        console.error("Failed to obtain coupon:", error);
                        this.coupons = utsArrayOf();
                        this.disabledCoupons = utsArrayOf();
                    }
            });
        }
        ;
        this.calculateRentalTime = fun(): String {
            try {
                console.log("计算租赁时间, 原始数据:", let {
                    object : UTSJSONObject() {
                        var startTime = it.orderInfo.start_time
                        var endTime = it.orderInfo.end_time
                        var startDate = it.orderInfo.startDate
                        var startTimeLocal = it.orderInfo.startTime
                        var endDate = it.orderInfo.endDate
                        var endTimeLocal = it.orderInfo.endTime
                    }
                });
                var startDateTime;
                var endDateTime;
                if (this.orderInfo.start_time && this.orderInfo.end_time) {
                    startDateTime = Date(this.orderInfo.start_time);
                    endDateTime = Date(this.orderInfo.end_time);
                } else if (this.orderInfo.startDate && this.orderInfo.startTime && this.orderInfo.endDate && this.orderInfo.endTime) {
                    startDateTime = Date("" + this.orderInfo.startDate + " " + this.orderInfo.startTime);
                    endDateTime = Date("" + this.orderInfo.endDate + " " + this.orderInfo.endTime);
                } else {
                    console.error("Lack of necessary date and time information");
                    return "N/A";
                }
                if (isNaN(startDateTime.getTime()) || isNaN(endDateTime.getTime())) {
                    console.error("Invalid date format:", startDateTime, endDateTime);
                    return "N/A";
                }
                console.log("计算租赁时间, 解析后的日期:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                    utsArrayOf(
                        "startDateTime",
                        startDateTime
                    ),
                    utsArrayOf(
                        "endDateTime",
                        endDateTime
                    )
                ))));
                val diffMs = endDateTime.getTime() - startDateTime.getTime();
                val hours = Math.round(diffMs / 3600000);
                console.log("计算租赁时间, 时间差:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                    utsArrayOf(
                        "diffMs",
                        diffMs
                    ),
                    utsArrayOf(
                        "hours",
                        hours
                    )
                ))));
                if (hours >= 24) {
                    val days = Math.floor(hours / 24);
                    val remainingHours = hours % 24;
                    if (remainingHours === 0) {
                        return "" + days + " " + (if (days === 1) {
                            "day";
                        } else {
                            "days";
                        });
                    } else {
                        return "" + days + " " + (if (days === 1) {
                            "day";
                        } else {
                            "days";
                        }) + " " + remainingHours + " " + (if (remainingHours === 1) {
                            "hour";
                        } else {
                            "hours";
                        });
                    }
                } else {
                    return "" + hours + " " + (if (hours === 1) {
                        "hour";
                    } else {
                        "hours";
                    }
                    );
                }
            }
             catch (error: Throwable) {
                console.error("Error in calculating lease term:", error);
                return "N/A";
            }
        }
        ;
    }
    open lateinit var handleBack: () -> Unit;
    open lateinit var togglePayment: (method) -> Unit;
    open lateinit var showPasswordInput: () -> Unit;
    open lateinit var closePasswordInput: () -> Unit;
    open lateinit var onPasswordInput: (key) -> Unit;
    open lateinit var onPasswordDelete: () -> Unit;
    open lateinit var handlePayment: () -> Unit;
    open lateinit var onChange: (index) -> Unit;
    open lateinit var onExchange: (code) -> Unit;
    open lateinit var calculateCouponDiscount: () -> String;
    open lateinit var calculateTotal: () -> Unit;
    open lateinit var loadUserCreditCards: () -> Unit;
    open lateinit var formatCardNumber: (cardNumber) -> String;
    open lateinit var toggleCardSelection: (card) -> Unit;
    open lateinit var confirmCardSelection: () -> Unit;
    open lateinit var goToAddCard: () -> Unit;
    open lateinit var loadAvailableCoupons: () -> UTSPromise<Unit>;
    open lateinit var calculateRentalTime: () -> String;
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
                return utsMapOf("payment-page" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to bottom, #f0faff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "boxSizing" to "border-box", "display" to "flex", "flexDirection" to "column", "gap" to "25rpx")), "scroll-content" to padStyleMapOf(utsMapOf("flex" to 1, "overflowY" to "auto", "paddingBottom" to "140rpx")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "25rpx", "left" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "header" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "textAlign" to "center", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center")), "page-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff")), "order-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "!borderRadius" to "24rpx", "!paddingTop" to "20rpx", "!paddingRight" to "20rpx", "!paddingBottom" to "20rpx", "!paddingLeft" to "20rpx", "boxShadow" to "0 6px 14px rgba(0, 0, 0, 0.06)", "marginLeft" to 0, "marginRight" to 0, "marginBottom" to "30rpx", "fontSize" to "32rpx")), "coupon-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "!borderRadius" to "24rpx", "!paddingTop" to "20rpx", "!paddingRight" to "20rpx", "!paddingBottom" to "20rpx", "!paddingLeft" to "20rpx", "boxShadow" to "0 6px 14px rgba(0, 0, 0, 0.06)", "marginLeft" to 0, "marginRight" to 0, "marginBottom" to "30rpx", "fontSize" to "32rpx")), "amount-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "!borderRadius" to "24rpx", "!paddingTop" to "20rpx", "!paddingRight" to "20rpx", "!paddingBottom" to "20rpx", "!paddingLeft" to "20rpx", "boxShadow" to "0 6px 14px rgba(0, 0, 0, 0.06)", "marginLeft" to 0, "marginRight" to 0, "marginBottom" to "30rpx", "fontSize" to "32rpx")), "payment-method" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "!borderRadius" to "24rpx", "!paddingTop" to "20rpx", "!paddingRight" to "20rpx", "!paddingBottom" to "20rpx", "!paddingLeft" to "20rpx", "boxShadow" to "0 6px 14px rgba(0, 0, 0, 0.06)", "marginLeft" to 0, "marginRight" to 0, "marginBottom" to "30rpx", "fontSize" to "32rpx")), "scooter-preview" to padStyleMapOf(utsMapOf("width" to "180rpx", "height" to "100rpx", "borderRadius" to "12rpx", "objectFit" to "contain")), "scroll-time-line" to padStyleMapOf(utsMapOf("whiteSpace" to "nowrap", "overflowX" to "auto", "fontSize" to "28rpx")), "to-text" to padStyleMapOf(utsMapOf("marginTop" to 0, "marginRight" to "8rpx", "marginBottom" to 0, "marginLeft" to "8rpx")), "amount" to padStyleMapOf(utsMapOf("!fontSize" to "36rpx")), "currency" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "marginRight" to "10rpx", "color" to "#007aff")), "price" to padStyleMapOf(utsMapOf("fontSize" to "44rpx", "fontWeight" to "bold", "color" to "#007aff")), "payment-icon" to padStyleMapOf(utsMapOf("width" to "48rpx", "height" to "48rpx", "marginRight" to "24rpx", "objectFit" to "contain")), "bottom-button" to padStyleMapOf(utsMapOf("position" to "fixed", "bottom" to 0, "left" to 0, "right" to 0, "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "boxShadow" to "0 -4px 12px rgba(0, 0, 0, 0.05)", "backdropFilter" to "blur(10px)", "zIndex" to 999)), "van-button--primary" to padStyleMapOf(utsMapOf("!backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "!backgroundColor" to "rgba(0,0,0,0)", "!borderWidth" to "medium", "!borderStyle" to "none", "!borderColor" to "#000000", "!color" to "#ffffff", "!boxShadow" to "0 6px 20px rgba(0, 122, 255, 0.3)", "!transitionDuration" to "0.3s", "!fontSize" to "34rpx", "!height" to "96rpx", "!lineHeight" to "96rpx", "transform:active" to "scale(0.96)")), "password-popup" to padStyleMapOf(utsMapOf("paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "380rpx", "paddingLeft" to "40rpx")), "popup-title" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "textAlign" to "center", "color" to "#333333", "marginBottom" to "16rpx")), "popup-amount" to padStyleMapOf(utsMapOf("fontSize" to "48rpx", "textAlign" to "center", "color" to "#007aff", "fontWeight" to "bold", "marginBottom" to "40rpx")), "password-input" to padStyleMapOf(utsMapOf("marginBottom" to "48rpx")), "card-list-popup" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "height" to "100%", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx")), "popup-header" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "textAlign" to "center", "position" to "relative", "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f5f5f5")), "close-icon" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "10rpx", "right" to "30rpx", "fontSize" to "40rpx", "color" to "#999999", "zIndex" to 10)), "card-list" to padStyleMapOf(utsMapOf("flex" to 1, "overflowY" to "auto", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0)), "loading-container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "height" to "300rpx", "gap" to "20rpx")), "empty-container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "height" to "300rpx", "gap" to "20rpx")), "loading-text" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#999999", "marginTop" to "20rpx")), "empty-text" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#999999", "marginTop" to "20rpx")), "card-item" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "flexDirection" to "row", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "marginBottom" to "30rpx", "backgroundImage" to "linear-gradient(135deg, #f8f9fa, #e9ecef)", "backgroundColor" to "rgba(0,0,0,0)", "borderRadius" to "20rpx", "boxShadow" to "0 6rpx 16rpx rgba(0, 0, 0, 0.08)", "transitionDuration" to "0.3s")), "card-selected" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(135deg, #e6f7ff, #d0e8ff)", "backgroundColor" to "rgba(0,0,0,0)", "borderWidth" to "2rpx", "borderStyle" to "solid", "borderColor" to "#007aff", "transform" to "translateY(-4rpx)", "boxShadow" to "0 10rpx 20rpx rgba(0, 122, 255, 0.15)")), "card-left" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "gap" to "30rpx", "flexDirection" to "row")), "card-icon" to padStyleMapOf(utsMapOf("width" to "100rpx", "height" to "100rpx", "backgroundImage" to "none", "backgroundColor" to "rgba(0,122,255,0.1)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "card-info" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column")), "card-number" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "color" to "#333333")), "card-expiry" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666", "marginTop" to "12rpx")), "card-action" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to 0, "paddingBottom" to "30rpx", "paddingLeft" to 0, "borderTopWidth" to 1, "borderTopStyle" to "solid", "borderTopColor" to "#eeeeee")), "@TRANSITION" to utsMapOf("card-item" to utsMapOf("duration" to "0.3s")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
