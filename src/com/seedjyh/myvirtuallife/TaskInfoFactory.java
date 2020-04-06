package com.seedjyh.myvirtuallife;

import java.util.HashMap;

import android.app.Activity;

public class TaskInfoFactory
{
	public TaskInfoFactory(Activity activity)
	{
		activity_ = activity;
	}
	
	public HashMap<String, String> Create(int id, String name, String value)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		result.clear();
		result.put(activity_.getString(R.string.task_listview_id_key), "" + id);
		result.put(activity_.getString(R.string.task_listview_title_key), name);
		result.put(activity_.getString(R.string.task_listview_subtitle_key), value);
		return result;
	}
	
	private Activity activity_;
}