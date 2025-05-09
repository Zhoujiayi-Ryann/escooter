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
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading;
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo;
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch;
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesLoginLogin : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        return createElementVNode("view", utsMapOf("class" to "login-page"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "header-illustration"), utsArrayOf(
                createElementVNode("image", utsMapOf("class" to "illustration-img", "src" to "/static/login/login_illustration.png", "mode" to "widthFix"))
            )),
            createElementVNode("view", utsMapOf("class" to "glass-container"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "form-container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                        "input-wrapper",
                        utsMapOf("focused" to (_ctx.focusedInput === "username"))
                    ))), utsArrayOf(
                        createElementVNode("image", utsMapOf("class" to "input-icon", "src" to "/static/icons/user-solid.svg", "mode" to "aspectFit")),
                        createElementVNode("input", utsMapOf("class" to "uni-input", "modelValue" to _ctx.username, "onInput" to utsArrayOf(
                            fun(`$event`: InputEvent){
                                _ctx.username = `$event`.detail.value;
                            }
                            ,
                            _ctx.validateUsername
                        ), "type" to "text", "placeholder" to "Enter username", "onFocus" to fun(){
                            _ctx.focusedInput = "username";
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
                    if (isTrue(_ctx.usernameError)) {
                        createElementVNode("text", utsMapOf("key" to 0, "class" to "error-text"), "Username cannot exceed 15 characters.");
                    } else {
                        createCommentVNode("v-if", true);
                    }
                    ,
                    createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                        "input-wrapper",
                        utsMapOf("focused" to (_ctx.focusedInput === "password"))
                    ))), utsArrayOf(
                        createElementVNode("image", utsMapOf("class" to "input-icon", "src" to "/static/icons/lock-solid.svg", "mode" to "aspectFit")),
                        createElementVNode("input", utsMapOf("class" to "uni-input", "modelValue" to _ctx.password, "onInput" to utsArrayOf(
                            fun(`$event`: InputEvent){
                                _ctx.password = `$event`.detail.value;
                            }
                            ,
                            _ctx.validatePassword
                        ), "type" to if (_ctx.showPassword) {
                            "text";
                        } else {
                            "password";
                        }
                        , "placeholder" to "Enter password", "onFocus" to fun(){
                            _ctx.focusedInput = "password";
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
                        createElementVNode("text", utsMapOf("key" to 1, "class" to "error-text"), "Password cannot exceed 15 characters.");
                    } else {
                        createCommentVNode("v-if", true);
                    }
                    ,
                    createElementVNode("view", utsMapOf("class" to "tips-wrapper"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "tip-link", "onClick" to _ctx.goToRegister), "Sign up", 8, utsArrayOf(
                            "onClick"
                        )),
                        createElementVNode("text", utsMapOf("class" to "tip-link", "onClick" to _ctx.forgetPassword), "Forgot password?", 8, utsArrayOf(
                            "onClick"
                        ))
                    )),
                    createElementVNode("button", utsMapOf("class" to "login-btn", "onClick" to _ctx.handleLogin), " Log in ", 8, utsArrayOf(
                        "onClick"
                    ))
                ))
            ))
        ));
    }
    open var username: String by `$data`;
    open var password: String by `$data`;
    open var showPassword: Boolean by `$data`;
    open var usernameError: Boolean by `$data`;
    open var passwordError: Boolean by `$data`;
    open var focusedInput: String by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("username" to "", "password" to "", "showPassword" to false, "usernameError" to false, "passwordError" to false, "focusedInput" to "");
    }
    override fun `$initMethods`() {
        this.handleLogin = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend w@{
                    if (!this.username || !this.password) {
                        uni_showToast(ShowToastOptions(title = "Please enter username and password", icon = "none"));
                        return@w;
                    }
                    try {
                        val res = await(userApi.login(let {
                            object : UTSJSONObject() {
                                var username = it.username
                                var password = it.password
                            }
                        }));
                        if (res.code === 1) {
                            uni_showLoading(ShowLoadingOptions(title = "Logging in..."));
                            setToken(res.data.token);
                            try {
                                val detailRes = await(userApi.getUserDetail(res.data.user_id));
                                if (detailRes.code === 1) {
                                    val fullUserInfo = UTSJSONObject.assign<UTSJSONObject>(UTSJSONObject(), detailRes.data, object : UTSJSONObject() {
                                        var token = res.data.token
                                    }) as UTSJSONObject;
                                    userApi.saveUserInfo(fullUserInfo);
                                    console.log("Saved full user info:", fullUserInfo);
                                } else {
                                    val basicUserInfo: UTSJSONObject = object : UTSJSONObject() {
                                        var user_id = res.data.user_id
                                        var username = res.data.username
                                        var token = res.data.token
                                    };
                                    userApi.saveUserInfo(basicUserInfo);
                                    console.log("Failed to fetch details. Saved basic user info.");
                                }
                            } catch (detailError: Throwable) {
                                console.error("Failed to get user details:", detailError);
                                val basicUserInfo: UTSJSONObject = object : UTSJSONObject() {
                                    var user_id = res.data.user_id
                                    var username = res.data.username
                                    var token = res.data.token
                                };
                                userApi.saveUserInfo(basicUserInfo);
                                console.log("Error while fetching details. Saved basic info.");
                            }
                            uni_hideLoading();
                            uni_showToast(ShowToastOptions(title = res.msg || "Login successful", icon = "success"));
                            setTimeout(fun(){
                                uni_reLaunch(ReLaunchOptions(url = "/pages/home/home"));
                            }, 1500);
                        } else {
                            uni_showToast(ShowToastOptions(title = res.msg || "Login failed", icon = "none"));
                        }
                    }
                     catch (error: Throwable) {
                        uni_showToast(ShowToastOptions(title = "Login failed. Please try again later.", icon = "none"));
                        console.error("Login error:", error);
                    }
            });
        }
        ;
        this.togglePasswordVisibility = fun() {
            this.showPassword = !this.showPassword;
        }
        ;
        this.goToRegister = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/register/register"));
        }
        ;
        this.validateUsername = fun() {
            this.usernameError = this.username.length > 15;
        }
        ;
        this.validatePassword = fun() {
            this.passwordError = this.password.length > 15;
        }
        ;
        this.forgetPassword = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/forget/forget"));
        }
        ;
    }
    open lateinit var handleLogin: () -> UTSPromise<Unit>;
    open lateinit var togglePasswordVisibility: () -> Unit;
    open lateinit var goToRegister: () -> Unit;
    open lateinit var validateUsername: () -> Unit;
    open lateinit var validatePassword: () -> Unit;
    open lateinit var forgetPassword: () -> Unit;
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
                return utsMapOf("login-page" to padStyleMapOf(utsMapOf("background" to "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )", "display" to "flex", "flexDirection" to "column", "height" to "100%")), "header-illustration" to padStyleMapOf(utsMapOf("marginTop" to "80rpx", "display" to "flex", "justifyContent" to "center", "alignItems" to "center")), "illustration-img" to padStyleMapOf(utsMapOf("width" to "60%")), "glass-container" to padStyleMapOf(utsMapOf("marginTop" to "60rpx", "marginRight" to "auto", "marginBottom" to "auto", "marginLeft" to "auto", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "width" to "90%", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.2)", "borderRadius" to "60rpx", "boxShadow" to "0px 10px 30px rgba(0, 0, 0, 0.1)", "backdropFilter" to "blur(15px)", "textAlign" to "center")), "form-container" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "paddingTop" to 0, "paddingRight" to "20rpx", "paddingBottom" to 0, "paddingLeft" to "20rpx", "display" to "flex", "flexDirection" to "column")), "input-wrapper" to utsMapOf("" to utsMapOf("position" to "relative", "marginBottom" to "25rpx", "backgroundColor" to "rgba(255,255,255,0.6)", "boxShadow" to "0px 4px 10px rgba(0, 0, 0, 0.1)", "borderRadius" to "70rpx", "display" to "flex", "flexDirection" to "row", "alignItems" to "center", "paddingTop" to 0, "paddingRight" to "50rpx", "paddingBottom" to 0, "paddingLeft" to "50rpx", "transitionDuration" to "0.3s"), ".focused" to utsMapOf("borderWidth" to "2rpx", "borderStyle" to "solid", "borderColor" to "#007aff", "backgroundColor" to "#eaf5ff")), "input-icon" to padStyleMapOf(utsMapOf("width" to "40rpx", "height" to "40rpx", "marginRight" to "25rpx")), "uni-input" to padStyleMapOf(utsMapOf("flex" to 1, "height" to "90rpx", "fontSize" to "28rpx", "borderWidth" to "medium", "borderStyle" to "none", "borderColor" to "#000000", "backgroundColor" to "rgba(0,0,0,0)")), "eye-icon" to padStyleMapOf(utsMapOf("width" to "36rpx", "height" to "36rpx", "cursor" to "pointer", "transitionProperty" to "transform", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease-in-out", "transform:hover" to "scale(1.1)")), "error-text" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#0073f1", "marginTop" to "0rpx", "marginBottom" to "30rpx", "marginLeft" to "25rpx")), "login-btn" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "height" to "90rpx", "lineHeight" to "90rpx", "fontSize" to "30rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0px 4px 10px rgba(0, 122, 255, 0.3)", "color" to "#ffffff", "borderRadius" to "70rpx", "transitionDuration" to "0.3s", "transform:active" to "scale(0.96)")), "tips-wrapper" to padStyleMapOf(utsMapOf("marginLeft" to "50rpx", "marginRight" to "50rpx", "display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between")), "tip-link" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#666666", "transitionProperty" to "color", "transitionDuration" to "0.2s", "color:hover" to "#005bb5")), "third-party-section" to padStyleMapOf(utsMapOf("marginTop" to "auto", "marginRight" to "auto", "marginBottom" to "200rpx", "marginLeft" to "auto", "textAlign" to "center")), "third-party-text" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#999999", "marginTop" to "auto", "marginRight" to "auto", "marginBottom" to "auto", "marginLeft" to "auto")), "third-party-icons" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "display" to "flex", "flexDirection" to "row", "justifyContent" to "center", "gap" to "40rpx")), "third-party-icon" to padStyleMapOf(utsMapOf("width" to "60rpx", "height" to "60rpx", "paddingTop" to "15rpx", "paddingRight" to "15rpx", "paddingBottom" to "15rpx", "paddingLeft" to "15rpx", "backgroundColor" to "rgba(255,255,255,0.6)", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease-in-out", "transform:hover" to "scale(1.05)")), "@TRANSITION" to utsMapOf("input-wrapper" to utsMapOf("duration" to "0.3s"), "eye-icon" to utsMapOf("property" to "transform", "duration" to "0.3s", "timingFunction" to "ease-in-out"), "login-btn" to utsMapOf("duration" to "0.3s"), "tip-link" to utsMapOf("property" to "color", "duration" to "0.2s"), "third-party-icon" to utsMapOf("duration" to "0.3s", "timingFunction" to "ease-in-out")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
