package com.crowdquarter.drinkcaptain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainMenuActivity extends Activity {
	private ActionBarDrawerToggle myDrawerToggle;
	private DrawerLayout myDrawerLayout;
	private ListView myDrawerList;

	private String[] drawerArray;
	private Set<String> setShoppingCartString;

	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		myDrawerList = (ListView) findViewById(R.id.left_drawer);
		drawerArray = getResources().getStringArray(R.array.aDrawerList);

		myDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		myDrawerList.setAdapter(new DrawerListAdapter(this, drawerArray));

		myDrawerList.setOnItemClickListener(new DrawerListClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
				R.drawable.ic_drawer, R.string.sOpen, R.string.sClose) {

			@Override
			public void onDrawerClosed(View drawerView) {
				invalidateOptionsMenu();

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}

		};
		myDrawerLayout.setDrawerListener(myDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
		// shopping cart
		settings = getSharedPreferences(ShoppingCartActivity.PRER, MODE_PRIVATE);
		setShoppingCartString = settings.getStringSet(
				ShoppingCartActivity.PRER_SHOPPING_CART, null);
		if (setShoppingCartString == null) { // initialize shopping cart
			setShoppingCartString = new HashSet<String>();
			settings.edit()
					.putStringSet(ShoppingCartActivity.PRER_SHOPPING_CART,
							setShoppingCartString).commit();
		} else {

		}

		new MyAsyncTask()
				.execute("http://devdc.azurewebsites.net/api/productcategories");
	}

	private void selectItem(int position) {
		if (position == 0) {
			TileFragment tileFragment = new TileFragment();

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, tileFragment).commit();

			myDrawerList.setItemChecked(position, true);
			myDrawerLayout.closeDrawer(myDrawerList);
		}

	}

	private class DrawerListClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0) {
				selectItem(position);
			}
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.my_action_bar, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (myDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.getItemId() == R.id.shoppingCart) {
			Intent iShoppingCart = new Intent(getApplicationContext(),
					ShoppingCartActivity.class);
			startActivity(iShoppingCart);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		myDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		myDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private class MyAsyncTask extends AsyncTask<String, String, String> {

		protected String doInBackground(String... args) {
			String result = null;
			URL url;
			try {
				url = new URL(args[0]);
				URLConnection connection = url.openConnection();

				HttpURLConnection httpConnection = (HttpURLConnection) connection;

				int responseCode = httpConnection.getResponseCode();

				// Tests if responseCode == 200 Good Connection
				if (responseCode == HttpURLConnection.HTTP_OK) {

					// Reads data from the connection
					InputStream is = httpConnection.getInputStream();

					BufferedReader bufferReader = null;
					try {
						bufferReader = new BufferedReader(
								new InputStreamReader(is, "UTF-8"), 8);
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					StringBuilder stringBuilder = new StringBuilder();

					String line;
					try {
						while ((line = bufferReader.readLine()) != null) {
							stringBuilder.append(line);
						}
						result = stringBuilder.toString();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (is != null)
							try {
								is.close();
							} catch (Exception e) {
							}
					}
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;

		}

		// Changes the values for a bunch of TextViews on the GUI
		protected void onPostExecute(String result) {
			try {
				JSONObject jObject = new JSONObject(result);

				Log.v("jObject", jObject.getJSONArray("ProductCategory")
						.toString());
				JSONArray jArray = jObject.getJSONArray("ProductCategory");
				for (int i = 0; i < jArray.length(); i++) {
					Log.v("Category", jArray.getJSONObject(i).getString("name"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
