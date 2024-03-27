package com.example.QuanLyGiaoDich.Services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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
    private final JdbcTemplate jdbcTemplate;
    private final String userSystemName;

    @Autowired
    public TransactionService(JdbcTemplate jdbcTemplate, @Value("${spring.datasource.username}") String userSystemName) {
        this.jdbcTemplate = jdbcTemplate;
        this.userSystemName = userSystemName;
    }
	
	public Boolean add_transaction(TransactionDto transactionDto)
			throws SQLException, ClassNotFoundException {
		Connection connection = MapUserConnection.getConnection(transactionDto.userName);
		if(connection == null)
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
	@Transactional(readOnly = true)
    public List<InfoTransaction> getUserTransactions(String userName) {
        List<InfoTransaction> transactions = new ArrayList<>();
        jdbcTemplate.execute((ConnectionCallback<Object>) connection -> {
            try (CallableStatement call = connection.prepareCall("{ call GET_USER_TRANSACTIONS(?, ?) }")) {
                call.setString(1, userName);
                call.registerOutParameter(2, Types.REF_CURSOR);
                call.execute();

                try (ResultSet rs = (ResultSet) call.getObject(2)) {
                    while (rs.next()) {
                        InfoTransaction transaction = new InfoTransaction();
                        transaction.setTransactionID(rs.getLong("TransactionID"));
                        transaction.setSenderUser(rs.getString("UserNameGui"));
                        transaction.setRecipientUser(rs.getString("UserNameNhan"));
                        transaction.setTransactionType(rs.getString("TransactionType"));
                        transaction.setAmount(rs.getDouble("Amount"));
                        transaction.setTransactionDate(rs.getDate("TransactionDate"));
                        transactions.add(transaction);
                    }
                }
            }
            return null;
        });
        return transactions;
    }
}
