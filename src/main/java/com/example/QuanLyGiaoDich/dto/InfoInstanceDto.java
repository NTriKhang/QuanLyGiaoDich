package com.example.QuanLyGiaoDich.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InfoInstanceDto {
	@Id
	private Long id;
	
	public String instance_number;
	public String instance_name;
	public String version;
	public String startup_time;
	public String status;
	public String database_status;
}
