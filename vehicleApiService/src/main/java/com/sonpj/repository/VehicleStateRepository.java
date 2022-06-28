package com.sonpj.repository;

import com.sonpj.domain.VehicleState;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VehicleState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleStateRepository extends JpaRepository<VehicleState, Long> {}
