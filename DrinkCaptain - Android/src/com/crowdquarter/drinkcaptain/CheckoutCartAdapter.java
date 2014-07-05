package com.crowdquarter.drinkcaptain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crowdquarter.backend.Product;
import com.crowdquarter.backend.ShoppingCart;

public class CheckoutCartAdapter extends BaseAdapter {
	private Context context;
	private ShoppingCart shoppingCart;

	public CheckoutCartAdapter(Context context, ShoppingCart shoppingCart) {
		this.context = context;
		this.shoppingCart = shoppingCart;

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View singleRow = layoutInflater.inflate(R.layout.checkout_listview,
				parent, false);

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
