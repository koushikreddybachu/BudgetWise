package com.koushik.expansetracker.controller;

import com.koushik.expansetracker.dto.RoleScreenPermissionRequest;
import com.koushik.expansetracker.entity.security.RoleScreenPermission;
import com.koushik.expansetracker.service.RoleScreenPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role-screen-permissions")
public class RoleScreenPermissionController {

    @Autowired
    private RoleScreenPermissionService roleScreenPermissionService;

    @PostMapping
    public ResponseEntity<RoleScreenPermission> assignPermission(@RequestBody RoleScreenPermissionRequest request) {
        RoleScreenPermission permission = roleScreenPermissionService.assignPermission(request);
        return ResponseEntity.ok(permission);
    }
}
