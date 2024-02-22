package com.example.QuanLyGiaoDich.controller;

import com.example.QuanLyGiaoDich.TableSpace.TablespaceService;
import com.example.QuanLyGiaoDich.models.TablespaceInfo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/")
public class TablespaceController {

	@Autowired
    private TablespaceService tablespaceService;

    @GetMapping("/tablespace")
    public String home(Model model) {
        List<TablespaceInfo> tablespaces = tablespaceService.getTablespaceInfo();
        model.addAttribute("tablespaces", tablespaces);
        return "tablespace";
    }
    @PostMapping("/api/create-tablespace")
    public ResponseEntity<String> createTablespace(@RequestBody TablespaceInfo tablespaceInfo) {
        try {
            tablespaceService.createTablespace(tablespaceInfo.getTablespaceName(), tablespaceInfo.getFileName(), tablespaceInfo.getSize());
            return ResponseEntity.ok("Tablespace created successfully");
        } catch (Exception e) {
        	  System.err.println("Lỗi xảy ra: " + e.getMessage());
        	    e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating tablespace: " + e.getMessage());
        }
    }
    @GetMapping("/api/tablespaces/{username}")
    public ResponseEntity<List<TablespaceInfo>> getUserTablespaces(@PathVariable String username) {
        try {
            List<TablespaceInfo> tablespaces = tablespaceService.getUserTablespaces(username);
            return ResponseEntity.ok(tablespaces);
        } catch (Exception e) {
        	 System.err.println("Lỗi xảy ra: " + e.getMessage());
     	    e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/api/add-datafile-to-tablespace")
    public ResponseEntity<?> addDatafileToTablespace(@RequestBody TablespaceInfo tablespaceInfo) {
        try {
            tablespaceService.addDatafileToTablespace(
                tablespaceInfo.getTablespaceName(),
                tablespaceInfo.getFileName(),
                tablespaceInfo.getSize() 
            );
            return new ResponseEntity<>("Datafile added successfully", HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Error adding datafile: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}