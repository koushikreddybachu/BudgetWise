package com.koushik.expansetracker.service;

import com.koushik.expansetracker.dto.CreateRoleRequest;
import com.koushik.expansetracker.entity.Role;
import com.koushik.expansetracker.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(CreateRoleRequest request) {

        Role role = Role.builder()
                .roleName(request.getRoleName())
                .roleDescription(request.getRoleDescription())
                .build();

        return roleRepository.save(role);
    }
}
