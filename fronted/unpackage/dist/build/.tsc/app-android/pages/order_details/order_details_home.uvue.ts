import { ref } from "vue";
import { onLoad } from "@dcloudio/uni-app";
import { orderApi } from "@/utils/api/order";

// 🚲 订单信息

const __sfc__ = defineComponent({
  __name: 'order_details_home',
  setup(__props): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const order = ref({
  orderId: '',
  scooterId: '',
  startTime: '',
  endTime: '',
  duration: '',
  cost: 0,
  address: '',
  scooterLocation: '',
});

// 📍 地图位置
const location = ref({ lat: 0, lng: 0 });
const markers = ref([]);

// 面板高度控制
const anchors = [
  120,
  Math.round(0.4 * window.innerHeight),
  Math.round(0.55 * window.innerHeight),
];
const panelHeight = ref(anchors[0]);

// 时间格式化 yyyy-MM-dd HH:mm
function formatToCST(utcStr) {
  if (!utcStr) return 'N/A';
  const date = new Date(utcStr);
  if (isNaN(date.getTime())) return 'Invalid Date';
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
}

// 计算持续时间
function formatDuration(startStr, endStr) {
  const start = new Date(startStr);
  const end = new Date(endStr);
  if (isNaN(start) || isNaN(end)) return 'Unknown';

  const diffMs = end - start;
  const totalMinutes = Math.floor(diffMs / (1000 * 60));
  const hours = Math.floor(totalMinutes / 60);
  const minutes = totalMinutes % 60;
  return `${hours}h ${minutes}min`;
}

// 🚀 页面加载时获取参数并拉取订单详情
onLoad((options) => {
  const rawId = options?.orderId;
  const orderId = Number(rawId);
  if (!orderId || isNaN(orderId)) {
    uni.showToast({ title: 'Order ID is invalid', icon: 'none' });
    return;
  }
  fetchOrderDetail(orderId);
});

// 📦 获取订单数据
const fetchOrderDetail = async (orderId) => {
  try {
    const res = await orderApi.getOrderInfo(orderId);
    if (res.code === 1 && res.data) {
      const data = res.data;

      const lat = data.scooter_info?.latitude;
      const lng = data.scooter_info?.longitude;

      if (!lat || !lng) {
        uni.showToast({ title: 'Invalid vehicle location information', icon: 'none' });
        return;
      }

      const totalCost = (data.cost ?? 0) + (data.extended_cost ?? 0);
      const totalDuration = (data.duration ?? 0) + (data.extended_duration ?? 0);
      const finalEndTime = data.new_end_time || data.end_time;

      order.value = {
        orderId: data.order_id,
        scooterId: data.scooter_id,
        startTime: formatToCST(data.start_time),
        endTime: formatToCST(finalEndTime),
        duration: `${totalDuration} hour${totalDuration !== 1 ? 's' : ''}`,
        cost: totalCost,
        address: data.address || 'Unknown',
        scooterLocation: data.pickup_address || 'Unknown',
      };

      location.value = { lat, lng };
      loadScooterLocation();
    } else {
      uni.showToast({ title: res.msg || 'Order retrieval failed', icon: 'none' });
    }
  } catch (err) {
    console.error('🚨 Network error:', err);
    uni.showToast({ title: 'Network malfunction', icon: 'none' });
  }
};

// 地图打点
const loadScooterLocation = () => {
  if (!location.value.lat || !location.value.lng) return;
  markers.value = [{
    id: 1,
    latitude: location.value.lat,
    longitude: location.value.lng,
    iconPath: '/static/order_details/maker.svg',
    width: 32,
    height: 32,
    callout: {
      content: order.value.scooterLocation,
      display: 'ALWAYS',
      fontSize: 15,
      borderRadius: 5,
      padding: 5,
      bgColor: "#ffffff",
      color: "#007aff"
    }
  }];
};

// 页面跳转
const handleBack = () => {
  uni.reLaunch({ url: '/pages/home/home' });
};
const goToSettings = () => {
  uni.navigateTo({ url: '/pages/settings/my_settings/my_settings' });
};
const goToFeedback = () => {
  uni.navigateTo({ url: '/pages/feedback/feedback' });
};
const goHome = () => {
  uni.reLaunch({ url: '/pages/home/home' });
};

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
      onClick: goToSettings
    }), [
      createVNode(_component_van_icon, utsMapOf({
        name: "manager",
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
      }), null, 8 /* PROPS */, ["latitude", "longitude", "markers"]),
      createElementVNode("cover-view", utsMapOf({ class: "map-overlay" }))
    ]),
    createVNode(_component_van_floating_panel, utsMapOf({
      height: panelHeight.value,
      "onUpdate:height": $event => {(panelHeight).value = $event},
      anchors: anchors,
      class: "floating-panel"
    }), utsMapOf({
      default: withSlotCtx((): any[] => [
        createElementVNode("view", utsMapOf({ class: "order-details" }), [
          createElementVNode("view", utsMapOf({ class: "order-info" }), [
            createElementVNode("text", utsMapOf({ class: "order-price" }), "Completed"),
            createElementVNode("view", utsMapOf({ class: "order-tags" }), [
              createElementVNode("view", utsMapOf({ class: "badge" }), "Order ID: " + toDisplayString(order.value.orderId), 1 /* TEXT */),
              createElementVNode("view", utsMapOf({ class: "status-tag" }), "Completed")
            ])
          ]),
          createElementVNode("view", utsMapOf({ class: "info-box" }), [
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Scooter ID:"),
              createElementVNode("text", utsMapOf({ class: "value" }), "SC" + toDisplayString(order.value.scooterId), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Start Time:"),
              createElementVNode("text", utsMapOf({ class: "value" }), toDisplayString(order.value.startTime), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "End Time:"),
              createElementVNode("text", utsMapOf({ class: "value" }), toDisplayString(order.value.endTime), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Duration:"),
              createElementVNode("text", utsMapOf({ class: "value" }), toDisplayString(order.value.duration), 1 /* TEXT */)
            ]),
            createElementVNode("view", utsMapOf({ class: "divider" })),
            createElementVNode("view", utsMapOf({ class: "info-item" }), [
              createElementVNode("text", utsMapOf({ class: "label" }), "Cost:"),
              createElementVNode("text", utsMapOf({ class: "value" }), "￡" + toDisplayString(order.value.cost), 1 /* TEXT */)
            ])
          ]),
          createElementVNode("view", utsMapOf({ class: "button-group" }), [
            createVNode(_component_van_button, utsMapOf({
              class: "half-button home-btn",
              type: "default",
              onClick: goHome
            }), utsMapOf({
              default: withSlotCtx((): any[] => [" Return Home "]),
              _: 1 /* STABLE */
            })),
            createVNode(_component_van_button, utsMapOf({
              class: "half-button feedback-btn",
              type: "primary",
              onClick: goToFeedback
            }), utsMapOf({
              default: withSlotCtx((): any[] => [" Give Feedback "]),
              _: 1 /* STABLE */
            }))
          ])
        ])
      ]),
      _: 1 /* STABLE */
    }), 8 /* PROPS */, ["height", "onUpdate:height"])
  ])
}
}

})
export default __sfc__
const GenPagesOrderDetailsOrderDetailsHomeStyles = [utsMapOf([["back-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["top", "50rpx"], ["left", "60rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["zIndex", 10], ["backgroundColor", "#f4f8ff"], ["borderRadius", "25rpx"], ["width", "80rpx"], ["height", "80rpx"]]))], ["back-icon", padStyleMapOf(utsMapOf([["fontSize", "55rpx"], ["color", "#0084ff"]]))], ["person-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["top", "50rpx"], ["right", "60rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["zIndex", 10], ["backgroundColor", "#f4f8ff"], ["borderRadius", "25rpx"], ["width", "80rpx"], ["height", "80rpx"]]))], ["person-icon", padStyleMapOf(utsMapOf([["fontSize", "55rpx"], ["color", "#0084ff"]]))], ["map-container", padStyleMapOf(utsMapOf([["width", "100%"], ["position", "relative"]]))], ["map", padStyleMapOf(utsMapOf([["width", "100%"], ["height", "100%"]]))], ["map-overlay", padStyleMapOf(utsMapOf([["position", "absolute"], ["top", 0], ["left", 0], ["width", "100%"], ["height", "100%"], ["background", "linear-gradient(\n    to bottom,\n    rgba(0, 123, 255, 0.2) 0%,   \n    rgba(0, 123, 255, 0.1) 50%,  \n    rgba(255, 255, 255, 0) 100%  \n  )"], ["pointerEvents", "none"], ["zIndex", 2]]))], ["floating-panel", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.8)"], ["backdropFilter", "blur(15px)"], ["borderTopLeftRadius", 20], ["borderTopRightRadius", 20], ["borderBottomRightRadius", 0], ["borderBottomLeftRadius", 0], ["boxShadow", "0 -5px 20px rgba(0, 0, 0, 0.1)"]]))], ["order-details", padStyleMapOf(utsMapOf([["paddingTop", 20], ["paddingRight", 20], ["paddingBottom", 20], ["paddingLeft", 20]]))], ["order-info", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["flexDirection", "row"], ["fontSize", 16], ["fontWeight", "bold"], ["paddingBottom", 10], ["borderBottomWidth", 1], ["borderBottomStyle", "solid"], ["borderBottomColor", "rgba(255,255,255,0.3)"]]))], ["order-tags", padStyleMapOf(utsMapOf([["display", "flex"], ["alignItems", "center"], ["gap", "10px"]]))], ["badge", padStyleMapOf(utsMapOf([["backgroundColor", "#eeeeee"], ["paddingTop", 4], ["paddingRight", 10], ["paddingBottom", 4], ["paddingLeft", 10], ["borderRadius", 12], ["fontSize", 12], ["color", "#444444"]]))], ["status-tag", padStyleMapOf(utsMapOf([["backgroundColor", "#007aff"], ["color", "#FFFFFF"], ["paddingTop", 4], ["paddingRight", 10], ["paddingBottom", 4], ["paddingLeft", 10], ["fontSize", 12], ["borderRadius", 12]]))], ["order-price", padStyleMapOf(utsMapOf([["fontSize", 25], ["fontWeight", "bold"], ["color", "#0079fe"]]))], ["feedback-icon", padStyleMapOf(utsMapOf([["fontSize", 24], ["color", "#007aff"], ["cursor", "pointer"]]))], ["info-box", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "rgba(229,231,245,0.5)"], ["backdropFilter", "blur(25px)"], ["paddingTop", 30], ["paddingRight", 30], ["paddingBottom", 30], ["paddingLeft", 30], ["borderRadius", 15], ["marginTop", 15], ["borderWidth", 1], ["borderStyle", "solid"], ["borderColor", "#82b1ff"], ["boxShadow", "0 5px 15px rgba(0, 0, 0, 0.1)"]]))], ["info-item", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "row"], ["justifyContent", "space-between"], ["marginBottom", 8]]))], ["label", padStyleMapOf(utsMapOf([["fontSize", 17], ["fontWeight", "bold"]]))], ["value", padStyleMapOf(utsMapOf([["fontSize", 14], ["color", "#666666"]]))], ["divider", padStyleMapOf(utsMapOf([["width", "100%"], ["height", 1], ["backgroundColor", "#dddddd"], ["marginTop", 25], ["marginRight", 0], ["marginBottom", 25], ["marginLeft", 0]]))], ["feedback-btn", padStyleMapOf(utsMapOf([["borderRadius", 25], ["fontSize", "34rpx"], ["height", "96rpx"], ["backgroundImage", "linear-gradient(to right, #82b1ff, #007aff)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#FFFFFF"], ["boxShadow", "0 4px 10px rgba(0, 122, 255, 0.2)"]]))], ["button-group", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "row"], ["justifyContent", "space-between"], ["gap", "20rpx"], ["marginTop", 20]]))], ["half-button", padStyleMapOf(utsMapOf([["flex", 1], ["height", "96rpx"], ["borderRadius", 25], ["fontSize", "32rpx"], ["transform:active", "scale(0.98)"]]))], ["home-btn", padStyleMapOf(utsMapOf([["borderRadius", 25], ["fontSize", "34rpx"], ["height", "96rpx"], ["backgroundImage", "none"], ["backgroundColor", "#e7edf1"], ["color", "#0084ff"], ["borderWidth", 1], ["borderStyle", "solid"], ["borderColor", "#82b1ff"]]))]])]
