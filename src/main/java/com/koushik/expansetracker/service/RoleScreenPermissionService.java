package com.koushik.expansetracker.service;

import com.koushik.expansetracker.dto.RoleScreenPermissionRequest;
import com.koushik.expansetracker.entity.RoleScreenPermission;
import com.koushik.expansetracker.repository.RoleScreenPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleScreenPermissionService {

    @Autowired
    private RoleScreenPermissionRepository roleScreenPermissionRepository;

    public RoleScreenPermission assignPermission(RoleScreenPermissionRequest request) {

        RoleScreenPermission permission = RoleScreenPermission.builder()
                .roleId(request.getRoleId())
                .screenId(request.getScreenId())
                .canView(request.isCanView())
                .canEdit(request.isCanEdit())
                .canDelete(request.isCanDelete())
                .build();

        return roleScreenPermissionRepository.save(permission);
    }
}
