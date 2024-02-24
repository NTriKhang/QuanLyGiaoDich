package com.example.QuanLyGiaoDich.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.QuanLyGiaoDich.models.Transaction;
import com.example.QuanLyGiaoDich.repositories.TransactionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Endpoint to get all transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Endpoint to get a transaction by ID
    @GetMapping("/{transactionID}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long transactionID) {
        return transactionRepository.findById(transactionID)
                .map(transaction -> new ResponseEntity<>(transaction, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to create a new transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionRepository.save(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
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
