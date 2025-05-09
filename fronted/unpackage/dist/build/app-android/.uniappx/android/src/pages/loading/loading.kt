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
open class GenPagesLoadingLoading : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesLoadingLoading) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!;
            val _ctx = __ins.proxy as GenPagesLoadingLoading;
            val _cache = __ins.renderCache;
            val slides = ref(utsArrayOf(
                object : UTSJSONObject() {
                    var image = "/static/loading/img1.png"
                    var title = "We offer professional escooter services"
                    var description = "Enjoy a smooth and convenient ride-hailing experience"
                },
                object : UTSJSONObject() {
                    var image = "/static/loading/img2.png"
                    var title = "Instant ride-hailing at your fingertips"
                    var description = "Quick rides, worry-free travel"
                },
                object : UTSJSONObject() {
                    var image = "/static/loading/img3.png"
                    var title = "Experience a whole new escooter service"
                    var description = "Discover smarter travel options with us"
                }
            ));
            val currentIndex = ref(0);
            val onSwipeChange = fun(event){
                currentIndex.value = event.detail.current;
            }
            ;
            val handleButtonClick = fun(){
                if (currentIndex.value < slides.value.length - 1) {
                    currentIndex.value++;
                } else {
                    uni_navigateTo(NavigateToOptions(url = "/pages/register/register"));
                }
            }
            ;
            return fun(): Any? {
                val _component_img = resolveComponent("img");
                val _component_h2 = resolveComponent("h2");
                val _component_p = resolveComponent("p");
                val _component_div = resolveComponent("div");
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("swiper", utsMapOf("class" to "swipe-box", "current" to currentIndex.value, "onChange" to onSwipeChange, "circular" to false, "autoplay" to false), utsArrayOf(
                        createElementVNode(Fragment, null, RenderHelpers.renderList(slides.value, fun(item, index, __index, _cached): Any {
                            return createElementVNode("swiper-item", utsMapOf("key" to index), utsArrayOf(
                                createVNode(_component_div, utsMapOf("class" to "slide-content"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                    return utsArrayOf(
                                        createVNode(_component_img, utsMapOf("src" to item.image, "class" to "illustration"), null, 8, utsArrayOf(
                                            "src"
                                        )),
                                        createVNode(_component_h2, null, utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                            return utsArrayOf(
                                                toDisplayString(item.title)
                                            );
                                        }
                                        ), "_" to 2), 1024),
                                        createVNode(_component_p, null, utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                            return utsArrayOf(
                                                toDisplayString(item.description)
                                            );
                                        }
                                        ), "_" to 2), 1024)
                                    );
                                }
                                ), "_" to 2), 1024)
                            ));
                        }
                        ), 128)
                    ), 40, utsArrayOf(
                        "current"
                    )),
                    createElementVNode("view", utsMapOf("class" to "content"), utsArrayOf(
                        createElementVNode("button", utsMapOf("class" to "next-btn", "onClick" to handleButtonClick), toDisplayString(if (currentIndex.value < slides.value.length - 1) {
                            "Next";
                        } else {
                            "Start Now";
                        }
                        ), 1)
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "textAlign" to "center")), "swipe-box" to padStyleMapOf(utsMapOf("width" to "100%", "height" to 400)), "slide-content" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "width" to "100%", "height" to "100%")), "illustration" to padStyleMapOf(utsMapOf("width" to "80%", "maxHeight" to 250, "objectFit" to "contain", "marginBottom" to 20)), "content" to padStyleMapOf(utsMapOf("position" to "absolute", "bottom" to 80, "width" to "100%", "textAlign" to "center")), "next-btn" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "marginRight" to "50rpx", "marginBottom" to 0, "marginLeft" to "50rpx", "height" to "90rpx", "lineHeight" to "90rpx", "fontSize" to "30rpx", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "boxShadow" to "0px 4px 10px rgba(0, 122, 255, 0.3)", "color" to "#ffffff", "borderRadius" to "70rpx", "transitionDuration" to "0.3s", "transform:active" to "scale(0.96)")), "@TRANSITION" to utsMapOf("next-btn" to utsMapOf("duration" to "0.3s")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
