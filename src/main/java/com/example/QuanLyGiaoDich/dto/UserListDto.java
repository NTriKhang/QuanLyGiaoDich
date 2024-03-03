package com.example.QuanLyGiaoDich.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserListDto {
	@Id
	private Long id;
	
	public String user_id;
	public String username;
	public String account_status;
}
