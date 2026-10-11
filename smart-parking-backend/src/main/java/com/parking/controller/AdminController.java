package com.parking.controller;

import com.parking.dto.AdminDashboardResponse;

import com.parking.service.AdminService;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(
            AdminService adminService) {

        this.adminService =
                adminService;
    }

    // =====================================================
    // ADMIN DASHBOARD
    // =====================================================

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse>
    getDashboard() {

        return ResponseEntity.ok(
                adminService.getDashboard()
        );
    }

    // =====================================================
    // DATE-WISE ADMIN REPORT
    // =====================================================

    @GetMapping("/date-report")
    public ResponseEntity<Map<String, Object>>
    getDateWiseReport(

            @RequestParam("date")
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate date) {

        return ResponseEntity.ok(
                adminService.getDateWiseReport(date)
        );
    }
}
