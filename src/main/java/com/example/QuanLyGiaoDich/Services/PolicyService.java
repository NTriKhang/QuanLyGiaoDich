package com.example.QuanLyGiaoDich.Services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.naming.java.javaURLContextFactory;
import org.aspectj.apache.bcel.classfile.StackMapType;
import org.hibernate.boot.jaxb.hbm.internal.CacheAccessTypeConverter;
import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.dto.AddProfileDto;
import com.example.QuanLyGiaoDich.dto.AuditPolicyDto;
import com.example.QuanLyGiaoDich.dto.AuditTrialDto;
import com.example.QuanLyGiaoDich.dto.ProfileDto;
import com.fasterxml.jackson.core.TSFBuilder;

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
    
    public String addFgaPolicy(String objectSchema, String objectName, String policyName, String statementType, String auditCondition) {	
    	return jdbcTemplate.execute(
                conn -> {
                	CallableStatement stmt = conn.prepareCall("{? = call ADD_FGA_POLICY(?,?,?,?,?) }");
                	stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
    				stmt.setString(2, objectSchema);
    				stmt.setString(3, objectName);
    				stmt.setString(4, policyName);
    				stmt.setString(5, statementType);
    				stmt.setString(6, auditCondition);
                    return stmt;
                },
                (CallableStatementCallback<String>) stmt -> {
                    stmt.execute();
                   return stmt.getString(1);
                }
            );
    }
    
    public String deleteFgaPolicy(String objectSchema, String objectName, String policyName) {	
    	return jdbcTemplate.execute(
                conn -> {
                	CallableStatement stmt = conn.prepareCall("{? = call DELETE_FGA_POLICY(?,?,?) }");
                	stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
    				stmt.setString(2, objectSchema);
    				stmt.setString(3, objectName);
    				stmt.setString(4, policyName);
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
    
    public List<AuditTrialDto> getAuditTrail() {
    	return jdbcTemplate.execute(
    			conn -> {
    				CallableStatement stmt = conn.prepareCall("{ ? = call GET_AUDIT_TRIAL() }");
    				stmt.registerOutParameter(1, OracleTypes.CURSOR);
    				return stmt;
    			},
    			(CallableStatementCallback<List<AuditTrialDto>>) stmt -> {
    				stmt.execute();
    				ResultSet rs = (ResultSet) stmt.getObject(1);
    				List<AuditTrialDto> listResult = new ArrayList<>();
    				while(rs.next()) {
    					AuditTrialDto data = new AuditTrialDto();
    					data.SESSION_ID = rs.getString("SESSION_ID");
    					data.TIMESTAMP = rs.getString("TIMESTAMP");
    					data.DB_USER = rs.getString("DB_USER");
    					data.OBJECT_SCHEMA = rs.getString("OBJECT_SCHEMA");
    					data.OBJECT_NAME = rs.getString("OBJECT_NAME");
    					data.SQL_TEXT = rs.getString("SQL_TEXT");
    					
    					listResult.add(data);
    				}
    				return listResult;
    			}
    		);
    }
    
    public List<ProfileDto> getProfiles() {
    	return jdbcTemplate.execute(
    			conn -> {
    				CallableStatement stmt = conn.prepareCall("{ ? = call get_profiles() }");
    				stmt.registerOutParameter(1, OracleTypes.CURSOR);
    				return stmt;
    			},
    			(CallableStatementCallback<List<ProfileDto>>) stmt -> {
    				stmt.execute();
    				ResultSet rs = (ResultSet) stmt.getObject(1);
    				List<ProfileDto> listResult = new ArrayList<>();
    				while(rs.next()) {
    					ProfileDto data = new ProfileDto();
    					data.PROFILE = rs.getString("PROFILE");
    					data.RESOURCE_NAME = rs.getString("RESOURCE_NAME");
    					data.LIMIT = rs.getString("LIMIT");
    					
    					listResult.add(data);
    				}
    				return listResult;
    			}
    		);
    }
    
    public int addProfile(String profileName, int sessionPerUser, int idleTime, 
    		int connectTime, int failedLoginAttempts, int passwordLockTime) {	
    	return jdbcTemplate.execute(
                conn -> {
                	CallableStatement stmt = conn.prepareCall("{? = call add_profile(?,?,?,?,?,?) }");
                	stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
    				stmt.setString(2, profileName);
    				stmt.setInt(3, sessionPerUser);
    				stmt.setInt(4, idleTime);
    				stmt.setInt(5, connectTime);
    				stmt.setInt(6, failedLoginAttempts);
    				stmt.setInt(7, passwordLockTime);
                    return stmt;
                },
                (CallableStatementCallback<Integer>) stmt -> {
                    stmt.execute();
                   return stmt.getInt(1);
                }
            );
    }
    public int alterProfile(String profile, String username) {
    	return jdbcTemplate.execute(
    			conn -> {
    				CallableStatement stmt = conn.prepareCall("{ ? = call assign_profile(?,?) }");
    				stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
    				stmt.setString(2, profile);
    				stmt.setString(3, username);
    				return stmt;
    			},
    			(CallableStatementCallback<Integer>) stmt -> {
    				stmt.execute();
    				return stmt.getInt(1);
    			}
    		);
    }
    
}
