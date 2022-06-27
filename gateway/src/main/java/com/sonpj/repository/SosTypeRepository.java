package com.sonpj.repository;

import com.sonpj.domain.SosType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the SosType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SosTypeRepository extends ReactiveCrudRepository<SosType, Long>, SosTypeRepositoryInternal {
    @Override
    <S extends SosType> Mono<S> save(S entity);

    @Override
    Flux<SosType> findAll();

    @Override
    Mono<SosType> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SosTypeRepositoryInternal {
    <S extends SosType> Mono<S> save(S entity);

    Flux<SosType> findAllBy(Pageable pageable);

    Flux<SosType> findAll();

    Mono<SosType> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SosType> findAllBy(Pageable pageable, Criteria criteria);

}
