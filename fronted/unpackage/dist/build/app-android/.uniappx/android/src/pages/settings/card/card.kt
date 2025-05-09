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
open class GenPagesSettingsCardCard : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesSettingsCardCard) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!;
            val _ctx = __ins.proxy as GenPagesSettingsCardCard;
            val _cache = __ins.renderCache;
            val cardNumber = ref("");
            val expiry = ref("");
            val cvv = ref("");
            val showKeyboard = ref(false);
            val showCountryPicker = ref(false);
            val currentField = ref("");
            val isSaving = ref(false);
            val selectedCountry = ref(object : UTSJSONObject() {
                var flag = "🇨🇳"
                var code = "CN"
            });
            val countries = ref(utsArrayOf(
                object : UTSJSONObject() {
                    var text = "🇨🇳 CN"
                    var value = "CN"
                },
                object : UTSJSONObject() {
                    var text = "🇺🇸 US"
                    var value = "US"
                },
                object : UTSJSONObject() {
                    var text = "🇯🇵 JP"
                    var value = "JP"
                },
                object : UTSJSONObject() {
                    var text = "🇫🇷 FR"
                    var value = "FR"
                },
                object : UTSJSONObject() {
                    var text = "🇩🇪 DE"
                    var value = "DE"
                }
            ));
            val onCountryConfirm = fun(ref1){
                var selectedOptions = ref1.selectedOptions;
                if (selectedOptions && selectedOptions[0]) {
                    val code = selectedOptions[0].value;
                    val flag = selectedOptions[0].text.split(" ")[0];
                    selectedCountry.value = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                        utsArrayOf(
                            "flag",
                            flag
                        ),
                        utsArrayOf(
                            "code",
                            code
                        )
                    )));
                }
                showCountryPicker.value = false;
            }
            ;
            val focus = fun(field){
                currentField.value = field;
                showKeyboard.value = true;
            }
            ;
            val onInput = fun(kVal){
                if (currentField.value === "card" && cardNumber.value.replace(UTSRegExp("\\s", "g"), "").length < 17) {
                    cardNumber.value = formatCardNumber(cardNumber.value + kVal);
                }
                if (currentField.value === "expiry" && expiry.value.replace("/", "").length < 4) {
                    val newValue = expiry.value.replace("/", "") + kVal;
                    if (newValue.length === 2) {
                        val month = parseInt(newValue.slice(0, 2), 10);
                        if (month < 1 || month > 12) {
                            showToast("Invalid month");
                            return;
                        }
                    }
                    expiry.value = formatExpiry(newValue);
                }
                if (currentField.value === "cvv" && cvv.value.length < 3) {
                    cvv.value += kVal;
                }
            }
            ;
            val onDelete = fun(){
                if (currentField.value === "card") {
                    cardNumber.value = formatCardNumber(cardNumber.value.slice(0, -1).replace(UTSRegExp("\\s", "g"), ""));
                }
                if (currentField.value === "expiry") {
                    expiry.value = formatExpiry(expiry.value.replace("/", "").slice(0, -1));
                }
                if (currentField.value === "cvv") {
                    cvv.value = cvv.value.slice(0, -1);
                }
            }
            ;
            val formatCardNumber = fun(kVal): Any {
                return kVal.replace(UTSRegExp("\\D", "g"), "").slice(0, 16).replace(UTSRegExp("(\\d{4})(?=\\d)", "g"), "\$1 ").trim();
            }
            ;
            val formatExpiry = fun(kVal): Any {
                return kVal.replace(UTSRegExp("\\D", "g"), "").slice(0, 4).replace(UTSRegExp("^(\\d{2})(\\d{0,2})\$", ""), fun(_, m, y){
                    return if (y) {
                        "" + m + "/" + y;
                    } else {
                        m;
                    }
                    ;
                }
                );
            }
            ;
            val onSave = fun(){
                if (!cardNumber.value || !expiry.value || !cvv.value) {
                    return showToast("Please complete all fields");
                }
                val cardNumberClean = cardNumber.value.replace(UTSRegExp("\\s", "g"), "");
                if (cardNumberClean.length !== 16 || !UTSRegExp("^\\d+\$", "").test(cardNumberClean)) {
                    return showToast("Invalid card number");
                }
                val expiryRaw = expiry.value.replace("/", "");
                val month = parseInt(expiryRaw.slice(0, 2), 10);
                val year = parseInt("20" + expiryRaw.slice(2), 10);
                if (month < 1 || month > 12) {
                    return showToast("Expiry month invalid");
                }
                if (expiryRaw.length !== 4) {
                    return showToast("Expiry date incomplete");
                }
                if (cvv.value.length !== 3 || !UTSRegExp("^\\d+\$", "").test(cvv.value)) {
                    return showToast("Invalid CVV");
                }
                val expiryDate = "" + year + "-" + month.toString(10).padStart(2, "0") + "-01";
                val userId = userApi.getUserId(1);
                val cardData: UTSJSONObject = object : UTSJSONObject() {
                    var user_id = userId
                    var card_number = cardNumberClean
                    var security_code = cvv.value
                    var expiry_date = expiryDate
                    var country = selectedCountry.value.code
                };
                isSaving.value = true;
                creditCardApi.addCreditCard(cardData).then(fun(res){
                    if (res.code === 1) {
                        showToast("Card added successfully");
                        setTimeout(fun(){
                            uni_navigateBack(null);
                        }, 1500);
                    } else {
                        showToast(res.msg || "Failed to add card");
                    }
                }
                ).`catch`(fun(err){
                    console.error("Failed to add bank card:", err);
                    showToast("Network error, please try again");
                }
                ).`finally`(fun(){
                    isSaving.value = false;
                }
                );
            }
            ;
            val onClose = fun(){
                val pages = getCurrentPages();
                if (pages.length > 1) {
                    uni_navigateBack(null);
                } else {
                    uni_reLaunch(ReLaunchOptions(url = "/pages/home/home"));
                }
            }
            ;
            return fun(): Any? {
                val _component_van_icon = resolveComponent("van-icon");
                val _component_van_button = resolveComponent("van-button");
                val _component_van_picker = resolveComponent("van-picker");
                val _component_van_popup = resolveComponent("van-popup");
                val _component_van_number_keyboard = resolveComponent("van-number-keyboard");
                return createElementVNode("view", utsMapOf("class" to "card-page"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "close-btn", "onClick" to onClose), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "cross", "class" to "close-icon"))
                    )),
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "credit-pay", "size" to "30", "color" to "#007aff")),
                        createElementVNode("text", utsMapOf("class" to "title"), "Add Bank Card ")
                    )),
                    createElementVNode("view", utsMapOf("class" to "input-card", "onClick" to fun(){
                        focus("card");
                    }
                    ), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "credit-pay", "size" to "20")),
                        createElementVNode("input", utsMapOf("modelValue" to cardNumber.value, "onInput" to fun(`$event`: InputEvent){
                            cardNumber.value = `$event`.detail.value;
                        }
                        , "placeholder" to "Credit Card Number", "readonly" to ""), null, 40, utsArrayOf(
                            "modelValue",
                            "onInput"
                        ))
                    ), 8, utsArrayOf(
                        "onClick"
                    )),
                    createElementVNode("view", utsMapOf("class" to "input-row"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "input-half", "onClick" to fun(){
                            focus("expiry");
                        }
                        ), utsArrayOf(
                            createElementVNode("input", utsMapOf("modelValue" to expiry.value, "onInput" to fun(`$event`: InputEvent){
                                expiry.value = `$event`.detail.value;
                            }
                            , "placeholder" to "MM/YY", "readonly" to ""), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            ))
                        ), 8, utsArrayOf(
                            "onClick"
                        )),
                        createElementVNode("view", utsMapOf("class" to "input-half"), utsArrayOf(
                            createElementVNode("input", utsMapOf("modelValue" to cvv.value, "onInput" to fun(`$event`: InputEvent){
                                cvv.value = `$event`.detail.value;
                            }
                            , "placeholder" to "CVV", "readonly" to "", "maxlength" to "3", "onClick" to fun(){
                                focus("cvv");
                            }
                            ), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput",
                                "onClick"
                            )),
                            createElementVNode("view", utsMapOf("class" to "country", "onClick" to fun(){
                                showCountryPicker.value = true;
                            }
                            ), utsArrayOf(
                                createElementVNode("text", null, toDisplayString(selectedCountry.value.flag) + " " + toDisplayString(selectedCountry.value.code), 1)
                            ), 8, utsArrayOf(
                                "onClick"
                            ))
                        ))
                    )),
                    createVNode(_component_van_button, utsMapOf("class" to "save-btn", "type" to "primary", "block" to "", "onClick" to onSave, "loading" to isSaving.value), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            " Save "
                        );
                    }
                    ), "_" to 1), 8, utsArrayOf(
                        "loading"
                    )),
                    createVNode(_component_van_popup, utsMapOf("show" to showCountryPicker.value, "onUpdate:show" to fun(`$event`){
                        showCountryPicker.value = `$event`;
                    }
                    , "position" to "bottom"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            createVNode(_component_van_picker, utsMapOf("show-toolbar" to "", "title" to "Select Country", "columns" to countries.value, "columns-field-names" to object : UTSJSONObject() {
                                var text = "text"
                                var value = "value"
                            }, "onConfirm" to onCountryConfirm, "onCancel" to fun(){
                                showCountryPicker.value = false;
                            }
                            ), null, 8, utsArrayOf(
                                "columns",
                                "onCancel"
                            ))
                        );
                    }
                    ), "_" to 1), 8, utsArrayOf(
                        "show",
                        "onUpdate:show"
                    )),
                    createVNode(_component_van_number_keyboard, utsMapOf("show" to showKeyboard.value, "extra-key" to ".", "close-button-text" to "Finish", "onBlur" to fun(){
                        showKeyboard.value = false;
                    }
                    , "onInput" to onInput, "onDelete" to onDelete), null, 8, utsArrayOf(
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
                return utsMapOf("card-page" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#f8fbff", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "boxSizing" to "border-box", "display" to "flex", "flexDirection" to "column", "gap" to "15rpx")), "close-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "50rpx", "left" to "50rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "borderRadius" to "45rpx", "backgroundColor" to "#ffffff", "boxShadow" to "0 8px 20px rgba(0, 0, 0, 0.08)", "width" to "90rpx", "height" to "90rpx")), "close-icon" to padStyleMapOf(utsMapOf("fontSize" to "50rpx", "color" to "#3b88c3")), "header" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "marginTop" to 150, "marginBottom" to "100rpx")), "title" to padStyleMapOf(utsMapOf("fontSize" to "60rpx", "fontWeight" to "bold", "marginLeft" to "20rpx", "alignItems" to "center", "textAlign" to "center", "color" to "#007aff")), "input-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderRadius" to "50rpx", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "display" to "flex", "flexDirection" to "row", "alignItems" to "center", "justifyContent" to "space-between", "marginBottom" to "20rpx", "height" to 60, "boxShadow" to "0 8px 20px rgba(0, 0, 0, 0.08)")), "input-row" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "marginBottom" to "40rpx", "borderRadius" to "50rpx", "height" to 60, "boxShadow" to "0 8px 20px rgba(0, 0, 0, 0.08)")), "input-half" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderRadius" to "10rpx", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "width" to "49%", "display" to "flex", "flexDirection" to "row", "alignItems" to "center")), "country" to padStyleMapOf(utsMapOf("paddingLeft" to "15rpx", "fontSize" to "30rpx", "cursor" to "pointer")), "save-btn" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "height" to "100rpx", "lineHeight" to "90rpx", "fontSize" to "30rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0px 4px 10px rgba(0, 122, 255, 0.3)", "color" to "#ffffff", "borderRadius" to "70rpx", "transitionDuration" to "0.3s", "transform:active" to "scale(0.96)")), "@TRANSITION" to utsMapOf("save-btn" to utsMapOf("duration" to "0.3s")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
