package com.crowdquarter.drinkcaptain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ShoppingCartActivity extends Activity {

	private SharedPreferences settings;

	private Set<String> setShoppingCartString;

	private List<JSONObject> listShoppingCart = new ArrayList<JSONObject>();

	ShoppingCartListAdapter shoppingCartAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);

		settings = getSharedPreferences(MainMenuActivity.PRER, MODE_PRIVATE);

		setShoppingCartString = settings.getStringSet(
				MainMenuActivity.PRER_SHOPPING_CART, null);

		try {
			for (String s : setShoppingCartString) {
				listShoppingCart.add(new JSONObject(s));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ListView lvShoppingCart = (ListView) findViewById(R.id.listView);

		shoppingCartAdapter = new ShoppingCartListAdapter(this,
				listShoppingCart);

<<<<<<< HEAD
		lvShoppingCart.setAdapter(shoppingCartAdapter);

=======
>>>>>>> FETCH_HEAD
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
