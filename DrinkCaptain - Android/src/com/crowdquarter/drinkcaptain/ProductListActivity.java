package com.crowdquarter.drinkcaptain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.TypedArray;
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

import com.crowdquarter.drinkcaptain.util.CustomHttpsClient;

public class ProductListActivity extends Activity {
	TextView tvInfo;
	RelativeLayout rlBackground;
	ListView lvProducts;
	TextView tvMood, tvDescption;
	ImageView ivMood;

	private String urlTarget = "http://sandbox.delivery.com";
	private String urlEndpoint;
	private String merchant_id;

	private int background, moodIndex;

	TypedArray categoryMoods;

	private ProductListAdapter productListAdapter;

	private List<JSONObject> productObjects;
	private List<String> productNames;

	public final static String KEY_NAME = "com.crowdquarter.drinkcaptain.ProductListActivity.name";
	public final static String KEY_PRICE = "com.crowdquarter.drinkcaptain.ProductListActivity.price";
	public final static String KEY_PRODUCT_JSON = "com.crowdquarter.drinkcaptain.ProductListActivity.product_json";

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

		productObjects = new ArrayList<JSONObject>();

		productNames = Arrays.asList(getResources().getStringArray(
				getResources().obtainTypedArray(R.array.aProductCategoryMoods)
						.getResourceId(moodIndex, 0)));

		merchant_id = getResources().getStringArray(R.array.aMerchantIds)[moodIndex];

		lvProducts = (ListView) findViewById(R.id.lvProducts);
		lvProducts.setOnItemClickListener(goProductInfoListener);
		lvProducts.setOnItemLongClickListener(goSearchListener);

		String[] merchants = merchant_id.split("_");

		for (int i = 0; i < merchants.length; i++) {
			urlEndpoint = urlTarget + "/merchant/" + merchants[i] + "/menu"; // static

			new InfoAsync(i).execute(urlEndpoint);
		}

	}

	public OnItemClickListener goProductInfoListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent iProductInfo = new Intent(getApplicationContext(),
					ProductInfoActivity.class);
			JSONObject product = productObjects.get(position);
			try {

				iProductInfo.putExtra(KEY_NAME, product.getString("name"));
				iProductInfo.putExtra(KEY_PRICE, product.getString("price"));
				iProductInfo.putExtra(KEY_PRODUCT_JSON, product.toString());
				startActivity(iProductInfo);
			} catch (JSONException e) {
				e.printStackTrace();
			}

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

	private void setUpProductList(final JSONObject result, int time) {
		JSONArray menu;
		try {

			menu = result.getJSONArray("menu");
			// Log.v("menu", menu.toString());
			for (int i = 0; i < menu.length(); i++) {
				recurrsiveProductList((JSONObject) menu.get(i));
			}
			if (time == 0) {
				productListAdapter = new ProductListAdapter(this,
						productObjects);
				// Log.v("length", productObjects.size() + "");
				lvProducts.setAdapter(productListAdapter);
			} else
				productListAdapter.notifyDataSetChanged();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void recurrsiveProductList(JSONObject product) {
		try {
			if (product.getJSONArray("children").length() == 0) {
				for (int i = 0; i < productNames.size(); i++) {
					if (product.getString("name").contains(productNames.get(i))) {
						productObjects.add(product);
					}
				}

			} else {
				JSONArray menu = product.getJSONArray("children");
				for (int i = 0; i < menu.length(); i++) {
					recurrsiveProductList((JSONObject) menu.get(i));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class InfoAsync extends AsyncTask<String, Void, StringBuilder> {
		private int i;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		public InfoAsync(int i) {
			this.i = i;
		}

		@Override
		protected StringBuilder doInBackground(String... params) {
			HttpClient client = CustomHttpsClient.getHttpClient();

			HttpGet get = new HttpGet(params[0]);

			HttpResponse response = null;

			try {
				response = client.execute(get);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (response != null) {
				InputStream is = null;
				BufferedReader br = null;
				StringBuilder sb = null;
				try {
					is = response.getEntity().getContent();
					br = new BufferedReader(new InputStreamReader(is));
					sb = new StringBuilder();

					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					return sb;
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						br.close();
						is.close();
					} catch (Exception e) {
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(StringBuilder result) {
			if (result != null) {
				try {
					JSONObject searchResult = new JSONObject(result.toString());
					setUpProductList(searchResult, i);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				super.onPostExecute(result);
			}
		}
	}
}
