package com.example.QuanLyGiaoDich.dto;

public class RoleDetailObjectDto {
	public String TableName;
	public String Privilege;
	public RoleDetailObjectDto(String tableName, String privilege) {
		super();
		TableName = tableName;
		Privilege = privilege;
	}
}
