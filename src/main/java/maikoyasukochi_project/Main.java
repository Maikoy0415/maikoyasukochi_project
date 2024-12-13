package maikoyasukochi_project;

import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		ProductsManager manager = new ProductsManager();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\nMenu:");
			System.out.println("1. Add Product");
			System.out.println("2. Remove Product");
			System.out.println("3. Get Product by Name");
			System.out.println("4. Display All Products");
			System.out.println("5. Search Products by Keyword");
			System.out.println("6. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				// Add product
				try {
					System.out.print("Enter product ID: ");
					int id = scanner.nextInt();
					scanner.nextLine();

					System.out.print("Enter product name: ");
					String name = scanner.nextLine();
					try {
						if (name == null || name.trim().isEmpty()) {
							throw new IllegalArgumentException("Product name cannot be empty.");
						}
					} catch (IllegalArgumentException e) {
						System.out.println("Error: " + e.getMessage());
						continue;
					}

					System.out.print("Enter product price: ");
					int price = scanner.nextInt();
					scanner.nextLine();
					try {
						if (price <= 0) {
							throw new IllegalArgumentException("Product price must be a positive value.");
						}
					} catch (IllegalArgumentException e) {
						System.out.println("Error: " + e.getMessage());
						continue;
					}

					System.out.print("Enter product stock: ");
					int stock = scanner.nextInt();
					scanner.nextLine();
					try {
						if (stock < 0) {
							throw new IllegalArgumentException("Product stock cannot be negative.");
						}
					} catch (IllegalArgumentException e) {
						System.out.println("Error: " + e.getMessage());
						continue;
					}

					System.out.print("Enter discount rate (percentage) or type 'No' if no discount: ");
					String discountInput = scanner.nextLine();

					if ("No".equalsIgnoreCase(discountInput) || discountInput.trim().isEmpty()) {
						Product product = new Product(id, name, price, stock);
						manager.addProduct(product);
					} else {
						try {
							double discountRate = Double.parseDouble(discountInput);
							DiscountedProduct discountedProduct = new DiscountedProduct(id, name, price, stock,
									discountRate);
							manager.addProduct(discountedProduct);
						} catch (NumberFormatException e) {
							System.out.println("Invalid discount rate. Product not added.");
						}
					}
				} catch (IllegalArgumentException e) {
					System.out.println("Error adding product: " + e.getMessage());
				}
				break;

			case 2:
				// Remove product
				System.out.print("Enter product ID to remove: ");
				int removeId = scanner.nextInt();
				manager.removeProduct(removeId);
				break;

			case 3:
				// Get product by name
				System.out.print("Enter product name: ");
				String searchName = scanner.nextLine();
				Product foundProduct = manager.getProductByName(searchName);
				if (foundProduct != null) {
					System.out.println("Found: \n" + foundProduct);
				} else {
					System.out.println("Product not found.");
				}
				break;

			case 4:
				// Display all products
				manager.displayAllProducts();
				break;

			case 5:
				// Search products by keyword
				System.out.print("Enter keyword to search (Product name): ");
				String keyword = scanner.nextLine();
				List<Product> results = manager.search(keyword);
				if (results.isEmpty()) {
					System.out.println("No products found.");
				} else {
					System.out.println("Search results:");
					for (Product product : results) {
						System.out.println(product);
					}
				}
				break;

			case 6:
				// Exit
				System.out.println("Bye");
				scanner.close();
				return;

			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}
}