package maikoyasukochi_project;

import java.text.DecimalFormat;

public class DiscountedProduct extends Product {
	private double discountRate;

	// Constructor
	public DiscountedProduct(int id, String name, int price, int stock, double discountRate) {
		super(id, name, price, stock);
		this.discountRate = discountRate;
	}

	// Getter for discountRate
	public double getDiscountRate() {
		return discountRate;
	}

	// Setter for discountRate
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	// Method to calculate the discounted price
	public double calculateDiscountedPrice() {
		return getPrice() * (1 - discountRate / 100);
	}

	// Override the toString method to include discountRate
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("0");
		return super.toString() + "\n" +
				"discount rate: " + discountRate + "%\n" +
				"discounted price: " + df.format(calculateDiscountedPrice()) + "yen";
	}
}
