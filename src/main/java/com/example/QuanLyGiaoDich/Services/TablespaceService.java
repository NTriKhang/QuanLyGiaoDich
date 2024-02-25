package com.example.QuanLyGiaoDich.Services;

import com.example.QuanLyGiaoDich.models.TablespaceInfo;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

@Service
public class TablespaceService {

	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public TablespaceService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public List<TablespaceInfo> getTablespaceInfo() {
        return jdbcTemplate.execute(
            conn -> {
                CallableStatement stmt = conn.prepareCall("{ call GET_TABLESPACE_INFO(?) }");
                stmt.registerOutParameter(1, OracleTypes.CURSOR);
                return stmt;
            },
            (CallableStatementCallback<List<TablespaceInfo>>) stmt -> {
                stmt.execute();
                ResultSet rs = (ResultSet) stmt.getObject(1);
                List<TablespaceInfo> tablespaces = new ArrayList<>();
                while (rs.next()) {
                    TablespaceInfo info = new TablespaceInfo();
                    info.setFileName(rs.getString("file_name"));
                    info.setSize(rs.getInt("size_mb"));
                    info.setTablespaceName(rs.getString("tablespace_name"));
                    tablespaces.add(info);
                }
                return tablespaces;
            }
        );  
    }
  
    public List<TablespaceInfo> getUserTablespacesInfo(String username) {
        return jdbcTemplate.execute(
            conn -> {
                CallableStatement stmt = conn.prepareCall("{ ? = call GET_USER_TABLESPACES_INFO(?) }");
                stmt.registerOutParameter(1, OracleTypes.CURSOR); 
                stmt.setString(2, username.toUpperCase()); 
                return stmt;
            },
            (CallableStatementCallback<List<TablespaceInfo>>) stmt -> {
                stmt.execute();
                ResultSet rs = (ResultSet) stmt.getObject(1); 
                List<TablespaceInfo> tablespaces = new ArrayList<>();
                while (rs.next()) {
                    TablespaceInfo info = new TablespaceInfo();
                    info.setFileName(rs.getString("file_name"));
                    info.setSize(rs.getInt("size_in_mb"));
                    info.setTablespaceName(rs.getString("tablespace_name"));
                    tablespaces.add(info);
                }
                return tablespaces;
            }
        );  
    }
    public void createTablespace(String tablespaceName, String datafilePath, int size) {      
    	try {
            jdbcTemplate.execute(
                (Connection connection) -> {
                    CallableStatement cs = connection.prepareCall("{ call CREATE_TABLESPACE_USER_CHOICE(?, ?, ?) }");
                    cs.setString(1, tablespaceName);
                    cs.setString(2, datafilePath);
                    cs.setInt(3, size);
                    return cs;
                },
                (CallableStatement cs) -> {
                    cs.execute();
                    return null;
                }
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.err.println("Lỗi xảy ra: " + e.getMessage());
        }
    }
    public void addDatafileToTablespace(String tablespaceName, String datafilePath, int size) {
        try {
            jdbcTemplate.execute(
                (Connection connection) -> {
                    CallableStatement cs = connection.prepareCall("{ call ADD_DATAFILE_TO_TABLESPACE(?, ?, ?) }");
                    cs.setString(1, tablespaceName);
                    cs.setString(2, datafilePath);
                    cs.setInt(3, size);
                    return cs;
                },
                (CallableStatement cs) -> {
                    cs.execute();
                    return null;
                }
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.err.println("Lỗi xảy ra: " + e.getMessage());
        }
    }

}
