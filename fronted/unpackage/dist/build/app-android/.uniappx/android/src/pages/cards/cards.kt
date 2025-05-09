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
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack;
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo;
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch;
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading;
import io.dcloud.uniapp.extapi.showModal as uni_showModal;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesCardsCards : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onPageShow(fun() {
            this.fetchCards();
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        return createElementVNode("view", utsMapOf("class" to "cards-container"), utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "back-btn", "onClick" to _ctx.handleBack), utsArrayOf(
                    createVNode(_component_van_icon, utsMapOf("name" to "arrow-left", "class" to "back-icon"))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("text", utsMapOf("class" to "header-title"), "Cards")
            )),
            if (isTrue(_ctx.loading)) {
                createElementVNode("view", utsMapOf("key" to 0, "class" to "loading-container"), utsArrayOf(
                    createElementVNode("text", utsMapOf("class" to "loading-text"), "Loading...")
                ));
            } else {
                if (isTrue(_ctx.cards && _ctx.cards.length > 0)) {
                    createElementVNode("view", utsMapOf("key" to 1, "class" to "card-list"), utsArrayOf(
                        createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.cards, fun(card, index, __index, _cached): Any {
                            return createElementVNode("view", utsMapOf("key" to index, "class" to "card-item"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "card-info"), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "card-country"), toDisplayString(card.country), 1),
                                    createElementVNode("view", utsMapOf("class" to "card-number-container"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "card-number"), toDisplayString(_ctx.formatCardNumber(card.card_number)), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "card-expires"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "expires-label"), "Valid Thru:"),
                                        createElementVNode("text", utsMapOf("class" to "expires-date"), toDisplayString(_ctx.formatExpiryDate(card.expiry_date)), 1)
                                    ))
                                )),
                                createElementVNode("view", utsMapOf("class" to "delete-btn", "onClick" to withModifiers(fun(){
                                    _ctx.deleteCard(card.card_id);
                                }, utsArrayOf(
                                    "stop"
                                ))), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "delete-icon"), "×")
                                ), 8, utsArrayOf(
                                    "onClick"
                                ))
                            ));
                        }), 128)
                    ));
                } else {
                    createElementVNode("view", utsMapOf("key" to 2, "class" to "empty-state"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "empty-icon"), "💳"),
                        createElementVNode("text", utsMapOf("class" to "empty-text"), "No cards found"),
                        createElementVNode("text", utsMapOf("class" to "empty-subtext"), "Add a card to enable payment")
                    ));
                }
                ;
            }
            ,
            createElementVNode("view", utsMapOf("class" to "add-card", "onClick" to _ctx.addNewCard), utsArrayOf(
                createElementVNode("text", utsMapOf("class" to "add-icon"), "+"),
                createElementVNode("text", utsMapOf("class" to "add-text"), "Add New Card")
            ), 8, utsArrayOf(
                "onClick"
            ))
        ));
    }
    open var cards: UTSArray<Any?> by `$data`;
    open var loading: Boolean by `$data`;
    open var error: Any? by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("cards" to utsArrayOf(), "loading" to true, "error" to null);
    }
    override fun `$initMethods`() {
        this.fetchCards = fun() {
            this.loading = true;
            this.error = null;
            val userId = userApi.getUserId();
            if (userId === 0) {
                uni_navigateTo(NavigateToOptions(url = "/pages/login/login"));
                return;
            }
            creditCardApi.getUserCreditCards(userId).then(fun(res){
                this.loading = false;
                if (res.code === 1) {
                    this.cards = res.data;
                    console.log("Successfully obtained bank card:", this.cards);
                } else {
                    this.error = res.message || "Failed to obtain bank card";
                    uni_showToast(ShowToastOptions(title = this.error, icon = "none"));
                }
            }
            ).`catch`(fun(err){
                this.loading = false;
                this.error = "Network error, please try again later";
                uni_showToast(ShowToastOptions(title = this.error, icon = "none"));
                console.error("Error in obtaining bank card:", err);
            }
            );
        }
        ;
        this.addNewCard = fun() {
            uni_navigateTo(NavigateToOptions(url = "/pages/settings/card/card"));
        }
        ;
        this.deleteCard = fun(cardId) {
            uni_showModal(ShowModalOptions(title = "Delete Card", content = "Are you sure you want to delete this card?？", confirmText = "Confirm", cancelText = "Cancel", success = fun(res){
                if (res.confirm) {
                    val userId = userApi.getUserId();
                    uni_showLoading(ShowLoadingOptions(title = "Deleting..."));
                    creditCardApi.deleteCreditCard(cardId, userId).then(fun(res){
                        uni_hideLoading();
                        if (res.code === 1) {
                            uni_showToast(ShowToastOptions(title = "Delete Success", icon = "success"));
                            this.fetchCards();
                        } else {
                            uni_showToast(ShowToastOptions(title = res.message || "Delete Fail", icon = "none"));
                        }
                    }
                    ).`catch`(fun(err){
                        uni_hideLoading();
                        uni_showToast(ShowToastOptions(title = "Network error, please try again later", icon = "none"));
                        console.error("Delete bank card error:", err);
                    }
                    );
                }
            }
            ));
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
        this.formatCardNumber = fun(cardNumber): String {
            if (!cardNumber) {
                return "";
            }
            return "•••• •••• •••• " + cardNumber.slice(-4);
        }
        ;
        this.formatExpiryDate = fun(date): Any {
            if (!date) {
                return "";
            }
            val parts = date.split("-");
            if (parts.length >= 2) {
                val month = parts[1];
                val year = parts[0].slice(-2);
                return "" + month + "/" + year;
            }
            return date;
        }
        ;
    }
    open lateinit var fetchCards: () -> Unit;
    open lateinit var addNewCard: () -> Unit;
    open lateinit var deleteCard: (cardId) -> Unit;
    open lateinit var handleBack: () -> Unit;
    open lateinit var formatCardNumber: (cardNumber) -> String;
    open lateinit var formatExpiryDate: (date) -> Any;
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
                return utsMapOf("cards-container" to padStyleMapOf(utsMapOf("paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "backgroundImage" to "linear-gradient(to bottom, #e0f0ff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "25rpx", "left" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#0084ff")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center")), "header" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "textAlign" to "center", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "position" to "relative", "marginBottom" to "30rpx")), "loading-container" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "height" to "400rpx")), "loading-text" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "color" to "#666666")), "card-list" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "marginRight" to 0, "marginBottom" to "20rpx", "marginLeft" to 0)), "card-item" to padStyleMapOf(utsMapOf("marginBottom" to "30rpx", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "borderRadius" to "25rpx", "backgroundImage" to "linear-gradient(135deg, #0396FF, #0D47A1)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0 10rpx 20rpx rgba(3, 150, 255, 0.2)", "position" to "relative", "overflow" to "hidden", "content::before" to "''", "position::before" to "absolute", "top::before" to "-100rpx", "right::before" to "-100rpx", "width::before" to "300rpx", "height::before" to "300rpx", "backgroundImage::before" to "none", "backgroundColor::before" to "rgba(255,255,255,0.1)", "content::after" to "''", "position::after" to "absolute", "bottom::after" to "-150rpx", "left::after" to "-150rpx", "width::after" to "400rpx", "height::after" to "400rpx", "backgroundImage::after" to "none", "backgroundColor::after" to "rgba(255,255,255,0.05)")), "card-info" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "gap" to "20rpx", "position" to "relative", "zIndex" to 1)), "card-country" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "rgba(255,255,255,0.8)", "textTransform" to "uppercase", "letterSpacing" to "2rpx")), "card-number-container" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "justifyContent" to "space-between", "marginTop" to "20rpx", "marginRight" to 0, "marginBottom" to "20rpx", "marginLeft" to 0)), "card-number" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "letterSpacing" to "4rpx", "color" to "#ffffff", "fontFamily" to "monospace", "textShadow" to "0 2rpx 4rpx rgba(0, 0, 0, 0.1)")), "card-expires" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "gap" to "10rpx")), "expires-label" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "rgba(255,255,255,0.7)")), "expires-date" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#ffffff", "fontWeight" to "bold")), "delete-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "20rpx", "right" to "20rpx", "width" to "60rpx", "height" to "60rpx", "borderRadius" to "30rpx", "backgroundColor" to "rgba(255,255,255,0.2)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 2)), "delete-icon" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "color" to "#ffffff")), "empty-state" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "paddingTop" to "100rpx", "paddingRight" to 0, "paddingBottom" to "100rpx", "paddingLeft" to 0)), "empty-icon" to padStyleMapOf(utsMapOf("fontSize" to "120rpx", "marginBottom" to "30rpx")), "empty-text" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "color" to "#333333", "fontWeight" to "bold", "marginBottom" to "10rpx")), "empty-subtext" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666")), "add-card" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx", "backgroundColor" to "#ffffff", "borderRadius" to "25rpx", "marginTop" to "40rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.05)", "borderWidth" to "2rpx", "borderStyle" to "solid", "borderColor" to "rgba(0,0,0,0.05)", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "transform:active" to "scale(0.98)", "backgroundColor:active" to "#f8f9fa")), "add-icon" to padStyleMapOf(utsMapOf("fontSize" to "44rpx", "marginRight" to "20rpx", "color" to "#0396FF")), "add-text" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "flex" to 1, "color" to "#333333")), "@TRANSITION" to utsMapOf("add-card" to utsMapOf("duration" to "0.3s", "timingFunction" to "ease")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
