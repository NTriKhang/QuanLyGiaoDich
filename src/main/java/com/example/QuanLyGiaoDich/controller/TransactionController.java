 package com.example.QuanLyGiaoDich.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.QuanLyGiaoDich.Services.AlertService;
import com.example.QuanLyGiaoDich.Services.TablespaceService;
import com.example.QuanLyGiaoDich.Services.TransactionService;
import com.example.QuanLyGiaoDich.dto.InfoTransaction;
import com.example.QuanLyGiaoDich.dto.TransactionDto;
import com.example.QuanLyGiaoDich.dto.TransactionResponseDto;
import com.example.QuanLyGiaoDich.dto.UserLoginDto;
import com.example.QuanLyGiaoDich.models.Alert;
import com.example.QuanLyGiaoDich.models.Transaction;
import com.example.QuanLyGiaoDich.repositories.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

	private final TransactionRepository transactionRepository;
	private final TransactionService transactionService;
	private final AlertService alertService;

	@Autowired
	public TransactionController(TransactionRepository transactionRepository, TransactionService transactionService,
			AlertService alertService) {
		this.transactionRepository = transactionRepository;
		this.transactionService = transactionService;
		this.alertService = alertService;
	}

	// Endpoint to get all transactions
	@GetMapping
	public ResponseEntity<List<Transaction>> getAllTransactions() {
		List<Transaction> transactions = transactionRepository.findAll();
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	@GetMapping("/userTransactions/{userName}")
	public ResponseEntity<List<InfoTransaction>> getUserTransactions(@PathVariable("userName") String userName) throws ClassNotFoundException, SQLException {
	    try {
	        List<InfoTransaction> transactions = transactionService.getUserTransactions(userName);
	        if (transactions.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        }
	        return new ResponseEntity<>(transactions, HttpStatus.OK);
	    } catch (DataAccessException e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	// Endpoint to get a transaction by ID
	@GetMapping("/{transactionID}")
	public ResponseEntity<Transaction> getTransactionById(@PathVariable Long transactionID) {
		return transactionRepository.findById(transactionID)
				.map(transaction -> new ResponseEntity<>(transaction, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<?> createTransaction(@RequestBody TransactionDto transactionDto)
			throws ClassNotFoundException, SQLException {
		boolean transactionResult = transactionService.add_transaction(transactionDto);
		
		String alertMessage = null;
		if (transactionResult) {
			Alert latestUnprocessedAlert = alertService.getLatestUnprocessedAlert();
			alertMessage = latestUnprocessedAlert != null ? latestUnprocessedAlert.getMessage() : null;
			TransactionResponseDto response = new TransactionResponseDto(transactionDto, alertMessage);
			return ResponseEntity.ok(response);
		}
		return new ResponseEntity<>(transactionResult, HttpStatus.BAD_REQUEST);
	}

	// Endpoint to update an existing transaction
	/*
	 * @PutMapping("/{transactionID}") public ResponseEntity<Transaction>
	 * updateTransaction(@PathVariable Long transactionID,
	 * 
	 * @RequestBody Transaction transaction) { if
	 * (transactionRepository.existsById(transactionID)) {
	 * transaction.setTransactionID(transactionID); // Set the ID to ensure it's
	 * updated Transaction updatedTransaction =
	 * transactionRepository.save(transaction); return new
	 * ResponseEntity<>(updatedTransaction, HttpStatus.OK); } else { return new
	 * ResponseEntity<>(HttpStatus.NOT_FOUND); } }
	 */
	@PutMapping("/{transactionId}")
	public ResponseEntity<String> updateTransaction(
	    @PathVariable long transactionId,
	    @RequestHeader("UserName") String userName,
	    @RequestBody Map<String, Object> requestBody) {
	    try {
	    	Double newAmount = (Double) requestBody.get("newAmount");
	        String newTransactionType = (String) requestBody.get("newTransactionType");
	        boolean success = transactionService.update_transaction(transactionId, newAmount, newTransactionType, userName);
	        if (success) {
	            return ResponseEntity.ok("Transaction updated successfully.");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No transaction updated.");
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating transaction.");
	    }
	}


	// Endpoint to delete a transaction by ID
	/*
	 * @DeleteMapping("/{transactionID}") public ResponseEntity<Void>
	 * deleteTransaction(@PathVariable Long transactionID) { if
	 * (transactionRepository.existsById(transactionID)) {
	 * transactionRepository.deleteById(transactionID); return new
	 * ResponseEntity<>(HttpStatus.NO_CONTENT); } else { return new
	 * ResponseEntity<>(HttpStatus.NOT_FOUND); } }
	 */
	
	@DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable long transactionId, @RequestHeader("UserName") String userName) {
        try {
            boolean success = transactionService.delete_transaction(transactionId, userName);
            if (success) {
                return ResponseEntity.ok("Transaction deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting transaction.");
        }
    }
}
