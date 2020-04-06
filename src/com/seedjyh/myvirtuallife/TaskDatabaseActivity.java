package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class TaskDatabaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_database);
		Init();
		Bind();
		RefreshTaskListView();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_database_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int item_id = item.getItemId();
		Log.i("test", "item_id = " + item_id);

		if (R.id.action_add_task == item_id)
		{
			Intent intent = new Intent(TaskDatabaseActivity.this, TaskEditorActivity.class);
			int new_id = 1;
			SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getWritableDatabase();
			Cursor c = db.rawQuery("select max(id) from tb_task_list;",  null);
			if (c.moveToNext() )
			{
				Log.i("test", "c.getCount() = " + c.getCount() );
				Log.i("test", "c.getColumnCount() = " + c.getColumnCount() );
				Log.i("test", "c.getType(0) = " + c.getType(0) );
				new_id = c.getInt(0) + 1;
			}
			intent.putExtra(getString(R.string.task_listview_id_key), new_id);
			
			
			startActivityForResult(intent, enter_task_editor_activity_request_code);
		}
		return true;

		// return super.onOptionsItemSelected(item);
	}
	
	private void Init()
	{
		checkbox_listener = new CompoundButton.OnCheckedChangeListener()
		{ 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) {
            	RefreshTaskListView();
            }; 
		};
		task_listview_data = new ArrayList<HashMap<String, String>>();
		task_listview_data.clear();
		
		task_listview_adapter = new SimpleAdapter(
				this,
				task_listview_data,
				R.layout.listview_item_task,
				new String[] {getString(R.string.task_listview_id_key), getString(R.string.task_listview_title_key), getString(R.string.task_listview_subtitle_key)},
				new int[] {R.id.textview_id_task_listview_item_task_id, R.id.textview_id_task_listview_item_task_title, R.id.textview_id_task_listview_item_task_subtitle}
		);

		task_listview_item_click_listener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				HashMap<String, String> one_item_data = (HashMap<String, String>)task_listview_adapter.getItem(arg2);
				int target_task_id = Integer.parseInt(one_item_data.get(getString(R.string.task_listview_id_key) ) );

				if (((MyApp)getApplication() ).getDatabaseHelper().IsAcceptedTask(target_task_id) ) // acceptable task
				{
					Intent intent = new Intent(TaskDatabaseActivity.this, TaskViewerToAccomplishOrAbortActivity.class);
					intent.putExtra(getString(R.string.task_listview_id_key), target_task_id);
					startActivityForResult(intent, enter_task_viewer_to_accomplish_or_abort_activity_request_code);
				}
				else
				{
					Intent intent = new Intent(TaskDatabaseActivity.this, TaskViewerToAcceptActivity.class);
					intent.putExtra(getString(R.string.task_listview_id_key), target_task_id);
					startActivityForResult(intent, enter_task_viewer_to_accept_activity_request_code);
				}

				return;
			}
		};
		
		return;
	}
	
	private void Bind()
	{
		((CheckBox)findViewById(R.id.checkbox_id_show_acceptable_task) ).setOnCheckedChangeListener(checkbox_listener);
		((CheckBox)findViewById(R.id.checkbox_id_show_accepted_task) ).setOnCheckedChangeListener(checkbox_listener);
		((ListView)findViewById(R.id.listview_id_task_database)).setAdapter(task_listview_adapter);
		((ListView)findViewById(R.id.listview_id_task_database)).setOnItemClickListener(task_listview_item_click_listener);
		return;
	}
	
	private void RefreshTaskListView()
	{
		Log.i("test", "enter RefreshTaskListView");
		task_listview_data.clear();

    	boolean show_acceptable_task = ((CheckBox)findViewById(R.id.checkbox_id_show_acceptable_task) ).isChecked();
    	boolean show_accepted_task   = ((CheckBox)findViewById(R.id.checkbox_id_show_accepted_task) ).isChecked();
    	
    	String sql = CreateSQLForSelectTask(show_acceptable_task, show_accepted_task);
    	
		TaskInfoFactory task_info_factory = new TaskInfoFactory(this);
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery(sql,  null);
		while (c.moveToNext() )
		{
			task_listview_data.add(task_info_factory.Create(c.getInt(0), c.getString(1), c.getString(2) ) );
		}
		task_listview_adapter.notifyDataSetChanged();
		return;
	}

	protected void onActivityResult(int request_code, int result_code, Intent intent)
	{
		if (enter_task_editor_activity_request_code == request_code)
		{
			if (1 == result_code)
			{
				RefreshTaskListView();
			}
		}
		else if (enter_task_viewer_to_accept_activity_request_code == request_code)
		{
			if (1 == result_code)
			{
				Log.i("intent", "intent.getExtras().size() = " + intent.getExtras().size() );
				int task_id = intent.getExtras().getInt(getString(R.string.task_listview_id_key) );

				SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getWritableDatabase();
				db.execSQL("insert into tb_accepted_task_list(task_id, accept_date_time) values (" + task_id + ", datetime(\"'now\",\"localtime\") );");
				
				String task_full_title = ((MyApp)getApplication() ).getDatabaseHelper().getTaskFullTitle(task_id);
				((MyApp)getApplication() ).getDatabaseHelper().WriteLog("接受了任务[" + task_id + "]：" + task_full_title + "。");
				RefreshTaskListView();
			}
		}
		else  if (enter_task_viewer_to_accomplish_or_abort_activity_request_code == request_code)
		{
			if (1 == result_code)
			{
				Log.i("intent", "intent.getExtras().size() = " + intent.getExtras().size() );
				int task_id = intent.getExtras().getInt(getString(R.string.task_listview_id_key) );

				SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getWritableDatabase();
				db.execSQL("delete from tb_accepted_task_list where task_id = " + task_id + ";");

				String task_full_title = ((MyApp)getApplication() ).getDatabaseHelper().getTaskFullTitle(task_id);
				((MyApp)getApplication() ).getDatabaseHelper().WriteLog("任务[" + task_id + "]：" + task_full_title + "完成！");

				RefreshTaskListView();
				
				db.execSQL("update tb_player_attribute_list set value = value + ifnull((select effect_value from  tb_task_effect_list where id = player_attribute_id and task_id = " + task_id + "), 0);");
			}
			else if (2 == result_code)
			{
				Log.i("intent", "intent.getExtras().size() = " + intent.getExtras().size() );
				int task_id = intent.getExtras().getInt(getString(R.string.task_listview_id_key) );

				SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getWritableDatabase();
				db.execSQL("delete from tb_accepted_task_list where task_id = " + task_id + ";");

				String task_full_title = ((MyApp)getApplication() ).getDatabaseHelper().getTaskFullTitle(task_id);
				((MyApp)getApplication() ).getDatabaseHelper().WriteLog("放弃了任务[" + task_id + "]：" + task_full_title + "……");
				RefreshTaskListView();
			}
		}
	}
	
	private String CreateSQLForSelectTask(boolean show_acceptable_task, boolean show_accepted_task)
	{
		if (show_acceptable_task && show_accepted_task)
		{
			return "select id, title, subtitle from tb_task_list;";
		}
		else if (!show_acceptable_task && !show_accepted_task)
		{
			return "select * from tb_task_list where 1 = 0;";
		}
		else if (show_acceptable_task)
		{
			return "select id, title, subtitle from tb_task_list where id not in (select task_id from tb_accepted_task_list);";
		}
		else // show_accepted_task
		{
			return "select id, title, subtitle from tb_task_list where id in (select task_id from tb_accepted_task_list);";
		}
	}
	
	// listener
	private CompoundButton.OnCheckedChangeListener checkbox_listener;
	private OnItemClickListener task_listview_item_click_listener;

	// adapter for listview
	private SimpleAdapter task_listview_adapter;
	
	// data for listview
	private ArrayList<HashMap<String, String>> task_listview_data;
	
	// request code
	private int enter_task_editor_activity_request_code = 1;
	private int enter_task_viewer_to_accept_activity_request_code = 2;
	private int enter_task_viewer_to_accomplish_or_abort_activity_request_code = 3;
}
