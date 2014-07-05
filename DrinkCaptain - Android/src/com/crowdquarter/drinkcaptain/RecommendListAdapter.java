package com.crowdquarter.drinkcaptain;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crowdquarter.backend.Product;

public class RecommendListAdapter extends BaseAdapter {
	private Context context;
	private List<Product> products;

	public RecommendListAdapter(Context context, List<Product> products) {
		this.context = context;
		this.products = products;
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public Product getItem(int position) {
		return products.get(position);
	}

	public void updateProducts(List<Product> products) {
		this.products = products;
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

		Product product = products.get(position);
		tvName.setText(product.getName());
		tvPrice.setText("$" + product.getPrice());

		return singleRow;
	}

}
