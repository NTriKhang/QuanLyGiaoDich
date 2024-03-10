package com.example.QuanLyGiaoDich.Services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.Configure.MapUserConnection;
import com.example.QuanLyGiaoDich.dto.TransactionDto;

@Service
public class TransactionService {
	@Value("${spring.datasource.username}")
    private String userSystemName;
	
	public Boolean add_transaction(TransactionDto transactionDto)
			throws SQLException, ClassNotFoundException {
		Connection connection = MapUserConnection.getConnection(transactionDto.userName);
		if(connection == null)
			return false; 
		try {
            // Prepare the call to the stored procedure
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


}
