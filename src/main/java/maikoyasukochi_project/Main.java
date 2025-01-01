package maikoyasukochi_project;

import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		ProductsManager manager = new ProductsManager();
		Categories categories = new Categories();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\nMenu:");
			System.out.println("1. Add Product");
			System.out.println("2. Add Category");
			System.out.println("3. Remove Product");
			System.out.println("4. Get Product by Name");
			System.out.println("5. Display All Products");
			System.out.println("6. Search Products by Keyword");
			System.out.println("7. Update Product");
			System.out.println("8. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				try {
					System.out.print("Enter product ID: ");
					int id = scanner.nextInt();
					scanner.nextLine();

					System.out.print("Enter product name: ");
					String name = scanner.nextLine();
					if (name == null || name.trim().isEmpty()) {
						throw new IllegalArgumentException("Product name cannot be empty.");
					}

					System.out.print("Enter product price: ");
					int price = scanner.nextInt();
					scanner.nextLine();
					if (price <= 0) {
						throw new IllegalArgumentException("Product price must be a positive value.");
					}

					System.out.print("Enter product stock: ");
					int stock = scanner.nextInt();
					scanner.nextLine();
					if (stock < 0) {
						throw new IllegalArgumentException("Product stock cannot be negative.");
					}

					System.out.print("Enter discount rate (percentage) or type 'No' if no discount: ");
					String discountInput = scanner.nextLine();
					Product product;

					// Display available categories
					System.out.println("Available categories:");
					List<String> categoryList = categories.getCategoriesWithId();
					for (String cat : categoryList) {
						System.out.println("- " + cat);
					}
					System.out.print("Enter product category or type a new one: ");
					String category = scanner.nextLine();

					if (!categoryList.contains(category)) {
						System.out.println("Adding new category: " + category);
						categories.addCategory(category);
					}

					if ("No".equalsIgnoreCase(discountInput) || discountInput.trim().isEmpty()) {
						product = new Product(id, name, price, stock, category);
					} else {
						try {
							double discountRate = Double.parseDouble(discountInput);
							if (discountRate < 0 || discountRate > 100) {
								throw new IllegalArgumentException("Discount rate must be between 0 and 100.");
							}
							product = new DiscountedProduct(id, name, price, stock, discountRate, category);
						} catch (NumberFormatException e) {
							System.out.println("Invalid discount rate. Product not added.");
							continue;
						}
					}

					manager.addProduct(product);
					System.out.println("Product added: " + product);
				} catch (IllegalArgumentException e) {
					System.out.println("Error adding product: " + e.getMessage());
				}
				break;

			case 2:
				System.out.print("Enter new category name: ");
				String newCategory = scanner.nextLine();
				categories.addCategory(newCategory);
				break;

			case 3:
				System.out.print("Enter product ID to remove: ");
				int removeId = scanner.nextInt();
				manager.removeProduct(removeId);
				break;

			case 4:
				System.out.print("Enter product name: ");
				String searchName = scanner.nextLine();
				Product foundProduct = manager.getProductByName(searchName);
				if (foundProduct != null) {
					System.out.println("Found: \n" + foundProduct);
				} else {
					System.out.println("Product not found.");
				}
				break;

			case 5:
				manager.displayAllProducts();
				break;

			case 6:
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

			case 7:
				System.out.print("Enter product ID to update: ");
				int updateId = scanner.nextInt();
				scanner.nextLine();

				Product productToUpdate = manager.getProductById(updateId);
				if (productToUpdate != null) {
					System.out.println("Current product details: " + productToUpdate);

					System.out.print("Enter new name (Leave blank to keep current): ");
					String newName = scanner.nextLine();
					if (!newName.isEmpty()) {
						productToUpdate.setName(newName);
					}

					System.out.print("Enter new price (Leave blank to keep current): ");
					String newPriceInput = scanner.nextLine();
					if (!newPriceInput.isEmpty()) {
						try {
							int newPrice = Integer.parseInt(newPriceInput);
							productToUpdate.setPrice(newPrice);
						} catch (NumberFormatException e) {
							System.out.println("Invalid price input. Keeping current price.");
						}
					}

					System.out.print("Enter new stock (Leave blank to keep current): ");
					String newStockInput = scanner.nextLine();
					if (!newStockInput.isEmpty()) {
						try {
							int newStock = Integer.parseInt(newStockInput);
							productToUpdate.setStock(newStock);
						} catch (NumberFormatException e) {
							System.out.println("Invalid stock input. Keeping current stock.");
						}
					}

					System.out.print("Enter new discount rate (percentage, Leave blank to keep current): ");
					String newDiscountInput = scanner.nextLine();
					if (!newDiscountInput.isEmpty()) {
						try {
							double newDiscountRate = Double.parseDouble(newDiscountInput);
							if (productToUpdate instanceof DiscountedProduct) {
								((DiscountedProduct) productToUpdate).setDiscountRate(newDiscountRate);
							} else {
								productToUpdate = new DiscountedProduct(
										productToUpdate.getId(),
										productToUpdate.getName(),
										productToUpdate.getPrice(),
										productToUpdate.getStock(),
										newDiscountRate,
										productToUpdate.getCategory());
							}
						} catch (NumberFormatException e) {
							System.out.println("Invalid discount rate input. Keeping current discount rate.");
						}
					}

					System.out.println("Available categories:");
					List<String> categoryList = categories.getCategoriesWithId();
					for (String cat : categoryList) {
						System.out.println("- " + cat);
					}

					System.out.print("Enter new category (Leave blank to keep current): ");
					String newCategoryInput = scanner.nextLine();
					if (!newCategoryInput.isEmpty()) {
						if (!categoryList.contains(newCategoryInput)) {
							System.out.println("Adding new category: " + newCategoryInput);
							categories.addCategory(newCategoryInput);
						}
						productToUpdate.setCategory(newCategoryInput);
					}

					// データベースに変更を反映
					manager.updateProduct(productToUpdate);

					System.out.println("Updated product details: " + productToUpdate);
				} else {
					System.out.println("Product with ID " + updateId + " not found.");
				}
				break;

			case 8:
				System.out.println("Bye");
				scanner.close();
				return;

			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}
}
