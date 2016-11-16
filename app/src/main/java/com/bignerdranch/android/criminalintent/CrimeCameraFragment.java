package com.bignerdranch.android.criminalintent;


import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
/**
 * add in chapter 19
 * 照相机Activity托管的Fragment视图
 * */
public class CrimeCameraFragment extends Fragment {
	private static final String TAG="CrimeCameraFragment";
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	//add in chapter20
	private View mProgressContainer;
	
	public static final String EXTRA_PHOTO_FILENAME="com.bignerdranch.android.criminalintent.photo_filename";
	
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
		View v=inflater.inflate(R.layout.fragment_crime_camera, parent, false);
		Button takePictureButton=(Button)v.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//getActivity().finish();	
			//add in chapter 20
				if(mCamera!=null){
					mCamera.takePicture(mShutterCallback, null, mJpegCallback);
				}
			}
		});
		mSurfaceView=(SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder holder=mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if(mCamera!=null){
					mCamera.stopPreview();
				}
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				try{
				if(mCamera!=null){
					mCamera.setPreviewDisplay(holder);
				}
				}catch(IOException exception){
					
				}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				Camera.Parameters parameters=mCamera.getParameters();
				//Size s=null;
				Size s=getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPreviewSize(s.width, s.height);
				s=getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPictureSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try{
					mCamera.startPreview();					
				}catch(Exception ex){
					mCamera.release();
					mCamera=null;
				}
			}
		});
		
		//add in chapter20
		mProgressContainer=v.findViewById(R.id.crime_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);//即使方法需要int，也要定义整数常量，数字是无意义的。
		return v;
	}
	
	/**
	 * 打开相机
	 * */
	public void onResume(){
		super.onResume();
		
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
			mCamera=Camera.open(0);
		}else{
			mCamera=Camera.open();
		}
	}
	
	/**
	 * 关闭相机
	 * */
	public void onPause(){
		super.onPause();//重写父类生命周期的方法时，别忘记调用父类自己定义的方法
		if(mCamera!=null){
			mCamera.release();
			mCamera=null;//why?
		}
	}
	/**
	 * 获取最大尺寸
	 * */
	private Size getBestSupportedSize(List<Size> sizes,int width,int height){
		Size bestSize=sizes.get(0);
		int largestArea=bestSize.width*bestSize.height;
		for (Size size : sizes) {
			int area=size.height*size.width;
			if(area>largestArea){
				bestSize=size;
				largestArea=area;
			}
		}
		return bestSize;
	}
	
	
	//实现从相机的预览中捕获一帧图像，然后将它保存为jpg格式，要拍摄一张照片需要实现Camera.takePicture方法
	/*public final void takePicture(Camera.ShutterCallback shutter,Camera.PictureCallback raw,Camera.PictureCallback jpeg){
		
	}*/
	//需要实现Camera.ShutterCallback and Camera.PictureCallback interface
	public Camera.ShutterCallback mShutterCallback=new Camera.ShutterCallback() {
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};
	
	private Camera.PictureCallback  mJpegCallback=new Camera.PictureCallback(){
		public void onPictureTaken(byte[] data,Camera camera){
			String fileName=UUID.randomUUID().toString()+".jpg";
			FileOutputStream os=null;
			boolean success=true;
			try{
				os=getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
				os.write(data);
			}catch(Exception e){
				Log.e(TAG,"Error writing to file "+fileName,e);
				success=false;
			}finally{
				try{
					if(os!=null){
						os.close();
					}
				}catch(Exception e){
					Log.e(TAG,"Error writing to file "+fileName,e);
					success=false;	
				}
			}
			if(success){
				Intent i=new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME,fileName);
				getActivity().setResult(Activity.RESULT_OK,i);
				Log.i(TAG, "JPEG saved at "+fileName);
			}else{
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();
		}
	};
}
