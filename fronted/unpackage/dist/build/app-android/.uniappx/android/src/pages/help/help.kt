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
open class GenPagesHelpHelp : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
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
                createElementVNode("text", utsMapOf("class" to "header-title"), "Help")
            )),
            createElementVNode("text", utsMapOf("class" to "page-title"), "Common Usual Questions"),
            createElementVNode("view", utsMapOf("class" to "help-list"), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "help-item", "onClick" to fun(){
                    _ctx.toggleExpand("start");
                }
                ), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "item-left"), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "question-o", "size" to "20px", "color" to "#007AFF")),
                        createElementVNode("text", utsMapOf("class" to "help-text"), "How to rent an e-scooter?")
                    )),
                    createVNode(_component_van_icon, utsMapOf("name" to if (_ctx.expanded.start) {
                        "arrow-up";
                    } else {
                        "arrow";
                    }
                    , "size" to "16px", "color" to "#999"), null, 8, utsArrayOf(
                        "name"
                    ))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                    "expand-wrapper",
                    utsMapOf("expanded" to _ctx.expanded.start)
                ))), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "expand-content"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "solution-text"), " 1. Select the time and the period you would like to rent an e-scooter and click next button\n 2. Choose an e-scooter from the list. You can see information about available e-scooters on the list and map\n 3. Click next button to confirm the renting details\n 4. Finish the payment and rent successfully ")
                    ))
                ), 2),
                createElementVNode("view", utsMapOf("class" to "divider")),
                createElementVNode("view", utsMapOf("class" to "help-item", "onClick" to fun(){
                    _ctx.toggleExpand("unlock");
                }
                ), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "item-left"), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "warning-o", "size" to "20px", "color" to "#FF9500")),
                        createElementVNode("text", utsMapOf("class" to "help-text"), "How to expand the period?")
                    )),
                    createVNode(_component_van_icon, utsMapOf("name" to if (_ctx.expanded.unlock) {
                        "arrow-up";
                    } else {
                        "arrow";
                    }
                    , "size" to "16px", "color" to "#999"), null, 8, utsArrayOf(
                        "name"
                    ))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                    "expand-wrapper",
                    utsMapOf("expanded" to _ctx.expanded.unlock)
                ))), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "expand-content"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "solution-text"), " 1. Go to the order list\n 2. Find the order you want to expand\n 3. Click the \"expand\" button. ")
                    ))
                ), 2),
                createElementVNode("view", utsMapOf("class" to "divider")),
                createElementVNode("view", utsMapOf("class" to "help-item", "onClick" to fun(){
                    _ctx.toggleExpand("broken");
                }
                ), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "item-left"), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "info-o", "size" to "20px", "color" to "#FF3B30")),
                        createElementVNode("text", utsMapOf("class" to "help-text"), "How does it charge?")
                    )),
                    createVNode(_component_van_icon, utsMapOf("name" to if (_ctx.expanded.broken) {
                        "arrow-up";
                    } else {
                        "arrow";
                    }
                    , "size" to "16px", "color" to "#999"), null, 8, utsArrayOf(
                        "name"
                    ))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                    "expand-wrapper",
                    utsMapOf("expanded" to _ctx.expanded.broken)
                ))), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "expand-content"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "solution-text"), " The e-scooters' prices for each hour are shown in the list on home page.\n The total cost will be it multiplys the time used. ")
                    ))
                ), 2),
                createElementVNode("view", utsMapOf("class" to "divider")),
                createElementVNode("view", utsMapOf("class" to "help-item", "onClick" to fun(){
                    _ctx.toggleExpand("payment");
                }
                ), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "item-left"), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "info-o", "size" to "20px", "color" to "#FF3B30")),
                        createElementVNode("text", utsMapOf("class" to "help-text"), "How to pay the bill?")
                    )),
                    createVNode(_component_van_icon, utsMapOf("name" to if (_ctx.expanded.broken) {
                        "arrow-up";
                    } else {
                        "arrow";
                    }
                    , "size" to "16px", "color" to "#999"), null, 8, utsArrayOf(
                        "name"
                    ))
                ), 8, utsArrayOf(
                    "onClick"
                )),
                createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                    "expand-wrapper",
                    utsMapOf("expanded" to _ctx.expanded.payment)
                ))), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "expand-content"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "solution-text"), " You can follow the steps when reserving e-scooters and just select a card to pay.\n If you do not pay when reserving, there will be announcements on home page and you can just click them, or go to the order list page to finish the payment. ")
                    ))
                ), 2),
                createElementVNode("view", utsMapOf("class" to "divider")),
                createElementVNode("view", utsMapOf("class" to "help-item", "onClick" to fun(){
                    _ctx.navigateToDetail("feedback");
                }
                ), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "item-left"), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "comment-o", "size" to "20px", "color" to "#34C759")),
                        createElementVNode("text", utsMapOf("class" to "help-text"), "Give feedback")
                    )),
                    createVNode(_component_van_icon, utsMapOf("name" to "arrow", "size" to "16px", "color" to "#999"))
                ), 8, utsArrayOf(
                    "onClick"
                ))
            ))
        ));
    }
    open var expanded: UTSJSONObject by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("expanded" to object : UTSJSONObject() {
            var start = false
            var unlock = false
            var broken = false
            var payment = false
        });
    }
    override fun `$initMethods`() {
        this.toggleExpand = fun(type) {
            this.expanded[type] = !this.expanded[type];
        }
        ;
        this.navigateToDetail = fun(type) {
            val typeMapping: UTSJSONObject = object : UTSJSONObject() {
                var feedback = "/pages/feedback/feedback"
            };
            if (typeMapping[type]) {
                uni_navigateTo(NavigateToOptions(url = typeMapping[type]));
            }
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
    open lateinit var toggleExpand: (type) -> Unit;
    open lateinit var navigateToDetail: (type) -> Unit;
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to bottom, #e6f2ff, #ffffff)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "32rpx", "paddingRight" to "32rpx", "paddingBottom" to "32rpx", "paddingLeft" to "32rpx", "boxSizing" to "border-box", "overflowY" to "auto")), "page-title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#007aff", "marginTop" to "20rpx", "marginRight" to 0, "marginBottom" to "40rpx", "marginLeft" to 0, "textAlign" to "center")), "help-list" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "borderRadius" to "24rpx", "overflow" to "hidden", "boxShadow" to "0 4px 20px rgba(0, 122, 255, 0.1)")), "help-item" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "28rpx", "paddingRight" to "32rpx", "paddingBottom" to "28rpx", "paddingLeft" to "32rpx", "backgroundColor" to "#ffffff", "transitionProperty" to "backgroundColor", "transitionDuration" to "0.2s", "transitionTimingFunction" to "ease", "backgroundColor:active" to "#f2f6fa")), "item-left" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "alignItems" to "center", "gap" to "16rpx")), "help-text" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "color" to "#333333")), "divider" to padStyleMapOf(utsMapOf("height" to "2rpx", "backgroundColor" to "#f0f0f0", "marginTop" to 0, "marginRight" to "32rpx", "marginBottom" to 0, "marginLeft" to "32rpx")), "expand-wrapper" to utsMapOf("" to utsMapOf("maxHeight" to 0, "overflow" to "hidden", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease-out"), ".expanded" to utsMapOf("maxHeight" to "400rpx")), "expand-content" to utsMapOf("" to utsMapOf("paddingTop" to "24rpx", "paddingRight" to "32rpx", "paddingBottom" to "24rpx", "paddingLeft" to "32rpx", "backgroundColor" to "#f9fbfd", "borderTopWidth" to "1rpx", "borderTopStyle" to "solid", "borderTopColor" to "#ebeff5", "transform" to "translateY(-10%)", "opacity" to 0), ".expand-wrapper.expanded " to utsMapOf("transform" to "translateY(0)", "opacity" to 1)), "solution-text" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#666666", "lineHeight" to 1.8)), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to "25rpx", "left" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "zIndex" to 10, "backgroundColor" to "#f4f8ff", "borderRadius" to "25rpx", "width" to "80rpx", "height" to "80rpx")), "back-icon" to padStyleMapOf(utsMapOf("fontSize" to "55rpx", "color" to "#007aff")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "fontWeight" to "bold", "color" to "#007aff", "textAlign" to "center")), "header" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "textAlign" to "center", "borderRadius" to "20rpx", "boxShadow" to "0 4px 12px rgba(0, 0, 0, 0.05)", "position" to "relative", "marginBottom" to "40rpx")), "@TRANSITION" to utsMapOf("help-item" to utsMapOf("property" to "backgroundColor", "duration" to "0.2s", "timingFunction" to "ease"), "expand-wrapper" to utsMapOf("duration" to "0.3s", "timingFunction" to "ease-out")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
