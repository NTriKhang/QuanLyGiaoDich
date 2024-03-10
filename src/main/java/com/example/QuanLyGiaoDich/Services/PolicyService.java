package com.example.QuanLyGiaoDich.Services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.aspectj.apache.bcel.classfile.StackMapType;
import org.hibernate.boot.jaxb.hbm.internal.CacheAccessTypeConverter;
import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.dto.AuditPolicyDto;

@Service
public class PolicyService {

	private JdbcTemplate jdbcTemplate;
    @Autowired
    public PolicyService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public List<AuditPolicyDto> getAuditPolicyDtos() {
        return jdbcTemplate.execute(
            conn -> {
                CallableStatement stmt = conn.prepareCall("{ ? = call get_audit_policies() }");
                stmt.registerOutParameter(1, OracleTypes.CURSOR);
                return stmt;
            },
            (CallableStatementCallback<List<AuditPolicyDto>>) stmt -> {
                stmt.execute();
                ResultSet rs = (ResultSet) stmt.getObject(1);
                List<AuditPolicyDto> aditPolicy = new ArrayList<>();
                while (rs.next()) {
                	AuditPolicyDto info = new AuditPolicyDto();
                    info.OBJECT_SCHEMA = rs.getString("OBJECT_SCHEMA");
                    info.OBJECT_NAME = rs.getString("OBJECT_NAME");
                    info.POLICY_OWNER = rs.getString("POLICY_OWNER");
                    info.POLICY_NAME = rs.getString("POLICY_NAME");
                    info.ENABLED = rs.getString("ENABLED");
                    aditPolicy.add(info);
                }
                return aditPolicy;
            }
        );  
    }
    
    public String addFgaPolicy(String objectSchema, String objectName, String policyName, String statementType) {	
    	return jdbcTemplate.execute(
                conn -> {
                	CallableStatement stmt = conn.prepareCall("{? = call ADD_FGA_POLICY(?,?,?,?) }");
                	stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
    				stmt.setString(2, objectSchema);
    				stmt.setString(3, objectName);
    				stmt.setString(4, policyName);
    				stmt.setString(5, statementType);
                    return stmt;
                },
                (CallableStatementCallback<String>) stmt -> {
                    stmt.execute();
                   return stmt.getString(1);
                }
            );
    }
    
    public List<String> getAllTable(String p_owner) {
    	return jdbcTemplate.execute(
    			conn -> {
    				CallableStatement stmt = conn.prepareCall("{ ? = call GET_TABLES_BY_OWNER(?) }");
    				stmt.registerOutParameter(1, OracleTypes.CURSOR);
    				stmt.setString(2, p_owner);
    				return stmt;
    			},
    			(CallableStatementCallback<List<String>>) stmt -> {
    				stmt.execute();
    				ResultSet rs = (ResultSet) stmt.getObject(1);
    				List<String> listResult = new ArrayList<>();
    				while(rs.next()) {
    					listResult.add(rs.getString("TABLE_NAME"));
    				}
    				return listResult;
    			}
    	);
    }
    
}
