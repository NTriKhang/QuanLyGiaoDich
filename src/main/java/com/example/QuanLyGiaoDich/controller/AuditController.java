package com.example.QuanLyGiaoDich.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyGiaoDich.Services.PolicyService;
import com.example.QuanLyGiaoDich.dto.AuditPolicyDto;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {
	@Autowired
	private PolicyService policyService;
	@GetMapping
	public ResponseEntity<List<AuditPolicyDto>> getAll() {
		List<AuditPolicyDto> auditPolicyDtos = policyService.getAuditPolicyDtos();
		return ResponseEntity.ok(auditPolicyDtos);
	}
	
	
}
