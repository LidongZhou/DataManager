package com.dm.datamanager;

import java.io.File;
import java.util.Calendar;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


public class BackupToExcel { 
	
	public static final int[] COLWIDTH_ARR = { 8, 12, 20,14,8,8,8,8,8,30};//单元格宽度
	public static final String[] FIELD_ARR = { "ShipwnerName","ShipOwnerPhone", "ShipName", "ShipLength", "ShipWidth",
											"ShipHeigth","ShipHeigth","HaulHeight","More"};//要写入exceld的字段
	public static final String[] FIELD_ARR_TITLE = { "序号", "船主","电话","船名" ,"长","宽","高","载重","吊水","备注"};//要写入exceld的字段
	
	public BackupToExcel(){
		

			
	}
	
	/*获取sd 卡路径*/
	public String getSDPath(){ 
		File sdDir = null; 
		boolean sdCardExist = Environment.getExternalStorageState() 
		.equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在 
		if (sdCardExist) 
		{ 
		sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
		} 
		String dir = sdDir.toString();
		return dir; 

		} 
	/*创建sd文件*/
	public static void makeDir(File dir) {
		if(! dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}
				
	public String getDate(){
		
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE); 

		return year+""+(month+1)/10+""+((month+1)%10)+""+((date/10))+""+(date%10)+"";
	}

	public <T> void BackuptoExcelWrite(SQLiteDatabase database, String filedir){
		File file = new File(getSDPath()+"/"+filedir);
		makeDir(file);
		ExcleJXLUtil.initExcel(file.toString()+"/"+getDate()+"_backup.xls", FIELD_ARR_TITLE, COLWIDTH_ARR);
		
		ExcleJXLUtil.writeToExcel(database, file.toString()+"/"+getDate()+"_backup.xls", FIELD_ARR);
	}
	
	public <T> void BackuptoExcelRead(SQLiteDatabase database, String filedir){
		File file = new File(filedir);
			
		ExcleJXLUtil.readToExcel(database, filedir, FIELD_ARR);
	}
			
	public int 	 BackuptoGetProgress(){
		
		return  ExcleJXLUtil.getProgress();
	}  
	

}
