package com.sonpj.repository.rowmapper;

import com.sonpj.domain.UserAuth;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserAuth}, with proper type conversions.
 */
@Service
public class UserAuthRowMapper implements BiFunction<Row, String, UserAuth> {

    private final ColumnConverter converter;

    public UserAuthRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserAuth} stored in the database.
     */
    @Override
    public UserAuth apply(Row row, String prefix) {
        UserAuth entity = new UserAuth();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setUsername(converter.fromRow(row, prefix + "_username", String.class));
        entity.setAuthorities(converter.fromRow(row, prefix + "_authorities", String.class));
        entity.setPassword(converter.fromRow(row, prefix + "_password", String.class));
        return entity;
    }
}
