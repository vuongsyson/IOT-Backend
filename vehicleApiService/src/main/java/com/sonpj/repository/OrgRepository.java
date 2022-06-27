package com.sonpj.repository;

import com.sonpj.domain.Org;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Org entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgRepository extends JpaRepository<Org, Long> {}
