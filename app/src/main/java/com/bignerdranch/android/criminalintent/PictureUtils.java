package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * 从本地的图片文件中加载一张缩略图到当前窗口
 * */
public class PictureUtils {
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity a,String path){
		Display display=a.getWindowManager().getDefaultDisplay();
		float destWidth=display.getWidth();
		float destHeight=display.getHeight();
		
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(path,options);
		
		float srcWidth=options.outWidth;
		float srcHeight=options.outHeight;
		
		int inSampleSize=1;
		if(srcHeight>destHeight || srcWidth>destWidth){
			if(srcWidth>srcHeight){
				inSampleSize=Math.round(srcHeight/destHeight);
			}else{
				inSampleSize=Math.round(srcWidth/destWidth);
			}
		}
		
		options=new BitmapFactory.Options();
		options.inSampleSize=inSampleSize;
		Bitmap bitmap=BitmapFactory.decodeFile(path,options);
		return new BitmapDrawable(a.getResources(),bitmap);
	}
	
	/**
	 * 清理卸载图片
	 * */
	public static void cleanImagView(ImageView imageView){
		if(!(imageView.getDrawable() instanceof BitmapDrawable))
			return;
		BitmapDrawable b=(BitmapDrawable)imageView.getDrawable();
		b.getBitmap().recycle();
		imageView.setImageBitmap(null);
	}
}
