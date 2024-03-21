package com.example.QuanLyGiaoDich.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyGiaoDich.Services.AlertService;
import com.example.QuanLyGiaoDich.models.Alert;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/latest-unprocessed")
    public ResponseEntity<?> getLatestUnprocessedAlert() {
        try {
            Alert latestAlert = alertService.getLatestUnprocessedAlert();
            if (latestAlert != null) {
                return ResponseEntity.ok(latestAlert);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching the latest unprocessed alert");
        }
    }
    @PutMapping("/mark-processed/{alertId}")
    public ResponseEntity<?> markAlertAsProcessed(@PathVariable Long alertId) {
        try {
            alertService.markAlertAsProcessed(alertId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
        }
    }
    @GetMapping("/alerts/{username}")
    public ResponseEntity<List<Map<String, Object>>> getUserAlerts(@PathVariable String username) {
        List<Map<String, Object>> alerts = alertService.getUserAlerts(username);
        if(alerts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(alerts);
    }
}