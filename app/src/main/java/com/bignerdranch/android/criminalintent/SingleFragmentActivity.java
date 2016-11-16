package com.bignerdranch.android.criminalintent;


import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
/**
 * create in 2014-11-02
 * add in chapter9
 * abstractly fragmentActivity for my project,extends by other fragmentActivity tuo guan fragment
 * 
 * */
public abstract class SingleFragmentActivity extends FragmentActivity {
	public abstract Fragment createFragment();// add in chapter9
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);   
		//get fragmentManager :manager fragment queue and transaction back stack
		FragmentManager fragmentManager=this.getSupportFragmentManager();//TODO 该方法指定只能使用扩展包的类，影响后期使用Fragment
		//FragmentManager fragmentManager=this.getFragmentManager();
		Fragment fragment=fragmentManager.findFragmentById(R.id.fragmentContainer);
		if(fragment==null){
			fragment=createFragment();
			fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
		}
	}
}
