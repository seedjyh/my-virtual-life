package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskInfo
{
	public TaskInfo(int id, String title, String subtitle, String description, ArrayList<HashMap<String, String> > attribute_listview_data)
	{
		id_ = id;
		title_ = title;
		subtitle_ = subtitle;
		description_ = description;
		attribute_listview_data_ = attribute_listview_data;
		return;
	}
	
	public int id()
	{
		return id_;
	}
	
	public String title()
	{
		return title_;
	}
	
	public String subtitle()
	{
		return subtitle_;
	}
	
	public String description()
	{
		return description_;
	}
	
	public ArrayList<HashMap<String, String> > attribute_listview_data()
	{
		return attribute_listview_data_;
	}

	private int id_;
	private String title_;
	private String subtitle_;
	private String description_;
	private ArrayList<HashMap<String, String> > attribute_listview_data_;
};
