package org.zuzukov.t1task4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zuzukov.t1task4.enitty.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserId(UUID id);
    Optional<User> findByEmail(String email);
}
