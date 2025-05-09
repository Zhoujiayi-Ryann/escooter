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
import io.dcloud.uniapp.extapi.redirectTo as uni_redirectTo;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesBookingRightBookingRight : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLoad(fun(query: OnLoadOptions) {
            console.log("📦 booking_right received query:", query);
            this.orderId = query.orderId;
            console.log("✅ Received orderId value:", this.orderId);
            console.log("✅ orderId type:", UTSAndroid.`typeof`( this.orderId));
            if (UTSAndroid.`typeof`( this.orderId) === "string") {
                val numId = Number(this.orderId);
                if (!isNaN(numId)) {
                    console.log("✅ orderId converted to number:", numId);
                } else {
                    console.warn("⚠️ orderId is not a valid number:", this.orderId);
                }
            }
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        val _component_img = resolveComponent("img");
        val _component_h2 = resolveComponent("h2");
        val _component_br = resolveComponent("br");
        val _component_p = resolveComponent("p");
        return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "floating-btn close-btn", "onClick" to _ctx.navigateToHome), utsArrayOf(
                createVNode(_component_van_icon, utsMapOf("name" to "close", "class" to "close-icon"))
            ), 8, utsArrayOf(
                "onClick"
            )),
            createElementVNode("view", utsMapOf("class" to "content"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "icon"), utsArrayOf(
                    createVNode(_component_img, utsMapOf("src" to "/static/booking_su/icon.png", "alt" to "icon"))
                )),
                createVNode(_component_h2, null, utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        "Booking placed successfully"
                    );
                }
                ), "_" to 1)),
                createVNode(_component_p, null, utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        "Thanks for your Booking. ",
                        createVNode(_component_br),
                        " Your Booking has been placed successfully.",
                        createVNode(_component_br),
                        " Please continue your Book. "
                    );
                }
                ), "_" to 1))
            )),
            createElementVNode("view", utsMapOf("class" to "btn-content"), utsArrayOf(
                createElementVNode("button", utsMapOf("class" to "see", "onClick" to _ctx.navigateToOrderDetails), " See Details ", 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("button", utsMapOf("class" to "back", "onClick" to _ctx.navigateToHome), " Back Home ", 8, utsArrayOf(
                    "onClick"
                ))
            ))
        ));
    }
    open var orderId: Any? by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("orderId" to null);
    }
    override fun `$initMethods`() {
        this.navigateToHome = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/home/home"));
        }
        ;
        this.navigateToOrderDetails = fun() {
            if (!this.orderId) {
                uni_showToast(ShowToastOptions(title = "Order ID is missing", icon = "none"));
                return;
            }
            console.log("🔀 Navigating to order_not_used, orderId =", this.orderId);
            val orderId = String(this.orderId);
            uni_navigateTo(NavigateToOptions(url = "/pages/order_not_used/order_not_used_home?orderId=" + orderId, success = fun(_){
                console.log("✅ Navigation succeeded");
            }
            , fail = fun(err){
                console.error("❌ Navigation failed:", err);
                uni_redirectTo(RedirectToOptions(url = "/pages/order_not_used/order_not_used_home?orderId=" + orderId));
            }
            ));
        }
        ;
    }
    open lateinit var navigateToHome: () -> Unit;
    open lateinit var navigateToOrderDetails: () -> Unit;
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "space-between", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center", "backgroundImage" to "linear-gradient(to bottom, #f0faff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)")), "close-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "50rpx", "left" to "50rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10)), "close-icon" to padStyleMapOf(utsMapOf("fontSize" to "90rpx", "color" to "#7dafff")), "floating-btn" to padStyleMapOf(utsMapOf("position" to "fixed", "top" to 30, "left" to 30, "zIndex" to 100, "backgroundColor" to "rgba(255,255,255,0.8)", "width" to 50, "height" to 50, "display" to "flex", "justifyContent" to "center", "alignItems" to "center", "boxShadow" to "0 2px 4px rgba(0, 0, 0, 0.2)")), "van-icon" to utsMapOf(".floating-btn " to utsMapOf("fontSize" to 28, "color" to "#007aff")), "content" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "flex" to 1, "textAlign" to "center")), "icon" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "marginRight" to 0, "marginBottom" to "20rpx", "marginLeft" to 0)), "btn-content" to padStyleMapOf(utsMapOf("width" to "100%", "position" to "absolute", "bottom" to "50rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center")), "back" to padStyleMapOf(utsMapOf("width" to "80%", "marginTop" to "10rpx", "marginRight" to 0, "marginBottom" to "10rpx", "marginLeft" to 0, "height" to "90rpx", "lineHeight" to "90rpx", "fontSize" to "35rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#ffffff", "borderRadius" to "70rpx", "transitionDuration" to "0.3s", "transform:active" to "scale(0.96)")), "see" to padStyleMapOf(utsMapOf("width" to "80%", "marginTop" to "10rpx", "marginRight" to 0, "marginBottom" to "10rpx", "marginLeft" to 0, "height" to "90rpx", "lineHeight" to "90rpx", "fontSize" to "35rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "color" to "#82b1ff", "borderRadius" to "70rpx", "transitionDuration" to "0.3s", "borderWidth" to 1, "borderStyle" to "solid", "borderColor" to "#82b1ff", "transform:active:active" to "scale(0.96)")), "@TRANSITION" to utsMapOf("back" to utsMapOf("duration" to "0.3s"), "see" to utsMapOf("duration" to "0.3s")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
