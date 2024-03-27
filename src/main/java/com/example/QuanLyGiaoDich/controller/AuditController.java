package com.example.QuanLyGiaoDich.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyGiaoDich.Services.PolicyService;
import com.example.QuanLyGiaoDich.dto.AddAuditDto;
import com.example.QuanLyGiaoDich.dto.AuditPolicyDto;
import com.example.QuanLyGiaoDich.dto.AuditTrialDto;
import com.example.QuanLyGiaoDich.dto.UserDetailAdminDto;
import com.example.QuanLyGiaoDich.dto.UserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {
	@Value("${spring.datasource.username}")
	private String userSystemName;
	
	@Autowired
	private PolicyService policyService;
	@GetMapping
	public ResponseEntity<List<AuditPolicyDto>> getAll() {
		List<AuditPolicyDto> auditPolicyDtos = policyService.getAuditPolicyDtos();
		return ResponseEntity.ok(auditPolicyDtos);
	}
	
	@PostMapping("/addAudit")
	public ResponseEntity<AddAuditDto> addAudit(@RequestBody String auditInfo) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		AddAuditDto audit = mapper.readValue(auditInfo, AddAuditDto.class);
		System.out.println(audit);
	    policyService.addFgaPolicy(userSystemName, audit.p_object_name, audit.p_policy_name, audit.p_type, audit.p_audit_condition);
	    return ResponseEntity.ok(audit);
	}
	
	@PostMapping("/deleteAudit")
	public ResponseEntity<AddAuditDto> deleteAudit(@RequestBody String auditInfo) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		AddAuditDto audit = mapper.readValue(auditInfo, AddAuditDto.class);
		System.out.println(audit);
	    policyService.deleteFgaPolicy(userSystemName, audit.p_object_name, audit.p_policy_name);
	    return ResponseEntity.ok(audit);
	}
	
	@GetMapping("/getAllTable")
	public ResponseEntity<List<String>> getAllTableName() {
		List<String> listTableName = policyService.getAllTable(userSystemName);
		return ResponseEntity.ok(listTableName);
	}
	
	@GetMapping("/getAuditTrail")
	public ResponseEntity<List<AuditTrialDto>> getAuditTrialTable() {
		List<AuditTrialDto> listAuditTrail = policyService.getAuditTrail();
		return ResponseEntity.ok(listAuditTrail);
	}
}
