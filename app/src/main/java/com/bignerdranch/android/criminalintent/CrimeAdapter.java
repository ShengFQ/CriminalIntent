package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CrimeAdapter extends ArrayAdapter<Crime> {

	private Context mActivityContext;
	public Context getActivityContext() {
		return mActivityContext;
	}

	public void setActivityContext(Context activityContext) {
		mActivityContext = activityContext;
	}

	public CrimeAdapter(Context appContext,ArrayList<Crime> list){
		super(appContext,0,list);
		setActivityContext(appContext.getApplicationContext());
	}
	
	//display the view item
	public View getView(int position,View convertView,ViewGroup parent){
		//inflat the convertView
		if(convertView==null){
			convertView=View.inflate(getActivityContext(),R.layout.list_item_crime,null);
		}
		//get the current crime instance
		Crime c=getItem(position);
		//fill the instance to display component
		TextView titleTextView=(TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
		TextView dateTextView=(TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
		dateTextView.setText(c.getDate().toString());
		
		CheckBox solvedCheckBox=(CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
		solvedCheckBox.setChecked(c.isSolved());
		return convertView;
		
	}

	
}
