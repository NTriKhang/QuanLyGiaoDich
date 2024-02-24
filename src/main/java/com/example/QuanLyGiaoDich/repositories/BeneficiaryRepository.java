package com.example.QuanLyGiaoDich.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.QuanLyGiaoDich.models.Beneficiary;
@Repository
public interface BeneficiaryRepository  extends JpaRepository<Beneficiary, Long> {

}
