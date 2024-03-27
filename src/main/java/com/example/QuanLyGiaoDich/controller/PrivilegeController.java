package com.example.QuanLyGiaoDich.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import com.example.QuanLyGiaoDich.Services.PolicyService;
import com.example.QuanLyGiaoDich.Services.PrivilegeService;
import com.example.QuanLyGiaoDich.Services.PrivilegeServices;
import com.example.QuanLyGiaoDich.dto.AddAuditDto;
import com.example.QuanLyGiaoDich.dto.AddRoleDto;
import com.example.QuanLyGiaoDich.dto.AssignRoleDto;
import com.example.QuanLyGiaoDich.dto.AuditPolicyDto;
import com.example.QuanLyGiaoDich.dto.RoleDetailObjectDto;
import com.example.QuanLyGiaoDich.dto.RoleDto;
import com.example.QuanLyGiaoDich.dto.UserAssignedRoleDto;
import com.example.QuanLyGiaoDich.dto.UserDetailAdminDto;
import com.example.QuanLyGiaoDich.dto.UserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.example.QuanLyGiaoDich.dto.TablePrivilegeDto;
import com.example.QuanLyGiaoDich.dto.UserPrivilegeDto;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/api/v1/privilege")
public class PrivilegeController {
	@Value("${spring.datasource.username}")
	private String userSystemName;
	@Autowired
	private PolicyService policyService;
	@Autowired
	private PrivilegeService privilegeService;
	@GetMapping
	public ResponseEntity<List<RoleDto>> getAllRole(){
		List<RoleDto> roleDtos = privilegeService.getAllRole();
		return ResponseEntity.ok(roleDtos);
	}
	@GetMapping("/getCol/{table_name}")
	public ResponseEntity<List<String>> getAllTable(@PathVariable String table_name) {
		System.out.println(table_name);
		List<String> tableCol = privilegeService.getAllTableAttribute(table_name);
		return ResponseEntity.ok(tableCol);
	}
	@GetMapping("/getProc")
	public ResponseEntity<List<String>> getAllTable() {
		List<String> tableCol = privilegeService.getAllProcedure();
		return ResponseEntity.ok(tableCol);
	}
	@GetMapping("/getAllTable")
	public ResponseEntity<List<String>> getAllTableName() {
		List<String> listTableName = policyService.getAllTable(userSystemName);
		return ResponseEntity.ok(listTableName);
	}
	@DeleteMapping("/{roleName}")
	public ResponseEntity<String> deleteRole(@PathVariable String roleName) {
		String res = privilegeService.deleteRole(roleName);
		return ResponseEntity.ok(res);
	}
	@GetMapping("/roleDetail/{roleName}")
	public ResponseEntity<List<RoleDetailObjectDto>> roleDetail(@PathVariable String roleName) {
		List<RoleDetailObjectDto> res = privilegeService.getRoleDetailObject(roleName);
		return ResponseEntity.ok(res);
	}
	@GetMapping("/roleDetail_user/{roleName}")
	public ResponseEntity<List<UserAssignedRoleDto>> roleDetail_user(@PathVariable String roleName) {
		List<UserAssignedRoleDto> res = privilegeService.getUserAssignedRole(roleName);
		return ResponseEntity.ok(res);
	}
	@PostMapping
	public ResponseEntity<String> addRole(@RequestBody String roleList) throws JsonMappingException, JsonProcessingException{
		//String res = privilegeService.addRolePrivilege(userSystemName, userSystemName, userSystemName);
		ObjectMapper mapper = new ObjectMapper();
		List<AddRoleDto> roleListMapper = mapper.readValue(roleList, new TypeReference<List<AddRoleDto>>() {});
		if(roleListMapper.size() > 0) {
			int res = privilegeService.isRoleExist(roleListMapper.get(0).roleName);
			System.out.println(res);
			if(res == 0) {
				for (AddRoleDto addRoleDto : roleListMapper) {
					System.out.println(addRoleDto.roleName);
					privilegeService.addRolePrivilege(addRoleDto.roleName, addRoleDto.executeCmd, addRoleDto.tableName);
				}
				return ResponseEntity.ok("completed");
			}
			return new ResponseEntity("role already exist", HttpStatus.CONFLICT);
		}
		return new ResponseEntity("Invalid parameter", HttpStatus.BAD_REQUEST);
	}
	@PostMapping("/assignRoleToUser")
	public ResponseEntity<Integer> assignRoleToUser(@RequestBody String assignRole) throws JsonMappingException, JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    AssignRoleDto assignRoleDto = mapper.readValue(assignRole, AssignRoleDto.class);
	    Integer result = privilegeService.assignRoleToUser(assignRoleDto.RoleName, assignRoleDto.UserName);
	    System.out.println(result);
	    return new ResponseEntity<Integer>(result, HttpStatus.OK);
  }
	@PostMapping("/grant_execute_to_role")
	public ResponseEntity<String> grant_execute_to_role(@RequestBody String assignRole) throws JsonMappingException, JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    AssignRoleDto assignRoleDto = mapper.readValue(assignRole, AssignRoleDto.class);
	    String result = privilegeService.grant_execute_to_role(assignRoleDto.RoleName, assignRoleDto.UserName);
	    System.out.println(result);
	    return new ResponseEntity<String>(result, HttpStatus.OK);
  }
	@PostMapping("/revoke_execute_proc")
	public ResponseEntity<String> revoke_execute_proc(@RequestBody String assignRole) throws JsonMappingException, JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    AssignRoleDto assignRoleDto = mapper.readValue(assignRole, AssignRoleDto.class);
	    String result = privilegeService.revoke_execute_proc(assignRoleDto.RoleName, assignRoleDto.UserName);
	    System.out.println(result);
	    return new ResponseEntity<String>(result, HttpStatus.OK);
  }
	@PostMapping("/revoke_role_from_user")
	public ResponseEntity<String> revoke_role_from_user(@RequestBody String assignRole) throws JsonMappingException, JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    AssignRoleDto assignRoleDto = mapper.readValue(assignRole, AssignRoleDto.class);
	    String result = privilegeService.revoke_role_from_user(assignRoleDto.RoleName, assignRoleDto.UserName);
	    System.out.println(result);
	    return new ResponseEntity<String>(result, HttpStatus.OK);
  }
	@Autowired
	private PrivilegeServices privilegeServices;
	
	@PostMapping("/grantTable")
	public ResponseEntity<UserPrivilegeDto> grantPrivilegeToTable(@RequestBody String infoPrivilege) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		UserPrivilegeDto privilege = mapper.readValue(infoPrivilege, UserPrivilegeDto.class);
		int status = privilegeServices.grantPrivilegeToTable(privilege.p_username, privilege.p_table, privilege.p_privilege);
		if(status == 1) {
			return ResponseEntity.ok(privilege);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/revokeTable")
	public ResponseEntity<UserPrivilegeDto> revokePrivilegeToTable(@RequestBody String infoPrivilege) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		UserPrivilegeDto privilege = mapper.readValue(infoPrivilege, UserPrivilegeDto.class);
		int status = privilegeServices.revokePrivilegeToTable(privilege.p_username, privilege.p_table, privilege.p_privilege);
		if(status == 1) {
			return ResponseEntity.ok(privilege);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/getUserPrivilege/{username}")
	public ResponseEntity<List<TablePrivilegeDto>> getUserPrivilege(@PathVariable("username") String username) {
		List<TablePrivilegeDto> listPrivilege = privilegeServices.getPrivilegeUser(username);
		return ResponseEntity.ok(listPrivilege);
	}
}
