package com.crowdquarter.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
	private Map<Product, Integer> productToQuantity;
	private List<Product> products;

	public ShoppingCart() {
		productToQuantity = new HashMap<Product, Integer>();
		products = new ArrayList<Product>();

	}

	public ShoppingCart(String sShoppingCart) {
		productToQuantity = new HashMap<Product, Integer>();
		products = new ArrayList<Product>();
		if (sShoppingCart != null) {
			String[] sProducts = Arrays.copyOfRange(sShoppingCart.split("\\{"),
					1, sShoppingCart.split("\\{").length);

			for (String sProduct : sProducts) {

				String sQuantity = sProduct.split("\\}")[1].substring(1,
						sProduct.split("\\}")[1].length() - 2);

				sProduct = "{" + sProduct.split("\\}")[0] + "}";

				productToQuantity.put(new Product(sProduct),
						Integer.parseInt(sQuantity));
				products.add(new Product(sProduct));
			}
		}

	}

	/**
	 * 
	 * @param product
	 * @return true iff product already in shopping cart ,add 1 to quantity
	 *         fasle otherwise
	 */
	public boolean putProduct(Product product) {
		for (Product otherProduct : productToQuantity.keySet()) {
			if (product.equals(otherProduct)) {
				productToQuantity.put(otherProduct,
						productToQuantity.get(otherProduct) + 1);
				return true;
			}
		}
		productToQuantity.put(product, 1);
		products.add(product);
		return false;

	}

	/**
	 * 
	 * @param product
	 * @return true iff product already in shopping cart ,add quantity to
	 *         quantity fasle otherwise
	 */
	public boolean putProduct(Product product, int quantity) {
		for (Product otherProduct : productToQuantity.keySet()) {
			if (product.equals(otherProduct)) {
				productToQuantity.put(otherProduct,
						productToQuantity.get(otherProduct) + quantity);
				return true;
			}
		}
		productToQuantity.put(product, quantity);
		products.add(product);
		return false;
	}

	public boolean deleteProduct(Product product) {
		productToQuantity.remove(product);
		return products.remove(product);
	}

	public void deleteProduct(int position) {
		Product product = products.remove(position);
		productToQuantity.remove(product);

	}

	public boolean contains(Product product) {
		return productToQuantity.containsKey(product);
	}

	public int size() {
		return productToQuantity.size();
	}

	public double getTotalPrice() {
		double total = 0;
		for (Product product : productToQuantity.keySet()) {
			total += product.getDoublePrice() * productToQuantity.get(product);
		}
		total = Math.round(total * 100) / 100;
		return total;
	}

	public String getStringTotalPrice() {
		double total = 0;
		for (Product product : productToQuantity.keySet()) {
			total += product.getDoublePrice() * productToQuantity.get(product);
		}
		total = Math.round(total * 100) / 100;
		return total + "";
	}

	public double getTotalTax() {
		double total = 0;
		for (Product product : productToQuantity.keySet()) {
			total += product.getDoublePrice() * productToQuantity.get(product);
		}
		total *= 0.13;
		total = Math.round(total * 100) / 100;
		return total;
	}

	public String getStringTotalTax() {
		double total = 0;
		for (Product product : productToQuantity.keySet()) {
			total += product.getDoublePrice() * productToQuantity.get(product);
		}
		total *= 0.13;
		total = Math.round(total * 100) / 100;
		return total + "";
	}

	public Product get(int position) {
		return products.get(position);
	}

	public String getStringQuantity(Product product) {
		return productToQuantity.get(product).toString();
	}

	public int getQuantity(Product product) {
		return productToQuantity.get(product);
	}

	public List<Product> getProducts() {
		return products;
	}

	public Map<Product, Integer> getProductToQuantity() {
		return productToQuantity;
	}

	@Override
	public String toString() {

		return productToQuantity.toString().substring(1,
				productToQuantity.toString().length() - 1)
				+ ", ";
	}

}
