package com.kumaapp.zhongxiang;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kumaapp.zhongxiang.util.PermissionUtil;

import java.io.File;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;


public class BaseImageWay extends XtomObject {
	private Activity mContext;//
	private Fragment mFragment;//

	private PopupWindow mWindow;
	private ViewGroup mViewGroup;
	private TextView tv_text1;
	private TextView tv_text2;
	private TextView cancel;
	
	protected int albumRequestCode;//
	protected int cameraRequestCode;//
	private static final String IMAGE_TYPE = ".jpg";//
	private String imagePathByCamera;// ·

	public BaseImageWay(Activity mContext, int albumRequestCode,
                        int cameraRequestCode) {
		this.mContext = mContext;
		this.albumRequestCode = albumRequestCode;
		this.cameraRequestCode = cameraRequestCode;
	}

	public BaseImageWay(Fragment mFragment, int albumRequestCode,
                        int cameraRequestCode) {
		this.mFragment = mFragment;
		this.albumRequestCode = albumRequestCode;
		this.cameraRequestCode = cameraRequestCode;
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void show() {
		
		if (mWindow != null) {
			mWindow.dismiss();
		}
		mWindow = new PopupWindow(mContext);
		mWindow.setWidth(LayoutParams.MATCH_PARENT);
		mWindow.setHeight(LayoutParams.MATCH_PARENT);
		mWindow.setBackgroundDrawable(new BitmapDrawable());
		mWindow.setFocusable(true);
		mWindow.setAnimationStyle(R.style.PopupAnimation);
		mViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(
				R.layout.pop_two_type, null);
		tv_text1 = (TextView) mViewGroup.findViewById(R.id.textview);
		tv_text2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
		cancel = (TextView) mViewGroup.findViewById(R.id.textview_2);
		BaseUtil.fitPopupWindowOverStatusBar(mWindow, true);
		mWindow.setContentView(mViewGroup);
		mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
		tv_text1.setText("拍照");
		tv_text1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWindow.dismiss();
				click(1);
			}
		});

		tv_text2.setText("从手机相册选择");
		tv_text2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWindow.dismiss();
				click(0);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWindow.dismiss();
			}
		});
	}
	
	

	private void click(int which) {
		switch (which) {
		case 0:
			checkAlbum();
			break;
		case 1:
			camera();
			break;
		case 2:
			break;
		}
	}

	//检查相册权限
	private void checkAlbum(){
		if(mContext != null){
			if (ContextCompat.checkSelfPermission(mContext,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)
					!= PackageManager.PERMISSION_GRANTED) {//判断是否拥有读取相册的权限
				ActivityCompat.requestPermissions(mContext,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
						PermissionUtil.WRITE_EXTERNAL_STORAGE);
			} else {
				album();
			}
		}else if(mFragment != null){
			if (ContextCompat.checkSelfPermission(mFragment.getActivity(),
					Manifest.permission.WRITE_EXTERNAL_STORAGE)
					!= PackageManager.PERMISSION_GRANTED) {//判断是否拥有读取相册的权限
				ActivityCompat.requestPermissions(mFragment.getActivity(),
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						PermissionUtil.WRITE_EXTERNAL_STORAGE);
			} else {
				album();
			}
		}
	}

	public void album() {
		Intent it1 = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		if (mContext != null)
			mContext.startActivityForResult(it1, albumRequestCode);
		else
			mFragment.startActivityForResult(it1, albumRequestCode);
	}
	public void camera() {
		if (cameraPermissionEnable()) {
			String imageName = XtomBaseUtil.getFileName() + IMAGE_TYPE;
			Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Context context = mContext == null ? mFragment.getActivity() : mContext;
			String imageDir = XtomFileUtil.getTempFileDir(context);
			imagePathByCamera = imageDir + imageName;
			File file = new File(imageDir);
			if (!file.exists()) {
				file.mkdir();
			}
			// 设置图片保存路径
			File out = new File(file, imageName);
			Uri uri;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				uri = FileProvider.getUriForFile(context, context.getApplicationInfo().packageName + ".fileprovider", out);
				it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			} else {
				uri = Uri.fromFile(out);
			}
			it.putExtra(MediaStore.EXTRA_OUTPUT, uri);

			if (mContext == null) {
				mFragment.startActivityForResult(it, cameraRequestCode);
			} else {
				mContext.startActivityForResult(it, cameraRequestCode);
			}
		}
	}

	/**
	 * @return
	 */
	public String getCameraImage() {
		return imagePathByCamera;
	}
	private boolean cameraPermissionEnable() {
		Activity activity = mContext == null ? mFragment.getActivity() : mContext;
		if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PermissionUtil.CAMERA);
			return false;
		}
		return true;
	}

}