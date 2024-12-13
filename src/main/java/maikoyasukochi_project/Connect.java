package maikoyasukochi_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    public static void main(String[] args) {
        // DB接続用定数
        String DATABASE_NAME = "product_management";
        String PROPATIES = "?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
        String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + PROPATIES;
        // DB接続用・ユーザ定数
        String USER = "root";
        String PASS = "Maiko5415!";

        try {
            // MySQL に接続する
            Class.forName("com.mysql.cj.jdbc.Driver");
            // データベースに接続
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            // データベースに対する処理
            System.out.println("Connection successful");

            // 接続を閉じる
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
