﻿package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

	private static DBManager dbConnect = new DBManager();

	public static DBManager getInstance() {
		return dbConnect;
	}

	public static Connection conn = null;
	public Statement stmt = null;
	public PreparedStatement pstmt = null;
	public ResultSet rs = null;

	public static Connection getConnection() {
		try {
			// 1. 드라이버 설정
			//Class.forName("com.mysql.jdbc.Driver");
			
			//for MAC mysql..
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			String ip = "127.0.0.1";
			String port = "3306";
			String id = "root";
			String pw = "mysql";
	
				// 2. DB 연결
			//String url = "jdbc:mysql://localhost/lckdb?characterEncoding=utf8&amp;useSSL=false&amp;autoReconnection=true";
			
			//for MAC mysql..
			String url = "jdbc:mysql://localhost:3306/lck?characterEncoding=UTF-8&serverTimezone=UTC";
			
			
			conn = DriverManager.getConnection(url, id, pw);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("데이터베이스 연결 오류");
		}
		return conn;
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

}