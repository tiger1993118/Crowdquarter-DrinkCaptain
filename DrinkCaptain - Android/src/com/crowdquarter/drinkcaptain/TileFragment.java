package com.crowdquarter.drinkcaptain;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TileFragment extends Fragment {
	TextView tvMood1, tvMood2, tvMood3, tvMood4, tvMood5, tvMood6, tvTime;
	LinearLayout llBackground;
	ImageView ivMood1, ivMood2, ivMood3, ivMood4, ivMood5, ivMood6;

	String sTime, sDay = "";

	public final static String PREF_NAME = "MyPrefsFile";
	public final static String ACCESS_TOKEN = "access_token";
	public final static String REFRESH_TOKEN = "refresh_token";
	public final static String TOKEN_EXPIRES = "expires";
	public final static int MAIN_REQ_CODE = 777;
	public final static String KEY_BG = "com.crowdquarter.drinkcaptain.bg";
	public final static String KEY_MOOD_ID = "com.crowdquarter.drinkcaptain.moodIndex";
	public final static String KEY_MOOD_IV = "com.crowdquarter.drinkcaptain.moodimage";

	private int background;
	private int[] moodsIndex;
	private static String[] aStringCategoryMoods;

	public static TypedArray categoryMoods, dayMoods;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_tile, container, false);
		setUpVariable(root);
		return root;
	}

	@SuppressLint("Recycle")
	private void setUpVariable(View view) {

		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int hour = calendar.get(Calendar.HOUR_OF_DAY);

		if (1 <= day && day <= 4) {
			sDay = getResources().getStringArray(R.array.aDays)[day];
			background = getResources().obtainTypedArray(R.array.aTileBgs)
					.getResourceId(day, 0);

		} else {
			day = 0;
			sDay = getResources().getStringArray(R.array.aDays)[day];
			background = getResources().obtainTypedArray(R.array.aTileBgs)
					.getResourceId(day, 0);
		}
		if (5 <= hour && hour < 12)
			sTime = getResources().getStringArray(R.array.aTimes)[0];
		else if (12 <= hour && hour < 19)
			sTime = getResources().getStringArray(R.array.aTimes)[1];
		else
			sTime = getResources().getStringArray(R.array.aTimes)[2];

		tvTime = (TextView) view.findViewById(R.id.tvTime);
		tvTime.setText("It's a " + sDay + " " + sTime + "\n"
				+ "What are you mood for?");

		llBackground = (LinearLayout) view.findViewById(R.id.llBackground);
		llBackground.setBackgroundResource(background);

		categoryMoods = getResources().obtainTypedArray(R.array.aCategoryMoods);

		dayMoods = getResources().obtainTypedArray(R.array.aDayMoods);
		moodsIndex = getResources().getIntArray(dayMoods.getResourceId(day, 0));

		ivMood1 = (ImageView) view.findViewById(R.id.ivMood1);
		ivMood2 = (ImageView) view.findViewById(R.id.ivMood2);
		ivMood3 = (ImageView) view.findViewById(R.id.ivMood3);
		ivMood4 = (ImageView) view.findViewById(R.id.ivMood4);
		ivMood5 = (ImageView) view.findViewById(R.id.ivMood5);
		ivMood6 = (ImageView) view.findViewById(R.id.ivMood6);

		ivMood1.setBackgroundResource(categoryMoods.getResourceId(
				moodsIndex[0], 0));
		ivMood2.setBackgroundResource(categoryMoods.getResourceId(
				moodsIndex[1], 0));
		ivMood3.setBackgroundResource(categoryMoods.getResourceId(
				moodsIndex[2], 0));
		ivMood4.setBackgroundResource(categoryMoods.getResourceId(
				moodsIndex[3], 0));
		ivMood5.setBackgroundResource(categoryMoods.getResourceId(
				moodsIndex[4], 0));
		ivMood6.setBackgroundResource(categoryMoods.getResourceId(
				moodsIndex[5], 0));

		ivMood1.setOnClickListener(new MoodOnClickListener());
		ivMood2.setOnClickListener(new MoodOnClickListener());
		ivMood3.setOnClickListener(new MoodOnClickListener());
		ivMood4.setOnClickListener(new MoodOnClickListener());
		ivMood5.setOnClickListener(new MoodOnClickListener());
		ivMood6.setOnClickListener(new MoodOnClickListener());
		categoryMoods.recycle();

		tvMood1 = (TextView) view.findViewById(R.id.tvMood1);
		tvMood2 = (TextView) view.findViewById(R.id.tvMood2);
		tvMood3 = (TextView) view.findViewById(R.id.tvMood3);
		tvMood4 = (TextView) view.findViewById(R.id.tvMood4);
		tvMood5 = (TextView) view.findViewById(R.id.tvMood5);
		tvMood6 = (TextView) view.findViewById(R.id.tvMood6);

		aStringCategoryMoods = getResources().getStringArray(
				R.array.aStringCategoryMoods);
		tvMood1.setText(aStringCategoryMoods[moodsIndex[0]]);
		tvMood2.setText(aStringCategoryMoods[moodsIndex[1]]);
		tvMood3.setText(aStringCategoryMoods[moodsIndex[2]]);
		tvMood4.setText(aStringCategoryMoods[moodsIndex[3]]);
		tvMood5.setText(aStringCategoryMoods[moodsIndex[4]]);
		tvMood6.setText(aStringCategoryMoods[moodsIndex[5]]);

	}

	public TileFragment() {

	}

	public class MoodOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent iProductList = new Intent(getActivity(),
					CategoryListActivity.class);
			iProductList.putExtra(KEY_BG, background);
			switch (v.getId()) {
			case R.id.ivMood1:
				iProductList.putExtra(KEY_MOOD_ID, moodsIndex[0]);
				startActivity(iProductList);
				break;
			case R.id.ivMood2:
				iProductList.putExtra(KEY_MOOD_ID, moodsIndex[1]);
				startActivity(iProductList);
				break;
			case R.id.ivMood3:
				iProductList.putExtra(KEY_MOOD_ID, moodsIndex[2]);
				startActivity(iProductList);
				break;
			case R.id.ivMood4:
				iProductList.putExtra(KEY_MOOD_ID, moodsIndex[3]);
				startActivity(iProductList);
				break;
			case R.id.ivMood5:
				iProductList.putExtra(KEY_MOOD_ID, moodsIndex[4]);
				startActivity(iProductList);
				break;
			case R.id.ivMood6:
				iProductList.putExtra(KEY_MOOD_ID, moodsIndex[5]);
				startActivity(iProductList);
				break;

			}

		}

	}
}
