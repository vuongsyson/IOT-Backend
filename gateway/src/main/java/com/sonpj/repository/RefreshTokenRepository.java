package com.sonpj.repository;

import com.sonpj.domain.RefreshToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the RefreshToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshToken, Long>, RefreshTokenRepositoryInternal {
    @Override
    <S extends RefreshToken> Mono<S> save(S entity);

    @Override
    Flux<RefreshToken> findAll();

    @Override
    Mono<RefreshToken> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RefreshTokenRepositoryInternal {
    <S extends RefreshToken> Mono<S> save(S entity);

    Flux<RefreshToken> findAllBy(Pageable pageable);

    Flux<RefreshToken> findAll();

    Mono<RefreshToken> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RefreshToken> findAllBy(Pageable pageable, Criteria criteria);

}
