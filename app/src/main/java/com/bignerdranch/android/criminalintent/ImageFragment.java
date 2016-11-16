package com.bignerdranch.android.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
/**
 * 显示坏习惯图片的DialogFragment预览窗口
 * */
public class ImageFragment extends DialogFragment {
	public static final String EXTRA_IMAGE_PATH="com.bignerdranch.android.criminalintent.image_path";
	private ImageView mImageView;
	public static ImageFragment newInstance(String imagePath){
		Bundle args=new Bundle();
		args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
		
		ImageFragment fragment=new ImageFragment();
		fragment.setArguments(args);
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		
		return fragment;
	}
	/**
	 * 要实现显示大图片的对话框，采用覆盖onCreateView方法并使用简单视图的方式，要比覆盖onCreateDialog方法并使用Dialog更简单，快捷。
	 * */
	public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
		mImageView=new ImageView(getActivity());//通过代码构建
		String path=(String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
		BitmapDrawable image=PictureUtils.getScaledDrawable(getActivity(), path);
		mImageView.setImageDrawable(image);
		return mImageView;	
	}
	/**
	 * 释放
	 * */
	public void onDestroyView(){
		super.onDestroyView();
		PictureUtils.cleanImagView(mImageView);
	}
}
