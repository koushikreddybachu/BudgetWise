package com.koushik.expansetracker.repository;

import com.koushik.expansetracker.entity.UserScreenPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserScreenPermissionRepository extends JpaRepository<UserScreenPermission, Long> {
    List<UserScreenPermission> findByUserId(Long userId);
}
