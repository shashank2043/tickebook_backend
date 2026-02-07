package org.team11.tickebook.auth_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team11.tickebook.auth_service.model.enums.Role;
import org.team11.tickebook.auth_service.model.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    //    boolean existsByRole(Role role);
    boolean existsByRolesContains(Role role);
}
