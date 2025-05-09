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
open class GenPagesSettingsMySettingsMySettings : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLoad(fun(_: OnLoadOptions) {
            this.getUserInformation();
        }
        , __ins);
        onPageShow(fun() {
            this.getUserInformation();
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_image = resolveComponent("van-image");
        val _component_van_cell = resolveComponent("van-cell");
        val _component_van_notice_bar = resolveComponent("van-notice-bar");
        val _component_van_icon = resolveComponent("van-icon");
        val _component_van_col = resolveComponent("van-col");
        val _component_van_row = resolveComponent("van-row");
        val _component_van_grid_item = resolveComponent("van-grid-item");
        val _component_van_grid = resolveComponent("van-grid");
        val _component_van_cell_group = resolveComponent("van-cell-group");
        val _component_van_tabbar_item = resolveComponent("van-tabbar-item");
        val _component_van_tabbar = resolveComponent("van-tabbar");
        return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "mine-page"), utsArrayOf(
                createVNode(_component_van_cell, utsMapOf("class" to "user-info", "center" to ""), utsMapOf("icon" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createVNode(_component_van_image, utsMapOf("round" to "", "width" to "60", "height" to "60", "src" to _ctx.user.avatar), null, 8, utsArrayOf(
                            "src"
                        ))
                    );
                }
                ), "title" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createElementVNode("view", null, utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to "username"), toDisplayString(_ctx.user.name), 1),
                            createElementVNode("view", utsMapOf("class" to "userid"), "ID: " + toDisplayString(_ctx.user.id), 1)
                        ))
                    );
                }
                ), "_" to 1)),
                createVNode(_component_van_notice_bar, utsMapOf("scrollable" to "", "text" to "Member users will receive surprise benefits during holidays, and randomly drop out great value coupons! Up to £ 100 coupon, you can save more on riding!")),
                createElementVNode("view", utsMapOf("class" to "mileage-card"), utsArrayOf(
                    createVNode(_component_van_row, utsMapOf("gutter" to "20"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            createVNode(_component_van_col, utsMapOf("span" to "12", "class" to "mileage-item"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                return utsArrayOf(
                                    createVNode(_component_van_icon, utsMapOf("name" to "clock-o", "class" to "mileage-icon")),
                                    createElementVNode("view", utsMapOf("class" to "mileage-label"), "Usage Hours"),
                                    createElementVNode("view", utsMapOf("class" to "mileage-value"), toDisplayString(_ctx.mileage.hours), 1)
                                );
                            }
                            ), "_" to 1)),
                            createVNode(_component_van_col, utsMapOf("span" to "12", "class" to "mileage-item"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                return utsArrayOf(
                                    createVNode(_component_van_icon, utsMapOf("name" to "balance-o", "class" to "mileage-icon")),
                                    createElementVNode("view", utsMapOf("class" to "mileage-label"), "Total Spent"),
                                    createElementVNode("view", utsMapOf("class" to "mileage-value"), "￡" + toDisplayString(_ctx.mileage.spent.toFixed(2)), 1)
                                );
                            }
                            ), "_" to 1))
                        );
                    }
                    ), "_" to 1))
                )),
                createElementVNode("view", utsMapOf("class" to "page-section"), utsArrayOf(
                    createVNode(_component_van_grid, utsMapOf("column-num" to 4, "border" to ""), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            createVNode(_component_van_grid_item, utsMapOf("icon" to "orders-o", "text" to "All Orders", "onClick" to fun(){
                                _ctx.navigateTo("orders");
                            }
                            ), null, 8, utsArrayOf(
                                "onClick"
                            )),
                            createVNode(_component_van_grid_item, utsMapOf("icon" to "card", "text" to "My Card", "onClick" to fun(){
                                _ctx.navigateTo("card");
                            }
                            ), null, 8, utsArrayOf(
                                "onClick"
                            )),
                            createVNode(_component_van_grid_item, utsMapOf("icon" to "service-o", "text" to "Help", "onClick" to fun(){
                                _ctx.navigateTo("help");
                            }
                            ), null, 8, utsArrayOf(
                                "onClick"
                            )),
                            createVNode(_component_van_grid_item, utsMapOf("icon" to "bell", "text" to "Notifications", "onClick" to fun(){
                                _ctx.navigateTo("notify");
                            }
                            ), null, 8, utsArrayOf(
                                "onClick"
                            ))
                        );
                    }
                    ), "_" to 1)),
                    createVNode(_component_van_cell_group, utsMapOf("class" to "function-group"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            createVNode(_component_van_cell, utsMapOf("title" to "Add Card", "is-link" to "", "size" to "large", "onClick" to _ctx.goCard), null, 8, utsArrayOf(
                                "onClick"
                            )),
                            createVNode(_component_van_cell, utsMapOf("title" to "Feedback", "is-link" to "", "size" to "large", "onClick" to _ctx.goFeedback), null, 8, utsArrayOf(
                                "onClick"
                            )),
                            createVNode(_component_van_cell, utsMapOf("title" to "Edit Profile", "is-link" to "", "size" to "large", "onClick" to _ctx.gochange), null, 8, utsArrayOf(
                                "onClick"
                            )),
                            createVNode(_component_van_cell, utsMapOf("title" to "Log Out", "is-link" to "", "size" to "large", "onClick" to _ctx.handleLogout), null, 8, utsArrayOf(
                                "onClick"
                            ))
                        );
                    }
                    ), "_" to 1))
                )),
                createVNode(_component_van_tabbar, utsMapOf("route" to "", "fixed" to ""), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createVNode(_component_van_tabbar_item, utsMapOf("icon" to "home-o", "to" to "/pages/home/home"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                "Home"
                            );
                        }
                        ), "_" to 1)),
                        createVNode(_component_van_tabbar_item, utsMapOf("icon" to "user-o", "to" to "/pages/settings/my_settings/my_settings"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                "Mine"
                            );
                        }
                        ), "_" to 1))
                    );
                }
                ), "_" to 1))
            ))
        ));
    }
    open var user: UTSJSONObject by `$data`;
    open var orderStatus: UTSJSONObject by `$data`;
    open var mileage: UTSJSONObject by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("user" to object : UTSJSONObject() {
            var name = "Guest"
            var id = "0"
            var avatar = ""
        }, "orderStatus" to object : UTSJSONObject() {
            var unused: Number = 1
        }, "mileage" to object : UTSJSONObject() {
            var hours: Number = 0
            var spent: Number = 0
        });
    }
    override fun `$initMethods`() {
        this.getUserInformation = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend w@{
                    try {
                        val userId = userApi.getUserId();
                        if (!userId) {
                            uni_showToast(ShowToastOptions(title = "Not logged in", icon = "none"));
                            return@w;
                        }
                        val res = await(userApi.getUserDetail(userId));
                        if (res.code === 1 && res.data) {
                            val user = res.data;
                            this.user.name = user.username || "Guest";
                            this.user.id = String(user.user_id || user.userId || 0);
                            val avatarPath = user.avatar_path || user.avatarPath;
                            this.user.avatar = if (avatarPath && avatarPath.trim() !== "") {
                                avatarPath;
                            } else {
                                "/static/avatar/userp.jpg";
                            };
                            this.mileage.hours = parseFloat(user.totalUsageHours || 0).toFixed(1);
                            this.mileage.spent = parseFloat(user.totalSpent || 0);
                            userApi.updateLocalUserInfo(user);
                            console.log("✅ Loaded user info from server:", this.user.avatar);
                        } else {
                            throw UTSError(res.msg || "Failed to load user");
                        }
                    }
                     catch (e: Throwable) {
                        console.error("❌ Error loading user info:", e);
                        val cached = userApi.getUserInfo();
                        if (cached) {
                            this.user.name = cached.username;
                            this.user.id = String(cached.user_id || cached.userId || 0);
                            this.user.avatar = cached.avatar_path || "/static/avatar/userp.jpg";
                        }
                    }
            });
        }
        ;
        this.handleLogout = fun() {
            userApi.logout();
            uni_showToast(ShowToastOptions(title = "Logged out", icon = "success"));
            this.user.name = "Guest";
            this.user.id = "0";
            setTimeout(fun(){
                uni_navigateTo(NavigateToOptions(url = "/pages/login/login"));
            }
            , 1500);
        }
        ;
        this.goCard = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/settings/card/card"));
        }
        ;
        this.navigateTo = fun(page) {
            val map: UTSJSONObject = object : UTSJSONObject() {
                var orders = "/pages/settings/orders/orders"
                var card = "/pages/cards/cards"
                var help = "/pages/help/help"
                var notify = "/pages/notification/notification"
            };
            if (map[page]) {
                uni_navigateTo(NavigateToOptions(url = map[page]));
            }
        }
        ;
        this.goFeedback = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/feedback/feedback"));
        }
        ;
        this.gochange = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/settings/change/change"));
        }
        ;
    }
    open lateinit var getUserInformation: () -> UTSPromise<Unit>;
    open lateinit var handleLogout: () -> Unit;
    open lateinit var goCard: () -> Unit;
    open lateinit var navigateTo: (page) -> Unit;
    open lateinit var goFeedback: () -> Unit;
    open lateinit var gochange: () -> Unit;
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
                return utsMapOf("mine-page" to padStyleMapOf(utsMapOf("background" to "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "boxSizing" to "border-box", "display" to "flex", "flexDirection" to "column", "gap" to "15rpx")), "user-info" to padStyleMapOf(utsMapOf("marginTop" to "100rpx", "borderRadius" to "24rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "boxShadow" to "0 8px 20px rgba(0, 0, 0, 0.05)", "!backgroundImage" to "none", "!backgroundColor" to "rgba(255,255,255,0.95)")), "username" to padStyleMapOf(utsMapOf("fontSize" to "34rpx", "fontWeight" to "bold", "color" to "#222222", "marginLeft" to "25rpx")), "userid" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#777777", "marginTop" to "6rpx", "marginLeft" to "25rpx")), "van-notice-bar" to padStyleMapOf(utsMapOf("marginTop" to "10rpx", "borderRadius" to "16rpx", "height" to "56rpx", "fontSize" to "24rpx")), "van-grid" to padStyleMapOf(utsMapOf("borderRadius" to "20rpx", "overflow" to "hidden", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "boxShadow" to "0 4px 15px rgba(0,0,0,0.05)")), "van-grid-item__content" to padStyleMapOf(utsMapOf("paddingTop" to "35rpx", "paddingRight" to 0, "paddingBottom" to "35rpx", "paddingLeft" to 0)), "van-grid-item__icon" to padStyleMapOf(utsMapOf("!fontSize" to "44rpx")), "van-grid-item__text" to padStyleMapOf(utsMapOf("fontSize" to "26rpx")), "page-section" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "display" to "flex", "flexDirection" to "column", "gap" to "45rpx")), "mileage-card" to padStyleMapOf(utsMapOf("borderRadius" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to 0, "paddingBottom" to "30rpx", "paddingLeft" to 0, "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "boxShadow" to "0 4px 12px rgba(0,0,0,0.05)", "marginTop" to "10rpx")), "mileage-item" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center")), "mileage-icon" to padStyleMapOf(utsMapOf("fontSize" to "50rpx", "color" to "#00cc66", "marginBottom" to "10rpx")), "mileage-value" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#333333")), "mileage-label" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#777777")), "function-group" to padStyleMapOf(utsMapOf("!borderRadius" to "18rpx", "marginTop" to "20rpx", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "boxShadow" to "0 4px 15px rgba(0,0,0,0.05)", "!paddingLeft" to 0, "!paddingRight" to 0, "overflow" to "hidden")), "van-cell" to utsMapOf(".function-group " to utsMapOf("!fontSize" to "30rpx", "!color" to "#333333", "marginBottom" to "30rpx", "borderBottomWidth::after" to 1, "borderBottomStyle::after" to "solid", "borderBottomColor::after" to "rgba(0,0,0,0.05)")), "van-tabbar" to padStyleMapOf(utsMapOf("boxShadow" to "0 -2px 12px rgba(0,0,0,0.05)", "height" to "100rpx")), "van-tabbar-item" to padStyleMapOf(utsMapOf("fontSize" to "26rpx")), "van-tabbar-item__icon" to padStyleMapOf(utsMapOf("fontSize" to "38rpx")), "van-tabbar-item--active" to padStyleMapOf(utsMapOf("!color" to "#007aff")), "van-badge" to padStyleMapOf(utsMapOf("transform" to "translate(5rpx,-5rpx)")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
