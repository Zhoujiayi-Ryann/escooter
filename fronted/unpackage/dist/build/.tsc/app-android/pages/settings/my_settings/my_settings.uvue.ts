
import { userApi } from '../../../utils/api/user/index.uts';

const __sfc__ = defineComponent({
  data() {
    return {
      user: {
        name: 'Guest',
        id: '0',
        avatar: ''
      },
      orderStatus: {
        unused: 1
      },
      mileage: {
        hours: 0,
        spent: 0
      }
    }
  },
  onLoad() {
    this.getUserInformation();
  },
  onShow() {
    // Refresh user information each time the page is shown to ensure up-to-date data
    this.getUserInformation();
  },
  methods: {
    async getUserInformation() {
      try {
        const userId = userApi.getUserId();
        if (!userId) {
          uni.showToast({ title: 'Not logged in', icon: 'none' });
          return;
        }
    
        // 拉取后端数据
        const res = await userApi.getUserDetail(userId);
        if (res.code === 1 && res.data) {
          const user = res.data;
    
          // 更新页面显示
          this.user.name = user.username || 'Guest';
          this.user.id = String(user.user_id || user.userId || 0);
    
          const avatarPath = user.avatar_path || user.avatarPath;
          this.user.avatar = avatarPath && avatarPath.trim() !== ''
            ? avatarPath
            : '/static/avatar/userp.jpg';
    
          this.mileage.hours = parseFloat(user.totalUsageHours || 0).toFixed(1);
          this.mileage.spent = parseFloat(user.totalSpent || 0);
    
          // 同步缓存
          userApi.updateLocalUserInfo(user);
          console.log('✅ Loaded user info from server:', this.user.avatar);
        } else {
          throw new Error(res.msg || 'Failed to load user');
        }
      } catch (e) {
        console.error('❌ Error loading user info:', e);
        // 回退缓存数据
        const cached = userApi.getUserInfo();
        if (cached) {
          this.user.name = cached.username;
          this.user.id = String(cached.user_id || cached.userId || 0);
          this.user.avatar = cached.avatar_path || '/static/avatar/userp.jpg';
        }
      }
    },
    handleLogout() {
      // Call API to log out
      userApi.logout();

      // Show toast message
      uni.showToast({
        title: 'Logged out',
        icon: 'success'
      });

      // Reset user data to guest state
      this.user.name = 'Guest';
      this.user.id = '0';

      // Navigate to login page after delay
      setTimeout(() => {
        uni.navigateTo({
          url: '/pages/login/login'
        });
      }, 1500);
    },
    goCard() {
      uni.navigateTo({
        url: '/pages/settings/card/card'
      });
    },
    navigateTo(page) {
      const map = {
        orders: '/pages/settings/orders/orders',
        card: '/pages/cards/cards',
        help: '/pages/help/help',
        notify: '/pages/notification/notification'
      };

      if (map[page]) {
        uni.navigateTo({ url: map[page] });
      }
    },

    goFeedback() {
      uni.navigateTo({
        url: '/pages/feedback/feedback'
      });
    },
    gochange() {
      uni.navigateTo({
        url: '/pages/settings/change/change'
      });
    }
  }
})

export default __sfc__
function GenPagesSettingsMySettingsMySettingsRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
const _component_van_image = resolveComponent("van-image")
const _component_van_cell = resolveComponent("van-cell")
const _component_van_notice_bar = resolveComponent("van-notice-bar")
const _component_van_icon = resolveComponent("van-icon")
const _component_van_col = resolveComponent("van-col")
const _component_van_row = resolveComponent("van-row")
const _component_van_grid_item = resolveComponent("van-grid-item")
const _component_van_grid = resolveComponent("van-grid")
const _component_van_cell_group = resolveComponent("van-cell-group")
const _component_van_tabbar_item = resolveComponent("van-tabbar-item")
const _component_van_tabbar = resolveComponent("van-tabbar")

  return createElementVNode("view", utsMapOf({ class: "container" }), [
    createElementVNode("view", utsMapOf({ class: "mine-page" }), [
      createVNode(_component_van_cell, utsMapOf({
        class: "user-info",
        center: ""
      }), utsMapOf({
        icon: withSlotCtx((): any[] => [
          createVNode(_component_van_image, utsMapOf({
            round: "",
            width: "60",
            height: "60",
            src: _ctx.user.avatar
          }), null, 8 /* PROPS */, ["src"])
        ]),
        title: withSlotCtx((): any[] => [
          createElementVNode("view", null, [
            createElementVNode("view", utsMapOf({ class: "username" }), toDisplayString(_ctx.user.name), 1 /* TEXT */),
            createElementVNode("view", utsMapOf({ class: "userid" }), "ID: " + toDisplayString(_ctx.user.id), 1 /* TEXT */)
          ])
        ]),
        _: 1 /* STABLE */
      })),
      createVNode(_component_van_notice_bar, utsMapOf({
        scrollable: "",
        text: "Member users will receive surprise benefits during holidays, and randomly drop out great value coupons! Up to £ 100 coupon, you can save more on riding!"
      })),
      createElementVNode("view", utsMapOf({ class: "mileage-card" }), [
        createVNode(_component_van_row, utsMapOf({ gutter: "20" }), utsMapOf({
          default: withSlotCtx((): any[] => [
            createVNode(_component_van_col, utsMapOf({
              span: "12",
              class: "mileage-item"
            }), utsMapOf({
              default: withSlotCtx((): any[] => [
                createVNode(_component_van_icon, utsMapOf({
                  name: "clock-o",
                  class: "mileage-icon"
                })),
                createElementVNode("view", utsMapOf({ class: "mileage-label" }), "Usage Hours"),
                createElementVNode("view", utsMapOf({ class: "mileage-value" }), toDisplayString(_ctx.mileage.hours), 1 /* TEXT */)
              ]),
              _: 1 /* STABLE */
            })),
            createVNode(_component_van_col, utsMapOf({
              span: "12",
              class: "mileage-item"
            }), utsMapOf({
              default: withSlotCtx((): any[] => [
                createVNode(_component_van_icon, utsMapOf({
                  name: "balance-o",
                  class: "mileage-icon"
                })),
                createElementVNode("view", utsMapOf({ class: "mileage-label" }), "Total Spent"),
                createElementVNode("view", utsMapOf({ class: "mileage-value" }), "￡" + toDisplayString(_ctx.mileage.spent.toFixed(2)), 1 /* TEXT */)
              ]),
              _: 1 /* STABLE */
            }))
          ]),
          _: 1 /* STABLE */
        }))
      ]),
      createElementVNode("view", utsMapOf({ class: "page-section" }), [
        createVNode(_component_van_grid, utsMapOf({
          "column-num": 4,
          border: ""
        }), utsMapOf({
          default: withSlotCtx((): any[] => [
            createVNode(_component_van_grid_item, utsMapOf({
              icon: "orders-o",
              text: "All Orders",
              onClick: () => {_ctx.navigateTo('orders')}
            }), null, 8 /* PROPS */, ["onClick"]),
            createVNode(_component_van_grid_item, utsMapOf({
              icon: "card",
              text: "My Card",
              onClick: () => {_ctx.navigateTo('card')}
            }), null, 8 /* PROPS */, ["onClick"]),
            createVNode(_component_van_grid_item, utsMapOf({
              icon: "service-o",
              text: "Help",
              onClick: () => {_ctx.navigateTo('help')}
            }), null, 8 /* PROPS */, ["onClick"]),
            createVNode(_component_van_grid_item, utsMapOf({
              icon: "bell",
              text: "Notifications",
              onClick: () => {_ctx.navigateTo('notify')}
            }), null, 8 /* PROPS */, ["onClick"])
          ]),
          _: 1 /* STABLE */
        })),
        createVNode(_component_van_cell_group, utsMapOf({ class: "function-group" }), utsMapOf({
          default: withSlotCtx((): any[] => [
            createVNode(_component_van_cell, utsMapOf({
              title: "Add Card",
              "is-link": "",
              size: "large",
              onClick: _ctx.goCard
            }), null, 8 /* PROPS */, ["onClick"]),
            createVNode(_component_van_cell, utsMapOf({
              title: "Feedback",
              "is-link": "",
              size: "large",
              onClick: _ctx.goFeedback
            }), null, 8 /* PROPS */, ["onClick"]),
            createVNode(_component_van_cell, utsMapOf({
              title: "Edit Profile",
              "is-link": "",
              size: "large",
              onClick: _ctx.gochange
            }), null, 8 /* PROPS */, ["onClick"]),
            createVNode(_component_van_cell, utsMapOf({
              title: "Log Out",
              "is-link": "",
              size: "large",
              onClick: _ctx.handleLogout
            }), null, 8 /* PROPS */, ["onClick"])
          ]),
          _: 1 /* STABLE */
        }))
      ]),
      createVNode(_component_van_tabbar, utsMapOf({
        route: "",
        fixed: ""
      }), utsMapOf({
        default: withSlotCtx((): any[] => [
          createVNode(_component_van_tabbar_item, utsMapOf({
            icon: "home-o",
            to: "/pages/home/home"
          }), utsMapOf({
            default: withSlotCtx((): any[] => ["Home"]),
            _: 1 /* STABLE */
          })),
          createVNode(_component_van_tabbar_item, utsMapOf({
            icon: "user-o",
            to: "/pages/settings/my_settings/my_settings"
          }), utsMapOf({
            default: withSlotCtx((): any[] => ["Mine"]),
            _: 1 /* STABLE */
          }))
        ]),
        _: 1 /* STABLE */
      }))
    ])
  ])
}
const GenPagesSettingsMySettingsMySettingsStyles = [utsMapOf([["mine-page", padStyleMapOf(utsMapOf([["background", "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["boxSizing", "border-box"], ["display", "flex"], ["flexDirection", "column"], ["gap", "15rpx"]]))], ["user-info", padStyleMapOf(utsMapOf([["marginTop", "100rpx"], ["borderRadius", "24rpx"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["boxShadow", "0 8px 20px rgba(0, 0, 0, 0.05)"], ["!backgroundImage", "none"], ["!backgroundColor", "rgba(255,255,255,0.95)"]]))], ["username", padStyleMapOf(utsMapOf([["fontSize", "34rpx"], ["fontWeight", "bold"], ["color", "#222222"], ["marginLeft", "25rpx"]]))], ["userid", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#777777"], ["marginTop", "6rpx"], ["marginLeft", "25rpx"]]))], ["van-notice-bar", padStyleMapOf(utsMapOf([["marginTop", "10rpx"], ["borderRadius", "16rpx"], ["height", "56rpx"], ["fontSize", "24rpx"]]))], ["van-grid", padStyleMapOf(utsMapOf([["borderRadius", "20rpx"], ["overflow", "hidden"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.95)"], ["boxShadow", "0 4px 15px rgba(0,0,0,0.05)"]]))], ["van-grid-item__content", padStyleMapOf(utsMapOf([["paddingTop", "35rpx"], ["paddingRight", 0], ["paddingBottom", "35rpx"], ["paddingLeft", 0]]))], ["van-grid-item__icon", padStyleMapOf(utsMapOf([["!fontSize", "44rpx"]]))], ["van-grid-item__text", padStyleMapOf(utsMapOf([["fontSize", "26rpx"]]))], ["page-section", padStyleMapOf(utsMapOf([["marginTop", "20rpx"], ["display", "flex"], ["flexDirection", "column"], ["gap", "45rpx"]]))], ["mileage-card", padStyleMapOf(utsMapOf([["borderRadius", "20rpx"], ["paddingTop", "30rpx"], ["paddingRight", 0], ["paddingBottom", "30rpx"], ["paddingLeft", 0], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.95)"], ["boxShadow", "0 4px 12px rgba(0,0,0,0.05)"], ["marginTop", "10rpx"]]))], ["mileage-item", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["mileage-icon", padStyleMapOf(utsMapOf([["fontSize", "50rpx"], ["color", "#00cc66"], ["marginBottom", "10rpx"]]))], ["mileage-value", padStyleMapOf(utsMapOf([["fontSize", "32rpx"], ["fontWeight", "bold"], ["color", "#333333"]]))], ["mileage-label", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#777777"]]))], ["function-group", padStyleMapOf(utsMapOf([["!borderRadius", "18rpx"], ["marginTop", "20rpx"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.95)"], ["boxShadow", "0 4px 15px rgba(0,0,0,0.05)"], ["!paddingLeft", 0], ["!paddingRight", 0], ["overflow", "hidden"]]))], ["van-cell", utsMapOf([[".function-group ", utsMapOf([["!fontSize", "30rpx"], ["!color", "#333333"], ["marginBottom", "30rpx"], ["borderBottomWidth::after", 1], ["borderBottomStyle::after", "solid"], ["borderBottomColor::after", "rgba(0,0,0,0.05)"]])]])], ["van-tabbar", padStyleMapOf(utsMapOf([["boxShadow", "0 -2px 12px rgba(0,0,0,0.05)"], ["height", "100rpx"]]))], ["van-tabbar-item", padStyleMapOf(utsMapOf([["fontSize", "26rpx"]]))], ["van-tabbar-item__icon", padStyleMapOf(utsMapOf([["fontSize", "38rpx"]]))], ["van-tabbar-item--active", padStyleMapOf(utsMapOf([["!color", "#007aff"]]))], ["van-badge", padStyleMapOf(utsMapOf([["transform", "translate(5rpx,-5rpx)"]]))]])]
