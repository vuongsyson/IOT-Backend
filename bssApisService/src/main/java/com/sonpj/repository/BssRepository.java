package com.sonpj.repository;

import com.sonpj.domain.Bss;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bss entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BssRepository extends JpaRepository<Bss, Long> {}
