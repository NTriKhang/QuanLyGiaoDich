package com.example.QuanLyGiaoDich.dto;

import java.sql.Date;

import jakarta.persistence.Entity;

public class UserDetailAdminDto {
	public String user_id;
	public String username;
	public String created;
	public String expiry_date;
	public String account_status;
	public String last_login;
	public String profile;
	
	public UserDetailAdminDto() {
		super();
		this.user_id = "";
		this.username = "";
		this.created = "";
		this.expiry_date = "";
		this.account_status = "";
		this.last_login = "";
	}
	public UserDetailAdminDto(String user_id, String username, String created, String expiry_date,
			String account_status, String last_login) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.created = created;
		this.expiry_date = expiry_date;
		this.account_status = account_status;
		this.last_login = last_login;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(String date) {
		this.expiry_date = date;
	}
	public String getAccount_status() {
		return account_status;
	}
	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}
	public String getLast_login() {
		return last_login;
	}
	public void setLast_login(String date) {
		this.last_login = date;
	}
	public void setProfile(String profile) {
		// TODO Auto-generated method stub
		this.profile = profile;
	}
}
