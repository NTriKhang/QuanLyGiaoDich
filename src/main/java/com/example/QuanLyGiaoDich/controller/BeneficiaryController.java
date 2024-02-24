package com.example.QuanLyGiaoDich.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.QuanLyGiaoDich.models.Beneficiary;
import com.example.QuanLyGiaoDich.repositories.BeneficiaryRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryRepository beneficiaryRepository;

    @Autowired
    public BeneficiaryController(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    // Endpoint to get all beneficiaries
    @GetMapping
    public ResponseEntity<List<Beneficiary>> getAllBeneficiaries() {
        List<Beneficiary> beneficiaries = beneficiaryRepository.findAll();
        return new ResponseEntity<>(beneficiaries, HttpStatus.OK);
    }

    // Endpoint to get a beneficiary by ID
    @GetMapping("/{beneficiaryID}")
    public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable Long beneficiaryID) {
        return beneficiaryRepository.findById(beneficiaryID)
                .map(beneficiary -> new ResponseEntity<>(beneficiary, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to create a new beneficiary
    @PostMapping
    public ResponseEntity<Beneficiary> createBeneficiary(@RequestBody Beneficiary beneficiary) {
        Beneficiary createdBeneficiary = beneficiaryRepository.save(beneficiary);
        return new ResponseEntity<>(createdBeneficiary, HttpStatus.CREATED);
    }

    // Endpoint to update an existing beneficiary
    @PutMapping("/{beneficiaryID}")
    public ResponseEntity<Beneficiary> updateBeneficiary(@PathVariable Long beneficiaryID, @RequestBody Beneficiary beneficiary) {
        if (beneficiaryRepository.existsById(beneficiaryID)) {
            beneficiary.setBeneficiaryID(beneficiaryID); // Set the ID to ensure it's updated
            Beneficiary updatedBeneficiary = beneficiaryRepository.save(beneficiary);
            return new ResponseEntity<>(updatedBeneficiary, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete a beneficiary by ID
    @DeleteMapping("/{beneficiaryID}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long beneficiaryID) {
        if (beneficiaryRepository.existsById(beneficiaryID)) {
            beneficiaryRepository.deleteById(beneficiaryID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
