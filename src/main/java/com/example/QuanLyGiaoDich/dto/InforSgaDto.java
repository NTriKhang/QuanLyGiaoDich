package com.example.QuanLyGiaoDich.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InforSgaDto {
	@Id
	private Long id;
	
	public String name;
	public String value;
	public String con_id;
}
