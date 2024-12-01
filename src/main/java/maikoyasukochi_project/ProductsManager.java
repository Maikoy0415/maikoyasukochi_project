package maikoyasukochi_project;

import java.util.ArrayList;
import java.util.List;

public class ProductsManager implements Searchable {
	private List<Product> products;

	public ProductsManager() {
		this.products = new ArrayList<>();
	}

	public void addProduct(Product product) {
		products.add(product);
		System.out.println("Product added: " + product);
	}

	public void removeProduct(int id) {
		products.removeIf(product -> product.getId() == id);
		System.out.println("Product with ID " + id + " removed.");
	}

	public Product getProductByName(String name) {
		for (Product product : products) {
			if (product.getName().equalsIgnoreCase(name)) {
				return product;
			}
		}
		return null;
	}

	public void displayAllProducts() {
		if (products.isEmpty()) {
			System.out.println("No products available.");
		} else {
			for (Product product : products) {
				System.out.println(product);
			}
		}
	}

	@Override
	public List<Product> search(String keyword) {
		List<Product> result = new ArrayList<>();
		for (Product product : products) {
			if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
				result.add(product);
			}
		}
		return result;
	}
}
