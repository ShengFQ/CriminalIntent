package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {
	//Map数据结构的Key一般用常量
	private static final String JSON_ID="id";
	private static final String JSON_TITLE="title";
	private static final String JSON_SOLVED="solved";
	private static final String JSON_DATE="date";
	private static final String JSON_PHOTO="photo";
	private static final String JSON_SUSPECT="suspect";
	//entity class
	private UUID mId;//read only
	private String mTitle;//read and write
	private Date mDate;
	private boolean isSolved;
	private Photo mPhoto;//add in chapter20
	private String mSuspect;//add in chapter20
	public Crime() {
		mId=UUID.randomUUID();
		mDate=new Date();//mDate没有初始化，应用程序无法启动起来
		//DateFormat.format("YYYY-MM-DD", new Date()).toString();
	}
	/***/
	public Crime(JSONObject json) throws JSONException{
		mId=UUID.fromString(json.getString(JSON_ID));
		if(json.has(JSON_TITLE)){
			mTitle=json.getString(JSON_TITLE);			
		}
		isSolved=json.getBoolean(JSON_SOLVED);
		mDate=new Date(json.getLong(JSON_DATE));
		if(json.has(JSON_PHOTO)){
			mPhoto=new Photo(json.getJSONObject(JSON_PHOTO));
		}
		if(json.has(JSON_SUSPECT)){
			mSuspect=json.getString(JSON_SUSPECT);
		}
	}
	public UUID getId(){
		return mId;
	}
	
	public String getTitle(){
		return mTitle;
	}
	public void setTitle(String title){
		this.mTitle=title;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return isSolved;
	}

	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}
	
	public Photo getPhoto(){
		return mPhoto;
	}
	
	public void setPhoto(Photo photo){
		this.mPhoto=photo;
	}
	
	public String getSuspect(){
		return mSuspect;
	}
	public void setSuspect(String suspect){
		mSuspect=suspect;
	}
	public String toString(){
		return mTitle;
	}
	
	/**
	 * add in 2014-12-25 chapter17
	 * java中有高级方法可以直接将对象输出为JSON格式JSONObject，但是最终还是下面的方法起作用。
	 * */
	public JSONObject toJSON () throws JSONException{
		JSONObject json=new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, isSolved);
		json.put(JSON_DATE, mDate.getTime());
		if(mPhoto!=null){
		json.put(JSON_PHOTO, mPhoto.toJSON());
		}
		json.put(JSON_SUSPECT, mSuspect);
		return json;
	}
}
