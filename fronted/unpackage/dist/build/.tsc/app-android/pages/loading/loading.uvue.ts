import { ref } from 'vue';


const __sfc__ = defineComponent({
  __name: 'loading',
  setup(__props): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const slides = ref([
  {
    image: '/static/loading/img1.png',
    title: 'We offer professional escooter services',
    description: 'Enjoy a smooth and convenient ride-hailing experience',
  },
  {
    image: '/static/loading/img2.png',
    title: 'Instant ride-hailing at your fingertips',
    description: 'Quick rides, worry-free travel',
  },
  {
    image: '/static/loading/img3.png',
    title: 'Experience a whole new escooter service',
    description: 'Discover smarter travel options with us',
  },
]);

const currentIndex = ref(0);

// swiper 滑动事件
const onSwipeChange = (event) => {
  currentIndex.value = event.detail.current;
};

// 点击按钮跳转或下一页
const handleButtonClick = () => {
  if (currentIndex.value < slides.value.length - 1) {
    currentIndex.value++;
  } else {
    uni.navigateTo({
      url: '/pages/register/register'
    });
  }
};

return (): any | null => {

const _component_img = resolveComponent("img")
const _component_h2 = resolveComponent("h2")
const _component_p = resolveComponent("p")
const _component_div = resolveComponent("div")

  return createElementVNode("view", utsMapOf({ class: "container" }), [
    createElementVNode("swiper", utsMapOf({
      class: "swipe-box",
      current: currentIndex.value,
      onChange: onSwipeChange,
      circular: false,
      autoplay: false
    }), [
      createElementVNode(Fragment, null, RenderHelpers.renderList(slides.value, (item, index, __index, _cached): any => {
        return createElementVNode("swiper-item", utsMapOf({ key: index }), [
          createVNode(_component_div, utsMapOf({ class: "slide-content" }), utsMapOf({
            default: withSlotCtx((): any[] => [
              createVNode(_component_img, utsMapOf({
                src: item.image,
                class: "illustration"
              }), null, 8 /* PROPS */, ["src"]),
              createVNode(_component_h2, null, utsMapOf({
                default: withSlotCtx((): any[] => [toDisplayString(item.title)]),
                _: 2 /* DYNAMIC */
              }), 1024 /* DYNAMIC_SLOTS */),
              createVNode(_component_p, null, utsMapOf({
                default: withSlotCtx((): any[] => [toDisplayString(item.description)]),
                _: 2 /* DYNAMIC */
              }), 1024 /* DYNAMIC_SLOTS */)
            ]),
            _: 2 /* DYNAMIC */
          }), 1024 /* DYNAMIC_SLOTS */)
        ])
      }), 128 /* KEYED_FRAGMENT */)
    ], 40 /* PROPS, NEED_HYDRATION */, ["current"]),
    createElementVNode("view", utsMapOf({ class: "content" }), [
      createElementVNode("button", utsMapOf({
        class: "next-btn",
        onClick: handleButtonClick
      }), toDisplayString(currentIndex.value < slides.value.length - 1 ? 'Next' : 'Start Now'), 1 /* TEXT */)
    ])
  ])
}
}

})
export default __sfc__
const GenPagesLoadingLoadingStyles = [utsMapOf([["container", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["textAlign", "center"]]))], ["swipe-box", padStyleMapOf(utsMapOf([["width", "100%"], ["height", 400]]))], ["slide-content", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["width", "100%"], ["height", "100%"]]))], ["illustration", padStyleMapOf(utsMapOf([["width", "80%"], ["maxHeight", 250], ["objectFit", "contain"], ["marginBottom", 20]]))], ["content", padStyleMapOf(utsMapOf([["position", "absolute"], ["bottom", 80], ["width", "100%"], ["textAlign", "center"]]))], ["next-btn", padStyleMapOf(utsMapOf([["marginTop", "40rpx"], ["marginRight", "50rpx"], ["marginBottom", 0], ["marginLeft", "50rpx"], ["height", "90rpx"], ["lineHeight", "90rpx"], ["fontSize", "30rpx"], ["backgroundImage", "linear-gradient(to right, #82b1ff, #007aff)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxShadow", "0px 4px 10px rgba(0, 122, 255, 0.3)"], ["color", "#ffffff"], ["borderRadius", "70rpx"], ["transitionDuration", "0.3s"], ["transform:active", "scale(0.96)"]]))], ["@TRANSITION", utsMapOf([["next-btn", utsMapOf([["duration", "0.3s"]])]])]])]
