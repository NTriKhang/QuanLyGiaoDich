package com.example.QuanLyGiaoDich.dto;

import java.sql.Date;

public class InfoTransaction {
	public String transactionID;
	public String senderUser;
	public String recipientUser;
	public String transactionType;
	public double amount;
	public Date transactionDate;
	public byte[] voice;
    
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getSenderUser() {
		return senderUser;
	}
	public void setSenderUser(String senderUser) {
		this.senderUser = senderUser;
	}
	public String getRecipientUser() {
		return recipientUser;
	}
	public void setRecipientUser(String recipientUser) {
		this.recipientUser = recipientUser;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
}
