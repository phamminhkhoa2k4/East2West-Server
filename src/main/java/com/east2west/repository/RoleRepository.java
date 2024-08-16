package com.east2west.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.east2west.models.Entity.ERole;
import com.east2west.models.Entity.Role;

public interface RoleRepository  extends JpaRepository<Role, Integer>{
Optional<Role> findByRoleName(ERole roleName);
}
