package com.dm;


import com.dm.helper.MySqlHelper;
import com.dm.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCustomerActivity extends Activity {

	private EditText ship_name;
	private EditText ship_length;
	private EditText ship_width;
	private EditText ship_heigth;
	private EditText ship_weight;
	private EditText haul_height;
	private EditText ship_owner_name;
	private EditText ship_owner_phone;
	private EditText more;

	private Button add_submit;
	private Button add_cancel;

	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cst_add_layout);

		mySqlHelper = new MySqlHelper(AddCustomerActivity.this,
				"customer_inf.db", null, 1);
		db = mySqlHelper.getWritableDatabase();
	
		ship_name = (EditText) findViewById(R.id.ship_name);
		ship_length = (EditText) findViewById(R.id.ship_length);
		ship_width = (EditText) findViewById(R.id.ship_width);
		ship_heigth = (EditText) findViewById(R.id.ship_heigth);
		ship_weight = (EditText) findViewById(R.id.ship_weight);
		haul_height = (EditText) findViewById(R.id.haul_height);
		ship_owner_name = (EditText) findViewById(R.id.ship_owner_name);
		ship_owner_phone = (EditText) findViewById(R.id.ship_owner_phone);
		more = (EditText) findViewById(R.id.more);
		
		add_submit = (Button) findViewById(R.id.add_submit);
		add_cancel = (Button) findViewById(R.id.add_cancel);
		
		add_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addCustomerInf();
			}
		});
		add_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}


	public void addCustomerInf() {

		if (ship_owner_name.getText().toString().equals("") 
				|| ship_owner_phone.getText().toString().equals("") ) {
			Toast.makeText(AddCustomerActivity.this, "您输入的信息不完整！",
					Toast.LENGTH_SHORT).show();
		} else {
			
			ContentValues values = new ContentValues();
			values.put("shipownername", ship_owner_name.getText().toString());
			values.put("shipownerphone", ship_owner_phone.getText().toString());
			values.put("shipname", ship_name.getText().toString());
			values.put("shiplength", ship_length.getText().toString());
			values.put("shipwidth", ship_width.getText().toString());
			values.put("shipheigth", ship_heigth.getText().toString());
			values.put("shipweight", ship_weight.getText().toString());
			values.put("haulheight", haul_height.getText().toString());
			values.put("more", more.getText().toString());
				
			db.insert("customer", null, values);
			Toast.makeText(AddCustomerActivity.this, "添加成功！", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(AddCustomerActivity.this,
					MainActivty.class);
			startActivity(intent);
			finish();
		}
	}

	
		
	
}
