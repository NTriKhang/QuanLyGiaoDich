package com.example.QuanLyGiaoDich.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
	@Autowired
	private final DataSource dataSource;
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	
	public DatabaseService(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public Connection connect(String userName, String password, String database) throws SQLException, ClassNotFoundException {
			if(userName == "sys" || userName == "SYS") {
				userName += " as sysdba";
			}
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection connection = DriverManager.getConnection(url, userName, password);
			System.out.println(connection);
			return connection;
	}
	public Connection getConnection(String userName, String password) throws SQLException {
		Connection connection = dataSource.getConnection(userName, password);
		return connection;
	}
	public Boolean closeConnection(Connection connection) {
		try {
			if(connection != null && !connection.isClosed())
			{
				connection.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
}
