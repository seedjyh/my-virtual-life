package com.seedjyh.myvirtuallife;

import android.app.Application;

public class MyApp extends Application {
	private MyDatabaseHelper database_helper_;
	
	public MyDatabaseHelper getDatabaseHelper()
	{
		return database_helper_;
	}
	
	public void onCreate()
	{
		super.onCreate();

		database_helper_ = new MyDatabaseHelper(this, getString(R.string.database_name) );

		return;
	}
}
