package com.sonpj.repository;

import com.sonpj.domain.DeviceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeviceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceTypeRepository extends JpaRepository<DeviceType, Long> {}
