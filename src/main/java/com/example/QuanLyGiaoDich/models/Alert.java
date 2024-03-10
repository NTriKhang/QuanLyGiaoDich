package com.example.QuanLyGiaoDich.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alertID;
    
    public String message;
    @Column(name = "CREATEDDATE")
    public Timestamp createdDate;
      
}  