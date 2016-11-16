package com.bignerdranch.android.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
/*import android.app.DialogFragment;*/
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.support.v4.app.DialogFragment;

/**
 * show the date picker dialog and time picker dialog add in 2014-11-10
 * ,chapter12
 * */
public class DatepickeFragment extends DialogFragment {
	public static final String EXTRA_DATE="com.bignerdranch.android.criminalintent.date";
	private Date mDate;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//获取传递的日期对象
				mDate=(Date)getArguments().getSerializable(EXTRA_DATE);
		//解析Date
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(mDate);
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
		int day=calendar.get(Calendar.DAY_OF_MONTH);
		//初始化Dialog的视图
		View view=(View)getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		DatePicker datePicker=(DatePicker)view.findViewById(R.id.dialog_date_datePicker);	
		//初始化dialog的年月日
		datePicker.init(year, month, day, new OnDateChangedListener() {			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				//保存mdate
				mDate=new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});
		//保存mDate
		// return super.onCreateDialog(savedInstanceState);
		return new AlertDialog.Builder(getActivity())
		.setView(view)
		.setTitle(R.string.date_picker_title)
		.setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_OK);//????????
			}
		}).create();
	}
	
	//create an argument getInstance
	public static  DatepickeFragment getInstance(Date argu){
		Bundle bundle =new Bundle();
		bundle.putSerializable(EXTRA_DATE, argu);
		DatepickeFragment fm=new DatepickeFragment();
		fm.setArguments(bundle);
		return fm;
	}
	
	private void sendResult(int resultcode){
		Intent i=new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultcode, i);
	}
}
