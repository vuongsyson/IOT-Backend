package com.sonpj.repository;

import com.sonpj.domain.BpSwapRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BpSwapRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BpSwapRecordRepository extends JpaRepository<BpSwapRecord, Long> {}
