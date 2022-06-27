package com.sonpj.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.sonpj.domain.UserAuth;
import com.sonpj.repository.rowmapper.UserAuthRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the UserAuth entity.
 */
@SuppressWarnings("unused")
class UserAuthRepositoryInternalImpl extends SimpleR2dbcRepository<UserAuth, Long> implements UserAuthRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserAuthRowMapper userauthMapper;

    private static final Table entityTable = Table.aliased("user_auth", EntityManager.ENTITY_ALIAS);

    public UserAuthRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserAuthRowMapper userauthMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserAuth.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userauthMapper = userauthMapper;
    }

    @Override
    public Flux<UserAuth> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserAuth> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserAuthSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserAuth.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserAuth> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserAuth> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private UserAuth process(Row row, RowMetadata metadata) {
        UserAuth entity = userauthMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends UserAuth> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
