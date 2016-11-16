package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

/**
 * create in 2014-11-02
 * storage Crimes and single instance model
 * add in chapter9
 * */
public class CrimeLab {
	
	//add in chapter17
	private static final String TAG="CrimeLab";
	private static final String FILENAME="crimes.json";
	
	private ArrayList<Crime> mCrimes;
	//create a single instance for 3 steps
	//1. add  a private static instance
	//2.make the instructure private
	//3.get instance by method
	private static CrimeLab instance;
	private Context mAppContext;
	private CriminalIntentJSONSerializer mSerializer;
	private CrimeLab(Context context){
		//initialization all variables
		this.mAppContext=context;
		this.mCrimes=new ArrayList<Crime>();
		this.mSerializer=new CriminalIntentJSONSerializer(mAppContext,FILENAME);
		/*for (int i = 0; i < 100; i++) {
			Crime crime=new Crime();
			crime.setTitle("Crime #"+i);
			crime.setSolved(i%2==0);
			mCrimes.add(crime);
		}*/
		//add in 2014-12-29 chapter17
		try{
			mCrimes=mSerializer.loadCrimes();
		}catch(Exception e){
			mCrimes=new ArrayList<Crime>();
			Log.e(TAG, "ERROR LOADING crimes",e);
		}
	}
	public static CrimeLab getInstance(Context context){
		if(instance==null){
			//create a new instance
			instance=new CrimeLab(context.getApplicationContext());
		}
		return instance;
	}
	
	//arraylist getter
	public ArrayList<Crime> getCrimes(){
		return mCrimes;
	}
	
	//get crime by id************???
	//add in 2014-11-03 chapter10
	public Crime getCrime(UUID id){
		for(Crime crime:mCrimes){
			if(crime.getId().equals(id)){
				return crime;
			}			
		}
		return null;
	}
	
	/**add in chapter16
	 * 对于数据的添加方法，只对arrayList进行修改，因为在onPause方法里面有List的全部序列化
	 * */
	public void addCrime(Crime c){
		mCrimes.add(c);
	}
	/**
	 * add in chapter 18
	 *  对于数据的删除方法，只对arrayList进行修改，因为在onPause方法里面有List的全部序列化
	 * */
	public void deleteCrime(Crime c){
		mCrimes.remove(c);
	}
	/**
	 * add in chapter17 in 2014-12-25
	 * **/
	public boolean saveCrimes(){
		try{
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "crimes saved to file");
			return true;
		}catch(Exception e){
			Log.d(TAG, "Error saving crimes:",e);
			return false;
		}
	}
}
