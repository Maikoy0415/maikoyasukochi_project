package maikoyasukochi_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Categories {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/product_management";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "Maiko5415!";

	private Connection connect() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	public boolean removeCategoryById(int categoryId) {
		String sql = "DELETE FROM categories WHERE id = ?";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, categoryId);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println("Error removing category: " + e.getMessage());
			return false;
		}
	}

	public List<String> getCategoriesWithId() {
		List<String> categoriesList = new ArrayList<>();
		String sql = "SELECT id, name FROM categories";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				categoriesList.add(id + ", " + name);
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving categories: " + e.getMessage());
		}
		return categoriesList;
	}

	public String getCategoryNameById(int categoryId) {
		String categoryName = null;
		String sql = "SELECT name FROM categories WHERE id = ?";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, categoryId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				categoryName = rs.getString("name");
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving category name: " + e.getMessage());
		}
		return categoryName;
	}

	public int addCategory(String category) {
		String checkSql = "SELECT COUNT(*) FROM categories WHERE name = ?";
		try (Connection conn = connect(); PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
			checkStmt.setString(1, category);
			ResultSet rs = checkStmt.executeQuery();
			rs.next();

			if (rs.getInt(1) > 0) {
				System.out.println("Category already exists: " + category);
				return getCategoryIdByName(category);
			}
		} catch (SQLException e) {
			System.err.println("Error checking category: " + e.getMessage());
			return -1;
		}

		String insertSql = "INSERT INTO categories (name) VALUES (?)";
		try (Connection conn = connect();
				PreparedStatement insertStmt = conn.prepareStatement(insertSql,
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			insertStmt.setString(1, category);
			insertStmt.executeUpdate();

			// 挿入したカテゴリIDを返す
			ResultSet generatedKeys = insertStmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("Error adding category: " + e.getMessage());
			return -1;
		}
		return -1;
	}

	public int getCategoryIdByName(String categoryName) {
		int categoryId = -1;
		String sql = "SELECT id FROM categories WHERE name = ?";
		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, categoryName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				categoryId = rs.getInt("id");
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving category ID: " + e.getMessage());
		}
		return categoryId;
	}
}
