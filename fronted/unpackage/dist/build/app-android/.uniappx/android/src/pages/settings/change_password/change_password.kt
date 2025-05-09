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
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch;
import vant.showToast;
open class GenPagesSettingsChangePasswordChangePassword : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesSettingsChangePasswordChangePassword) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!;
            val _ctx = __ins.proxy as GenPagesSettingsChangePasswordChangePassword;
            val _cache = __ins.renderCache;
            val oldPassword = ref("");
            val newPassword = ref("");
            val confirmPassword = ref("");
            val showOld = ref(false);
            val showNew = ref(false);
            val showConfirm = ref(false);
            val showKeyboard = ref(false);
            val currentField = ref("");
            val loading = ref(false);
            val focus = fun(field){
                currentField.value = field;
                showKeyboard.value = true;
            }
            ;
            val toggle = fun(field){
                if (field === "old") {
                    showOld.value = !showOld.value;
                }
                if (field === "new") {
                    showNew.value = !showNew.value;
                }
                if (field === "confirm") {
                    showConfirm.value = !showConfirm.value;
                }
            }
            ;
            val onInput = fun(kVal){
                if (currentField.value === "old" && oldPassword.value.length < 15) {
                    oldPassword.value += kVal;
                }
                if (currentField.value === "new" && newPassword.value.length < 15) {
                    newPassword.value += kVal;
                }
                if (currentField.value === "confirm" && confirmPassword.value.length < 15) {
                    confirmPassword.value += kVal;
                }
            }
            ;
            val onDelete = fun(){
                if (currentField.value === "old") {
                    oldPassword.value = oldPassword.value.slice(0, -1);
                }
                if (currentField.value === "new") {
                    newPassword.value = newPassword.value.slice(0, -1);
                }
                if (currentField.value === "confirm") {
                    confirmPassword.value = confirmPassword.value.slice(0, -1);
                }
            }
            ;
            val submitChange = fun(): UTSPromise<ToastWrapperInstance | undefined> {
                return wrapUTSPromise(suspend w@{
                        if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
                            return@w showToast("Please complete all fields");
                        }
                        if (newPassword.value !== confirmPassword.value) {
                            return@w showToast("Passwords do not match");
                        }
                        val userId = userApi.getUserId();
                        if (!userId || isNaN(userId)) {
                            return@w showToast("Not logged in");
                        }
                        loading.value = true;
                        try {
                            val res = await(userApi.changePassword(userId, object : UTSJSONObject() {
                                var oldPassword = oldPassword.value
                                var newPassword = newPassword.value
                            }));
                            if (res.code === 1) {
                                showToast("Password changed successfully");
                                setTimeout(fun(){
                                    return goBack();
                                }, 1500);
                            } else {
                                showToast(res.msg || "Failed to change password");
                            }
                        }
                         catch (err: Throwable) {
                            console.error("Change password error:", err);
                            showToast("Network error");
                        }
                         finally{
                            loading.value = false;
                        }
                });
            }
            ;
            val goBack = fun(){
                val pages = getCurrentPages();
                if (pages.length > 1) {
                    uni_navigateBack(null);
                } else {
                    uni_reLaunch(ReLaunchOptions(url = "/pages/settings/change/change"));
                }
            }
            ;
            return fun(): Any? {
                val _component_van_icon = resolveComponent("van-icon");
                val _component_van_button = resolveComponent("van-button");
                val _component_van_number_keyboard = resolveComponent("van-number-keyboard");
                return createElementVNode("view", utsMapOf("class" to "change-password-page"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "close-btn", "onClick" to goBack), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "close-icon"))
                    )),
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("class" to "title-icon", "name" to "lock", "size" to "50", "color" to "#007aff")),
                        createElementVNode("text", utsMapOf("class" to "title"), "Change Password")
                    )),
                    createElementVNode("view", utsMapOf("class" to "input-card", "onClick" to fun(){
                        focus("old");
                    }
                    ), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "passed", "size" to "20")),
                        createElementVNode("input", utsMapOf("type" to if (showOld.value) {
                            "text";
                        } else {
                            "password";
                        }
                        , "modelValue" to oldPassword.value, "onInput" to fun(`$event`: InputEvent){
                            oldPassword.value = `$event`.detail.value;
                        }
                        , "placeholder" to "Enter current password", "readonly" to ""), null, 40, utsArrayOf(
                            "type",
                            "modelValue",
                            "onInput"
                        )),
                        createVNode(_component_van_icon, utsMapOf("name" to if (showOld.value) {
                            "eye-o";
                        } else {
                            "closed-eye";
                        }
                        , "onClick" to withModifiers(fun(){
                            toggle("old");
                        }
                        , utsArrayOf(
                            "stop"
                        ))), null, 8, utsArrayOf(
                            "name",
                            "onClick"
                        ))
                    ), 8, utsArrayOf(
                        "onClick"
                    )),
                    createElementVNode("view", utsMapOf("class" to "input-card", "onClick" to fun(){
                        focus("new");
                    }
                    ), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "edit", "size" to "20")),
                        createElementVNode("input", utsMapOf("type" to if (showNew.value) {
                            "text";
                        } else {
                            "password";
                        }
                        , "modelValue" to newPassword.value, "onInput" to fun(`$event`: InputEvent){
                            newPassword.value = `$event`.detail.value;
                        }
                        , "placeholder" to "Enter new password", "readonly" to ""), null, 40, utsArrayOf(
                            "type",
                            "modelValue",
                            "onInput"
                        )),
                        createVNode(_component_van_icon, utsMapOf("name" to if (showNew.value) {
                            "eye-o";
                        } else {
                            "closed-eye";
                        }
                        , "onClick" to withModifiers(fun(){
                            toggle("new");
                        }
                        , utsArrayOf(
                            "stop"
                        ))), null, 8, utsArrayOf(
                            "name",
                            "onClick"
                        ))
                    ), 8, utsArrayOf(
                        "onClick"
                    )),
                    createElementVNode("view", utsMapOf("class" to "input-card", "onClick" to fun(){
                        focus("confirm");
                    }
                    ), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "shield-o", "size" to "20")),
                        createElementVNode("input", utsMapOf("type" to if (showConfirm.value) {
                            "text";
                        } else {
                            "password";
                        }
                        , "modelValue" to confirmPassword.value, "onInput" to fun(`$event`: InputEvent){
                            confirmPassword.value = `$event`.detail.value;
                        }
                        , "placeholder" to "Confirm new password", "readonly" to ""), null, 40, utsArrayOf(
                            "type",
                            "modelValue",
                            "onInput"
                        )),
                        createVNode(_component_van_icon, utsMapOf("name" to if (showConfirm.value) {
                            "eye-o";
                        } else {
                            "closed-eye";
                        }
                        , "onClick" to withModifiers(fun(){
                            toggle("confirm");
                        }
                        , utsArrayOf(
                            "stop"
                        ))), null, 8, utsArrayOf(
                            "name",
                            "onClick"
                        ))
                    ), 8, utsArrayOf(
                        "onClick"
                    )),
                    createVNode(_component_van_button, utsMapOf("class" to "save-btn", "type" to "primary", "block" to "", "onClick" to submitChange, "loading" to loading.value), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            " Save New Password "
                        );
                    }
                    ), "_" to 1), 8, utsArrayOf(
                        "loading"
                    )),
                    createVNode(_component_van_number_keyboard, utsMapOf("show" to showKeyboard.value, "extra-key" to ".", "close-button-text" to "Done", "onInput" to onInput, "onDelete" to onDelete, "onBlur" to fun(){
                        showKeyboard.value = false;
                    }
                    ), null, 8, utsArrayOf(
                        "show",
                        "onBlur"
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
                return utsMapOf("change-password-page" to padStyleMapOf(utsMapOf("paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "backgroundImage" to "linear-gradient(to bottom, #e0f0ff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)", "display" to "flex", "flexDirection" to "column")), "close-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "50rpx", "left" to "50rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 10, "backgroundColor" to "#ffffff", "borderRadius" to "45rpx", "boxShadow" to "0 8px 20px rgba(0, 0, 0, 0.08)", "width" to "90rpx", "height" to "90rpx")), "close-icon" to padStyleMapOf(utsMapOf("fontSize" to "50rpx", "color" to "#3b88c3")), "header" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "marginTop" to 120, "marginBottom" to "100rpx", "gap" to "25rpx")), "title" to padStyleMapOf(utsMapOf("fontSize" to "60rpx", "fontWeight" to "bold", "marginLeft" to "20rpx", "color" to "#007aff")), "title-icon" to padStyleMapOf(utsMapOf("fontSize" to "60rpx", "fontWeight" to "bold", "marginLeft" to "20rpx", "color" to "#007aff")), "input-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderRadius" to "50rpx", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "display" to "flex", "flexDirection" to "row", "alignItems" to "center", "justifyContent" to "space-between", "boxShadow" to "0 8px 20px rgba(0, 0, 0, 0.08)", "marginBottom" to "30rpx")), "save-btn" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "height" to "100rpx", "fontSize" to "30rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "borderRadius" to "70rpx", "boxShadow" to "0 4px 10px rgba(0, 122, 255, 0.3)")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
