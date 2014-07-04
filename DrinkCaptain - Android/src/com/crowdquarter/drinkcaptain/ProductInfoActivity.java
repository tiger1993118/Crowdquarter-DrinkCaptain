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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProductInfoActivity extends Activity {
	int quantity = 1;

	double total, price;

	Button bQuantity;

	TextView tvTotal;

	private Set<String> setShoppingCartString;

	private List<JSONObject> listShoppingCart = new ArrayList<JSONObject>();

	private JSONObject jsonProduct;

	private SharedPreferences settings;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_info);

		tvTotal = (TextView) findViewById(R.id.tvTotal);
		bQuantity = (Button) findViewById(R.id.ibQuantity);

		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvPrice = (TextView) findViewById(R.id.tvPrice);
		TextView tvVolume = (TextView) findViewById(R.id.tvVolume);

		try {
			jsonProduct = new JSONObject(getIntent().getStringExtra(
					ProductListActivity.INTENT_PRODUCT));
			// Set Up Infos
			tvName.setText(jsonProduct.getString("name"));
			tvPrice.setText(jsonProduct.getString("price"));
			tvVolume.setText(jsonProduct.getString("volume"));
			tvTotal.setText(jsonProduct.getString("price"));

			// Set Up Shopping Cart
			settings = getSharedPreferences(MainMenuActivity.PRER, MODE_PRIVATE);
			setShoppingCartString = settings.getStringSet(
					MainMenuActivity.PRER_SHOPPING_CART, null);

			for (String s : setShoppingCartString) {
				JSONObject j = new JSONObject(s);
				listShoppingCart.add(j);
			}

			total = price = Double.parseDouble(jsonProduct.getString("price"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void add(View view) {

		total += price;

		total = (double) Math.round(total * 100) / 100;

		quantity += 1;

		bQuantity.setText(quantity + "");

		tvTotal.setText(total + "");

	}

	public void minus(View view) {
		if (quantity > 1) {
			total -= price;

			total = (double) Math.round(total * 100) / 100;

			quantity -= 1;

			bQuantity.setText(quantity + "");

			tvTotal.setText(total + "");
		}
	}

	public void addToCart(View view) {

		try {

			boolean inCart = false;

			for (int i = 0; i < listShoppingCart.size(); i++) {
				if ((listShoppingCart.get(i).getString("name"))
						.equals(jsonProduct.get("name"))) {

					inCart = true;

					listShoppingCart.get(i).put(
							"quantity",
							listShoppingCart.get(i).getInt("quantity")
									+ quantity);

					i = listShoppingCart.size();

				}
			}
			if (!inCart)
				listShoppingCart.add(jsonProduct.put("quantity", quantity));

			setShoppingCartString.clear();

			for (JSONObject jsonObj : listShoppingCart) {
				setShoppingCartString.add(jsonObj.toString());
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		settings.edit()
				.putStringSet(MainMenuActivity.PRER_SHOPPING_CART,
						setShoppingCartString).commit();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.my_action_bar, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.shoppingCart) {

			Intent iShoppingCart = new Intent(getApplicationContext(),
					ShoppingCartActivity.class);

			startActivity(iShoppingCart);
		}
		return super.onOptionsItemSelected(item);
	}

}
