package com.koushik.expansetracker.controller;

import com.koushik.expansetracker.dto.CreateRoleRequest;
import com.koushik.expansetracker.entity.Role;
import com.koushik.expansetracker.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequest request) {
        Role savedRole = roleService.createRole(request);
        return ResponseEntity.ok(savedRole);
    }
}
