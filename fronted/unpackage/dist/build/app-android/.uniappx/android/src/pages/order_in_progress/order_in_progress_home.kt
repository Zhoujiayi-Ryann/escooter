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
import io.dcloud.uniapp.framework.onLoad as onLoad1;
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesOrderInProgressOrderInProgressHome : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesOrderInProgressOrderInProgressHome) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!;
            val _ctx = __ins.proxy as GenPagesOrderInProgressOrderInProgressHome;
            val _cache = __ins.renderCache;
            val order = ref(object : UTSJSONObject() {
                var orderId = ""
                var scooterId = ""
                var scooterLocation = ""
                var battery = ""
                var duration = ""
                var usedDuration = ""
                var cost: Number = 0
            });
            val location = ref(object : UTSJSONObject() {
                var lat: Number = 0
                var lng: Number = 0
            });
            val markers = ref(utsArrayOf());
            val anchors = utsArrayOf(
                120,
                Math.round(0.45 * window.innerHeight),
                Math.round(0.6 * window.innerHeight)
            );
            val panelHeight = ref(anchors[0]);
            fun genFormatDurationFn(startStr): String {
                val start = Date(startStr);
                val now = Date();
                if (isNaN(start.getTime())) {
                    return "Unknown";
                }
                val diffMs = now.getTime() - start.getTime();
                val totalMinutes = Math.floor(diffMs / 60000);
                val hours = Math.floor(totalMinutes / 60);
                val minutes = totalMinutes % 60;
                return "" + hours + "h " + minutes + "m";
            }
            val formatDuration = ::genFormatDurationFn;
            onLoad1(fun(options){
                val rawId = options?.orderId;
                val orderId = Number(rawId);
                if (!orderId || isNaN(orderId)) {
                    uni_showToast(ShowToastOptions(title = "Invalid order ID", icon = "none"));
                    return;
                }
                fetchOrderDetail(orderId);
            }
            );
            val fetchOrderDetail = fun(orderId): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        try {
                            val res = await(orderApi.getOrderInfo(orderId));
                            if (res.code === 1 && res.data) {
                                val data = res.data;
                                val lat = data.scooter_info?.latitude;
                                val lng = data.scooter_info?.longitude;
                                if (!lat || !lng) {
                                    uni_showToast(ShowToastOptions(title = "Invalid scooter location", icon = "none"));
                                    return@w;
                                }
                                val baseCost = data.cost ?: 0;
                                val extendedCost = data.extended_cost ?: 0;
                                val totalCost = baseCost + extendedCost;
                                val baseDuration = data.duration ?: 0;
                                val extendedDuration = data.extended_duration ?: 0;
                                val totalDuration = baseDuration + extendedDuration;
                                order.value = object : UTSJSONObject() {
                                    var orderId = data.order_id
                                    var scooterId = data.scooter_id
                                    var scooterLocation = data.pickup_address || "Unknown"
                                    var battery = data.scooter_info?.battery_level ?: "N/A"
                                    var duration = "" + totalDuration + " hour" + (if (totalDuration > 1) {
                                        "s";
                                    } else {
                                        "";
                                    })
                                    var usedDuration = formatDuration(data.start_time)
                                    var cost = totalCost
                                };
                                location.value = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                                    utsArrayOf(
                                        "lat",
                                        lat
                                    ),
                                    utsArrayOf(
                                        "lng",
                                        lng
                                    )
                                )));
                                loadScooterLocation();
                            } else {
                                uni_showToast(ShowToastOptions(title = res.msg || "Failed to fetch order details", icon = "none"));
                            }
                        }
                         catch (err: Throwable) {
                            console.error("🚨 Network error:", err);
                            uni_showToast(ShowToastOptions(title = "Network error. Please try again later.", icon = "none"));
                        }
                });
            }
            ;
            val loadScooterLocation = fun(){
                if (!location.value.lat || !location.value.lng) {
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
                            var fontSize: Number = 15
                            var borderRadius: Number = 5
                            var padding: Number = 5
                            var bgColor = "#ffffff"
                            var color = "#007aff"
                        }
                    }
                );
            }
            ;
            val handleBack = fun(){
                uni_reLaunch(ReLaunchOptions(url = "/pages/home/home"));
            }
            ;
            val goToHelp = fun(){
                uni_navigateTo(NavigateToOptions(url = "/pages/help/help"));
            }
            ;
            val goToReturn = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (!order.value.orderId) {
                            uni_showToast(ShowToastOptions(title = "Order ID is missing", icon = "none"));
                            return@w;
                        }
                        try {
                            val res = await(orderApi.completeOrder(order.value.orderId));
                            if (res.code === 1) {
                                uni_showToast(ShowToastOptions(title = "Order completed. Proceed to return.", icon = "success"));
                                uni_navigateTo(NavigateToOptions(url = "/pages/order_details/order_details_home?orderId=" + order.value.orderId));
                            } else {
                                uni_showToast(ShowToastOptions(title = res.msg || "Failed to complete order", icon = "none"));
                            }
                        }
                         catch (err: Throwable) {
                            console.error("🚨 Failed to complete order:", err);
                            uni_showToast(ShowToastOptions(title = "Network error. Please try again later.", icon = "none"));
                        }
                });
            }
            ;
            val goHome = fun(){
                uni_reLaunch(ReLaunchOptions(url = "/pages/home/home"));
            }
            ;
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
                                    createElementVNode("text", utsMapOf("class" to "order-price"), "In Progress"),
                                    createElementVNode("view", utsMapOf("class" to "order-tags"), utsArrayOf(
                                        createElementVNode("view", utsMapOf("class" to "badge"), "Order ID: " + toDisplayString(order.value.orderId), 1),
                                        createElementVNode("view", utsMapOf("class" to "status-tag"), "In Progress")
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
                                        createElementVNode("text", utsMapOf("class" to "label"), "Time Elapsed:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(order.value.usedDuration), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Usage Time:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(order.value.duration), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "divider")),
                                    createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), "Price:"),
                                        createElementVNode("text", utsMapOf("class" to "value"), "￡" + toDisplayString(order.value.cost), 1)
                                    ))
                                )),
                                createElementVNode("view", utsMapOf("class" to "button-group"), utsArrayOf(
                                    createVNode(_component_van_button, utsMapOf("class" to "half-button home-btn", "type" to "default", "onClick" to goHome), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                        return utsArrayOf(
                                            " Return Home "
                                        );
                                    }
                                    ), "_" to 1)),
                                    createVNode(_component_van_button, utsMapOf("class" to "half-button feedback-btn", "type" to "primary", "onClick" to goToReturn), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                        return utsArrayOf(
                                            " Go to return vehicle "
                                        );
                                    }
                                    ), "_" to 1))
                                ))
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
                return utsMapOf("back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "50rpx", "left" to "60rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "person-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "50rpx", "right" to "60rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "person-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "map-container" to padStyleMapOf(utsMapOf("width" to "100%", "position" to "relative")), "map" to padStyleMapOf(utsMapOf("width" to "100%", "height" to "100%")), "map-overlay" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to 0, "left" to 0, "width" to "100%", "height" to "100%", "background" to "linear-gradient(\n    to bottom,\n    rgba(0, 123, 255, 0.2) 0%,   \n    rgba(0, 123, 255, 0.1) 50%,  \n    rgba(255, 255, 255, 0) 100%  \n  )", "pointerEvents" to "none", "zIndex" to 2)), "floating-panel" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.8)", "backdropFilter" to "blur(15px)", "borderTopLeftRadius" to 20, "borderTopRightRadius" to 20, "borderBottomRightRadius" to 0, "borderBottomLeftRadius" to 0, "boxShadow" to "0 -5px 20px rgba(0, 0, 0, 0.1)")), "order-details" to padStyleMapOf(utsMapOf("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20)), "order-info" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "flexDirection" to "row", "fontSize" to 16, "fontWeight" to "bold", "paddingBottom" to 10, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "rgba(255,255,255,0.3)")), "order-tags" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "gap" to "10px")), "badge" to padStyleMapOf(utsMapOf("backgroundColor" to "#eeeeee", "paddingTop" to 4, "paddingRight" to 10, "paddingBottom" to 4, "paddingLeft" to 10, "borderRadius" to 12, "fontSize" to 12, "color" to "#444444")), "status-tag" to padStyleMapOf(utsMapOf("backgroundColor" to "#007aff", "color" to "#FFFFFF", "paddingTop" to 4, "paddingRight" to 10, "paddingBottom" to 4, "paddingLeft" to 10, "fontSize" to 12, "borderRadius" to 12)), "order-price" to padStyleMapOf(utsMapOf("fontSize" to 25, "fontWeight" to "bold", "color" to "#0079fe")), "feedback-icon" to padStyleMapOf(utsMapOf("fontSize" to 24, "color" to "#007aff", "cursor" to "pointer")), "info-box" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "rgba(229,231,245,0.5)", "backdropFilter" to "blur(25px)", "paddingTop" to 30, "paddingRight" to 30, "paddingBottom" to 30, "paddingLeft" to 30, "borderRadius" to 15, "marginTop" to 15, "borderWidth" to 1, "borderStyle" to "solid", "borderColor" to "#82b1ff", "boxShadow" to "0 5px 15px rgba(0, 0, 0, 0.1)")), "info-item" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "marginBottom" to 8, "overflow" to "visible", "wordBreak" to "break-word")), "label" to padStyleMapOf(utsMapOf("fontSize" to 17, "fontWeight" to "bold")), "value" to utsMapOf("" to utsMapOf("fontSize" to 14, "color" to "#666666"), ".address" to utsMapOf("fontSize" to "25rpx", "color" to "#666666", "marginTop" to "4rpx", "width" to 160, "textAlign" to "right")), "divider" to padStyleMapOf(utsMapOf("width" to "100%", "height" to 1, "backgroundColor" to "#dddddd", "marginTop" to 25, "marginRight" to 0, "marginBottom" to 25, "marginLeft" to 0)), "feedback-btn" to padStyleMapOf(utsMapOf("borderRadius" to 25, "fontSize" to "34rpx", "height" to "96rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "boxShadow" to "0 4px 10px rgba(0, 122, 255, 0.2)", "transform:active" to "scale(0.98)")), "button-group" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "gap" to "20rpx", "marginTop" to 20)), "half-button" to padStyleMapOf(utsMapOf("flex" to 1, "height" to "96rpx", "borderRadius" to 25, "fontSize" to "32rpx", "transform:active" to "scale(0.98)")), "home-btn" to padStyleMapOf(utsMapOf("borderRadius" to 25, "fontSize" to "34rpx", "height" to "96rpx", "backgroundImage" to "none", "backgroundColor" to "#e7edf1", "color" to "#0084ff", "borderWidth" to 1, "borderStyle" to "solid", "borderColor" to "#82b1ff")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
