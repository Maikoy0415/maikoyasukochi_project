package maikoyasukochi_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProductsManager implements Searchable {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/product_management";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "Maiko5415!";

	private Connection connect() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	public void addProduct(Product product) {
		String sql = "INSERT INTO products (id, name, price, stock, discount_rate) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, product.getId());
			stmt.setString(2, product.getName());
			stmt.setInt(3, product.getPrice());
			stmt.setInt(4, product.getStock());
			if (product instanceof DiscountedProduct) {
				stmt.setDouble(5, ((DiscountedProduct) product).getDiscountRate());
			} else {
				stmt.setNull(5, Types.DOUBLE);
			}
			stmt.executeUpdate();
			System.out.println("Product added: " + product);
		} catch (SQLException e) {
			System.err.println("Error adding product: " + e.getMessage());
		}
	}

	public void removeProduct(int id) {
		String sql = "DELETE FROM products WHERE id = ?";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Product with ID " + id + " removed.");
			} else {
				System.out.println("Product with ID " + id + " not found.");
			}
		} catch (SQLException e) {
			System.err.println("Error removing product: " + e.getMessage());
		}
	}

	public Product getProductByName(String name) {
		String sql = "SELECT * FROM products WHERE name = ?";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return mapToProduct(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving product: " + e.getMessage());
		}
		return null;
	}

	public void displayAllProducts() {
		String sql = "SELECT * FROM products";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			if (!rs.isBeforeFirst()) {
				System.out.println("No products available.");
			} else {
				while (rs.next()) {
					Product product = mapToProduct(rs);
					System.out.println(product);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error displaying products: " + e.getMessage());
		}
	}

	@Override
	public List<Product> search(String keyword) {
		String sql = "SELECT * FROM products WHERE name LIKE ?";
		List<Product> results = new ArrayList<>();
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, "%" + keyword + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				results.add(mapToProduct(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error searching products: " + e.getMessage());
		}
		return results;
	}

	private Product mapToProduct(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		int price = rs.getInt("price");
		int stock = rs.getInt("stock");
		double discountRate = rs.getDouble("discount_rate");
		if (rs.wasNull()) {
			return new Product(id, name, price, stock);
		} else {
			return new DiscountedProduct(id, name, price, stock, discountRate);
		}
	}
}
