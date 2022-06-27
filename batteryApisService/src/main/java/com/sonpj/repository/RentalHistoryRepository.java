package com.sonpj.repository;

import com.sonpj.domain.RentalHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RentalHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {}
