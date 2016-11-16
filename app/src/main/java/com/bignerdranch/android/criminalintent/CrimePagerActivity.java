package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
/*
 * TODO 只能使用android.support.v4.app包，why？？
 * import android.app.Fragment;
import android.app.FragmentManager;*/


/**
 * add in 2014-11-08 substitute for CrimeActivity tuo guan CrimeFragment,not use
 * FrameLayout ,use viewPager(like adapterView)
 * from chapter11
 * */
public class CrimePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;//viewpager like adapterview as listview 
	private ArrayList<Crime> mCrimes;
	protected void onCreate(Bundle arg0) {		
		super.onCreate(arg0);
		mViewPager=new ViewPager(getApplication());
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);//一般的activity的xml视图，在这一步骤都是xml定义的视图文件id，这里设置的ViewPager是代码创建视图方式
		
		mCrimes=CrimeLab.getInstance(getApplication()).getCrimes();
		//viewpager与apdaterview类似，同理，也需要adapter的支持，这里使用的是PageAdapter
		FragmentManager fm=this.getSupportFragmentManager();//TODO 该处使得调用android.support.v4.app.Fragment
		//FragmentManager fm=this.getFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm){

			@Override
			public Fragment getItem(int arg0) {
				Crime crime=mCrimes.get(arg0);
				return CrimeFragment.getInstance(crime.getId());//tuo guan le CrimeFragment
			}

			@Override
			public int getCount() {
				return mCrimes.size();
			}});
		//add in chapter11 for initialization the viewpager currentItem ,or the the first item to show
		UUID uuid=(UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (int i = 0; i < mCrimes.size(); i++) {
			if(uuid.equals(mCrimes.get(i).getId())){
				mViewPager.setCurrentItem(i);
				break;
			}
		}
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {		
			public void onPageSelected(int arg0) {
				Crime crime=mCrimes.get(arg0);
				if(crime.getTitle()!=null){
					setTitle(crime.getTitle());
				}
			}			
			public void onPageScrolled(int arg0, float arg1, int arg2) {}			
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
}
