package com.kumaapp.zhongxiang.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.kumaapp.zhongxiang.BaseActivity;
import com.kumaapp.zhongxiang.activity.ShowActivity;

import java.io.IOException;

/**
 * 引导页
 */
public class ShowAdapter extends PagerAdapter {

    private BaseActivity mContext;
    private String[] imgs;

    public ShowAdapter(Context mContext, String[] imgs) {
        this.mContext = (BaseActivity) mContext;
        this.imgs = imgs;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
        View mView;
        if (container.getChildAt(position) == null) {
            mView = new ImageView(mContext);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            mView.setLayoutParams(params);
            try {
                BitmapDrawable d = new BitmapDrawable(mContext.getAssets()
                        .open(imgs[position]));
                mView.setBackgroundDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(position == getCount() - 1){
                mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((ShowActivity)mContext).dealData();
                    }
                });
            }
            container.addView(mView, position);
        } else
            mView = container.getChildAt(position);

        return mView;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}
