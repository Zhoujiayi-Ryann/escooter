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
open class GenPagesNewPassNewPass : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLoad(fun(options: OnLoadOptions) {
            if (options.email) {
                this.email = decodeURIComponent(options.email);
            }
            if (options.code) {
                this.code = decodeURIComponent(options.code);
            }
            if (!this.email || !this.code) {
                uni_showToast(ShowToastOptions(title = "Missing required information", icon = "none"));
                setTimeout(fun(){
                    uni_redirectTo(RedirectToOptions(url = "/pages/forget/forget"));
                }
                , 1500);
            }
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        return createElementVNode("view", utsMapOf("class" to "forget-page"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "header-illustration"), utsArrayOf(
                createElementVNode("image", utsMapOf("class" to "illustration-img", "src" to "/static/login/login_illustration.png", "mode" to "widthFix"))
            )),
            createElementVNode("view", utsMapOf("class" to "glass-container"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "form-container"), utsArrayOf(
                    createElementVNode("text", utsMapOf("class" to "form-title"), "Set a new password"),
                    createElementVNode("text", utsMapOf("class" to "form-subtitle"), "Please enter your new password"),
                    createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                        "input-wrapper",
                        utsMapOf("focused" to (_ctx.focusedInput === "password"))
                    ))), utsArrayOf(
                        createElementVNode("image", utsMapOf("class" to "input-icon", "src" to "/static/icons/unlock-solid.svg", "mode" to "aspectFit")),
                        createElementVNode("input", utsMapOf("class" to "uni-input", "modelValue" to _ctx.password, "onInput" to fun(`$event`: InputEvent){
                            _ctx.password = `$event`.detail.value;
                        }
                        , "type" to if (_ctx.showPassword) {
                            "text";
                        } else {
                            "password";
                        }
                        , "placeholder" to "Enter password", "onFocus" to fun(){
                            _ctx.focusedInput = "password";
                        }
                        , "onBlur" to fun(){
                            _ctx.focusedInput = "";
                            _ctx.checkPassword();
                        }
                        ), null, 40, utsArrayOf(
                            "modelValue",
                            "onInput",
                            "type",
                            "onFocus",
                            "onBlur"
                        )),
                        createElementVNode("image", utsMapOf("class" to "eye-icon", "src" to if (_ctx.showPassword) {
                            "/static/icons/eye-solid.svg";
                        } else {
                            "/static/icons/eye-slash-solid.svg";
                        }
                        , "mode" to "aspectFit", "onClick" to _ctx.togglePasswordVisibility), null, 8, utsArrayOf(
                            "src",
                            "onClick"
                        ))
                    ), 2),
                    if (isTrue(_ctx.passwordError)) {
                        createElementVNode("text", utsMapOf("key" to 0, "class" to "error-text"), toDisplayString(_ctx.passwordErrorMessage), 1);
                    } else {
                        createCommentVNode("v-if", true);
                    }
                    ,
                    createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                        "input-wrapper",
                        utsMapOf("focused" to (_ctx.focusedInput === "confirmPassword"))
                    ))), utsArrayOf(
                        createElementVNode("image", utsMapOf("class" to "input-icon", "src" to "/static/icons/lock-solid.svg", "mode" to "aspectFit")),
                        createElementVNode("input", utsMapOf("class" to "uni-input", "modelValue" to _ctx.confirmPassword, "onInput" to fun(`$event`: InputEvent){
                            _ctx.confirmPassword = `$event`.detail.value;
                        }
                        , "type" to if (_ctx.showConfirmPassword) {
                            "text";
                        } else {
                            "password";
                        }
                        , "placeholder" to "Confirm password", "onFocus" to fun(){
                            _ctx.focusedInput = "confirmPassword";
                        }
                        , "onBlur" to fun(){
                            _ctx.focusedInput = "";
                        }
                        ), null, 40, utsArrayOf(
                            "modelValue",
                            "onInput",
                            "type",
                            "onFocus",
                            "onBlur"
                        )),
                        createElementVNode("image", utsMapOf("class" to "eye-icon", "src" to if (_ctx.showConfirmPassword) {
                            "/static/icons/eye-solid.svg";
                        } else {
                            "/static/icons/eye-slash-solid.svg";
                        }
                        , "mode" to "aspectFit", "onClick" to _ctx.toggleConfirmPasswordVisibility), null, 8, utsArrayOf(
                            "src",
                            "onClick"
                        ))
                    ), 2),
                    if (isTrue(_ctx.confirmPasswordError)) {
                        createElementVNode("text", utsMapOf("key" to 1, "class" to "error-text"), toDisplayString(_ctx.confirmPasswordErrorMessage), 1);
                    } else {
                        createCommentVNode("v-if", true);
                    }
                    ,
                    createElementVNode("button", utsMapOf("class" to "login-btn", "onClick" to _ctx.resetPassword, "disabled" to _ctx.isLoading), toDisplayString(if (_ctx.isLoading) {
                        "Processing...";
                    } else {
                        "Confirm";
                    }
                    ), 9, utsArrayOf(
                        "onClick",
                        "disabled"
                    )),
                    createElementVNode("view", utsMapOf("class" to "tips-wrapper", "style" to normalizeStyle(utsMapOf("justify-content" to "center", "margin-top" to "40rpx"))), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "tip-link", "onClick" to _ctx.goBack), "← Go to Login", 8, utsArrayOf(
                            "onClick"
                        ))
                    ), 4)
                ))
            ))
        ));
    }
    open var email: String by `$data`;
    open var code: String by `$data`;
    open var password: String by `$data`;
    open var confirmPassword: String by `$data`;
    open var showPassword: Boolean by `$data`;
    open var showConfirmPassword: Boolean by `$data`;
    open var focusedInput: String by `$data`;
    open var passwordError: Boolean by `$data`;
    open var confirmPasswordError: Boolean by `$data`;
    open var passwordErrorMessage: String by `$data`;
    open var confirmPasswordErrorMessage: String by `$data`;
    open var isLoading: Boolean by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("email" to "", "code" to "", "password" to "", "confirmPassword" to "", "showPassword" to false, "showConfirmPassword" to false, "focusedInput" to "", "passwordError" to false, "confirmPasswordError" to false, "passwordErrorMessage" to "", "confirmPasswordErrorMessage" to "", "isLoading" to false);
    }
    override fun `$initMethods`() {
        this.togglePasswordVisibility = fun() {
            this.showPassword = !this.showPassword;
        }
        ;
        this.toggleConfirmPasswordVisibility = fun() {
            this.showConfirmPassword = !this.showConfirmPassword;
        }
        ;
        this.checkPassword = fun() {
            this.passwordError = false;
            this.passwordErrorMessage = "";
            if (this.password.length < 6) {
                this.passwordError = true;
                this.passwordErrorMessage = "Password must be at least 6 characters.";
            } else if (this.password.length > 20) {
                this.passwordError = true;
                this.passwordErrorMessage = "Password cannot exceed 20 characters.";
            }
        }
        ;
        this.resetPassword = fun() {
            this.checkPassword();
            if (this.passwordError) {
                uni_showToast(ShowToastOptions(title = "Please fix password issues", icon = "none"));
                return;
            }
            if (this.password !== this.confirmPassword) {
                this.confirmPasswordError = true;
                this.confirmPasswordErrorMessage = "Passwords do not match.";
                uni_showToast(ShowToastOptions(title = "Passwords do not match", icon = "none"));
                return;
            }
            if (!this.email || !this.code) {
                uni_showToast(ShowToastOptions(title = "Missing email or verification code", icon = "none"));
                return;
            }
            this.confirmPasswordError = false;
            this.isLoading = true;
            passwordApi.resetPassword(this.email, this.code, this.password).then(fun(res){
                if (res.code === 1) {
                    uni_showToast(ShowToastOptions(title = "Password reset successfully!", icon = "success"));
                    setTimeout(fun(){
                        return uni_redirectTo(RedirectToOptions(url = "/pages/login/login"));
                    }, 1500);
                } else {
                    uni_showToast(ShowToastOptions(title = res.msg || "Failed to reset password", icon = "none"));
                }
            }
            ).`catch`(fun(err){
                uni_showToast(ShowToastOptions(title = "Server error, please try again later", icon = "none"));
                console.error("Password reset failed:", err);
            }
            ).`finally`(fun(){
                this.isLoading = false;
            }
            );
        }
        ;
        this.goBack = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/login/login"));
        }
        ;
    }
    open lateinit var togglePasswordVisibility: () -> Unit;
    open lateinit var toggleConfirmPasswordVisibility: () -> Unit;
    open lateinit var checkPassword: () -> Unit;
    open lateinit var resetPassword: () -> Unit;
    open lateinit var goBack: () -> Unit;
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
                return utsMapOf("forget-page" to padStyleMapOf(utsMapOf("background" to "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )", "display" to "flex", "flexDirection" to "column", "height" to "100%")), "header-illustration" to padStyleMapOf(utsMapOf("marginTop" to "80rpx", "display" to "flex", "justifyContent" to "center", "alignItems" to "center")), "illustration-img" to padStyleMapOf(utsMapOf("width" to "60%")), "glass-container" to padStyleMapOf(utsMapOf("marginTop" to "60rpx", "marginRight" to "auto", "marginBottom" to "auto", "marginLeft" to "auto", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "width" to "90%", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.2)", "borderRadius" to "60rpx", "boxShadow" to "0px 10px 30px rgba(0, 0, 0, 0.1)", "backdropFilter" to "blur(15px)", "textAlign" to "center")), "form-container" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "paddingTop" to 0, "paddingRight" to "20rpx", "paddingBottom" to 0, "paddingLeft" to "20rpx", "display" to "flex", "flexDirection" to "column")), "input-wrapper" to utsMapOf("" to utsMapOf("position" to "relative", "marginBottom" to "25rpx", "backgroundColor" to "rgba(255,255,255,0.6)", "boxShadow" to "0px 4px 10px rgba(0, 0, 0, 0.1)", "borderRadius" to "70rpx", "display" to "flex", "flexDirection" to "row", "alignItems" to "center", "paddingTop" to 0, "paddingRight" to "50rpx", "paddingBottom" to 0, "paddingLeft" to "50rpx", "transitionDuration" to "0.3s"), ".focused" to utsMapOf("borderWidth" to "2rpx", "borderStyle" to "solid", "borderColor" to "#007aff", "backgroundColor" to "#eaf5ff")), "input-icon" to padStyleMapOf(utsMapOf("width" to "40rpx", "height" to "40rpx", "marginRight" to "25rpx")), "uni-input" to padStyleMapOf(utsMapOf("flex" to 1, "height" to "90rpx", "fontSize" to "28rpx", "borderWidth" to "medium", "borderStyle" to "none", "borderColor" to "#000000", "backgroundColor" to "rgba(0,0,0,0)")), "eye-icon" to padStyleMapOf(utsMapOf("width" to "36rpx", "height" to "36rpx", "cursor" to "pointer", "transitionProperty" to "transform", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease-in-out", "transform:hover" to "scale(1.1)")), "error-text" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#0073f1", "marginTop" to "0rpx", "marginBottom" to "30rpx", "marginLeft" to "25rpx")), "login-btn" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "height" to "90rpx", "lineHeight" to "90rpx", "fontSize" to "30rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0px 4px 10px rgba(0, 122, 255, 0.3)", "color" to "#ffffff", "borderRadius" to "70rpx", "transitionDuration" to "0.3s", "transform:active" to "scale(0.96)")), "tips-wrapper" to padStyleMapOf(utsMapOf("marginLeft" to "50rpx", "marginRight" to "50rpx", "display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between")), "tip-link" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#666666", "transitionProperty" to "color", "transitionDuration" to "0.2s", "color:hover" to "#005bb5")), "third-party-section" to padStyleMapOf(utsMapOf("marginTop" to "auto", "marginRight" to "auto", "marginBottom" to "200rpx", "marginLeft" to "auto", "textAlign" to "center")), "third-party-text" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#999999", "marginTop" to "auto", "marginRight" to "auto", "marginBottom" to "auto", "marginLeft" to "auto")), "third-party-icons" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "display" to "flex", "flexDirection" to "row", "justifyContent" to "center", "gap" to "40rpx")), "third-party-icon" to padStyleMapOf(utsMapOf("width" to "60rpx", "height" to "60rpx", "paddingTop" to "15rpx", "paddingRight" to "15rpx", "paddingBottom" to "15rpx", "paddingLeft" to "15rpx", "backgroundColor" to "rgba(255,255,255,0.6)", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease-in-out", "transform:hover" to "scale(1.05)")), "form-title" to padStyleMapOf(utsMapOf("fontSize" to "45rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "20rpx", "textAlign" to "center")), "form-subtitle" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#666666", "marginBottom" to "40rpx", "textAlign" to "center")), "@TRANSITION" to utsMapOf("input-wrapper" to utsMapOf("duration" to "0.3s"), "eye-icon" to utsMapOf("property" to "transform", "duration" to "0.3s", "timingFunction" to "ease-in-out"), "login-btn" to utsMapOf("duration" to "0.3s"), "tip-link" to utsMapOf("property" to "color", "duration" to "0.2s"), "third-party-icon" to utsMapOf("duration" to "0.3s", "timingFunction" to "ease-in-out")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
