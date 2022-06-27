package com.sonpj.repository.rowmapper;

import com.sonpj.domain.SosType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SosType}, with proper type conversions.
 */
@Service
public class SosTypeRowMapper implements BiFunction<Row, String, SosType> {

    private final ColumnConverter converter;

    public SosTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SosType} stored in the database.
     */
    @Override
    public SosType apply(Row row, String prefix) {
        SosType entity = new SosType();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
