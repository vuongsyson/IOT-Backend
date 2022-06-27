package com.sonpj.repository;

import com.sonpj.domain.Group;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Group entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupRepository extends ReactiveCrudRepository<Group, Long>, GroupRepositoryInternal {
    @Override
    <S extends Group> Mono<S> save(S entity);

    @Override
    Flux<Group> findAll();

    @Override
    Mono<Group> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface GroupRepositoryInternal {
    <S extends Group> Mono<S> save(S entity);

    Flux<Group> findAllBy(Pageable pageable);

    Flux<Group> findAll();

    Mono<Group> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Group> findAllBy(Pageable pageable, Criteria criteria);

}
