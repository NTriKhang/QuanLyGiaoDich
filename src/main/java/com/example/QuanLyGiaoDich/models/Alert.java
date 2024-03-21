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
    public Long getAlertID() {
        return alertID;
    }

    public void setAlertID(Long alertID) {
        this.alertID = alertID;
    }
    
    public String message;
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Column(name = "CREATEDDATE")
    public Timestamp createdDate;
    
    @Column(name = "ISPROCESSED")
    private Boolean isProcessed = Boolean.FALSE;
    public boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }   
}  