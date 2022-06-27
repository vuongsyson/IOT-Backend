package com.sonpj.repository;

import com.sonpj.domain.BatteryState;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BatteryState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatteryStateRepository extends JpaRepository<BatteryState, Long> {}
