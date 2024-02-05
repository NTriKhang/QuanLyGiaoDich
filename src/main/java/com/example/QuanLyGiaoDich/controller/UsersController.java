package com.example.QuanLyGiaoDich.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.QuanLyGiaoDich.dto.UserSignUpDto;
import com.example.QuanLyGiaoDich.models.Users;
import com.example.QuanLyGiaoDich.repositories.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.PageAttributes.MediaType;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UsersController {

    private final UsersRepository userRepository;

    @Autowired
    public UsersController(UsersRepository userRepository) {
        this.userRepository = userRepository;
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
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<>(new UserSignUpDto(), HttpStatus.BAD_REQUEST);
		}
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
