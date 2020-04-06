package com.seedjyh.myvirtuallife;

import java.util.HashMap;

import android.app.Activity;

public class PlayerAttributeFactory
{
	public PlayerAttributeFactory(Activity activity)
	{
		activity_ = activity;
	}
	
	public HashMap<String, String> Create(String id, String name, String value)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		result.clear();
		result.put(activity_.getString(R.string.player_attribute_listview_id_key), id);
		result.put(activity_.getString(R.string.player_attribute_listview_name_key), name);
		result.put(activity_.getString(R.string.player_attribute_listview_value_key), value);
		return result;
	}

	private Activity activity_;
}