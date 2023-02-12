package com.midlaj.apiGatewayUser.repository;

import com.midlaj.apiGatewayUser.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
