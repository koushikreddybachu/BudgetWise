package com.koushik.expansetracker.controller;

import com.koushik.expansetracker.dto.UserPermissionSummary;
import com.koushik.expansetracker.service.PermissionSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
public class PermissionSummaryController {

    private final PermissionSummaryService permissionSummaryService;

    public PermissionSummaryController(PermissionSummaryService permissionSummaryService) {
        this.permissionSummaryService = permissionSummaryService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserPermissionSummary> getPermissions(@PathVariable Long userId) {
        return ResponseEntity.ok(permissionSummaryService.getUserPermissions(userId));
    }
}
