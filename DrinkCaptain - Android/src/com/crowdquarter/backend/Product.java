package com.crowdquarter.backend;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
	private String id;
	private String name;
	private String volume;
	private String price;
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

}
