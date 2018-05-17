package com.kumaapp.zhongxiang;

/**
 * EventBus的配置类型类
 * Created by CoderHu on 2017-03-10.
 */

public enum EventBusConfig {

    LOGIN_SUCCESS(0, "登录成功"),
    LOGOUT_SUCCESS(1, "退出成功"),
    CLIENT_ID(2, "个推注册成功"),
    NEW_MESSAGE(3, "收到新消息"),
    REFRESH_CUSTOMER_INFO(4, "刷新客户信息"),
    REFRESH_MAIN_DATA(5, "刷新首页数据"),
    NOTICE_SAVE(6, "通知操作"),
    REFRESH_USER(11, "刷新我的详情"),
    PAY_WECHAT(14, "微信支付"),
    PUSH_TYPE(19, "推送类型");

    private int id;
    private String description;

    EventBusConfig(int id, String description) {
        this.id = id;
        this.description = description;
    }
}
