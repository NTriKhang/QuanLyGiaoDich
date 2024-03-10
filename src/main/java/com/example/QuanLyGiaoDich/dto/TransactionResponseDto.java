package com.example.QuanLyGiaoDich.dto;

public class TransactionResponseDto {
    private TransactionDto transaction;
    private String warning;

    public TransactionResponseDto(TransactionDto transaction, String warning) {
        this.transaction = transaction;
        this.warning = warning;
    }

    public TransactionResponseDto() {
		// TODO Auto-generated constructor stub
	}

	public TransactionDto getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionDto transaction) {
        this.transaction = transaction;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}
