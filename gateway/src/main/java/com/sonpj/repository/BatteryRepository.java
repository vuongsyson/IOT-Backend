package com.sonpj.repository;

import com.sonpj.domain.Battery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Battery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatteryRepository extends ReactiveCrudRepository<Battery, Long>, BatteryRepositoryInternal {
    @Query("SELECT * FROM battery entity WHERE entity.rental_history_id = :id")
    Flux<Battery> findByRentalHistory(Long id);

    @Query("SELECT * FROM battery entity WHERE entity.rental_history_id IS NULL")
    Flux<Battery> findAllWhereRentalHistoryIsNull();

    @Override
    <S extends Battery> Mono<S> save(S entity);

    @Override
    Flux<Battery> findAll();

    @Override
    Mono<Battery> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BatteryRepositoryInternal {
    <S extends Battery> Mono<S> save(S entity);

    Flux<Battery> findAllBy(Pageable pageable);

    Flux<Battery> findAll();

    Mono<Battery> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Battery> findAllBy(Pageable pageable, Criteria criteria);

}
