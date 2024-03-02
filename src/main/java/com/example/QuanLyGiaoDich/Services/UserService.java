package com.example.QuanLyGiaoDich.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.repositories.UsersRepository;

@Service
public class UserService {
	@Autowired
	private final DataSource dataSource;
	@Autowired
	private final UsersRepository usersRepository;
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";

	public UserService(DataSource dataSource, UsersRepository usersRepository) {
		this.dataSource = dataSource;
		this.usersRepository = usersRepository;
	}
	//not use
	public Connection connect(String userName, String password, String database)
			throws SQLException, ClassNotFoundException {
		if (userName == "sys" || userName == "SYS") {
			userName += " as sysdba";
		}
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection connection = DriverManager.getConnection(url, userName, password);
		System.out.println(connection);
		return connection;
	}
	public Boolean connect_v2(String userName, String password, String database)
			throws SQLException, ClassNotFoundException {
		/*
		 * if(usersRepository.checkUserSignin(userName) == 200) return false;
		 */
		if (userName == "sys" || userName == "SYS") {
			userName += " as sysdba";
		}
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection connection = DriverManager.getConnection(url, userName, password);
		System.out.println(connection);
		return true;
	}
	//not use
	public Connection getConnection(String userName, String password) throws SQLException {
		Connection connection = dataSource.getConnection(userName, password);
		return connection;
	}
	//not use
	public Boolean closeConnection(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	public void logout(String username) {
		usersRepository.logout(username);

	}
	public void logout_all(String username) {
		usersRepository.logout_all(username);

	}
}
