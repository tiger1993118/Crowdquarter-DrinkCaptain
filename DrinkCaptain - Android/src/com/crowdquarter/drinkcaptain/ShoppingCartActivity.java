package com.crowdquarter.drinkcaptain;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crowdquarter.backend.ShoppingCart;

public class ShoppingCartActivity extends Activity {

	private TextView tvTax, tvTotal;

	private ShoppingCart shoppingCart;

	private SharedPreferences settings;

	ShoppingCartListAdapter shoppingCartAdapter;

	public final static String PRER_SHOPPING_CART = "preference shopping cart";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);

		setUpShoppingCart();

		LinearLayout llBackground = (LinearLayout) findViewById(R.id.llBackground);
		llBackground.setBackgroundResource(TileFragment.background);

		settings = getSharedPreferences(MainMenuActivity.PRER, MODE_PRIVATE);
		shoppingCart = new ShoppingCart(settings.getString(PRER_SHOPPING_CART,
				null));

		tvTax = (TextView) findViewById(R.id.tvTax);
		tvTotal = (TextView) findViewById(R.id.tvTotal);

		tvTax.setText(shoppingCart.getStringTotalTax());
		tvTotal.setText(shoppingCart.getStringTotalPrice());
	}

	private void setUpShoppingCart() {
		ListView lvShoppingCart = (ListView) findViewById(R.id.listView);

		shoppingCartAdapter = new ShoppingCartListAdapter(this);

		lvShoppingCart.setAdapter(shoppingCartAdapter);

		SwipeDismissTouchListener touchListener = new SwipeDismissTouchListener(
				lvShoppingCart,
				new SwipeDismissTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							shoppingCartAdapter.remove(position);
							shoppingCart = new ShoppingCart(settings.getString(
									PRER_SHOPPING_CART, null));
							tvTax.setText(shoppingCart.getStringTotalTax());
							tvTotal.setText(shoppingCart.getStringTotalPrice());
						}
						shoppingCartAdapter.notifyDataSetChanged();
					}
				});
		lvShoppingCart.setOnTouchListener(touchListener);

	}

	public void checkout(View view) {
		Intent iMap = new Intent(getApplicationContext(), MapActivity.class);
		startActivity(iMap);
	}
}
