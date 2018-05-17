package com.kumaapp.zhongxiang;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.emoji.EmojiParser;
import com.hemaapp.hm_FrameWork.emoji.ParseEmojiMsgUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomTimeUtil;
import xtom.frame.util.XtomToastUtil;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.os.Environment.DIRECTORY_PICTURES;


/**
 *
 */
public class BaseUtil {
    private static double EARTH_RADIUS = 6378.137;// 地球半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static String transDuration(long duration) {
        String ds = "";
        long min = duration / 60;
        if (min < 60) {
            ds += (min + "分钟");
        } else {
            long hour = min / 60;
            long rm = min % 60;
            if (rm > 0)
                ds += (hour + "小时" + rm + "分钟");
            else
                ds += (hour + "小时");
        }
        return ds;
    }

    /**
     * 判断wifi连接状态
     *
     * @param ctx
     * @return
     */
    public static boolean isWifiAvailable(Context ctx) {
        ConnectivityManager conMan = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (NetworkInfo.State.CONNECTED == wifi) {
            return true;
        } else {
            return false;
        }
    }

    public static String transDistance(float distance) {
        String ds = "";
        if (distance < 1000) {
            ds += (distance + "m");
        } else {
            float km = distance / 1000;
            ds += (String.format(Locale.getDefault(), "%.3f", km));
        }
        return ds;
    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    /**
     * 计算两点间的距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static Double GetDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


    /**
     * 隐藏用户名
     *
     * @param nickname
     * @return
     */
    public static String hideNickname(String nickname) {
        int length = nickname.length();
        String first = nickname.substring(0, 1);
        String last = nickname.substring(length - 1, length);
        String x = "";
        for (int i = 0; i < length - 2; i++) {
            x += "*";
        }
        return first + x + last;
    }

    /**
     * 转换时间显示形式(与当前系统时间比较),在显示即时聊天的时间时使用
     *
     * @param time 时间字符串
     * @return String
     */
    public static String transTimeChat(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            String current = XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
            String dian24 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                    + " 24:00:00";
            String dian00 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                    + " 00:00:00";

            Date dt = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);
            calendar.add(Calendar.DAY_OF_YEAR, 1);// 日期加1天
            Date dt1 = calendar.getTime();
            String dian48 = XtomTimeUtil.TransTime(sdf.format(dt1), "yyyy-MM-dd")
                    + " 24:00:00";

            calendar.add(Calendar.DAY_OF_YEAR, 1);// 日期加2天
            Date dt2 = calendar.getTime();
            String dian72 = XtomTimeUtil.TransTime(sdf.format(dt2), "yyyy-MM-dd")
                    + " 24:00:00";

//			Date now = null;
            Date date = null;
            Date d24 = null;
            Date d00 = null;
            Date d48 = null;
            Date d72 = null;
            try {
//				now = sdf.parse(current); // 将当前时间转化为日期
                date = sdf.parse(time); // 将传入的时间参数转化为日期
                d00 = sdf.parse(dian00);
                d24 = sdf.parse(dian24);
                d48 = sdf.parse(dian48);
                d72 = sdf.parse(dian72);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (date.getTime() <= d24.getTime()
                    && date.getTime() >= d00.getTime())
                return "今天" + XtomTimeUtil.TransTime(time, "HH:mm");

            if (date.getTime() >= d24.getTime() && date.getTime() <= d48.getTime())
                return "明天" + XtomTimeUtil.TransTime(time, "HH:mm");

            if (date.getTime() >= d48.getTime() && date.getTime() <= d72.getTime())
                return "后天" + XtomTimeUtil.TransTime(time, "HH:mm");

            int sendYear = Integer
                    .valueOf(XtomTimeUtil.TransTime(time, "yyyy"));
            int nowYear = Integer.valueOf(XtomTimeUtil.TransTime(current,
                    "yyyy"));
            if (sendYear < nowYear)
                return XtomTimeUtil.TransTime(time, "yyyy-MM-dd HH:mm");
            else
                return XtomTimeUtil.TransTime(time, "MM-dd HH:mm");

        } catch (Exception e) {
            return null;
        }

    }


    // 聊天中的表情
    public static void SetMessageTextView(Context mContext, TextView mtextview,
                                          String mcontent) {
        if (mcontent == null || "".equals(mcontent)) {
            mtextview.setText("");
            return;
        }

        String unicode = EmojiParser.getInstance(mContext).parseEmoji(mcontent);
        SpannableString spannableString = ParseEmojiMsgUtil
                .getExpressionString(mContext, unicode);
        mtextview.setText(spannableString);
    }

    /**
     * 计算缓存大小的表现形式
     */
    public static String getSize(long size) {

        /** size 如果 小于1024 * 1024,以KB单位返回,反则以MB单位返回 */

        DecimalFormat df = new DecimalFormat("###.##");
        float f;
        if (size < 1024 * 1024) {
            f = (float) ((float) size / (float) 1024);
            return (df.format(new Float(f).doubleValue()) + "KB");
        } else {
            f = (float) ((float) size / (float) (1024 * 1024));
            return (df.format(new Float(f).doubleValue()) + "MB");
        }
    }


    public static String transString(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd",
                Locale.getDefault());
        String str = sdf.format(d);
        return str;
    }


    public static String get2double(double data) {
        DecimalFormat df2 = new DecimalFormat("###.00");
        String value = df2.format(data);
        if (data == 0)
            value = "0.00";
        return value;
    }


    public static void hideInput(Context context, View v) {
        ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
                (v.getWindowToken(), 0);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean net = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || net) {
            return true;
        }
        return false;

    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public static final void hideInputWhenTouchOtherViewBase(Activity activity, MotionEvent ev, List<View> excludeViews) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (excludeViews != null && !excludeViews.isEmpty()) {
                for (int i = 0; i < excludeViews.size(); i++) {
                    if (isTouchView(excludeViews.get(i), ev)) {
                        return;
                    }
                }
            }
            View v = activity.getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        activity.getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        }
    }

    public static final boolean isTouchView(View view, MotionEvent event) {
        if (view == null || event == null) {
            return false;
        }
        int[] leftTop = {0, 0};
        view.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + view.getHeight();
        int right = left + view.getWidth();
        if (event.getRawX() > left && event.getRawX() < right
                && event.getRawY() > top && event.getRawY() < bottom) {
            return true;
        }
        return false;
    }

    public static final boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            return !isTouchView(v, event);
        }
        return false;
    }

    //加
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    //减
    public static String subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    public static float sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).floatValue();
    }

    //乘
    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    //除
    public static String divide(String v1, String v2, int scale) {
        //如果精确范围小于0，抛出异常信息
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    //保留小数
    public static String round(String v, int scale) {
        if (scale < 0) {
            return "0";
        }
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }


    public static String initImagePath(Context context) {
        String imagePath;
        try {

            String cachePath_internal = XtomFileUtil.getCacheDir(context)
                    + "images/";// 获取缓存路径
            File dirFile = new File(cachePath_internal);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            imagePath = cachePath_internal + "share_1.png";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic;

                pic = BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher);

                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            imagePath = null;
        }
        return imagePath;
    }

    public static void fitPopupWindowOverStatusBar(PopupWindow pop, boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(pop, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转进行安装apk
     *
     * @param file
     */
    public static void installApk(Context context, File file) {
        if (file != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.hemaapp.wcpc_user.fileprovider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                context.startActivity(intent);
            }
        }
    }

    /**
     * 生成视图的预览
     *
     * @param activity
     * @param v
     * @return 视图生成失败返回null
     * 视图生成成功返回视图的绝对路径
     */
    public static String saveImage(Activity activity, View v) {
        Bitmap bitmap;
        String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/";
        //String path = XtomFileUtil.getFileDir(activity) + "preview.png";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        path = path + "preview.png";
        File file = new File(path);

        try {
            if (file.exists())
                file.delete();
            file.createNewFile();
            bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], v.getWidth(), v.getHeight());
            FileOutputStream fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
            // }
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            XtomToastUtil.showShortToast(activity, "已保存图片到手机");
            Log.e("png", "生成预览图片成功：" + path);
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("png", "生成预览图片失败：" + e);
        } catch (IllegalArgumentException e) {
            Log.e("png", "width is <= 0, or height is <= 0");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 清理缓存
            view.destroyDrawingCache();
        }
        return null;

    }

    //dip和px转换
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    /**
     * 适合播放声音短，文件小
     * 可以同时播放多种音频
     * 消耗资源较小
     */
    public static void playSound(Context context,int rawId) {
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频的数量
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的类
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        }
        //第一个参数Context,第二个参数资源Id，第三个参数优先级
        soundPool.load(context, rawId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(1, 1, 1, 0, 0, 1);
            }
        });
        //第一个参数id，即传入池中的顺序，第二个和第三个参数为左右声道，第四个参数为优先级，第五个是否循环播放，0不循环，-1循环
        //最后一个参数播放比率，范围0.5到2，通常为1表示正常播放
//        soundPool.play(1, 1, 1, 0, 0, 1);
        //回收Pool中的资源
        //soundPool.release();
    }
}
