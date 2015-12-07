package com.dm.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatebaseManage {

	
	public DatebaseManage(){
		
		
	}
	
	public Cursor BaseSearch(SQLiteDatabase db, String query){
		
		Cursor cursor = db.rawQuery(
				"select * from customer where shipname like '%"
						+ query + "%'"+" or "+" shipownerphone like '%"+query+"%'"
						+" or "+" shipownername like '%"+query+"%'", null);
		return cursor;
	}
	
	public int BaseDelete(SQLiteDatabase db, String string){
		
		String whereClause =  "shipownerphone=?";  
		String[] whereArgs = new String[] {string};  
		db.delete("customer", whereClause, whereArgs); 
		System.out.println("zhou  BaseDelete");
		return 0;
	}
	
	public int BaseModify(SQLiteDatabase db, String string,ContentValues values){
		
		
		String whereClause =  "shipownerphone=?";  
		String[] whereArgs = new String[] {string}; 
		db.update("customer", values, whereClause, whereArgs);
		System.out.println("zhou  BaseModify");
		return 0;
	}
	

}
