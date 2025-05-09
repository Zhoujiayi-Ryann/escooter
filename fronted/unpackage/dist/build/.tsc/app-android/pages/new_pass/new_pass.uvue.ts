
import { passwordApi } from '../../utils/api/password/index.uts';

const __sfc__ = defineComponent({
  data() {
    return {
      email: '',
      code: '',
      password: '',
      confirmPassword: '',
      showPassword: false,
      showConfirmPassword: false,
      focusedInput: '',
      passwordError: false,
      confirmPasswordError: false,
      passwordErrorMessage: '',
      confirmPasswordErrorMessage: '',
      isLoading: false
    };
  },
  onLoad(options) {
    // 从URL参数获取邮箱和验证码
    if (options.email) {
      this.email = decodeURIComponent(options.email);
    }
    if (options.code) {
      this.code = decodeURIComponent(options.code);
    }
    
    // 如果没有邮箱或验证码，返回到忘记密码页面
    if (!this.email || !this.code) {
      uni.showToast({
        title: 'Missing required information',
        icon: 'none'
      });
      setTimeout(() => {
        uni.redirectTo({ url: '/pages/forget/forget' });
      }, 1500);
    }
  },
  methods: {
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
    toggleConfirmPasswordVisibility() {
      this.showConfirmPassword = !this.showConfirmPassword;
    },
    checkPassword() {
      this.passwordError = false;
      this.passwordErrorMessage = '';
      
      if (this.password.length < 6) {
        this.passwordError = true;
        this.passwordErrorMessage = 'Password must be at least 6 characters.';
      } else if (this.password.length > 20) {
        this.passwordError = true;
        this.passwordErrorMessage = 'Password cannot exceed 20 characters.';
      }
    },
    resetPassword() {
      this.checkPassword();

      if (this.passwordError) {
        uni.showToast({ title: 'Please fix password issues', icon: 'none' });
        return;
      }

      if (this.password !== this.confirmPassword) {
        this.confirmPasswordError = true;
        this.confirmPasswordErrorMessage = 'Passwords do not match.';
        uni.showToast({ title: 'Passwords do not match', icon: 'none' });
        return;
      }
      
      if (!this.email || !this.code) {
        uni.showToast({ title: 'Missing email or verification code', icon: 'none' });
        return;
      }

      // 清除错误状态
      this.confirmPasswordError = false;

      // 调用后端API重置密码
      this.isLoading = true;
      passwordApi.resetPassword(this.email, this.code, this.password)
        .then(res => {
          if (res.code === 1) {
            uni.showToast({ title: 'Password reset successfully!', icon: 'success' });
            setTimeout(() => uni.redirectTo({ url: '/pages/login/login' }), 1500);
          } else {
            uni.showToast({ title: res.msg || 'Failed to reset password', icon: 'none' });
          }
        })
        .catch(err => {
          uni.showToast({ title: 'Server error, please try again later', icon: 'none' });
          console.error('Password reset failed:', err);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    goBack() {
      uni.navigateTo({ url: '/pages/login/login' });
    }
  }
});

export default __sfc__
function GenPagesNewPassNewPassRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return createElementVNode("view", utsMapOf({ class: "forget-page" }), [
    createElementVNode("view", utsMapOf({ class: "header-illustration" }), [
      createElementVNode("image", utsMapOf({
        class: "illustration-img",
        src: "/static/login/login_illustration.png",
        mode: "widthFix"
      }))
    ]),
    createElementVNode("view", utsMapOf({ class: "glass-container" }), [
      createElementVNode("view", utsMapOf({ class: "form-container" }), [
        createElementVNode("text", utsMapOf({ class: "form-title" }), "Set a new password"),
        createElementVNode("text", utsMapOf({ class: "form-subtitle" }), "Please enter your new password"),
        createElementVNode("view", utsMapOf({
          class: normalizeClass(["input-wrapper", utsMapOf({ focused: _ctx.focusedInput === 'password' })])
        }), [
          createElementVNode("image", utsMapOf({
            class: "input-icon",
            src: "/static/icons/unlock-solid.svg",
            mode: "aspectFit"
          })),
          createElementVNode("input", utsMapOf({
            class: "uni-input",
            modelValue: _ctx.password,
            onInput: ($event: InputEvent) => {(_ctx.password) = $event.detail.value},
            type: _ctx.showPassword ? 'text' : 'password',
            placeholder: "Enter password",
            onFocus: () => {_ctx.focusedInput = 'password'},
            onBlur: () => {_ctx.focusedInput = ''; _ctx.checkPassword()}
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
              key: 0,
              class: "error-text"
            }), toDisplayString(_ctx.passwordErrorMessage), 1 /* TEXT */)
          : createCommentVNode("v-if", true),
        createElementVNode("view", utsMapOf({
          class: normalizeClass(["input-wrapper", utsMapOf({ focused: _ctx.focusedInput === 'confirmPassword' })])
        }), [
          createElementVNode("image", utsMapOf({
            class: "input-icon",
            src: "/static/icons/lock-solid.svg",
            mode: "aspectFit"
          })),
          createElementVNode("input", utsMapOf({
            class: "uni-input",
            modelValue: _ctx.confirmPassword,
            onInput: ($event: InputEvent) => {(_ctx.confirmPassword) = $event.detail.value},
            type: _ctx.showConfirmPassword ? 'text' : 'password',
            placeholder: "Confirm password",
            onFocus: () => {_ctx.focusedInput = 'confirmPassword'},
            onBlur: () => {_ctx.focusedInput = ''}
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput", "type", "onFocus", "onBlur"]),
          createElementVNode("image", utsMapOf({
            class: "eye-icon",
            src: _ctx.showConfirmPassword ? '/static/icons/eye-solid.svg' : '/static/icons/eye-slash-solid.svg',
            mode: "aspectFit",
            onClick: _ctx.toggleConfirmPasswordVisibility
          }), null, 8 /* PROPS */, ["src", "onClick"])
        ], 2 /* CLASS */),
        isTrue(_ctx.confirmPasswordError)
          ? createElementVNode("text", utsMapOf({
              key: 1,
              class: "error-text"
            }), toDisplayString(_ctx.confirmPasswordErrorMessage), 1 /* TEXT */)
          : createCommentVNode("v-if", true),
        createElementVNode("button", utsMapOf({
          class: "login-btn",
          onClick: _ctx.resetPassword,
          disabled: _ctx.isLoading
        }), toDisplayString(_ctx.isLoading ? 'Processing...' : 'Confirm'), 9 /* TEXT, PROPS */, ["onClick", "disabled"]),
        createElementVNode("view", utsMapOf({
          class: "tips-wrapper",
          style: normalizeStyle(utsMapOf({"justify-content":"center","margin-top":"40rpx"}))
        }), [
          createElementVNode("text", utsMapOf({
            class: "tip-link",
            onClick: _ctx.goBack
          }), "← Go to Login", 8 /* PROPS */, ["onClick"])
        ], 4 /* STYLE */)
      ])
    ])
  ])
}
const GenPagesNewPassNewPassStyles = [utsMapOf([["forget-page", padStyleMapOf(utsMapOf([["background", "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )"], ["display", "flex"], ["flexDirection", "column"], ["height", "100%"]]))], ["header-illustration", padStyleMapOf(utsMapOf([["marginTop", "80rpx"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"]]))], ["illustration-img", padStyleMapOf(utsMapOf([["width", "60%"]]))], ["glass-container", padStyleMapOf(utsMapOf([["marginTop", "60rpx"], ["marginRight", "auto"], ["marginBottom", "auto"], ["marginLeft", "auto"], ["paddingTop", "40rpx"], ["paddingRight", "40rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "40rpx"], ["width", "90%"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.2)"], ["borderRadius", "60rpx"], ["boxShadow", "0px 10px 30px rgba(0, 0, 0, 0.1)"], ["backdropFilter", "blur(15px)"], ["textAlign", "center"]]))], ["form-container", padStyleMapOf(utsMapOf([["marginTop", "20rpx"], ["paddingTop", 0], ["paddingRight", "20rpx"], ["paddingBottom", 0], ["paddingLeft", "20rpx"], ["display", "flex"], ["flexDirection", "column"]]))], ["input-wrapper", utsMapOf([["", utsMapOf([["position", "relative"], ["marginBottom", "25rpx"], ["backgroundColor", "rgba(255,255,255,0.6)"], ["boxShadow", "0px 4px 10px rgba(0, 0, 0, 0.1)"], ["borderRadius", "70rpx"], ["display", "flex"], ["flexDirection", "row"], ["alignItems", "center"], ["paddingTop", 0], ["paddingRight", "50rpx"], ["paddingBottom", 0], ["paddingLeft", "50rpx"], ["transitionDuration", "0.3s"]])], [".focused", utsMapOf([["borderWidth", "2rpx"], ["borderStyle", "solid"], ["borderColor", "#007aff"], ["backgroundColor", "#eaf5ff"]])]])], ["input-icon", padStyleMapOf(utsMapOf([["width", "40rpx"], ["height", "40rpx"], ["marginRight", "25rpx"]]))], ["uni-input", padStyleMapOf(utsMapOf([["flex", 1], ["height", "90rpx"], ["fontSize", "28rpx"], ["borderWidth", "medium"], ["borderStyle", "none"], ["borderColor", "#000000"], ["backgroundColor", "rgba(0,0,0,0)"]]))], ["eye-icon", padStyleMapOf(utsMapOf([["width", "36rpx"], ["height", "36rpx"], ["cursor", "pointer"], ["transitionProperty", "transform"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease-in-out"], ["transform:hover", "scale(1.1)"]]))], ["error-text", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#0073f1"], ["marginTop", "0rpx"], ["marginBottom", "30rpx"], ["marginLeft", "25rpx"]]))], ["login-btn", padStyleMapOf(utsMapOf([["marginTop", "40rpx"], ["height", "90rpx"], ["lineHeight", "90rpx"], ["fontSize", "30rpx"], ["backgroundImage", "linear-gradient(to right, #82b1ff, #007aff)"], ["backgroundColor", "rgba(0,0,0,0)"], ["boxShadow", "0px 4px 10px rgba(0, 122, 255, 0.3)"], ["color", "#ffffff"], ["borderRadius", "70rpx"], ["transitionDuration", "0.3s"], ["transform:active", "scale(0.96)"]]))], ["tips-wrapper", padStyleMapOf(utsMapOf([["marginLeft", "50rpx"], ["marginRight", "50rpx"], ["display", "flex"], ["flexDirection", "row"], ["justifyContent", "space-between"]]))], ["tip-link", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#666666"], ["transitionProperty", "color"], ["transitionDuration", "0.2s"], ["color:hover", "#005bb5"]]))], ["third-party-section", padStyleMapOf(utsMapOf([["marginTop", "auto"], ["marginRight", "auto"], ["marginBottom", "200rpx"], ["marginLeft", "auto"], ["textAlign", "center"]]))], ["third-party-text", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#999999"], ["marginTop", "auto"], ["marginRight", "auto"], ["marginBottom", "auto"], ["marginLeft", "auto"]]))], ["third-party-icons", padStyleMapOf(utsMapOf([["marginTop", "20rpx"], ["display", "flex"], ["flexDirection", "row"], ["justifyContent", "center"], ["gap", "40rpx"]]))], ["third-party-icon", padStyleMapOf(utsMapOf([["width", "60rpx"], ["height", "60rpx"], ["paddingTop", "15rpx"], ["paddingRight", "15rpx"], ["paddingBottom", "15rpx"], ["paddingLeft", "15rpx"], ["backgroundColor", "rgba(255,255,255,0.6)"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease-in-out"], ["transform:hover", "scale(1.05)"]]))], ["form-title", padStyleMapOf(utsMapOf([["fontSize", "45rpx"], ["fontWeight", "bold"], ["color", "#333333"], ["marginBottom", "20rpx"], ["textAlign", "center"]]))], ["form-subtitle", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#666666"], ["marginBottom", "40rpx"], ["textAlign", "center"]]))], ["@TRANSITION", utsMapOf([["input-wrapper", utsMapOf([["duration", "0.3s"]])], ["eye-icon", utsMapOf([["property", "transform"], ["duration", "0.3s"], ["timingFunction", "ease-in-out"]])], ["login-btn", utsMapOf([["duration", "0.3s"]])], ["tip-link", utsMapOf([["property", "color"], ["duration", "0.2s"]])], ["third-party-icon", utsMapOf([["duration", "0.3s"], ["timingFunction", "ease-in-out"]])]])]])]
