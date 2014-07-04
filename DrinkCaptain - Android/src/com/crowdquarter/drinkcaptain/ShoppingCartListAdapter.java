package com.crowdquarter.drinkcaptain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShoppingCartListAdapter extends BaseAdapter {
	private Context context;
	private List<JSONObject> productObjects;
	private SharedPreferences settings;

	public ShoppingCartListAdapter(Context context,
			List<JSONObject> productObjects) {
		this.context = context;
		this.productObjects = productObjects;

		settings = this.context.getSharedPreferences(MainMenuActivity.PRER,
				ShoppingCartActivity.MODE_PRIVATE);

	}

	@Override
	public int getCount() {
		return productObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return productObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		productObjects.remove(position);
		notifyDataSetChanged();

		Set<String> setShoppingCartString = new HashSet<String>();
		for (JSONObject jsonObj : productObjects) {
			setShoppingCartString.add(jsonObj.toString());
		}

		settings.edit()
				.putStringSet(MainMenuActivity.PRER_SHOPPING_CART,
						setShoppingCartString).commit();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View singleRow = layoutInflater.inflate(
				R.layout.shopping_cart_listview, parent, false);

		TextView tvName = (TextView) singleRow.findViewById(R.id.tvName);
		TextView tvVolume = (TextView) singleRow.findViewById(R.id.tvVolume);
		TextView tvQuantity = (TextView) singleRow.findViewById(R.id.tvQuanty);
		TextView tvPrice = (TextView) singleRow.findViewById(R.id.tvPrice);

		JSONObject productObject = productObjects.get(position);
		try {

			tvName.setText(productObject.getString("name"));
			tvVolume.setText(productObject.getString("volume"));
			tvQuantity.setText(productObject.getString("quantity"));
			tvPrice.setText("$" + productObject.getString("price"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return singleRow;
	}
}
