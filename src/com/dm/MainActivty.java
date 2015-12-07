package com.dm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dm.MyAdapter.ViewHolder;
import com.dm.datamanager.BackupToExcel;
import com.dm.datamanager.FileSelect;
import com.dm.helper.DatebaseManage;
import com.dm.helper.MySqlHelper;
import com.dm.pojo.Customer;
import com.dm.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivty extends Activity {

	private LinearLayout showallinfolist;
	private ImageView refresh;
	private ImageView search;
	private ImageView add;
	private ImageView set;
	private ListView cst_listview;
	private List<Customer> customers = new ArrayList<Customer>();
	private List<LinearLayout> delbuttons = new ArrayList<LinearLayout>();
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private static final int REQUEST_EX = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		setContentView(R.layout.activity_main);

		mySqlHelper = new MySqlHelper(MainActivty.this, "customer_inf.db",
				null, 1);
		db = mySqlHelper.getWritableDatabase();

		layout_init();
		System.out.println("zhou onCreate");
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Because this activity has set launchMode="singleTop", the system
		// calls this method
		// to deliver the intent if this activity is currently the foreground
		// activity when
		// invoked again (when the user executes a search from this activity, we
		// don't create
		// a new instance of this activity, so the system delivers the search
		// intent here)
		System.out.println("zhou onNewIntent");
		handleIntent(intent);
	}

	@SuppressLint("NewApi")
	private void layout_init() {

		showallinfolist = (LinearLayout) findViewById(R.id.showallinfo);
		showallinfolist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("zhou layout_init");
				removeOtherButton(null);
				showinfolist();
			}
		});
		
		refresh = (ImageView) findViewById(R.id.refresh);
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				removeOtherButton(null);
				showinfolist();
			}
		});


		search = (ImageView) findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onSearchRequested();
			}
		});

		add = (ImageView) findViewById(R.id.add);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivty.this,
						AddCustomerActivity.class);
				startActivity(intent);
			}
		});

		set = (ImageView) findViewById(R.id.set);
		set.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {

				PopupMenu pop = new PopupMenu(MainActivty.this, arg0);		
			
					
				pop.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						switch (item.getItemId()) {
						case R.id.data_backup:
							BackupToExcel backuptoExcel = new BackupToExcel();
							boolean sdCardExist = Environment.getExternalStorageState() 
									.equals(android.os.Environment.MEDIA_MOUNTED);
							if(fileIsExists()){
								backuptoExcel.BackuptoExcelWrite(db, "NoteBook");
								Toast.makeText(MainActivty.this, "备份路径sdcard/NoteBook", Toast.LENGTH_SHORT).show();
							}else{
								
								Toast.makeText(MainActivty.this, "未找到SDcard！", Toast.LENGTH_SHORT).show();
							}	
							break;
						case R.id.data_import:
							if(fileIsExists()){
								db.delete("customer", null, null);
								Intent intent = new Intent(MainActivty.this,
										FileSelect.class);
								startActivityForResult(intent, REQUEST_EX);
							}else{
								Toast.makeText(MainActivty.this, "未找到SDcard！", Toast.LENGTH_SHORT).show();
							}
							break;
						case R.id.data_clean:
							db.delete("customer", null, null);
							showinfolist();
							break;

						}
						return false;
					}
				});
				
				try {
				    Field[] fields = pop.getClass().getDeclaredFields();
				    for (Field field : fields) {
				        if ("mPopup".equals(field.getName())) {
				            field.setAccessible(true);
				            Object menuPopupHelper = field.get(pop);
				            Class<?> classPopupHelper = Class.forName(menuPopupHelper
				                    .getClass().getName());
				            Method setForceIcons = classPopupHelper.getMethod(
				                    "setForceShowIcon", boolean.class);
				            setForceIcons.invoke(menuPopupHelper, true);
				            break;
				        }
				    }
				} catch (Exception e) {
				    e.printStackTrace();
				}

				pop.getMenuInflater().inflate(R.menu.contextmenu, pop.getMenu());
				pop.show();
			}
		});

		cst_listview = (ListView) findViewById(R.id.cst_listview2);
		cst_listview.setAdapter(stu_adapter);

		cst_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { // TODO Auto-generated method stub
				
				removeOtherButton(arg1);
				showInformation(arg2);
				System.out.println(arg2);
			}
		});

		cst_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final ViewHolder holder = (ViewHolder) arg1.getTag();
				final int position = arg2;
				 TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,   
				            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,   
				            0.0f, Animation.RELATIVE_TO_SELF, 0.0f);   
				    mShowAction.setDuration(500); 

		        holder.btnDelLayout.startAnimation(mShowAction);  
				holder.btnDelLayout.setVisibility(View.VISIBLE);
				System.out.println("zhou setOnLongClickListener");
				delbuttons.add(holder.btnDelLayout);
				removeOtherButton(arg1);
				holder.btnDel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
																	
						 TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,   
						            Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,   
						            0.0f, Animation.RELATIVE_TO_SELF, 0.0f);   
						    mShowAction.setDuration(500); 

				        holder.btnDelLayout.startAnimation(mShowAction);
						holder.btnDelLayout.setVisibility(View.GONE);
						(new DatebaseManage()).BaseDelete(db, customers.get(position).getShipOwnerPhone().toString());						
						customers.remove(position);
						stu_adapter.setList(customers);
						stu_adapter.notifyDataSetChanged();
					}
				});
				
				holder.btnCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						 TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,   
						            Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,   
						            0.0f, Animation.RELATIVE_TO_SELF, 0.0f);   
						    mShowAction.setDuration(500); 

				        holder.btnDelLayout.startAnimation(mShowAction);
						holder.btnDelLayout.setVisibility(View.GONE);
					}
				});
				
				return true;
			}

		});

	}

	private void removeOtherButton(View view) {

		if (view != null) {
			final ViewHolder holder = (ViewHolder) view.getTag();
			Iterator<LinearLayout> iter = delbuttons.iterator();
			while (iter.hasNext()) {
				LinearLayout s = iter.next();
				if (s.equals(holder.btnDelLayout)) {

				} else {
					s.setVisibility(View.GONE);
					iter.remove();
				}

			}
		} else {
			Iterator<LinearLayout> iter = delbuttons.iterator();
			while (iter.hasNext()) {
				LinearLayout s = iter.next();

				s.setVisibility(View.GONE);
				iter.remove();

			}
		}

	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// handles a click on a search suggestion; launches activity to show
			// word
			/*
			 * Intent wordIntent = new Intent(this, MainActivty.class);
			 * wordIntent.setData(intent.getData()); startActivity(wordIntent);
			 */
		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			String query = intent.getStringExtra(SearchManager.QUERY);
			System.out.println("zhou "
					+ intent.getStringExtra(SearchManager.QUERY));
			customers = new ArrayList<Customer>();
			Cursor cursor = (new DatebaseManage()).BaseSearch(db, query);
			if (cursor.getCount() == 0) {
				Toast.makeText(MainActivty.this, "未找到！", Toast.LENGTH_SHORT)
						.show();
			}

			while (cursor.moveToNext()) {
				customers.add(new Customer(cursor.getInt(1),cursor.getString(1), cursor
						.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5), cursor
								.getString(6), cursor.getString(7), cursor
								.getString(8), cursor.getString(9)));

			}
			stu_adapter.setList(customers);
			stu_adapter.notifyDataSetChanged();
		} else {

			showinfolist();
		}
	}

	@Override
	protected void onResume() {
		/*
		 * // TODO Auto-generated method stub customers = new
		 * ArrayList<Customer>(); Cursor cursor =
		 * db.rawQuery("select * from customer", null); while
		 * (cursor.moveToNext()) { customers.add(new
		 * Customer(cursor.getString(1))); } //
		 * stu_listview.setAdapter(stu_adapter);
		 * stu_adapter.notifyDataSetChanged();
		 * System.out.println("zhou resume");
		 */
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		/* super.onActivityResult(requestCode, resultCode, data); */

		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_EX) {
				Bundle bl = data.getExtras();
				String title = bl.getString("swappingdata");
				Toast.makeText(MainActivty.this, title, Toast.LENGTH_SHORT)
						.show();
				showinfolist();
			}
		}

	}

	MyAdapter stu_adapter = new MyAdapter(MainActivty.this, customers);

	
	public boolean fileIsExists(){
		boolean sdCardExist = Environment.getExternalStorageState() 
				.equals(android.os.Environment.MEDIA_MOUNTED);
		
			return   sdCardExist; 
	}

	public void showinfolist() {

		customers = new ArrayList<Customer>();
		Cursor cursor = db.rawQuery("select * from customer ORDER  by shipownername  COLLATE LOCALIZED", null);
		System.out.println("zhou showinfolist " + cursor.getCount());
		while (cursor.moveToNext()) {
			customers.add(new Customer(cursor.getInt(1),cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor
							.getString(4), cursor.getString(5), cursor
							.getString(6), cursor.getString(7), cursor
							.getString(8), cursor.getString(9)));
		}
		// stu_listview.setAdapter(stu_adapter);
		stu_adapter.setList(customers);
		stu_adapter.notifyDataSetChanged();

	}

	
	public void showInformation(final int temp) {
		
		Builder builder = new Builder(MainActivty.this);
		LayoutInflater inflater = LayoutInflater.from(MainActivty.this);
		View view = inflater.inflate(R.layout.cst_showinfo_layout, null);
		
		
		final int id = customers.get(temp).getTableId();
		final AlertDialog dialog = builder.setTitle("        详细信息").
				setIcon(R.drawable.cst_showinfo_icon).
				setView(view)
				.create();

		final EditText ship_name_info = (EditText) view
				.findViewById(R.id.ship_name_info);
		final EditText ship_length = (EditText) view
				.findViewById(R.id.ship_length_info);
		final EditText ship_width = (EditText) view
				.findViewById(R.id.ship_width_info);
		final EditText ship_heigth = (EditText) view
				.findViewById(R.id.ship_heigth_info);
		final EditText ship_weight = (EditText) view
				.findViewById(R.id.ship_weight_info);
		final EditText haul_height = (EditText) view
				.findViewById(R.id.haul_height_info);
		final EditText ship_owner_name = (EditText) view
				.findViewById(R.id.ship_owner_name_info);
		final EditText ship_owner_phone = (EditText) view
				.findViewById(R.id.ship_owner_phone_info);
		final EditText more_info = (EditText) view.findViewById(R.id.more_info);
		
		Button confirm =(Button) view.findViewById(R.id.showinfo_confirm);
		Button modify =(Button) view.findViewById(R.id.showinfo_modify);
		Button cancel =(Button) view.findViewById(R.id.showinfo_cancel);
		ImageView call =(ImageView) view.findViewById(R.id.showinfo_call);
		ImageView message =(ImageView) view.findViewById(R.id.showinfo_message);

		ship_name_info.setText(customers.get(temp).getShipName().toString());
		ship_length.setText(customers.get(temp).getShipLength().toString());
		ship_width.setText(customers.get(temp).getShipWidth().toString());
		ship_heigth.setText(customers.get(temp).getShipHeigth().toString());
		ship_weight.setText(customers.get(temp).geShipWeight().toString());
		haul_height.setText(customers.get(temp).getHaulHeight().toString());
		ship_owner_name.setText(customers.get(temp).getShipownerName()
				.toString());
		ship_owner_phone.setText(customers.get(temp).getShipOwnerPhone()
				.toString());
		more_info.setText(customers.get(temp).getMore().toString());

		System.out.println("zhou end" + ship_name_info + "   " + more_info);
		dialog.show();
		
		modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ship_name_info.setEnabled(true);
				ship_length.setEnabled(true);
				ship_width.setEnabled(true);
				ship_heigth.setEnabled(true);
				ship_weight.setEnabled(true);
				haul_height.setEnabled(true);
				ship_owner_name.setEnabled(true);
				ship_owner_phone.setEnabled(true);
				more_info.setEnabled(true);
			}
		});
		
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContentValues values = new ContentValues();
			    values.put("shipownername", ship_owner_name.getText().toString());
				values.put("shipownerphone", ship_owner_phone.getText().toString());
				values.put("shipname", ship_name_info.getText().toString());
				values.put("shiplength", ship_length.getText().toString());
				values.put("shipwidth", ship_width.getText().toString());
				values.put("shipheigth", ship_heigth.getText().toString());
				values.put("shipweight", ship_weight.getText().toString());
				values.put("haulheight", haul_height.getText().toString());
				values.put("more", more_info.getText().toString());
		
			
				new DatebaseManage().BaseModify(db, customers.get(temp).getShipOwnerPhone().toString(), values);
				showinfolist();
				dialog.dismiss();
				
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		
		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ ship_owner_phone.getText().toString()));
				startActivity(intent);
			}
		});
		
		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" 
						+ ship_owner_phone.getText().toString()));
				startActivity(intent);
			}
		});
		
		
	}
	
	

}