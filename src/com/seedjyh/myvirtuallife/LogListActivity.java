package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class LogListActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_list);
		Init();
		Bind();
		RefreshLogListView();
	}
	
	
	private void Init()
	{
		button_listener_to_see_oldest_log = new Button.OnClickListener() {
			public void onClick(View v)
			{
				log_page_index = 1;
				RefreshLogListView();
				return;
			}
		};

		button_listener_to_see_older_log = new Button.OnClickListener() {
			public void onClick(View v)
			{
				log_page_index--;
				RefreshLogListView();
				return;
			}
		};

		button_listener_to_see_newer_log = new Button.OnClickListener() {
			public void onClick(View v)
			{
				log_page_index++;
				RefreshLogListView();
				return;
			}
		};
		
		button_listener_to_see_newest_log = new Button.OnClickListener() {
			public void onClick(View v)
			{
				log_page_index = GetLogPageCount();
				RefreshLogListView();
				return;
			}
		};
		
		log_listview_data = new ArrayList<HashMap<String, String>>();
		log_listview_data.clear();

		log_page_index = GetLogPageCount();
		
		log_listview_adapter = new SimpleAdapter(
				this,
				log_listview_data,
				R.layout.listview_item_log,
				new String[] {getString(R.string.log_listview_id_key), getString(R.string.log_listview_datetime_key), getString(R.string.log_listview_content_key)},
				new int[] {R.id.textview_id_log_listview_item_log_id, R.id.textview_id_log_listview_item_log_datetime, R.id.textview_id_log_listview_item_log_content}
		);
		return;
	}
	
	private void Bind()
	{
		((Button)findViewById(R.id.button_id_oldest_log)).setOnClickListener(button_listener_to_see_oldest_log);
		((Button)findViewById(R.id.button_id_older_log)).setOnClickListener(button_listener_to_see_older_log);
		((Button)findViewById(R.id.button_id_newer_log)).setOnClickListener(button_listener_to_see_newer_log);
		((Button)findViewById(R.id.button_id_newest_log)).setOnClickListener(button_listener_to_see_newest_log);
		((ListView)findViewById(R.id.listview_id_log_list)).setAdapter(log_listview_adapter);
		return;
	}
	
	private int GetLogLineCount()
	{
		LogInfoFactory log_info_factory = new LogInfoFactory(this);
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery("select count(id) from tb_log;",  null);
		if (c.moveToNext() )
		{
			return c.getInt(0);
		}
		return 0;
	}
	
	private int GetLogPageCount()
	{
		return (GetLogLineCount() + line_for_each_log_page - 1) / line_for_each_log_page;
	}
	
	private void RefreshLogPageIndex(int page_index, int max_page_count)
	{
		String text_to_show = "" + page_index + "/" + max_page_count;
		((TextView)findViewById(R.id.textview_id_page_number) ).setText(text_to_show);
		return;
	}
	
	private void RefreshLogListView()
	{
		Log.i("test", "enter RefreshLogListView");
		log_listview_data.clear();
		
		int log_line_count = GetLogLineCount();
		int log_page_count = GetLogPageCount();
		
		if (log_page_index < 1)
			log_page_index = 1;
		
		if (log_page_index > log_page_count)
			log_page_index = log_page_count;
		
		int min_log_id   = (log_page_index - 1) * line_for_each_log_page + 1;
		int max_log_id = min_log_id + (line_for_each_log_page - 1);
		if (min_log_id < 1)
			min_log_id = 1;

		RefreshLogPageIndex(log_page_index, log_page_count);
		
		LogInfoFactory log_info_factory = new LogInfoFactory(this);
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery("select id, date_time, content from tb_log where id >= " + min_log_id + " and id <= " + max_log_id + " order by id;",  null);
		while (c.moveToNext() )
		{
			log_listview_data.add(log_info_factory.Create(c.getInt(0), c.getString(1), c.getString(2) ) );
		}
		log_listview_adapter.notifyDataSetChanged();
		return;
	}
	
	// listeners for buttons
	private Button.OnClickListener button_listener_to_see_oldest_log;
	private Button.OnClickListener button_listener_to_see_older_log;
	private Button.OnClickListener button_listener_to_see_newer_log;
	private Button.OnClickListener button_listener_to_see_newest_log;

	// adapter for listview
	private SimpleAdapter log_listview_adapter;
	private int log_page_index;
	private int line_for_each_log_page = 20;
	
	// data for listview
	private ArrayList<HashMap<String, String>> log_listview_data;
}
