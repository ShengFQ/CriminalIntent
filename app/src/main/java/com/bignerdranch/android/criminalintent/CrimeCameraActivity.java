package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class CrimeCameraActivity extends SingleFragmentActivity {

	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//以上代码必须在Activity.setContentView创建Activity视图之前调用
		super.onCreate(savedInstanceState);
	}
	@Override
	public Fragment createFragment() {
		return new CrimeCameraFragment();
	}

}
