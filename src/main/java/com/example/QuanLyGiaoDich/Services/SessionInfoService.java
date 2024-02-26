package com.example.QuanLyGiaoDich.Services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.dialect.OracleTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.dto.InforSgaDto;
import com.example.QuanLyGiaoDich.dto.SessionDto;

@Service
public class SessionInfoService {
	private final JdbcTemplate jdbcTemplate;

	public SessionInfoService(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<SessionDto> getSessionInfo() {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_session_info")
	            .returningResultSet("session_info", (rs, rowNum) -> {
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
	    return (List<SessionDto>) result.get("session_info");
    }
	
	public void killSession(long sid, long serial) {
		jdbcTemplate.update("CALL kill_session(?, ?)", sid, serial);
    }
}
