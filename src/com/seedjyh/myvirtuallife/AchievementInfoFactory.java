package com.seedjyh.myvirtuallife;

import java.util.HashMap;

import android.app.Activity;

public class AchievementInfoFactory
{
	public AchievementInfoFactory(Activity activity)
	{
		activity_ = activity;
	}
	
	public HashMap<String, String> Create(int id, String title, String description, String attribute_name, int current_value, int request_value)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		result.clear();
		result.put(activity_.getString(R.string.achievement_listview_id_key), "" + id);
		result.put(activity_.getString(R.string.achievement_listview_title_key), title);
		result.put(activity_.getString(R.string.achievement_listview_description_key), description);
		
		String status = attribute_name + "(" + current_value + "/" + request_value + ")";
		result.put(activity_.getString(R.string.achievement_listview_status_key), status);
		return result;
	}
	
	private Activity activity_;
}
