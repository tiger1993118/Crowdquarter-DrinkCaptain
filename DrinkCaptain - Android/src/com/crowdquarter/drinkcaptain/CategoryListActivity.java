package com.crowdquarter.drinkcaptain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CategoryListActivity extends Activity {
	TextView tvInfo;
	RelativeLayout rlBackground;
	ListView lvProducts;
	TextView tvMood, tvDescption;
	ImageView ivMood;

	private int background, moodIndex;

	TypedArray categoryMoods;

	private CategoryListAdapter productListAdapter;

	public final static String KEY_NAME = "com.crowdquarter.drinkcaptain.ProductListActivity.name";
	public final static String KEY_PRICE = "com.crowdquarter.drinkcaptain.ProductListActivity.price";
	public final static String KEY_PRODUCT_JSON = "com.crowdquarter.drinkcaptain.ProductListActivity.product_json";

	public final static String[] arrayDrinkCategory = { "Beer", "Wine", "Rum",
			"Scotch" };

	@SuppressLint("Recycle")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);

		background = getIntent().getIntExtra(TileFragment.KEY_BG, 0);
		moodIndex = getIntent().getIntExtra(TileFragment.KEY_MOOD_ID, 0);

		rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);
		rlBackground.setBackgroundResource(background);

		tvMood = (TextView) findViewById(R.id.tvMood);
		tvMood.setText(getResources().getStringArray(
				R.array.aStringCategoryMoods)[moodIndex]);

		tvDescption = (TextView) findViewById(R.id.tvDescrption);
		tvDescption.setText(getResources().getStringArray(
				R.array.aDescrptionMoods)[moodIndex]);

		ivMood = (ImageView) findViewById(R.id.ivMood);
		ivMood.setImageResource(getResources().obtainTypedArray(
				R.array.aCategoryMoods).getResourceId(moodIndex, 0));

		lvProducts = (ListView) findViewById(R.id.lvProducts);
		lvProducts.setOnItemClickListener(goProductListListener);
		// lvProducts.setOnItemLongClickListener(goSearchListener);

		productListAdapter = new CategoryListAdapter(this);
		lvProducts.setAdapter(productListAdapter);

	}

	public OnItemClickListener goProductListListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent iProductList = new Intent(getApplicationContext(),
					ProductListActivity.class);
			iProductList.putExtra(TileFragment.KEY_BG, background);
			startActivity(iProductList);

		}
	};
}
