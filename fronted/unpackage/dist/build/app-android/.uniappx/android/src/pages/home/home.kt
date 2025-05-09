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
import io.dcloud.uniapp.extapi.`$off` as uni__off;
import io.dcloud.uniapp.extapi.`$on` as uni__on;
import io.dcloud.uniapp.extapi.getLocation as uni_getLocation;
import io.dcloud.uniapp.extapi.getSystemInfoSync as uni_getSystemInfoSync;
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading;
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo;
import io.dcloud.uniapp.extapi.onCompassChange as uni_onCompassChange;
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading;
import io.dcloud.uniapp.extapi.showToast as uni_showToast;
open class GenPagesHomeHome : BasePage {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onMounted(fun() {
            this.getLocation();
            this.startCompass();
            this.updateMarkers();
            this.getUsername();
            this.initDateFormatting();
            this.checkUnstartedOrder();
            this.checkUnreadNotifications();
            uni__on("refreshHomePage", fun(){
                this.checkUnstartedOrder();
            }
            );
        }
        , __ins);
        onReady(fun() {
            this.setHeight();
            this.setWidth();
            this.getAnchors();
        }
        , __ins);
        onPageShow(fun() {
            this.getUsername();
            this.checkUnstartedOrder();
            this.checkUnreadNotifications();
        }
        , __ins);
        onUnload(fun() {
            uni__off("refreshHomePage", null);
        }
        , __ins);
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this;
        val _cache = this.`$`.renderCache;
        val _component_van_icon = resolveComponent("van-icon");
        val _component_van_button = resolveComponent("van-button");
        val _component_van_cell = resolveComponent("van-cell");
        val _component_van_cell_group = resolveComponent("van-cell-group");
        val _component_van_floating_panel = resolveComponent("van-floating-panel");
        val _component_van_date_picker = resolveComponent("van-date-picker");
        val _component_van_time_picker = resolveComponent("van-time-picker");
        val _component_van_picker_group = resolveComponent("van-picker-group");
        val _component_van_popup = resolveComponent("van-popup");
        val _component_map = resolveComponent("map");
        return createElementVNode("view", null, utsArrayOf(
            createElementVNode("view", utsMapOf("class" to "menu-button", "onClick" to _ctx.toggleSidebar), utsArrayOf(
                createVNode(_component_van_icon, utsMapOf("name" to "bars", "size" to "24px", "color" to "#333"))
            ), 8, utsArrayOf(
                "onClick"
            )),
            createElementVNode("view", utsMapOf("class" to "notification-button", "onClick" to fun(){
                _ctx.navigateTo("notification");
            }
            ), utsArrayOf(
                createVNode(_component_van_icon, utsMapOf("name" to "bell", "size" to "24px", "color" to "#333")),
                if (isTrue(_ctx.hasUnreadNotifications)) {
                    createElementVNode("view", utsMapOf("key" to 0, "class" to "notification-dot"));
                } else {
                    createCommentVNode("v-if", true);
                }
            ), 8, utsArrayOf(
                "onClick"
            )),
            if (isTrue(_ctx.orderTipInfo.show)) {
                createElementVNode("view", utsMapOf("key" to 0, "class" to "order-tip"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "tip-content"), utsArrayOf(
                        createElementVNode("text", null, toDisplayString(_ctx.orderTipInfo.message), 1),
                        createVNode(_component_van_button, utsMapOf("type" to "primary", "size" to "small", "class" to "use-button", "onClick" to _ctx.orderTipInfo.action), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                            return utsArrayOf(
                                toDisplayString(_ctx.orderTipInfo.buttonText)
                            );
                        }), "_" to 1), 8, utsArrayOf(
                            "onClick"
                        ))
                    ))
                ));
            } else {
                createCommentVNode("v-if", true);
            }
            ,
            createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                "sidebar",
                utsMapOf("sidebar-open" to _ctx.showSidebar)
            ))), utsArrayOf(
                createElementVNode("view", utsMapOf("class" to "user-info"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "greeting"), utsArrayOf(
                        createElementVNode("text", null, "Welcome,"),
                        createElementVNode("text", null, toDisplayString(_ctx.username || "Guest") + "!", 1)
                    ))
                )),
                createElementVNode("view", utsMapOf("class" to "payment-section"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "payment-hint"), utsArrayOf(
                        createElementVNode("text", null, "Add Payment To Start"),
                        createVNode(_component_van_icon, utsMapOf("name" to "card", "size" to "20px"))
                    )),
                    createVNode(_component_van_button, utsMapOf("type" to "primary", "block" to "", "round" to "", "class" to "payment-button", "onClick" to fun(){
                        _ctx.navigateTo("card");
                    }
                    ), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            "Add Cards"
                        );
                    }
                    ), "_" to 1), 8, utsArrayOf(
                        "onClick"
                    ))
                )),
                createElementVNode("view", utsMapOf("class" to "sidebar-menu"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "menu-item", "onClick" to fun(){
                        _ctx.navigateTo("cards");
                    }
                    ), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "balance-o", "size" to "22px")),
                        createElementVNode("text", null, "Cards")
                    ), 8, utsArrayOf(
                        "onClick"
                    )),
                    createElementVNode("view", utsMapOf("class" to "menu-item", "onClick" to fun(){
                        _ctx.navigateTo("records");
                    }
                    ), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "clock-o", "size" to "22px")),
                        createElementVNode("text", null, "Records")
                    ), 8, utsArrayOf(
                        "onClick"
                    )),
                    createElementVNode("view", utsMapOf("class" to "menu-item", "onClick" to fun(){
                        _ctx.navigateTo("coupons");
                    }
                    ), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "coupon-o", "size" to "22px")),
                        createElementVNode("text", null, "Coupons")
                    ), 8, utsArrayOf(
                        "onClick"
                    )),
                    createElementVNode("view", utsMapOf("class" to "menu-item", "onClick" to fun(){
                        _ctx.navigateTo("help");
                    }
                    ), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "question-o", "size" to "22px")),
                        createElementVNode("text", null, "Help")
                    ), 8, utsArrayOf(
                        "onClick"
                    )),
                    createElementVNode("view", utsMapOf("class" to "menu-item", "onClick" to fun(){
                        _ctx.navigateTo("settings");
                    }
                    ), utsArrayOf(
                        createVNode(_component_van_icon, utsMapOf("name" to "setting-o", "size" to "22px")),
                        createElementVNode("text", null, "Settings")
                    ), 8, utsArrayOf(
                        "onClick"
                    ))
                ))
            ), 2),
            if (isTrue(_ctx.showSidebar)) {
                createElementVNode("view", utsMapOf("key" to 1, "class" to "sidebar-overlay", "onClick" to _ctx.toggleSidebar), null, 8, utsArrayOf(
                    "onClick"
                ));
            } else {
                createCommentVNode("v-if", true);
            }
            ,
            withDirectives(createVNode(_component_van_floating_panel, utsMapOf("height" to _ctx.height, "onUpdate:height" to fun(`$event`: Number){
                _ctx.height = `$event`;
            }
            , "anchors" to _ctx.anchors), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                return utsArrayOf(
                    if (_ctx.panelState === "timeSelector") {
                        createElementVNode("view", utsMapOf("key" to 0, "class" to "time-selector-panel"), utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to "instruction"), utsArrayOf(
                                createElementVNode("text", null, "Rent E-scooters")
                            )),
                            createElementVNode("view", utsMapOf("class" to "time-section"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "date-time-picker"), utsArrayOf(
                                    createElementVNode("view", utsMapOf("class" to "date-item", "onClick" to fun(){
                                        _ctx.showStartPicker = true;
                                    }), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "date-text"), toDisplayString(_ctx.formatDate(_ctx.startDate)), 1),
                                        createElementVNode("text", utsMapOf("class" to "time-text"), toDisplayString(_ctx.getDayOfWeek(_ctx.startDate)) + " " + toDisplayString(_ctx.startTime), 1)
                                    ), 8, utsArrayOf(
                                        "onClick"
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "days-display"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "days-text"), toDisplayString(_ctx.rentalDurationText), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "date-item", "onClick" to fun(){
                                        _ctx.showEndPicker = true;
                                    }), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "date-text"), toDisplayString(_ctx.formatDate(_ctx.endDate)), 1),
                                        createElementVNode("text", utsMapOf("class" to "time-text"), toDisplayString(_ctx.getDayOfWeek(_ctx.endDate)) + " " + toDisplayString(_ctx.endTime), 1)
                                    ), 8, utsArrayOf(
                                        "onClick"
                                    ))
                                )),
                                createElementVNode("view", utsMapOf("class" to "quick-options"), utsArrayOf(
                                    createElementVNode("view", utsMapOf("class" to "quick-option-title"), utsArrayOf(
                                        createElementVNode("text", null, "Quick Choices:")
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "quick-option-buttons"), utsArrayOf(
                                        createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                                            "quick-option",
                                            utsMapOf("active-option" to (_ctx.quickOptionSelected === "1h"))
                                        )), "onClick" to fun(){
                                            _ctx.setQuickOption("1h");
                                        }), utsArrayOf(
                                            createElementVNode("text", null, "1 Hour")
                                        ), 10, utsArrayOf(
                                            "onClick"
                                        )),
                                        createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                                            "quick-option",
                                            utsMapOf("active-option" to (_ctx.quickOptionSelected === "4h"))
                                        )), "onClick" to fun(){
                                            _ctx.setQuickOption("4h");
                                        }), utsArrayOf(
                                            createElementVNode("text", null, "4 Hours")
                                        ), 10, utsArrayOf(
                                            "onClick"
                                        ))
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "quick-option-buttons"), utsArrayOf(
                                        createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                                            "quick-option",
                                            utsMapOf("active-option" to (_ctx.quickOptionSelected === "1d"))
                                        )), "onClick" to fun(){
                                            _ctx.setQuickOption("1d");
                                        }), utsArrayOf(
                                            createElementVNode("text", null, "1 Day")
                                        ), 10, utsArrayOf(
                                            "onClick"
                                        )),
                                        createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                                            "quick-option",
                                            utsMapOf("active-option" to (_ctx.quickOptionSelected === "1w"))
                                        )), "onClick" to fun(){
                                            _ctx.setQuickOption("1w");
                                        }), utsArrayOf(
                                            createElementVNode("text", null, "7 Days")
                                        ), 10, utsArrayOf(
                                            "onClick"
                                        ))
                                    ))
                                )),
                                createElementVNode("view", utsMapOf("class" to "time-limit-tip"), utsArrayOf(
                                    createElementVNode("text", null, "Note: You can only reserve within the next 7 days")
                                ))
                            )),
                            createElementVNode("view", utsMapOf("class" to "next-button-area"), utsArrayOf(
                                createVNode(_component_van_button, utsMapOf("round" to "", "type" to "primary", "disabled" to !_ctx.isTimeValid, "onClick" to _ctx.showScooterList), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                    return utsArrayOf(
                                        " See E-scooter list "
                                    );
                                }), "_" to 1), 8, utsArrayOf(
                                    "disabled",
                                    "onClick"
                                ))
                            ))
                        ));
                    } else {
                        createCommentVNode("v-if", true);
                    }
                    ,
                    if (_ctx.panelState === "scooterList") {
                        createElementVNode("view", utsMapOf("key" to 1, "class" to "scooter-list-panel"), utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to "instruction"), utsArrayOf(
                                createElementVNode("text", null, "Rent E-scooters")
                            )),
                            createVNode(_component_van_cell_group, utsMapOf("class" to "list-container"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                return utsArrayOf(
                                    createElementVNode(Fragment, null, RenderHelpers.renderList(_ctx.scooters, fun(scooter, __key, __index, _cached): Any {
                                        return createVNode(_component_van_cell, utsMapOf("key" to scooter.scooterId, "class" to normalizeClass(utsMapOf("selected" to (_ctx.selected === scooter.scooterId))), "onClick" to fun(){
                                            _ctx.selectEscooter(scooter.scooterId);
                                        }), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                            return utsArrayOf(
                                                createElementVNode("view", utsMapOf("class" to "scooter-info"), utsArrayOf(
                                                    createElementVNode("image", utsMapOf("class" to "scooter-image", "src" to "/static/bikelogo/escooter_car2.png", "mode" to "aspectFit")),
                                                    createElementVNode("text", utsMapOf("class" to "scooter-id"), "SC" + toDisplayString(scooter.scooterId), 1),
                                                    createElementVNode("text", utsMapOf("class" to "battery"), toDisplayString(scooter.batteryLevel) + "%", 1),
                                                    createElementVNode("text", utsMapOf("class" to "price"), "￡" + toDisplayString(scooter.price) + "/hour", 1)
                                                ))
                                            );
                                        }), "_" to 2), 1032, utsArrayOf(
                                            "class",
                                            "onClick"
                                        ));
                                    }), 128)
                                );
                            }), "_" to 1)),
                            createElementVNode("view", utsMapOf("class" to "next-button-area"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "button-group"), utsArrayOf(
                                    createVNode(_component_van_button, utsMapOf("round" to "", "type" to "default", "onClick" to _ctx.backToTimeSelector, "class" to "back-button"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                        return utsArrayOf(
                                            createVNode(_component_van_icon, utsMapOf("name" to "arrow-left"))
                                        );
                                    }), "_" to 1), 8, utsArrayOf(
                                        "onClick"
                                    )),
                                    createVNode(_component_van_button, utsMapOf("round" to "", "type" to "success", "disabled" to (_ctx.selected == null), "onClick" to fun(){
                                        _ctx.navigateTo("confirm");
                                    }, "class" to "confirm-button"), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                                        return utsArrayOf(
                                            toDisplayString(if (_ctx.selected == null) {
                                                "Choose An E-scooter";
                                            } else {
                                                "Next";
                                            })
                                        );
                                    }), "_" to 1), 8, utsArrayOf(
                                        "disabled",
                                        "onClick"
                                    ))
                                ))
                            ))
                        ));
                    } else {
                        createCommentVNode("v-if", true);
                    }
                );
            }
            ), "_" to 1), 8, utsArrayOf(
                "height",
                "onUpdate:height",
                "anchors"
            )), utsArrayOf(
                utsArrayOf(
                    vShow,
                    !_ctx.orderTipInfo.show
                )
            )),
            createVNode(_component_van_popup, utsMapOf("show" to _ctx.showStartPicker, "onUpdate:show" to fun(`$event`: Boolean){
                _ctx.showStartPicker = `$event`;
            }
            , "position" to "bottom", "round" to ""), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                return utsArrayOf(
                    createVNode(_component_van_picker_group, utsMapOf("title" to "Start Time", "tabs" to utsArrayOf(
                        "Date",
                        "Time"
                    ), "onConfirm" to _ctx.onConfirmStartTime, "onCancel" to fun(){
                        _ctx.showStartPicker = false;
                    }
                    ), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            createVNode(_component_van_date_picker, utsMapOf("modelValue" to _ctx.tempStartDate, "onUpdate:modelValue" to fun(`$event`: UTSArray<String>){
                                _ctx.tempStartDate = `$event`;
                            }
                            , "min-date" to Date(), "max-date" to Date(Date().getTime() + 604800000)), null, 8, utsArrayOf(
                                "modelValue",
                                "onUpdate:modelValue",
                                "min-date",
                                "max-date"
                            )),
                            createVNode(_component_van_time_picker, utsMapOf("modelValue" to _ctx.tempStartTime, "onUpdate:modelValue" to fun(`$event`: UTSArray<String>){
                                _ctx.tempStartTime = `$event`;
                            }
                            , "min-hour" to _ctx.getMinHour(_ctx.tempStartDate)), null, 8, utsArrayOf(
                                "modelValue",
                                "onUpdate:modelValue",
                                "min-hour"
                            ))
                        );
                    }
                    ), "_" to 1), 8, utsArrayOf(
                        "onConfirm",
                        "onCancel"
                    ))
                );
            }
            ), "_" to 1), 8, utsArrayOf(
                "show",
                "onUpdate:show"
            )),
            createVNode(_component_van_popup, utsMapOf("show" to _ctx.showEndPicker, "onUpdate:show" to fun(`$event`: Boolean){
                _ctx.showEndPicker = `$event`;
            }
            , "position" to "bottom", "round" to ""), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                return utsArrayOf(
                    createVNode(_component_van_picker_group, utsMapOf("title" to "End Time", "tabs" to utsArrayOf(
                        "Date",
                        "Time"
                    ), "onConfirm" to _ctx.onConfirmEndTime, "onCancel" to fun(){
                        _ctx.showEndPicker = false;
                    }
                    ), utsMapOf("default" to withSlotCtx(fun(): UTSArray<Any> {
                        return utsArrayOf(
                            createVNode(_component_van_date_picker, utsMapOf("modelValue" to _ctx.tempEndDate, "onUpdate:modelValue" to fun(`$event`: UTSArray<String>){
                                _ctx.tempEndDate = `$event`;
                            }
                            , "min-date" to _ctx.startDate), null, 8, utsArrayOf(
                                "modelValue",
                                "onUpdate:modelValue",
                                "min-date"
                            )),
                            createVNode(_component_van_time_picker, utsMapOf("modelValue" to _ctx.tempEndTime, "onUpdate:modelValue" to fun(`$event`: UTSArray<String>){
                                _ctx.tempEndTime = `$event`;
                            }
                            , "min-hour" to _ctx.getMinHourForEndTime(_ctx.tempEndDate)), null, 8, utsArrayOf(
                                "modelValue",
                                "onUpdate:modelValue",
                                "min-hour"
                            ))
                        );
                    }
                    ), "_" to 1), 8, utsArrayOf(
                        "onConfirm",
                        "onCancel"
                    ))
                );
            }
            ), "_" to 1), 8, utsArrayOf(
                "show",
                "onUpdate:show"
            )),
            createElementVNode("cover-view", utsMapOf("class" to "map-overlay")),
            createVNode(_component_map, utsMapOf("id" to "myMap", "longitude" to _ctx.mapCenter.longitude, "latitude" to _ctx.mapCenter.latitude, "style" to normalizeStyle(utsMapOf("width" to (_ctx.windowWidth + "px"), "height" to (_ctx.windowHeight + "px"))), "show-location" to true, "markers" to _ctx.markers, "rotate" to _ctx.direction, "scale" to _ctx.scale), null, 8, utsArrayOf(
                "longitude",
                "latitude",
                "style",
                "markers",
                "rotate",
                "scale"
            ))
        ));
    }
    open var showSidebar: Boolean by `$data`;
    open var longitude: Number by `$data`;
    open var latitude: Number by `$data`;
    open var windowWidth: Number by `$data`;
    open var windowHeight: Number by `$data`;
    open var markers: UTSArray<{
        var id: String
        var latitude: Number
        var longitude: Number
        var width: Number
        var height: Number
        var iconPath: String
        var callout: {
            var content: String
            var display: String
        }
    }> by `$data`;
    open var direction: Number by `$data`;
    open var scale: Number by `$data`;
    open var height: Number by `$data`;
    open var anchors: UTSArray<Number> by `$data`;
    open var selected: Any? by `$data`;
    open var mapCenter: UTSJSONObject by `$data`;
    open var scooters: UTSArray<{
        var scooterId: Number
        var locationLat: Number
        var locationLng: Number
        var status: String
        var batteryLevel: Number
        var price: Number
    }> by `$data`;
    open var username: String by `$data`;
    open var panelState: String by `$data`;
    open var startDate: Date by `$data`;
    open var startTime: Any? by `$data`;
    open var endDate: Date by `$data`;
    open var endTime: Any? by `$data`;
    open var tempStartDate: UTSArray<String> by `$data`;
    open var tempStartTime: UTSArray<String> by `$data`;
    open var tempEndDate: UTSArray<String> by `$data`;
    open var tempEndTime: UTSArray<String> by `$data`;
    open var showStartPicker: Boolean by `$data`;
    open var showEndPicker: Boolean by `$data`;
    open var quickOptionSelected: String by `$data`;
    open var orderTipInfo: UTSJSONObject by `$data`;
    open var hasUnreadNotifications: Boolean by `$data`;
    open var rentalDurationText: String by `$data`;
    open var isTimeValid: Boolean by `$data`;
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("showSidebar" to false, "longitude" to 116.39742, "latitude" to 39.909, "windowWidth" to 0, "windowHeight" to 0, "markers" to utsArrayOf<{
            var id: String
            var latitude: Number
            var longitude: Number
            var width: Number
            var height: Number
            var iconPath: String
            var callout: {
                var content: String
                var display: String
            }
        }>(), "direction" to 0, "scale" to 16, "height" to 60, "anchors" to utsArrayOf(
            60,
            0
        ), "selected" to null, "mapCenter" to object : UTSJSONObject() {
            var longitude: Number = 116.39742
            var latitude: Number = 39.909
        }, "scooters" to utsArrayOf<{
            var scooterId: Number
            var locationLat: Number
            var locationLng: Number
            var status: String
            var batteryLevel: Number
            var price: Number
        }>(), "username" to "", "panelState" to "timeSelector", "startDate" to Date(), "startTime" to (Date().getHours().toString(10).padStart(2, "0") + ":" + Date().getMinutes().toString(10).padStart(2, "0")), "endDate" to Date(), "endTime" to (Date().getHours().toString(10).padStart(2, "0") + ":" + Date().getMinutes().toString(10).padStart(2, "0")), "tempStartDate" to utsArrayOf(
            "",
            "",
            ""
        ), "tempStartTime" to utsArrayOf(
            "",
            ""
        ), "tempEndDate" to utsArrayOf(
            "",
            "",
            ""
        ), "tempEndTime" to utsArrayOf(
            "",
            ""
        ), "showStartPicker" to false, "showEndPicker" to false, "quickOptionSelected" to "", "orderTipInfo" to object : UTSJSONObject() {
            var show = false
            var message = ""
            var buttonText = ""
            var action = fun(){}
        }, "hasUnreadNotifications" to false, "rentalDurationText" to computed<String>(fun(): String {
            val startDateTime = Date(this.startDate);
            val _this_startTime_split_map = this.startTime.split(":").map(Number);
            val startHour = _this_startTime_split_map[0];
            val startMinute = _this_startTime_split_map[1];
            startDateTime.setHours(startHour, startMinute);
            val endDateTime = Date(this.endDate);
            val _this_endTime_split_map = this.endTime.split(":").map(Number);
            val endHour = _this_endTime_split_map[0];
            val endMinute = _this_endTime_split_map[1];
            endDateTime.setHours(endHour, endMinute);
            val diffMs = endDateTime.getTime() - startDateTime.getTime();
            val diffMinutes = Math.ceil(diffMs / 60000);
            if (diffMinutes < 60) {
                return "" + diffMinutes + " Minutes";
            } else {
                val diffHours = Math.ceil(diffMinutes / 60);
                if (diffHours < 24) {
                    return if (diffHours === 1) {
                        "1 Hour";
                    } else {
                        "" + diffHours + " Hours";
                    };
                } else {
                    val diffDays = Math.floor(diffHours / 24);
                    return if (diffDays === 1) {
                        "1 Day";
                    } else {
                        "" + diffDays + " Days";
                    }
                    ;
                }
            }
        }
        ), "isTimeValid" to computed<Boolean>(fun(): Boolean {
            val startDateTime = Date(this.startDate);
            val _this_startTime_split_map = this.startTime.split(":").map(Number);
            val startHour = _this_startTime_split_map[0];
            val startMinute = _this_startTime_split_map[1];
            startDateTime.setHours(startHour, startMinute);
            val endDateTime = Date(this.endDate);
            val _this_endTime_split_map = this.endTime.split(":").map(Number);
            val endHour = _this_endTime_split_map[0];
            val endMinute = _this_endTime_split_map[1];
            endDateTime.setHours(endHour, endMinute);
            return startDateTime.getTime() !== endDateTime.getTime();
        }
        ));
    }
    override fun `$initMethods`() {
        this.getLocation = fun() {
            uni_getLocation(GetLocationOptions(type = "gcj02", success = fun(res){
                this.longitude = res.longitude;
                this.latitude = res.latitude;
                this.mapCenter.longitude = res.longitude;
                this.mapCenter.latitude = res.latitude;
            }
            , fail = fun(err){
                uni_showToast(ShowToastOptions(title = "Fail to locate.", icon = "none", duration = 2000));
            }
            ));
        }
        ;
        this.setHeight = fun() {
            this.windowHeight = uni_getSystemInfoSync().windowHeight;
        }
        ;
        this.setWidth = fun() {
            this.windowWidth = uni_getSystemInfoSync().windowWidth;
        }
        ;
        this.startCompass = fun() {
            uni_onCompassChange(fun(res){
                this.direction = res.direction;
            }
            );
        }
        ;
        this.getAnchors = fun() {
            this.anchors = utsArrayOf(
                60,
                Math.round(0.5 * this.windowHeight)
            );
        }
        ;
        this.updateMarkers = fun() {
            if (!this.scooters || this.scooters.length === 0) {
                this.markers = utsArrayOf();
                return;
            }
            this.markers = this.scooters.map(fun(scooter){
                return (let {
                    object : UTSJSONObject() {
                        var id = scooter.scooterId.toString(10)
                        var latitude = scooter.locationLat
                        var longitude = scooter.locationLng
                        var width = if (it.selected === scooter.scooterId) {
                            32;
                        } else {
                            25;
                        }
                        var height = if (it.selected === scooter.scooterId) {
                            32;
                        } else {
                            25;
                        }
                        var iconPath = "/static/order_details/maker.svg"
                        var callout = let {
                            object : UTSJSONObject() {
                                var content = "Scooter " + scooter.scooterId
                                var display = if (it.selected === scooter.scooterId) {
                                    "ALWAYS";
                                } else {
                                    "BYCLICK";
                                }
                            }
                        }
                    }
                });
            }
            );
        }
        ;
        this.selectEscooter = fun(id) {
            if (this.selected === id) {
                this.selected = null;
                this.mapCenter.longitude = this.longitude;
                this.mapCenter.latitude = this.latitude;
            } else {
                this.selected = id;
                val selectedScooter = this.scooters.find(fun(s): Boolean {
                    return s.scooterId === id;
                }
                );
                if (selectedScooter) {
                    this.mapCenter.longitude = selectedScooter.locationLng;
                    this.mapCenter.latitude = selectedScooter.locationLat;
                }
            }
            this.updateMarkers();
        }
        ;
        this.toggleSidebar = fun() {
            this.showSidebar = !this.showSidebar;
        }
        ;
        this.navigateTo = fun(page) {
            this.showSidebar = false;
            val pageMapping: UTSJSONObject = object : UTSJSONObject() {
                var confirm = "/pages/order/confirm"
                var notification = "/pages/notification/notification"
                var cards = "/pages/cards/cards"
                var records = "/pages/settings/orders/orders"
                var coupons = "/pages/settings/coupons/coupon"
                var help = "/pages/help/help"
                var settings = "/pages/settings/my_settings/my_settings"
                var card = "/pages/settings/card/card"
            };
            if (pageMapping[page]) {
                if (page === "confirm") {
                    if (this.selected == null) {
                        uni_showToast(ShowToastOptions(title = "Please choose a scooter", icon = "none"));
                        return;
                    }
                    val selectedScooter = this.scooters.find(fun(s): Boolean {
                        return s.scooterId === this.selected;
                    });
                    if (!selectedScooter) {
                        uni_showToast(ShowToastOptions(title = "Scooter information not found", icon = "none"));
                        return;
                    }
                    if (!userApi.isLoggedIn()) {
                        uni_showToast(ShowToastOptions(title = "Please log in first", icon = "none", duration = 2000));
                        setTimeout(fun(){
                            uni_navigateTo(NavigateToOptions(url = "/pages/login/login"));
                        }
                        , 1500);
                        return;
                    }
                    val userId = userApi.getUserId();
                    uni_navigateTo(NavigateToOptions(url = "" + pageMapping[page] + "?scooterId=" + this.selected + "&batteryLevel=" + selectedScooter.batteryLevel + "&price=" + selectedScooter.price + "&startDate=" + encodeURIComponent(this.formatFullDate(this.startDate)) + "&startTime=" + encodeURIComponent(this.startTime) + "&endDate=" + encodeURIComponent(this.formatFullDate(this.endDate)) + "&endTime=" + encodeURIComponent(this.endTime) + "&latitude=" + selectedScooter.locationLat + "&longitude=" + selectedScooter.locationLng));
                } else if (page === "cards") {
                    val isLoggedIn = userApi.isLoggedIn();
                    if (!isLoggedIn) {
                        uni_showToast(ShowToastOptions(title = "Please log in", icon = "none", duration = 2000));
                        return;
                    }
                    uni_navigateTo(NavigateToOptions(url = pageMapping[page]));
                } else {
                    uni_navigateTo(NavigateToOptions(url = pageMapping[page]));
                }
            }
        }
        ;
        this.getUsername = fun() {
            this.username = userApi.getUsername("Guest");
        }
        ;
        this.initDateFormatting = fun() {
            val now = Date();
            this.tempStartDate = utsArrayOf(
                now.getFullYear().toString(10),
                (now.getMonth() + 1).toString(10).padStart(2, "0"),
                now.getDate().toString(10).padStart(2, "0")
            );
            this.tempStartTime = utsArrayOf(
                now.getHours().toString(10).padStart(2, "0"),
                now.getMinutes().toString(10).padStart(2, "0")
            );
            this.tempEndDate = this.tempStartDate.slice();
            this.tempEndTime = this.tempStartTime.slice();
            this.startDate = now;
            this.endDate = Date(now);
            this.startTime = "" + this.tempStartTime[0] + ":" + this.tempStartTime[1];
            this.endTime = this.startTime;
        }
        ;
        this.formatDate = fun(date): String {
            val month = date.getMonth() + 1;
            val day = date.getDate();
            return "" + month + "/" + day;
        }
        ;
        this.getDayOfWeek = fun(date): String {
            val today = Date();
            if (date.getDate() === today.getDate() && date.getMonth() === today.getMonth() && date.getFullYear() === today.getFullYear()) {
                return "Today";
            }
            val weekdays = utsArrayOf(
                "Sun.",
                "Mon.",
                "Tues.",
                "Wed.",
                "Thur.",
                "Fri.",
                "Sat."
            );
            return weekdays[date.getDay()];
        }
        ;
        this.getMinHour = fun(date): Number {
            val today = Date();
            val selectedDate = Date(Number(date[0]), Number(date[1]) - 1, Number(date[2]));
            if (selectedDate.getDate() === today.getDate() && selectedDate.getMonth() === today.getMonth() && selectedDate.getFullYear() === today.getFullYear()) {
                return today.getHours();
            }
            return 0;
        }
        ;
        this.getMinHourForEndTime = fun(date): Number {
            val selectedDate = Date(Number(date[0]), Number(date[1]) - 1, Number(date[2]));
            if (selectedDate.getDate() === this.startDate.getDate() && selectedDate.getMonth() === this.startDate.getMonth() && selectedDate.getFullYear() === this.startDate.getFullYear()) {
                return Number(this.tempStartTime[0]);
            }
            return 0;
        }
        ;
        this.onConfirmStartTime = fun() {
            this.startDate = Date(Number(this.tempStartDate[0]), Number(this.tempStartDate[1]) - 1, Number(this.tempStartDate[2]));
            this.startTime = "" + this.tempStartTime[0] + ":" + this.tempStartTime[1];
            if (this.quickOptionSelected) {
                val startDateTime = Date(this.startDate);
                val _this_startTime_split_map = this.startTime.split(":").map(Number);
                val startHour = _this_startTime_split_map[0];
                val startMinute = _this_startTime_split_map[1];
                startDateTime.setHours(startHour, startMinute);
                when (this.quickOptionSelected) {
                    "1h" -> 
                        {
                            val oneHourLater = Date(startDateTime.getTime() + 3600000);
                            this.endDate = oneHourLater;
                            this.endTime = "" + oneHourLater.getHours().toString(10).padStart(2, "0") + ":" + oneHourLater.getMinutes().toString(10).padStart(2, "0");
                        }
                    "4h" -> 
                        {
                            val fourHoursLater = Date(startDateTime.getTime() + 14400000);
                            this.endDate = fourHoursLater;
                            this.endTime = "" + fourHoursLater.getHours().toString(10).padStart(2, "0") + ":" + fourHoursLater.getMinutes().toString(10).padStart(2, "0");
                        }
                    "1d" -> 
                        {
                            val oneDayLater = Date(startDateTime.getTime() + 86400000);
                            this.endDate = oneDayLater;
                            this.endTime = this.startTime;
                        }
                    "1w" -> 
                        {
                            val oneWeekLater = Date(startDateTime.getTime() + 604800000);
                            this.endDate = oneWeekLater;
                            this.endTime = this.startTime;
                        }
                }
            } else {
                this.endDate = Date(this.startDate);
                this.endTime = this.startTime;
            }
            this.tempEndTime = utsArrayOf(
                this.endTime.split(":")[0],
                this.endTime.split(":")[1]
            );
            this.tempEndDate = utsArrayOf(
                this.endDate.getFullYear().toString(10),
                (this.endDate.getMonth() + 1).toString(10).padStart(2, "0"),
                this.endDate.getDate().toString(10).padStart(2, "0")
            );
            this.showStartPicker = false;
        }
        ;
        this.onConfirmEndTime = fun() {
            val newEndDate = Date(Number(this.tempEndDate[0]), Number(this.tempEndDate[1]) - 1, Number(this.tempEndDate[2]));
            val isSameDay = (newEndDate.getDate() === this.startDate.getDate() && newEndDate.getMonth() === this.startDate.getMonth() && newEndDate.getFullYear() === this.startDate.getFullYear());
            if (isSameDay && Number(this.tempEndTime[0]) < Number(this.tempStartTime[0]) || (Number(this.tempEndTime[0]) === Number(this.tempStartTime[0]) && Number(this.tempEndTime[1]) < Number(this.tempStartTime[1]))) {
                this.tempEndTime = this.tempStartTime.slice();
                uni_showToast(ShowToastOptions(title = "The end time cannot be earlier than the start time", icon = "none"));
            }
            this.endDate = newEndDate;
            this.endTime = "" + this.tempEndTime[0] + ":" + this.tempEndTime[1];
            this.tempEndDate = utsArrayOf(
                this.endDate.getFullYear().toString(10),
                (this.endDate.getMonth() + 1).toString(10).padStart(2, "0"),
                this.endDate.getDate().toString(10).padStart(2, "0")
            );
            this.showEndPicker = false;
        }
        ;
        this.setQuickOption = fun(option) {
            if (this.quickOptionSelected === option) {
                this.quickOptionSelected = "";
                return;
            }
            this.quickOptionSelected = option;
            val startDateTime = Date(this.startDate);
            val _this_startTime_split_map = this.startTime.split(":").map(Number);
            val startHour = _this_startTime_split_map[0];
            val startMinute = _this_startTime_split_map[1];
            startDateTime.setHours(startHour, startMinute);
            when (option) {
                "1h" -> 
                    {
                        val oneHourLater = Date(startDateTime.getTime() + 3600000);
                        this.endDate = oneHourLater;
                        this.endTime = "" + oneHourLater.getHours().toString(10).padStart(2, "0") + ":" + oneHourLater.getMinutes().toString(10).padStart(2, "0");
                    }
                "4h" -> 
                    {
                        val fourHoursLater = Date(startDateTime.getTime() + 14400000);
                        this.endDate = fourHoursLater;
                        this.endTime = "" + fourHoursLater.getHours().toString(10).padStart(2, "0") + ":" + fourHoursLater.getMinutes().toString(10).padStart(2, "0");
                    }
                "1d" -> 
                    {
                        val oneDayLater = Date(startDateTime.getTime() + 86400000);
                        this.endDate = oneDayLater;
                        this.endTime = this.startTime;
                    }
                "1w" -> 
                    {
                        val oneWeekLater = Date(startDateTime.getTime() + 604800000);
                        this.endDate = oneWeekLater;
                        this.endTime = this.startTime;
                    }
            }
            this.tempEndDate = utsArrayOf(
                this.endDate.getFullYear().toString(10),
                (this.endDate.getMonth() + 1).toString(10).padStart(2, "0"),
                this.endDate.getDate().toString(10).padStart(2, "0")
            );
            this.tempEndTime = utsArrayOf(
                this.endTime.split(":")[0],
                this.endTime.split(":")[1]
            );
        }
        ;
        this.updateQuickOptionSelected = fun() {
            val startMs = this.startDate.getTime();
            val endMs = this.endDate.getTime();
            val diff = endMs - startMs;
            val oneHourMs: Number = 3600000;
            val fourHoursMs = 4 * oneHourMs;
            val oneDayMs = 24 * oneHourMs;
            val oneWeekMs = 7 * oneDayMs;
            val errorMargin: Number = 600000;
            if (Math.abs(diff - oneHourMs) <= errorMargin) {
                this.quickOptionSelected = "1h";
            } else if (Math.abs(diff - fourHoursMs) <= errorMargin) {
                this.quickOptionSelected = "4h";
            } else if (Math.abs(diff - oneDayMs) <= errorMargin) {
                this.quickOptionSelected = "1d";
            } else if (Math.abs(diff - oneWeekMs) <= errorMargin) {
                this.quickOptionSelected = "1w";
            } else {
                this.quickOptionSelected = "";
            }
        }
        ;
        this.showScooterList = fun() {
            this.panelState = "scooterList";
            this.getAnchors();
            val startDateTime = Date(this.startDate);
            val _this_startTime_split_map = this.startTime.split(":").map(Number);
            val startHour = _this_startTime_split_map[0];
            val startMinute = _this_startTime_split_map[1];
            startDateTime.setHours(startHour, startMinute);
            val endDateTime = Date(this.endDate);
            val _this_endTime_split_map = this.endTime.split(":").map(Number);
            val endHour = _this_endTime_split_map[0];
            val endMinute = _this_endTime_split_map[1];
            endDateTime.setHours(endHour, endMinute);
            val startISOString = dateUtils.formatDateForBackend(startDateTime);
            val endISOString = dateUtils.formatDateForBackend(endDateTime);
            console.log("Sending dates to backend:", startISOString, endISOString);
            uni_showLoading(ShowLoadingOptions(title = "Loading available scooters..."));
            scooterApi.getScooters().then(fun(res){
                if (res.code === 1) {
                    this.scooters = res.data.filter(fun(scooter){
                        return scooter.status === "free";
                    }).map(fun(scooter){
                        return (object : UTSJSONObject() {
                            var scooterId = scooter.scooter_id
                            var locationLat = scooter.location_lat
                            var locationLng = scooter.location_lng
                            var status = scooter.status
                            var batteryLevel = scooter.battery_level
                            var price = scooter.price
                        });
                    });
                    this.updateMarkers();
                    if (this.scooters.length === 0) {
                        uni_showToast(ShowToastOptions(title = "No available scooters now", icon = "none", duration = 2000));
                    }
                } else {
                    console.error("Failed to obtain scooter:", res.msg);
                    uni_showToast(ShowToastOptions(title = "Failed to get available scooters", icon = "none", duration = 2000));
                }
            }
            ).`catch`(fun(err){
                console.error("Abnormal acquisition of skateboard:", err);
                uni_showToast(ShowToastOptions(title = "Network request exception", icon = "none", duration = 2000));
            }
            ).`finally`(fun(){
                uni_hideLoading();
            }
            );
        }
        ;
        this.backToTimeSelector = fun() {
            this.panelState = "timeSelector";
            this.getAnchors();
        }
        ;
        this.formatFullDate = fun(date): String {
            val year = date.getFullYear();
            val month = (date.getMonth() + 1).toString().padStart(2, "0");
            val day = date.getDate().toString().padStart(2, "0");
            return "" + year + "-" + month + "-" + day;
        }
        ;
        this.checkUnstartedOrder = fun() {
            if (!userApi.isLoggedIn()) {
                console.log("The user has not logged in, skipping the check and not starting the order");
                this.orderTipInfo.show = false;
                return;
            }
            val userId = userApi.getUserId();
            orderApi.getUserOrders(userId).then(fun(res){
                if (res.code === 1 && res.data) {
                    res.data.forEach(fun(order, index){
                        val orderMap = order.toMap();
                    });
                    val pendingOrder = res.data.find(fun(order){
                        val orderMap = order.toMap();
                        val status = orderMap.get("status");
                        return status === "pending";
                    });
                    val activeOrder = res.data.find(fun(order){
                        val orderMap = order.toMap();
                        val status = orderMap.get("status");
                        return status === "active";
                    });
                    val paidOrder = res.data.find(fun(order){
                        val orderMap = order.toMap();
                        val status = orderMap.get("status");
                        return status === "paid";
                    });
                    if (pendingOrder) {
                        val orderMap = pendingOrder.toMap();
                        val orderId = orderMap.get("order_id");
                        this.orderTipInfo = object : UTSJSONObject() {
                            var show = true
                            var message = "You have an unpaid order."
                            var buttonText = "Go to pay"
                            var action = fun(){
                                orderApi.getOrderDetail(orderId).then(fun(res){
                                    if (res.code === 1 && res.data) {
                                        val orderDetail = res.data;
                                        val orderInfo: UTSJSONObject = object : UTSJSONObject() {
                                            var orderId = orderDetail.order_id
                                            var scooterCode = "SC" + orderDetail.scooter_id
                                            var startDate = dateUtils.formatDate(Date(orderDetail.start_time))
                                            var startTime = dateUtils.formatTime(Date(orderDetail.start_time))
                                            var endDate = dateUtils.formatDate(Date(orderDetail.end_time))
                                            var endTime = dateUtils.formatTime(Date(orderDetail.end_time))
                                            var duration = orderDetail.duration
                                            var cost = orderDetail.cost
                                            var address = orderDetail.address
                                        };
                                        uni_navigateTo(NavigateToOptions(url = "/pages/order/payment?orderInfo=" + encodeURIComponent(JSON.stringify(orderInfo))));
                                    } else {
                                        uni_showToast(ShowToastOptions(title = "Failed to get order details", icon = "none"));
                                    }
                                }).`catch`(fun(err){
                                    console.error("Failed to obtain order details:", err);
                                    uni_showToast(ShowToastOptions(title = "Network request exception", icon = "none"));
                                });
                            }
                        };
                    } else if (activeOrder) {
                        val orderMap = activeOrder.toMap();
                        val orderId = orderMap.get("order_id");
                        this.orderTipInfo = object : UTSJSONObject() {
                            var show = true
                            var message = "You have an active order."
                            var buttonText = "Check details"
                            var action = fun(){
                                uni_navigateTo(NavigateToOptions(url = "/pages/order_in_progress/order_in_progress?orderId=" + orderId));
                            }
                        };
                    } else if (paidOrder) {
                        val orderMap = paidOrder.toMap();
                        val orderId = orderMap.get("order_id");
                        this.orderTipInfo = object : UTSJSONObject() {
                            var show = true
                            var message = "You have already had an order."
                            var buttonText = "Go to use"
                            var action = fun(){
                                uni_navigateTo(NavigateToOptions(url = "/pages/order_not_used/order_not_used?orderId=" + orderId));
                            }
                        };
                    } else {
                        this.orderTipInfo.show = false;
                    }
                } else {
                    console.log("获取订单数据失败或数据为空");
                    this.orderTipInfo.show = false;
                }
            }
            ).`catch`(fun(err){
                console.error("Failed to obtain order:", err);
                this.orderTipInfo.show = false;
            }
            );
        }
        ;
        this.checkUnreadNotifications = fun(): UTSPromise<Unit> {
            return wrapUTSPromise(suspend w@{
                    val userId = userApi.getUserId();
                    if (!userId) {
                        this.hasUnreadNotifications = false;
                        return@w;
                    }
                    try {
                        val res = await(notificationApi.countUserUnreadNotifications(userId));
                        if (res.code === 1) {
                            this.hasUnreadNotifications = res.data > 0;
                        } else {
                            this.hasUnreadNotifications = false;
                        }
                    }
                     catch (err: Throwable) {
                        console.error("Failed to retrieve the number of unread notifications:", err);
                        this.hasUnreadNotifications = false;
                    }
            });
        }
        ;
    }
    open lateinit var getLocation: () -> Unit;
    open lateinit var setHeight: () -> Unit;
    open lateinit var setWidth: () -> Unit;
    open lateinit var startCompass: () -> Unit;
    open lateinit var getAnchors: () -> Unit;
    open lateinit var updateMarkers: () -> Unit;
    open lateinit var selectEscooter: (id) -> Unit;
    open lateinit var toggleSidebar: () -> Unit;
    open lateinit var navigateTo: (page) -> Unit;
    open lateinit var getUsername: () -> Unit;
    open lateinit var initDateFormatting: () -> Unit;
    open lateinit var formatDate: (date) -> String;
    open lateinit var getDayOfWeek: (date) -> String;
    open lateinit var getMinHour: (date) -> Number;
    open lateinit var getMinHourForEndTime: (date) -> Number;
    open lateinit var onConfirmStartTime: () -> Unit;
    open lateinit var onConfirmEndTime: () -> Unit;
    open lateinit var setQuickOption: (option) -> Unit;
    open lateinit var updateQuickOptionSelected: () -> Unit;
    open lateinit var showScooterList: () -> Unit;
    open lateinit var backToTimeSelector: () -> Unit;
    open lateinit var formatFullDate: (date) -> String;
    open lateinit var checkUnstartedOrder: () -> Unit;
    open lateinit var checkUnreadNotifications: () -> UTSPromise<Unit>;
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(
                styles0
            ), utsArrayOf(
                GenApp.styles
            ))
        };
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return utsMapOf("map-overlay" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to 0, "left" to 0, "width" to "100%", "height" to "100%", "background" to "linear-gradient(\r\n\t    to bottom,\r\n\t    rgba(0, 123, 255, 0.2) 0%,   \r\n\t    rgba(0, 123, 255, 0.1) 50%,  \r\n\t    rgba(255, 255, 255, 0) 100%  \r\n\t  )", "pointerEvents" to "none", "zIndex" to 2)), "instruction" to padStyleMapOf(utsMapOf("textAlign" to "center", "zIndex" to 1, "paddingTop" to 5, "paddingRight" to 5, "paddingBottom" to 5, "paddingLeft" to 5, "position" to "fixed", "top" to 15, "width" to "100%", "backgroundColor" to "#ffffff", "fontSize" to 26)), "list-container" to padStyleMapOf(utsMapOf("marginTop" to 35, "marginBottom" to 65)), "next-button-area" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "position" to "fixed", "width" to "100%", "paddingTop" to 5, "paddingRight" to 5, "paddingBottom" to 5, "paddingLeft" to 5, "bottom" to 0)), "van-button" to utsMapOf(".next-button-area " to utsMapOf("fontSize" to 20, "height" to 55, "lineHeight" to "48px", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "backgroundColor" to "rgba(0,0,0,0)", "borderRadius" to 25, "color" to "#FFFFFF", "fontWeight" to "bold", "boxShadow" to "0 4px 10px rgba(0, 122, 255, 0.2)", "marginBottom" to 20), ".button-group " to utsMapOf("flex" to 1)), "van-button--disabled" to utsMapOf(".next-button-area " to utsMapOf("!backgroundColor" to "#007aff", "opacity" to 0.5)), "van-button--success" to utsMapOf(".next-button-area " to utsMapOf("!backgroundColor" to "#007aff", "!borderColor" to "#007aff")), "scooter-info" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "alignItems" to "center", "gap" to "15px", "height" to 60)), "scooter-image" to padStyleMapOf(utsMapOf("width" to 40, "height" to 40, "marginRight" to 10)), "scooter-id" to padStyleMapOf(utsMapOf("fontSize" to 22, "fontWeight" to "bold", "flex" to 1, "height" to "100%", "display" to "flex", "alignItems" to "center", "color" to "#666666")), "battery" to padStyleMapOf(utsMapOf("color" to "#666666", "fontSize" to 20, "flex" to 1, "textAlign" to "center", "height" to "100%", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "price" to padStyleMapOf(utsMapOf("fontSize" to 20, "color" to "#666666", "flex" to 1, "textAlign" to "right", "height" to "100%", "display" to "flex", "alignItems" to "center", "justifyContent" to "flex-end")), "selected" to padStyleMapOf(utsMapOf("!borderWidth" to 2, "!borderStyle" to "solid", "!borderColor" to "#007aff", "borderRadius" to 10, "boxShadow" to "0 3px 8px rgba(0, 122, 255, 0.2)")), "van-cell" to padStyleMapOf(utsMapOf("borderWidth" to 1, "borderStyle" to "solid", "borderColor" to "#eeeeee", "borderRadius" to 8, "width" to "95%", "marginTop" to 5, "marginRight" to "auto", "marginBottom" to 5, "marginLeft" to "auto")), "time-section" to padStyleMapOf(utsMapOf("marginTop" to 40, "marginBottom" to 60, "paddingTop" to 15, "paddingRight" to 15, "paddingBottom" to 15, "paddingLeft" to 15)), "date-time-picker" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between", "alignItems" to "center", "marginTop" to "20rpx", "paddingTop" to 0, "paddingRight" to 15, "paddingBottom" to 0, "paddingLeft" to 15, "width" to "100%", "gap" to "20rpx")), "date-item" to padStyleMapOf(utsMapOf("backgroundColor" to "#f4f8ff", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderRadius" to "20rpx", "flex" to 1, "minWidth" to 0, "textAlign" to "center", "boxShadow" to "0 4px 10px rgba(0, 122, 255, 0.08)", "backgroundImage" to "none", "borderWidth" to "2rpx", "borderStyle" to "solid", "borderColor" to "#cce6ff")), "date-text" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "color" to "#007aff", "textAlign" to "center", "marginBottom" to 5, "whiteSpace" to "nowrap", "overflow" to "hidden", "textOverflow" to "ellipsis", "fontWeight" to "bold")), "time-text" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#555555", "textAlign" to "center", "whiteSpace" to "nowrap", "overflow" to "hidden", "textOverflow" to "ellipsis", "marginTop" to "10rpx")), "days-display" to padStyleMapOf(utsMapOf("paddingTop" to 0, "paddingRight" to 10, "paddingBottom" to 0, "paddingLeft" to 10, "minWidth" to 80, "textAlign" to "center", "width" to "140rpx", "height" to "100rpx", "borderRadius" to "20rpx", "fontWeight" to "bold", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "boxShadow" to "0 4px 12px rgba(0, 122, 255, 0.25)")), "days-text" to padStyleMapOf(utsMapOf("fontSize" to 14, "color" to "#333333", "whiteSpace" to "nowrap")), "quick-options" to padStyleMapOf(utsMapOf("marginTop" to 30)), "quick-option-title" to padStyleMapOf(utsMapOf("fontSize" to 22, "color" to "#333333", "marginBottom" to 10, "fontWeight" to "bold")), "quick-option-buttons" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "space-between")), "quick-option" to padStyleMapOf(utsMapOf("backgroundColor" to "rgba(255,255,255,0.5)", "paddingTop" to 12, "paddingRight" to 0, "paddingBottom" to 12, "paddingLeft" to 0, "flex" to 1, "marginTop" to 0, "marginRight" to 5, "marginBottom" to 0, "marginLeft" to 5, "boxShadow" to "0 2px 5px rgba(0,0,0,0.05)", "display" to "flex", "justifyContent" to "center", "alignItems" to "center", "fontSize" to 18, "borderWidth" to 1, "borderStyle" to "solid", "borderColor" to "rgba(0,0,0,0)", "backgroundImage" to "none", "borderRadius" to 10, "transitionDuration" to "0.3s")), "active-option" to padStyleMapOf(utsMapOf("backgroundColor" to "rgba(0,0,0,0)", "borderWidth" to "medium", "borderStyle" to "none", "borderColor" to "#000000", "color" to "#FFFFFF", "fontWeight" to "bold", "backgroundImage" to "linear-gradient(to right, #82b1ff, #007aff)", "boxShadow" to "0 3px 8px rgba(0, 122, 255, 0.3)")), "menu-button" to padStyleMapOf(utsMapOf("position" to "fixed", "top" to 30, "left" to 30, "zIndex" to 100, "backgroundColor" to "rgba(255,255,255,0.8)", "width" to 50, "height" to 50, "display" to "flex", "justifyContent" to "center", "alignItems" to "center", "boxShadow" to "0 2px 4px rgba(0, 0, 0, 0.2)")), "van-icon" to utsMapOf(".menu-button " to utsMapOf("fontSize" to 28), ".notification-button " to utsMapOf("fontSize" to 28)), "sidebar" to padStyleMapOf(utsMapOf("position" to "fixed", "paddingTop" to 20, "left" to "-90%", "width" to "80%", "height" to "100%", "backgroundColor" to "#FFFFFF", "zIndex" to 1000, "transitionProperty" to "left", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "display" to "flex", "flexDirection" to "column")), "sidebar-open" to padStyleMapOf(utsMapOf("left" to 0)), "sidebar-overlay" to padStyleMapOf(utsMapOf("position" to "fixed", "top" to 0, "left" to 0, "width" to "100%", "height" to "100%", "backgroundColor" to "rgba(0,0,0,0.5)", "zIndex" to 999)), "sidebar-header" to padStyleMapOf(utsMapOf("paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f5f5f5")), "sidebar-title" to padStyleMapOf(utsMapOf("fontSize" to 24, "fontWeight" to "bold")), "user-info" to padStyleMapOf(utsMapOf("paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f5f5f5")), "greeting" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "fontSize" to 26, "fontWeight" to "bold", "color" to "#333333", "lineHeight" to 1.5, "marginBottom" to 15)), "payment-section" to padStyleMapOf(utsMapOf("paddingTop" to 15, "paddingRight" to 20, "paddingBottom" to 15, "paddingLeft" to 20, "backgroundColor" to "#f0f9ff", "borderBottomWidth" to 1, "borderBottomStyle" to "solid", "borderBottomColor" to "#f5f5f5", "height" to 150, "display" to "flex", "justifyContent" to "center")), "payment-hint" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "center", "gap" to "20px", "alignItems" to "center", "marginBottom" to 15, "fontSize" to 22)), "payment-button" to padStyleMapOf(utsMapOf("width" to "100%", "fontSize" to 20)), "sidebar-menu" to padStyleMapOf(utsMapOf("flex" to 1, "paddingTop" to 10, "paddingRight" to 0, "paddingBottom" to 10, "paddingLeft" to 0, "overflowY" to "auto")), "menu-item" to padStyleMapOf(utsMapOf("paddingTop" to 20, "paddingRight" to 20, "paddingBottom" to 20, "paddingLeft" to 20, "display" to "flex", "flexDirection" to "row", "alignItems" to "center", "gap" to "15px", "cursor" to "pointer", "transitionProperty" to "backgroundColor", "transitionDuration" to "0.2s", "fontSize" to 22, "backgroundColor:hover" to "#f5f5f5", "backgroundColor:active" to "#e5e5e5")), "version-info" to padStyleMapOf(utsMapOf("fontSize" to 18, "color" to "#999999")), "notification-button" to padStyleMapOf(utsMapOf("position" to "fixed", "top" to 30, "right" to 30, "zIndex" to 100, "backgroundColor" to "rgba(255,255,255,0.8)", "width" to 50, "height" to 50, "display" to "flex", "justifyContent" to "center", "alignItems" to "center", "boxShadow" to "0 2px 4px rgba(0, 0, 0, 0.2)")), "notification-dot" to padStyleMapOf(utsMapOf("position" to "absolute", "top" to 4, "right" to 4, "width" to 15, "height" to 15, "backgroundColor" to "#ff4d4f", "borderWidth" to 2, "borderStyle" to "solid", "borderColor" to "#ffffff", "boxShadow" to "0 1px 2px rgba(0, 0, 0, 0.2)")), "button-group" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "gap" to "10px", "paddingTop" to 0, "paddingRight" to 10, "paddingBottom" to 0, "paddingLeft" to 10)), "back-button" to utsMapOf(".button-group " to utsMapOf("flex" to 0.2, "!minWidth" to 50)), "confirm-button" to utsMapOf(".button-group " to utsMapOf("flex" to 0.8)), "order-tip" to padStyleMapOf(utsMapOf("position" to "fixed", "bottom" to 100, "left" to "50%", "transform" to "translateX(-50%)", "zIndex" to 100, "backgroundColor" to "rgba(255,255,255,0.95)", "borderRadius" to 8, "boxShadow" to "0 2px 8px rgba(0, 0, 0, 0.15)", "paddingTop" to 10, "paddingRight" to 15, "paddingBottom" to 10, "paddingLeft" to 15, "width" to "90%", "maxWidth" to 400, "height" to 100, "display" to "flex", "justifyContent" to "center")), "tip-content" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "justifyContent" to "center", "alignItems" to "center", "gap" to "15px")), "use-button" to padStyleMapOf(utsMapOf("marginLeft" to 15)), "time-limit-tip" to padStyleMapOf(utsMapOf("marginTop" to 20, "alignItems" to "center", "textAlign" to "center", "fontSize" to 14, "color" to "#666666")), "@TRANSITION" to utsMapOf("sidebar" to utsMapOf("property" to "left", "duration" to "0.3s", "timingFunction" to "ease"), "menu-item" to utsMapOf("property" to "backgroundColor", "duration" to "0.2s"), "quick-option" to utsMapOf("duration" to "0.3s")));
            }
        var inheritAttrs = true;
        var inject: Map<String, Map<String, Any?>> = utsMapOf();
        var emits: Map<String, Any?> = utsMapOf();
        var props = normalizePropsOptions(utsMapOf());
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf();
        var components: Map<String, CreateVueComponent> = utsMapOf();
    }
}
