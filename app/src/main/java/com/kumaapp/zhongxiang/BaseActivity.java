package com.kumaapp.zhongxiang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.hemaapp.hm_FrameWork.HemaActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.kumaapp.zhongxiang.activity.LoginActivity;
import com.kumaapp.zhongxiang.model.User;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.XtomActivityManager;
import xtom.frame.net.XtomNetWorker;

/**
 */
public abstract class BaseActivity extends HemaActivity {
    public List<View> noHideInput = new ArrayList<>();//点击这些view不会收回键盘
    public ImmersionBar mImmersionBar;
    TokenReceiver receiver;

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);
        noHideInput.clear();
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f); //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.keyboardEnable(true);//软键盘遮挡输入框冲突
        //mImmersionBar .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mImmersionBar.init();   //所有子类都将继承这些相同的属性
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new TokenReceiver();
        IntentFilter filter = new IntentFilter("token");
        registerReceiver(receiver, filter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        BaseUtil.hideInputWhenTouchOtherViewBase(this, ev, noHideInput);//点击屏幕收回输入键盘（除去noHideInput和Eidittext）
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }

    @Override
    protected HemaNetWorker initNetWorker() {
        return new BaseNetWorker(mContext);
    }

    @Override
    public BaseNetWorker getNetWorker() {
        return (BaseNetWorker) super.getNetWorker();
    }

    @Override
    public BaseApplication getApplicationContext() {
        return (BaseApplication) super.getApplicationContext();
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        if (baseResult.getError_code() == 404) {
            showTextDialog("您即将访问的页面不存在");
            finish();
        }
    }

    @Override
    public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                     HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
        switch (failedType) {
            case 0:// 服务器处理失败
                int error_code = baseResult.getError_code();
                switch (error_code) {
                    case 102:// 密码错误
                        XtomActivityManager.finishAll();
                        Intent it = new Intent(mContext, LoginActivity.class);
                        startActivity(it);
                        return true;
                    default:
                        break;
                }
            case XtomNetWorker.FAILED_HTTP:// 网络异常
            case XtomNetWorker.FAILED_DATAPARSE:// 数据异常
            case XtomNetWorker.FAILED_NONETWORK:// 无网络
                break;
        }
        return false;
    }

    // ------------------------下面填充项目自定义方法---------------------------

    private class TokenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra("token");
            User user = getUser();
            user.setToken(token);
            BaseApplication.getInstance().setUser(user);
        }
    }
    /**
     * 获取用户
     */
    public User getUser() {
        User user = BaseApplication.getInstance().getUser();
        if (user == null)
            return null;
        else {
            return user;
        }
    }
}
