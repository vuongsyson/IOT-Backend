package com.sonpj.repository;

import com.sonpj.domain.UserAuth;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the UserAuth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAuthRepository extends ReactiveCrudRepository<UserAuth, Long>, UserAuthRepositoryInternal {
    @Override
    <S extends UserAuth> Mono<S> save(S entity);

    @Override
    Flux<UserAuth> findAll();

    @Override
    Mono<UserAuth> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserAuthRepositoryInternal {
    <S extends UserAuth> Mono<S> save(S entity);

    Flux<UserAuth> findAllBy(Pageable pageable);

    Flux<UserAuth> findAll();

    Mono<UserAuth> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserAuth> findAllBy(Pageable pageable, Criteria criteria);

}
