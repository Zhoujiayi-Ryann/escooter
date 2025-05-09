import { ref, onMounted } from "vue";
import { orderApi } from '@/utils/api/order';


const __sfc__ = defineComponent({
  __name: 'order_not_used_home',
  setup(__props): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const order = ref({
  orderId: '',
  scooterId: '',
  scooterLocation: '',
  battery: '',
  bookTime: '',
  orderTime: '',
  duration: '',
  price: 0
});

const location = ref({ lng: 0, lat: 0 });
const markers = ref([]);

const anchors = [
  120,
  Math.round(0.4 * window.innerHeight),
  Math.round(0.65 * window.innerHeight)
];
const panelHeight = ref(anchors[0]);

let pageOrderId = null;

onLoad((query) => {
  console.log('📦 onLoad received:', query);
  if (query && query.orderId) {
    pageOrderId = query.orderId;
    console.log('📦 orderId received from onLoad:', pageOrderId);
  }
  fetchOrderDetail();
});

const fetchOrderDetail = async () => {
  let rawId = pageOrderId;

  if (!rawId) {
    try {
      const pages = getCurrentPages();
      const currentPage = pages[pages.length - 1];
      const options = currentPage.$page?.options;
      if (options && options.orderId) {
        rawId = options.orderId;
        console.log('📦 Fallback orderId from page options:', rawId);
      }
    } catch (err) {
      console.error('📦 Failed to get page parameters:', err);
    }
  }

  if (!rawId) {
    console.warn('⚠️ orderId missing, using test value');
    rawId = '1';
  }

  const orderId = Number(rawId);
  if (!orderId || isNaN(orderId)) {
    uni.showToast({ title: 'Invalid Order ID', icon: 'none', duration: 3000 });
    return;
  }

  try {
    const res = await orderApi.getOrderInfo(orderId);
    if (res.code === 1 && res.data) {
      const data = res.data;
      const hasExtension = !!data.new_end_time;

      const totalCost = (data.cost ?? 0) + (data.extended_cost ?? 0);
      const totalDurationHours = (data.duration ?? 0) + (data.extended_duration ?? 0);

      order.value = {
        orderId: data.order_id,
        scooterId: data.scooter_id,
        scooterLocation: data.pickup_address || 'Unknown',
        battery: data.scooter_info?.battery_level ?? 'N/A',
        bookTime: formatToCST(data.start_time),
        orderTime: formatToCST(data.created_at),
        duration: `${totalDurationHours} hour${totalDurationHours > 1 ? 's' : ''}`,
        price: totalCost
      };

      location.value = {
        lat: data.scooter_info?.latitude ?? 0,
        lng: data.scooter_info?.longitude ?? 0
      };

      loadScooterLocation();
    } else {
      uni.showToast({ title: res.msg || 'Failed to fetch order', icon: 'none' });
    }
  } catch (err) {
    console.error('🚨 API error:', err);
    uni.showToast({ title: 'Network error, try again later', icon: 'none' });
  }
};

function formatToCST(utcStr) {
  if (!utcStr) return 'N/A';
  const date = new Date(utcStr);
  if (isNaN(date.getTime())) return 'Invalid Date';

  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
}

const loadScooterLocation = () => {
  if (!location.value.lat || !location.value.lng) {
    console.warn('📌 Invalid coordinates for marker', location.value);
    return;
  }

  markers.value = [{
    id: 1,
    latitude: location.value.lat,
    longitude: location.value.lng,
    iconPath: '/static/order_details/maker.svg',
    width: 32,
    height: 32,
    callout: {
      content: order.value.scooterLocation || `Scooter SC${order.value.scooterId}`,
      display: 'ALWAYS',
      fontSize: 17,
      borderRadius: 10,
      padding: 5,
      bgColor: "#ffffff",
      color: "#057cff",
      fontWeight: 'bold',
    }
  }];
};

const goToHelp = () => {
  uni.navigateTo({ url: '/pages/help/help' });
};

const handleBack = () => {
    uni.reLaunch({ url: '/pages/home/home' });
};

const goToUse = async () => {
  if (!order.value.orderId) {
    uni.showToast({ title: 'Missing Order ID', icon: 'none' });
    return;
  }

  try {
    const res = await orderApi.activateOrder(order.value.orderId);
    if (res.code === 1) {
      uni.showToast({ title: 'Order activated. You can now use the scooter.', icon: 'success' });

      uni.navigateTo({
        url: `/pages/order_in_progress/order_in_progress_home?orderId=${order.value.orderId}`
      });
    } else {
      uni.showToast({ title: res.msg || 'Failed to activate order', icon: 'none' });
    }
  } catch (err) {
    console.error('🚨 Failed to activate order:', err);
    uni.showToast({ title: 'Network error, please try again later', icon: 'none' });
  }
};

onMounted(() => {
  if (!pageOrderId) {
    fetchOrderDetail();
  }
});

return (): any | null => {

const _component_van_icon = resolveComponent("van-icon")
const _component_map = resolveComponent("map")
const _component_van_button = resolveComponent("van-button")
const _component_van_floating_panel = resolveComponent("van-floating-panel")

  return createElementVNode("view", utsMapOf({ class: "container" }), [
    createElementVNode("view", utsMapOf({
      class: "back-btn",
      onClick: handleBack
    }), [
      createVNode(_component_van_icon, utsMapOf({
        name: "arrow-left",
        class: "back-icon"
      }))
    ]),
    createElementVNode("view", utsMapOf({
      class: "person-btn",
      onClick: goToHelp
    }), [
      createVNode(_component_van_icon, utsMapOf({
        name: "service-o",
        class: "person-icon"
      }))
    ]),
    createElementVNode("view", utsMapOf({ class: "map-container" }), [
      createVNode(_component_map, utsMapOf({
        id: "orderMap",
        class: "map",
        latitude: location.value.lat,
        longitude: location.value.lng,
        markers: markers.value,
        scale: 16,
        "show-location": "",
        "enable-3D": "",
        "enable-poi": "",
        "enable-overlooking": ""
      }), null, 8 /* PROPS */, ["latitude", "longitude", "markers"])
    ]),
    createElementVNode("cover-view", utsMapOf({ class: "map-overlay" })),
    createVNode(_component_van_floating_panel, utsMapOf({
      height: panelHeight.value,
      "onUpdate:height": $event => {(panelHeight).value = $event},
      anchors: anchors,
      class: "floating-panel"
    }), utsMapOf({
      default: withSlotCtx((): any[] => [
        createElementVNode("view", utsMapOf({ class: "order-details" }), [
          createElementVNode("view", utsMapOf({ class: "order-info" }), [
            createElementVNode("text", utsMapOf({ class: "order-price" }), "To be used"),
            createElementVNode("view", utsMapOf({ class: "order-tags" }), [
              createElementVNode("view", utsMapOf({ class: "badge" }), "Order ID: " + toDisplayString(order.value.orderId), 1 /* TEXT */),
              createElementVNode("view", utsMapOf({ class: "status-tag" }), "Unused")
            ])
          ]),
          createElementVNode("view", utsMapOf({ class: "info-box" }), [
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Order ID:"),
              createElementVNode("text", utsMapOf({ class: "value" }), toDisplayString(order.value.orderId), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Scooter ID:"),
              createElementVNode("text", utsMapOf({ class: "value" }), "SC" + toDisplayString(order.value.scooterId), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Scooter Location:"),
              createElementVNode("view", utsMapOf({ class: "value address" }), toDisplayString(order.value.scooterLocation), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Battery:"),
              createElementVNode("text", utsMapOf({ class: "value" }), toDisplayString(order.value.battery) + "%", 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Start Time:"),
              createElementVNode("text", utsMapOf({ class: "value" }), toDisplayString(order.value.bookTime), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Duration:"),
              createElementVNode("text", utsMapOf({ class: "value" }), toDisplayString(order.value.duration), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Ordered At:"),
              createElementVNode("text", utsMapOf({ class: "value" }), toDisplayString(order.value.orderTime), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "divider" })),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Price:"),
              createElementVNode("text", utsMapOf({ class: "value" }), "￡" + toDisplayString(order.value.price), 1 /* TEXT */)
            ])
          ]),
          createVNode(_component_van_button, utsMapOf({
            class: "feedback-btn",
            type: "primary",
            onClick: goToUse
          }), utsMapOf({
            default: withSlotCtx((): any[] => [" Go to use vehicle "]),
            _: 1 /* STABLE */
          }))
        ])
      ]),
      _: 1 /* STABLE */
    }), 8 /* PROPS */, ["height", "onUpdate:height"])
  ])
}
}

})
export default __sfc__
const GenPagesOrderNotUsedOrderNotUsedHomeStyles = [utsMapOf([["back-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["top", "50rpx"], ["left", "60rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["zIndex", 10], ["backgroundColor", "#f4f8ff"], ["borderRadius", "25rpx"], ["width", "80rpx"], ["height", "80rpx"]]))], ["back-icon", padStyleMapOf(utsMapOf([["fontSize", "55rpx"], ["color", "#0084ff"]]))], ["person-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["top", "50rpx"], ["right", "60rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["zIndex", 10], ["backgroundColor", "#f4f8ff"], ["borderRadius", "25rpx"], ["width", "80rpx"], ["height", "80rpx"]]))], ["person-icon", padStyleMapOf(utsMapOf([["fontSize", "55rpx"], ["color", "#0084ff"]]))], ["map-container", padStyleMapOf(utsMapOf([["width", "100%"], ["position", "relative"]]))], ["map", padStyleMapOf(utsMapOf([["width", "100%"], ["height", "100%"]]))], ["map-overlay", padStyleMapOf(utsMapOf([["position", "absolute"], ["top", 0], ["left", 0], ["width", "100%"], ["height", "100%"], ["background", "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )"], ["pointerEvents", "none"], ["zIndex", 2]]))], ["floating-panel", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.8)"], ["backdropFilter", "blur(15px)"], ["borderTopLeftRadius", 20], ["borderTopRightRadius", 20], ["borderBottomRightRadius", 0], ["borderBottomLeftRadius", 0], ["boxShadow", "0 -5px 20px rgba(0, 0, 0, 0.1)"]]))], ["order-details", padStyleMapOf(utsMapOf([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20]]))], ["order-info", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["flexDirection", "row"], ["fontSize", 16], ["fontWeight", "bold"], ["paddingBottom", 10], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "rgba(255,255,255,0.3)"]]))], ["order-tags", padStyleMapOf(utsMapOf([["display", "flex"], ["alignItems", "center"], ["gap", "10px"]]))], ["badge", padStyleMapOf(utsMapOf([["backgroundColor", "#eeeeee"], ["paddingTop", 4], ["paddingRight", 10], ["paddingBottom", 4], ["paddingLeft", 10], ["borderRadius", 12], ["fontSize", 12], ["color", "#444444"]]))], ["status-tag", padStyleMapOf(utsMapOf([["backgroundColor", "#007aff"], ["color", "#FFFFFF"], ["paddingTop", 4], ["paddingRight", 10], ["paddingBottom", 4], ["paddingLeft", 10], ["fontSize", 12], ["borderRadius", 12]]))], ["order-price", padStyleMapOf(utsMapOf([["fontSize", 25], ["fontWeight", "bold"], ["color", "#0079fe"]]))], ["feedback-icon", padStyleMapOf(utsMapOf([["fontSize", 24], ["color", "#007aff"], ["cursor", "pointer"]]))], ["info-box", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "rgba(229,231,245,0.5)"], ["backdropFilter", "blur(25px)"], ["paddingTop", 30], ["paddingRight", 30], ["paddingBottom", 30], ["paddingLeft", 30], ["borderRadius", 15], ["marginTop", 15], ["borderWidth", 1], ["borderStyle", "solid"], ["borderColor", "#82b1ff"], ["boxShadow", "0 5px 15px rgba(0, 0, 0, 0.1)"]]))], ["info-item", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "row"], ["justifyContent", "space-between"], ["marginBottom", 8], ["overflow", "visible"], ["wordBreak", "break-word"]]))], ["label", padStyleMapOf(utsMapOf([["fontSize", 17], ["fontWeight", "bold"]]))], ["value", utsMapOf([["", utsMapOf([["fontSize", 14], ["color", "#666666"]])], [".address", utsMapOf([["fontSize", "25rpx"], ["color", "#666666"], ["marginTop", "4rpx"], ["width", 160], ["textAlign", "right"]])]])], ["divider", padStyleMapOf(utsMapOf([["width", "100%"], ["height", 1], ["backgroundColor", "#dddddd"], ["marginTop", 25], ["marginRight", 0], ["marginBottom", 25], ["marginLeft", 0]]))], ["feedback-btn", padStyleMapOf(utsMapOf([["marginTop", 20], ["width", "100%"], ["borderRadius", 25], ["fontSize", "34rpx"], ["height", "96rpx"], ["backgroundImage", "linear-gradient(to right, #82b1ff, #007aff)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#FFFFFF"], ["boxShadow", "0 4px 10px rgba(0, 122, 255, 0.2)"], ["transform:active", "scale(0.98)"]]))]])]
