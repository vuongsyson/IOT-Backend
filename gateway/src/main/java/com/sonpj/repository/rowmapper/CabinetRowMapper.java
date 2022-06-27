package com.sonpj.repository.rowmapper;

import com.sonpj.domain.Cabinet;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Cabinet}, with proper type conversions.
 */
@Service
public class CabinetRowMapper implements BiFunction<Row, String, Cabinet> {

    private final ColumnConverter converter;

    public CabinetRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Cabinet} stored in the database.
     */
    @Override
    public Cabinet apply(Row row, String prefix) {
        Cabinet entity = new Cabinet();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setBssId(converter.fromRow(row, prefix + "_bss_id", Long.class));
        entity.setBpId(converter.fromRow(row, prefix + "_bp_id", Long.class));
        entity.setBpReady(converter.fromRow(row, prefix + "_bp_ready", Boolean.class));
        entity.setSwapNo(converter.fromRow(row, prefix + "_swap_no", Long.class));
        entity.setStateCode(converter.fromRow(row, prefix + "_state_code", Integer.class));
        return entity;
    }
}
