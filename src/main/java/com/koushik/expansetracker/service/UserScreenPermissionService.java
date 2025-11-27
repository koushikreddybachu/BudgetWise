package com.koushik.expansetracker.service;

import com.koushik.expansetracker.dto.UserScreenPermissionRequest;
import com.koushik.expansetracker.entity.UserScreenPermission;
import com.koushik.expansetracker.repository.UserScreenPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserScreenPermissionService {

    @Autowired
    private UserScreenPermissionRepository userScreenPermissionRepository;

    public UserScreenPermission assignUserPermission(UserScreenPermissionRequest request) {

        UserScreenPermission permission = UserScreenPermission.builder()
                .userId(request.getUserId())
                .screenId(request.getScreenId())
                .canView(request.isCanView())
                .canEdit(request.isCanEdit())
                .canDelete(request.isCanDelete())
                .build();

        return userScreenPermissionRepository.save(permission);
    }
}
