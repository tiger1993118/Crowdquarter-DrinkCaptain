package com.crowdquarter.drinkcaptain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ShoppingCartActivity extends Activity {

	ShoppingCartListAdapter shoppingCartAdapter;

	public final static String PRER_SHOPPING_CART = "preference shopping cart";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);

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
						}
						shoppingCartAdapter.notifyDataSetChanged();
					}
				});
		lvShoppingCart.setOnTouchListener(touchListener);
	}

	public void checkout(View view) {
		Intent iCheckout = new Intent(getApplicationContext(),
				CheckoutActivity.class);
		startActivity(iCheckout);
	}
}
