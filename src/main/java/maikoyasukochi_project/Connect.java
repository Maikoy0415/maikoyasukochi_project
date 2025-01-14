package maikoyasukochi_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	private static final String DATABASE_NAME = "product_management";
	private static final String PROPERTIES = "?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
	private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + PROPERTIES;
	private static final String USER = "root";
	private static final String PASS = "Maiko5415!";

	// 静的な接続メソッド
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}

	// 接続確認用のテストメイン
	public static void main(String[] args) {
		try (Connection conn = Connect.getConnection()) {
			System.out.println("Connection successful");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
