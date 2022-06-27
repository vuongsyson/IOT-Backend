package com.sonpj.repository;

import com.sonpj.domain.BatteryState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the BatteryState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatteryStateRepository extends ReactiveCrudRepository<BatteryState, Long>, BatteryStateRepositoryInternal {
    @Override
    <S extends BatteryState> Mono<S> save(S entity);

    @Override
    Flux<BatteryState> findAll();

    @Override
    Mono<BatteryState> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BatteryStateRepositoryInternal {
    <S extends BatteryState> Mono<S> save(S entity);

    Flux<BatteryState> findAllBy(Pageable pageable);

    Flux<BatteryState> findAll();

    Mono<BatteryState> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<BatteryState> findAllBy(Pageable pageable, Criteria criteria);

}
