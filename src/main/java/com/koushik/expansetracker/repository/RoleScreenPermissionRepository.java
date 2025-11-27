package com.koushik.expansetracker.repository;

import com.koushik.expansetracker.entity.RoleScreenPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleScreenPermissionRepository extends JpaRepository<RoleScreenPermission, Long> {
    List<RoleScreenPermission> findByRoleId(Long roleId);
}

