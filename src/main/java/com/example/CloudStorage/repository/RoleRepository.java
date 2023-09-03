package com.example.CloudStorage.repository;

import com.example.CloudStorage.entity.Role;
import com.example.CloudStorage.model.EnumRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(EnumRoles name);
}
