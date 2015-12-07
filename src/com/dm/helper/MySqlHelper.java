package com.dm.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqlHelper extends SQLiteOpenHelper {

	public MySqlHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table customer(_id integer primary key autoincrement,"
				+"shipownername varchar(10),"
				+"shipownerphone varchar(10),"
				+"shipname varchar(10),"
				+"shiplength varchar(10),"
				+"shipwidth varchar(10),"
				+"shipheigth varchar(10),"
				+"shipweight varchar(10),"
				+"haulheight varchar(10),"
				+"more varchar(10))");

		System.out.println("onCreate 被调用");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// db.execSQL("drop table if exists student");
		System.out.println("onUpgrade 被调用" + oldVersion + "--" + newVersion);

	}
	
	

}
