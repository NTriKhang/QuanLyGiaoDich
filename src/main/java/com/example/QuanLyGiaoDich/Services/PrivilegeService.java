package com.example.QuanLyGiaoDich.Services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLType;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.dto.RoleDetailObjectDto;
import com.example.QuanLyGiaoDich.dto.RoleDto;
import com.example.QuanLyGiaoDich.dto.UserAssignedRoleDto;

@Service
public class PrivilegeService {
	private JdbcTemplate jdbcTemplate;
	@Value("${spring.datasource.username}")
	private String userSystemName;
	@Autowired
	public PrivilegeService(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	public List<RoleDto> getAllRole() {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ ? = call get_all_roles() }");
			stmt.registerOutParameter(1, OracleTypes.CURSOR);
			return stmt;
		}, (CallableStatementCallback<List<RoleDto>>) stmt -> {
			stmt.execute();
			ResultSet rs = (ResultSet) stmt.getObject(1);
			List<RoleDto> listResult = new ArrayList<>();
			while (rs.next()) {
				listResult.add(new RoleDto(rs.getString("ROLE"), rs.getString("ORACLE_MAINTAINED")));
			}
			return listResult;
		});
	}
	public List<RoleDetailObjectDto> getRoleDetailObject(String roleName) {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ ? = call get_role_privileges(?) }");
			stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setString(2, roleName);
			return stmt;
		}, (CallableStatementCallback<List<RoleDetailObjectDto>>) stmt -> {
			stmt.execute();
			ResultSet rs = (ResultSet) stmt.getObject(1);
			List<RoleDetailObjectDto> listResult = new ArrayList<>();
			while (rs.next()) {
				listResult.add(new RoleDetailObjectDto(rs.getString("TABLE_NAME"), rs.getString("PRIVILEGE")));
			}
			return listResult;
		});
	}
	public List<UserAssignedRoleDto> getUserAssignedRole(String roleName) {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ ? = call get_users_assigned_to_role(?) }");
			stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setString(2, roleName);
			return stmt;
		}, (CallableStatementCallback<List<UserAssignedRoleDto>>) stmt -> {
			stmt.execute();
			ResultSet rs = (ResultSet) stmt.getObject(1);
			List<UserAssignedRoleDto> listResult = new ArrayList<>();
			while (rs.next()) {
				listResult.add(new UserAssignedRoleDto(rs.getString("GRANTEE")));
			}
			return listResult;
		});
	}
	public String deleteRole(String roleName) {
	    return jdbcTemplate.execute(conn -> {
	        CallableStatement stmt = conn.prepareCall("DROP ROLE " + roleName.toUpperCase());
	        return stmt;
	    }, (CallableStatementCallback<String>) stmt -> {
	        stmt.execute();
	        return "Role " + roleName + " dropped successfully.";
	    });
	}
	public List<String> getAllTableAttribute(String tableName) {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ ? = call get_table_columns(?) }");
			stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setString(2, tableName);
			return stmt;
		}, (CallableStatementCallback<List<String>>) stmt -> {
			stmt.execute();
			ResultSet rs = (ResultSet) stmt.getObject(1);
			List<String> listResult = new ArrayList<>();
			while (rs.next()) {
				listResult.add(rs.getString("COLUMN_NAME"));
			}
			return listResult;
		});
	}
	public List<String> getAllProcedure() {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ ? = call get_procedures(?) }");
			stmt.registerOutParameter(1, OracleTypes.CURSOR);
			stmt.setString(2, userSystemName);
			return stmt;
		}, (CallableStatementCallback<List<String>>) stmt -> {
			stmt.execute();
			ResultSet rs = (ResultSet) stmt.getObject(1);
			List<String> listResult = new ArrayList<>();
			while (rs.next()) {
				listResult.add(rs.getString("OBJECT_NAME"));
			}
			return listResult;
		});
	}

	public String addRolePrivilege(String p_role_name, String execute_cmds, String table_names) {
		System.out.println(p_role_name + " " + execute_cmds + " " + table_names);
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ call create_group_privilege(?,?,?) }");
			stmt.setString(1, p_role_name);
			stmt.setString(2, execute_cmds);
			stmt.setString(3, table_names);
			return stmt;
		}, (CallableStatementCallback<String>) stmt -> {
			stmt.execute();
			return "completed";
		});
	}

	public String grant_execute_to_role(String p_role_name, String p_procedure_name) {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ call grant_execute_to_role(?,?) }");
			stmt.setString(1, p_procedure_name);
			stmt.setString(2, p_role_name);
			return stmt;
		}, (CallableStatementCallback<String>) stmt -> {
			stmt.execute();
			return "completed";
		});
	}
	public String revoke_execute_proc(String p_role_name, String p_procedure_name) {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ call revoke_execute_proc(?,?) }");
			stmt.setString(1, p_procedure_name);
			stmt.setString(2, p_role_name);
			return stmt;
		}, (CallableStatementCallback<String>) stmt -> {
			stmt.execute();
			return "completed";
		});
	}
	public String revoke_role_from_user(String p_role_name, String p_username) {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ call revoke_role_from_user(?,?) }");
			stmt.setString(1, p_role_name);
			stmt.setString(2, p_username);
			return stmt;
		}, (CallableStatementCallback<String>) stmt -> {
			stmt.execute();
			return "completed";
		});
	}
	public Integer isRoleExist(String p_role_name) {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ ? = call role_exists(?) }");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, p_role_name);
			return stmt;
		}, (CallableStatementCallback<Integer>) stmt -> {
			stmt.execute();
			return stmt.getInt(1);
		});
	}
	public Integer assignRoleToUser(String p_role_name, String p_user_name) {
		return jdbcTemplate.execute(conn -> {
			CallableStatement stmt = conn.prepareCall("{ call assign_role_to_user(?, ?) }");
			stmt.setString(1, p_role_name);
			stmt.setString(2, p_user_name);
			return stmt;
		}, (CallableStatementCallback<Integer>) stmt -> {
			stmt.execute();
			return 200;
		});
	}
}
