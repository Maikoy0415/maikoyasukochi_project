package maikoyasukochi_project;
import java.util.Scanner;

public class Item {

	public String name;
	public int price;

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Item item = new Item();
		
		System.out.print("Enter item name: ");
        item.name = scanner.nextLine();

        System.out.print("Enter item price: ");
        item.price = scanner.nextInt();

		System.out.println(item.name + ": " + item.price + "yen");
		
		scanner.close();

	}

}
