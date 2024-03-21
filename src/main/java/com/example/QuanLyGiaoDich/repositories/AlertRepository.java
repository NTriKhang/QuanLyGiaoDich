package com.example.QuanLyGiaoDich.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.QuanLyGiaoDich.models.Alert;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {     
    @Query(value = "SELECT * FROM Alert WHERE IsProcessed = 0 AND ROWNUM = 1 ORDER BY CreatedDate DESC", nativeQuery = true)
    Alert findLatestUnprocessedAlert();
}
