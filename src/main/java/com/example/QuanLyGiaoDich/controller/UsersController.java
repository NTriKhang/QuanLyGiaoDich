package com.example.QuanLyGiaoDich.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.QuanLyGiaoDich.Configure.MapUserConnection;
import com.example.QuanLyGiaoDich.Services.DatabaseService;
import com.example.QuanLyGiaoDich.dto.UserLoginDto;
import com.example.QuanLyGiaoDich.dto.UserSignUpDto;
import com.example.QuanLyGiaoDich.models.Users;
import com.example.QuanLyGiaoDich.repositories.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UsersController {

    private final UsersRepository userRepository;
    private final DatabaseService databaseService;

    @Autowired
    public UsersController(UsersRepository userRepository,
    						DatabaseService databaseService) {
        this.userRepository = userRepository;
        this.databaseService = databaseService;
    }

    // Endpoint to get all users
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint to get a user by ID
    @GetMapping("/{userID}")
    public ResponseEntity<Users> getUserById(@PathVariable String userID) {
        return userRepository.findById(userID)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to create a new user
	/*
	 * @PostMapping public ResponseEntity<Users> createUser(@RequestBody Users user)
	 * { Users createdUser = userRepository.save(user); return new
	 * ResponseEntity<>(createdUser, HttpStatus.CREATED); }
	 */
    @PostMapping
    public ResponseEntity<UserSignUpDto> signupUser(@RequestPart String user, @RequestParam MultipartFile file){
    	System.out.println(user);
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		UserSignUpDto userSignUpDto = mapper.readValue(user,UserSignUpDto.class);
    		int resultCode = userRepository.createNewApplicationUser(UUID.randomUUID().toString(),
    												userSignUpDto.firstName,
    												userSignUpDto.lastName,
    												userSignUpDto.address,
    												userSignUpDto.phone,
    												userSignUpDto.email,
    												userSignUpDto.userName,
    												userSignUpDto.password,
    												file.getBytes());
    		System.out.println(resultCode);
    		if(resultCode == 200)
    			return new ResponseEntity<>(userSignUpDto, HttpStatus.CREATED); 
    		return new ResponseEntity<>(userSignUpDto, HttpStatus.BAD_REQUEST); 
		} catch (Exception error) {
			// TODO: handle exception
			System.out.println(error.getMessage());
			return new ResponseEntity<>(new UserSignUpDto(), HttpStatus.BAD_REQUEST);
		}
    }
    @PostMapping(path = "/login")
    public ResponseEntity<Object> loginUser(@RequestBody String userData) throws JsonMappingException, JsonProcessingException, SQLException, ClassNotFoundException{

        	ObjectMapper mapper = new ObjectMapper();
    		UserLoginDto userLoginUpDto = mapper.readValue(userData,UserLoginDto.class);
    		System.out.println(userLoginUpDto.userName + " " + userLoginUpDto.password);
    		if(MapUserConnection.mapUserConnection.containsKey(userLoginUpDto.userName)) {
    			return new ResponseEntity<>("have logged in on another devices", HttpStatus.ALREADY_REPORTED);
    		}
    		Connection conn = databaseService.connect(userLoginUpDto.userName, userLoginUpDto.password, " "); 
    		MapUserConnection.mapUserConnection.put(userLoginUpDto.userName, conn);
    		return new ResponseEntity<>("login success", HttpStatus.OK);
    }
    @GetMapping(path = "/logout/{userName}")
    public ResponseEntity<Object> logoutUser(@PathVariable String userName) throws JsonMappingException, JsonProcessingException, SQLException, ClassNotFoundException{
		//Connection res = databaseService.getConnection(userLoginUpDto.userName, userLoginUpDto.password); 
		Connection conn = null;
		System.out.println(userName);
		if(MapUserConnection.mapUserConnection.containsKey(userName)) {
			conn = MapUserConnection.mapUserConnection.get(userName);
		}
		else {
			return new ResponseEntity<>("no connection", HttpStatus.OK);
		}
		databaseService.closeConnection(conn);
		MapUserConnection.mapUserConnection.remove(userName);
		return new ResponseEntity<>("logout successfully", HttpStatus.OK);
}
    // Endpoint to update an existing user
    @PutMapping("/{userID}")
    public ResponseEntity<Users> updateUser(@PathVariable String userID, @RequestBody Users user) {
        if (userRepository.existsById(userID)) {
            user.setUserID(userID); // Set the ID to ensure it's updated
            Users updatedUser = userRepository.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete a user by ID
    @DeleteMapping("/{userID}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userID) {
        if (userRepository.existsById(userID)) {
            userRepository.deleteById(userID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
