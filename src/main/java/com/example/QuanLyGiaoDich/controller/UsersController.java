package com.example.QuanLyGiaoDich.controller;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.QuanLyGiaoDich.Configure.MapUserConnection;
import com.example.QuanLyGiaoDich.Services.UserService;
import com.example.QuanLyGiaoDich.dto.UserLoginDto;
import com.example.QuanLyGiaoDich.dto.UserSignUpDto;
import com.example.QuanLyGiaoDich.dto.UserListDto;
import com.example.QuanLyGiaoDich.dto.UserDetailAdminDto;
import com.example.QuanLyGiaoDich.dto.UserInfoDto;
import com.example.QuanLyGiaoDich.models.Users;
import com.example.QuanLyGiaoDich.repositories.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import java.io.Console;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UsersController {

	private final UsersRepository userRepository;
	private final UserService userService;
	public UsersController(UsersRepository userRepository, UserService databaseService) {
		this.userRepository = userRepository;
		this.userService = databaseService;
	}

	// Endpoint to get all users
	@GetMapping
	public ResponseEntity<List<Users>> getAllUsers() {
		List<Users> users = userRepository.findAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/listUser")
	public List<UserListDto> getListUser(){
		try {
			return userService.getListUser();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@GetMapping("/TableUser")
	public List<Users> getTableUser(){
		try {
			return userService.getTableUser();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@PostMapping("/findUserById")
	public ResponseEntity<UserDetailAdminDto> findUserById(@RequestBody String userInfo) throws JsonMappingException, JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    UserInfoDto userDetail = mapper.readValue(userInfo, UserInfoDto.class);
	    System.out.println(userDetail);
	    UserDetailAdminDto result = userService.getUserInfoById(userDetail.user_id);
	    System.out.println(result);
	    return new ResponseEntity<UserDetailAdminDto>(result, HttpStatus.OK);
	}
	
	// Endpoint to get a user by ID
	@GetMapping("/{userID}")
	public ResponseEntity<Users> getUserById(@PathVariable String userID) {
		return userRepository.findById(userID).map(user -> new ResponseEntity<>(user, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Endpoint to create a new user
	/*
	 * @PostMapping public ResponseEntity<Users> createUser(@RequestBody Users user)
	 * { Users createdUser = userRepository.save(user); return new
	 * ResponseEntity<>(createdUser, HttpStatus.CREATED); }
	 */
	@PostMapping
	public ResponseEntity<UserSignUpDto> signupUser(@RequestPart String user, @RequestParam MultipartFile file) {
		System.out.println(user);
		ObjectMapper mapper = new ObjectMapper();
		try {
			UserSignUpDto userSignUpDto = mapper.readValue(user, UserSignUpDto.class);
			int resultCode = userRepository.createNewApplicationUser(UUID.randomUUID().toString(),
					userSignUpDto.firstName, userSignUpDto.lastName, userSignUpDto.address, userSignUpDto.phone,
					userSignUpDto.email, userSignUpDto.userName, userSignUpDto.password, file.getBytes());
			System.out.println(resultCode);
			if (resultCode == 200)
				return new ResponseEntity<>(userSignUpDto, HttpStatus.CREATED);
			return new ResponseEntity<>(userSignUpDto, HttpStatus.BAD_REQUEST);
		} catch (Exception error) {
			// TODO: handle exception
			System.out.println(error.getMessage());
			return new ResponseEntity<>(new UserSignUpDto(), HttpStatus.BAD_REQUEST);
		}
	}
	//not use
	@PostMapping(path = "/login")
	public ResponseEntity<Object> loginUser(@RequestBody String userData)
			throws JsonMappingException, JsonProcessingException, SQLException, ClassNotFoundException {

		ObjectMapper mapper = new ObjectMapper();
		UserLoginDto userLoginUpDto = mapper.readValue(userData, UserLoginDto.class);
		System.out.println(userLoginUpDto.userName + " " + userLoginUpDto.password);
		String[] userName = userLoginUpDto.userName.split(" ");
		if (MapUserConnection.mapUserConnection.containsKey(userName[0] + ' ' + userName[1])) {
			return new ResponseEntity<>("have logged in on another devices", HttpStatus.CONFLICT);
		}
		Connection conn = userService.connect(userName[0], userLoginUpDto.password, " ");
		MapUserConnection.mapUserConnection.put(userName[0] + ' ' +  userName[1], conn);
		return new ResponseEntity<>("login success", HttpStatus.OK);
	}

	@PostMapping(path = "/login_v2")
	public ResponseEntity<Object> loginUser_v2(@RequestBody String userData)
			throws JsonMappingException, JsonProcessingException, SQLException, ClassNotFoundException {

		ObjectMapper mapper = new ObjectMapper();
		UserLoginDto userLoginUpDto = mapper.readValue(userData, UserLoginDto.class);
		System.out.println(userLoginUpDto.userName + " " + userLoginUpDto.password);

		if (!userService.connect_v2(userLoginUpDto.userName, userLoginUpDto.password, " ")) {
			return new ResponseEntity<>("have logged in on another devices", HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>("login success", HttpStatus.OK);
	}
	//not use
	@GetMapping(path = "/logout/{userName}")
	public ResponseEntity<Object> logoutUser(@PathVariable String userName)
			throws JsonMappingException, JsonProcessingException, SQLException, ClassNotFoundException {
		// Connection res = databaseService.getConnection(userLoginUpDto.userName,
		// userLoginUpDto.password);
		Connection conn = null;
		System.out.println(userName);
 		if (MapUserConnection.mapUserConnection.containsKey(userName)) {
			conn = MapUserConnection.mapUserConnection.get(userName);
		} else {
			return new ResponseEntity<>("no connection", HttpStatus.OK);
		}
		userService.closeConnection(conn);
		MapUserConnection.mapUserConnection.remove(userName);
		return new ResponseEntity<>("logout successfully", HttpStatus.OK);
	}
	@GetMapping(path = "/logout_v2/{userName}")
	public ResponseEntity<Object> logoutUser_v2(@PathVariable String userName)
			throws JsonMappingException, JsonProcessingException, SQLException, ClassNotFoundException {
		// Connection res = databaseService.getConnection(userLoginUpDto.userName,
		// userLoginUpDto.password);
		try {
			System.out.println(userName);
			
			userService.logout(userName);
			return new ResponseEntity<>("logout successfully", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Lỗi xảy ra: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	@GetMapping(path = "/logout_all/{userName}")
	public ResponseEntity<Object> logoutUserAll(@PathVariable String userName)
			throws JsonMappingException, JsonProcessingException, SQLException, ClassNotFoundException {
		// Connection res = databaseService.getConnection(userLoginUpDto.userName,
		// userLoginUpDto.password);
		try {
			System.out.println(userName);
			
			userService.logout_all(userName);
			return new ResponseEntity<>("logout successfully", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Lỗi xảy ra: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
	@GetMapping("/tablespaces")
    public ResponseEntity<List<String>> getTablespaces() {
        try {
            List<String> tablespaces = userService.getAllTablespaces();
            return ResponseEntity.ok(tablespaces);
        } catch (Exception e) {
            System.err.println("Lỗi xảy ra khi lấy danh sách tablespace: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	@PostMapping("/submitAccount")
	public ResponseEntity<?> setDataQuota(@RequestBody Map<String, Object> data) {
	    try {
	        String username = (String) data.get("username");
	        String tablespace = (String) data.get("tablespace");
	        int quota = (Integer) data.get("dataQuota");
	        userService.submitAccount(username, tablespace, quota);
	        return ResponseEntity.ok(Collections.singletonMap("message", "Quota đã được cài đặt thành công."));
	    } catch (DataAccessException e) {
	        Throwable rootCause = e.getMostSpecificCause();
	        String errorMessage = rootCause != null ? rootCause.getMessage() : e.getMessage();
	        return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(Collections.singletonMap("error", errorMessage));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity
	            .status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(Collections.singletonMap("error", "Lỗi server không xác định."));
	    }
	}
	@GetMapping("/maxQuota")
	public ResponseEntity<?> getMaxQuota(@RequestParam String tablespaceName) {
	    try {
	        int maxQuotaSize = userService.getTablespaceSize(tablespaceName);
	        if(maxQuotaSize == -1) {
	            return new ResponseEntity<>("Quota không tìm thấy cho tablespace này.", HttpStatus.NOT_FOUND);
	        }
	        return ResponseEntity.ok(maxQuotaSize);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Lỗi khi lấy thông tin max quota size.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}
