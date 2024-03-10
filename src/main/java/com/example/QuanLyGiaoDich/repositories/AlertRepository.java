package com.example.QuanLyGiaoDich.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.QuanLyGiaoDich.models.Alert;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    
    @Query("SELECT a FROM Alert a ORDER BY a.createdDate DESC")
    List<Alert> findAllAlertsOrderByCreatedDateDesc();

    Alert findTopByOrderByCreatedDateDesc();
}
