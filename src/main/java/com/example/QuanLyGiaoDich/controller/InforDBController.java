package com.example.QuanLyGiaoDich.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.example.QuanLyGiaoDich.repositories.InforDBRepository;
import com.example.QuanLyGiaoDich.dto.InforSgaDto;
import com.example.QuanLyGiaoDich.dto.InfoPgaDto;
import com.example.QuanLyGiaoDich.dto.InfoDatafileDto;
import com.example.QuanLyGiaoDich.dto.InfoInstanceDto;
import com.example.QuanLyGiaoDich.dto.InfoControlfileDto;

@RestController
@RequestMapping("api/v1/inforDB")
public class InforDBController {
	//private final InforDBRepository inforDBRepository;
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public InforDBController(JdbcTemplate jdbcTemplate) {
		
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@GetMapping
	public ResponseEntity<String> getAll() {
        String a = "Hello";
        return new ResponseEntity<>(a, HttpStatus.OK);
    }
	
	@GetMapping("/sga")
	public List<InforSgaDto> getSgaInfo() {
	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_sga_info")
	            .returningResultSet("sga_info", (rs, rowNum) -> {
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
	    return (List<InforSgaDto>) result.get("sga_info");
	}
	
	@GetMapping("/pga")
	public List<InfoPgaDto> getPgaInfo() {
	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_pga_info")
	            .returningResultSet("pga_info", (rs, rowNum) -> {
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
	    return (List<InfoPgaDto>) result.get("pga_info");
	}
	
	@GetMapping("/process")
	public List<InfoPgaDto> getProcessInfo() {
	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_process_info")
	            .returningResultSet("process_info", (rs, rowNum) -> {
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
	    return (List<InfoPgaDto>) result.get("process_info");
	}
	
	@GetMapping("/datafile")
	public List<InfoDatafileDto> getDatafileInfo() {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withFunctionName("get_datafile_info")
				.returningResultSet("datafile_info", (rs, rowNum) -> {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();
					List<Map<String, Object>> resultList = new ArrayList<>();
					 do {
						Map<String, Object> rowData = new HashMap<>();
						for(int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnLabel(i);
							Object value = rs.getObject(i);
							rowData.put(columnName, value);
						}
						resultList.add(rowData);
					} while(rs.next());
					return resultList;
				});
		 	Map<String, Object> result = jdbcCall.execute();
		    return (List<InfoDatafileDto>) result.get("datafile_info");
	}
	
	@GetMapping("/instance")
	public List<InfoInstanceDto> getInstanceInfo() {
	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_instance_info")
	            .returningResultSet("instance_info", (rs, rowNum) -> {
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
	    return (List<InfoInstanceDto>) result.get("instance_info");
	}
	
	@GetMapping("/controlfile")
	public List<InfoControlfileDto> getControlfileInfo() {
	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_controlfile_info")
	            .returningResultSet("controlfile_info", (rs, rowNum) -> {
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
	    return (List<InfoControlfileDto>) result.get("controlfile_info");
	}
	
	@GetMapping("/spfile")
	public List<InfoControlfileDto> getSpfileInfo() {
	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_spfile_info")
	            .returningResultSet("spfile_info", (rs, rowNum) -> {
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
	    return (List<InfoControlfileDto>) result.get("spfile_info");
	}
	
	@GetMapping("/database")
	public List<InfoControlfileDto> getDatabaseInfo() {
	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
	            .withFunctionName("get_database_info")
	            .returningResultSet("database_info", (rs, rowNum) -> {
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
	    return (List<InfoControlfileDto>) result.get("database_info");
	}
}
