
package com.example.QuanLyGiaoDich.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyGiaoDich.Services.TablespaceService;
import com.example.QuanLyGiaoDich.models.TablespaceInfo;



@RestController
@RequestMapping("/api/v1/tableSpace")
public class TablespaceController {

	@Autowired
	private TablespaceService tablespaceService;
	

	@GetMapping
	public ResponseEntity<List<TablespaceInfo>> getAll() {
		List<TablespaceInfo> tablespaces = tablespaceService.getTablespaceInfo();
		return ResponseEntity.ok(tablespaces);
	}

	@PostMapping("/create-tablespace")
	public ResponseEntity<String> createTablespace(@RequestBody TablespaceInfo tablespaceInfo) {
			tablespaceService.createTablespace(tablespaceInfo.getTablespaceName(), tablespaceInfo.getFileName(),
					tablespaceInfo.getSize());
			return ResponseEntity.ok("Tablespace tạo thành công");

	}

	@GetMapping("/{username}")
    public ResponseEntity<List<TablespaceInfo>> getUserTablespaces(@PathVariable String username) {
        try {
            List<TablespaceInfo> tablespaces = tablespaceService.getUserTablespacesInfo(username);
            return ResponseEntity.ok(tablespaces);
        } catch (Exception e) {
        	 System.err.println("Lỗi xảy ra: " + e.getMessage());
     	    e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	@GetMapping("/users")
    public ResponseEntity<List<String>> getUsers() {
        try {
            List<String> users = tablespaceService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.err.println("Lỗi xảy ra khi lấy danh sách người dùng: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



	@PostMapping("/add-datafile-to-tablespace")
	public ResponseEntity<?> addDatafileToTablespace(@RequestBody TablespaceInfo tablespaceInfo) {
		try {
			tablespaceService.addDatafileToTablespace(tablespaceInfo.getTablespaceName(), tablespaceInfo.getFileName(),
					tablespaceInfo.getSize());
			return new ResponseEntity<>("Datafile thêm thành công", HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Lỗi xảy ra: " + e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>("Lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/delete")
	public ResponseEntity<?> manageDatafileOrTablespace(@RequestParam String tablespaceName, @RequestParam String datafileName) {

		try {
			tablespaceService.manageDatafileOrTablespace(tablespaceName, datafileName);
			return ResponseEntity.ok(
					"Tablespace " + tablespaceName + " and datafile " + datafileName + " xóa thành công.");
		} catch (DataAccessException e) {
			e.printStackTrace();
			System.err.println("Lỗi xảy ra: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xảy ra: " + e.getMessage());
		}
	}
}
