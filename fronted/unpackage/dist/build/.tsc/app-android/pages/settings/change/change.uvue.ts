
import { userApi } from '@/utils/api/user';
import { uploadApi } from '@/utils/api/upload';

const __sfc__ = defineComponent({
  data() {
    return {
      editing: '',
      user: {},
      avatar_path: '',
      form: {
        name: '',
        email: '',
        phone: '',
        avatar: '',
      },
      defaultAvatar: '/static/settings/userp.jpg',
    };
  },
  onLoad() {
    // Load cached data first to immediately show avatar and user info
    const cachedUser = userApi.getUserInfo();
    if (cachedUser) {
      this.user = cachedUser;
      this.form.name = cachedUser.username || '';
      this.form.email = cachedUser.email || '';
      this.form.phone = cachedUser.phoneNumber || '';
      this.form.avatar = cachedUser.avatar_path || '';
    }

    // Then fetch the latest data from the backend asynchronously
    this.loadUserInfo();
  },
  onShow() {
    this.loadUserInfo();
  },

  computed: {
    avatarSrc() {
      const path = this.user.avatar;
      console.log('🖼 avatar_path is:', path);
      return path && path.trim() !== '' ? path : this.defaultAvatar;
    }
  },
  methods: {
    async loadUserInfo() {
      try {
        const userId = userApi.getUserId();
        console.log("🧾 Retrieved userId:", userId);

        if (!userId || userId === 0) {
          uni.showToast({ title: 'Not logged in', icon: 'none' });
          console.warn('❌ Invalid userId, might not be logged in or cached info missing');
          return;
        }

        const res = await userApi.getUserDetail(userId);
        console.log("📦 User detail API response:", res);

        if (res.code === 1 && res.data) {
          this.user = res.data;
          this.form.name = res.data.username || '';
          this.form.email = res.data.email || '';
          this.form.phone = res.data.phoneNumber || '';
          this.form.avatar = res.data.avatar_path || '';
          const avatarPath = res.data.avatarPath;
          this.user.avatar = avatarPath && avatarPath.trim() !== ''
            ? avatarPath
            : '/static/avatar/userp.jpg';
          console.log('👤 Current user avatar path:', this.user.avatar);

          console.log('✅ User data loaded successfully:', this.user);
        } else {
          console.error("❌ Failed to load, backend returned error:", res);
          uni.showToast({ title: `Load failed: ${res.msg || 'Unknown error'}`, icon: 'none' });
        }
      } catch (err) {
        console.error("🔥 Error loading user info:", err);
        uni.showToast({ title: 'Network request failed', icon: 'none' });
      }
    },

    async chooseAvatar() {
      try {
        const res = await uni.chooseImage({ count: 1 });
        const filePath = res.tempFilePaths[0];

        const userId = userApi.getUserId();

        // 👉 Actually upload the avatar to server
        const uploadRes = await uploadApi.uploadAvatar(filePath, userId);

        if (uploadRes.code === 1) {
          const newAvatarUrl = uploadRes.data; // URL returned by API after upload

          // Update avatar locally
          this.user.avatar_path = newAvatarUrl;
          this.user.avatar = newAvatarUrl; // ✅ Update displayed avatar
          this.form.avatar = newAvatarUrl;

          // Update local cache
          userApi.updateLocalUserInfo({ avatar_path: newAvatarUrl });

          uni.showToast({ title: 'Avatar updated', icon: 'success' });
        } else {
          uni.showToast({ title: uploadRes.msg || 'Avatar upload failed', icon: 'none' });
        }
      } catch (err) {
        console.error('❌ Avatar upload error:', err);
        uni.showToast({ title: 'Upload failed', icon: 'none' });
      }
    },

async toggleEdit(field) {
  if (this.editing === field) {
    // Save operation
    const userId = userApi.getUserId();

    if (field === 'email' && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)) {
      uni.showToast({ title: 'Invalid email', icon: 'none' });
      return;
    }

    if (field === 'phone' && !/^\d{6,15}$/.test(this.form.phone)) {
      uni.showToast({ title: 'Invalid phone', icon: 'none' });
      return;
    }

    const dataMap = {
      name: { username: this.form.name },
      email: { email: this.form.email },
      phone: { phone_number: this.form.phone },
    };

    try {
      const res = await userApi.updateUserInfo(userId, dataMap[field]);
      console.log('🧾 Update response:', res);

      if (res.code === 1) {
        this.user = res.data;
        userApi.updateLocalUserInfo(res.data);
        uni.showToast({ title: 'Updated successfully', icon: 'success' });
        this.editing = '';
      } else {
        const msg = res.msg?.toLowerCase() || '';
        if (msg.includes('exists') || msg.includes('already')) {
          uni.showToast({
            title: 'This value has been used, please try another',
            icon: 'none'
          });
        } else {
          uni.showToast({
            title: res.msg || 'Update failed',
            icon: 'none'
          });
        }
      }
    } catch (e) {
      console.error('❌ Update request failed:', e);
      uni.showToast({ title: 'Request failed', icon: 'none' });
    }

  } else {
    this.form[field] = field === 'name'
      ? this.user.username
      : field === 'email'
        ? this.user.email
        : this.user.phoneNumber;
    this.editing = field;
  }
},

    goToChangePassword() {
      uni.navigateTo({ url: '/pages/settings/change_password/change_password' });
    },

    handleBack() {
      const pages = getCurrentPages();
      if (pages.length > 1) {
        uni.navigateBack();
      } else {
        uni.reLaunch({ url: '/pages/settings/my_settings/my_settings' });
      }
    },

    async updateField(key, value) {
      const userId = userApi.getUserId();
      const data = {};
      data[key] = value;
      const res = await userApi.updateUserInfo(userId, data);
      if (res.code === 1) {
        this.user = res.data;
      }
    },
  }
});

export default __sfc__
function GenPagesSettingsChangeChangeRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
const _component_van_icon = resolveComponent("van-icon")
const _component_van_button = resolveComponent("van-button")

  return createElementVNode("view", utsMapOf({ class: "edit-profile-page" }), [
    createElementVNode("view", utsMapOf({ class: "header" }), [
      createElementVNode("view", utsMapOf({
        class: "back-btn",
        onClick: _ctx.handleBack
      }), [
        createVNode(_component_van_icon, utsMapOf({
          name: "arrow-left",
          class: "back-icon"
        }))
      ], 8 /* PROPS */, ["onClick"]),
      createElementVNode("text", utsMapOf({ class: "header-title" }), "Edit Profile")
    ]),
    createElementVNode("view", utsMapOf({ class: "form-item avatar-form" }), [
      createElementVNode("view", utsMapOf({ class: "label" }), "Avatar"),
      createElementVNode("view", utsMapOf({
        class: "avatar-preview",
        onClick: _ctx.chooseAvatar
      }), [
        createElementVNode("image", utsMapOf({
          class: "avatar-img",
          src: _ctx.avatarSrc,
          mode: "aspectFill"
        }), null, 8 /* PROPS */, ["src"])
      ], 8 /* PROPS */, ["onClick"])
    ]),
    createElementVNode("view", utsMapOf({
      class: normalizeClass(["form-item", utsMapOf({ focused: _ctx.editing === 'name' })])
    }), [
      createElementVNode("view", utsMapOf({ class: "label" }), "Name"),
      _ctx.editing === 'name'
        ? createElementVNode("input", utsMapOf({
            key: 0,
            modelValue: _ctx.form.name,
            onInput: ($event: InputEvent) => {(_ctx.form.name) = $event.detail.value},
            class: "input",
            placeholder: "Enter name"
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
        : createElementVNode("view", utsMapOf({
            key: 1,
            class: "value"
          }), toDisplayString(_ctx.user.username), 1 /* TEXT */),
      createVNode(_component_van_button, utsMapOf({
        size: "small",
        type: _ctx.editing === 'name' ? 'success' : 'primary',
        class: "edit-btn",
        onClick: () => {_ctx.toggleEdit('name')}
      }), utsMapOf({
        default: withSlotCtx((): any[] => [toDisplayString(_ctx.editing === 'name' ? 'Save' : 'Edit')]),
        _: 1 /* STABLE */
      }), 8 /* PROPS */, ["type", "onClick"])
    ], 2 /* CLASS */),
    createElementVNode("view", utsMapOf({
      class: normalizeClass(["form-item", utsMapOf({ focused: _ctx.editing === 'email' })])
    }), [
      createElementVNode("view", utsMapOf({ class: "label" }), "Email"),
      _ctx.editing === 'email'
        ? createElementVNode("input", utsMapOf({
            key: 0,
            modelValue: _ctx.form.email,
            onInput: ($event: InputEvent) => {(_ctx.form.email) = $event.detail.value},
            class: "input",
            placeholder: "Enter email"
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
        : createElementVNode("view", utsMapOf({
            key: 1,
            class: "value"
          }), toDisplayString(_ctx.user.email), 1 /* TEXT */),
      createVNode(_component_van_button, utsMapOf({
        size: "small",
        type: _ctx.editing === 'email' ? 'success' : 'primary',
        class: "edit-btn",
        onClick: () => {_ctx.toggleEdit('email')}
      }), utsMapOf({
        default: withSlotCtx((): any[] => [toDisplayString(_ctx.editing === 'email' ? 'Save' : 'Edit')]),
        _: 1 /* STABLE */
      }), 8 /* PROPS */, ["type", "onClick"])
    ], 2 /* CLASS */),
    createElementVNode("view", utsMapOf({
      class: normalizeClass(["form-item", utsMapOf({ focused: _ctx.editing === 'phone' })])
    }), [
      createElementVNode("view", utsMapOf({ class: "label" }), "Phone"),
      _ctx.editing === 'phone'
        ? createElementVNode("input", utsMapOf({
            key: 0,
            modelValue: _ctx.form.phone,
            onInput: ($event: InputEvent) => {(_ctx.form.phone) = $event.detail.value},
            class: "input",
            placeholder: "Enter phone"
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
        : createElementVNode("view", utsMapOf({
            key: 1,
            class: "value"
          }), toDisplayString(_ctx.user.phoneNumber), 1 /* TEXT */),
      createVNode(_component_van_button, utsMapOf({
        size: "small",
        type: _ctx.editing === 'phone' ? 'success' : 'primary',
        class: "edit-btn",
        onClick: () => {_ctx.toggleEdit('phone')}
      }), utsMapOf({
        default: withSlotCtx((): any[] => [toDisplayString(_ctx.editing === 'phone' ? 'Save' : 'Edit')]),
        _: 1 /* STABLE */
      }), 8 /* PROPS */, ["type", "onClick"])
    ], 2 /* CLASS */),
    createElementVNode("view", utsMapOf({ class: "form-item" }), [
      createElementVNode("view", utsMapOf({ class: "label" }), "Password"),
      createElementVNode("view", utsMapOf({ class: "value" }), "************"),
      createVNode(_component_van_button, utsMapOf({
        size: "small",
        type: "primary",
        class: "edit-btn",
        onClick: _ctx.goToChangePassword
      }), utsMapOf({
        default: withSlotCtx((): any[] => [" Edit "]),
        _: 1 /* STABLE */
      }), 8 /* PROPS */, ["onClick"])
    ]),
    createElementVNode("view", utsMapOf({ class: "bottom-hint" }), [
      createElementVNode("text", null, "Remember to save your changes.")
    ])
  ])
}
const GenPagesSettingsChangeChangeStyles = [utsMapOf([["edit-profile-page", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["paddingTop", "40rpx"], ["paddingRight", "40rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "40rpx"], ["backgroundImage", "linear-gradient(to bottom, #e0f0ff, #ffffff)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxSizing", "border-box"]]))], ["back-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["top", "25rpx"], ["left", "30rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["zIndex", 10], ["backgroundColor", "#f4f8ff"], ["borderRadius", "25rpx"], ["width", "80rpx"], ["height", "80rpx"]]))], ["back-icon", padStyleMapOf(utsMapOf([["fontSize", "55rpx"], ["color", "#0084ff"]]))], ["header", padStyleMapOf(utsMapOf([["backgroundColor", "#ffffff"], ["paddingTop", "40rpx"], ["paddingRight", 0], ["paddingBottom", "40rpx"], ["paddingLeft", 0], ["textAlign", "center"], ["borderRadius", "20rpx"], ["boxShadow", "0 4px 12px rgba(0, 0, 0, 0.05)"]]))], ["header-title", padStyleMapOf(utsMapOf([["fontSize", "40rpx"], ["fontWeight", "bold"], ["color", "#007aff"], ["textAlign", "center"]]))], ["page-title", padStyleMapOf(utsMapOf([["fontSize", "40rpx"], ["fontWeight", "bold"], ["color", "#007aff"]]))], ["avatar-section", padStyleMapOf(utsMapOf([["textAlign", "center"], ["marginBottom", "60rpx"]]))], ["avatar-img", padStyleMapOf(utsMapOf([["width", "100%"], ["height", "100%"], ["objectFit", "cover"]]))], ["avatar-label", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["color", "#777777"], ["marginTop", "12rpx"]]))], ["form-item", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.95)"], ["marginBottom", "30rpx"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["borderRadius", "24rpx"], ["boxShadow", "0 4px 12px rgba(0, 0, 0, 0.05)"], ["position", "relative"]]))], ["label", padStyleMapOf(utsMapOf([["fontSize", "30rpx"], ["fontWeight", "bold"], ["marginBottom", "10rpx"], ["color", "#333333"]]))], ["value", padStyleMapOf(utsMapOf([["fontSize", "32rpx"], ["color", "#666666"], ["transitionProperty", "color"], ["transitionDuration", "0.3s"]]))], ["input", padStyleMapOf(utsMapOf([["fontSize", "32rpx"], ["paddingTop", "20rpx"], ["paddingRight", 0], ["paddingBottom", "20rpx"], ["paddingLeft", 0], ["borderWidth", "medium"], ["borderStyle", "none"], ["borderColor", "#000000"], ["borderBottomWidth", "2rpx"], ["borderBottomStyle", "solid"], ["borderBottomColor", "#007aff"], ["width", "100%"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#111111"], ["transitionProperty", "borderColor"], ["transitionDuration", "0.3s"]]))], ["edit-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["right", "30rpx"], ["top", "40%"], ["transform", "translateY(-50%)"], ["fontSize", "26rpx"], ["paddingTop", 0], ["paddingRight", "20rpx"], ["paddingBottom", 0], ["paddingLeft", "20rpx"], ["height", "56rpx"], ["lineHeight", "56rpx"], ["borderRadius", "28rpx"], ["opacity:active", 0.8], ["transform:active", "translateY(-50%) scale(0.97)"]]))], ["focused", padStyleMapOf(utsMapOf([["borderWidth", "2rpx"], ["borderStyle", "solid"], ["borderColor", "#007aff"], ["backgroundColor", "#eaf5ff"], ["boxShadow", "0 0 12rpx rgba(0, 122, 255, 0.15)"]]))], ["avatar-form", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "row"], ["alignItems", "center"], ["justifyContent", "space-between"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["borderRadius", "24rpx"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.95)"], ["marginTop", "30rpx"], ["boxShadow", "0 4px 12px rgba(0, 0, 0, 0.05)"]]))], ["avatar-preview", padStyleMapOf(utsMapOf([["width", "100rpx"], ["height", "100rpx"], ["overflow", "hidden"], ["flexShrink", 0]]))], ["bottom-hint", padStyleMapOf(utsMapOf([["width", "100%"], ["textAlign", "center"], ["alignItems", "center"], ["fontSize", "28rpx"], ["color", "#888888"], ["paddingTop", "40rpx"], ["paddingRight", 0], ["paddingBottom", "40rpx"], ["paddingLeft", 0], ["marginTop", "auto"]]))], ["@TRANSITION", utsMapOf([["value", utsMapOf([["property", "color"], ["duration", "0.3s"]])], ["input", utsMapOf([["property", "borderColor"], ["duration", "0.3s"]])]])]])]
