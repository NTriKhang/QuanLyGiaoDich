package com.example.QuanLyGiaoDich.Services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.QuanLyGiaoDich.Configure.MapUserConnection;
import com.example.QuanLyGiaoDich.dto.InfoTransaction;
import com.example.QuanLyGiaoDich.dto.TransactionDto;

@Service
public class TransactionService {
	private final String userSystemName;

	@Autowired
	public TransactionService(JdbcTemplate jdbcTemplate,
			@Value("${spring.datasource.username}") String userSystemName) {
		this.userSystemName = userSystemName;
	}

	public Boolean add_transaction(TransactionDto transactionDto) throws SQLException, ClassNotFoundException {
		Connection connection = MapUserConnection.getConnection(transactionDto.userName);
		if (connection == null)
			return false;

		try {
            // Prepare the call to the stored procedureA
            CallableStatement callableStatement = connection.prepareCall("{call " + userSystemName + ".INSERT_TRANSACTION(?, ?, ?, ?)}");

            callableStatement.setString(1, transactionDto.userName.split(" ")[0]); 
            callableStatement.setString(2, transactionDto.recipientUserName); 
            callableStatement.setString(3, "test"); 
            callableStatement.setDouble(4, transactionDto.amount); 
            callableStatement.execute();
            System.out.println("Stored procedure executed successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}

	public boolean isAmountExceedLimit(Double amount) {
		final double LIMIT = 100000000;
		return amount > LIMIT;
	}

	public List<InfoTransaction> getUserTransactions(String userName) throws SQLException, ClassNotFoundException {
		List<InfoTransaction> transactions = new ArrayList<>();

		Connection connection = MapUserConnection.getConnection(userName);
		if (connection == null) {
			return Collections.emptyList();
		}
		String userNameSplit = userName.split(" ")[0];
		String sqlQuery = "SELECT t.TransactionID, u1.UserName AS UserNameGui, u2.UserName AS UserNameNhan, "
				+ "t.TransactionType, t.Amount, t.TransactionDate " + "FROM " + userSystemName.toUpperCase()
				+ ".Transaction t " + "JOIN " + userSystemName.toUpperCase()
				+ ".Users u1 ON t.SenderUserID = u1.UserID " + "JOIN " + userSystemName.toUpperCase()
				+ ".Users u2 ON t.RecipientUserID = u2.UserID "
				+ "WHERE u1.UserName = ? OR u2.UserName = ? ORDER BY t.TransactionDate DESC";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
			preparedStatement.setString(1, userNameSplit);
			preparedStatement.setString(2, userNameSplit);

			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {

					InfoTransaction transaction = new InfoTransaction();

					transaction.setTransactionID(rs.getString("TransactionID"));
					transaction.setSenderUser(rs.getString("UserNameGui"));
					transaction.setRecipientUser(rs.getString("UserNameNhan"));
					transaction.setTransactionType(rs.getString("TransactionType"));
					transaction.setAmount(rs.getDouble("Amount"));
					transaction.setTransactionDate(rs.getDate("TransactionDate"));

					transactions.add(transaction);
				}
			}

			System.out.println("Query executed successfully.");
		}
		return transactions;
	}

	public Boolean delete_transaction(long transactionId, String userName) throws SQLException, ClassNotFoundException {
		Connection connection = MapUserConnection.getConnection(userName);
		if (connection == null) {
			return false;
			}
		String sqlQuery = "DELETE FROM "+ userSystemName.toUpperCase() +".Transaction WHERE TransactionID = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
			preparedStatement.setLong(1, transactionId);
			preparedStatement.execute();
			System.out.println("Stored procedure executed successfully.");

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public Boolean update_transaction(long transactionId, Double newAmount, String newTransactionType, String userName) throws SQLException, ClassNotFoundException{
		Connection connection = MapUserConnection.getConnection(userName);
		if(connection == null) {
			System.out.println("Connection is null!");
			return false;
		}
		String sqlQuery = "UPDATE "+ userSystemName.toUpperCase() +".Transaction " +
			               "SET " +
			               "Amount = ?, " +
			               "TransactionType = ? " +
			               "WHERE " +
			               "TransactionID = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
			if (newAmount != null) {
	            preparedStatement.setDouble(1, newAmount);
	        } else {
	            preparedStatement.setNull(1, Types.DOUBLE);
	        }
	        preparedStatement.setString(2, newTransactionType);
	        preparedStatement.setLong(3, transactionId);
	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Transaction updated successfully.");
	            return true;
	        } else {
	            System.out.println("No transaction updated.");
	            return false;
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
