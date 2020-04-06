package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper
{
	MyDatabaseHelper(Context context, String db_name)
	{
		super(context, db_name, null, 1);
	}
	
	public void onCreate(SQLiteDatabase db)
	{
		Log.i("test", "enter MyDatabaseHelper::onCreate");
		CreateTables(db);
		InitializeTables(db);
		return;
	}
	
	public void onUpgrade(SQLiteDatabase db, int old_version, int new_version)
	{
		return;
	}
	
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
	}
	
	public TaskInfo getTaskInfo(Activity activity, int task_id)
	{
		// basic info
		SQLiteDatabase db = super.getReadableDatabase();
		Cursor c = db.rawQuery("select title, subtitle, description from tb_task_list where id = " + task_id + ";", null);
		if (!c.moveToNext() )
		{
			assert(false);
		}

		String title = c.getString(0);
		String subtitle = c.getString(1);
		String description = c.getString(2);

		// effected attribute info
		ArrayList<HashMap<String, String> > attribute_listview_data = new ArrayList<HashMap<String, String> >();

		attribute_listview_data.clear();
		PlayerAttributeFactory player_attribute_factory = new PlayerAttributeFactory(activity);
		c = db.rawQuery("select id, name, effect_value from tb_player_attribute_list, tb_task_effect_list where tb_task_effect_list.task_id = " + task_id + " and tb_player_attribute_list.id = tb_task_effect_list.player_attribute_id and effect_value != 0;", null);
		while (c.moveToNext() )
		{
			attribute_listview_data.add(player_attribute_factory.Create("" + c.getInt(0), c.getString(1), "" + c.getInt(2) ) );
		}
		return new TaskInfo(task_id, title, subtitle, description, attribute_listview_data);
	}
	
	public String getTaskAcceptDateTime(Activity activity, int task_id)
	{
		SQLiteDatabase db = super.getReadableDatabase();
		Cursor c = db.rawQuery("select accept_date_time from tb_accepted_task_list where task_id = " + task_id + ";", null);
		if (!c.moveToNext() )
		{
			assert(false);
		}
		return c.getString(0);
	}
	
	private void CreateTables(SQLiteDatabase db)
	{
		String sql = new String();

		// tb_task_list
		sql = "create table tb_task_list (id integer PRIMARY KEY autoincrement, title varchar(32), subtitle varchar(32), description varchar(256) );";
		Log.i("Create database table", sql);
		db.execSQL(sql);

		// tb_player_attribute_list
		sql = "create table tb_player_attribute_list (id integer PRIMARY KEY autoincrement, type int, name varchar(32), description varchar(128), value int);";
		Log.i("Create database table", sql);
		db.execSQL(sql);

		// tb_task_effect_list
		sql = "create table tb_task_effect_list (task_id int, player_attribute_id int, effect_value int);";
		Log.i("Create database table", sql);
		db.execSQL(sql);
		
		// tb_accepted_task_list
		sql = "create table tb_accepted_task_list (task_id int, accept_date_time datetime);";
		Log.i("Create database table", sql);
		db.execSQL(sql);
		
		// tb_log
		sql = "create table tb_log (id integer PRIMARY KEY autoincrement, date_time , content);";
		Log.i("Create database table", sql);
		db.execSQL(sql);

		// tb_achievement_list
		sql = "create table tb_achievement_list (id integer PRIMARY KEY autoincrement, title varchar(32), description varchar(128), request_attribute_id int, request_attribute_value int);";
		Log.i("Create database table", sql);
		db.execSQL(sql);
		return;
	}
	
	private void InitializeTables(SQLiteDatabase db)
	{
		String sql = new String();

		// tb_player_attribute_list
		sql = "delete from tb_player_attribute_list;";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"力量\", \"\", 110)";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"敏捷\", \"\", 110);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"耐力\", \"\", 110);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"毅力\", \"\", 100);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"智力\", \"\", 100);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"洞察力\", \"\", 120);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"容姿\", \"\", 70);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"气质\", \"\", 70);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"健康\", \"\", 110);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (0, \"感知\", \"\", 80);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (1, \"身体疲劳度\", \"\", 0);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (1, \"精神疲劳度\", \"\", 0);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (1, \"清洁度\", \"\", 0);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (1, \"饥饿度\", \"\", 0);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (1, \"精神压力\", \"\", 150);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (1, \"魅力\", \"\", 70);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (1, \"金钱\", \"\", 0);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (2, \"英语\", \"\", 1900);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (2, \"足球\", \"\", 0);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (2, \"厨艺\", \"\", 0);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (2, \"AI\", \"\", 0);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (2, \"Android开发\", \"\", 0);";
		Log.i("Initialize database table", sql);

		sql = "insert into tb_player_attribute_list (type, name, description, value) values (2, \"C++\", \"\", 0);";
		Log.i("Initialize database table", sql);
		db.execSQL(sql);

		return;
	}
	
	public void WriteLog(String content)
	{
		SQLiteDatabase db = super.getWritableDatabase();
		db.execSQL("insert into tb_log(date_time, content) values (datetime(\"now\",\"localtime\"), \"" + content + "\");");
		return;
	}
	
	public String getTaskFullTitle(int task_id)
	{
		SQLiteDatabase db = super.getWritableDatabase();
		Cursor c = db.rawQuery("select title, subtitle from tb_task_list where id = " + task_id, null);
		if (!c.moveToNext() )
		{
			assert(false);
		}
		
		String title = c.getString(0);
		String subtitle = c.getString(1);
		return title + "（" + subtitle + "）";
	}
	
	public String getAchievementFullTitle(int achievement_id)
	{
		SQLiteDatabase db = super.getWritableDatabase();
		Cursor c = db.rawQuery("select title, description from tb_achievement_list where id = " + achievement_id, null);
		if (!c.moveToNext() )
		{
			assert(false);
		}
		
		String title = c.getString(0);
		String subtitle = c.getString(1);
		return title + "（" + subtitle + "）";
	}
	
	public boolean IsAcceptedTask(int task_id)
	{
		SQLiteDatabase db = super.getReadableDatabase();
		Cursor c = db.rawQuery("select task_id from tb_accepted_task_list where task_id = " + task_id + ";", null);
		
		return c.moveToNext();
	}
	
	public void SaveTaskAttributeEffect(int task_id, int attribute_id, int effect_value)
	{
		SQLiteDatabase db = super.getWritableDatabase();
		if (db.rawQuery("select * from tb_task_effect_list where task_id = " + task_id + " and player_attribute_id = " + attribute_id + ";", null).moveToNext() )
		{
			db.execSQL("update tb_task_effect_list set effect_value = " + effect_value + " where task_id = " + task_id + " and player_attribute_id = " + attribute_id + ";");
		}
		else
		{
			db.execSQL("insert into tb_task_effect_list(task_id, player_attribute_id, effect_value) values (" + task_id + ", " + attribute_id + ", " + effect_value + ");");
		}
		return;
	}
}
