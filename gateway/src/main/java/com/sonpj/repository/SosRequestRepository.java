package com.sonpj.repository;

import com.sonpj.domain.SosRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the SosRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SosRequestRepository extends ReactiveCrudRepository<SosRequest, Long>, SosRequestRepositoryInternal {
    @Override
    <S extends SosRequest> Mono<S> save(S entity);

    @Override
    Flux<SosRequest> findAll();

    @Override
    Mono<SosRequest> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SosRequestRepositoryInternal {
    <S extends SosRequest> Mono<S> save(S entity);

    Flux<SosRequest> findAllBy(Pageable pageable);

    Flux<SosRequest> findAll();

    Mono<SosRequest> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SosRequest> findAllBy(Pageable pageable, Criteria criteria);

}
