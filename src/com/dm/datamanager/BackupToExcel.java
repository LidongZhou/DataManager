package com.dm.datamanager;

import java.io.File;
import java.util.Calendar;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


public class BackupToExcel { 
	
	public static final int[] COLWIDTH_ARR = { 8, 12, 20,14,8,8,8,8,8,30};//��Ԫ����
	public static final String[] FIELD_ARR = { "ShipwnerName","ShipOwnerPhone", "ShipName", "ShipLength", "ShipWidth",
											"ShipHeigth","ShipHeigth","HaulHeight","More"};//Ҫд��exceld���ֶ�
	public static final String[] FIELD_ARR_TITLE = { "���", "����","�绰","����" ,"��","��","��","����","��ˮ","��ע"};//Ҫд��exceld���ֶ�
	
	public BackupToExcel(){
		

			
	}
	
	/*��ȡsd ��·��*/
	public String getSDPath(){ 
		File sdDir = null; 
		boolean sdCardExist = Environment.getExternalStorageState() 
		.equals(android.os.Environment.MEDIA_MOUNTED); //�ж�sd���Ƿ���� 
		if (sdCardExist) 
		{ 
		sdDir = Environment.getExternalStorageDirectory();//��ȡ��Ŀ¼ 
		} 
		String dir = sdDir.toString();
		return dir; 

		} 
	/*����sd�ļ�*/
	public static void makeDir(File dir) {
		if(! dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}
				
	public String getDate(){
		
		Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶��޸�
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
