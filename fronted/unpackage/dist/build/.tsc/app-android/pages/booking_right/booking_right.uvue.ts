
const __sfc__ = defineComponent({
  data() {
    return {
      orderId: null
    };
  },
  onLoad(query) {
    console.log('📦 booking_right received query:', query);
    this.orderId = query.orderId;

    // Debug info
    console.log('✅ Received orderId value:', this.orderId);
    console.log('✅ orderId type:', typeof this.orderId);
    
    // If orderId is a string, try converting it to a number
    if (typeof this.orderId === 'string') {
      const numId = Number(this.orderId);
      if (!isNaN(numId)) {
        console.log('✅ orderId converted to number:', numId);
      } else {
        console.warn('⚠️ orderId is not a valid number:', this.orderId);
      }
    }
  },
  methods: {
    navigateToHome() {
      uni.navigateTo({ url: '/pages/home/home' });
    },
    navigateToOrderDetails() {
      if (!this.orderId) {
        uni.showToast({ title: 'Order ID is missing', icon: 'none' });
        return;
      }

      console.log('🔀 Navigating to order_not_used, orderId =', this.orderId);

      // Convert to string to ensure correct URL encoding
      const orderId = String(this.orderId);

      // Use navigateTo instead of reLaunch to keep history
      uni.navigateTo({
        url: `/pages/order_not_used/order_not_used_home?orderId=${orderId}`,
        success: () => {
          console.log('✅ Navigation succeeded');
        },
        fail: (err) => {
          console.error('❌ Navigation failed:', err);
          // Fallback to redirectTo
          uni.redirectTo({
            url: `/pages/order_not_used/order_not_used_home?orderId=${orderId}`
          });
        }
      });
    }
  }
});

export default __sfc__
function GenPagesBookingRightBookingRightRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
const _component_van_icon = resolveComponent("van-icon")
const _component_img = resolveComponent("img")
const _component_h2 = resolveComponent("h2")
const _component_br = resolveComponent("br")
const _component_p = resolveComponent("p")

  return createElementVNode("view", utsMapOf({ class: "container" }), [
    createElementVNode("view", utsMapOf({
      class: "floating-btn close-btn",
      onClick: _ctx.navigateToHome
    }), [
      createVNode(_component_van_icon, utsMapOf({
        name: "close",
        class: "close-icon"
      }))
    ], 8 /* PROPS */, ["onClick"]),
    createElementVNode("view", utsMapOf({ class: "content" }), [
      createElementVNode("view", utsMapOf({ class: "icon" }), [
        createVNode(_component_img, utsMapOf({
          src: "/static/booking_su/icon.png",
          alt: "icon"
        }))
      ]),
      createVNode(_component_h2, null, utsMapOf({
        default: withSlotCtx((): any[] => ["Booking placed successfully"]),
        _: 1 /* STABLE */
      })),
      createVNode(_component_p, null, utsMapOf({
        default: withSlotCtx((): any[] => [
          "Thanks for your Booking. ",
          createVNode(_component_br),
          " Your Booking has been placed successfully.",
          createVNode(_component_br),
          " Please continue your Book. "
        ]),
        _: 1 /* STABLE */
      }))
    ]),
    createElementVNode("view", utsMapOf({ class: "btn-content" }), [
      createElementVNode("button", utsMapOf({
        class: "see",
        onClick: _ctx.navigateToOrderDetails
      }), " See Details ", 8 /* PROPS */, ["onClick"]),
      createElementVNode("button", utsMapOf({
        class: "back",
        onClick: _ctx.navigateToHome
      }), " Back Home ", 8 /* PROPS */, ["onClick"])
    ])
  ])
}
const GenPagesBookingRightBookingRightStyles = [utsMapOf([["container", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "space-between"], ["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20], ["textAlign", "center"], ["backgroundImage", "linear-gradient(to bottom, #f0faff, #ffffff)"], ["backgroundColor", "rgba(0,0,0,0)"]]))], ["close-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["top", "50rpx"], ["left", "50rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["zIndex", 10]]))], ["close-icon", padStyleMapOf(utsMapOf([["fontSize", "90rpx"], ["color", "#7dafff"]]))], ["floating-btn", padStyleMapOf(utsMapOf([["position", "fixed"], ["top", 30], ["left", 30], ["zIndex", 100], ["backgroundColor", "rgba(255,255,255,0.8)"], ["width", 50], ["height", 50], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"], ["boxShadow", "0 2px 4px rgba(0, 0, 0, 0.2)"]]))], ["van-icon", utsMapOf([[".floating-btn ", utsMapOf([["fontSize", 28], ["color", "#007aff"]])]])], ["content", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["flex", 1], ["textAlign", "center"]]))], ["icon", padStyleMapOf(utsMapOf([["marginTop", "20rpx"], ["marginRight", 0], ["marginBottom", "20rpx"], ["marginLeft", 0]]))], ["btn-content", padStyleMapOf(utsMapOf([["width", "100%"], ["position", "absolute"], ["bottom", "50rpx"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"]]))], ["back", padStyleMapOf(utsMapOf([["width", "80%"], ["marginTop", "10rpx"], ["marginRight", 0], ["marginBottom", "10rpx"], ["marginLeft", 0], ["height", "90rpx"], ["lineHeight", "90rpx"], ["fontSize", "35rpx"], ["backgroundImage", "linear-gradient(to right, #82b1ff, #007aff)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#ffffff"], ["borderRadius", "70rpx"], ["transitionDuration", "0.3s"], ["transform:active", "scale(0.96)"]]))], ["see", padStyleMapOf(utsMapOf([["width", "80%"], ["marginTop", "10rpx"], ["marginRight", 0], ["marginBottom", "10rpx"], ["marginLeft", 0], ["height", "90rpx"], ["lineHeight", "90rpx"], ["fontSize", "35rpx"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["color", "#82b1ff"], ["borderRadius", "70rpx"], ["transitionDuration", "0.3s"], ["borderWidth", 1], ["borderStyle", "solid"], ["borderColor", "#82b1ff"], ["transform:active:active", "scale(0.96)"]]))], ["@TRANSITION", utsMapOf([["back", utsMapOf([["duration", "0.3s"]])], ["see", utsMapOf([["duration", "0.3s"]])]])]])]
