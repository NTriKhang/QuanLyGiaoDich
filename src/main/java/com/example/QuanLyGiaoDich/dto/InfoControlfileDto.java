package com.example.QuanLyGiaoDich.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InfoControlfileDto {
	@Id
	private Long id;
	
	public String status;
	public String name;
}
