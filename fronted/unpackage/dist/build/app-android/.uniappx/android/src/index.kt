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
import vant.Locale;
import vant as Vant;
import vant/es/locale/lang/en-US as enUS;
import vant/es/locale/lang/en-US as enUS1;
import io.dcloud.uniapp.extapi.exit as uni_exit;
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync;
import io.dcloud.uniapp.extapi.redirectTo as uni_redirectTo;
import io.dcloud.uniapp.extapi.removeStorageSync as uni_removeStorageSync;
import io.dcloud.uniapp.extapi.request as uni_request;
import io.dcloud.uniapp.extapi.setStorageSync as uni_setStorageSync;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
import io.dcloud.uniapp.extapi.uploadFile as uni_uploadFile;
val runBlock1 = run {
    __uniConfig.getAppStyles = fun(): Map<String, Map<String, Map<String, Any>>> {
        return GenApp.styles;
    }
    ;
}
var firstBackTime: Number = 0;
open class GenApp : BaseApp {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLaunch(fun(_: OnLaunchOptions) {
            console.log("App Launch");
        }
        , __ins);
        onAppShow(fun(_: OnShowOptions) {
            console.log("App Show");
        }
        , __ins);
        onAppHide(fun() {
            console.log("App Hide");
        }
        , __ins);
        onLastPageBackPress(fun() {
            console.log("App LastPageBackPress");
            if (firstBackTime == 0) {
                uni_showToast(ShowToastOptions(title = "再按一次退出应用", position = "bottom"));
                firstBackTime = Date.now();
                setTimeout(fun(){
                    firstBackTime = 0;
                }, 2000);
            } else if (Date.now() - firstBackTime < 2000) {
                firstBackTime = Date.now();
                uni_exit(null);
            }
        }
        , __ins);
        onExit(fun() {
            console.log("App Exit");
        }
        , __ins);
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(
                styles0
            ))
        };
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return utsMapOf("uni-row" to padStyleMapOf(utsMapOf("flexDirection" to "row")), "uni-column" to padStyleMapOf(utsMapOf("flexDirection" to "column")));
            }
    }
}
val GenAppClass = CreateVueAppComponent(GenApp::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "app", name = "", inheritAttrs = true, inject = Map(), props = Map(), propsNeedCastKeys = utsArrayOf(), emits = Map(), components = Map(), styles = GenApp.styles);
}
, fun(instance): GenApp {
    return GenApp(instance);
}
);
interface ApiResponse<T> {
    var code: Number
    var msg: String
    var data: T
}
interface LoginResponseData {
    var user_id: Number
    var username: String
    var token: String
}
interface RegisterResponseData {
    var user_id: Number
    var username: String
    var email: String
}
interface Scooter {
    var scooter_id: Number
    var location_lat: Number
    var location_lng: Number
    var battery_level: Number
    var price: Number
    var status: String
}
interface CreateOrderRequest {
    var user_id: Number
    var scooter_id: Number
    var pickup_address: String
    var start_time: String
    var end_time: String
}
interface OrderResponse {
    var order_id: Number
    var user_id: Number
    var scooter_id: Number
    var start_time: String
    var end_time: String
    var new_end_time: String?
    var extended_duration: Number
    var extended_cost: Number
    var cost: Number
    var discount_amount: Number
    var pickup_address: String
    var status: String
    var created_at: String
}
interface ScooterInfo {
    var latitude: Number
    var longitude: Number
    var battery_level: Number
    var price: Number
}
interface OrderDetailResponse {
    var order_id: Number
    var user_id: Number
    var scooter_id: Number
    var start_time: String
    var end_time: String
    var new_end_time: String?
    var duration: Number
    var extended_duration: Number
    var extended_cost: Number
    var cost: Number
    var discount_amount: Number
    var pickup_address: String
    var status: String
    var created_at: String
    var previous_status: String?
    var is_deleted: Boolean
    var scooter_info: ScooterInfo
}
interface PayOrderResponse {
    var order_id: Number
    var user_id: Number
    var status: String
    var cost: Number
    var discount_amount: Number
    var coupon_id: Number?
    var coupon_amount: Number?
}
interface AddCreditCardRequest {
    var user_id: Number
    var card_number: String
    var security_code: String
    var expiry_date: String
    var country: String?
}
interface AddCreditCardResponse {
    var card_id: Number
}
interface CreditCardResponse {
    var card_id: Number
    var card_number: String
    var expiry_date: String
    var country: String
}
enum class FeedbackType(override val value: String) : UTSEnumString {
    USING_PROBLEM("USING_PROBLEM"),
    EXPERIENCE_FEEDBACK("EXPERIENCE_FEEDBACK")
}
interface FeedbackRequest {
    var user_id: Number
    var feedback_type: FeedbackType
    var description: String
    var happening_time: String?
    var bill_number: String?
    var image_urls: UTSArray<String>?
}
interface FeedbackResponse {
    var feedback_id: Number
    var order_id: Number?
    var image_urls: UTSArray<String>
}
interface CouponRequest {
    var couponId: Number
    var orderId: Number
}
interface Coupon {
    var id: Number
    var name: String
    var description: String
    var type: Number
    var value: Number
    var threshold: Number
    var startTime: String
    var endTime: String
    var status: Number
}
interface AvailableCouponsResponse {
    var coupons: UTSArray<Coupon>
}
interface ExtendOrderRequest {
    var order_id: Number
    var new_end_time: String
    var reason: String?
}
interface ChangeOrderStatusResponse {
    var order_id: Number
    var status: String
    var previous_status: String
    var message: String
}
interface AvailableTimeSlotsResponse {
    var current_end_time: String
    var next_start_time: String
    var max_extended_hours: Number
}
enum class NotificationType(override val value: String) : UTSEnumString {
    COUPON("COUPON"),
    COMMENT_REPLY("COMMENT_REPLY")
}
interface NotificationResponse {
    var id: Number
    var user_id: Number
    var type: NotificationType
    var title: String
    var content: String
    var is_read: Boolean
    var related_id: Number
    var created_at: String
}
val BASE_URL = "https://khnrsggvzudb.sealoshzh.site/api";
val TOKEN_KEY = "token";
val DEV_MODE = true;
fun getToken(): String {
    val token = uni_getStorageSync(TOKEN_KEY);
    return token || "";
}
fun setToken(token: String) {
    uni_setStorageSync(TOKEN_KEY, token);
}
fun removeToken() {
    uni_removeStorageSync(TOKEN_KEY);
}
fun <T> request(options: {
    var url: String
    var method: String?
    var data: Any?
    var needToken: Boolean?
}): UTSPromise<ApiResponse<T>> {
    return UTSPromise(fun(resolve, reject){
        val token = getToken();
        val header: Any = object : UTSJSONObject() {
            var `Content-Type` = "application/json"
            var Accept = "application/json"
        };
        if (!DEV_MODE && options.needToken && token) {
            header.token = token;
        }
        uni_request<Any>(RequestOptions(url = BASE_URL + options.url, method = options.method || "GET", data = options.data, header = header, withCredentials = true, success = fun(res: Any){
            if (!DEV_MODE && res.statusCode === 401) {
                removeToken();
                uni_redirectTo(RedirectToOptions(url = "/pages/login/login"));
                reject(UTSError("Unauthorized"));
                return;
            }
            resolve(res.data);
        }
        , fail = fun(err: Any){
            console.error("请求失败:", err);
            reject(err);
        }
        ));
    }
    );
}
interface UserInfo {
    var user_id: Number
    var username: String
    var password: String?
    var email: String?
    var phone_number: String?
    var registration_date: String?
    var total_usage_hours: Number?
    var total_spent: Number?
    var user_types: String?
    var avatar_path: String?
    var is_disabled: Boolean?
    var token: String?
}
interface UserDetailInfo {
    var user_id: Number
    var username: String
    var password: String
    var email: String
    var phone_number: String
    var registration_date: String
    var total_usage_hours: Number
    var total_spent: Number
    var user_types: String
    var avatar_path: String
    var is_disabled: Boolean
}
val userApi: UTSJSONObject = let {
    object : UTSJSONObject() {
        var login = fun(data: {
            var username: String
            var password: String
        }): UTSPromise<ApiResponse<LoginResponseData>> {
            return request<LoginResponseData>(UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "url",
                    "/users/login"
                ),
                utsArrayOf(
                    "method",
                    "POST"
                ),
                utsArrayOf(
                    "data",
                    data
                )
            ))));
        }
        var register = fun(data: {
            var username: String
            var password: String
            var email: String
            var phone_number: String
        }): UTSPromise<ApiResponse<RegisterResponseData>> {
            return request<RegisterResponseData>(UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "url",
                    "/users/register"
                ),
                utsArrayOf(
                    "method",
                    "POST"
                ),
                utsArrayOf(
                    "data",
                    data
                )
            ))));
        }
        var getUserDetail = fun(userId: Number): UTSPromise<ApiResponse<UserDetailInfo>> {
            return request<UserDetailInfo>(object : UTSJSONObject() {
                var url = "/users/profile/" + userId
                var method = "GET"
            });
        }
        var updateUserInfo = fun(userId: Number, data: {
            var username: String?
            var email: String?
            var phone_number: String?
            var password: String?
        }): UTSPromise<ApiResponse<UserDetailInfo>> {
            return request<UserDetailInfo>(UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "url",
                    ("/users/profile/" + userId)
                ),
                utsArrayOf(
                    "method",
                    "PATCH"
                ),
                utsArrayOf(
                    "data",
                    data
                )
            ))));
        }
        var changePassword = fun(userId: Number, data: {
            var oldPassword: String
            var newPassword: String
        }): UTSPromise<ApiResponse<UserDetailInfo>> {
            return request<UserDetailInfo>(UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "url",
                    ("/users/profile/" + userId + "/password")
                ),
                utsArrayOf(
                    "method",
                    "PATCH"
                ),
                utsArrayOf(
                    "data",
                    data
                )
            ))));
        }
        var saveUserInfo = fun(userInfo: UserInfo): Unit {
            try {
                uni_setStorageSync("userInfo", JSON.stringify(userInfo));
                console.log("完整用户信息已保存到本地存储", userInfo);
                if (userInfo.token) {
                    uni_setStorageSync("token", userInfo.token);
                    console.log("用户token已单独保存");
                }
            }
             catch (e: Throwable) {
                console.error("保存用户信息失败:", e);
            }
        }
        var getUserInfo = fun(): UserInfo? {
            try {
                val userInfoStr = uni_getStorageSync("userInfo");
                if (userInfoStr) {
                    return JSON.parse(userInfoStr);
                }
            }
             catch (e: Throwable) {
                console.error("解析用户信息失败:", e);
            }
            return null;
        }
        var getUserId = fun(defaultId: Number = 0): Number {
            val userInfo = it.getUserInfo();
            if (userInfo && (userInfo.userId || userInfo.user_id)) {
                return userInfo.userId || userInfo.user_id;
            }
            if (defaultId !== 0) {
                console.warn("\u672A\u627E\u5230\u7528\u6237ID\uFF0C\u4F7F\u7528\u9ED8\u8BA4\u503C" + defaultId);
            }
            return defaultId;
        }
        var getUsername = fun(defaultName: String = "Guest"): String {
            val userInfo = it.getUserInfo();
            if (userInfo && userInfo.username) {
                return userInfo.username;
            }
            return defaultName;
        }
        var isLoggedIn = fun(): Boolean {
            return it.getUserInfo() != null;
        }
        var logout = fun(): Unit {
            try {
                uni_removeStorageSync("userInfo");
                uni_removeStorageSync("token");
                console.log("用户已退出登录");
            }
             catch (e: Throwable) {
                console.error("退出登录失败:", e);
            }
        }
        var updateLocalUserInfo = fun(newInfo: Partial<UserInfo>): Unit {
            try {
                val currentUserInfo = it.getUserInfo();
                if (currentUserInfo) {
                    val updatedInfo = UTSJSONObject.assign<UTSJSONObject>(UTSJSONObject(), currentUserInfo, newInfo) as UTSJSONObject;
                    it.saveUserInfo(updatedInfo);
                    console.log("本地用户信息已更新");
                } else {
                    console.warn("没有找到本地用户信息，无法更新");
                }
            }
             catch (e: Throwable) {
                console.error("更新本地用户信息失败:", e);
            }
        }
    }
};
val GenPagesIndexIndexClass = CreateVueComponent(GenPagesIndexIndex::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesIndexIndex.inheritAttrs, inject = GenPagesIndexIndex.inject, props = GenPagesIndexIndex.props, propsNeedCastKeys = GenPagesIndexIndex.propsNeedCastKeys, emits = GenPagesIndexIndex.emits, components = GenPagesIndexIndex.components, styles = GenPagesIndexIndex.styles);
}
, fun(instance): GenPagesIndexIndex {
    return GenPagesIndexIndex(instance);
}
);
val orderApi: UTSJSONObject = object : UTSJSONObject() {
    var createOrder = fun(data: CreateOrderRequest): UTSPromise<ApiResponse<OrderResponse>> {
        return request<OrderResponse>(UTSJSONObject(Map<String, Any?>(utsArrayOf(
            utsArrayOf(
                "url",
                "/create_orders"
            ),
            utsArrayOf(
                "method",
                "POST"
            ),
            utsArrayOf(
                "data",
                data
            ),
            utsArrayOf(
                "needToken",
                false
            )
        ))));
    }
    var getOrderDetail = fun(orderId: Number): UTSPromise<ApiResponse<OrderDetailResponse>> {
        return request<OrderDetailResponse>(object : UTSJSONObject() {
            var url = "/orders/" + orderId + "/rent"
            var method = "GET"
            var needToken = false
        });
    }
    var getOrderInfo = fun(orderId: Number): UTSPromise<ApiResponse<OrderDetailResponse>> {
        return request<OrderDetailResponse>(object : UTSJSONObject() {
            var url = "/orders/" + orderId
            var method = "GET"
            var needToken = false
        });
    }
    var payOrder = fun(orderId: Number, couponRequest: CouponRequest?): UTSPromise<ApiResponse<PayOrderResponse>> {
        return request<PayOrderResponse>(object : UTSJSONObject() {
            var url = "/orders/" + orderId + "/pay"
            var method = "POST"
            var data = couponRequest
            var needToken = false
        });
    }
    var payOrderWithoutCoupon = fun(orderId: Number): UTSPromise<ApiResponse<PayOrderResponse>> {
        return request<PayOrderResponse>(object : UTSJSONObject() {
            var url = "/orders/" + orderId + "/pay"
            var method = "POST"
            var needToken = false
        });
    }
    var getAvailableCoupons = fun(userId: Number): UTSPromise<ApiResponse<AvailableCouponsResponse>> {
        return request<AvailableCouponsResponse>(object : UTSJSONObject() {
            var url = "/users/" + userId + "/coupons"
            var method = "GET"
            var needToken = false
        });
    }
    var getUserOrders = fun(userId: Number): UTSPromise<ApiResponse<UTSArray<OrderResponse>>> {
        return request<UTSArray<OrderResponse>>(object : UTSJSONObject() {
            var url = "/users/" + userId + "/orders"
            var method = "GET"
            var needToken = false
        });
    }
    var extendOrder = fun(data: ExtendOrderRequest): UTSPromise<ApiResponse<OrderResponse>> {
        return request<OrderResponse>(UTSJSONObject(Map<String, Any?>(utsArrayOf(
            utsArrayOf(
                "url",
                ("/orders/" + data.order_id + "/extend")
            ),
            utsArrayOf(
                "method",
                "POST"
            ),
            utsArrayOf(
                "data",
                data
            ),
            utsArrayOf(
                "needToken",
                true
            )
        ))));
    }
    var activateOrder = fun(orderId: Number): UTSPromise<ApiResponse<ChangeOrderStatusResponse>> {
        return request<ChangeOrderStatusResponse>(object : UTSJSONObject() {
            var url = "/orders/" + orderId + "/activate"
            var method = "POST"
            var needToken = true
        });
    }
    var completeOrder = fun(orderId: Number): UTSPromise<ApiResponse<ChangeOrderStatusResponse>> {
        return request<ChangeOrderStatusResponse>(object : UTSJSONObject() {
            var url = "/orders/" + orderId + "/complete"
            var method = "POST"
            var needToken = true
        });
    }
    var deleteOrder = fun(orderId: Number): UTSPromise<ApiResponse<Boolean>> {
        return request<Boolean>(object : UTSJSONObject() {
            var url = "/orders/" + orderId
            var method = "DELETE"
            var needToken = true
        });
    }
    var getAvailableTimeSlots = fun(orderId: Number): UTSPromise<ApiResponse<AvailableTimeSlotsResponse>> {
        return request<AvailableTimeSlotsResponse>(object : UTSJSONObject() {
            var url = "/orders/" + orderId + "/available-slots"
            var method = "GET"
            var needToken = true
        });
    }
    var softDeleteOrder = fun(orderId: Number): UTSPromise<ApiResponse<Boolean>> {
        return request<Boolean>(object : UTSJSONObject() {
            var url = "/orders/" + orderId + "/soft"
            var method = "DELETE"
            var needToken = true
        });
    }
};
val scooterApi: UTSJSONObject = object : UTSJSONObject() {
    var getScooters = fun(): UTSPromise<ApiResponse<UTSArray<Scooter>>> {
        return request<UTSArray<Scooter>>(object : UTSJSONObject() {
            var url = "/scooters"
            var method = "GET"
            var needToken = false
        });
    }
    var getAvailableScooters = fun(startTime: String, endTime: String): UTSPromise<ApiResponse<UTSArray<Scooter>>> {
        return request<UTSArray<Scooter>>(object : UTSJSONObject() {
            var url = "/scooters/available"
            var method = "POST"
            var data = object : UTSJSONObject() {
                var start_time = startTime
                var end_time = endTime
            }
            var needToken = false
        });
    }
    var getScooterById = fun(scooterId: Number): UTSPromise<ApiResponse<Scooter>> {
        return request<Scooter>(object : UTSJSONObject() {
            var url = "/scooters/" + scooterId
            var method = "GET"
            var needToken = false
        });
    }
};
val feedbackApi: UTSJSONObject = object : UTSJSONObject() {
    var submitFeedback = fun(data: FeedbackRequest): UTSPromise<ApiResponse<FeedbackResponse>> {
        return request<FeedbackResponse>(UTSJSONObject(Map<String, Any?>(utsArrayOf(
            utsArrayOf(
                "url",
                "/feedback"
            ),
            utsArrayOf(
                "method",
                "POST"
            ),
            utsArrayOf(
                "data",
                data
            )
        ))));
    }
};
val couponApi: UTSJSONObject = object : UTSJSONObject() {
    var getAvailableCoupons = fun(userId: Number): UTSPromise<ApiResponse<UTSArray<Coupon>>> {
        return request<UTSArray<Coupon>>(object : UTSJSONObject() {
            var url = "/users/" + userId + "/coupons"
            var method = "GET"
            var needToken = true
        });
    }
};
val creditCardApi: UTSJSONObject = object : UTSJSONObject() {
    var addCreditCard = fun(data: AddCreditCardRequest): UTSPromise<ApiResponse<AddCreditCardResponse>> {
        return request<AddCreditCardResponse>(UTSJSONObject(Map<String, Any?>(utsArrayOf(
            utsArrayOf(
                "url",
                "/bank-cards"
            ),
            utsArrayOf(
                "method",
                "POST"
            ),
            utsArrayOf(
                "data",
                data
            ),
            utsArrayOf(
                "needToken",
                true
            )
        ))));
    }
    var getUserCreditCards = fun(userId: Number): UTSPromise<ApiResponse<UTSArray<CreditCardResponse>>> {
        return request<UTSArray<CreditCardResponse>>(object : UTSJSONObject() {
            var url = "/bank-cards?userId=" + userId
            var method = "GET"
            var needToken = true
        });
    }
    var deleteCreditCard = fun(cardId: Number, userId: Number): UTSPromise<ApiResponse<String>> {
        return request<String>(object : UTSJSONObject() {
            var url = "/bank-cards/" + cardId + "?userId=" + userId
            var method = "DELETE"
            var needToken = true
        });
    }
};
val passwordApi: UTSJSONObject = object : UTSJSONObject() {
    var forgotPassword = fun(email: String): UTSPromise<ApiResponse<String>> {
        return request<String>(object : UTSJSONObject() {
            var url = "/password/forgot"
            var method = "POST"
            var data = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "email",
                    email
                )
            )))
        });
    }
    var verifyCode = fun(email: String, code: String): UTSPromise<ApiResponse<String>> {
        return request<String>(object : UTSJSONObject() {
            var url = "/password/verify-code"
            var method = "POST"
            var data = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "email",
                    email
                ),
                utsArrayOf(
                    "code",
                    code
                )
            )))
        });
    }
    var resetPassword = fun(email: String, code: String, newPassword: String): UTSPromise<ApiResponse<String>> {
        return request<String>(object : UTSJSONObject() {
            var url = "/password/reset"
            var method = "POST"
            var data = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "email",
                    email
                ),
                utsArrayOf(
                    "code",
                    code
                ),
                utsArrayOf(
                    "newPassword",
                    newPassword
                )
            )))
        });
    }
};
val notificationApi: UTSJSONObject = object : UTSJSONObject() {
    var getUserNotifications = fun(userId: Number): UTSPromise<ApiResponse<UTSArray<NotificationResponse>>> {
        return request<UTSArray<NotificationResponse>>(object : UTSJSONObject() {
            var url = "/notifications/user/" + userId
            var method = "GET"
            var needToken = true
        });
    }
    var getUserUnreadNotifications = fun(userId: Number): UTSPromise<ApiResponse<UTSArray<NotificationResponse>>> {
        return request<UTSArray<NotificationResponse>>(object : UTSJSONObject() {
            var url = "/notifications/user/" + userId + "/unread"
            var method = "GET"
            var needToken = true
        });
    }
    var countUserUnreadNotifications = fun(userId: Number): UTSPromise<ApiResponse<Number>> {
        return request<Number>(object : UTSJSONObject() {
            var url = "/notifications/user/" + userId + "/unread/count"
            var method = "GET"
            var needToken = true
        });
    }
    var markAsRead = fun(notificationId: Number, userId: Number): UTSPromise<ApiResponse<Boolean>> {
        return request<Boolean>(object : UTSJSONObject() {
            var url = "/notifications/" + notificationId + "/read"
            var method = "PATCH"
            var data = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "userId",
                    userId
                )
            )))
            var needToken = true
        });
    }
    var markAllAsRead = fun(userId: Number): UTSPromise<ApiResponse<Number>> {
        return request<Number>(object : UTSJSONObject() {
            var url = "/notifications/user/" + userId + "/read/all"
            var method = "PATCH"
            var needToken = true
        });
    }
    var deleteNotification = fun(notificationId: Number, userId: Number): UTSPromise<ApiResponse<Boolean>> {
        return request<Boolean>(object : UTSJSONObject() {
            var url = "/notifications/" + notificationId + "?userId=" + userId
            var method = "DELETE"
            var needToken = true
        });
    }
};
val GenPagesLoginLoginClass = CreateVueComponent(GenPagesLoginLogin::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesLoginLogin.inheritAttrs, inject = GenPagesLoginLogin.inject, props = GenPagesLoginLogin.props, propsNeedCastKeys = GenPagesLoginLogin.propsNeedCastKeys, emits = GenPagesLoginLogin.emits, components = GenPagesLoginLogin.components, styles = GenPagesLoginLogin.styles);
}
, fun(instance): GenPagesLoginLogin {
    return GenPagesLoginLogin(instance);
}
);
val dateUtils: UTSJSONObject = let {
    object : UTSJSONObject() {
        var formatDateForBackend = fun(dateTime: Any): String {
            val isoString = if (dateTime is Date) {
                dateTime.toISOString();
            } else {
                String(dateTime);
            }
            ;
            return isoString.replace(UTSRegExp("\\.\\d{3}Z\$", ""), "Z");
        }
        var formatDate = fun(date: Date, format: String = "yyyy-MM-dd"): String {
            val year = date.getFullYear();
            val month = (date.getMonth() + 1).toString(10).padStart(2, "0");
            val day = date.getDate().toString(10).padStart(2, "0");
            var result = format;
            result = result.replace("yyyy", year.toString(10));
            result = result.replace("MM", month);
            result = result.replace("dd", day);
            return result;
        }
        var formatTime = fun(date: Date, format: String = "HH:mm:ss"): String {
            val hours = date.getHours().toString(10).padStart(2, "0");
            val minutes = date.getMinutes().toString(10).padStart(2, "0");
            val seconds = date.getSeconds().toString(10).padStart(2, "0");
            var result = format;
            result = result.replace("HH", hours);
            result = result.replace("mm", minutes);
            result = result.replace("ss", seconds);
            return result;
        }
        var formatDateTime = fun(date: Date, format: String = "yyyy-MM-dd HH:mm:ss"): String {
            return it.formatDate(date, format.split(" ")[0]) + " " + it.formatTime(date, format.split(" ")[1]);
        }
    }
};
val GenPagesHomeHomeClass = CreateVueComponent(GenPagesHomeHome::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesHomeHome.inheritAttrs, inject = GenPagesHomeHome.inject, props = GenPagesHomeHome.props, propsNeedCastKeys = GenPagesHomeHome.propsNeedCastKeys, emits = GenPagesHomeHome.emits, components = GenPagesHomeHome.components, styles = GenPagesHomeHome.styles);
}
, fun(instance): GenPagesHomeHome {
    return GenPagesHomeHome(instance);
}
);
val GenPagesRegisterRegisterClass = CreateVueComponent(GenPagesRegisterRegister::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesRegisterRegister.inheritAttrs, inject = GenPagesRegisterRegister.inject, props = GenPagesRegisterRegister.props, propsNeedCastKeys = GenPagesRegisterRegister.propsNeedCastKeys, emits = GenPagesRegisterRegister.emits, components = GenPagesRegisterRegister.components, styles = GenPagesRegisterRegister.styles);
}
, fun(instance): GenPagesRegisterRegister {
    return GenPagesRegisterRegister(instance);
}
);
val runBlock2 = run {
    Locale.use("en-US", enUS);
}
val GenPagesOrderConfirmClass = CreateVueComponent(GenPagesOrderConfirm::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesOrderConfirm.inheritAttrs, inject = GenPagesOrderConfirm.inject, props = GenPagesOrderConfirm.props, propsNeedCastKeys = GenPagesOrderConfirm.propsNeedCastKeys, emits = GenPagesOrderConfirm.emits, components = GenPagesOrderConfirm.components, styles = GenPagesOrderConfirm.styles);
}
, fun(instance): GenPagesOrderConfirm {
    return GenPagesOrderConfirm(instance);
}
);
val runBlock3 = run {
    Locale.use("en-US", enUS1);
}
val GenPagesOrderPaymentClass = CreateVueComponent(GenPagesOrderPayment::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesOrderPayment.inheritAttrs, inject = GenPagesOrderPayment.inject, props = GenPagesOrderPayment.props, propsNeedCastKeys = GenPagesOrderPayment.propsNeedCastKeys, emits = GenPagesOrderPayment.emits, components = GenPagesOrderPayment.components, styles = GenPagesOrderPayment.styles);
}
, fun(instance): GenPagesOrderPayment {
    return GenPagesOrderPayment(instance);
}
);
val GenPagesRentDetailsRentDetailsClass = CreateVueComponent(GenPagesRentDetailsRentDetails::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesRentDetailsRentDetails.inheritAttrs, inject = GenPagesRentDetailsRentDetails.inject, props = GenPagesRentDetailsRentDetails.props, propsNeedCastKeys = GenPagesRentDetailsRentDetails.propsNeedCastKeys, emits = GenPagesRentDetailsRentDetails.emits, components = GenPagesRentDetailsRentDetails.components, styles = GenPagesRentDetailsRentDetails.styles);
}
, fun(instance): GenPagesRentDetailsRentDetails {
    return GenPagesRentDetailsRentDetails(instance);
}
);
val GenPagesLoadingLoadingClass = CreateVueComponent(GenPagesLoadingLoading::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesLoadingLoading.inheritAttrs, inject = GenPagesLoadingLoading.inject, props = GenPagesLoadingLoading.props, propsNeedCastKeys = GenPagesLoadingLoading.propsNeedCastKeys, emits = GenPagesLoadingLoading.emits, components = GenPagesLoadingLoading.components, styles = GenPagesLoadingLoading.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesLoadingLoading.setup(props as GenPagesLoadingLoading);
    }
    );
}
, fun(instance): GenPagesLoadingLoading {
    return GenPagesLoadingLoading(instance);
}
);
val GenPagesBookingRightBookingRightClass = CreateVueComponent(GenPagesBookingRightBookingRight::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesBookingRightBookingRight.inheritAttrs, inject = GenPagesBookingRightBookingRight.inject, props = GenPagesBookingRightBookingRight.props, propsNeedCastKeys = GenPagesBookingRightBookingRight.propsNeedCastKeys, emits = GenPagesBookingRightBookingRight.emits, components = GenPagesBookingRightBookingRight.components, styles = GenPagesBookingRightBookingRight.styles);
}
, fun(instance): GenPagesBookingRightBookingRight {
    return GenPagesBookingRightBookingRight(instance);
}
);
val GenPagesOrderDetailsOrderDetailsClass = CreateVueComponent(GenPagesOrderDetailsOrderDetails::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesOrderDetailsOrderDetails.inheritAttrs, inject = GenPagesOrderDetailsOrderDetails.inject, props = GenPagesOrderDetailsOrderDetails.props, propsNeedCastKeys = GenPagesOrderDetailsOrderDetails.propsNeedCastKeys, emits = GenPagesOrderDetailsOrderDetails.emits, components = GenPagesOrderDetailsOrderDetails.components, styles = GenPagesOrderDetailsOrderDetails.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesOrderDetailsOrderDetails.setup(props as GenPagesOrderDetailsOrderDetails);
    }
    );
}
, fun(instance): GenPagesOrderDetailsOrderDetails {
    return GenPagesOrderDetailsOrderDetails(instance);
}
);
val GenPagesSettingsOrdersOrdersClass = CreateVueComponent(GenPagesSettingsOrdersOrders::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesSettingsOrdersOrders.inheritAttrs, inject = GenPagesSettingsOrdersOrders.inject, props = GenPagesSettingsOrdersOrders.props, propsNeedCastKeys = GenPagesSettingsOrdersOrders.propsNeedCastKeys, emits = GenPagesSettingsOrdersOrders.emits, components = GenPagesSettingsOrdersOrders.components, styles = GenPagesSettingsOrdersOrders.styles);
}
, fun(instance): GenPagesSettingsOrdersOrders {
    return GenPagesSettingsOrdersOrders(instance);
}
);
val GenPagesSettingsCouponsCouponClass = CreateVueComponent(GenPagesSettingsCouponsCoupon::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesSettingsCouponsCoupon.inheritAttrs, inject = GenPagesSettingsCouponsCoupon.inject, props = GenPagesSettingsCouponsCoupon.props, propsNeedCastKeys = GenPagesSettingsCouponsCoupon.propsNeedCastKeys, emits = GenPagesSettingsCouponsCoupon.emits, components = GenPagesSettingsCouponsCoupon.components, styles = GenPagesSettingsCouponsCoupon.styles);
}
, fun(instance): GenPagesSettingsCouponsCoupon {
    return GenPagesSettingsCouponsCoupon(instance);
}
);
val GenPagesSettingsMySettingsMySettingsClass = CreateVueComponent(GenPagesSettingsMySettingsMySettings::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesSettingsMySettingsMySettings.inheritAttrs, inject = GenPagesSettingsMySettingsMySettings.inject, props = GenPagesSettingsMySettingsMySettings.props, propsNeedCastKeys = GenPagesSettingsMySettingsMySettings.propsNeedCastKeys, emits = GenPagesSettingsMySettingsMySettings.emits, components = GenPagesSettingsMySettingsMySettings.components, styles = GenPagesSettingsMySettingsMySettings.styles);
}
, fun(instance): GenPagesSettingsMySettingsMySettings {
    return GenPagesSettingsMySettingsMySettings(instance);
}
);
val GenPagesSettingsCardCardClass = CreateVueComponent(GenPagesSettingsCardCard::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesSettingsCardCard.inheritAttrs, inject = GenPagesSettingsCardCard.inject, props = GenPagesSettingsCardCard.props, propsNeedCastKeys = GenPagesSettingsCardCard.propsNeedCastKeys, emits = GenPagesSettingsCardCard.emits, components = GenPagesSettingsCardCard.components, styles = GenPagesSettingsCardCard.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesSettingsCardCard.setup(props as GenPagesSettingsCardCard);
    }
    );
}
, fun(instance): GenPagesSettingsCardCard {
    return GenPagesSettingsCardCard(instance);
}
);
interface UploadResponse {
    var code: Number
    var msg: String
    var data: UTSArray<String>
}
val uploadApi: UTSJSONObject = let {
    object : UTSJSONObject() {
        var uploadFeedbackImages = fun(filePaths: UTSArray<String>): UTSPromise<UTSArray<String>> {
            return wrapUTSPromise(suspend w@{
                    if (!filePaths || filePaths.length === 0) {
                        return@w utsArrayOf();
                    }
                    val uploadPromises = filePaths.map(fun(filePath): Any {
                        return it.uploadSingleImage(filePath);
                    }
                    );
                    return@w UTSPromise.all(uploadPromises);
            });
        }
        var uploadSingleImage = fun(filePath: String): UTSPromise<String> {
            return wrapUTSPromise(suspend w@{
                    return@w UTSPromise(fun(resolve, reject){
                        uni_uploadFile(UploadFileOptions(url = BASE_URL + "/upload/feedback/images", filePath = filePath, name = "files", success = fun(res){
                            try {
                                val result = JSON.parse(res.data) as UploadResponse;
                                if (result.code === 1 && result.data && result.data.length > 0) {
                                    resolve(result.data[0]);
                                } else {
                                    reject(UTSError(result.msg || "上传失败"));
                                }
                            }
                             catch (err: Throwable) {
                                reject(UTSError("解析响应失败"));
                            }
                        }
                        , fail = fun(err){
                            reject(err);
                        }
                        ));
                    }
                    );
            });
        }
        var uploadAvatar = fun(filePath: String, userId: Number): UTSPromise<Result<String>> {
            return UTSPromise(fun(resolve, reject){
                uni_uploadFile(UploadFileOptions(url = "" + BASE_URL + "/users/avatar/upload/" + userId, filePath = filePath, name = "file", success = fun(res){
                    try {
                        val result = JSON.parse(res.data) as Result<String>;
                        if (result.code === 1) {
                            val avatarUrl = if (result.data.startsWith("http")) {
                                result.data;
                            } else {
                                "" + BASE_URL + result.data;
                            }
                            ;
                            result.data = avatarUrl;
                        }
                        resolve(result);
                    }
                     catch (e: Throwable) {
                        reject(UTSError("Response failed"));
                    }
                }
                , fail = fun(err){
                    reject(UTSError("Fail to upload file: " + err.errMsg));
                }
                ));
            }
            );
        }
    }
};
val GenPagesSettingsChangeChangeClass = CreateVueComponent(GenPagesSettingsChangeChange::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesSettingsChangeChange.inheritAttrs, inject = GenPagesSettingsChangeChange.inject, props = GenPagesSettingsChangeChange.props, propsNeedCastKeys = GenPagesSettingsChangeChange.propsNeedCastKeys, emits = GenPagesSettingsChangeChange.emits, components = GenPagesSettingsChangeChange.components, styles = GenPagesSettingsChangeChange.styles);
}
, fun(instance): GenPagesSettingsChangeChange {
    return GenPagesSettingsChangeChange(instance);
}
);
val GenPagesNotificationNotificationClass = CreateVueComponent(GenPagesNotificationNotification::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesNotificationNotification.inheritAttrs, inject = GenPagesNotificationNotification.inject, props = GenPagesNotificationNotification.props, propsNeedCastKeys = GenPagesNotificationNotification.propsNeedCastKeys, emits = GenPagesNotificationNotification.emits, components = GenPagesNotificationNotification.components, styles = GenPagesNotificationNotification.styles);
}
, fun(instance): GenPagesNotificationNotification {
    return GenPagesNotificationNotification(instance);
}
);
val GenPagesHelpHelpClass = CreateVueComponent(GenPagesHelpHelp::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesHelpHelp.inheritAttrs, inject = GenPagesHelpHelp.inject, props = GenPagesHelpHelp.props, propsNeedCastKeys = GenPagesHelpHelp.propsNeedCastKeys, emits = GenPagesHelpHelp.emits, components = GenPagesHelpHelp.components, styles = GenPagesHelpHelp.styles);
}
, fun(instance): GenPagesHelpHelp {
    return GenPagesHelpHelp(instance);
}
);
val GenPagesFeedbackFeedbackClass = CreateVueComponent(GenPagesFeedbackFeedback::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesFeedbackFeedback.inheritAttrs, inject = GenPagesFeedbackFeedback.inject, props = GenPagesFeedbackFeedback.props, propsNeedCastKeys = GenPagesFeedbackFeedback.propsNeedCastKeys, emits = GenPagesFeedbackFeedback.emits, components = GenPagesFeedbackFeedback.components, styles = GenPagesFeedbackFeedback.styles);
}
, fun(instance): GenPagesFeedbackFeedback {
    return GenPagesFeedbackFeedback(instance);
}
);
val GenPagesCardsCardsClass = CreateVueComponent(GenPagesCardsCards::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesCardsCards.inheritAttrs, inject = GenPagesCardsCards.inject, props = GenPagesCardsCards.props, propsNeedCastKeys = GenPagesCardsCards.propsNeedCastKeys, emits = GenPagesCardsCards.emits, components = GenPagesCardsCards.components, styles = GenPagesCardsCards.styles);
}
, fun(instance): GenPagesCardsCards {
    return GenPagesCardsCards(instance);
}
);
val GenPagesForgetForgetClass = CreateVueComponent(GenPagesForgetForget::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesForgetForget.inheritAttrs, inject = GenPagesForgetForget.inject, props = GenPagesForgetForget.props, propsNeedCastKeys = GenPagesForgetForget.propsNeedCastKeys, emits = GenPagesForgetForget.emits, components = GenPagesForgetForget.components, styles = GenPagesForgetForget.styles);
}
, fun(instance): GenPagesForgetForget {
    return GenPagesForgetForget(instance);
}
);
val GenPagesNewPassNewPassClass = CreateVueComponent(GenPagesNewPassNewPass::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesNewPassNewPass.inheritAttrs, inject = GenPagesNewPassNewPass.inject, props = GenPagesNewPassNewPass.props, propsNeedCastKeys = GenPagesNewPassNewPass.propsNeedCastKeys, emits = GenPagesNewPassNewPass.emits, components = GenPagesNewPassNewPass.components, styles = GenPagesNewPassNewPass.styles);
}
, fun(instance): GenPagesNewPassNewPass {
    return GenPagesNewPassNewPass(instance);
}
);
val GenPagesOrderNotUsedOrderNotUsedClass = CreateVueComponent(GenPagesOrderNotUsedOrderNotUsed::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesOrderNotUsedOrderNotUsed.inheritAttrs, inject = GenPagesOrderNotUsedOrderNotUsed.inject, props = GenPagesOrderNotUsedOrderNotUsed.props, propsNeedCastKeys = GenPagesOrderNotUsedOrderNotUsed.propsNeedCastKeys, emits = GenPagesOrderNotUsedOrderNotUsed.emits, components = GenPagesOrderNotUsedOrderNotUsed.components, styles = GenPagesOrderNotUsedOrderNotUsed.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesOrderNotUsedOrderNotUsed.setup(props as GenPagesOrderNotUsedOrderNotUsed);
    }
    );
}
, fun(instance): GenPagesOrderNotUsedOrderNotUsed {
    return GenPagesOrderNotUsedOrderNotUsed(instance);
}
);
val GenPagesOrderInProgressOrderInProgressClass = CreateVueComponent(GenPagesOrderInProgressOrderInProgress::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesOrderInProgressOrderInProgress.inheritAttrs, inject = GenPagesOrderInProgressOrderInProgress.inject, props = GenPagesOrderInProgressOrderInProgress.props, propsNeedCastKeys = GenPagesOrderInProgressOrderInProgress.propsNeedCastKeys, emits = GenPagesOrderInProgressOrderInProgress.emits, components = GenPagesOrderInProgressOrderInProgress.components, styles = GenPagesOrderInProgressOrderInProgress.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesOrderInProgressOrderInProgress.setup(props as GenPagesOrderInProgressOrderInProgress);
    }
    );
}
, fun(instance): GenPagesOrderInProgressOrderInProgress {
    return GenPagesOrderInProgressOrderInProgress(instance);
}
);
val GenPagesOrderInProgressOrderInProgressHomeClass = CreateVueComponent(GenPagesOrderInProgressOrderInProgressHome::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesOrderInProgressOrderInProgressHome.inheritAttrs, inject = GenPagesOrderInProgressOrderInProgressHome.inject, props = GenPagesOrderInProgressOrderInProgressHome.props, propsNeedCastKeys = GenPagesOrderInProgressOrderInProgressHome.propsNeedCastKeys, emits = GenPagesOrderInProgressOrderInProgressHome.emits, components = GenPagesOrderInProgressOrderInProgressHome.components, styles = GenPagesOrderInProgressOrderInProgressHome.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesOrderInProgressOrderInProgressHome.setup(props as GenPagesOrderInProgressOrderInProgressHome);
    }
    );
}
, fun(instance): GenPagesOrderInProgressOrderInProgressHome {
    return GenPagesOrderInProgressOrderInProgressHome(instance);
}
);
val GenPagesOrderNotUsedOrderNotUsedHomeClass = CreateVueComponent(GenPagesOrderNotUsedOrderNotUsedHome::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesOrderNotUsedOrderNotUsedHome.inheritAttrs, inject = GenPagesOrderNotUsedOrderNotUsedHome.inject, props = GenPagesOrderNotUsedOrderNotUsedHome.props, propsNeedCastKeys = GenPagesOrderNotUsedOrderNotUsedHome.propsNeedCastKeys, emits = GenPagesOrderNotUsedOrderNotUsedHome.emits, components = GenPagesOrderNotUsedOrderNotUsedHome.components, styles = GenPagesOrderNotUsedOrderNotUsedHome.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesOrderNotUsedOrderNotUsedHome.setup(props as GenPagesOrderNotUsedOrderNotUsedHome);
    }
    );
}
, fun(instance): GenPagesOrderNotUsedOrderNotUsedHome {
    return GenPagesOrderNotUsedOrderNotUsedHome(instance);
}
);
val GenPagesOrderDetailsOrderDetailsHomeClass = CreateVueComponent(GenPagesOrderDetailsOrderDetailsHome::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesOrderDetailsOrderDetailsHome.inheritAttrs, inject = GenPagesOrderDetailsOrderDetailsHome.inject, props = GenPagesOrderDetailsOrderDetailsHome.props, propsNeedCastKeys = GenPagesOrderDetailsOrderDetailsHome.propsNeedCastKeys, emits = GenPagesOrderDetailsOrderDetailsHome.emits, components = GenPagesOrderDetailsOrderDetailsHome.components, styles = GenPagesOrderDetailsOrderDetailsHome.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesOrderDetailsOrderDetailsHome.setup(props as GenPagesOrderDetailsOrderDetailsHome);
    }
    );
}
, fun(instance): GenPagesOrderDetailsOrderDetailsHome {
    return GenPagesOrderDetailsOrderDetailsHome(instance);
}
);
val GenPagesSettingsChangePasswordChangePasswordClass = CreateVueComponent(GenPagesSettingsChangePasswordChangePassword::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesSettingsChangePasswordChangePassword.inheritAttrs, inject = GenPagesSettingsChangePasswordChangePassword.inject, props = GenPagesSettingsChangePasswordChangePassword.props, propsNeedCastKeys = GenPagesSettingsChangePasswordChangePassword.propsNeedCastKeys, emits = GenPagesSettingsChangePasswordChangePassword.emits, components = GenPagesSettingsChangePasswordChangePassword.components, styles = GenPagesSettingsChangePasswordChangePassword.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesSettingsChangePasswordChangePassword.setup(props as GenPagesSettingsChangePasswordChangePassword);
    }
    );
}
, fun(instance): GenPagesSettingsChangePasswordChangePassword {
    return GenPagesSettingsChangePasswordChangePassword(instance);
}
);
fun createApp(): UTSJSONObject {
    val app = createSSRApp(GenAppClass);
    app.use(Vant);
    return UTSJSONObject(Map<String, Any?>(utsArrayOf(
        utsArrayOf(
            "app",
            app
        )
    )));
}
fun main(app: IApp) {
    definePageRoutes();
    defineAppConfig();
    (createApp()["app"] as VueApp).mount(app, GenUniApp());
}
open class UniAppConfig : io.dcloud.uniapp.appframe.AppConfig {
    override var name: String = "demo1-0308";
    override var appid: String = "__UNI__E4E24E1";
    override var versionName: String = "1.0.0";
    override var versionCode: String = "100";
    override var uniCompilerVersion: String = "4.45";
    constructor() : super() {}
}
fun definePageRoutes() {
    __uniRoutes.push(UniPageRoute(path = "pages/index/index", component = GenPagesIndexIndexClass, meta = UniPageMeta(isQuit = true), style = utsMapOf("navigationBarTitleText" to "uni-app x", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/login/login", component = GenPagesLoginLoginClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "login", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/home/home", component = GenPagesHomeHomeClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "home", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/register/register", component = GenPagesRegisterRegisterClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "Register", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/order/confirm", component = GenPagesOrderConfirmClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "Order confirmation", "navigationBarBackgroundColor" to "#cccccc", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/order/payment", component = GenPagesOrderPaymentClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "Confirm payment", "navigationBarBackgroundColor" to "#cccccc", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/rent-details/rent-details", component = GenPagesRentDetailsRentDetailsClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "rent-details")));
    __uniRoutes.push(UniPageRoute(path = "pages/loading/loading", component = GenPagesLoadingLoadingClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/booking_right/booking_right", component = GenPagesBookingRightBookingRightClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/order_details/order_details", component = GenPagesOrderDetailsOrderDetailsClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/settings/orders/orders", component = GenPagesSettingsOrdersOrdersClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/settings/coupons/coupon", component = GenPagesSettingsCouponsCouponClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/settings/my_settings/my_settings", component = GenPagesSettingsMySettingsMySettingsClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom", "animationType" to "slide-in-bottom", "animationDuration" to 500)));
    __uniRoutes.push(UniPageRoute(path = "pages/settings/card/card", component = GenPagesSettingsCardCardClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom", "animationType" to "slide-in-top", "animationDuration" to 500)));
    __uniRoutes.push(UniPageRoute(path = "pages/settings/change/change", component = GenPagesSettingsChangeChangeClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/notification/notification", component = GenPagesNotificationNotificationClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "Notification", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/help/help", component = GenPagesHelpHelpClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "Help", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/feedback/feedback", component = GenPagesFeedbackFeedbackClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "Feedback", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/cards/cards", component = GenPagesCardsCardsClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "Cards", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/forget/forget", component = GenPagesForgetForgetClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/new_pass/new_pass", component = GenPagesNewPassNewPassClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/order_not_used/order_not_used", component = GenPagesOrderNotUsedOrderNotUsedClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/order_in_progress/order_in_progress", component = GenPagesOrderInProgressOrderInProgressClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/order_in_progress/order_in_progress_home", component = GenPagesOrderInProgressOrderInProgressHomeClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/order_not_used/order_not_used_home", component = GenPagesOrderNotUsedOrderNotUsedHomeClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/order_details/order_details_home", component = GenPagesOrderDetailsOrderDetailsHomeClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
    __uniRoutes.push(UniPageRoute(path = "pages/settings/change_password/change_password", component = GenPagesSettingsChangePasswordChangePasswordClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "navigationStyle" to "custom")));
}
val __uniLaunchPage: Map<String, Any?> = utsMapOf("url" to "pages/index/index", "style" to utsMapOf("navigationBarTitleText" to "uni-app x", "navigationStyle" to "custom"));
fun defineAppConfig() {
    __uniConfig.entryPagePath = "/pages/index/index";
    __uniConfig.globalStyle = utsMapOf("navigationBarTextStyle" to "black", "navigationBarTitleText" to "uni-app x", "navigationBarBackgroundColor" to "#F8F8F8", "backgroundColor" to "#F8F8F8");
    __uniConfig.getTabBarConfig = fun(): Map<String, Any>? {
        return null;
    }
    ;
    __uniConfig.tabBar = __uniConfig.getTabBarConfig();
    __uniConfig.conditionUrl = "";
    __uniConfig.uniIdRouter = utsMapOf();
    __uniConfig.ready = true;
}
open class GenUniApp : UniAppImpl() {
    open val vm: GenApp?
        get() {
            return getAppVm() as GenApp?;
        }
    open val `$vm`: GenApp?
        get() {
            return getAppVm() as GenApp?;
        }
}
fun getApp(): GenUniApp {
    return getUniApp() as GenUniApp;
}
