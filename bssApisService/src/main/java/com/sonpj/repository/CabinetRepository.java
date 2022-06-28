package com.sonpj.repository;

import com.sonpj.domain.Cabinet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cabinet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, Long> {}
