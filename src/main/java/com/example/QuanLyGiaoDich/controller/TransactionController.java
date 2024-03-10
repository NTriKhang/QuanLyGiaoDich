package com.example.QuanLyGiaoDich.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.QuanLyGiaoDich.Services.TransactionService;
import com.example.QuanLyGiaoDich.dto.TransactionDto;
import com.example.QuanLyGiaoDich.dto.UserLoginDto;
import com.example.QuanLyGiaoDich.models.Transaction;
import com.example.QuanLyGiaoDich.repositories.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository, TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    // Endpoint to get all transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Endpoint to get a transaction by ID
    @GetMapping("/{transactionID}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long transactionID)  {
        return transactionRepository.findById(transactionID)
                .map(transaction -> new ResponseEntity<>(transaction, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to create a new transaction
    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody String transaction) throws ClassNotFoundException, SQLException, JsonProcessingException {
    	try {
    		ObjectMapper mapper = new ObjectMapper();
        	TransactionDto createdTransaction = mapper.readValue(transaction, TransactionDto.class);
        	boolean res = transactionService.add_transaction(createdTransaction);
        	if(res == false)
        		return new ResponseEntity<>(createdTransaction, HttpStatus.CONFLICT);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<TransactionDto>(HttpStatus.BAD_REQUEST);
		}
    }

    // Endpoint to update an existing transaction
    @PutMapping("/{transactionID}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long transactionID, @RequestBody Transaction transaction) {
        if (transactionRepository.existsById(transactionID)) {
            transaction.setTransactionID(transactionID); // Set the ID to ensure it's updated
            Transaction updatedTransaction = transactionRepository.save(transaction);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete a transaction by ID
    @DeleteMapping("/{transactionID}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionID) {
        if (transactionRepository.existsById(transactionID)) {
            transactionRepository.deleteById(transactionID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
