package com.crowdquarter.drinkcaptain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ListView;

public class CheckoutActivity extends Activity {

<<<<<<< HEAD
	private SharedPreferences settings;
=======
	SharedPreferences settings;
>>>>>>> FETCH_HEAD

	private Set<String> setShoppingCartString;

	private List<JSONObject> listShoppingCart = new ArrayList<JSONObject>();

<<<<<<< HEAD
=======
	private ListView lvShoppingCart;

	private ExpandableListView expandableLvInfo;

>>>>>>> FETCH_HEAD
	private List<String> listInfo = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);

<<<<<<< HEAD
		settings = getSharedPreferences(MainMenuActivity.PRER, MODE_PRIVATE);

		setShoppingCartString = settings.getStringSet(
				MainMenuActivity.PRER_SHOPPING_CART, null);
=======
		settings = getSharedPreferences(ShoppingCartActivity.PRER, MODE_PRIVATE);

		setShoppingCartString = settings.getStringSet(
				ShoppingCartActivity.PRER_SHOPPING_CART, null);
>>>>>>> FETCH_HEAD

		try {
			for (String s : setShoppingCartString)
				listShoppingCart.add(new JSONObject(s));
		} catch (JSONException e) {
			e.printStackTrace();
		}
<<<<<<< HEAD
		ListView lvShoppingCart = (ListView) findViewById(R.id.lvProducts);
=======
		lvShoppingCart = (ListView) findViewById(R.id.lvProducts);
>>>>>>> FETCH_HEAD

		lvShoppingCart.setAdapter(new CheckoutCartAdapter(
				getApplicationContext(), listShoppingCart));

		listInfo.add(getResources().getString(R.string.sAddress));
		listInfo.add(getResources().getString(R.string.sPayment));
		listInfo.add(getResources().getString(R.string.sBilling));

		CheckoutExpandableAdapter checkoutExpandableAdapter = new CheckoutExpandableAdapter(
				getApplicationContext(), listInfo);
<<<<<<< HEAD
		ExpandableListView expandableLvInfo = (ExpandableListView) findViewById(R.id.expandableListView1);
=======
		expandableLvInfo = (ExpandableListView) findViewById(R.id.expandableListView1);
>>>>>>> FETCH_HEAD

		expandableLvInfo.setAdapter(checkoutExpandableAdapter);

		Gallery gallery = (Gallery) findViewById(R.id.gallery);

		gallery.setSpacing(1);

		gallery.setAdapter(new GalleryAdapter(getApplicationContext()));
	}
}
