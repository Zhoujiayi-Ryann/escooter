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
open class GenPagesNotificationNotification : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onPageShow(fun() {
            this.fetchNotifications();
            this.checkUnreadNotifications();
            this.startAutoCheck();
        }
        , __ins);
        onPageHide(fun() {
            this.stopAutoCheck();
        }
        , __ins);
        onUnload(fun() {
            this.stopAutoCheck();
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "back-btn", "onClick" to _ctx.handleBack), utsArrayOf(
                    createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "back-icon"))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("text", utsMapOf("class" to "header-title"), "Notification")
            )),
            createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.notifications, fun(item, __key, __index, _cached): Any {
                return createElementVNode("view", utsMapOf("key" to item.id, "class" to "msg-card"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "msg-title"), toDisplayString(item.title), 1),
                    createElementVNode("view", utsMapOf("class" to "msg-content"), toDisplayString(item.content), 1),
                    createElementVNode("view", utsMapOf("class" to "msg-time"), toDisplayString(_ctx.formatTime(item.createdAt)), 1),
                    if (item.type === "COUPON") {
                        createElementVNode("view", utsMapOf("key" to 0, "class" to "link", "onClick" to fun(){
                            _ctx.goToPages(item.type);
                        }), utsArrayOf(
                            createElementVNode("text", null, "See Details"),
                            createVNode(_component_van_icon, utsMapOf("name" to "arrow", "class" to "arrow-icon"))
                        ), 8, utsArrayOf(
                            "onClick"
                        ));
                    } else {
                        createCommentVNode("v-if", true);
                    }
                ));
            }
            ), 128),
            if (_ctx.notifications.length === 0) {
                createElementVNode("view", utsMapOf("key" to 0, "class" to "empty-tip"), "No Messages");
            } else {
                createCommentVNode("v-if", true);
            }
        ));
    }
    open var notifications: UTSArray<Any?> by `$data`;
    open var hasUnread: Boolean by `$data`;
    open var checkInterval: Any? by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("notifications" to utsArrayOf(), "hasUnread" to false, "checkInterval" to null);
    }
    override fun `$initMethods`() {
        this.startAutoCheck = fun() {
            this.stopAutoCheck();
            this.checkInterval = setInterval(fun(){
                this.checkUnreadNotifications();
            }
            , 30000);
        }
        ;
        this.stopAutoCheck = fun() {
            if (this.checkInterval) {
                clearInterval(this.checkInterval);
                this.checkInterval = null;
            }
        }
        ;
        this.handleBack = fun() {
            val pages = getCurrentPages();
            if (pages.length > 1) {
                uni_navigateBack(null);
            } else {
                uni_reLaunch(ReLaunchOptions(url = "/pages/home/home"));
            }
        }
        ;
        this.checkUnreadNotifications = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend w@{
                    val userId = userApi.getUserId();
                    if (!userId) {
                        this.hasUnread = false;
                        return@w;
                    }
                    try {
                        val res = await(notificationApi.countUserUnreadNotifications(userId));
                        if (res.code === 1) {
                            this.hasUnread = res.data > 0;
                        } else {
                            this.hasUnread = false;
                        }
                    }
                     catch (err: Throwable) {
                        console.error("Failed to check unread notifications:", err);
                        this.hasUnread = false;
                    }
            });
        }
        ;
        this.fetchNotifications = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend w@{
                    val userId = userApi.getUserId();
                    if (!userId) {
                        this.notifications = utsArrayOf();
                        return@w;
                    }
                    try {
                        val res = await(notificationApi.getUserNotifications(userId));
                        console.log("Raw notification data:", res.data);
                        if (res.code === 1 && UTSArray.isArray(res.data)) {
                            this.notifications = res.data.sort(fun(a, b){
                                return Date(b.createdAt) - Date(a.createdAt);
                            });
                            await(this.markAllAsRead(userId));
                        } else {
                            this.notifications = utsArrayOf();
                        }
                    }
                     catch (err: Throwable) {
                        console.error("Failed to fetch notifications:", err);
                        this.notifications = utsArrayOf();
                    }
            });
        }
        ;
        this.markAllAsRead = fun(userId): UTSPromise<Unit> {
            return wrapUTSPromise(suspend {
                    try {
                        val res = await(notificationApi.markAllAsRead(userId));
                        if (res.code === 1) {
                            console.log("All notifications marked as read");
                            this.hasUnread = false;
                        }
                    }
                     catch (err: Throwable) {
                        console.error("Failed to mark notifications as read:", err);
                    }
            });
        }
        ;
        this.goToPages = fun(type) {
            if (type === "COUPON") {
                uni_navigateTo(NavigateToOptions(url = "/pages/settings/coupons/coupon"));
            } else if (type === "COMMENT_REPLY") {
                uni_navigateTo(NavigateToOptions(url = "/pages/feedback/detail"));
            }
        }
        ;
        this.formatTime = fun(timestamp): String {
            if (!timestamp) {
                return "";
            }
            val date = Date(timestamp);
            val year = date.getFullYear();
            val month = String(date.getMonth() + 1).padStart(2, "0");
            val day = String(date.getDate()).padStart(2, "0");
            val hours = String(date.getHours()).padStart(2, "0");
            val minutes = String(date.getMinutes()).padStart(2, "0");
            return "" + year + "-" + month + "-" + day + " " + hours + ":" + minutes;
        }
        ;
    }
    open lateinit var startAutoCheck: () -> Unit;
    open lateinit var stopAutoCheck: () -> Unit;
    open lateinit var handleBack: () -> Unit;
    open lateinit var checkUnreadNotifications: () -> UTSPromise<Unit>;
    open lateinit var fetchNotifications: () -> UTSPromise<Unit>;
    open lateinit var markAllAsRead: (userId) -> UTSPromise<Unit>;
    open lateinit var goToPages: (type) -> Unit;
    open lateinit var formatTime: (timestamp) -> String;
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("overflowY" to "auto", "backgroundImage" to "linear-gradient(to bottom, #e0f0ff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "boxSizing" to "border-box")), "header" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "textAlign" to "center", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "position" to "relative", "marginBottom" to "30rpx")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "25rpx", "left" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center")), "msg-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#f8fbfd", "borderRadius" to "18rpx", "paddingTop" to "32rpx", "paddingRight" to "28rpx", "paddingBottom" to "20rpx", "paddingLeft" to "28rpx", "marginBottom" to "28rpx", "boxShadow" to "0 2px 8px rgba(0,0,0,0.03)", "position" to "relative")), "msg-title" to padStyleMapOf(utsMapOf("fontSize" to "34rpx", "fontWeight" to "bold", "color" to "#222222", "marginBottom" to "12rpx")), "msg-content" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#444444", "marginBottom" to "16rpx")), "msg-time" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#b0b0b0", "textAlign" to "left", "paddingTop" to "8rpx", "paddingRight" to 0, "paddingBottom" to "8rpx", "paddingLeft" to 0, "marginTop" to "8rpx", "marginRight" to 0, "marginBottom" to "8rpx", "marginLeft" to 0)), "empty-tip" to padStyleMapOf(utsMapOf("textAlign" to "center", "color" to "#aaaaaa", "marginTop" to "80rpx", "fontSize" to "32rpx")), "link" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "alignItems" to "center", "justifyContent" to "space-between", "paddingTop" to "16rpx", "paddingRight" to 0, "paddingBottom" to 0, "paddingLeft" to 0, "marginTop" to "8rpx", "borderTopWidth" to 1, "borderTopStyle" to "solid", "borderTopColor" to "#eeeeee", "color" to "#0084ff", "fontSize" to "28rpx", "cursor" to "pointer")), "arrow-icon" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "color" to "#0084ff")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
