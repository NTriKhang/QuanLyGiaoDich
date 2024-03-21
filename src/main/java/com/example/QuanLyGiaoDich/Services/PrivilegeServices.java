package com.example.QuanLyGiaoDich.Services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.dto.TablePrivilegeDto;

@Service
public class PrivilegeServices {
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public PrivilegeServices(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	 public int grantPrivilegeToTable(String username, String table, String privilege) {
	    	return jdbcTemplate.execute(
	    			conn -> {
	    				CallableStatement stmt = conn.prepareCall("{ ? = call grant_privilege(?,?,?) }");
	    				stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    				stmt.setString(2, username);
	    				stmt.setString(3, table);
	    				stmt.setString(4, privilege);
	    				return stmt;
	    			},
	    			(CallableStatementCallback<Integer>) stmt -> {
	    				stmt.execute();
	    				return stmt.getInt(1);
	    			}
	    		);
	    }
	 
	 public int revokePrivilegeToTable(String username, String table, String privilege) {
	    	return jdbcTemplate.execute(
	    			conn -> {
	    				CallableStatement stmt = conn.prepareCall("{ ? = call revoke_privilege(?,?,?) }");
	    				stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    				stmt.setString(2, username);
	    				stmt.setString(3, table);
	    				stmt.setString(4, privilege);
	    				return stmt;
	    			},
	    			(CallableStatementCallback<Integer>) stmt -> {
	    				stmt.execute();
	    				return stmt.getInt(1);
	    			}
	    		);
	    }
	 public List<TablePrivilegeDto> getPrivilegeUser(String username) {
	    	return jdbcTemplate.execute(
	    			conn -> {
	    				CallableStatement stmt = conn.prepareCall("{ ? = call get_privilege_user(?) }");
	    				stmt.registerOutParameter(1, OracleTypes.CURSOR);
	    				stmt.setString(2, username);
	    				return stmt;
	    			},
	    			(CallableStatementCallback<List<TablePrivilegeDto>>) stmt -> {
	    				stmt.execute();
	    				ResultSet rs = (ResultSet) stmt.getObject(1);
	    				List<TablePrivilegeDto> listResult = new ArrayList<>();
	    				while(rs.next()) {
	    					TablePrivilegeDto data = new TablePrivilegeDto();
	    					data.TABLE_NAME = rs.getString("TABLE_NAME");
	    					data.PRIVILEGE = rs.getString("PRIVILEGE");
	    					
	    					listResult.add(data);
	    				}
	    				return listResult;
	    			}
	    		);
	    }
}
