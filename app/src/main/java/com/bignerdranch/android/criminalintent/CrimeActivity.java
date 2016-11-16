package com.bignerdranch.android.criminalintent;

import java.util.UUID;

import android.support.v4.app.Fragment;

/*import android.app.Fragment;*/

/**
 * create in 2014-10-25
 * modify in 2014-10-27
 * modify in2014-11-02 become a common tuo guan activity
 * 
 * */
public class CrimeActivity extends SingleFragmentActivity{
	@Override
	//托管fragment实例
	public Fragment createFragment() {
		//add in 2014-11-03 chapter10
		UUID id=(UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		return CrimeFragment.getInstance(id);
		//add in 2014-11-02 chapter9
		//return new CrimeFragment();
	}


}
