package com.sonpj.repository;

import com.sonpj.domain.RentalHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the RentalHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalHistoryRepository extends ReactiveCrudRepository<RentalHistory, Long>, RentalHistoryRepositoryInternal {
    @Override
    <S extends RentalHistory> Mono<S> save(S entity);

    @Override
    Flux<RentalHistory> findAll();

    @Override
    Mono<RentalHistory> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RentalHistoryRepositoryInternal {
    <S extends RentalHistory> Mono<S> save(S entity);

    Flux<RentalHistory> findAllBy(Pageable pageable);

    Flux<RentalHistory> findAll();

    Mono<RentalHistory> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RentalHistory> findAllBy(Pageable pageable, Criteria criteria);

}
