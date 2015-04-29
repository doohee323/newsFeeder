package com.tz.newsfeeder.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	SQLiteDatabase db;
	
	private static final String DATABASE_NAME = "datafile.db";
	public static final String TABLE_NAME = "newsfeed";

	public static final String SQL_CREATE_TABLE_NAME = "CREATE TABLE " + TABLE_NAME
			+ " (" 
			+ " _id INTEGER PRIMARY KEY AUTOINCREMENT, "
      + " last_updated TEXT, "
      + " published_date TEXT, "
			+ " indx TEXT NOT NULL, " 
      + " title TEXT, "
      + " url TEXT, "
      + " multimedia TEXT, "
      + " abstract TEXT, "
      + " per_facet TEXT, "
      + " org_facet TEXT "
			+ ") ";
	public static final String SQL_DELETE_TABLE_NAME = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public static final int DATABASE_VERSION = 1;

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_NAME);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_TABLE_NAME);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}