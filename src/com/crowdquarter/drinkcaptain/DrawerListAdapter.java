package com.crowdquarter.drinkcaptain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrawerListAdapter extends BaseAdapter {
	private Context context;

	private String[] arrayDrawer;

	public DrawerListAdapter(Context context, String[] arrayDrawer) {
		this.context = context;
		this.arrayDrawer = arrayDrawer;

	}

	@Override
	public int getCount() {
		return arrayDrawer.length;
	}

	@Override
	public Object getItem(int position) {
		return arrayDrawer[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View singleRow = layoutInflater.inflate(R.layout.main_menu_drawer_row,
				parent, false);

		TextView tvName = (TextView) singleRow.findViewById(R.id.tvName);
		tvName.setText(arrayDrawer[position]);

		return singleRow;
	}
}
