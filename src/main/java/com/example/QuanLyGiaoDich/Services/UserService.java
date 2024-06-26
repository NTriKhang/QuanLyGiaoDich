package com.example.QuanLyGiaoDich.Services;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import javax.sql.DataSource;

import org.apache.catalina.User;
import org.hibernate.internal.build.AllowSysOut;
import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.repositories.UsersRepository;
import com.fasterxml.jackson.core.TSFBuilder;
import com.example.QuanLyGiaoDich.dto.SessionDto;
import com.example.QuanLyGiaoDich.dto.UserListDto;
import com.example.QuanLyGiaoDich.dto.UserDetailAdminDto;
import com.example.QuanLyGiaoDich.models.Users;

@Service
public class UserService {
	@Autowired
	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;
	@Autowired
	private final UsersRepository usersRepository;
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";

	public UserService(DataSource dataSource, UsersRepository usersRepository) {
		this.dataSource = dataSource;
		this.usersRepository = usersRepository;
		this.jdbcTemplate = new JdbcTemplate(dataSource); 
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

	public List<UserListDto> getListUser(){
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_list_user")
	            .returningResultSet("list_user", (rs, rowNum) -> {
	                ResultSetMetaData metaData = rs.getMetaData();
	                int columnCount = metaData.getColumnCount();
	                List<Map<String, Object>> resultList = new ArrayList<>();
	                 do {
	                    Map<String, Object> rowData = new HashMap<>();
	                    for (int i = 1; i <= columnCount; i++) {
	                        String columnName = metaData.getColumnLabel(i);
	                        Object value = rs.getObject(i);
	                        rowData.put(columnName, value);
	                    }
	                    resultList.add(rowData);
	                } while (rs.next());
	                return resultList;
	            });
	    Map<String, Object> result = jdbcCall.execute();
	    return (List<UserListDto>) result.get("list_user");
	}
	
	public UserDetailAdminDto getUserInfoById(String id) {
	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_info_user_by_id")
	            .returningResultSet("user_info", BeanPropertyRowMapper.newInstance(UserDetailAdminDto.class));

	    MapSqlParameterSource in = new MapSqlParameterSource()
	            .addValue("id", id, Types.VARCHAR);

	    Map<String, Object> result = jdbcCall.execute(in);

	    List<UserDetailAdminDto> userInfoList = (List<UserDetailAdminDto>) result.get("user_info");
	    return userInfoList.isEmpty() ? null : userInfoList.get(0);
	}
	
	public List<Users> getTableUser(){
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_table_user")
	            .returningResultSet("table_user", (rs, rowNum) -> {
	                List<Users> resultList = new ArrayList<>();
	                 do {
	                	 Blob imageBlob = rs.getBlob("IMAGEPROFILE");
	                	 byte[] imageBytes = null;
	                	 if (imageBlob != null) {
	                	     imageBytes = imageBlob.getBytes(imageBlob.length() - 20, (int) imageBlob.length());
	                	 }
	                	 Users user = new Users(
	                	     rs.getString("USERID"),
	                	     rs.getString("FIRSTNAME"),
	                	     rs.getString("LASTNAME"),
	                	     rs.getString("ADDRESS"),
	                	     rs.getString("PHONE"),
	                	     rs.getString("EMAIL"),
	                	     rs.getString("USERNAME"),
	                	     rs.getString("PASSWORD"),
	                	     imageBytes,
	                	     rs.getDouble("MONEY"),
	                	     rs.getTimestamp("CREATEDDATE"),
	                	     rs.getTimestamp("LASTLOGIN")
	                	 );
	                	 resultList.add(user);
	                	 
	                } while (rs.next());
	                return resultList;
	            });
	    Map<String, Object> result = jdbcCall.execute();
	    return (List<Users>) result.get("table_user");
	}
	public List<String> getAllTablespaces() {
        String sql = "SELECT tablespace_name FROM dba_tablespaces";
        try {
            return jdbcTemplate.queryForList(sql, String.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.err.println("Lỗi xảy ra: " + e.getMessage());
            return null;
        }
    }
	public void submitAccount(String username, String tablespace, int quota) {
		try {
			jdbcTemplate.execute((Connection connection) -> {
				CallableStatement cs = connection.prepareCall("{ call CHANGE_TBSPACE_UNLOCK(?, ?, ?) }");
				cs.setString(1, username);
				cs.setString(2, tablespace);
				cs.setInt(3, quota);
				return cs;
			}, (CallableStatement cs) -> {
				cs.execute();
				return null;
			});
		} catch (DataAccessException e) {
			e.printStackTrace();
			System.err.println("Lỗi xảy ra: " + e.getMessage());
		}	
	}
	public int getTablespaceSize(String tablespaceName) {
	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	        .withProcedureName("get_tablespace_size")
	        .declareParameters(
	            new SqlParameter("p_tablespace_name", Types.VARCHAR),
	            new SqlOutParameter("p_tablespace_size", Types.NUMERIC)
	        );

	    SqlParameterSource inParams = new MapSqlParameterSource()
	        .addValue("p_tablespace_name", tablespaceName);

	    Map<String, Object> out = jdbcCall.execute(inParams);
	    BigDecimal size = (BigDecimal) out.get("p_tablespace_size");
	    return size != null ? size.intValue() : -1;
	}
	public int deleteApplicationUser(String userName) {
		try {
            return jdbcTemplate.execute((Connection connection) -> {
                CallableStatement cs = connection.prepareCall("{ call user_management_pkg.delete_application_user(?, ?) }");
                cs.setString(1, userName);
                cs.registerOutParameter(2, java.sql.Types.INTEGER);
                cs.execute();
                return cs.getInt(2);
            });
        } catch (DataAccessException e) {
			e.printStackTrace();
			System.err.println("Lỗi xảy ra: " + e.getMessage());
			return -1;
		}	
	}
}
