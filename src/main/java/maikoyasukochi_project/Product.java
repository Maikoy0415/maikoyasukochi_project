package maikoyasukochi_project;

public class Product {
	private int id;
	private String name;
	private int price;
	private int stock;

	// Constructor
	public Product(int id, String name, int price, int stock) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String toString() {
		String result = 
				"id: " + id + "\n" +
				"name: " + name + "\n" +
				"price: " + price + "yen\n" +
				"stock: " + stock + "\n";

		return result;
	}

}
