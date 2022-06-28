package com.sonpj.repository;

import com.sonpj.domain.RefreshToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RefreshToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {}
