package com.crowdquarter.drinkcaptain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class ShoppingCartActivity extends Activity {

	public final static String PRER_SHOPPING_CART = "preference shopping cart";
	public final static String PRER = "preference";

	private Set<String> setShoppingCartString;

	private List<JSONObject> listShoppingCart = new ArrayList<JSONObject>();

	private ListView lvShoppingCart;

	private SharedPreferences settings;

	ShoppingCartListAdapter shoppingCartAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);

		settings = getSharedPreferences(PRER, MODE_PRIVATE);

		setShoppingCartString = settings.getStringSet(PRER_SHOPPING_CART, null);

		try {
			for (String s : setShoppingCartString) {
				listShoppingCart.add(new JSONObject(s));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		lvShoppingCart = (ListView) findViewById(R.id.listView);
		shoppingCartAdapter = new ShoppingCartListAdapter(this,
				listShoppingCart);
		lvShoppingCart.setAdapter(shoppingCartAdapter);

		// lvShoppingCart
		// .setOnItemLongClickListener(new DeleteShoppingCartListener());

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

	public class DeleteShoppingCartListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {

			listShoppingCart.remove(position);
			shoppingCartAdapter.notifyDataSetChanged();
			lvShoppingCart.invalidate();

			for (JSONObject jsonObj : listShoppingCart) {
				setShoppingCartString.add(jsonObj.toString());
			}

			settings.edit()
					.putStringSet(ShoppingCartActivity.PRER_SHOPPING_CART,
							setShoppingCartString).commit();

			return true;
		}
	}
}