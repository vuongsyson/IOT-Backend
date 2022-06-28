package com.sonpj.repository;

import com.sonpj.domain.Cabinet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Cabinet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CabinetRepository extends ReactiveCrudRepository<Cabinet, Long>, CabinetRepositoryInternal {
    @Override
    <S extends Cabinet> Mono<S> save(S entity);

    @Override
    Flux<Cabinet> findAll();

    @Override
    Mono<Cabinet> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CabinetRepositoryInternal {
    <S extends Cabinet> Mono<S> save(S entity);

    Flux<Cabinet> findAllBy(Pageable pageable);

    Flux<Cabinet> findAll();

    Mono<Cabinet> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Cabinet> findAllBy(Pageable pageable, Criteria criteria);

}
