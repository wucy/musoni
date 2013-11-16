package com.musoni.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Storage {
	
	private static Storage _instance;
	
	private SQLiteDatabase db;
	
	public static final String DB_NAME = "musoni.db";
	
	public static synchronized Storage getInstance() {
		if (_instance == null)
			_instance = new Storage();
		return _instance;
	}
		
	private Storage() {
	}
	
	public void setDB(SQLiteDatabase db) {
		this.db = db;
	}
	
	public Cursor getTaskQueue(String userName) {
		return db.rawQuery("SELECT * FROM taks WHERE completed = false", new String[]{});
	}
	
}
