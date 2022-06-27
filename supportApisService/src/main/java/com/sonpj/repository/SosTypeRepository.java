package com.sonpj.repository;

import com.sonpj.domain.SosType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SosType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SosTypeRepository extends JpaRepository<SosType, Long> {}
