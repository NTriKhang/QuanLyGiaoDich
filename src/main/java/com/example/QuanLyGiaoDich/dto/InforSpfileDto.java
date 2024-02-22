package com.example.QuanLyGiaoDich.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InforSpfileDto {
	@Id
	private Long id;
	
	public String value;
	public String name;
}
