
// Import the refactored API
import { userApi, setToken } from '../../utils/api';

const __sfc__ = defineComponent({
  data() {
    return {
      username: '',
      password: '',
      showPassword: false, // Whether to show password
      usernameError: false, // Username length error
      passwordError: false, // Password length error
      focusedInput: ''
    };
  },
  methods: {
    async handleLogin() {
      // Form validation
      if (!this.username || !this.password) {
        uni.showToast({
          title: 'Please enter username and password',
          icon: 'none'
        });
        return;
      }

      try {
        const res = await userApi.login({
          username: this.username,
          password: this.password
        });

        if (res.code === 1) {
          // Show loading message
          uni.showLoading({
            title: 'Logging in...'
          });

          // Save token
          setToken(res.data.token);

          // Get detailed user info
          try {
            const detailRes = await userApi.getUserDetail(res.data.user_id);
            if (detailRes.code === 1) {
              const fullUserInfo = {
                ...detailRes.data,
                token: res.data.token
              };
              userApi.saveUserInfo(fullUserInfo);
              console.log('Saved full user info:', fullUserInfo);
            } else {
              const basicUserInfo = {
                user_id: res.data.user_id,
                username: res.data.username,
                token: res.data.token
              };
              userApi.saveUserInfo(basicUserInfo);
              console.log('Failed to fetch details. Saved basic user info.');
            }
          } catch (detailError) {
            console.error('Failed to get user details:', detailError);
            const basicUserInfo = {
              user_id: res.data.user_id,
              username: res.data.username,
              token: res.data.token
            };
            userApi.saveUserInfo(basicUserInfo);
            console.log('Error while fetching details. Saved basic info.');
          }

          // Hide loading
          uni.hideLoading();

          // Show success message
          uni.showToast({
            title: res.msg || 'Login successful',
            icon: 'success'
          });

          // Redirect to homepage
          setTimeout(() => {
            uni.reLaunch({
              url: '/pages/home/home'
            });
          }, 1500);
        } else {
          uni.showToast({
            title: res.msg || 'Login failed',
            icon: 'none'
          });
        }
      } catch (error) {
        uni.showToast({
          title: 'Login failed. Please try again later.',
          icon: 'none'
        });
        console.error('Login error:', error);
      }
    },

    // Toggle password visibility
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },

    goToRegister() {
      uni.navigateTo({
        url: '/pages/register/register'
      });
    },

    // Validate username length
    validateUsername() {
      this.usernameError = this.username.length > 15;
    },

    // Validate password length
    validatePassword() {
      this.passwordError = this.password.length > 15;
    },

    forgetPassword() {
      uni.navigateTo({
        url: '/pages/forget/forget'
      });
    }

    // Social login stubs (optional):
    // loginWithWeChat() {
    //   uni.showToast({ title: 'Logging in with WeChat', icon: 'none' });
    // },
    // loginWithQQ() {
    //   uni.showToast({ title: 'Logging in with QQ', icon: 'none' });
    // },
    // loginWithWeibo() {
    //   uni.showToast({ title: 'Logging in with Weibo', icon: 'none' });
    // }
  }
});

export default __sfc__
function GenPagesLoginLoginRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return createElementVNode("view", utsMapOf({ class: "login-page" }), [
    createElementVNode("view", utsMapOf({ class: "header-illustration" }), [
      createElementVNode("image", utsMapOf({
        class: "illustration-img",
        src: "/static/login/login_illustration.png",
        mode: "widthFix"
      }))
    ]),
    createElementVNode("view", utsMapOf({ class: "glass-container" }), [
      createElementVNode("view", utsMapOf({ class: "form-container" }), [
        createElementVNode("view", utsMapOf({
          class: normalizeClass(["input-wrapper", utsMapOf({ focused: _ctx.focusedInput === 'username' })])
        }), [
          createElementVNode("image", utsMapOf({
            class: "input-icon",
            src: "/static/icons/user-solid.svg",
            mode: "aspectFit"
          })),
          createElementVNode("input", utsMapOf({
            class: "uni-input",
            modelValue: _ctx.username,
            onInput: [($event: InputEvent) => {(_ctx.username) = $event.detail.value}, _ctx.validateUsername],
            type: "text",
            placeholder: "Enter username",
            onFocus: () => {_ctx.focusedInput = 'username'},
            onBlur: () => {_ctx.focusedInput = ''}
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput", "onFocus", "onBlur"])
        ], 2 /* CLASS */),
        isTrue(_ctx.usernameError)
          ? createElementVNode("text", utsMapOf({
              key: 0,
              class: "error-text"
            }), "Username cannot exceed 15 characters.")
          : createCommentVNode("v-if", true),
        createElementVNode("view", utsMapOf({
          class: normalizeClass(["input-wrapper", utsMapOf({ focused: _ctx.focusedInput === 'password' })])
        }), [
          createElementVNode("image", utsMapOf({
            class: "input-icon",
            src: "/static/icons/lock-solid.svg",
            mode: "aspectFit"
          })),
          createElementVNode("input", utsMapOf({
            class: "uni-input",
            modelValue: _ctx.password,
            onInput: [($event: InputEvent) => {(_ctx.password) = $event.detail.value}, _ctx.validatePassword],
            type: _ctx.showPassword ? 'text' : 'password',
            placeholder: "Enter password",
            onFocus: () => {_ctx.focusedInput = 'password'},
            onBlur: () => {_ctx.focusedInput = ''}
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput", "type", "onFocus", "onBlur"]),
          createElementVNode("image", utsMapOf({
            class: "eye-icon",
            src: _ctx.showPassword ? '/static/icons/eye-solid.svg' : '/static/icons/eye-slash-solid.svg',
            mode: "aspectFit",
            onClick: _ctx.togglePasswordVisibility
          }), null, 8 /* PROPS */, ["src", "onClick"])
        ], 2 /* CLASS */),
        isTrue(_ctx.passwordError)
          ? createElementVNode("text", utsMapOf({
              key: 1,
              class: "error-text"
            }), "Password cannot exceed 15 characters.")
          : createCommentVNode("v-if", true),
        createElementVNode("view", utsMapOf({ class: "tips-wrapper" }), [
          createElementVNode("text", utsMapOf({
            class: "tip-link",
            onClick: _ctx.goToRegister
          }), "Sign up", 8 /* PROPS */, ["onClick"]),
          createElementVNode("text", utsMapOf({
            class: "tip-link",
            onClick: _ctx.forgetPassword
          }), "Forgot password?", 8 /* PROPS */, ["onClick"])
        ]),
        createElementVNode("button", utsMapOf({
          class: "login-btn",
          onClick: _ctx.handleLogin
        }), " Log in ", 8 /* PROPS */, ["onClick"])
      ])
    ])
  ])
}
const GenPagesLoginLoginStyles = [utsMapOf([["login-page", padStyleMapOf(utsMapOf([["background", "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )"], ["display", "flex"], ["flexDirection", "column"], ["height", "100%"]]))], ["header-illustration", padStyleMapOf(utsMapOf([["marginTop", "80rpx"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"]]))], ["illustration-img", padStyleMapOf(utsMapOf([["width", "60%"]]))], ["glass-container", padStyleMapOf(utsMapOf([["marginTop", "60rpx"], ["marginRight", "auto"], ["marginBottom", "auto"], ["marginLeft", "auto"], ["paddingTop", "40rpx"], ["paddingRight", "40rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "40rpx"], ["width", "90%"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.2)"], ["borderRadius", "60rpx"], ["boxShadow", "0px 10px 30px rgba(0, 0, 0, 0.1)"], ["backdropFilter", "blur(15px)"], ["textAlign", "center"]]))], ["form-container", padStyleMapOf(utsMapOf([["marginTop", "20rpx"], ["paddingTop", 0], ["paddingRight", "20rpx"], ["paddingBottom", 0], ["paddingLeft", "20rpx"], ["display", "flex"], ["flexDirection", "column"]]))], ["input-wrapper", utsMapOf([["", utsMapOf([["position", "relative"], ["marginBottom", "25rpx"], ["backgroundColor", "rgba(255,255,255,0.6)"], ["boxShadow", "0px 4px 10px rgba(0, 0, 0, 0.1)"], ["borderRadius", "70rpx"], ["display", "flex"], ["flexDirection", "row"], ["alignItems", "center"], ["paddingTop", 0], ["paddingRight", "50rpx"], ["paddingBottom", 0], ["paddingLeft", "50rpx"], ["transitionDuration", "0.3s"]])], [".focused", utsMapOf([["borderWidth", "2rpx"], ["borderStyle", "solid"], ["borderColor", "#007aff"], ["backgroundColor", "#eaf5ff"]])]])], ["input-icon", padStyleMapOf(utsMapOf([["width", "40rpx"], ["height", "40rpx"], ["marginRight", "25rpx"]]))], ["uni-input", padStyleMapOf(utsMapOf([["flex", 1], ["height", "90rpx"], ["fontSize", "28rpx"], ["borderWidth", "medium"], ["borderStyle", "none"], ["borderColor", "#000000"], ["backgroundColor", "rgba(0,0,0,0)"]]))], ["eye-icon", padStyleMapOf(utsMapOf([["width", "36rpx"], ["height", "36rpx"], ["cursor", "pointer"], ["transitionProperty", "transform"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease-in-out"], ["transform:hover", "scale(1.1)"]]))], ["error-text", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#0073f1"], ["marginTop", "0rpx"], ["marginBottom", "30rpx"], ["marginLeft", "25rpx"]]))], ["login-btn", padStyleMapOf(utsMapOf([["marginTop", "40rpx"], ["height", "90rpx"], ["lineHeight", "90rpx"], ["fontSize", "30rpx"], ["backgroundImage", "linear-gradient(to right, #82b1ff, #007aff)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxShadow", "0px 4px 10px rgba(0, 122, 255, 0.3)"], ["color", "#ffffff"], ["borderRadius", "70rpx"], ["transitionDuration", "0.3s"], ["transform:active", "scale(0.96)"]]))], ["tips-wrapper", padStyleMapOf(utsMapOf([["marginLeft", "50rpx"], ["marginRight", "50rpx"], ["display", "flex"], ["flexDirection", "row"], ["justifyContent", "space-between"]]))], ["tip-link", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#666666"], ["transitionProperty", "color"], ["transitionDuration", "0.2s"], ["color:hover", "#005bb5"]]))], ["third-party-section", padStyleMapOf(utsMapOf([["marginTop", "auto"], ["marginRight", "auto"], ["marginBottom", "200rpx"], ["marginLeft", "auto"], ["textAlign", "center"]]))], ["third-party-text", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#999999"], ["marginTop", "auto"], ["marginRight", "auto"], ["marginBottom", "auto"], ["marginLeft", "auto"]]))], ["third-party-icons", padStyleMapOf(utsMapOf([["marginTop", "20rpx"], ["display", "flex"], ["flexDirection", "row"], ["justifyContent", "center"], ["gap", "40rpx"]]))], ["third-party-icon", padStyleMapOf(utsMapOf([["width", "60rpx"], ["height", "60rpx"], ["paddingTop", "15rpx"], ["paddingRight", "15rpx"], ["paddingBottom", "15rpx"], ["paddingLeft", "15rpx"], ["backgroundColor", "rgba(255,255,255,0.6)"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease-in-out"], ["transform:hover", "scale(1.05)"]]))], ["@TRANSITION", utsMapOf([["input-wrapper", utsMapOf([["duration", "0.3s"]])], ["eye-icon", utsMapOf([["property", "transform"], ["duration", "0.3s"], ["timingFunction", "ease-in-out"]])], ["login-btn", utsMapOf([["duration", "0.3s"]])], ["tip-link", utsMapOf([["property", "color"], ["duration", "0.2s"]])], ["third-party-icon", utsMapOf([["duration", "0.3s"], ["timingFunction", "ease-in-out"]])]])]])]
