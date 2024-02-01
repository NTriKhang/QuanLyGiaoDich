package com.example.QuanLyGiaoDich.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.QuanLyGiaoDich.models.Users;

public interface UsersRepository extends JpaRepository<Users, String> {
	
	/*
	 * Users findByEmail(String email); Users findByPhone(String phone);
	 */
	
}
