package com.crowdquarter.drinkcaptain;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crowdquarter.backend.Product;
import com.crowdquarter.backend.ShoppingCart;

public class ProductInfoActivity extends Activity {
	int quantity = 1;

	double total, price;

	Button bQuantity;

	TextView tvTotal;

	private Product product;

	private SharedPreferences settings;

	private ShoppingCart shoppingCart;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_info);

		tvTotal = (TextView) findViewById(R.id.tvTotal);
		bQuantity = (Button) findViewById(R.id.ibQuantity);

		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvPrice = (TextView) findViewById(R.id.tvPrice);
		TextView tvVolume = (TextView) findViewById(R.id.tvVolume);

		product = new Product(getIntent().getStringExtra(
				RecommendListActivity.INTENT_PRODUCT));

		// Set Up Infos
		tvName.setText(product.getName());
		tvPrice.setText(product.getPrice());
		tvVolume.setText(product.getVolume());
		tvTotal.setText(product.getPrice());

		total = price = Double.parseDouble(product.getPrice());

		// Set Up Shopping Cart
		settings = getSharedPreferences(MainMenuActivity.PRER, MODE_PRIVATE);
		shoppingCart = new ShoppingCart(settings.getString(
				ShoppingCartActivity.PRER_SHOPPING_CART, null));
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

		shoppingCart.putProduct(product, quantity);
		settings.edit()
				.putString(ShoppingCartActivity.PRER_SHOPPING_CART,
						shoppingCart.toString()).commit();
		Log.v("ProductInfoSize", shoppingCart.size() + "");

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
