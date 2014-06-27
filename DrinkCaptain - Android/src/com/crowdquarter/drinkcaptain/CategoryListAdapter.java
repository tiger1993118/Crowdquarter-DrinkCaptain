package com.crowdquarter.drinkcaptain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {
	private Context context;

	private String[] arrayCategoryDrink = { "Beer", "Wine", "Rum", "Scotch" };

	public CategoryListAdapter(Context context) {
		this.context = context;

	}

	@Override
	public int getCount() {
		return arrayCategoryDrink.length;
	}

	@Override
	public Object getItem(int position) {
		return arrayCategoryDrink[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View singleRow = layoutInflater.inflate(R.layout.product_listview,
				parent, false);

		TextView tvName = (TextView) singleRow.findViewById(R.id.tvName);
		tvName.setText(arrayCategoryDrink[position]);

		return singleRow;
	}
}
