package com.crowdquarter.drinkcaptain;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
}
