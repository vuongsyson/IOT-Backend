package com.sonpj.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.sonpj.domain.Battery;
import com.sonpj.repository.rowmapper.BatteryRowMapper;
import com.sonpj.repository.rowmapper.RentalHistoryRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Battery entity.
 */
@SuppressWarnings("unused")
class BatteryRepositoryInternalImpl extends SimpleR2dbcRepository<Battery, Long> implements BatteryRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RentalHistoryRowMapper rentalhistoryMapper;
    private final BatteryRowMapper batteryMapper;

    private static final Table entityTable = Table.aliased("battery", EntityManager.ENTITY_ALIAS);
    private static final Table rentalHistoryTable = Table.aliased("rental_history", "rentalHistory");

    public BatteryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RentalHistoryRowMapper rentalhistoryMapper,
        BatteryRowMapper batteryMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Battery.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.rentalhistoryMapper = rentalhistoryMapper;
        this.batteryMapper = batteryMapper;
    }

    @Override
    public Flux<Battery> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Battery> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = BatterySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(RentalHistorySqlHelper.getColumns(rentalHistoryTable, "rentalHistory"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(rentalHistoryTable)
            .on(Column.create("rental_history_id", entityTable))
            .equals(Column.create("id", rentalHistoryTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Battery.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Battery> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Battery> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Battery process(Row row, RowMetadata metadata) {
        Battery entity = batteryMapper.apply(row, "e");
        entity.setRentalHistory(rentalhistoryMapper.apply(row, "rentalHistory"));
        return entity;
    }

    @Override
    public <S extends Battery> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
