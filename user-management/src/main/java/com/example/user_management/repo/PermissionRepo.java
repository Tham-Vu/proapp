package com.example.user_management.repo;

import com.example.user_management.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {
    Permission save(Permission newPermission);
}
