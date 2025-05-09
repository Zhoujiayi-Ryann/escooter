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
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage;
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack;
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo;
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesSettingsChangeChange : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLoad(fun(_: OnLoadOptions) {
            val cachedUser = userApi.getUserInfo();
            if (cachedUser) {
                this.user = cachedUser;
                this.form.name = cachedUser.username || "";
                this.form.email = cachedUser.email || "";
                this.form.phone = cachedUser.phoneNumber || "";
                this.form.avatar = cachedUser.avatar_path || "";
            }
            this.loadUserInfo();
        }
        , __ins);
        onPageShow(fun() {
            this.loadUserInfo();
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        val _component_van_button = resolveComponent("van-button");
        return createElementVNode("view", utsMapOf("class" to "edit-profile-page"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "back-btn", "onClick" to _ctx.handleBack), utsArrayOf(
                    createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "back-icon"))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("text", utsMapOf("class" to "header-title"), "Edit Profile")
            )),
            createElementVNode("view", utsMapOf("class" to "form-item avatar-form"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "label"), "Avatar"),
                createElementVNode("view", utsMapOf("class" to "avatar-preview", "onClick" to _ctx.chooseAvatar), utsArrayOf(
                    createElementVNode("image", utsMapOf("class" to "avatar-img", "src" to _ctx.avatarSrc, "mode" to "aspectFill"), null, 8, utsArrayOf(
                        "src"
                    ))
                ), 8, utsArrayOf(
                    "onClick"
                ))
            )),
            createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                "form-item",
                utsMapOf("focused" to (_ctx.editing === "name"))
            ))), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "label"), "Name"),
                if (_ctx.editing === "name") {
                    createElementVNode("input", utsMapOf("key" to 0, "modelValue" to _ctx.form.name, "onInput" to fun(`$event`: InputEvent){
                        _ctx.form.name = `$event`.detail.value;
                    }, "class" to "input", "placeholder" to "Enter name"), null, 40, utsArrayOf(
                        "modelValue",
                        "onInput"
                    ));
                } else {
                    createElementVNode("view", utsMapOf("key" to 1, "class" to "value"), toDisplayString(_ctx.user.username), 1);
                }
                ,
                createVNode(_component_van_button, utsMapOf("size" to "small", "type" to if (_ctx.editing === "name") {
                    "success";
                } else {
                    "primary";
                }
                , "class" to "edit-btn", "onClick" to fun(){
                    _ctx.toggleEdit("name");
                }
                ), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        toDisplayString(if (_ctx.editing === "name") {
                            "Save";
                        } else {
                            "Edit";
                        }
                        )
                    );
                }
                ), "_" to 1), 8, utsArrayOf(
                    "type",
                    "onClick"
                ))
            ), 2),
            createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                "form-item",
                utsMapOf("focused" to (_ctx.editing === "email"))
            ))), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "label"), "Email"),
                if (_ctx.editing === "email") {
                    createElementVNode("input", utsMapOf("key" to 0, "modelValue" to _ctx.form.email, "onInput" to fun(`$event`: InputEvent){
                        _ctx.form.email = `$event`.detail.value;
                    }, "class" to "input", "placeholder" to "Enter email"), null, 40, utsArrayOf(
                        "modelValue",
                        "onInput"
                    ));
                } else {
                    createElementVNode("view", utsMapOf("key" to 1, "class" to "value"), toDisplayString(_ctx.user.email), 1);
                }
                ,
                createVNode(_component_van_button, utsMapOf("size" to "small", "type" to if (_ctx.editing === "email") {
                    "success";
                } else {
                    "primary";
                }
                , "class" to "edit-btn", "onClick" to fun(){
                    _ctx.toggleEdit("email");
                }
                ), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        toDisplayString(if (_ctx.editing === "email") {
                            "Save";
                        } else {
                            "Edit";
                        }
                        )
                    );
                }
                ), "_" to 1), 8, utsArrayOf(
                    "type",
                    "onClick"
                ))
            ), 2),
            createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                "form-item",
                utsMapOf("focused" to (_ctx.editing === "phone"))
            ))), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "label"), "Phone"),
                if (_ctx.editing === "phone") {
                    createElementVNode("input", utsMapOf("key" to 0, "modelValue" to _ctx.form.phone, "onInput" to fun(`$event`: InputEvent){
                        _ctx.form.phone = `$event`.detail.value;
                    }, "class" to "input", "placeholder" to "Enter phone"), null, 40, utsArrayOf(
                        "modelValue",
                        "onInput"
                    ));
                } else {
                    createElementVNode("view", utsMapOf("key" to 1, "class" to "value"), toDisplayString(_ctx.user.phoneNumber), 1);
                }
                ,
                createVNode(_component_van_button, utsMapOf("size" to "small", "type" to if (_ctx.editing === "phone") {
                    "success";
                } else {
                    "primary";
                }
                , "class" to "edit-btn", "onClick" to fun(){
                    _ctx.toggleEdit("phone");
                }
                ), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        toDisplayString(if (_ctx.editing === "phone") {
                            "Save";
                        } else {
                            "Edit";
                        }
                        )
                    );
                }
                ), "_" to 1), 8, utsArrayOf(
                    "type",
                    "onClick"
                ))
            ), 2),
            createElementVNode("view", utsMapOf("class" to "form-item"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "label"), "Password"),
                createElementVNode("view", utsMapOf("class" to "value"), "************"),
                createVNode(_component_van_button, utsMapOf("size" to "small", "type" to "primary", "class" to "edit-btn", "onClick" to _ctx.goToChangePassword), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        " Edit "
                    );
                }
                ), "_" to 1), 8, utsArrayOf(
                    "onClick"
                ))
            )),
            createElementVNode("view", utsMapOf("class" to "bottom-hint"), utsArrayOf(
                createElementVNode("text", null, "Remember to save your changes.")
            ))
        ));
    }
    open var editing: String by `$data`;
    open var user: UTSJSONObject by `$data`;
    open var avatar_path: String by `$data`;
    open var form: UTSJSONObject by `$data`;
    open var defaultAvatar: String by `$data`;
    open var avatarSrc: Any by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("editing" to "", "user" to UTSJSONObject(), "avatar_path" to "", "form" to object : UTSJSONObject() {
            var name = ""
            var email = ""
            var phone = ""
            var avatar = ""
        }, "defaultAvatar" to "/static/settings/userp.jpg", "avatarSrc" to computed<Any>(fun(): Any {
            val path = this.user.avatar;
            console.log("🖼 avatar_path is:", path);
            return if (path && path.trim() !== "") {
                path;
            } else {
                this.defaultAvatar;
            }
            ;
        }
        ));
    }
    override fun `$initMethods`() {
        this.loadUserInfo = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend w@{
                    try {
                        val userId = userApi.getUserId();
                        console.log("🧾 Retrieved userId:", userId);
                        if (!userId || userId === 0) {
                            uni_showToast(ShowToastOptions(title = "Not logged in", icon = "none"));
                            console.warn("❌ Invalid userId, might not be logged in or cached info missing");
                            return@w;
                        }
                        val res = await(userApi.getUserDetail(userId));
                        console.log("📦 User detail API response:", res);
                        if (res.code === 1 && res.data) {
                            this.user = res.data;
                            this.form.name = res.data.username || "";
                            this.form.email = res.data.email || "";
                            this.form.phone = res.data.phoneNumber || "";
                            this.form.avatar = res.data.avatar_path || "";
                            val avatarPath = res.data.avatarPath;
                            this.user.avatar = if (avatarPath && avatarPath.trim() !== "") {
                                avatarPath;
                            } else {
                                "/static/avatar/userp.jpg";
                            };
                            console.log("👤 Current user avatar path:", this.user.avatar);
                            console.log("✅ User data loaded successfully:", this.user);
                        } else {
                            console.error("❌ Failed to load, backend returned error:", res);
                            uni_showToast(ShowToastOptions(title = "Load failed: " + (res.msg || "Unknown error"), icon = "none"));
                        }
                    }
                     catch (err: Throwable) {
                        console.error("🔥 Error loading user info:", err);
                        uni_showToast(ShowToastOptions(title = "Network request failed", icon = "none"));
                    }
            });
        }
        ;
        this.chooseAvatar = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend {
                    try {
                        val res = await(uni_chooseImage(ChooseImageOptions(count = 1)));
                        val filePath = res.tempFilePaths[0];
                        val userId = userApi.getUserId();
                        val uploadRes = await(uploadApi.uploadAvatar(filePath, userId));
                        if (uploadRes.code === 1) {
                            val newAvatarUrl = uploadRes.data;
                            this.user.avatar_path = newAvatarUrl;
                            this.user.avatar = newAvatarUrl;
                            this.form.avatar = newAvatarUrl;
                            userApi.updateLocalUserInfo(object : UTSJSONObject() {
                                var avatar_path = newAvatarUrl
                            });
                            uni_showToast(ShowToastOptions(title = "Avatar updated", icon = "success"));
                        } else {
                            uni_showToast(ShowToastOptions(title = uploadRes.msg || "Avatar upload failed", icon = "none"));
                        }
                    }
                     catch (err: Throwable) {
                        console.error("❌ Avatar upload error:", err);
                        uni_showToast(ShowToastOptions(title = "Upload failed", icon = "none"));
                    }
            });
        }
        ;
        this.toggleEdit = fun(field): UTSPromise<Unit> {
            return wrapUTSPromise(suspend w@{
                    if (this.editing === field) {
                        val userId = userApi.getUserId();
                        if (field === "email" && !UTSRegExp("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$", "").test(this.form.email)) {
                            uni_showToast(ShowToastOptions(title = "Invalid email", icon = "none"));
                            return@w;
                        }
                        if (field === "phone" && !UTSRegExp("^\\d{6,15}\$", "").test(this.form.phone)) {
                            uni_showToast(ShowToastOptions(title = "Invalid phone", icon = "none"));
                            return@w;
                        }
                        val dataMap: UTSJSONObject = object : UTSJSONObject() {
                            var name = let {
                                object : UTSJSONObject() {
                                    var username = it.form.name
                                }
                            }
                            var email = let {
                                object : UTSJSONObject() {
                                    var email = it.form.email
                                }
                            }
                            var phone = let {
                                object : UTSJSONObject() {
                                    var phone_number = it.form.phone
                                }
                            }
                        };
                        try {
                            val res = await(userApi.updateUserInfo(userId, dataMap[field]));
                            console.log("🧾 Update response:", res);
                            if (res.code === 1) {
                                this.user = res.data;
                                userApi.updateLocalUserInfo(res.data);
                                uni_showToast(ShowToastOptions(title = "Updated successfully", icon = "success"));
                                this.editing = "";
                            } else {
                                val msg = res.msg?.toLowerCase() || "";
                                if (msg.includes("exists") || msg.includes("already")) {
                                    uni_showToast(ShowToastOptions(title = "This value has been used, please try another", icon = "none"));
                                } else {
                                    uni_showToast(ShowToastOptions(title = res.msg || "Update failed", icon = "none"));
                                }
                            }
                        } catch (e: Throwable) {
                            console.error("❌ Update request failed:", e);
                            uni_showToast(ShowToastOptions(title = "Request failed", icon = "none"));
                        }
                    } else {
                        this.form[field] = if (field === "name") {
                            this.user.username;
                        } else {
                            if (field === "email") {
                                this.user.email;
                            } else {
                                this.user.phoneNumber;
                            }
                            ;
                        }
                        ;
                        this.editing = field;
                    }
            });
        }
        ;
        this.goToChangePassword = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/settings/change_password/change_password"));
        }
        ;
        this.handleBack = fun() {
            val pages = getCurrentPages();
            if (pages.length > 1) {
                uni_navigateBack(null);
            } else {
                uni_reLaunch(ReLaunchOptions(url = "/pages/settings/my_settings/my_settings"));
            }
        }
        ;
        this.updateField = fun(key, value): UTSPromise<Unit> {
            return wrapUTSPromise(suspend {
                    val userId = userApi.getUserId();
                    val data: UTSJSONObject = UTSJSONObject();
                    data[key] = value;
                    val res = await(userApi.updateUserInfo(userId, data));
                    if (res.code === 1) {
                        this.user = res.data;
                    }
            });
        }
        ;
    }
    open lateinit var loadUserInfo: () -> UTSPromise<Unit>;
    open lateinit var chooseAvatar: () -> UTSPromise<Unit>;
    open lateinit var toggleEdit: (field) -> UTSPromise<Unit>;
    open lateinit var goToChangePassword: () -> Unit;
    open lateinit var handleBack: () -> Unit;
    open lateinit var updateField: (key, value) -> UTSPromise<Unit>;
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
                return utsMapOf("edit-profile-page" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "backgroundImage" to "linear-gradient(to bottom, #e0f0ff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)", "boxSizing" to "border-box")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "25rpx", "left" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "header" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "textAlign" to "center", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center")), "page-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff")), "avatar-section" to padStyleMapOf(utsMapOf("textAlign" to "center", "marginBottom" to "60rpx")), "avatar-img" to padStyleMapOf(utsMapOf("width" to "100%", "height" to "100%", "objectFit" to "cover")), "avatar-label" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#777777", "marginTop" to "12rpx")), "form-item" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "marginBottom" to "30rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderRadius" to "24rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "position" to "relative")), "label" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "fontWeight" to "bold", "marginBottom" to "10rpx", "color" to "#333333")), "value" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "color" to "#666666", "transitionProperty" to "color", "transitionDuration" to "0.3s")), "input" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "borderWidth" to "medium", "borderStyle" to "none", "borderColor" to "#000000", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#007aff", "width" to "100%", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#111111", "transitionProperty" to "borderColor", "transitionDuration" to "0.3s")), "edit-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "right" to "30rpx", "top" to "40%", "transform" to "translateY(-50%)", "fontSize" to "26rpx", "paddingTop" to 0, "paddingRight" to "20rpx", "paddingBottom" to 0, "paddingLeft" to "20rpx", "height" to "56rpx", "lineHeight" to "56rpx", "borderRadius" to "28rpx", "opacity:active" to 0.8, "transform:active" to "translateY(-50%) scale(0.97)")), "focused" to padStyleMapOf(utsMapOf("borderWidth" to "2rpx", "borderStyle" to "solid", "borderColor" to "#007aff", "backgroundColor" to "#eaf5ff", "boxShadow" to "0 0 12rpx rgba(0, 122, 255, 0.15)")), "avatar-form" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "alignItems" to "center", "justifyContent" to "space-between", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderRadius" to "24rpx", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "marginTop" to "30rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)")), "avatar-preview" to padStyleMapOf(utsMapOf("width" to "100rpx", "height" to "100rpx", "overflow" to "hidden", "flexShrink" to 0)), "bottom-hint" to padStyleMapOf(utsMapOf("width" to "100%", "textAlign" to "center", "alignItems" to "center", "fontSize" to "28rpx", "color" to "#888888", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "marginTop" to "auto")), "@TRANSITION" to utsMapOf("value" to utsMapOf("property" to "color", "duration" to "0.3s"), "input" to utsMapOf("property" to "borderColor", "duration" to "0.3s")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
