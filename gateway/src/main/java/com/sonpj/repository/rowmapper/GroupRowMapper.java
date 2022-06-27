package com.sonpj.repository.rowmapper;

import com.sonpj.domain.Group;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Group}, with proper type conversions.
 */
@Service
public class GroupRowMapper implements BiFunction<Row, String, Group> {

    private final ColumnConverter converter;

    public GroupRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Group} stored in the database.
     */
    @Override
    public Group apply(Row row, String prefix) {
        Group entity = new Group();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
