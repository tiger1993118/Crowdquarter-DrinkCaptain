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
	int q;
	double total, price;

	Button bq;
	TextView tvName, tvPrice, tvVolume, tvTotal;

	private Set<String> setShoppingCartString;

	private List<JSONObject> listShoppingCart = new ArrayList<JSONObject>();

	private JSONObject jsonProduct;

	private SharedPreferences settings;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_info);

		try {
			jsonProduct = new JSONObject(getIntent().getStringExtra(
					ProductListActivity.KEY_PRODUCT_JSON));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		tvName = (TextView) findViewById(R.id.tvName);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvVolume = (TextView) findViewById(R.id.tvVolume);
		tvTotal = (TextView) findViewById(R.id.tvTotal);
		bq = (Button) findViewById(R.id.ibQuantity);
		try {
			String[] info = jsonProduct.getString("name").split("-");
			String productName = info[0];
			String productVolume = null;
			if (info.length > 1)
				productVolume = info[1];

			tvName.setText(productName);
			tvPrice.setText(jsonProduct.getString("price"));
			tvTotal.setText(jsonProduct.getString("price"));
			price = Double.parseDouble(jsonProduct.getString("price"));
			q = 1;
			total = price;
			if (productName.equals(null))
				tvVolume.setText("unknown");
			else
				tvVolume.setText(productVolume);

			// initialize shopping cart
			settings = getSharedPreferences(ShoppingCartActivity.PRER,
					MODE_PRIVATE);
			setShoppingCartString = settings.getStringSet(
					ShoppingCartActivity.PRER_SHOPPING_CART, null);

			for (String s : setShoppingCartString) {
				JSONObject j = new JSONObject(s);
				listShoppingCart.add(j);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void add(View view) {
		if (q < 25) {
			total += price;
			q += 1;
			total = (double) Math.round(total * 100) / 100;

			bq.setText(q + "");
			tvTotal.setText(total + "");
		}

	}

	public void minus(View view) {
		if (q > 1) {
			total -= price;
			q -= 1;
			total = (double) Math.round(total * 100) / 100;
			bq.setText(q + "");
			tvTotal.setText(total + "");
		}
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

	public void addToCart(View view) {

		try {

			boolean inCart = false;
			for (int i = 0; i < listShoppingCart.size(); i++) {
				if ((listShoppingCart.get(i).getString("name"))
						.equals(jsonProduct.get("name"))) {

					inCart = true;

					listShoppingCart.get(i).put("quantity",
							listShoppingCart.get(i).getInt("quantity") + q);

					i = listShoppingCart.size();

				}
			}
			if (!inCart)
				listShoppingCart.add(jsonProduct.put("quantity", q));

			setShoppingCartString.clear();

			for (JSONObject jsonObj : listShoppingCart) {
				setShoppingCartString.add(jsonObj.toString());
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		settings.edit()
				.putStringSet(ShoppingCartActivity.PRER_SHOPPING_CART,
						setShoppingCartString).commit();

	}
}
