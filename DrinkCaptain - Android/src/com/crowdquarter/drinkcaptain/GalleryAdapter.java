package com.crowdquarter.drinkcaptain;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GalleryAdapter extends BaseAdapter {

	Context c;
	List<Integer> listImages = new ArrayList<Integer>();

	public GalleryAdapter(Context c) {
		super();
		this.c = c;
		for (int i = 0; i < 10; i++)
			listImages.add(R.drawable.wine);

	}

	@Override
	public int getCount() {
		return listImages.size();
	}

	@Override
	public Object getItem(int position) {
		return listImages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(c);

		iv.setImageResource(listImages.get(position));
		iv.setLayoutParams(new Gallery.LayoutParams(150,
				ViewGroup.LayoutParams.MATCH_PARENT));
		iv.setScaleType(ScaleType.FIT_XY);
		return iv;
	}
}
