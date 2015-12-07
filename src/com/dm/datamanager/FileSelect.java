package com.dm.datamanager;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.app.ProgressDialog;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

import com.dm.R;
import com.dm.helper.MySqlHelper;


public class FileSelect extends Activity {
	private List<Map<String, Object>> mData;
	private String mDir = "/sdcard";
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private ListView cst_file_list;

	
	private int mTotalProgress;
	private int mCurrentProgress;
	private BackupToExcel backuptoExcelSwap ;
	
	private String excelPath;
	private boolean goingFlag =true;
	
	 private ProgressDialog progressDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cst_file_listview);
		initVariable();
	
		mDir = "/sdcard";

		//setTitle("Open from dir");
		
		mySqlHelper = new MySqlHelper(FileSelect.this,
				"customer_inf.db", null, 1);
		db = mySqlHelper.getWritableDatabase();
		
		mData = getData();
		FileAdapter adapter = new FileAdapter(FileSelect.this);
		cst_file_list=  (ListView) findViewById(R.id.cst_file_list);
		cst_file_list.setAdapter(adapter);
		cst_file_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { // TODO Auto-generated method stub
				if(goingFlag){
					if ((Integer) mData.get(arg2).get("img") == R.drawable.ex_folder) {
						mDir = (String) mData.get(arg2).get("info");
						mData = getData();
						FileAdapter adapter = new FileAdapter(FileSelect.this);
						cst_file_list.setAdapter(adapter);
					} else {
						finishWithResult((String) mData.get(arg2).get("info"));
					}
				}
			
			}
		});
		
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		File f = new File(mDir);
		File[] files = f.listFiles();

		if (!mDir.equals("/sdcard")) {
			map = new HashMap<String, Object>();
			map.put("title", "Back to ../");
			map.put("info", f.getParent());
			map.put("img", R.drawable.ex_folder);
			list.add(map);
		}
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				map = new HashMap<String, Object>();
				map.put("title", files[i].getName());
				map.put("info", files[i].getPath());
				if (files[i].isDirectory())
					map.put("img", R.drawable.ex_folder);
				else if(getFileType(files[i].getName()).equals("xls")){
					map.put("img", R.drawable.cst_file_xls);
				}else{
					map.put("img", R.drawable.ex_doc);
				}
				list.add(map);
			}
		}
		return list;
	}

	

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView info;
	}

	public class FileAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public FileAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.cst_file_items, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setBackgroundResource((Integer) mData.get(position).get(
					"img"));
			holder.title.setText((String) mData.get(position).get("title"));
			holder.info.setText((String) mData.get(position).get("info"));
			return convertView;
		}
	}


/**
         * 获取文件后缀名
         * 
         * @param fileName
         * @return 文件后缀名
         */
	public  String getFileType(String fileName) {
                if (fileName != null) {
                        int typeIndex = fileName.lastIndexOf(".");
                        if (typeIndex != -1) {
                                String fileType = fileName.substring(typeIndex + 1)
                                                .toLowerCase();
                                return fileType;
                        }
                }
                return "";
        }
        
	private void finishWithResult(String path) {

		goingFlag = false;
		excelPath = path;

		backuptoExcelSwap = new BackupToExcel();

   
		initView();
		
		new Thread(new FileReadExcel()).start();		
		new Thread(new ProgressRunable()).start();
	}
	
	
	
	/*************progress***********/
	private void initVariable() {
		mTotalProgress = 100;
		mCurrentProgress = 0;
	}
	
	private void initView() {
		progressDialog = new ProgressDialog(FileSelect.this);
		progressDialog.setMax(100);
		progressDialog.setTitle("任务完成百分比");
		progressDialog.setMessage("耗时任务的完成百分比");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(false);
		progressDialog.show(); 
	}
	
	class ProgressRunable implements Runnable {

		@Override
		public void run() {
			while (mCurrentProgress < mTotalProgress) {
				mCurrentProgress =backuptoExcelSwap.BackuptoGetProgress();
				System.out.println("zhou22 "+mCurrentProgress);
				if(mCurrentProgress==-1){
					break;
				}
					
				progressDialog.setProgress(mCurrentProgress);
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
			Bundle conData = new Bundle();
			if(mCurrentProgress==-1){
				conData.putString("swappingdata", "空文件!");
			}else{
				
				conData.putString("swappingdata", "导入成功!");
			}
			Intent intent = new Intent();
			intent.putExtras(conData);
			setResult(RESULT_OK, intent);
			finish();
			
		}
		
	}
	
	class FileReadExcel implements Runnable {

		@Override
		public void run() {
			
			backuptoExcelSwap.BackuptoExcelRead(db,excelPath);
			
		}
		
	}
};

