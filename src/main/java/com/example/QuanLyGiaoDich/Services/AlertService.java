package com.example.QuanLyGiaoDich.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.QuanLyGiaoDich.models.Alert;
import com.example.QuanLyGiaoDich.repositories.AlertRepository;

@Service
public class AlertService {
	private final AlertRepository alertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }
    public Alert getLatestAlert() {
        return alertRepository.findTopByOrderByCreatedDateDesc();
    }
}
