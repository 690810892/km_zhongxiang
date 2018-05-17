package com.kumaapp.zhongxiang.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.kumaapp.zhongxiang.BaseActivity;
import com.kumaapp.zhongxiang.BaseApplication;
import com.kumaapp.zhongxiang.BaseHttpInformation;
import com.kumaapp.zhongxiang.BaseNetWorker;
import com.kumaapp.zhongxiang.R;
import com.kumaapp.zhongxiang.adapter.ShowAdapter;
import com.kumaapp.zhongxiang.model.User;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 引导页
 */
public class ShowActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ShowAdapter mAdapter;

    public boolean isAutomaticLogin = false;// 是否自动登录

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show);
        super.onCreate(savedInstanceState);
        String[] imgs = new String[] { "start_1.png", "start_2.png", "start_3.png", "start_4.png"};
        mAdapter = new ShowAdapter(mContext, imgs);
        mViewPager.setAdapter(mAdapter);
        requestPermission();
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(mContext, new String[]{ Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 5);
            }
        }
    }

    // 检查是否自动登录
    private boolean isAutoLogin() {
        isAutomaticLogin = "true".equals(XtomSharedPreferencesUtil.get(
                mContext, "isAutoLogin"));
        return isAutomaticLogin;
    }

    @Override
    protected void onDestroy() {
        XtomSharedPreferencesUtil.save(mContext, "isShowed", "true"); // 将isShowed参数保存到XtomSharedPreferencesUtils里面
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }
    public void dealData() {
        //判断是否自动登录
        if(isAutoLogin()){
            String username = XtomSharedPreferencesUtil.get(this, "username");
            String password = XtomSharedPreferencesUtil.get(this, "password");
            if (!isNull(username) && !isNull(password)) {
                BaseNetWorker netWorker = getNetWorker();
                netWorker.clientLogin(username, password);
            }else if (HemaUtil.isThirdSave(mContext)) {// 如果是第三方登录
                BaseNetWorker netWorker = getNetWorker();
                netWorker.thirdSave();
            } else {
                toLogin();
                finish();
            }
        }else {
            toLogin();
            finish();
        }
    }
    private void toLogin(){
        Intent it = new Intent(mContext, LoginActivity.class);
        startActivity(it);
    }

    @Override
    protected void findView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected boolean onKeyBack() {
        return false;
    }

    @Override
    protected boolean onKeyMenu() {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void setListener() {

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                HemaArrayParse<User> uResult = (HemaArrayParse<User>) baseResult;
                user = uResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);
                toMain();
                break;
            case THIRD_SAVE:
                HemaArrayResult<User> mResult = (HemaArrayResult<User>) baseResult;
                user = mResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);
                toMain();
                break;
            default:
                break;
        }
    }

    private void toMain(){
//        Intent it = new Intent(mContext, MainActivity.class);
//        startActivity(it);
//        finish();
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
            case THIRD_SAVE:
                toLogin();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
            case THIRD_SAVE:
                toLogin();
                break;
            default:
                break;
        }
    }
}
