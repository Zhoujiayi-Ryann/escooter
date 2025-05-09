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
open class GenPagesOrderNotUsedOrderNotUsed : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesOrderNotUsedOrderNotUsed) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!;
            val _ctx = __ins.proxy as GenPagesOrderNotUsedOrderNotUsed;
            val _cache = __ins.renderCache;
            val order = ref(object : UTSJSONObject() {
                var orderId = ""
                var scooterId = ""
                var scooterLocation = ""
                var battery = ""
                var bookTime = ""
                var orderTime = ""
                var duration = ""
                var price: Number = 0
            });
            val location = ref(object : UTSJSONObject() {
                var lng: Number = 0
                var lat: Number = 0
            });
            val markers = ref(utsArrayOf());
            val anchors = utsArrayOf(
                120,
                Math.round(0.4 * window.innerHeight),
                Math.round(0.65 * window.innerHeight)
            );
            val panelHeight = ref(anchors[0]);
            var pageOrderId = null;
            onLoad(fun(query){
                console.log("📦 onLoad received:", query);
                if (query && query.orderId) {
                    pageOrderId = query.orderId;
                    console.log("📦 orderId received from onLoad:", pageOrderId);
                }
                fetchOrderDetail();
            }
            );
            val fetchOrderDetail = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        var rawId = pageOrderId;
                        if (!rawId) {
                            try {
                                val pages = getCurrentPages();
                                val currentPage = pages[pages.length - 1];
                                val options = currentPage.`$page`?.options;
                                if (options && options.orderId) {
                                    rawId = options.orderId;
                                    console.log("📦 Fallback orderId from page options:", rawId);
                                }
                            }
                             catch (err: Throwable) {
                                console.error("📦 Failed to get page parameters:", err);
                            }
                        }
                        if (!rawId) {
                            console.warn("⚠️ orderId missing, using test value");
                            rawId = "1";
                        }
                        val orderId = Number(rawId);
                        if (!orderId || isNaN(orderId)) {
                            uni_showToast(ShowToastOptions(title = "Invalid Order ID", icon = "none", duration = 3000));
                            return@w;
                        }
                        try {
                            val res = await(orderApi.getOrderInfo(orderId));
                            if (res.code === 1 && res.data) {
                                val data = res.data;
                                val hasExtension = !!data.new_end_time;
                                val totalCost = (data.cost ?: 0) + (data.extended_cost ?: 0);
                                val totalDurationHours = (data.duration ?: 0) + (data.extended_duration ?: 0);
                                order.value = object : UTSJSONObject() {
                                    var orderId = data.order_id
                                    var scooterId = data.scooter_id
                                    var scooterLocation = data.pickup_address || "Unknown"
                                    var battery = data.scooter_info?.battery_level ?: "N/A"
                                    var bookTime = formatToCST(data.start_time)
                                    var orderTime = formatToCST(data.created_at)
                                    var duration = "" + totalDurationHours + " hour" + (if (totalDurationHours > 1) {
                                        "s";
                                    } else {
                                        "";
                                    })
                                    var price = totalCost
                                };
                                location.value = object : UTSJSONObject() {
                                    var lat = data.scooter_info?.latitude ?: 0
                                    var lng = data.scooter_info?.longitude ?: 0
                                };
                                loadScooterLocation();
                            } else {
                                uni_showToast(ShowToastOptions(title = res.msg || "Failed to fetch order", icon = "none"));
                            }
                        }
                         catch (err: Throwable) {
                            console.error("🚨 API error:", err);
                            uni_showToast(ShowToastOptions(title = "Network error, try again later", icon = "none"));
                        }
                });
            }
            ;
            fun genFormatToCSTFn(utcStr): String {
                if (!utcStr) {
                    return "N/A";
                }
                val date = Date(utcStr);
                if (isNaN(date.getTime())) {
                    return "Invalid Date";
                }
                return "" + date.getFullYear() + "-" + (date.getMonth() + 1).toString(10).padStart(2, "0") + "-" + date.getDate().toString(10).padStart(2, "0") + " " + date.getHours().toString(10).padStart(2, "0") + ":" + date.getMinutes().toString(10).padStart(2, "0");
            }
            val formatToCST = ::genFormatToCSTFn;
            val loadScooterLocation = fun(){
                if (!location.value.lat || !location.value.lng) {
                    console.warn("📌 Invalid coordinates for marker", location.value);
                    return;
                }
                markers.value = utsArrayOf(
                    object : UTSJSONObject() {
                        var id: Number = 1
                        var latitude = location.value.lat
                        var longitude = location.value.lng
                        var iconPath = "/static/order_details/maker.svg"
                        var width: Number = 32
                        var height: Number = 32
                        var callout = object : UTSJSONObject() {
                            var content = order.value.scooterLocation || "Scooter SC" + order.value.scooterId
                            var display = "ALWAYS"
                            var fontSize: Number = 17
                            var borderRadius: Number = 10
                            var padding: Number = 5
                            var bgColor = "#ffffff"
                            var color = "#057cff"
                            var fontWeight = "bold"
                        }
                    }
                );
            }
            ;
            val goToHelp = fun(){
                uni_navigateTo(NavigateToOptions(url = "/pages/help/help"));
            }
            ;
            val handleBack = fun(){
                if (getCurrentPages().length > 1) {
                    uni_navigateBack(null);
                } else {
                    uni_reLaunch(ReLaunchOptions(url = "/pages/home/home"));
                }
            }
            ;
            val goToUse = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (!order.value.orderId) {
                            uni_showToast(ShowToastOptions(title = "Missing Order ID", icon = "none"));
                            return@w;
                        }
                        try {
                            val res = await(orderApi.activateOrder(order.value.orderId));
                            if (res.code === 1) {
                                uni_showToast(ShowToastOptions(title = "Order activated. You can now use the scooter.", icon = "success"));
                                uni_navigateTo(NavigateToOptions(url = "/pages/order_in_progress/order_in_progress_home?orderId=" + order.value.orderId));
                            } else {
                                uni_showToast(ShowToastOptions(title = res.msg || "Failed to activate order", icon = "none"));
                            }
                        }
                         catch (err: Throwable) {
                            console.error("🚨 Failed to activate order:", err);
                            uni_showToast(ShowToastOptions(title = "Network error, please try again later", icon = "none"));
                        }
                });
            }
            ;
            onMounted(fun(){
                if (!pageOrderId) {
                    fetchOrderDetail();
                }
            }
            );
            return fun(): Any? {
                val _component_van_icon = resolveComponent("van-icon");
                val _component_map = resolveComponent("map");
                val _component_van_button = resolveComponent("van-button");
                val _component_van_floating_panel = resolveComponent("van-floating-panel");
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "back-btn", "onClick" to handleBack), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "back-icon"))
                    )),
                    createElementVNode("view", utsMapOf("class" to "person-btn", "onClick" to goToHelp), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "service-o", "class" to "person-icon"))
                    )),
                    createElementVNode("view", utsMapOf("class" to "map-container"), utsArrayOf(
                        createVNode(_component_map, utsMapOf("id" to "orderMap", "class" to "map", "latitude" to location.value.lat, "longitude" to location.value.lng, "markers" to markers.value, "scale" to 16, "show-location" to "", "enable-3D" to "", "enable-poi" to "", "enable-overlooking" to ""), null, 8, utsArrayOf(
                            "latitude",
                            "longitude",
                            "markers"
                        ))
                    )),
                    createElementVNode("cover-view", utsMapOf("class" to "map-overlay")),
                    createVNode(_component_van_floating_panel, utsMapOf("height" to panelHeight.value, "onUpdate:height" to fun(`$event`){
                        panelHeight.value = `$event`;
                    }
                    , "anchors" to anchors, "class" to "floating-panel"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to "order-details"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "order-info"), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "order-price"), "To be used"),
                                    createElementVNode("view", utsMapOf("class" to "order-tags"), utsArrayOf(
                                        createElementVNode("view", utsMapOf("class" to "badge"), "Order ID: " + toDisplayString(order.value.orderId), 1),
                                        createElementVNode("view", utsMapOf("class" to "status-tag"), "Unused")
                                    ))
                                )),
                                createElementVNode("view", utsMapOf("class" to "info-box"), utsArrayOf(
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Order ID:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(order.value.orderId), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Scooter ID:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), "SC" + toDisplayString(order.value.scooterId), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Scooter Location:"),
                                        createElementVNode("view", utsMapOf("class" to "value address"), toDisplayString(order.value.scooterLocation), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Battery:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(order.value.battery) + "%", 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Start Time:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(order.value.bookTime), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Duration:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(order.value.duration), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Ordered At:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(order.value.orderTime), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "divider")),
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Price:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), "￡" + toDisplayString(order.value.price), 1)
                                    ))
                                )),
                                createVNode(_component_van_button, utsMapOf("class" to "feedback-btn", "type" to "primary", "onClick" to goToUse), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                    return utsArrayOf(
                                        " Go to use vehicle "
                                    );
                                }
                                ), "_" to 1))
                            ))
                        );
                    }
                    ), "_" to 1), 8, utsArrayOf(
                        "height",
                        "onUpdate:height"
                    ))
                ));
            }
            ;
        }
        ;
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(
                styles0
            ), utsArrayOf(
                GenApp.styles
            ))
        };
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return utsMapOf("back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "50rpx", "left" to "60rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "person-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "50rpx", "right" to "60rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "person-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "map-container" to padStyleMapOf(utsMapOf("width" to "100%", "position" to "relative")), "map" to padStyleMapOf(utsMapOf("width" to "100%", "height" to "100%")), "map-overlay" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to 0, "left" to 0, "width" to "100%", "height" to "100%", "background" to "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )", "pointerEvents" to "none", "zIndex" to 2)), "floating-panel" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.8)", "backdropFilter" to "blur(15px)", "borderTopLeftRadius" to 20, "borderTopRightRadius" to 20, "borderBottomRightRadius" to 0, "borderBottomLeftRadius" to 0, "boxShadow" to "0 -5px 20px rgba(0, 0, 0, 0.1)")), "order-details" to padStyleMapOf(utsMapOf("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20)), "order-info" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "flexDirection" to "row", "fontSize" to 16, "fontWeight" to "bold", "paddingBottom" to 10, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "rgba(255,255,255,0.3)")), "order-tags" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "gap" to "10px")), "badge" to padStyleMapOf(utsMapOf("backgroundColor" to "#eeeeee", "paddingTop" to 4, "paddingRight" to 10, "paddingBottom" to 4, "paddingLeft" to 10, "borderRadius" to 12, "fontSize" to 12, "color" to "#444444")), "status-tag" to padStyleMapOf(utsMapOf("backgroundColor" to "#007aff", "color" to "#FFFFFF", "paddingTop" to 4, "paddingRight" to 10, "paddingBottom" to 4, "paddingLeft" to 10, "fontSize" to 12, "borderRadius" to 12)), "order-price" to padStyleMapOf(utsMapOf("fontSize" to 25, "fontWeight" to "bold", "color" to "#0079fe")), "feedback-icon" to padStyleMapOf(utsMapOf("fontSize" to 24, "color" to "#007aff", "cursor" to "pointer")), "info-box" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "rgba(229,231,245,0.5)", "backdropFilter" to "blur(25px)", "paddingTop" to 30, "paddingRight" to 30, "paddingBottom" to 30, "paddingLeft" to 30, "borderRadius" to 15, "marginTop" to 15, "borderWidth" to 1, "borderStyle" to "solid", "borderColor" to "#82b1ff", "boxShadow" to "0 5px 15px rgba(0, 0, 0, 0.1)")), "info-item" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "marginBottom" to 8, "overflow" to "visible", "wordBreak" to "break-word")), "label" to padStyleMapOf(utsMapOf("fontSize" to 17, "fontWeight" to "bold")), "value" to utsMapOf("" to utsMapOf("fontSize" to 14, "color" to "#666666"), ".address" to utsMapOf("fontSize" to "25rpx", "color" to "#666666", "marginTop" to "4rpx", "width" to 160, "textAlign" to "right")), "divider" to padStyleMapOf(utsMapOf("width" to "100%", "height" to 1, "backgroundColor" to "#dddddd", "marginTop" to 25, "marginRight" to 0, "marginBottom" to 25, "marginLeft" to 0)), "feedback-btn" to padStyleMapOf(utsMapOf("marginTop" to 20, "width" to "100%", "borderRadius" to 25, "fontSize" to "34rpx", "height" to "96rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "boxShadow" to "0 4px 10px rgba(0, 122, 255, 0.2)", "transform:active" to "scale(0.98)")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
