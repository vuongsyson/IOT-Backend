package com.sonpj.repository.rowmapper;

import com.sonpj.domain.DeviceType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DeviceType}, with proper type conversions.
 */
@Service
public class DeviceTypeRowMapper implements BiFunction<Row, String, DeviceType> {

    private final ColumnConverter converter;

    public DeviceTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DeviceType} stored in the database.
     */
    @Override
    public DeviceType apply(Row row, String prefix) {
        DeviceType entity = new DeviceType();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
