package com.seedjyh.myvirtuallife;

import java.util.HashMap;

import android.app.Activity;

public class LogInfoFactory
{
	public LogInfoFactory(Activity activity)
	{
		activity_ = activity;
	}
	
	public HashMap<String, String> Create(int id, String datetime, String content)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		result.clear();
		result.put(activity_.getString(R.string.log_listview_id_key), "" + id);
		result.put(activity_.getString(R.string.log_listview_datetime_key), datetime);
		result.put(activity_.getString(R.string.log_listview_content_key), content);
		return result;
	}
	
	
	private Activity activity_;
}