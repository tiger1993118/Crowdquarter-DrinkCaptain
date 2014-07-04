package com.crowdquarter.drinkcaptain;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TileFragment extends Fragment {

	private SharedPreferences settings;

	private int background, day, hour;

	private int[] aMoodsIndex = new int[6];

	public static JSONArray jArrayMoods, jArrayProducts;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_tile, container, false);

		setUpVariable(root);

		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@SuppressLint("Recycle")
	private void setUpVariable(View view) {

		setTimeAndBackground(view);

		settings = this.getActivity().getSharedPreferences(
				MainMenuActivity.PRER, 0);

		try {
			jArrayMoods = new JSONArray(settings.getString(
					MainMenuActivity.PREF_MODE, null));
			Log.v("jArray", jArrayMoods.toString());
			jArrayProducts = new JSONArray(settings.getString(
					MainMenuActivity.PREF_PRODUCT, null));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		TypedArray aMoodsDrawable = getResources().obtainTypedArray(
				R.array.aMoodsDrawable);

		ImageView ivMood1 = (ImageView) view.findViewById(R.id.ivMood1);
		ImageView ivMood2 = (ImageView) view.findViewById(R.id.ivMood2);
		ImageView ivMood3 = (ImageView) view.findViewById(R.id.ivMood3);
		ImageView ivMood4 = (ImageView) view.findViewById(R.id.ivMood4);
		ImageView ivMood5 = (ImageView) view.findViewById(R.id.ivMood5);
		ImageView ivMood6 = (ImageView) view.findViewById(R.id.ivMood6);

		// Set Up Tiles ImageView
		ImageView[] aIv = { ivMood1, ivMood2, ivMood3, ivMood4, ivMood5,
				ivMood6 };

		for (int i = 0; i < aIv.length; i++) {
			aIv[i].setBackgroundResource(aMoodsDrawable.getResourceId(
					aMoodsIndex[i], 0));
			aIv[i].setOnClickListener(new TileOnClickListener());
		}

		TextView tvMood1 = (TextView) view.findViewById(R.id.tvMood1);
		TextView tvMood2 = (TextView) view.findViewById(R.id.tvMood2);
		TextView tvMood3 = (TextView) view.findViewById(R.id.tvMood3);
		TextView tvMood4 = (TextView) view.findViewById(R.id.tvMood4);
		TextView tvMood5 = (TextView) view.findViewById(R.id.tvMood5);
		TextView tvMood6 = (TextView) view.findViewById(R.id.tvMood6);

		// Set Up Tiles TextView
		TextView[] aTv = { tvMood1, tvMood2, tvMood3, tvMood4, tvMood5, tvMood6 };

		try {
			for (int i = 0; i < aTv.length; i++)
				aTv[i].setText(jArrayMoods.getJSONObject(aMoodsIndex[i])
						.getString("name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressLint("Recycle")
	private void setTimeAndBackground(View view) {
		String sTime, sDay = "";
		String[] aStringMoodsIndex;

		Calendar calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		hour = calendar.get(Calendar.HOUR_OF_DAY);

		if (1 <= day && day <= 4) { // DAY
			sDay = getResources().getStringArray(R.array.aDays)[day];
			background = getResources().obtainTypedArray(R.array.aTileBgs)

			.getResourceId(day, 0); // got Tile background for weekday
			aStringMoodsIndex = getResources()
					.getStringArray(R.array.aDayMoods)[day].split("_");

		} else {
			day = 0;
			sDay = getResources().getStringArray(R.array.aDays)[day];
			background = getResources().obtainTypedArray(R.array.aTileBgs)

			.getResourceId(0, 0);// got Tile background for weekend
			aStringMoodsIndex = getResources()
					.getStringArray(R.array.aDayMoods)[0].split("_");

		}

		if (5 <= hour && hour < 12) // HOUR
			sTime = getResources().getStringArray(R.array.aTimes)[0];
		else if (12 <= hour && hour < 19)
			sTime = getResources().getStringArray(R.array.aTimes)[1];
		else
			sTime = getResources().getStringArray(R.array.aTimes)[2];

		// Set Texts
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		tvTime.setText("It's a " + sDay + " " + sTime + "\n"
				+ "What are you mood for?");

		// Set Background Res
		LinearLayout llBackground = (LinearLayout) view
				.findViewById(R.id.llBackground);
		llBackground.setBackgroundResource(background);

		for (int i = 0; i < aStringMoodsIndex.length; i++)
			aMoodsIndex[i] = Integer.parseInt(aStringMoodsIndex[i]);
	}

	public class TileOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent iProductList = new Intent(getActivity(),
					ProductListActivity.class);
			iProductList.putExtra(ProductListActivity.INTENT_BG, background);
			iProductList.putExtra(ProductListActivity.INTENT_DAY_INDEX, day);
			switch (v.getId()) {
			case R.id.ivMood1:
				iProductList.putExtra(ProductListActivity.INTENT_MOOD_INDEX,
						aMoodsIndex[0]);
				startActivity(iProductList);
				break;
			case R.id.ivMood2:
				iProductList.putExtra(ProductListActivity.INTENT_MOOD_INDEX,
						aMoodsIndex[1]);
				startActivity(iProductList);
				break;
			case R.id.ivMood3:
				iProductList.putExtra(ProductListActivity.INTENT_MOOD_INDEX,
						aMoodsIndex[2]);
				startActivity(iProductList);
				break;
			case R.id.ivMood4:
				iProductList.putExtra(ProductListActivity.INTENT_MOOD_INDEX,
						aMoodsIndex[3]);
				startActivity(iProductList);
				break;
			case R.id.ivMood5:
				iProductList.putExtra(ProductListActivity.INTENT_MOOD_INDEX,
						aMoodsIndex[4]);
				startActivity(iProductList);
				break;
			case R.id.ivMood6:
				iProductList.putExtra(ProductListActivity.INTENT_MOOD_INDEX,
						aMoodsIndex[5]);
				startActivity(iProductList);
				break;

			}

		}

	}

}
