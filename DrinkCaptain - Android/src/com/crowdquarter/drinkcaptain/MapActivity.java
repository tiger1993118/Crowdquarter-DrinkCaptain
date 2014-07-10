package com.crowdquarter.drinkcaptain;

import java.io.IOException;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
	}

	public void go(View view) {
		Log.v("GO", "GO");
		Geocoder geocoder = new Geocoder(MapActivity.this);

		Address adLCBO = null;

		while (adLCBO == null) {
			try {
				adLCBO = geocoder.getFromLocationName(
						"337 SPADINA AVENUE TORONTO-CENTRAL M5T 2E9", 1).get(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		LatLng corAdLCBO = new LatLng(adLCBO.getLatitude(),
				adLCBO.getLongitude());

		Marker mAdLCBO = map.addMarker(new MarkerOptions().position(corAdLCBO)
				.title("LCBO"));
	}
}
