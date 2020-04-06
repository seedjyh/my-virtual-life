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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class AchievementDatabaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievement_database);
		Init();
		Bind();
		RefreshListView();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.achievement_database_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int item_id = item.getItemId();
		Log.i("test", "item_id = " + item_id);

		if (R.id.action_add_achievement == item_id)
		{
			Intent intent = new Intent(AchievementDatabaseActivity.this, AchievementEditorActivity.class);
			int new_id = 1;
			SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getWritableDatabase();
			Cursor c = db.rawQuery("select max(id) from tb_achievement_list;",  null);
			if (c.moveToNext() )
			{
				Log.i("test", "c.getCount() = " + c.getCount() );
				Log.i("test", "c.getColumnCount() = " + c.getColumnCount() );
				Log.i("test", "c.getType(0) = " + c.getType(0) );
				new_id = c.getInt(0) + 1;
			}
			intent.putExtra(getString(R.string.achievement_listview_id_key), new_id);
			
			
			startActivityForResult(intent, intent_request_code_to_achievement_editor_activity);
		}
		return true;

		// return super.onOptionsItemSelected(item);
	}
	
	private void Init()
	{
		achievement_listview_data = new ArrayList<HashMap<String, String>>();
		achievement_listview_data.clear();
		
		achievement_listview_adapter = new SimpleAdapter(
				this,
				achievement_listview_data,
				R.layout.listview_item_achievement,
				new String[] {getString(R.string.achievement_listview_id_key), getString(R.string.achievement_listview_title_key), getString(R.string.achievement_listview_description_key), getString(R.string.achievement_listview_status_key)},
				new int[] {R.id.textview_id_achievement_id, R.id.textview_id_achievement_title, R.id.textview_id_achievement_description, R.id.textview_id_achievement_status}
		);
		return;
	}
	
	private void Bind()
	{
		((ListView)findViewById(R.id.listview_id_achievement_database)).setAdapter(achievement_listview_adapter);
		return;
	}
	
	private void RefreshListView()
	{
		Log.i("test", "enter RefreshTaskListView");
		achievement_listview_data.clear();
		
		AchievementInfoFactory info_factory = new AchievementInfoFactory(this);
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery("select tb_achievement_list.id, title, tb_achievement_list.description, name, value, request_attribute_value from tb_achievement_list, tb_player_attribute_list where tb_achievement_list.request_attribute_id = tb_player_attribute_list.id;",  null);
		while (c.moveToNext() )
		{
			achievement_listview_data.add(info_factory.Create(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getInt(5) ) );
		}
		achievement_listview_adapter.notifyDataSetChanged();
		return;
	}

	protected void onActivityResult(int request_code, int result_code, Intent intent)
	{
		if (intent_request_code_to_achievement_editor_activity == request_code)
		{
			if (1 == result_code)
			{
				RefreshListView();
			}
		}
	}

	// adapter for listview
	private SimpleAdapter achievement_listview_adapter;
	
	// data for listview
	private ArrayList<HashMap<String, String>> achievement_listview_data;
	
	// request code
	private int intent_request_code_to_achievement_editor_activity = 1;
}
