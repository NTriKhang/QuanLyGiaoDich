package com.example.QuanLyGiaoDich.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.QuanLyGiaoDich.dto.AddProfileDto;
import com.example.QuanLyGiaoDich.dto.AlterProfileDto;
import com.example.QuanLyGiaoDich.dto.ProfileDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.QuanLyGiaoDich.Services.PolicyService;

@Controller
@RequestMapping("/api/v1/profile")
public class ProfileController {

	@Autowired
	private PolicyService policyService;
	
	@GetMapping
	public ResponseEntity<List<ProfileDto>> getAll() {
		List<ProfileDto> profiles = policyService.getProfiles();
		return ResponseEntity.ok(profiles);
	}
	
	@PostMapping("/addProfile")
	public ResponseEntity<String> addProfile(@RequestBody String infoProfile) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		AddProfileDto profile = mapper.readValue(infoProfile, AddProfileDto.class);
		int status = policyService.addProfile(profile.profileName, profile.sessionPerUser, profile.idleTime, 
				profile.connectTime, profile.failedLoginAttempts, profile.passwordLockTime);
		if(status == 1) {
			return ResponseEntity.ok().body("{\"success\": \"" + "create profile successfully" + "\"}");
		}
		else {
			return ResponseEntity.badRequest().body("{\"error\": \"" + "profile is already exits" + "\"}");
		}
	}
	
	@PostMapping("/alterProfile")
	public ResponseEntity<AlterProfileDto> alterProfile(@RequestBody String inforProfile) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		AlterProfileDto profileDto = mapper.readValue(inforProfile, AlterProfileDto.class);
		int status = policyService.alterProfile(profileDto.p_profile, profileDto.P_user_name);
		return ResponseEntity.ok(profileDto);
	}
}
