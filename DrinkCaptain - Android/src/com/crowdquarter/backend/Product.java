package com.crowdquarter.backend;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class Product {
	private String id;
	private String name;
	private String volume;
	private String price;
	private String imageURL;
	private Bitmap image;
	private JSONObject jObject;

	public Product(JSONObject jObject) {
		setUpProduct(jObject);

	}

	public Product(String sJObject) {
		try {
			setUpProduct(new JSONObject(sJObject));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void setUpProduct(JSONObject jObject) {
		this.jObject = jObject;
		try {
			this.id = jObject.getString("id");
			this.name = jObject.getString("name");
			this.volume = jObject.getString("volume");
			this.price = jObject.getString("price");
			this.imageURL = jObject.getString("imageURL");
			this.image = null;

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getVolume() {
		return volume;
	}

	public String getPrice() {
		return price;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public Bitmap getImage() {
		return image;
	}

	public double getDoublePrice() {
		return Double.parseDouble(price);
	}

	public JSONObject getjObject() {
		return jObject;
	}

	public String toString() {
		return getjObject().toString();
	}

	public void setjObject(JSONObject jObject) {
		this.jObject = jObject;
	}

	@Override
	public boolean equals(Object O) {
		return name.equals(((Product) O).getName());
	}

	@Override
	public int hashCode() {
		return Integer.parseInt(id);
	}

	public String getImageURL() {
		return imageURL;
	}

}
