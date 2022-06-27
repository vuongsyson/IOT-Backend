package com.sonpj.repository;

import com.sonpj.domain.VehicleState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the VehicleState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleStateRepository extends ReactiveCrudRepository<VehicleState, Long>, VehicleStateRepositoryInternal {
    @Override
    <S extends VehicleState> Mono<S> save(S entity);

    @Override
    Flux<VehicleState> findAll();

    @Override
    Mono<VehicleState> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface VehicleStateRepositoryInternal {
    <S extends VehicleState> Mono<S> save(S entity);

    Flux<VehicleState> findAllBy(Pageable pageable);

    Flux<VehicleState> findAll();

    Mono<VehicleState> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<VehicleState> findAllBy(Pageable pageable, Criteria criteria);

}
