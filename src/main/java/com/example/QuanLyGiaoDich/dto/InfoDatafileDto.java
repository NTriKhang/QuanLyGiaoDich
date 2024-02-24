package com.example.QuanLyGiaoDich.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InfoDatafileDto {
	@Id
	private Long id;
	
	public String file_name;
	public String file_id;
	public String tablespace_name;
	public String bytes;
	public String status;
}
