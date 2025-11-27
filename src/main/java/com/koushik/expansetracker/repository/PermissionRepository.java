package com.koushik.expansetracker.repository;

import com.koushik.expansetracker.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
