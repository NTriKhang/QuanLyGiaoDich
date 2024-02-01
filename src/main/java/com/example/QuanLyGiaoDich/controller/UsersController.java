package com.example.QuanLyGiaoDich.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.QuanLyGiaoDich.models.Users;
import com.example.QuanLyGiaoDich.repositories.UsersRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
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
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users createdUser = userRepository.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
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
