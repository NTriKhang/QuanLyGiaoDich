package com.example.QuanLyGiaoDich.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "USERS")
public class Users {

    @Id
    @Column(name = "USERID", length = 50)
    private String userID;

    @Column(name = "FIRSTNAME", length = 50)
    private String firstName;

    @Column(name = "LASTNAME", length = 50)
    private String lastName;

    @Column(name = "ADDRESS", length = 100)
    private String address;

    @Column(name = "PHONE", length = 15)
    private String phone;

    @Column(name = "EMAIL", length = 50, unique = true)
    private String email;

    @Column(name = "USERNAME", length = 50)
    private String userName;

    @Column(name = "PASSWORD", length = 50)
    private String password;

    @Lob
    @Column(name = "IMAGEPROFILE")
    private byte[] imageProfile;

    @Column(name = "MONEY")
    private Double money;

    @Column(name = "CREATEDDATE")
    private Timestamp createdDate;

    @Column(name = "LASTLOGIN")
    private Timestamp lastLogin;

    // Constructors, getters, and setters...

    // Default constructor
    public Users() {
    }

    // Parameterized constructor
    public Users(String userID, String firstName, String lastName, String address, String phone, String email,
                String userName, String password, byte[] imageProfile, Double money, Timestamp createdDate,
                Timestamp lastLogin) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.imageProfile = imageProfile;
        this.money = money;
        this.createdDate = createdDate;
        this.lastLogin = lastLogin;
    }

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getImageProfile() {
		return imageProfile;
	}

	public void setImageProfile(byte[] imageProfile) {
		this.imageProfile = imageProfile;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

    // Getters and setters...
    
}
