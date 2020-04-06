package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

public class TaskEditorActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor);
		
		Init();
		Bind();
		InitAttributeListViewData();
		RefreshAttributeListView();
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
		ok_button_click_listener = new Button.OnClickListener(){
			public void onClick(View v)
			{
				SaveNewTaskInfoToDatabase();
				setResult(1, new Intent() );
				finish();
			}
		};

		cancel_button_click_listener = new Button.OnClickListener(){
			public void onClick(View v)
			{
				setResult(0, new Intent() );
				finish();
			}
		};
		
		attribute_listview_long_click_listener = new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				HashMap<String, String> one_item_data = attribute_listview_data.get(arg2);

				SetAttributeValueDialog set_dialog = new SetAttributeValueDialog(
					TaskEditorActivity.this,
					Integer.parseInt(one_item_data.get(getString(R.string.player_attribute_listview_value_key) ) ),
					new ListViewDataSetter(attribute_listview_data, attribute_listview_adapter, arg2, getString(R.string.player_attribute_listview_value_key) )
				);

				set_dialog.setTitle(one_item_data.get(getString(R.string.player_attribute_listview_name_key) ) );
				set_dialog.show();

	            return false; // 表示继续执行OnCreateContextMenuListener
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

		task_id = getIntent().getIntExtra(getString(R.string.task_listview_id_key), -1);
	}
	
	private void Bind()
	{
		Log.i("test", "Bind");
		((Button)findViewById(R.id.button_id_task_editor_ok)).setOnClickListener(ok_button_click_listener);
		((Button)findViewById(R.id.button_id_task_editor_cancel)).setOnClickListener(cancel_button_click_listener);
		((TextView)findViewById(R.id.text_view_id_task_id) ).setText("" + task_id);
		((ListView)findViewById(R.id.listview_id_task_attribute_effect)).setAdapter(attribute_listview_adapter);
		((ListView)findViewById(R.id.listview_id_task_attribute_effect)).setOnItemLongClickListener(attribute_listview_long_click_listener);
		return;
	}
	
	private void InitAttributeListViewData()
	{
		attribute_listview_data.clear();
		PlayerAttributeFactory player_attribute_factory = new PlayerAttributeFactory(this); 
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery("select id, name, 0 from tb_player_attribute_list;",  null);
		while (c.moveToNext() )
		{
			attribute_listview_data.add(player_attribute_factory.Create("" + c.getInt(0), c.getString(1), "" + c.getInt(2) ) );
		}

		c = db.rawQuery("select player_attribute_id, effect_value from tb_task_effect_list where task_id = " + task_id + ";", null);
		while (c.moveToNext() )
		{
			int attribute_id = c.getInt(0);
			int attribute_effect_value = c.getInt(1);
			attribute_listview_data.get(attribute_id).put(getString(R.string.player_attribute_listview_value_key), "" + attribute_effect_value);
		}
		return;
	}
	
	private void RefreshAttributeListView()
	{
		attribute_listview_adapter.notifyDataSetChanged();
		return;
	}
	
	private void SaveNewTaskInfoToDatabase()
	{
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getWritableDatabase();
		db.execSQL(
			"insert into tb_task_list(title, subtitle, description) values(\"" +
			((EditText)findViewById(R.id.edit_text_id_task_title) ).getText().toString() +
			"\", \"" +
			((EditText)findViewById(R.id.edit_text_id_task_subtitle) ).getText().toString() +
			"\", \"" +
			((EditText)findViewById(R.id.edit_text_id_task_description) ).getText().toString() +
			"\");"
		);

		for (Iterator ite = attribute_listview_data.iterator(); ite.hasNext();)
		{
			HashMap<String, String> one_item_data = (HashMap<String, String>) ite.next();
			int attribute_id    = Integer.parseInt(one_item_data.get(getString(R.string.player_attribute_listview_id_key) ) );
			int attribute_value = Integer.parseInt(one_item_data.get(getString(R.string.player_attribute_listview_value_key) ) );
			if (attribute_value != 0)
			{
				((MyApp)getApplication() ).getDatabaseHelper().SaveTaskAttributeEffect(task_id, attribute_id, attribute_value);
			}
		}

		String task_full_title = ((MyApp)getApplication() ).getDatabaseHelper().getTaskFullTitle(task_id);
		((MyApp)getApplication() ).getDatabaseHelper().WriteLog("创建了任务[" + task_id + "]：" + task_full_title + "。");
		return;
	}

	//==========================================================================================
	// listener
	private Button.OnClickListener ok_button_click_listener;
	private Button.OnClickListener cancel_button_click_listener;
	private AdapterView.OnItemLongClickListener attribute_listview_long_click_listener;
	
	// adapter
	private SimpleAdapter attribute_listview_adapter;

	// data for listview
	private ArrayList<HashMap<String, String>> attribute_listview_data;

	private int task_id;
}
