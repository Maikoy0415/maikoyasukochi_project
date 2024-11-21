package maikoyasukochi_project;

import java.util.ArrayList;
import java.util.List;

public class ProductsManager {
	private List<Product> products;

	public ProductsManager() {
		this.products = new ArrayList<>();
	}

	// Add a product
	public void addProduct(Product product) {
		products.add(product);
		System.out.println("Product added: " + product);
	}

	// Remove a product by ID
	public void removeProduct(int id) {
		products.removeIf(product -> product.getId() == id);
		System.out.println("Product with ID " + id + " removed.");
	}

	// Get a product by name
	public Product getProductByName(String name) {
		for (Product product : products) {
			if (product.getName().equalsIgnoreCase(name)) {
				return product;
			}
		}
		return null;
	}

	// Display all products
	public void displayAllProducts() {
		if (products.isEmpty()) {
			System.out.println("No products available.");
		} else {
			for (Product product : products) {
				System.out.println(product);
			}
		}
	}
}
