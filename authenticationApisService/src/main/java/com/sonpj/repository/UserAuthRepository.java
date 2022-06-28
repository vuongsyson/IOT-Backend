package com.sonpj.repository;

import com.sonpj.domain.UserAuth;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserAuth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {}
