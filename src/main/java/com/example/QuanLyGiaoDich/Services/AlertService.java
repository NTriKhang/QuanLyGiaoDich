package com.example.QuanLyGiaoDich.Services;


import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.models.Alert;
import com.example.QuanLyGiaoDich.repositories.AlertRepository;

@Service
public class AlertService {
	private final AlertRepository alertRepository;
	private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AlertService(AlertRepository alertRepository, JdbcTemplate jdbcTemplate) {
        this.alertRepository = alertRepository;
        this.jdbcTemplate = jdbcTemplate;
    }
    public Alert getLatestUnprocessedAlert() {
        return alertRepository.findLatestUnprocessedAlert();
    }
    public void markAlertAsProcessed(Long alertId) {
        Alert alert = alertRepository.findById(alertId).orElse(null);
        if (alert != null) {
            alert.setIsProcessed(true);
            alertRepository.save(alert);
        }
    }
    public List<Map<String, Object>> getUserAlerts(String username) {
        return jdbcTemplate.execute(
            connection -> {
                CallableStatement cs = connection.prepareCall("{call GET_USER_ALERTS(?, ?)}");
                cs.setString(1, username);
                cs.registerOutParameter(2, OracleTypes.CURSOR);
                return cs;
            },
            (CallableStatementCallback<List<Map<String, Object>>>) cs -> {
                cs.execute();
                ResultSet rs = (ResultSet) cs.getObject(2);
                return new RowMapperResultSetExtractor<>(new AlertRowMapper()).extractData(rs);
            }
        );
    }

    private static class AlertRowMapper implements RowMapper<Map<String, Object>> {
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> row = new HashMap<>();
            row.put("message", rs.getString("Message"));
            row.put("createdDate", rs.getTimestamp("CreatedDate"));
            return row;
        }
    }
}
