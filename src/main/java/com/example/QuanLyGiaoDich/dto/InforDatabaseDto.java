package com.example.QuanLyGiaoDich.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InforDatabaseDto {
	@Id
	private Long id;
	
	public String dbid;
	public String name;
	public String created;
	public String open_mode;
	public String log_mode;
	public String controlfile_type;
}
