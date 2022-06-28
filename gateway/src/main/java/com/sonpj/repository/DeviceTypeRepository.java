package com.sonpj.repository;

import com.sonpj.domain.DeviceType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the DeviceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceTypeRepository extends ReactiveCrudRepository<DeviceType, Long>, DeviceTypeRepositoryInternal {
    @Override
    <S extends DeviceType> Mono<S> save(S entity);

    @Override
    Flux<DeviceType> findAll();

    @Override
    Mono<DeviceType> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DeviceTypeRepositoryInternal {
    <S extends DeviceType> Mono<S> save(S entity);

    Flux<DeviceType> findAllBy(Pageable pageable);

    Flux<DeviceType> findAll();

    Mono<DeviceType> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DeviceType> findAllBy(Pageable pageable, Criteria criteria);

}
