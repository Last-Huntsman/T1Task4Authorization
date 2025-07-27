package org.zuzukov.t1task4.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zuzukov.t1task4.entity.RevokedToken;


import java.util.Optional;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, String> {
    Optional<RevokedToken> findByToken(String token);

    boolean existsByToken(String tokenHash);
}
