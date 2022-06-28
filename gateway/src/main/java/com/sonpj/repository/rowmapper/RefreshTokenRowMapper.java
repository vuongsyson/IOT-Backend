package com.sonpj.repository.rowmapper;

import com.sonpj.domain.RefreshToken;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RefreshToken}, with proper type conversions.
 */
@Service
public class RefreshTokenRowMapper implements BiFunction<Row, String, RefreshToken> {

    private final ColumnConverter converter;

    public RefreshTokenRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RefreshToken} stored in the database.
     */
    @Override
    public RefreshToken apply(Row row, String prefix) {
        RefreshToken entity = new RefreshToken();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUsername(converter.fromRow(row, prefix + "_username", String.class));
        return entity;
    }
}
