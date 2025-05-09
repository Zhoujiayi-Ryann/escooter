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
open class GenPagesForgetForget : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onUnload(fun() {
            clearTimeout(this.codeExpireTimeout);
            clearInterval(this.countdownTimer);
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
                    createElementVNode("text", utsMapOf("class" to "form-title"), "Reset your password"),
                    createElementVNode("text", utsMapOf("class" to "form-subtitle"), "Enter your email to receive a reset code"),
                    createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                        "input-wrapper",
                        utsMapOf("focused" to (_ctx.focusedInput === "email"))
                    ))), utsArrayOf(
                        createElementVNode("image", utsMapOf("class" to "input-icon", "src" to "/static/icons/envelope-solid.svg", "mode" to "aspectFit")),
                        createElementVNode("input", utsMapOf("class" to "uni-input", "modelValue" to _ctx.email, "onInput" to fun(`$event`: InputEvent){
                            _ctx.email = `$event`.detail.value;
                        }
                        , "type" to "text", "placeholder" to "Enter your email", "onFocus" to fun(){
                            _ctx.focusedInput = "email";
                        }
                        , "onBlur" to fun(){
                            _ctx.focusedInput = "";
                        }
                        ), null, 40, utsArrayOf(
                            "modelValue",
                            "onInput",
                            "onFocus",
                            "onBlur"
                        ))
                    ), 2),
                    if (isTrue(_ctx.codeSent)) {
                        createElementVNode("view", utsMapOf("key" to 0, "class" to normalizeClass(utsArrayOf(
                            "input-wrapper",
                            utsMapOf("focused" to (_ctx.focusedInput === "code"))
                        ))), utsArrayOf(
                            createElementVNode("image", utsMapOf("class" to "input-icon", "src" to "/static/icons/key-solid.svg", "mode" to "aspectFit")),
                            createElementVNode("input", utsMapOf("class" to "uni-input", "modelValue" to _ctx.codeInput, "onInput" to fun(`$event`: InputEvent){
                                _ctx.codeInput = `$event`.detail.value;
                            }, "type" to "text", "placeholder" to "Enter the code", "onFocus" to fun(){
                                _ctx.focusedInput = "code";
                            }, "onBlur" to fun(){
                                _ctx.focusedInput = "";
                            }), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput",
                                "onFocus",
                                "onBlur"
                            )),
                            createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                                "resend-button",
                                utsMapOf("disabled" to (_ctx.resendCountdown > 0))
                            )), "onClick" to fun(){
                                _ctx.resendCountdown === 0 && _ctx.sendCode();
                            }), toDisplayString(if (_ctx.resendCountdown > 0) {
                                "(" + _ctx.resendCountdown + "s)";
                            } else {
                                "Resend";
                            }), 11, utsArrayOf(
                                "onClick"
                            ))
                        ), 2);
                    } else {
                        createCommentVNode("v-if", true);
                    }
                    ,
                    if (isTrue(_ctx.emailError)) {
                        createElementVNode("text", utsMapOf("key" to 1, "class" to "error-text"), toDisplayString(_ctx.emailErrorMessage), 1);
                    } else {
                        createCommentVNode("v-if", true);
                    }
                    ,
                    createElementVNode("button", utsMapOf("class" to "login-btn", "onClick" to _ctx.handleAction), toDisplayString(if (_ctx.verified) {
                        "Set New Password";
                    } else {
                        if (_ctx.codeSent) {
                            "Verify Code";
                        } else {
                            "Send Code";
                        }
                        ;
                    }
                    ), 9, utsArrayOf(
                        "onClick"
                    )),
                    createElementVNode("view", utsMapOf("class" to "tips-wrapper", "style" to normalizeStyle(utsMapOf("justify-content" to "center", "margin-top" to "40rpx"))), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "tip-link", "onClick" to _ctx.goBack), "← Back to Login", 8, utsArrayOf(
                            "onClick"
                        ))
                    ), 4)
                ))
            ))
        ));
    }
    open var email: String by `$data`;
    open var codeInput: String by `$data`;
    open var codeSent: Boolean by `$data`;
    open var emailError: Boolean by `$data`;
    open var emailErrorMessage: String by `$data`;
    open var verified: Boolean by `$data`;
    open var focusedInput: String by `$data`;
    open var resendCountdown: Number by `$data`;
    open var codeExpireTimeout: Any? by `$data`;
    open var codeExpired: Boolean by `$data`;
    open var countdownTimer: Any? by `$data`;
    open var isLoading: Boolean by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("email" to "", "codeInput" to "", "codeSent" to false, "emailError" to false, "emailErrorMessage" to "", "verified" to false, "focusedInput" to "", "resendCountdown" to 0, "codeExpireTimeout" to null, "codeExpired" to false, "countdownTimer" to null, "isLoading" to false);
    }
    override fun `$initMethods`() {
        this.handleAction = fun() {
            if (this.verified) {
                uni_navigateTo(NavigateToOptions(url = "/pages/new_pass/new_pass?email=" + encodeURIComponent(this.email) + "&code=" + encodeURIComponent(this.codeInput)));
                return;
            }
            if (!this.codeSent) {
                this.sendCode();
            } else {
                this.verifyCode();
            }
        }
        ;
        this.sendCode = fun() {
            if (!UTSRegExp("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$", "").test(this.email)) {
                this.emailError = true;
                this.emailErrorMessage = "Please enter a valid email.";
                return;
            }
            this.emailError = false;
            this.isLoading = true;
            passwordApi.forgotPassword(this.email).then(fun(res){
                if (res.code === 1) {
                    this.codeSent = true;
                    this.codeExpired = false;
                    uni_showToast(ShowToastOptions(title = "Code sent to email!", icon = "success"));
                    clearInterval(this.countdownTimer);
                    clearTimeout(this.codeExpireTimeout);
                    this.codeExpireTimeout = setTimeout(fun(){
                        this.codeExpired = true;
                    }, 120000);
                    this.resendCountdown = 60;
                    this.countdownTimer = setInterval(fun(){
                        this.resendCountdown--;
                        if (this.resendCountdown <= 0) {
                            clearInterval(this.countdownTimer);
                        }
                    }, 1000);
                } else {
                    this.emailError = true;
                    this.emailErrorMessage = res.msg || "Failed to send verification code.";
                }
            }
            ).`catch`(fun(err){
                this.emailError = true;
                this.emailErrorMessage = "Server error, please try again later.";
                console.error("Failed to send verification code:", err);
            }
            ).`finally`(fun(){
                this.isLoading = false;
            }
            );
        }
        ;
        this.verifyCode = fun() {
            if (this.codeExpired) {
                uni_showToast(ShowToastOptions(title = "Code has expired, please resend.", icon = "none"));
                return;
            }
            if (!this.codeInput || this.codeInput.length !== 6) {
                uni_showToast(ShowToastOptions(title = "Please enter a valid 6-digit code", icon = "none"));
                return;
            }
            this.isLoading = true;
            passwordApi.verifyCode(this.email, this.codeInput).then(fun(res){
                if (res.code === 1) {
                    this.verified = true;
                    uni_showToast(ShowToastOptions(title = "Verified!", icon = "success"));
                    clearTimeout(this.codeExpireTimeout);
                    clearInterval(this.countdownTimer);
                } else {
                    uni_showToast(ShowToastOptions(title = res.msg || "Invalid code", icon = "none"));
                }
            }
            ).`catch`(fun(err){
                uni_showToast(ShowToastOptions(title = "Server error, please try again later", icon = "none"));
                console.error("Verification code verification failed:", err);
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
    open lateinit var handleAction: () -> Unit;
    open lateinit var sendCode: () -> Unit;
    open lateinit var verifyCode: () -> Unit;
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
                return utsMapOf("forget-page" to padStyleMapOf(utsMapOf("background" to "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )", "display" to "flex", "flexDirection" to "column", "height" to "100%")), "header-illustration" to padStyleMapOf(utsMapOf("marginTop" to "80rpx", "display" to "flex", "justifyContent" to "center", "alignItems" to "center")), "illustration-img" to padStyleMapOf(utsMapOf("width" to "60%")), "glass-container" to padStyleMapOf(utsMapOf("marginTop" to "60rpx", "marginRight" to "auto", "marginBottom" to "auto", "marginLeft" to "auto", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "width" to "90%", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.2)", "borderRadius" to "60rpx", "boxShadow" to "0px 10px 30px rgba(0, 0, 0, 0.1)", "backdropFilter" to "blur(15px)", "textAlign" to "center")), "form-container" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "paddingTop" to 0, "paddingRight" to "20rpx", "paddingBottom" to 0, "paddingLeft" to "20rpx", "display" to "flex", "flexDirection" to "column")), "input-wrapper" to utsMapOf("" to utsMapOf("position" to "relative", "marginBottom" to "25rpx", "backgroundColor" to "rgba(255,255,255,0.6)", "boxShadow" to "0px 4px 10px rgba(0, 0, 0, 0.1)", "borderRadius" to "70rpx", "display" to "flex", "flexDirection" to "row", "alignItems" to "center", "paddingTop" to 0, "paddingRight" to "50rpx", "paddingBottom" to 0, "paddingLeft" to "50rpx", "transitionDuration" to "0.3s"), ".focused" to utsMapOf("borderWidth" to "2rpx", "borderStyle" to "solid", "borderColor" to "#007aff", "backgroundColor" to "#eaf5ff")), "input-icon" to padStyleMapOf(utsMapOf("width" to "40rpx", "height" to "40rpx", "marginRight" to "25rpx")), "uni-input" to padStyleMapOf(utsMapOf("flex" to 1, "height" to "90rpx", "fontSize" to "28rpx", "borderWidth" to "medium", "borderStyle" to "none", "borderColor" to "#000000", "backgroundColor" to "rgba(0,0,0,0)")), "eye-icon" to padStyleMapOf(utsMapOf("width" to "36rpx", "height" to "36rpx", "cursor" to "pointer", "transitionProperty" to "transform", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease-in-out", "transform:hover" to "scale(1.1)")), "error-text" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#0073f1", "marginTop" to "0rpx", "marginBottom" to "30rpx", "marginLeft" to "25rpx")), "login-btn" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "height" to "90rpx", "lineHeight" to "90rpx", "fontSize" to "30rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0px 4px 10px rgba(0, 122, 255, 0.3)", "color" to "#ffffff", "borderRadius" to "70rpx", "transitionDuration" to "0.3s", "transform:active" to "scale(0.96)")), "tips-wrapper" to padStyleMapOf(utsMapOf("marginLeft" to "50rpx", "marginRight" to "50rpx", "display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between")), "tip-link" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#666666", "transitionProperty" to "color", "transitionDuration" to "0.2s", "color:hover" to "#005bb5")), "third-party-section" to padStyleMapOf(utsMapOf("marginTop" to "auto", "marginRight" to "auto", "marginBottom" to "200rpx", "marginLeft" to "auto", "textAlign" to "center")), "third-party-text" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#999999", "marginTop" to "auto", "marginRight" to "auto", "marginBottom" to "auto", "marginLeft" to "auto")), "third-party-icons" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "display" to "flex", "flexDirection" to "row", "justifyContent" to "center", "gap" to "40rpx")), "third-party-icon" to padStyleMapOf(utsMapOf("width" to "60rpx", "height" to "60rpx", "paddingTop" to "15rpx", "paddingRight" to "15rpx", "paddingBottom" to "15rpx", "paddingLeft" to "15rpx", "backgroundColor" to "rgba(255,255,255,0.6)", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease-in-out", "transform:hover" to "scale(1.05)")), "form-title" to padStyleMapOf(utsMapOf("fontSize" to "45rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "20rpx", "textAlign" to "center")), "form-subtitle" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#666666", "marginTop" to "10rpx", "marginBottom" to "40rpx", "textAlign" to "center")), "resend-button" to utsMapOf("" to utsMapOf("fontSize" to "26rpx", "color" to "#007aff", "marginLeft" to "20rpx", "flexShrink" to 0, "paddingTop" to 0, "paddingRight" to "12rpx", "paddingBottom" to 0, "paddingLeft" to "12rpx", "height" to "60rpx", "lineHeight" to "60rpx", "backgroundColor" to "#eaf5ff", "borderRadius" to "30rpx", "transitionProperty" to "opacity", "transitionDuration" to "0.3s"), ".disabled" to utsMapOf("color" to "#aaaaaa", "backgroundColor" to "#f2f2f2", "pointerEvents" to "none")), "@TRANSITION" to utsMapOf("input-wrapper" to utsMapOf("duration" to "0.3s"), "eye-icon" to utsMapOf("property" to "transform", "duration" to "0.3s", "timingFunction" to "ease-in-out"), "login-btn" to utsMapOf("duration" to "0.3s"), "tip-link" to utsMapOf("property" to "color", "duration" to "0.2s"), "third-party-icon" to utsMapOf("duration" to "0.3s", "timingFunction" to "ease-in-out"), "resend-button" to utsMapOf("property" to "opacity", "duration" to "0.3s")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
