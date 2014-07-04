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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductListActivity extends Activity {
	private ListView lvProducts;

	private JSONObject jObjectMood;

	private int background, dayIndex, moodIndex, productIndex;

	ProductListAdapter productAdapter;

	public final static String INTENT_PRODUCT = "com.crowdquarter.drinkcaptain.product";

	@SuppressLint("Recycle")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);

		background = getIntent().getIntExtra(MoodListActivity.INTENT_BG, 0);
		dayIndex = getIntent()
				.getIntExtra(MoodListActivity.INTENT_DAY_INDEX, 0);
		moodIndex = getIntent().getIntExtra(MoodListActivity.INTENT_MOOD_INDEX,
				0);
		productIndex = getIntent().getIntExtra(
				MoodListActivity.INTENT_PRODUCT_INDEX, 0);

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

		lvProducts = (ListView) findViewById(R.id.lvProducts);
		lvProducts.setOnItemClickListener(goProductInfoListener);
		lvProducts.setOnItemLongClickListener(goSearchListener);

		productAdapter = new ProductListAdapter(ProductListActivity.this,
				new JSONArray());

		moodIndex++;
		String endpoint = "/" + dayIndex + "/" + moodIndex + "/" + productIndex;
		new RecommendProductsAsyncTask()
				.execute("http://devdc.azurewebsites.net/api/recommendproduct"
						+ endpoint);

	}

	public OnItemClickListener goProductInfoListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent iProductInfo = new Intent(getApplicationContext(),
					ProductInfoActivity.class);
			iProductInfo.putExtra(INTENT_PRODUCT,
					productAdapter.getItem(position).toString());
			startActivity(iProductInfo);
		}
	};

	public OnItemLongClickListener goSearchListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {

			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			TextView tv = (TextView) view.findViewById(R.id.tvName);
			intent.putExtra(SearchManager.QUERY, tv.getText());
			// catch event that there's no activity to handle intent

			startActivity(intent);

			return true;
		}

	};

	class RecommendProductsAsyncTask extends AsyncTask<String, String, String> {

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
				JSONArray jArray = new JSONObject(result)
						.getJSONArray("RecommendProductList");
				productAdapter.updateArray(jArray);
				lvProducts.setAdapter(productAdapter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public String isToString(InputStream is) {
			String sReturn = null, line;
			BufferedReader bufferReader = null;

			try {
				bufferReader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"), 8);
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
	}
}
