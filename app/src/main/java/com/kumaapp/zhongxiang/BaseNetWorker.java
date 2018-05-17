package com.kumaapp.zhongxiang;

import android.content.Context;

import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.task.CurrentTask;
import com.hemaapp.hm_FrameWork.task.ExecuteNetTask;
import com.kumaapp.zhongxiang.model.AlipayTrade;
import com.kumaapp.zhongxiang.model.FileUploadResult;
import com.kumaapp.zhongxiang.model.SysInitInfo;
import com.kumaapp.zhongxiang.model.Token;
import com.kumaapp.zhongxiang.model.User;
import com.kumaapp.zhongxiang.model.WeiXinPay;

import java.util.HashMap;

import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomDeviceUuidFactory;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 网络请求工具类
 */
public class BaseNetWorker extends HemaNetWorker {
    /**
     * 实例化网络请求工具类
     *
     * @param mContext
     */
    private Context mContext;

    public BaseNetWorker(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void clientLogin() {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<>();
        String username = XtomSharedPreferencesUtil.get(mContext, "username");
        params.put("username", username);// 用户登录名 手机号或邮箱
        String password = XtomSharedPreferencesUtil.get(mContext, "password");
        String login_type = XtomSharedPreferencesUtil.get(mContext, "login_type");
        if (isNull(login_type))
            login_type = "1";
        if (login_type.equals("1"))
            params.put("password", Md5Util.getMd5(XtomConfig.DATAKEY
                    + Md5Util.getMd5(password))); // 登陆密码 服务器端存储的是32位的MD5加密串
        else
            params.put("password", password);
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号
        // 记录用户的登录版本，方便服务器运维统计

        ExecuteNetTask<User> task = new ExecuteNetTask<>(information, params, User.class);
        executeTask(task);
    }

    /**
     * 第三方自动登录
     */
    @Override
    public boolean thirdSave() {
        return false;
    }

    /**
     * 系统初始化
     */
    public void init() {
        BaseHttpInformation information = BaseHttpInformation.INIT;
        HashMap<String, String> params = new HashMap<>();
        params.put("devicetype", "2");
        params.put("lastloginversion", HemaUtil.getAppVersionForSever(mContext));// 版本号码(默认：1.0.0)
        params.put("device_sn", XtomDeviceUuidFactory.get(mContext));// 客户端硬件串号

        ExecuteNetTask<SysInitInfo> task = new ExecuteNetTask<>(information, params, SysInitInfo.class);
        executeTask(task);
    }

    /**
     * 验证用户名是否合法
     */
    public void clientVerify(String username, String clienttype) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_VERIFY;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);// 用户登录名 手机号或邮箱
        params.put("clienttype", clienttype);

        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 申请随机验证码
     */
    public void codeGet(String username, String init_code) {
        BaseHttpInformation information = BaseHttpInformation.CODE_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);// 用户登录名 手机号或邮箱
        params.put("init_code", init_code);
        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 验证随机码
     */
    public void codeVerify(String username, String code) {
        BaseHttpInformation information = BaseHttpInformation.CODE_VERIFY;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);// 用户登录名 手机号或邮箱
        params.put("code", code);// 6位随机号码 测试阶段固定向服务器提交“123456”

        ExecuteNetTask<Token> task = new ExecuteNetTask<>(information, params, Token.class);
        executeTask(task);
    }

    /**
     * 登录
     *
     * @param username, password
     */
    public void clientLogin(String username, String password) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);// 用户登录名 手机号或邮箱
        String login_type = XtomSharedPreferencesUtil.get(mContext, "login_type");
        if (isNull(login_type))
            login_type = "1";
        if (login_type.equals("1"))
            params.put("password", Md5Util.getMd5(XtomConfig.DATAKEY
                    + Md5Util.getMd5(password))); // 登陆密码 服务器端存储的是32位的MD5加密串
        else
            params.put("password", password); // 登陆密码 服务器端存储的是32位的MD5加密串
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号
        // 记录用户的登录版本，方便服务器运维统计
        ExecuteNetTask<User> task = new ExecuteNetTask<>(information, params, User.class);
        executeTask(task);
    }

    /**
     * 上传文件（图片，音频，视频）
     */
    public void fileUpload(String token, String keytype, String keyid,
                           String duration, String orderby, String content, String temp_file) {
        BaseHttpInformation information = BaseHttpInformation.FILE_UPLOAD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("keytype", keytype); //
        params.put("keyid", keyid); //
        params.put("duration", duration); //
        params.put("orderby", orderby); //
        params.put("content", content);// 内容描述 有的项目中，展示性图片需要附属一段文字说明信息。默认传"无"
        HashMap<String, String> files = new HashMap<>();
        files.put("temp_file", temp_file); //

        ExecuteNetTask<FileUploadResult> task = new ExecuteNetTask<>(information, params, files, FileUploadResult.class);
        executeTask(task);
    }

    /**
     * 硬件注册保存接口
     */
    public void deviceSave(String token, String deviceid, String type,
                           String channelid) {
        BaseHttpInformation information = BaseHttpInformation.DEVICE_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("deviceid", deviceid);// 登陆手机硬件码 对应百度推送userid
        params.put("devicetype", type);// 登陆手机类型 1:苹果 2:安卓
        params.put("channelid", channelid);// 百度推送渠道id 方便直接从百度后台进行推送测试
        params.put("clienttype", "1");

        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 用户注册
     */
//    public void clientAdd(String tempToken, String username, String password, String nickname, String sex,
//                          String district_name, String invitecode) {
//        BaseHttpInformation information = BaseHttpInformation.CLIENT_ADD;
//        HashMap<String, String> params = new HashMap<>();
//        params.put("temp_token", tempToken);// 登陆令牌
//        params.put("username", username);
//        params.put("password", Md5Util.getMd5(XtomConfig.DATAKEY
//                + Md5Util.getMd5(password)));
//        params.put("realname", nickname);
//        params.put("sex", sex);
//        params.put("district_name", district_name);
//        params.put("invitecode", invitecode);
//
//        ExecuteNetTask<ClientAdd> task = new ExecuteNetTask<>(information, params, ClientAdd.class);
//        executeTask(task);
//    }

    /**
     * 重设密码
     */
    public void passwordReset(String temp_token, String keytype, String clienttype,
                              String new_password) {
        BaseHttpInformation information = BaseHttpInformation.PASSWORD_RESET;
        HashMap<String, String> params = new HashMap<>();
        params.put("temp_token", temp_token);// 临时令牌
        params.put("new_password", Md5Util.getMd5(XtomConfig.DATAKEY
                + Md5Util.getMd5(new_password)));// 新密码
        params.put("keytype", keytype);// 密码类型 1：登陆密码 2：支付密码
        params.put("clienttype", clienttype);
        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 保存当前用户坐标接口
     */
    public void positionSave(String token, String clienttype, String lng, String lat) {
        BaseHttpInformation information = BaseHttpInformation.POSITION_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("clienttype", clienttype);
        params.put("lng", lng);
        params.put("lat", lat);

        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 获取用户个人资料接口
     */
    public void clientGet(String token, String id) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);

        ExecuteNetTask<User> task = new ExecuteNetTask<>(information, params, User.class);
        executeTask(task);
    }


    /**
     * 保存用户资料接口
     */
    public void clientSave(String token, String realname, String sex) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("realname", realname);
        params.put("sex", sex);

        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }


    /**
     * 获取支付宝交易签名串
     */
    public void alipay(String token, String keytype, String keyid, String total_fee) {
        BaseHttpInformation information = BaseHttpInformation.ALIPAY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", "1");
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("total_fee", total_fee);

        ExecuteNetTask<AlipayTrade> task = new ExecuteNetTask<>(information, params, AlipayTrade.class);
        executeTask(task);
    }


    /**
     * 获取微信交易签名串
     */
    public void weixin(String token, String keytype, String keyid, String total_fee) {
        BaseHttpInformation information = BaseHttpInformation.WEI_XIN;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", "3");
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("total_fee", total_fee);

        ExecuteNetTask<WeiXinPay> task = new ExecuteNetTask<>(information, params, WeiXinPay.class);
        executeTask(task);
    }

    /**
     * 支付宝信息保存接口
     */
    public void alipaySave(String token, String alipay_no, String alipay_name) {
        BaseHttpInformation information = BaseHttpInformation.ALIPAY_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("clienttype", "1");
        params.put("alipay_no", alipay_no);
        params.put("alipay_name", alipay_name);
        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 申请提现接口
     */
    public void cashAdd(String token, String keytype, String applyfee, String paypassword) {
        BaseHttpInformation information = BaseHttpInformation.CASH_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("clienttype", "1");
        params.put("keytype", keytype);
        params.put("applyfee", applyfee);
        params.put("paypassword", Md5Util.getMd5(XtomConfig.DATAKEY
                + Md5Util.getMd5(paypassword)));

        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }


    /**
     * 银行卡信息保存接口
     */
    public void bankSave(String token, String bankuser, String bankcard, String bankname) {
        BaseHttpInformation information = BaseHttpInformation.BANK_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("clienttype", "1");
        params.put("bankuser", bankuser);
        params.put("bankcard", bankcard);
        params.put("bankname", bankname);

        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 修改并保存密码接口
     */
    public void passwordSave(String token, String keytype, String clienttype,
                             String old_password, String new_password) {
        BaseHttpInformation information = BaseHttpInformation.PASSWORD_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("keytype", keytype);
        params.put("clienttype", clienttype);
        params.put("old_password", Md5Util.getMd5(XtomConfig.DATAKEY
                + Md5Util.getMd5(old_password)));
        params.put("new_password", Md5Util.getMd5(XtomConfig.DATAKEY
                + Md5Util.getMd5(new_password)));

        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }


    /**
     * 退出登录接口
     */
    public void clientLoginout(String token, String keytype) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_LOGINOUT;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("clienttype", keytype);

        CurrentTask task = new CurrentTask(information, params);
        executeTask(task);
    }

}