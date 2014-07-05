package com.crowdquarter.drinkcaptain;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductListActivity extends Activity {

	private int background, moodIndex, dayIndex;

	private JSONObject jObjectMood;

	public final static String INTENT_BG = "com.crowdquarter.drinkcaptain.bg";

	public final static String INTENT_DAY_INDEX = "com.crowdquarter.drinkcaptain.dayIndex";

	public final static String INTENT_MOOD_INDEX = "com.crowdquarter.drinkcaptain.moodIndex";

	public final static String INTENT_PRODUCT_INDEX = "com.crowdquarter.drinkcaptain.productIndex";

	public final static String KEY_NAME = "com.crowdquarter.drinkcaptain.ProductListActivity.name";

	public final static String KEY_PRICE = "com.crowdquarter.drinkcaptain.ProductListActivity.price";

	public final static String KEY_PRODUCT_JSON = "com.crowdquarter.drinkcaptain.ProductListActivity.product_json";

	@SuppressLint("Recycle")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);

		background = getIntent().getIntExtra(INTENT_BG, 0);
		moodIndex = getIntent().getIntExtra(INTENT_MOOD_INDEX, 0);
		dayIndex = getIntent().getIntExtra(INTENT_DAY_INDEX, 0);

		// Set Background Resource
		RelativeLayout rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);
		rlBackground.setBackgroundResource(background);

		// Set TextViews
		TextView tvMood = (TextView) findViewById(R.id.tvMood);
		TextView tvDescption = (TextView) findViewById(R.id.tvDescrption);
		try {
			jObjectMood = TileFragment.jArrayMoods.getJSONObject(moodIndex);

			tvMood.setText(jObjectMood.getString("name"));
			tvDescption.setText(jObjectMood.getString("description"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Set ImageView
		ImageView ivMood = (ImageView) findViewById(R.id.ivMood);
		ivMood.setImageResource(getResources().obtainTypedArray(
				R.array.aMoodsDrawable).getResourceId(moodIndex, 0));

		ListView lvProducts = (ListView) findViewById(R.id.lvProducts);

		ProductListAdapter productListAdapter = new ProductListAdapter(this,
				TileFragment.jArrayProducts);
		lvProducts.setAdapter(productListAdapter);

		lvProducts.setOnItemClickListener(goProductListListener);
		// lvProducts.setOnItemLongClickListener(goSearchListener);

	}

	public OnItemClickListener goProductListListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent iProductList = new Intent(getApplicationContext(),
					RecommendListActivity.class);
			iProductList.putExtra(INTENT_BG, background);
			iProductList.putExtra(INTENT_DAY_INDEX, dayIndex);
			iProductList.putExtra(INTENT_MOOD_INDEX, moodIndex);
			iProductList.putExtra(INTENT_PRODUCT_INDEX, (int) id);
			startActivity(iProductList);

		}
	};
}
