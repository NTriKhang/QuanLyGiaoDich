package com.example.QuanLyGiaoDich.repositories;

import java.security.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.QuanLyGiaoDich.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	/*
	 * List<Transaction> findBySenderID(String senderUserID); List<Transaction>
	 * findByRecipientUserID(String recipientUserID); List<Transaction>
	 * findByTransactionType(String transactionType); List<Transaction>
	 * findByTransactionDate(Timestamp transactionDate);
	 */
}
