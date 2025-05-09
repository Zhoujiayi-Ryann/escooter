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
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesOrderConfirm : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLoad(fun(options: OnLoadOptions) {
            console.log("接收到的参数：", options);
            if (options.scooterId) {
                this.scooterId = Number(options.scooterId);
                this.batteryLevel = Number(options.batteryLevel) || 0;
                this.price = Number(options.price) || 0;
                this.startDate = decodeURIComponent(options.startDate || "");
                this.startTime = decodeURIComponent(options.startTime || "");
                this.endDate = decodeURIComponent(options.endDate || "");
                this.endTime = decodeURIComponent(options.endTime || "");
                this.latitude = Number(options.latitude) || 0;
                this.longitude = Number(options.longitude) || 0;
                this.scooterCode = "SC" + this.scooterId;
                this.otherInfo = "Electric Scooter";
                this.rentalPeriod = "" + this.startDate + " " + this.startTime + " to " + this.endDate + " " + this.endTime;
                if (this.latitude && this.longitude) {
                    this.getLocationAddress();
                }
            } else {
                this.scooterCode = "SC12345";
                this.otherInfo = "Electric Scooter Model XYZ, Range 100km";
                this.rentalPeriod = "Not specified";
            }
            if (options.userId) {
                this.userId = Number(options.userId);
                console.log("User ID from URL:", this.userId);
            } else {
                this.userId = userApi.getUserId(1);
                console.log("User ID from userApi:", this.userId);
            }
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        val _component_van_checkbox = resolveComponent("van-checkbox");
        val _component_br = resolveComponent("br");
        val _component_van_popup = resolveComponent("van-popup");
        return createElementVNode("view", utsMapOf("class" to "order-confirm-page"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "back-btn", "onClick" to _ctx.navitohome), utsArrayOf(
                    createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "back-icon"))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("text", utsMapOf("class" to "header-title"), "Order Details")
            )),
            createElementVNode("view", utsMapOf("class" to "info-card"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "bike-image-container"), utsArrayOf(
                    createElementVNode("image", utsMapOf("class" to "scooter-image", "src" to "/static/bikelogo/escooter_car2.png", "mode" to "aspectFit"))
                ))
            )),
            createElementVNode("view", utsMapOf("class" to "info-card"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                    createElementVNode("text", utsMapOf("class" to "info-value"), utsArrayOf(
                        "Scooter Number: ",
                        createElementVNode("text", utsMapOf("class" to "info-label"), toDisplayString(_ctx.scooterCode), 1)
                    ))
                )),
                createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                    createElementVNode("text", utsMapOf("class" to "info-value"), utsArrayOf(
                        "Other Information: ",
                        createElementVNode("text", utsMapOf("class" to "info-label"), toDisplayString(_ctx.otherInfo), 1)
                    ))
                )),
                createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                    createElementVNode("text", utsMapOf("class" to "info-value"), utsArrayOf(
                        "Battery Level: ",
                        createElementVNode("text", utsMapOf("class" to "info-label"), toDisplayString(_ctx.batteryLevel) + "%", 1)
                    ))
                )),
                createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                    createElementVNode("text", utsMapOf("class" to "info-value"), utsArrayOf(
                        "Price: ",
                        createElementVNode("text", utsMapOf("class" to "info-label"), "£" + toDisplayString(_ctx.price) + "/hour", 1)
                    ))
                ))
            )),
            createElementVNode("view", utsMapOf("class" to "info-item rental-period-item"), utsArrayOf(
                createElementVNode("text", utsMapOf("class" to "info-label-title"), "Rental Period:"),
                createElementVNode("view", utsMapOf("class" to "rental-period-value"), toDisplayString(_ctx.rentalPeriod), 1)
            )),
            createElementVNode("view", utsMapOf("class" to "address-section"), utsArrayOf(
                createElementVNode("text", utsMapOf("class" to "section-label"), "Address:"),
                createElementVNode("text", utsMapOf("class" to "address-text"), toDisplayString(_ctx.address || "Loading location..."), 1)
            )),
            if (isTrue(_ctx.addressError)) {
                createElementVNode("view", utsMapOf("key" to 0, "class" to "address-error"), utsArrayOf(
                    createElementVNode("text", utsMapOf("class" to "error-text"), toDisplayString(_ctx.addressError), 1)
                ));
            } else {
                createCommentVNode("v-if", true);
            }
            ,
            createElementVNode("view", utsMapOf("class" to "agreement-section"), utsArrayOf(
                createVNode(_component_van_checkbox, utsMapOf("modelValue" to _ctx.agreedToTerms, "onUpdate:modelValue" to fun(`$event`: Boolean){
                    _ctx.agreedToTerms = `$event`;
                }
                , "shape" to "square", "icon-size" to "18px"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "agreement-text"), "I acknowledge and agree to the"),
                        createElementVNode("view", utsMapOf("class" to "agreement-link-wrapper"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "agreement-link", "onClick" to _ctx.showAgreement), "User Agreement", 8, utsArrayOf(
                                "onClick"
                            ))
                        ))
                    );
                }
                ), "_" to 1), 8, utsArrayOf(
                    "modelValue",
                    "onUpdate:modelValue"
                ))
            )),
            createVNode(_component_van_popup, utsMapOf("show" to _ctx.showAgreementPopup, "onUpdate:show" to fun(`$event`: Boolean){
                _ctx.showAgreementPopup = `$event`;
            }
            , "position" to "bottom", "round" to "", "style" to normalizeStyle(utsMapOf("height" to "70%"))), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                return utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "agreement-popup"), utsArrayOf(
                        createElementVNode("scroll-view", utsMapOf("class" to "agreement-content", "scroll-y" to ""), utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to "agreement-title"), "User Agreement"),
                            createElementVNode("view", utsMapOf("class" to "agreement-section"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "section-title"), "1. General Terms"),
                                createElementVNode("view", utsMapOf("class" to "section-text"), " By using our e-scooter rental service, you agree to comply with all applicable laws and regulations. ")
                            )),
                            createElementVNode("view", utsMapOf("class" to "agreement-section"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "section-title"), "2. Safety Requirements"),
                                createElementVNode("view", utsMapOf("class" to "section-text"), utsArrayOf(
                                    " • You must be at least 16 years old to use our service",
                                    createVNode(_component_br),
                                    " • Wear a helmet while riding",
                                    createVNode(_component_br),
                                    " • Follow all traffic rules and regulations "
                                ))
                            )),
                            createElementVNode("view", utsMapOf("class" to "agreement-section"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "section-title"), "3. Rental Rules"),
                                createElementVNode("view", utsMapOf("class" to "section-text"), utsArrayOf(
                                    " • Return the e-scooter in the same condition as received",
                                    createVNode(_component_br),
                                    " • Report any damages or issues immediately",
                                    createVNode(_component_br),
                                    " • If you return earlier than the reserved time, no refund will be provided for the remaining rental period "
                                ))
                            )),
                            createElementVNode("view", utsMapOf("class" to "agreement-section"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "section-title"), "4. Payment Terms"),
                                createElementVNode("view", utsMapOf("class" to "section-text"), utsArrayOf(
                                    " • All charges will be processed through your registered credit card",
                                    createVNode(_component_br),
                                    " • Late returns will incur additional charges "
                                ))
                            )),
                            createElementVNode("view", utsMapOf("class" to "agreement-section"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "section-title"), "5. Liability"),
                                createElementVNode("view", utsMapOf("class" to "section-text"), utsArrayOf(
                                    " • Users are responsible for any damages caused to the scooter",
                                    createVNode(_component_br),
                                    " • We are not liable for any personal injuries or property damage "
                                ))
                            ))
                        ))
                    ))
                );
            }
            ), "_" to 1), 8, utsArrayOf(
                "show",
                "onUpdate:show",
                "style"
            )),
            createElementVNode("view", utsMapOf("class" to "bottom-button"), utsArrayOf(
                createElementVNode("button", utsMapOf("class" to normalizeClass(utsArrayOf(
                    "rent-button",
                    utsMapOf("rent-button-disabled" to !_ctx.agreedToTerms)
                )), "onClick" to _ctx.goToRent), "Go to Rent", 10, utsArrayOf(
                    "onClick"
                ))
            ))
        ));
    }
    open var scooterCode: String by `$data`;
    open var scooterId: Number by `$data`;
    open var otherInfo: String by `$data`;
    open var batteryLevel: Number by `$data`;
    open var price: Number by `$data`;
    open var startDate: String by `$data`;
    open var startTime: String by `$data`;
    open var endDate: String by `$data`;
    open var endTime: String by `$data`;
    open var rentalPeriod: String by `$data`;
    open var address: String by `$data`;
    open var addressError: String by `$data`;
    open var isLoading: Boolean by `$data`;
    open var apiError: String by `$data`;
    open var userId: Number by `$data`;
    open var latitude: Number by `$data`;
    open var longitude: Number by `$data`;
    open var agreedToTerms: Boolean by `$data`;
    open var showAgreementPopup: Boolean by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("scooterCode" to "", "scooterId" to 0, "otherInfo" to "", "batteryLevel" to 0, "price" to 0, "startDate" to "", "startTime" to "", "endDate" to "", "endTime" to "", "rentalPeriod" to "", "address" to "", "addressError" to "", "isLoading" to false, "apiError" to "", "userId" to 1, "latitude" to 0, "longitude" to 0, "agreedToTerms" to false, "showAgreementPopup" to false);
    }
    override fun `$initMethods`() {
        this.navitohome = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/home/home"));
        }
        ;
        this.validateAddress = fun(): Boolean {
            this.addressError = "";
            return true;
        }
        ;
        this.getScooterInfo = fun(scooterId) {
            this.isLoading = true;
            scooterApi.getScooterById(scooterId).then(fun(res){
                if (res.code === 1) {
                    val scooter = res.data;
                    this.scooterCode = "SC" + scooter.scooter_id;
                    this.otherInfo = "Electric Scooter, Battery: " + scooter.battery_level + "%, Price: \$" + scooter.price + "/hour";
                    this.batteryLevel = scooter.battery_level;
                    this.price = scooter.price;
                } else {
                    this.apiError = res.msg || "Failed to get scooter information";
                    uni_showToast(ShowToastOptions(title = this.apiError, icon = "none"));
                }
            }
            ).`catch`(fun(err){
                this.apiError = "Network request exception";
                uni_showToast(ShowToastOptions(title = this.apiError, icon = "none"));
                console.error("Exception when getting scooter information:", err);
            }
            ).`finally`(fun(){
                this.isLoading = false;
            }
            );
        }
        ;
        this.goToRent = fun() {
            if (!this.agreedToTerms) {
                uni_showToast(ShowToastOptions(title = "Please agree to the User Agreement", icon = "none", duration = 2000));
                return;
            }
            this.createOrder();
        }
        ;
        this.createOrder = fun() {
            this.isLoading = true;
            val startDateTime = Date("" + this.startDate + " " + this.startTime);
            val endDateTime = Date("" + this.endDate + " " + this.endTime);
            val formatDateTime = fun(date): String {
                val year = date.getFullYear();
                val month = String(date.getMonth() + 1).padStart(2, "0");
                val day = String(date.getDate()).padStart(2, "0");
                val hours = String(date.getHours()).padStart(2, "0");
                val minutes = String(date.getMinutes()).padStart(2, "0");
                val seconds = String(date.getSeconds()).padStart(2, "0");
                return "" + year + "-" + month + "-" + day + "T" + hours + ":" + minutes + ":" + seconds;
            }
            ;
            val orderData: UTSJSONObject = let {
                object : UTSJSONObject() {
                    var user_id = it.userId
                    var scooter_id = it.scooterId
                    var pickup_address = it.address
                    var start_time = formatDateTime(startDateTime)
                    var end_time = formatDateTime(endDateTime)
                }
            };
            console.log("Creating order with data:", orderData);
            orderApi.createOrder(orderData).then(fun(res){
                if (res.code === 1) {
                    val orderId = res.data.order_id;
                    console.log("Order created successfully:", orderId);
                    orderApi.getOrderDetail(orderId).then(fun(detailRes){
                        if (detailRes.code === 1) {
                            val orderDetail = detailRes.data;
                            val orderInfo: UTSJSONObject = let {
                                object : UTSJSONObject() {
                                    var orderId = orderDetail.order_id
                                    var scooterCode = "SC" + it.scooterId
                                    var address = orderDetail.pickup_address
                                    var cost = orderDetail.cost
                                    var startDate = it.startDate
                                    var startTime = it.startTime
                                    var endDate = it.endDate
                                    var endTime = it.endTime
                                    var duration = orderDetail.duration || 0
                                }
                            };
                            uni_navigateTo(NavigateToOptions(url = "/pages/order/payment?orderInfo=" + encodeURIComponent(JSON.stringify(orderInfo))));
                        } else {
                            uni_showToast(ShowToastOptions(title = detailRes.msg || "Get order detail failed", icon = "none"));
                        }
                    }).`catch`(fun(err){
                        uni_showToast(ShowToastOptions(title = "Get order detail failed", icon = "none"));
                        console.error("Get order detail exception:", err);
                    });
                } else {
                    uni_showToast(ShowToastOptions(title = res.msg || "Failed to create order", icon = "none"));
                }
            }
            ).`catch`(fun(err){
                uni_showToast(ShowToastOptions(title = "Network request exception", icon = "none"));
                console.error("Order creation exception:", err);
            }
            ).`finally`(fun(){
                this.isLoading = false;
            }
            );
        }
        ;
        this.getLocationAddress = fun() {
            this.address = "" + this.latitude.toFixed(6) + ", " + this.longitude.toFixed(6);
        }
        ;
        this.showAgreement = fun() {
            this.showAgreementPopup = true;
        }
        ;
    }
    open lateinit var navitohome: () -> Unit;
    open lateinit var validateAddress: () -> Boolean;
    open lateinit var getScooterInfo: (scooterId) -> Unit;
    open lateinit var goToRent: () -> Unit;
    open lateinit var createOrder: () -> Unit;
    open lateinit var getLocationAddress: () -> Unit;
    open lateinit var showAgreement: () -> Unit;
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
                return utsMapOf("order-confirm-page" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to bottom, #f0faff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)", "display" to "flex", "flexDirection" to "column", "gap" to "20rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "boxSizing" to "border-box")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "25rpx", "left" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "header" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "textAlign" to "center", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "marginTop" to "50rpx")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center", "marginBottom" to "20rpx")), "scooter-image" to padStyleMapOf(utsMapOf("width" to "100%", "maxHeight" to "300rpx", "objectFit" to "contain", "marginBottom" to "auto", "borderRadius" to "20rpx", "marginTop" to "auto", "marginRight" to "auto", "marginLeft" to "auto")), "info-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)")), "info-item" to padStyleMapOf(utsMapOf("marginBottom" to "20rpx", "display" to "flex", "justifyContent" to "space-between")), "info-label" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#333333")), "info-value" to padStyleMapOf(utsMapOf("fontSize" to "34rpx", "fontWeight" to "bold", "color" to "#555555")), "address-section" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderRadius" to "20rpx", "display" to "flex", "flexDirection" to "row", "alignItems" to "center", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)")), "section-label" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "color" to "#333333", "marginRight" to "20rpx", "fontWeight" to "bold")), "address-input" to padStyleMapOf(utsMapOf("flex" to 1, "fontSize" to "30rpx", "borderWidth" to "medium", "borderStyle" to "none", "borderColor" to "#000000", "outline" to "none", "color" to "#333333", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0)")), "address-error" to padStyleMapOf(utsMapOf("marginTop" to "10rpx", "marginRight" to "20rpx", "marginBottom" to 0, "marginLeft" to "20rpx", "paddingTop" to 0, "paddingRight" to "30rpx", "paddingBottom" to 0, "paddingLeft" to "30rpx")), "error-text" to padStyleMapOf(utsMapOf("color" to "#ff4d4f", "fontSize" to "26rpx")), "agreement-section" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to "20rpx", "paddingRight" to "30rpx", "paddingBottom" to "20rpx", "paddingLeft" to "30rpx", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "marginTop" to "20rpx", "marginRight" to 0, "marginBottom" to "36rpx", "marginLeft" to 0)), "agreement-text" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666", "marginLeft" to "20rpx")), "agreement-link" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#007aff", "textDecoration" to "underline", "marginLeft" to "20rpx")), "agreement-popup" to padStyleMapOf(utsMapOf("height" to "100%", "display" to "flex", "flexDirection" to "column")), "agreement-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center", "marginBottom" to "40rpx")), "agreement-content" to padStyleMapOf(utsMapOf("flex" to 1, "paddingTop" to "24rpx", "paddingRight" to "32rpx", "paddingBottom" to "24rpx", "paddingLeft" to "32rpx", "backgroundColor" to "#ffffff", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)")), "agreement-text-content" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666", "lineHeight" to 1.6)), "bottom-button" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "marginRight" to 0, "marginBottom" to "40rpx", "marginLeft" to 0, "paddingTop" to 0, "paddingRight" to "30rpx", "paddingBottom" to 0, "paddingLeft" to "30rpx")), "rent-button" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right, #007aff, #00c3ff)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#ffffff", "fontSize" to "34rpx", "height" to "96rpx", "lineHeight" to "96rpx", "borderRadius" to "48rpx", "fontWeight" to "bold", "boxShadow" to "0 6px 14px rgba(0, 122, 255, 0.2)", "textAlign" to "center")), "rent-button-disabled" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right, #cccccc, #dddddd)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "none", "opacity" to 0.7, "!pointerEvents" to "auto")), "section-title" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#007aff", "marginBottom" to "16rpx")), "section-text" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#444444", "lineHeight" to 1.8)), "rental-period-item" to padStyleMapOf(utsMapOf("flexDirection" to "column", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "backgroundImage" to "none", "backgroundColor" to "#f7faff", "borderWidth" to "2rpx", "borderStyle" to "dashed", "borderColor" to "#007aff", "borderRadius" to "16rpx", "marginBottom" to "30rpx", "boxShadow" to "0 2px 8px rgba(0, 122, 255, 0.08)")), "info-label-title" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "fontWeight" to "bold", "color" to "#007aff", "marginBottom" to "10rpx")), "rental-period-value" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#444444", "lineHeight" to 1.8, "textAlign" to "center", "alignItems" to "center")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
