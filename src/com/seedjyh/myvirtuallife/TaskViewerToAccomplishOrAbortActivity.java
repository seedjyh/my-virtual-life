package com.seedjyh.myvirtuallife;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class TaskViewerToAccomplishOrAbortActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_viewer_to_accomplish_or_abort);
		
		Init();
		Bind();
	}

	private void Init()
	{
		task_id = getIntent().getIntExtra(getString(R.string.task_listview_id_key), -1);
		Log.i("test", "task_id = " + task_id);

		accomplish_button_click_listener = new Button.OnClickListener(){
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.putExtra(getString(R.string.task_listview_id_key), task_id);
				setResult(1, intent);
				
				finish();
			}
		};
		
		abort_button_click_listener = new Button.OnClickListener(){
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.putExtra(getString(R.string.task_listview_id_key), task_id);
				setResult(2, intent);
				
				finish();
			}
		};
		
		attribute_listview_data = new ArrayList<HashMap<String, String>>();
		attribute_listview_data.clear();
		
		attribute_listview_adapter = new SimpleAdapter(
				this,
				attribute_listview_data,
				R.layout.listview_item_player_attribute,
				new String[] {getString(R.string.player_attribute_listview_name_key), getString(R.string.player_attribute_listview_value_key)},
				new int[] {R.id.textview_id_player_attribute_listview_name, R.id.textview_id_player_attribute_listview_value}
		);
		
	}
	
	private void Bind()
	{
		Log.i("test", "Bind");
		((Button)findViewById(R.id.button_id_accomplish)).setOnClickListener(accomplish_button_click_listener);
		((Button)findViewById(R.id.button_id_abort)).setOnClickListener(abort_button_click_listener);
		((TextView)findViewById(R.id.text_view_id_task_id) ).setText("" + task_id);
		
		TaskInfo task_info = ((MyApp)getApplication() ).getDatabaseHelper().getTaskInfo(this, task_id);
		((TextView)findViewById(R.id.textview_id_task_title) ).setText(task_info.title() );
		((TextView)findViewById(R.id.textview_id_task_subtitle) ).setText(task_info.subtitle() );
		((TextView)findViewById(R.id.textview_id_task_description) ).setText(task_info.description() );
		((TextView)findViewById(R.id.textview_id_task_accept_datetime) ).setText(((MyApp)getApplication() ).getDatabaseHelper().getTaskAcceptDateTime(this, task_id) );
		((ListView)findViewById(R.id.listview_id_task_attribute_effect)).setAdapter(attribute_listview_adapter);

		for (Iterator ite = task_info.attribute_listview_data().iterator(); ite.hasNext();)
		{
			attribute_listview_data.add((HashMap<String, String>) ((HashMap<String, String>)ite.next()).clone() );
		}
		attribute_listview_data = (ArrayList<HashMap<String, String>>)task_info.attribute_listview_data().clone();

		attribute_listview_adapter.notifyDataSetChanged();
		return;
	}
	//==========================================================================================
	// listener
	private Button.OnClickListener abort_button_click_listener;
	private Button.OnClickListener accomplish_button_click_listener;
	
	// adapter
	SimpleAdapter attribute_listview_adapter;

	// data for listview
	ArrayList<HashMap<String, String>> attribute_listview_data;

	private int task_id;
}

