package com.example.QuanLyGiaoDich.Services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

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
}