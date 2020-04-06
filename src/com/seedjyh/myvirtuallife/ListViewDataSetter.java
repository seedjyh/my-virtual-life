package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.SimpleAdapter;

public class ListViewDataSetter
{
	public ListViewDataSetter(
			ArrayList<HashMap<String, String> > listview_data,
			SimpleAdapter listview_adapter,
			int index_in_array,
			String key_in_hashmap
	)
	{
		listview_data_ = listview_data;
		listview_adapter_ = listview_adapter;
		index_in_array_ = index_in_array;
		key_in_hashmap_ = key_in_hashmap;
	}
	
	public void SetValue(int value)
	{
		listview_data_.get(index_in_array_).put(key_in_hashmap_, "" + value);
		listview_adapter_.notifyDataSetChanged();
		return;
	}
	
	
	private ArrayList<HashMap<String, String> > listview_data_;
	private SimpleAdapter listview_adapter_;
	int index_in_array_;
	String key_in_hashmap_;
}
