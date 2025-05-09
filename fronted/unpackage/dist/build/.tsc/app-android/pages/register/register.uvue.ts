
// 导入重构后的API
import { userApi } from '../../utils/api';

const __sfc__ = defineComponent({
  data() {
    return {
      username: '',
      email: '',
      phone_number: '',
      password: '',
      confirmPassword: '',
      showPassword: false, // 是否显示密码
      showConfirmPassword: false, // 是否显示确认密码
      focusedInput: '', // 选中的输入框
      usernameError: false,
      emailError: false,
      phoneError: false,
      passwordError: false,
      confirmPasswordError: false,
      usernameErrorMessage: '',
      emailErrorMessage: '',
      phoneErrorMessage: '',
      passwordErrorMessage: '',
      confirmPasswordErrorMessage: ''
    };
  },
  methods: {
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
    toggleConfirmPasswordVisibility() {
      this.showConfirmPassword = !this.showConfirmPassword;
    },

    checkUsername() {
      this.usernameError = this.username.length > 15;
      this.usernameErrorMessage = this.usernameError ? 'Username cannot exceed 15 characters.' : '';
    },

    checkEmail() {
      let emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      this.emailError = !emailRegex.test(this.email);
      this.emailErrorMessage = this.emailError ? 'Invalid email format.' : '';
    },

    checkPhoneNumber() {
      let phoneRegex = /^[1-9]\d{9,14}$/;
      this.phoneError = !phoneRegex.test(this.phone_number);
      this.phoneErrorMessage = this.phoneError ? 'Invalid phone number.' : '';
    },

    checkPassword() {
      this.passwordError = this.password.length > 15;
      this.passwordErrorMessage = this.passwordError ? 'Password cannot exceed 15 characters.' : '';
    },

    async handleRegister() {
      // 验证所有字段
      this.checkUsername();
      this.checkEmail();
      this.checkPhoneNumber();
      this.checkPassword();

      if (this.usernameError || this.emailError || this.phoneError || this.passwordError) {
        uni.showToast({ 
          title: 'Please correct the input error', 
          icon: 'none' 
        });
        return;
      }

      // 密码匹配检查
      if (this.password !== this.confirmPassword) {
        this.confirmPasswordError = true;
        this.confirmPasswordErrorMessage = 'Entered passwords differ!';
        uni.showToast({ 
          title: 'Entered passwords differ!', 
          icon: 'none' 
        });
        return;
      }

      try {
        const res = await userApi.register({
          username: this.username,
          password: this.password,
          email: this.email,
          phone_number: this.phone_number
        });

        if (res.code === 1) {  // 注意这里使用 1 表示成功
          uni.showToast({
            title: res.msg || 'registered successfully',
            icon: 'success'
          });
          
          // 注册成功后延迟跳转到登录页
          setTimeout(() => {
            this.goToLogin();
          }, 1500);
        } else {
          uni.showToast({
            title: res.msg || 'login has failed',
            icon: 'none'
          });
        }
      } catch (error) {
        uni.showToast({
          title: 'login has failed',
          icon: 'none'
        });
        console.error('login has failed:', error);
      }
    },

    goToLogin() {
      uni.navigateTo({ url: '/pages/login/login' });
    }
  }
});


export default __sfc__
function GenPagesRegisterRegisterRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return createElementVNode("view", utsMapOf({ class: "register-page" }), [
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
            onInput: ($event: InputEvent) => {(_ctx.username) = $event.detail.value},
            type: "text",
            placeholder: "Enter username",
            onFocus: () => {_ctx.focusedInput = 'username'},
            onBlur: () => {_ctx.focusedInput = ''; _ctx.checkUsername()}
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput", "onFocus", "onBlur"])
        ], 2 /* CLASS */),
        isTrue(_ctx.usernameError)
          ? createElementVNode("text", utsMapOf({
              key: 0,
              class: "error-text"
            }), toDisplayString(_ctx.usernameErrorMessage), 1 /* TEXT */)
          : createCommentVNode("v-if", true),
        createElementVNode("view", utsMapOf({
          class: normalizeClass(["input-wrapper", utsMapOf({ focused: _ctx.focusedInput === 'email' })])
        }), [
          createElementVNode("image", utsMapOf({
            class: "input-icon",
            src: "/static/icons/envelope-solid.svg",
            mode: "aspectFit"
          })),
          createElementVNode("input", utsMapOf({
            class: "uni-input",
            modelValue: _ctx.email,
            onInput: ($event: InputEvent) => {(_ctx.email) = $event.detail.value},
            type: "email",
            placeholder: "Enter email",
            onFocus: () => {_ctx.focusedInput = 'email'},
            onBlur: () => {_ctx.focusedInput = ''; _ctx.checkEmail()}
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput", "onFocus", "onBlur"])
        ], 2 /* CLASS */),
        isTrue(_ctx.emailError)
          ? createElementVNode("text", utsMapOf({
              key: 1,
              class: "error-text"
            }), toDisplayString(_ctx.emailErrorMessage), 1 /* TEXT */)
          : createCommentVNode("v-if", true),
        createElementVNode("view", utsMapOf({
          class: normalizeClass(["input-wrapper", utsMapOf({ focused: _ctx.focusedInput === 'phone' })])
        }), [
          createElementVNode("image", utsMapOf({
            class: "input-icon",
            src: "/static/icons/phone-solid.svg",
            mode: "aspectFit"
          })),
          createElementVNode("input", utsMapOf({
            class: "uni-input",
            modelValue: _ctx.phone_number,
            onInput: ($event: InputEvent) => {(_ctx.phone_number) = $event.detail.value},
            type: "number",
            placeholder: "Enter phone number",
            onFocus: () => {_ctx.focusedInput = 'phone'},
            onBlur: () => {_ctx.focusedInput = ''; _ctx.checkPhoneNumber()}
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput", "onFocus", "onBlur"])
        ], 2 /* CLASS */),
        isTrue(_ctx.phoneError)
          ? createElementVNode("text", utsMapOf({
              key: 2,
              class: "error-text"
            }), toDisplayString(_ctx.phoneErrorMessage), 1 /* TEXT */)
          : createCommentVNode("v-if", true),
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
              key: 3,
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
              key: 4,
              class: "error-text"
            }), toDisplayString(_ctx.confirmPasswordErrorMessage), 1 /* TEXT */)
          : createCommentVNode("v-if", true),
        createElementVNode("button", utsMapOf({
          class: "register-btn",
          onClick: _ctx.handleRegister
        }), " Sign up ", 8 /* PROPS */, ["onClick"])
      ])
    ]),
    createElementVNode("view", utsMapOf({ class: "tips-wrapper" }), [
      createElementVNode("text", utsMapOf({
        class: "tip-link",
        onClick: _ctx.goToLogin
      }), "Already have an account? Log in", 8 /* PROPS */, ["onClick"])
    ])
  ])
}
const GenPagesRegisterRegisterStyles = [utsMapOf([["register-page", padStyleMapOf(utsMapOf([["background", "linear-gradient(\r\n    to bottom,\r\n    rgba(0, 123, 255, 0.2) 0%,   \r\n    rgba(0, 123, 255, 0.1) 50%,  \r\n    rgba(255, 255, 255, 0) 100%  \r\n  )"], ["display", "flex"], ["flexDirection", "column"], ["height", "100%"]]))], ["header-illustration", padStyleMapOf(utsMapOf([["marginTop", "80rpx"], ["display", "flex"], ["justifyContent", "center"], ["alignItems", "center"]]))], ["illustration-img", padStyleMapOf(utsMapOf([["width", "50%"]]))], ["glass-container", padStyleMapOf(utsMapOf([["marginTop", "20rpx"], ["marginRight", "auto"], ["marginBottom", "auto"], ["marginLeft", "auto"], ["paddingTop", "40rpx"], ["paddingRight", "50rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "50rpx"], ["width", "90%"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.2)"], ["borderRadius", "60rpx"], ["boxShadow", "0px 10px 30px rgba(0, 0, 0, 0.1)"], ["backdropFilter", "blur(15px)"], ["textAlign", "center"]]))], ["input-wrapper", utsMapOf([["", utsMapOf([["position", "relative"], ["marginBottom", "25rpx"], ["backgroundColor", "rgba(255,255,255,0.6)"], ["boxShadow", "0px 4px 10px rgba(0, 0, 0, 0.1)"], ["borderRadius", "70rpx"], ["display", "flex"], ["flexDirection", "row"], ["alignItems", "center"], ["paddingTop", 0], ["paddingRight", "50rpx"], ["paddingBottom", 0], ["paddingLeft", "50rpx"], ["transitionDuration", "0.3s"]])], [".focused", utsMapOf([["borderWidth", "2rpx"], ["borderStyle", "solid"], ["borderColor", "#007aff"], ["backgroundColor", "#eaf5ff"]])]])], ["input-icon", padStyleMapOf(utsMapOf([["width", "40rpx"], ["height", "40rpx"], ["marginRight", "25rpx"]]))], ["uni-input", padStyleMapOf(utsMapOf([["flex", 1], ["height", "90rpx"], ["fontSize", "28rpx"], ["borderWidth", "medium"], ["borderStyle", "none"], ["borderColor", "#000000"], ["backgroundColor", "rgba(0,0,0,0)"]]))], ["eye-icon", padStyleMapOf(utsMapOf([["width", "36rpx"], ["height", "36rpx"], ["cursor", "pointer"], ["transitionProperty", "transform"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease-in-out"], ["transform:hover", "scale(1.1)"]]))], ["error-text", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#0073f1"], ["marginTop", "0rpx"], ["marginBottom", "30rpx"], ["marginLeft", "25rpx"]]))], ["register-btn", padStyleMapOf(utsMapOf([["marginTop", "30rpx"], ["height", "90rpx"], ["fontSize", "30rpx"], ["backgroundImage", "linear-gradient(to right, #82b1ff, #007aff)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#FFFFFF"], ["borderRadius", "70rpx"], ["transitionDuration", "0.3s"], ["transform:active", "scale(0.96)"]]))], ["tips-wrapper", padStyleMapOf(utsMapOf([["marginTop", "50rpx"], ["marginRight", "auto"], ["marginBottom", "auto"], ["marginLeft", "auto"], ["textAlign", "center"]]))], ["tip-link", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#666666"], ["transitionProperty", "color"], ["transitionDuration", "0.2s"], ["color:hover", "#005bb5"]]))], ["@TRANSITION", utsMapOf([["input-wrapper", utsMapOf([["duration", "0.3s"]])], ["eye-icon", utsMapOf([["property", "transform"], ["duration", "0.3s"], ["timingFunction", "ease-in-out"]])], ["register-btn", utsMapOf([["duration", "0.3s"]])], ["tip-link", utsMapOf([["property", "color"], ["duration", "0.2s"]])]])]])]
