package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView; 
import android.widget.Spinner;


public class AchievementEditorActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievement_editor);
		
		Init();
		Bind();
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
				SaveNewAchievementInfoToDatabase();
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
		
		attribute_spinner_on_item_selected_listener = new Spinner.OnItemSelectedListener() {
            @Override  
            public void onItemSelected(AdapterView<?> arg0, View arg1,  
                    int arg2, long arg3) {
            	selected_attribute_id = Integer.parseInt(attribute_spinner_data_id.get(arg2).toString() );
                arg0.setVisibility(View.VISIBLE);
                return;
            }
  
            @Override  
            public void onNothingSelected(AdapterView<?> arg0) {  
                // TODO Auto-generated method stub
            	return;
            }  
		};

		attribute_spinner_data_id = new ArrayList<String>();
		attribute_spinner_data_id.clear();

		attribute_spinner_data_name = new ArrayList<String>();
		attribute_spinner_data_name.clear();

		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getReadableDatabase();
		Cursor c = db.rawQuery("select id, name from tb_player_attribute_list", null);
		while (c.moveToNext() )
		{
			attribute_spinner_data_id.add("" + c.getInt(0) );
			attribute_spinner_data_name.add(c.getString(1) );
		}

		attribute_spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, attribute_spinner_data_name);
		attribute_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		achievement_id = getIntent().getIntExtra(getString(R.string.achievement_listview_id_key), -1);
	}
	
	private void Bind()
	{
		Log.i("test", "Bind");
		((Button)findViewById(R.id.button_id_achievement_editor_ok)).setOnClickListener(ok_button_click_listener);
		((Button)findViewById(R.id.button_id_achievement_editor_cancel)).setOnClickListener(cancel_button_click_listener);
		((TextView)findViewById(R.id.text_view_id_achievement_id) ).setText("" + achievement_id);
		((Spinner)findViewById(R.id.achievement_target_attribute_name_spinner) ).setAdapter(attribute_spinner_adapter);
		((Spinner)findViewById(R.id.achievement_target_attribute_name_spinner) ).setOnItemSelectedListener(attribute_spinner_on_item_selected_listener);
		return;
	}
	
	private void SaveNewAchievementInfoToDatabase()
	{
		SQLiteDatabase db = ((MyApp)getApplication() ).getDatabaseHelper().getWritableDatabase();
		db.execSQL(
			"insert into tb_achievement_list(title, description, request_attribute_id, request_attribute_value) values(\"" +
			((EditText)findViewById(R.id.edit_text_id_achievement_title) ).getText().toString() +
			"\", \"" +
			((EditText)findViewById(R.id.edit_text_id_achievement_description) ).getText().toString() +
			"\", " + selected_attribute_id + ", " +
			((EditText)findViewById(R.id.edit_text_id_achievement_target_attribute_value) ).getText().toString() +
			");"
		);

		String full_title = ((MyApp)getApplication() ).getDatabaseHelper().getAchievementFullTitle(achievement_id);
		((MyApp)getApplication() ).getDatabaseHelper().WriteLog("创建了成就[" + achievement_id + "]：" + full_title + "。");
		return;
	}

	//==========================================================================================
	// listener
	private Button.OnClickListener ok_button_click_listener;
	private Button.OnClickListener cancel_button_click_listener;
	private Spinner.OnItemSelectedListener attribute_spinner_on_item_selected_listener;
	
	// adapter
	private ArrayAdapter attribute_spinner_adapter;

	// data
	private ArrayList<String> attribute_spinner_data_id;
	private ArrayList<String> attribute_spinner_data_name;

	private int achievement_id;
	private int selected_attribute_id;
}
