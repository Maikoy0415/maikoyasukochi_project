package maikoyasukochi_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsManager implements Searchable {
	private Categories categories = new Categories();

	private Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/product_management", "root", "Maiko5415!");
	}

	public Product getProductById(int id) {
		String sql = "SELECT * FROM products WHERE id = ?";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return mapToProduct(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving information : " + e.getMessage());
		}
		return null;
	}

	public void addProduct(Product product) {
		String sql = "INSERT INTO products (id, name, price, stock, discount_rate, category_id) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, product.getId());
			stmt.setString(2, product.getName());
			stmt.setInt(3, product.getPrice());
			stmt.setInt(4, product.getStock());

			if (product instanceof DiscountedProduct) {
				stmt.setDouble(5, ((DiscountedProduct) product).getDiscountRate());
			} else {
				stmt.setNull(5, java.sql.Types.DOUBLE);
			}

			int categoryId = categories.getCategoryIdByName(product.getCategory());
			if (categoryId == -1) {
				categoryId = categories.addCategory(product.getCategory());
			}

			stmt.setInt(6, categoryId);
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

	public void updateProduct(Product product) {
		String sql = "UPDATE products SET name = ?, price = ?, stock = ?, discount_rate = ?, category_id = ? WHERE id = ?";
		try (Connection conn = connect()) {
			conn.setAutoCommit(false);
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, product.getName());
				stmt.setInt(2, product.getPrice());
				stmt.setInt(3, product.getStock());

				if (product instanceof DiscountedProduct) {
					stmt.setDouble(4, ((DiscountedProduct) product).getDiscountRate());
				} else {
					stmt.setNull(4, java.sql.Types.DOUBLE);
				}

				int categoryId = categories.getCategoryIdByName(product.getCategory());
				if (categoryId == -1) {
					categoryId = categories.addCategory(product.getCategory());
				}
				stmt.setInt(5, categoryId);

				stmt.setInt(6, product.getId());

				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {
					conn.commit();
					System.out.println("Product updated successfully.");
				} else {
					conn.rollback();
					System.out.println("No rows affected. Product may not exist or no changes were made.");
				}
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			System.err.println("Error updating product: " + e.getMessage());
		}
	}

	public void updateStockBatch(List<Integer> productIds, List<Integer> newStocks) {
		String sql = "UPDATE products SET stock = ? WHERE id = ?";
		try (Connection conn = connect()) {
			conn.setAutoCommit(false);

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				for (int i = 0; i < productIds.size(); i++) {
					stmt.setInt(1, newStocks.get(i));
					stmt.setInt(2, productIds.get(i));
					stmt.addBatch();
				}

				int[] updateCounts = stmt.executeBatch();

				boolean allUpdated = true;
				for (int count : updateCounts) {
					if (count == 0) {
						allUpdated = false;
						break;
					}
				}

				if (allUpdated) {
					conn.commit();
					System.out.println("All stocks updated successfully.");
				} else {
					conn.rollback();
					System.out.println("Batch update failed. All changes have been rolled back.");
				}
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			System.err.println("Error during batch stock update: " + e.getMessage());
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
		int categoryId = rs.getInt("category_id");

		String category = categories.getCategoryNameById(categoryId);

		if (rs.wasNull()) {
			return new Product(id, name, price, stock, category);
		} else {
			return new DiscountedProduct(id, name, price, stock, discountRate, category);
		}
	}
}