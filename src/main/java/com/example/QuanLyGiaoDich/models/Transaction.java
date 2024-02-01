package com.example.QuanLyGiaoDich.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTIONID")
    private Long transactionID;

    @Column(name = "SENDERUSERID", length = 50)
    private String senderUserID;

    @Column(name = "RECIPIENTUSERID", length = 50)
    private String recipientUserID;

    @Column(name = "TRANSACTIONTYPE", length = 20)
    private String transactionType;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "TRANSACTIONDATE")
    private Timestamp transactionDate;

    @ManyToOne
    @JoinColumn(name = "SENDERUSERID", referencedColumnName = "USERID", insertable = false, updatable = false)
    private Users senderUser;

    @ManyToOne
    @JoinColumn(name = "RECIPIENTUSERID", referencedColumnName = "USERID", insertable = false, updatable = false)
    private Users recipientUser;

    // Constructors, getters, and setters...

    // Default constructor
    public Transaction() {
    }

    // Parameterized constructor
    public Transaction(String senderUserID, String recipientUserID, String transactionType, Double amount,
                       Timestamp transactionDate) {
        this.senderUserID = senderUserID;
        this.recipientUserID = recipientUserID;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

	public Long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(Long transactionID) {
		this.transactionID = transactionID;
	}

	public String getSenderUserID() {
		return senderUserID;
	}

	public void setSenderUserID(String senderUserID) {
		this.senderUserID = senderUserID;
	}

	public String getRecipientUserID() {
		return recipientUserID;
	}

	public void setRecipientUserID(String recipientUserID) {
		this.recipientUserID = recipientUserID;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Users getSenderUser() {
		return senderUser;
	}

	public void setSenderUser(Users senderUser) {
		this.senderUser = senderUser;
	}

	public Users getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUser(Users recipientUser) {
		this.recipientUser = recipientUser;
	}

    // Getters and setters...
    
}

