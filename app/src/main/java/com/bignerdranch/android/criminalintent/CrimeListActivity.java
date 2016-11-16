package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

//import android.app.Fragment;
// TODO 与CrimeListFragment:ListFragment冲突 不能使用android.app.Fagement ??



/**
 * create in 2014-11-02
 * add in chapter9
 * tuoguan crime_list_fragment
 * */
public class CrimeListActivity extends SingleFragmentActivity {

	public Fragment createFragment() {
		return new CrimeListFragment();
	}

}
