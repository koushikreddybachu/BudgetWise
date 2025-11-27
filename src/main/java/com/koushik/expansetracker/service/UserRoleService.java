package com.koushik.expansetracker.service;

import com.koushik.expansetracker.dto.AssignRoleRequest;
import com.koushik.expansetracker.entity.UserRole;
import com.koushik.expansetracker.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRole assignRole(AssignRoleRequest request) {

        UserRole ur = UserRole.builder()
                .userId(request.getUserId())
                .roleId(request.getRoleId())
                .build();

        return userRoleRepository.save(ur);
    }
}
