package com.example.QuanLyGiaoDich.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.QuanLyGiaoDich.Services.PrivilegeServices;
import com.example.QuanLyGiaoDich.dto.TablePrivilegeDto;
import com.example.QuanLyGiaoDich.dto.UserPrivilegeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/api/v1/privilege")
public class PrivilegeController {
	
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
