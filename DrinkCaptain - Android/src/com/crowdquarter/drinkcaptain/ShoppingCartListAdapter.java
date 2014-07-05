package com.crowdquarter.drinkcaptain;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crowdquarter.backend.Product;
import com.crowdquarter.backend.ShoppingCart;

public class ShoppingCartListAdapter extends BaseAdapter {
	private Context context;

	private SharedPreferences settings;

	private ShoppingCart shoppingCart;

	public ShoppingCartListAdapter(Context context) {
		this.context = context;
		settings = this.context.getSharedPreferences(MainMenuActivity.PRER,
				ShoppingCartActivity.MODE_PRIVATE);
		shoppingCart = new ShoppingCart(settings.getString(
				ShoppingCartActivity.PRER_SHOPPING_CART, null));

	}

	@Override
	public int getCount() {
		return shoppingCart.size();
	}

	@Override
	public Product getItem(int position) {
		return shoppingCart.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		shoppingCart.deleteProduct(position);
		notifyDataSetChanged();
		settings.edit()
				.putString(ShoppingCartActivity.PRER_SHOPPING_CART,
						shoppingCart.toString()).commit();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View singleRow = layoutInflater.inflate(
				R.layout.shopping_cart_listview, parent, false);

		Product product = shoppingCart.get(position);

		TextView tvName = (TextView) singleRow.findViewById(R.id.tvName);
		TextView tvVolume = (TextView) singleRow.findViewById(R.id.tvVolume);
		TextView tvQuantity = (TextView) singleRow.findViewById(R.id.tvQuanty);
		TextView tvPrice = (TextView) singleRow.findViewById(R.id.tvPrice);

		tvName.setText(product.getName());
		tvVolume.setText(product.getVolume());
		tvPrice.setText("$" + product.getPrice());
		tvQuantity.setText(shoppingCart.getStringQuantity(product));

		return singleRow;
	}
}
