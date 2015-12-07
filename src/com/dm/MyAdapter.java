package com.dm;

import java.util.List;

import com.dm.pojo.Customer;
import com.dm.R;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private List<Customer> arrays = null;
	private Context mContext;

	public MyAdapter(Context mContext, List<Customer> arrays) {
		this.mContext = mContext;
		this.arrays = arrays;
	}
	
	public void setList(List<Customer> arrays){
		
		this.arrays = arrays;
	}

	@Override
	public int getCount() {
		return this.arrays.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.cst_items, null);
		
			viewHolder.listShipOwnerName = (TextView) view.findViewById(R.id.list_item_shipownername);
			viewHolder.listShipPhone = (TextView) view.findViewById(R.id.list_item_phone);
			viewHolder.listShipOther1 = (TextView) view.findViewById(R.id.list_item_other1);
			viewHolder.listShipOther2 = (TextView) view.findViewById(R.id.list_item_other2);
			viewHolder.btnDelLayout = (LinearLayout) view.findViewById(R.id.del_layout);
			viewHolder.itemsLayout = (LinearLayout) view.findViewById(R.id.items_layout);
			viewHolder.btnDel = (Button) view.findViewById(R.id.btndel);
			viewHolder.btnCancel = (Button) view.findViewById(R.id.btncancel);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		

	
		viewHolder.listShipOwnerName.setText(arrays.get(position).getShipownerName()
				.toString());
		viewHolder.listShipPhone.setText(arrays.get(position).getShipOwnerPhone()
				.toString());

		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		SpannableStringBuilder builder = new SpannableStringBuilder(
				arrays.get(position).getShipName().toString() + " "
						+ arrays.get(position).getShipLength().toString()
						+ "x"
						+ arrays.get(position).getShipWidth().toString()
						+ "x"
						+arrays.get(position).getShipHeigth().toString());
		builder.setSpan(redSpan, 0, arrays.get(position).getShipName()
				.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		viewHolder.listShipOther1.setText(builder);
		viewHolder.listShipOther2.setText(
				 "载重："
				+ arrays.get(position).geShipWeight().toString()
				+ " 吊水："
				+ arrays.get(position).getHaulHeight().toString());
		// 为删除按钮添加监听事件，实现点击删除按钮时删除该项
	
		return view;
	}

	final static class ViewHolder {
		TextView listShipOwnerName;
		TextView listShipPhone;
		TextView listShipOther1;
		TextView listShipOther2;
		Button btnDel;
		Button btnCancel;
		LinearLayout itemsLayout;
		LinearLayout btnDelLayout;
	}
}