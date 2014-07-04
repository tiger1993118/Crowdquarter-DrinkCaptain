package com.crowdquarter.drinkcaptain;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductListAdapter extends BaseAdapter {
	private Context context;
	private JSONArray jArray;

	public ProductListAdapter(Context context, JSONArray jArray) {
		this.context = context;
		this.jArray = jArray;

	}

	@Override
	public int getCount() {
		return jArray.length();
	}

	@Override
	public Object getItem(int position) {
		try {
			return jArray.getJSONObject(position).getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public long getItemId(int position) {
		try {
			return Integer.parseInt(jArray.getJSONObject(position).getString(
					"id"));
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View singleRow = layoutInflater.inflate(R.layout.product_listview,
				parent, false);

		TextView tvName = (TextView) singleRow.findViewById(R.id.tvName);
		try {
			tvName.setText(jArray.getJSONObject(position).getString("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return singleRow;
	}
}
