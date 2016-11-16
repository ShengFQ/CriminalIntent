package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author SHENGFQ
 * 坏习惯的证据-图片
 * @version 01
 * @date 2015-03-04
 * */
public class Photo {
	private static final String JSON_FILENAME="filename";
	
	private String mFilename;
	public String getFilename(){
		return mFilename;
	}
	public Photo(String filename){
		mFilename=filename;
	}
	
	public JSONObject toJSON() throws JSONException{
		JSONObject json=new JSONObject();
		json.put(JSON_FILENAME, mFilename);
		return json;
	}
	
	public Photo(JSONObject json) throws JSONException{
		mFilename=json.getString(JSON_FILENAME);
	}
}
