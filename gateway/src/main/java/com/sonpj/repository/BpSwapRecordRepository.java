package com.sonpj.repository;

import com.sonpj.domain.BpSwapRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the BpSwapRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BpSwapRecordRepository extends ReactiveCrudRepository<BpSwapRecord, Long>, BpSwapRecordRepositoryInternal {
    @Override
    <S extends BpSwapRecord> Mono<S> save(S entity);

    @Override
    Flux<BpSwapRecord> findAll();

    @Override
    Mono<BpSwapRecord> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BpSwapRecordRepositoryInternal {
    <S extends BpSwapRecord> Mono<S> save(S entity);

    Flux<BpSwapRecord> findAllBy(Pageable pageable);

    Flux<BpSwapRecord> findAll();

    Mono<BpSwapRecord> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<BpSwapRecord> findAllBy(Pageable pageable, Criteria criteria);

}
