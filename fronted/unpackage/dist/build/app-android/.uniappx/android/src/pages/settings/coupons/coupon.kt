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
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesSettingsCouponsCoupon : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLoad(fun(_: OnLoadOptions) {
            this.fetchCoupons();
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        val _component_van_tab = resolveComponent("van-tab");
        val _component_van_tabs = resolveComponent("van-tabs");
        return createElementVNode("view", utsMapOf("class" to "coupon-page"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "back-btn", "onClick" to _ctx.navigateBack), utsArrayOf(
                    createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "back-icon"))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("text", utsMapOf("class" to "header-title"), "My Coupons")
            )),
            createVNode(_component_van_tabs, utsMapOf("active" to _ctx.activeTab, "onUpdate:active" to fun(`$event`: Number){
                _ctx.activeTab = `$event`;
            }
            , "animated" to "", "swipeable" to "", "sticky" to ""), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                return utsArrayOf(
                    createVNode(_component_van_tab, utsMapOf("title" to "Available"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            if (_ctx.coupons.length > 0) {
                                createElementVNode("view", utsMapOf("key" to 0, "class" to "coupon-list"), utsArrayOf(
                                    createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.coupons, fun(coupon, index, __index, _cached): Any {
                                        return createElementVNode("view", utsMapOf("class" to "coupon-item", "key" to coupon.id), utsArrayOf(
                                            createElementVNode("view", utsMapOf("class" to "coupon-card"), utsArrayOf(
                                                createElementVNode("view", utsMapOf("class" to "coupon-left"), utsArrayOf(
                                                    createElementVNode("view", utsMapOf("class" to "coupon-amount"), utsArrayOf(
                                                        createElementVNode("text", utsMapOf("class" to "currency"), "£"),
                                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(coupon.denominations), 1)
                                                    )),
                                                    createElementVNode("text", utsMapOf("class" to "coupon-condition"), toDisplayString(coupon.condition), 1)
                                                )),
                                                createElementVNode("view", utsMapOf("class" to "coupon-right"), utsArrayOf(
                                                    createElementVNode("text", utsMapOf("class" to "coupon-name"), toDisplayString(coupon.coupon_name), 1),
                                                    createElementVNode("text", utsMapOf("class" to "coupon-desc"), toDisplayString(coupon.description), 1),
                                                    createElementVNode("text", utsMapOf("class" to "coupon-date"), "Valid until: " + toDisplayString(_ctx.formatDate(coupon.endAt)), 1)
                                                ))
                                            ))
                                        ));
                                    }), 128)
                                ));
                            } else {
                                createElementVNode("view", utsMapOf("key" to 1, "class" to "empty-state"), utsArrayOf(
                                    createElementVNode("image", utsMapOf("src" to "/static/icons/empty-coupon.png", "class" to "empty-icon", "mode" to "aspectFit")),
                                    createElementVNode("text", utsMapOf("class" to "empty-text"), "No Available Coupons")
                                ));
                            }
                        );
                    }
                    ), "_" to 1)),
                    createVNode(_component_van_tab, utsMapOf("title" to "Expired"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            if (_ctx.disabledCoupons.length > 0) {
                                createElementVNode("view", utsMapOf("key" to 0, "class" to "coupon-list disabled"), utsArrayOf(
                                    createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.disabledCoupons, fun(coupon, index, __index, _cached): Any {
                                        return createElementVNode("view", utsMapOf("class" to "coupon-item disabled", "key" to coupon.id), utsArrayOf(
                                            createElementVNode("view", utsMapOf("class" to "coupon-card"), utsArrayOf(
                                                createElementVNode("view", utsMapOf("class" to "coupon-left"), utsArrayOf(
                                                    createElementVNode("view", utsMapOf("class" to "coupon-amount"), utsArrayOf(
                                                        createElementVNode("text", utsMapOf("class" to "currency"), "£"),
                                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(coupon.denominations), 1)
                                                    )),
                                                    createElementVNode("text", utsMapOf("class" to "coupon-condition"), toDisplayString(coupon.condition), 1)
                                                )),
                                                createElementVNode("view", utsMapOf("class" to "coupon-right"), utsArrayOf(
                                                    createElementVNode("text", utsMapOf("class" to "coupon-name"), toDisplayString(coupon.coupon_name), 1),
                                                    createElementVNode("text", utsMapOf("class" to "coupon-desc"), toDisplayString(coupon.description), 1),
                                                    createElementVNode("text", utsMapOf("class" to "coupon-date"), "Expired: " + toDisplayString(_ctx.formatDate(coupon.endAt)), 1)
                                                )),
                                                createElementVNode("view", utsMapOf("class" to "expired-mark"), "Expired")
                                            ))
                                        ));
                                    }), 128)
                                ));
                            } else {
                                createElementVNode("view", utsMapOf("key" to 1, "class" to "empty-state"), utsArrayOf(
                                    createElementVNode("image", utsMapOf("src" to "/static/icons/empty-coupon.png", "class" to "empty-icon", "mode" to "aspectFit")),
                                    createElementVNode("text", utsMapOf("class" to "empty-text"), "No Expired Coupons")
                                ));
                            }
                        );
                    }
                    ), "_" to 1))
                );
            }
            ), "_" to 1), 8, utsArrayOf(
                "active",
                "onUpdate:active"
            ))
        ));
    }
    open var activeTab: Number by `$data`;
    open var coupons: UTSArray<Any?> by `$data`;
    open var disabledCoupons: UTSArray<Any?> by `$data`;
    open var loading: Boolean by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("activeTab" to 0, "coupons" to utsArrayOf(), "disabledCoupons" to utsArrayOf(), "loading" to false);
    }
    override fun `$initMethods`() {
        this.navigateBack = fun() {
            val pages = getCurrentPages();
            if (pages.length > 1) {
                uni_navigateBack(NavigateBackOptions(delta = 1));
            } else {
                uni_navigateTo(NavigateToOptions(url = "/pages/home/home"));
            }
        }
        ;
        this.formatDate = fun(timestamp): String {
            val date = Date(timestamp * 1000);
            return "" + date.getFullYear() + "-" + String(date.getMonth() + 1).padStart(2, "0") + "-" + String(date.getDate()).padStart(2, "0");
        }
        ;
        this.fetchCoupons = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend w@{
                    this.loading = true;
                    val userId = userApi.getUserId(0);
                    if (userId === 0) {
                        val userInfo = userApi.getUserInfo();
                        if (userInfo && userInfo.user_id) {
                            console.log("Using user_id from user info:", userInfo.user_id);
                            this.fetchCouponsWithUserId(userInfo.user_id);
                        } else {
                            uni_showToast(ShowToastOptions(title = "Please log in first", icon = "none"));
                            this.loading = false;
                        }
                        return@w;
                    }
                    this.fetchCouponsWithUserId(userId);
            });
        }
        ;
        this.fetchCouponsWithUserId = fun(userId): UTSPromise<Unit> {
            return wrapUTSPromise(suspend {
                    try {
                        console.log("Fetching coupons for userId:", userId);
                        val response = await(couponApi.getAvailableCoupons(userId));
                        console.log("Coupon API response:", response);
                        if (response.code === 1 && response.data) {
                            val now = Date();
                            val coupons = if (UTSArray.isArray(response.data)) {
                                response.data;
                            } else {
                                (response.data.coupons || utsArrayOf());
                            };
                            this.coupons = coupons.filter(fun(coupon){
                                return Date(coupon.valid_to) > now && coupon.is_active === true && coupon.is_used === false && coupon.status === "able";
                            }).map(fun(coupon){
                                return this.formatCouponData(coupon);
                            });
                            this.disabledCoupons = coupons.filter(fun(coupon){
                                return Date(coupon.valid_to) <= now || coupon.is_active !== true || coupon.is_used === true || coupon.status !== "able";
                            }).map(fun(coupon){
                                return this.formatCouponData(coupon);
                            });
                            console.log("Available coupons:", this.coupons);
                            console.log("Unavailable coupons:", this.disabledCoupons);
                        } else {
                            uni_showToast(ShowToastOptions(title = "Failed to retrieve coupons", icon = "none"));
                        }
                    }
                     catch (error: Throwable) {
                        console.error("Failed to retrieve coupons:", error);
                        uni_showToast(ShowToastOptions(title = "Failed to retrieve coupons. Please try again.", icon = "none"));
                    }
                     finally{
                        this.loading = false;
                    }
            });
        }
        ;
        this.formatCouponData = fun(coupon): UTSJSONObject {
            return object : UTSJSONObject() {
                var id = coupon.coupon_id
                var coupon_name = coupon.coupon_name
                var name = coupon.coupon_name
                var denominations = coupon.coupon_amount || 0
                var condition = if (coupon.min_spend > 0) {
                    "MinSpent \xa3" + coupon.min_spend;
                } else {
                    "No MinSpent";
                }
                var originCondition = coupon.min_spend || 0
                var startAt = if (coupon.valid_from) {
                    Date(coupon.valid_from).getTime() / 1000;
                } else {
                    Date.now() / 1000;
                }
                var endAt = if (coupon.valid_to) {
                    Date(coupon.valid_to).getTime() / 1000;
                } else {
                    (Date.now() / 1000 + 2592000);
                }
                var valueDesc = "\xa3" + (coupon.coupon_amount || 0)
                var description = coupon.description || "Rent Coupon"
            };
        }
        ;
    }
    open lateinit var navigateBack: () -> Unit;
    open lateinit var formatDate: (timestamp) -> String;
    open lateinit var fetchCoupons: () -> UTSPromise<Unit>;
    open lateinit var fetchCouponsWithUserId: (userId) -> UTSPromise<Unit>;
    open lateinit var formatCouponData: (coupon) -> UTSJSONObject;
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
                return utsMapOf("coupon-page" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to bottom, #f0faff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "boxSizing" to "border-box", "display" to "flex", "flexDirection" to "column", "gap" to "25rpx")), "header" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "textAlign" to "center", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "position" to "relative", "marginBottom" to "30rpx")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "25rpx", "left" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center")), "coupon-list" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "gap" to "20rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx")), "coupon-item" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderRadius" to "18rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "overflow" to "hidden")), "coupon-card" to utsMapOf("" to utsMapOf("display" to "flex", "position" to "relative", "flexDirection" to "row", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff"), ".disabled " to utsMapOf("filter" to "grayscale(1)", "opacity" to 0.7)), "coupon-left" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "paddingRight" to "30rpx", "borderRightWidth" to 1, "borderRightStyle" to "dashed", "borderRightColor" to "#e8e8e8", "minWidth" to "180rpx")), "coupon-amount" to padStyleMapOf(utsMapOf("display" to "flex")), "currency" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "color" to "#ff6666")), "value" to padStyleMapOf(utsMapOf("fontSize" to "60rpx", "fontWeight" to "bold", "color" to "#ff6666")), "coupon-condition" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#888888", "marginTop" to "8rpx")), "coupon-right" to padStyleMapOf(utsMapOf("flex" to 1, "paddingLeft" to "30rpx", "display" to "flex", "flexDirection" to "column", "justifyContent" to "space-between")), "coupon-name" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "10rpx")), "coupon-desc" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666", "marginBottom" to "10rpx")), "coupon-date" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#999999")), "expired-mark" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "50%", "right" to "30rpx", "transform" to "translateY(-50%) rotate(-30deg)", "color" to "#ff0000", "fontSize" to "40rpx", "fontWeight" to "bold", "borderWidth" to "3rpx", "borderStyle" to "solid", "borderColor" to "#ff0000", "paddingTop" to "5rpx", "paddingRight" to "15rpx", "paddingBottom" to "5rpx", "paddingLeft" to "15rpx", "borderRadius" to "8rpx")), "empty-state" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "paddingTop" to "100rpx", "paddingRight" to 0, "paddingBottom" to "100rpx", "paddingLeft" to 0)), "empty-icon" to padStyleMapOf(utsMapOf("width" to "200rpx", "height" to "200rpx", "marginBottom" to "30rpx")), "empty-text" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "color" to "#999999")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
