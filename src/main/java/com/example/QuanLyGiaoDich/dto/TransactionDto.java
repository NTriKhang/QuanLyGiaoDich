package com.example.QuanLyGiaoDich.dto;

public class TransactionDto {
	public String userName;
	 public String recipientUserName;
	public Double amount;
	
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

