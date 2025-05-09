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
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading;
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack;
import io.dcloud.uniapp.extapi.previewImage as uni_previewImage;
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch;
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading;
import io.dcloud.uniapp.extapi.showModal as uni_showModal;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesFeedbackFeedback : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        val _component_picker = resolveComponent("picker");
        return createElementVNode("view", utsMapOf("class" to "feedback-container"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "back-btn", "onClick" to _ctx.handleBack), utsArrayOf(
                    createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "back-icon"))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("text", utsMapOf("class" to "header-title"), "Submit Feedback")
            )),
            createElementVNode("view", utsMapOf("class" to "section"), utsArrayOf(
                createVNode(_component_picker, utsMapOf("onChange" to _ctx.onTypeChange, "value" to _ctx.typeIndex, "range" to _ctx.typeArray), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "label"), "Feedback Type"),
                        createElementVNode("view", utsMapOf("class" to "picker"), utsArrayOf(
                            toDisplayString(_ctx.typeArray[_ctx.typeIndex]) + " ",
                            createElementVNode("text", utsMapOf("class" to "picker-arrow"), "▼")
                        ))
                    );
                }
                ), "_" to 1), 8, utsArrayOf(
                    "onChange",
                    "value",
                    "range"
                ))
            )),
            createElementVNode("view", utsMapOf("class" to "section"), utsArrayOf(
                createElementVNode("text", utsMapOf("class" to "label"), "Description"),
                createElementVNode("textarea", utsMapOf("modelValue" to _ctx.description, "onInput" to fun(`$event`: InputEvent){
                    _ctx.description = `$event`.detail.value;
                }
                , "placeholder" to "Please explain your problems", "class" to "textarea"), null, 40, utsArrayOf(
                    "modelValue",
                    "onInput"
                )),
                createElementVNode("view", utsMapOf("class" to "image-upload"), utsArrayOf(
                    createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.images, fun(item, index, __index, _cached): Any {
                        return createElementVNode("view", utsMapOf("key" to index, "class" to "image-wrapper"), utsArrayOf(
                            createElementVNode("image", utsMapOf("src" to item, "class" to "preview-image", "mode" to "aspectFill", "onClick" to fun(){
                                _ctx.previewImage(index);
                            }
                            ), null, 8, utsArrayOf(
                                "src",
                                "onClick"
                            )),
                            createElementVNode("view", utsMapOf("class" to "delete-btn", "onClick" to withModifiers(fun(){
                                _ctx.deleteImage(index);
                            }
                            , utsArrayOf(
                                "stop"
                            ))), "×", 8, utsArrayOf(
                                "onClick"
                            ))
                        ));
                    }
                    ), 128),
                    if (_ctx.images.length < 3) {
                        createElementVNode("view", utsMapOf("key" to 0, "class" to "upload-btn", "onClick" to _ctx.chooseImage), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "upload-icon"), "+")
                        ), 8, utsArrayOf(
                            "onClick"
                        ));
                    } else {
                        createCommentVNode("v-if", true);
                    }
                ))
            )),
            createElementVNode("view", utsMapOf("class" to "section"), utsArrayOf(
                createElementVNode("text", utsMapOf("class" to "label"), "Happening Time (optimal)"),
                createVNode(_component_picker, utsMapOf("mode" to "date", "value" to _ctx.date, "onChange" to _ctx.onDateChange), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                    return utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "picker"), utsArrayOf(
                            toDisplayString(_ctx.date || "Please select time") + " ",
                            createElementVNode("text", utsMapOf("class" to "picker-arrow"), "▼")
                        ))
                    );
                }
                ), "_" to 1), 8, utsArrayOf(
                    "value",
                    "onChange"
                ))
            )),
            createElementVNode("view", utsMapOf("class" to "section"), utsArrayOf(
                createElementVNode("text", utsMapOf("class" to "label"), "Relevant Bill (optimal)"),
                createElementVNode("input", utsMapOf("type" to "text", "modelValue" to _ctx.orderNumber, "onInput" to fun(`$event`: InputEvent){
                    _ctx.orderNumber = `$event`.detail.value;
                }
                , "placeholder" to "Please input bill number", "class" to "input"), null, 40, utsArrayOf(
                    "modelValue",
                    "onInput"
                ))
            )),
            createElementVNode("button", utsMapOf("class" to "submit-btn", "onClick" to _ctx.submitFeedback), "Submit Feedback", 8, utsArrayOf(
                "onClick"
            ))
        ));
    }
    open var typeArray: UTSArray<String> by `$data`;
    open var typeIndex: Number by `$data`;
    open var description: String by `$data`;
    open var images: UTSArray<Any?> by `$data`;
    open var date: String by `$data`;
    open var orderNumber: String by `$data`;
    open var isSubmitting: Boolean by `$data`;
    open var uploadedImageUrls: UTSArray<Any?> by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("typeArray" to utsArrayOf(
            "Using Problems",
            "Experience Feedback"
        ), "typeIndex" to 0, "description" to "", "images" to utsArrayOf(), "date" to "", "orderNumber" to "", "isSubmitting" to false, "uploadedImageUrls" to utsArrayOf());
    }
    override fun `$initMethods`() {
        this.onTypeChange = fun(e) {
            this.typeIndex = e.detail.value;
        }
        ;
        this.onDateChange = fun(e) {
            this.date = e.detail.value;
        }
        ;
        this.chooseImage = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend {
                    try {
                        val res = await(uni_chooseImage(ChooseImageOptions(count = 3 - this.images.length, sizeType = utsArrayOf(
                            "compressed"
                        ), sourceType = utsArrayOf(
                            "album",
                            "camera"
                        ))));
                        this.images = this.images.concat(res.tempFilePaths);
                    }
                     catch (err: Throwable) {
                        console.error("Failed to choose image:", err);
                    }
            });
        }
        ;
        this.previewImage = fun(index) {
            uni_previewImage(PreviewImageOptions(urls = this.images, current = index));
        }
        ;
        this.deleteImage = fun(index) {
            uni_showModal(ShowModalOptions(title = "Confirm Deletion", content = "Are you sure you want to delete this image?", success = fun(res){
                if (res.confirm) {
                    this.images.splice(index, 1);
                    if (this.uploadedImageUrls.length > index) {
                        this.uploadedImageUrls.splice(index, 1);
                    }
                }
            }
            ));
        }
        ;
        this.submitFeedback = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend w@{
                    if (!this.description) {
                        uni_showToast(ShowToastOptions(title = "Please describe the issue", icon = "none"));
                        return@w;
                    }
                    if (this.isSubmitting) {
                        return@w;
                    }
                    this.isSubmitting = true;
                    uni_showLoading(ShowLoadingOptions(title = "Submitting..."));
                    try {
                        if (this.images.length > 0) {
                            try {
                                uni_showLoading(ShowLoadingOptions(title = "Uploading images..."));
                                this.uploadedImageUrls = await(uploadApi.uploadFeedbackImages(this.images));
                                console.log("Uploaded images:", this.uploadedImageUrls);
                            }
                             catch (error: Throwable) {
                                console.error("Image upload failed:", error);
                                uni_hideLoading();
                                uni_showToast(ShowToastOptions(title = "Failed to upload images", icon = "none"));
                                this.uploadedImageUrls = utsArrayOf();
                            }
                        }
                        val userId = userApi.getUserId();
                        if (!userId) {
                            throw UTSError("User ID not found. Please log in.");
                        }
                        val feedbackData: UTSJSONObject = let {
                            object : UTSJSONObject() {
                                var user_id = userId
                                var feedback_type = if (it.typeIndex === 0) {
                                    FeedbackType.USING_PROBLEM;
                                } else {
                                    FeedbackType.EXPERIENCE_FEEDBACK;
                                }
                                var description = it.description
                                var bill_number = it.orderNumber || undefined
                            }
                        };
                        if (this.date) {
                            feedbackData["happening_time"] = this.date;
                        }
                        if (this.uploadedImageUrls.length > 0) {
                            feedbackData["image_urls"] = this.uploadedImageUrls;
                        }
                        console.log("Submitting feedback data:", feedbackData);
                        uni_showLoading(ShowLoadingOptions(title = "Submitting feedback..."));
                        val response = await(feedbackApi.submitFeedback(feedbackData));
                        uni_hideLoading();
                        if (response.code === 1) {
                            uni_showToast(ShowToastOptions(title = "Submitted successfully!", icon = "success"));
                            this.description = "";
                            this.images = utsArrayOf();
                            this.uploadedImageUrls = utsArrayOf();
                            this.date = "";
                            this.orderNumber = "";
                            setTimeout(fun(){
                                uni_navigateBack(null);
                            }, 1500);
                        } else {
                            throw UTSError(response.msg || "Submission failed");
                        }
                    }
                     catch (err: Throwable) {
                        console.error("Feedback submission failed:", err);
                        uni_hideLoading();
                        uni_showToast(ShowToastOptions(title = err.message || "Submission failed", icon = "none"));
                    }
                     finally{
                        this.isSubmitting = false;
                    }
            });
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
    }
    open lateinit var onTypeChange: (e) -> Unit;
    open lateinit var onDateChange: (e) -> Unit;
    open lateinit var chooseImage: () -> UTSPromise<Unit>;
    open lateinit var previewImage: (index) -> Unit;
    open lateinit var deleteImage: (index) -> Unit;
    open lateinit var submitFeedback: () -> UTSPromise<Unit>;
    open lateinit var handleBack: () -> Unit;
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
                return utsMapOf("feedback-container" to padStyleMapOf(utsMapOf("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "backgroundImage" to "linear-gradient(to bottom, #e0f0ff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)", "display" to "flex", "flexDirection" to "column", "gap" to "18rpx")), "section" to padStyleMapOf(utsMapOf("marginBottom" to 20, "backgroundImage" to "none", "backgroundColor" to "#ffffff", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderRadius" to "24rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)")), "header" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "textAlign" to "center", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "position" to "relative", "marginBottom" to "30rpx")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "25rpx", "left" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center")), "label" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "color" to "#333333", "marginBottom" to "20rpx", "fontWeight" to "bold")), "picker" to padStyleMapOf(utsMapOf("height" to 40, "lineHeight" to "40px", "borderWidth" to "medium", "borderStyle" to "none", "borderColor" to "#000000", "borderRadius" to 4, "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "position" to "relative", "fontSize" to "30rpx", "backgroundColor" to "rgba(0,0,0,0)", "width" to "100%", "color" to "#111111", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#cccccc")), "picker-arrow" to padStyleMapOf(utsMapOf("position" to "absolute", "right" to 10, "color" to "#999999")), "textarea" to padStyleMapOf(utsMapOf("width" to "100%", "height" to "150rpx", "borderWidth" to "medium", "borderStyle" to "none", "borderColor" to "#000000", "borderRadius" to "12rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "boxSizing" to "border-box", "fontSize" to "30rpx", "backgroundColor" to "#f9f9f9", "color" to "#111111", "resize" to "none", "lineHeight" to 1.6)), "image-upload" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "flexWrap" to "nowrap", "marginTop" to "20rpx", "gap" to "20rpx", "overflowX" to "auto", "paddingBottom" to 5)), "image-wrapper" to padStyleMapOf(utsMapOf("position" to "relative", "width" to "160rpx", "height" to "160rpx", "overflow" to "hidden", "flexShrink" to 0, "borderRadius" to "12rpx", "boxShadow" to "0 2px 6px rgba(0, 0, 0, 0.1)")), "preview-image" to padStyleMapOf(utsMapOf("width" to "100%", "height" to "100%", "borderRadius" to 4, "objectFit" to "cover")), "delete-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "6rpx", "right" to "6rpx", "width" to "40rpx", "height" to "40rpx", "backgroundColor" to "rgba(0,0,0,0.6)", "color" to "#FFFFFF", "fontSize" to 18, "fontWeight" to "bold", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 10, "boxShadow" to "0 1px 3px rgba(0, 0, 0, 0.3)", "backgroundImage" to "none", "textAlign" to "center", "lineHeight" to "40rpx")), "upload-btn" to padStyleMapOf(utsMapOf("width" to "160rpx", "height" to "160rpx", "borderWidth" to "2rpx", "borderStyle" to "dashed", "borderColor" to "#cccccc", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "borderRadius" to "12rpx", "backgroundColor" to "#f4f4f4", "fontSize" to "50rpx", "color" to "#888888")), "upload-icon" to padStyleMapOf(utsMapOf("fontSize" to 30, "color" to "#999999")), "input" to padStyleMapOf(utsMapOf("height" to 40, "borderWidth" to "medium", "borderStyle" to "none", "borderColor" to "#000000", "borderRadius" to 4, "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "fontSize" to "30rpx", "backgroundColor" to "rgba(0,0,0,0)", "width" to "100%", "color" to "#111111", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#cccccc")), "submit-btn" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#ffffff", "borderRadius" to "70rpx", "height" to "90rpx", "lineHeight" to "90rpx", "fontSize" to "32rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "boxShadow" to "0px 4px 10px rgba(0, 122, 255, 0.3)")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
