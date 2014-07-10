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
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.crowdquarter.backend.Product;

public class RecommendListActivity extends Activity {
	private ListView lvProducts;

	private JSONObject jObjectMood;

	RecommendListAdapter productAdapter;

	List<Product> listProducts = new ArrayList<Product>();

	private int dayIndex, moodIndex, productIndex;

	public static Bitmap bitmap;

	public final static String INTENT_PRODUCT = "com.crowdquarter.drinkcaptain.product";

	public final static String URL_RECOMMENDED_LIST = "http://devdc.azurewebsites.net/api/recommendproduct";

	public final static String URL_PRODUCT = "http://devdc.azurewebsites.net/api/product";

	public final static String URL_IMAGE = "http://www.lcbo.com/app/images/products/0";

	public final static String GETRECOMMENDEDLIST = "retrieve recommended product list from database";

	public final static String GETPRODUCTIMAGE = "retrieve product image from LCBO database";

	@SuppressLint("Recycle")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);

		dayIndex = getIntent().getIntExtra(
				ProductListActivity.INTENT_DAY_INDEX, 0);
		moodIndex = getIntent().getIntExtra(
				ProductListActivity.INTENT_MOOD_INDEX, 0);
		productIndex = getIntent().getIntExtra(
				ProductListActivity.INTENT_PRODUCT_INDEX, 0);

		// Set Background Resource
		RelativeLayout rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);
		rlBackground.setBackgroundResource(TileFragment.background);

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

		productAdapter = new RecommendListAdapter(RecommendListActivity.this,
				new ArrayList<Product>());

		moodIndex++;
		String endpoint = "/" + dayIndex + "/" + moodIndex + "/" + productIndex;
		new myAsyncTask(0).execute(GETRECOMMENDEDLIST, URL_RECOMMENDED_LIST
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
			bitmap = productAdapter.getItem(position).getImage();
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

	class myAsyncTask extends AsyncTask<String, String, String> {

		private int i;
		private String action;

		public myAsyncTask(int i) {
			this.i = i;
		}

		protected String doInBackground(String... args) {

			action = args[0];

			if (args[1].equals("null")) {
				return null;
			}

			try {

				URL url = new URL(args[1]);

				URLConnection connection = url.openConnection();

				HttpURLConnection httpConnection = (HttpURLConnection) connection;

				int responseCode = httpConnection.getResponseCode();

				// Tests if responseCode == 200 Good Connection
				if (responseCode == HttpURLConnection.HTTP_OK) {

					// Reads data from the connection
					InputStream is = httpConnection.getInputStream();

					if (action.equals(GETRECOMMENDEDLIST)) {

						return isToString(is);

					} else if (action.equals(GETPRODUCTIMAGE)) {

						Bitmap bitmap = null;

						bitmap = BitmapFactory.decodeStream(is);

						listProducts.get(i).setImage(bitmap);

					}
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result) {

			if (action.equals(GETRECOMMENDEDLIST)) {
				try {
					JSONArray jArray = new JSONObject(result)
							.getJSONArray("RecommendProductList");

					for (int i = 0; i < jArray.length(); i++) {
						Product newProduct = new Product(
								jArray.getJSONObject(i));
						listProducts.add(newProduct);

						new myAsyncTask(i).execute(GETPRODUCTIMAGE,
								newProduct.getImageURL());
					}

					productAdapter.updateProducts(listProducts);
					lvProducts.setAdapter(productAdapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (action.equals(GETPRODUCTIMAGE)) {
				productAdapter.updateProducts(listProducts);
				productAdapter.notifyDataSetChanged();
			}

		}
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
}
