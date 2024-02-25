package com.example.QuanLyGiaoDich.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.stereotype.Repository;

import com.example.QuanLyGiaoDich.models.Users;
@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
	
	/*
	 * Users findByEmail(String email); Users findByPhone(String phone);
	 */
	@Procedure(procedureName = "create_new_application_user")
    int createNewApplicationUser(
            @Param("p_user_id") String userId,
            @Param("p_first_name") String firstName,
            @Param("p_last_name") String lastName,
            @Param("p_address") String address,
            @Param("p_phone") String phone,
            @Param("p_email") String email,
            @Param("p_user_name") String userName,
            @Param("p_password") String password,
            @Param("p_image_profile") byte[] imageProfile
    );
	@Procedure(procedureName = "check_user_signin")
    int checkUserSignin(@Param("username_in") String username);
	
	@Procedure(procedureName = "logout")
	int logout(@Param("username_in") String username);
}
