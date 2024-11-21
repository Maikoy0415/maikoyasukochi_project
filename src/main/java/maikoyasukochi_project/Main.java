package maikoyasukochi_project;

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
			System.out.println("5. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				System.out.print("Enter product ID: ");
				int id = scanner.nextInt();
				scanner.nextLine();
				System.out.print("Enter product name: ");
				String name = scanner.nextLine();
				System.out.print("Enter product price: ");
				int price = scanner.nextInt();
				System.out.print("Enter product stock: ");
				int stock = scanner.nextInt();
				Product product = new Product(id, name, price, stock);
				manager.addProduct(product);
				break;

			case 2:
				System.out.print("Enter product ID to remove: ");
				int removeId = scanner.nextInt();
				manager.removeProduct(removeId);
				break;

			case 3:
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
				manager.displayAllProducts();
				break;

			case 5:
				System.out.println("Bye");
				scanner.close();
				return;

			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}
}
