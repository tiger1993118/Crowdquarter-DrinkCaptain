package com.crowdquarter.drinkcaptain;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.TextView;

import com.crowdquarter.backend.ShoppingCart;

public class CheckoutActivity extends Activity {

	private ShoppingCart shoppingCart;

	private SharedPreferences settings;

	private List<String> listInfo = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);

		settings = getSharedPreferences(MainMenuActivity.PRER, MODE_PRIVATE);

		shoppingCart = new ShoppingCart(settings.getString(
				ShoppingCartActivity.PRER_SHOPPING_CART, null));

		ListView lvShoppingCart = (ListView) findViewById(R.id.lvProducts);

		lvShoppingCart = (ListView) findViewById(R.id.lvProducts);

		lvShoppingCart.setAdapter(new CheckoutCartAdapter(
				getApplicationContext(), shoppingCart));

		listInfo.add(getResources().getString(R.string.sAddress));
		listInfo.add(getResources().getString(R.string.sPayment));
		listInfo.add(getResources().getString(R.string.sBilling));

		CheckoutExpandableAdapter checkoutExpandableAdapter = new CheckoutExpandableAdapter(
				getApplicationContext(), listInfo);

		ExpandableListView expandableLvInfo = (ExpandableListView) findViewById(R.id.expandableListView1);

		expandableLvInfo.setAdapter(checkoutExpandableAdapter);

		Gallery gallery = (Gallery) findViewById(R.id.gallery);

		gallery.setSpacing(1);

		gallery.setAdapter(new GalleryAdapter(getApplicationContext()));

		TextView tvTax = (TextView) findViewById(R.id.tvTax);
		TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
		TextView tvShipping = (TextView) findViewById(R.id.tvShipping);

		tvTax.setText(shoppingCart.getStringTotalTax());
		tvTotal.setText(shoppingCart.getStringTotalPrice());

	}
}
