package com.kumaapp.zhongxiang.model;

import com.hemaapp.hm_FrameWork.orm.annotation.PrimaryKey;
import com.hemaapp.hm_FrameWork.orm.annotation.TableName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

@TableName(table = "SysInitInfo")
public class SysInitInfo extends XtomObject {
    @PrimaryKey(column = "sys_web_service")
    private String sys_web_service;// 后台服务根路径(含版本号)
    // 形如： http://192.168.0.146:8008/group1/wb_qk/index.php/Webservice/V100/
    // 说明：V100是服务器转换处理后的版本号，V100 对应客户端版本是1.0.0
    private String sys_plugins;// 第三方插件根路径 形如：
    // http://58.56.89.218:8008/group1/hm_PHP/plugins/
    private String sys_show_iospay; // 是否显示在线支付功能,专门应对苹果审核,安卓无需处理此标记
    // 苹果商店审核前置为0，审核通过需置为1,客户端根据此标记来决定是否进入在线支付模块,(0：弹窗提醒"暂未开放"1：进入支付模块)
    private String start_img; //启动页广告图片
    private String android_must_update;// 强制客户端升级标记 0：不强制
    // 1：强制（当软件架构进行了较大变动，客户端必须强制用户升级到最新版本）
    private String android_last_version;// 当前系统最新版本号 将该信息与本机版本号比对，如果不相等，则提醒在线升级
    private String iphone_must_update;// 强制客户端升级标记 0：不强制
    // 1：强制（当软件架构进行了较大变动，客户端必须强制用户升级到最新版本）
    private String iphone_last_version;// 当前系统最新版本号 将该信息与本机版本号比对，如果不相等，则提醒在线升级
    private String sys_chat_ip;// 聊天服务器IP地址 形如：192.168.0.146
    private String sys_chat_port;// 聊天服务器端口号 形如：5222（一个整数）
    private int sys_pagesize;// 系统规定单页记录数 此参数在系统列表分页时需要用到，默认：20
    private String sys_service_phone;// 我公司统一客服电话 前台客服解疑释惑专用，目前是"0531-67804172"
    private String android_update_url;// 安卓软件更新地址
    private String iphone_update_url;// 苹果软件更新地址
    private String driver_android_update_url;
    private String driver_iphone_update_url;
    private String iphone_comment_url;// 苹果软件评论地址 同上
    private String msg_invite;// 邀请下载短信内容
    private String sharetitle;
    private String sharecontent;
    public SysInitInfo() {
    }
    public SysInitInfo(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                sharetitle = get(jsonObject, "sharetitle");
                sharecontent = get(jsonObject, "sharecontent");
                sys_web_service = get(jsonObject, "sys_web_service");
                sys_plugins = get(jsonObject, "sys_plugins");
                sys_show_iospay = get(jsonObject, "sys_show_iospay");
                android_must_update = get(jsonObject, "android_must_update");
                android_last_version = get(jsonObject, "android_last_version");
                iphone_must_update = get(jsonObject, "iphone_must_update");
                iphone_last_version = get(jsonObject, "iphone_last_version");
                sys_chat_ip = get(jsonObject, "sys_chat_ip");
                sys_chat_port = get(jsonObject, "sys_chat_port");
                if (!jsonObject.isNull("sys_pagesize"))
                    sys_pagesize = jsonObject.getInt("sys_pagesize");
                sys_service_phone = get(jsonObject, "sys_service_phone");
                android_update_url = get(jsonObject, "android_update_url");
                iphone_update_url = get(jsonObject, "iphone_update_url");
                iphone_comment_url = get(jsonObject, "iphone_comment_url");
                msg_invite = get(jsonObject, "msg_invite");

                start_img = get(jsonObject, "start_img");

                driver_android_update_url = get(jsonObject, "driver_android_update_url");
                driver_iphone_update_url = get(jsonObject, "driver_iphone_update_url");

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }


    @Override
    public String toString() {
        return "SysInitInfo{" +
                "sys_web_service='" + sys_web_service + '\'' +
                ", sys_plugins='" + sys_plugins + '\'' +
                ", sys_show_iospay='" + sys_show_iospay + '\'' +
                ", start_img='" + start_img + '\'' +
                ", android_must_update='" + android_must_update + '\'' +
                ", android_last_version='" + android_last_version + '\'' +
                ", iphone_must_update='" + iphone_must_update + '\'' +
                ", iphone_last_version='" + iphone_last_version + '\'' +
                ", sys_chat_ip='" + sys_chat_ip + '\'' +
                ", sys_chat_port='" + sys_chat_port + '\'' +
                ", sys_pagesize=" + sys_pagesize +
                ", sys_service_phone='" + sys_service_phone + '\'' +
                ", android_update_url='" + android_update_url + '\'' +
                ", iphone_update_url='" + iphone_update_url + '\'' +
                ", driver_android_update_url='" + driver_android_update_url + '\'' +
                ", driver_iphone_update_url='" + driver_iphone_update_url + '\'' +
                ", iphone_comment_url='" + iphone_comment_url + '\'' +
                ", msg_invite='" + msg_invite + '\'' +
                ", sharetitle='" + sharetitle + '\'' +
                ", sharecontent='" + sharecontent + '\'' +
                '}';
    }

    public String getSys_web_service() {
        return sys_web_service;
    }

    public String getSys_plugins() {
        return sys_plugins;
    }

    public String getSys_show_iospay() {
        return sys_show_iospay;
    }

    public String getAndroid_must_update() {
        return android_must_update;
    }

    public String getAndroid_last_version() {
        return android_last_version;
    }

    public String getIphone_must_update() {
        return iphone_must_update;
    }

    public String getIphone_last_version() {
        return iphone_last_version;
    }

    public String getSys_chat_ip() {
        return sys_chat_ip;
    }

    public String getSys_chat_port() {
        return sys_chat_port;
    }

    public int getSys_pagesize() {
        return sys_pagesize;
    }

    public String getSys_service_phone() {
        return sys_service_phone;
    }

    public String getAndroid_update_url() {
        return android_update_url;
    }

    public String getIphone_update_url() {
        return iphone_update_url;
    }

    public String getIphone_comment_url() {
        return iphone_comment_url;
    }

    public String getMsg_invite() {
        return msg_invite;
    }

    public String getStart_img() {
        return start_img;
    }


    public String getDriver_android_update_url() {
        return driver_android_update_url;
    }


    public String getSharetitle() {
        return sharetitle;
    }

    public String getSharecontent() {
        return sharecontent;
    }


    public String getDriver_iphone_update_url() {
        return driver_iphone_update_url;
    }
}
