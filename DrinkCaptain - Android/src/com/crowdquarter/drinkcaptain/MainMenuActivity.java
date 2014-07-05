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

	private SharedPreferences settings;

	public final static String PRER = "preference";

	public final static String PREF_VERSION = "preference version";

	public final static String PREF_MODE = "preference mode";

	public final static String PREF_PRODUCT = "preference product";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		settings = getSharedPreferences(PRER, MODE_PRIVATE);

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

		int version;
		version = settings.getInt(PREF_VERSION, 0);

		if (version == 0) {

			new MyAsyncTask().execute(
					"http://devdc.azurewebsites.net/api/moodcategories",
					PREF_MODE, "MoodCategory");
			new MyAsyncTask().execute(
					"http://devdc.azurewebsites.net/api/productcategories",
					PREF_PRODUCT, "ProductCategory");

			settings.edit().putInt(PREF_VERSION, 1).commit();

		} else {

			selectItem(0);
		}

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

	public String isToString(InputStream is) {
		String sReturn = null, line;
		BufferedReader bufferReader = null;

		try {
			bufferReader = new BufferedReader(
					new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder stringBuilder = new StringBuilder();

			while ((line = bufferReader.readLine()) != null) {
				stringBuilder.append(line);
			}

			sReturn = stringBuilder.toString();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sReturn;
	}

	class MyAsyncTask extends AsyncTask<String, String, String> {

		String keyPref, keyJson;

		protected String doInBackground(String... args) {
			String result = null;
			URL url;
			try {
				url = new URL(args[0]);

				keyPref = args[1];

				keyJson = args[2];

				URLConnection connection = url.openConnection();

				HttpURLConnection httpConnection = (HttpURLConnection) connection;

				int responseCode = httpConnection.getResponseCode();

				// Tests if responseCode == 200 Good Connection
				if (responseCode == HttpURLConnection.HTTP_OK) {

					// Reads data from the connection
					InputStream is = httpConnection.getInputStream();

					result = isToString(is);
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			return result;

		}

		protected void onPostExecute(String result) {
			try {
				JSONArray jArray = new JSONObject(result).getJSONArray(keyJson);

				settings.edit().putString(keyPref, jArray.toString()).commit();

				if (keyPref.equals(PREF_PRODUCT))
					selectItem(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}
