package com.kumaapp.zhongxiang.model;

import com.hemaapp.hm_FrameWork.HemaUser;
import com.hemaapp.hm_FrameWork.orm.annotation.PrimaryKey;
import com.hemaapp.hm_FrameWork.orm.annotation.TableName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xtom.frame.exception.DataParseException;

/**
 * 用户登录信息
 */
@TableName(table = "User")
public class User extends HemaUser {
    @PrimaryKey(column = "id")
    private String id; //用户主键
    private String username; //登录名
    private String email; //用户邮箱
    private String realname; //用户昵称
    private String mobile; //用户手机
    private String password; //登陆密码
    private String paypassword; //支付密码
    private String sex; //性别
    private String avatar; //个人主页头像图片（小）
    private String avatarbig; //个人主页头像图片（大）
    private String lng; //经度
    private String lat;  //维度
    private String feeaccount; //账户余额
    private String android_must_update; //安卓强制更新标记 0：不强制 1：强制
    private String android_last_version; //安卓最新版本号
    private String android_update_url; //安卓软件更新地址
    public User() {
    }

    public User(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        try {
            id = get(jsonObject, "id");
            username = get(jsonObject, "username");
            email = get(jsonObject, "email");
            realname = get(jsonObject, "realname");
            mobile = get(jsonObject, "mobile");
            password = get(jsonObject, "password");
            paypassword = get(jsonObject, "paypassword");
            sex = get(jsonObject, "sex");
            avatar = get(jsonObject, "avatar");
            avatarbig = get(jsonObject, "avatarbig");
            lng = get(jsonObject, "lng");
            lat = get(jsonObject, "lat");
            feeaccount = get(jsonObject, "feeaccount");
            android_must_update = get(jsonObject, "android_must_update");
            android_last_version = get(jsonObject, "android_last_version");
            android_update_url = get(jsonObject, "android_update_url");

            log_i(toString());
        } catch (JSONException e) {
            throw new DataParseException(e);
        }
    }


    @Override
    public String toString() {
        return "User{" +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAndroid_must_update() {
        return android_must_update;
    }

    public String getAndroid_last_version() {
        return android_last_version;
    }

    public String getAndroid_update_url() {
        return android_update_url;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarbig() {
        return avatarbig;
    }

    public String getUsername() {
        return username;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public String getEmail() {
        return email;
    }

    public String getFeeaccount() {
        return feeaccount;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public String getRealname() {
        return realname;
    }

    public String getSex() {
        return sex;
    }


}
