package com.sonpj.repository;

import com.sonpj.domain.SosRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SosRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SosRequestRepository extends JpaRepository<SosRequest, Long> {}
