package com.example.QuanLyGiaoDich.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyGiaoDich.Services.SessionInfoService;
import com.example.QuanLyGiaoDich.dto.ProcessDto;
import com.example.QuanLyGiaoDich.dto.SessionDto;
import com.example.QuanLyGiaoDich.dto.SessionKillDto;
import com.example.QuanLyGiaoDich.dto.UserLoginDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("api/v1/session")
public class SessionController {
	 private final SessionInfoService sessionInfoService;

	    public SessionController(SessionInfoService sessionInfoService) {
	        this.sessionInfoService = sessionInfoService;
	    }

	    @GetMapping("/info")
	    public List<SessionDto> getSessionInfo() {
	        return sessionInfoService.getSessionInfo();
	    }
	    @PostMapping("/kill_session")
	    public ResponseEntity<Object> kill_session(@RequestBody String sessionInfo) throws JsonMappingException, JsonProcessingException{
	    	ObjectMapper mapper = new ObjectMapper();
	    	SessionKillDto sessionDto = mapper.readValue(sessionInfo, SessionKillDto.class);
			System.out.println(sessionDto.sid + " " + sessionDto.serial);
			sessionInfoService.killSession(sessionDto.sid,sessionDto.serial);
			return new ResponseEntity<>("login success", HttpStatus.OK);
	    }
	    @GetMapping("/info/{session_id}")
	    public ResponseEntity<List<ProcessDto>> getProcessDto(@PathVariable("session_id") long session_id){
	    	List<ProcessDto> processDto = sessionInfoService.getProcessInfo(session_id);
	    	return new ResponseEntity<List<ProcessDto>>(processDto, HttpStatus.OK);
	    }
	    
}
