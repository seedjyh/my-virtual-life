package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Init();
		Bind();
		RefreshPlayerAttributePropertyListView();
		RefreshPlayerAttributeStatusListView();
		RefreshPlayerAttributeSkillListView();
		RefreshPlayerAchievementListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int item_id = item.getItemId();
		Log.i("test", "item_id = " + item_id);

		if (R.id.action_enter_task_database == item_id)
		{
			Intent intent = new Intent(MainActivity.this, TaskDatabaseActivity.class);
			startActivityForResult(intent, intent_request_code_to_task_database_activity);
		}
		else if (R.id.action_enter_achievement_database == item_id)
		{
			Intent intent = new Intent(MainActivity.this, AchievementDatabaseActivity.class);
			startActivityForResult(intent, intent_request_code_to_achievement_database_activity);
		}
		else if (R.id.action_view_log == item_id)
		{
			Intent intent = new Intent(MainActivity.this, LogListActivity.class);
			startActivityForResult(intent, intent_request_code_to_log_list_activity);
		}
		else if (R.id.action_about == item_id)
		{
			AboutDialog about_dialog = new AboutDialog(MainActivity.this);

			about_dialog.setTitle(item.getTitle().toString() );
			about_dialog.show();
		}
		
		return true;

		// return super.onOptionsItemSelected(item);
	}
	


	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.leave_editor_alert)
                    .setNegativeButton(R.string.button_text_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                    })
                    .setPositiveButton(R.string.button_text_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
                            }
                    }).show();
            
            return true;
	    }else{                
	            return super.onKeyDown(keyCode, event);
	    }
	}
	
	private void Init()
	{
		player_attribute_property_listview_data = new ArrayList<HashMap<String, String>>();
		player_attribute_property_listview_data.clear();

		player_attribute_status_listview_data = new ArrayList<HashMap<String, String>>();
		player_attribute_status_listview_data.clear();

		player_attribute_skill_listview_data = new ArrayList<HashMap<String, String>>();
		player_attribute_skill_listview_data.clear();

		player_achievement_listview_data = new ArrayList<HashMap<String, String>>();
		player_achievement_listview_data.clear();
		
		player_attribute_property_listview_adapter = new SimpleAdapter(
				this,
				player_attribute_property_listview_data,
				R.layout.listview_item_player_attribute,
				new String[] {getString(R.string.player_attribute_listview_name_key), getString(R.string.player_attribute_listview_value_key)},
				new int[] {R.id.textview_id_player_attribute_listview_name, R.id.textview_id_player_attribute_listview_value}
		);
		
		player_attribute_status_listview_adapter = new SimpleAdapter(
				this,
				player_attribute_status_listview_data,
				R.layout.listview_item_player_attribute,
				new String[] {getString(R.string.player_attribute_listview_name_key), getString(R.string.player_attribute_listview_value_key)},
				new int[] {R.id.textview_id_player_attribute_listview_name, R.id.textview_id_player_attribute_listview_value}
		);
		
		player_attribute_skill_listview_adapter = new SimpleAdapter(
				this,
				player_attribute_skill_listview_data,
				R.layout.listview_item_player_attribute,
				new String[] {getString(R.string.player_attribute_listview_name_key), getString(R.string.player_attribute_listview_value_key)},
				new int[] {R.id.textview_id_player_attribute_listview_name, R.id.textview_id_player_attribute_listview_value}
		);
		
		player_achievement_listview_adapter = new SimpleAdapter(
				this,
				player_achievement_listview_data,
				R.layout.listview_item_achievement_lite,
				new String[] {getString(R.string.achievement_listview_title_key), getString(R.string.achievement_listview_status_key)},
				new int[] {R.id.textview_id_achievement_title, R.id.textview_id_achievement_status}
		);
	}
	
	private void Bind()
	{
		((ListView)findViewById(R.id.listview_id_player_attribute_property_listview)).setAdapter(player_attribute_property_listview_adapter);
		((ListView)findViewById(R.id.listview_id_player_attribute_status_listview)).setAdapter(player_attribute_status_listview_adapter);
		((ListView)findViewById(R.id.listview_id_player_attribute_skill_listview)).setAdapter(player_attribute_skill_listview_adapter);
		((ListView)findViewById(R.id.listview_id_achievement_listview)).setAdapter(player_achievement_listview_adapter);
	}
	
	private void RefreshPlayerAttributePropertyListView()
	{
		Log.i("test", "enter RefreshPlayerAttributePropertyListView");
		player_attribute_property_listview_data.clear();
		
		PlayerAttributeFactory player_attribute_factory = new PlayerAttributeFactory(this); 
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery("select id, name, value from tb_player_attribute_list where type = " + getString(R.string.player_attribute_type_id_property) + ";",  null);
		while (c.moveToNext() )
		{
			player_attribute_property_listview_data.add(player_attribute_factory.Create("" + c.getInt(0), c.getString(1), "" + c.getInt(2) ) );
		}
		player_attribute_property_listview_adapter.notifyDataSetChanged();
		return;
	}

	private void RefreshPlayerAttributeStatusListView()
	{
		Log.i("test", "enter RefreshPlayerAttributeStatusListView");
		player_attribute_status_listview_data.clear();
		
		PlayerAttributeFactory player_attribute_factory = new PlayerAttributeFactory(this); 
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery("select id, name, value from tb_player_attribute_list where type = " + getString(R.string.player_attribute_type_id_status) + ";",  null);
		while (c.moveToNext() )
		{
			player_attribute_status_listview_data.add(player_attribute_factory.Create("" + c.getInt(0), c.getString(1), "" + c.getInt(2) ) );
		}
		player_attribute_status_listview_adapter.notifyDataSetChanged();
		return;
	}

	private void RefreshPlayerAttributeSkillListView()
	{
		Log.i("test", "enter RefreshPlayerAttributeSkillListView");
		player_attribute_skill_listview_data.clear();
		
		PlayerAttributeFactory player_attribute_factory = new PlayerAttributeFactory(this); 
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery("select id, name, value from tb_player_attribute_list where type = " + getString(R.string.player_attribute_type_id_skill) + ";",  null);
		while (c.moveToNext() )
		{
			player_attribute_skill_listview_data.add(player_attribute_factory.Create("" + c.getInt(0), c.getString(1), "" + c.getInt(2) ) );
		}
		player_attribute_skill_listview_adapter.notifyDataSetChanged();
		return;
	}
	
	private void RefreshPlayerAchievementListView()
	{
		Log.i("test", "enter RefreshPlayerAchievementListView");
		player_achievement_listview_data.clear();
		
		AchievementInfoFactory info_factory = new AchievementInfoFactory(this);
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery("select tb_achievement_list.id, title, tb_achievement_list.description, name, value, request_attribute_value from tb_achievement_list, tb_player_attribute_list where tb_achievement_list.request_attribute_id = tb_player_attribute_list.id;",  null);
		while (c.moveToNext() )
		{
			Log.i("test", "find one achievement");
			player_achievement_listview_data.add(info_factory.Create(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getInt(5) ) );
		}
		player_achievement_listview_adapter.notifyDataSetChanged();
		return;
	}

	protected void onActivityResult(int request_code, int result_code, Intent intent)
	{
		RefreshPlayerAttributePropertyListView();
		RefreshPlayerAttributeStatusListView();
		RefreshPlayerAttributeSkillListView();
		RefreshPlayerAchievementListView();
		return;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// adapter for listview
	private SimpleAdapter player_attribute_property_listview_adapter;
	private SimpleAdapter player_attribute_status_listview_adapter;
	private SimpleAdapter player_attribute_skill_listview_adapter;
	private SimpleAdapter player_achievement_listview_adapter;
	
	// data for listview
	private ArrayList<HashMap<String, String>> player_attribute_property_listview_data;
	private ArrayList<HashMap<String, String>> player_attribute_status_listview_data;
	private ArrayList<HashMap<String, String>> player_attribute_skill_listview_data;
	private ArrayList<HashMap<String, String>> player_achievement_listview_data;

	// request code
	private int intent_request_code_to_acceptable_task_list_activity = 1;
	private int intent_request_code_to_accepted_task_list_activity   = 2;
	private int intent_request_code_to_task_database_activity        = 3;
	private int intent_request_code_to_achievement_database_activity = 4;
	private int intent_request_code_to_log_list_activity             = 5;
}
