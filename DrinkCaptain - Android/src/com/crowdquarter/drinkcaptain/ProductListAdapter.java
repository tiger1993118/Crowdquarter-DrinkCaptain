package com.crowdquarter.drinkcaptain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			return jArray.get(position);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateArray(JSONArray newJArray) {
		this.jArray = newJArray;
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
		TextView tvPrice = (TextView) singleRow.findViewById(R.id.tvPrice);

		try {
			JSONObject productObject = jArray.getJSONObject(position);

			tvName.setText(productObject.getString("name"));
			tvPrice.setText("$" + productObject.getString("price"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return singleRow;
	}

}
