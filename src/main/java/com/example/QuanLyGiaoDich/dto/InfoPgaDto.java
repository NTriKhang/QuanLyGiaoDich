package com.example.QuanLyGiaoDich.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InfoPgaDto {
		@Id
		private Long id;
		
		public String name;
		public String value;
		public String unit;
		public String con_id;

}
