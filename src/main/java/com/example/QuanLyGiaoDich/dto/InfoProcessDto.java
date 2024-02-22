package com.example.QuanLyGiaoDich.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InfoProcessDto {
	@Id
	private Long id;
	
	public String pid;
	public String spid;
	public String program;
}
