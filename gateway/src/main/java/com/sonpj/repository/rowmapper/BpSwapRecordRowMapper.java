package com.sonpj.repository.rowmapper;

import com.sonpj.domain.BpSwapRecord;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link BpSwapRecord}, with proper type conversions.
 */
@Service
public class BpSwapRecordRowMapper implements BiFunction<Row, String, BpSwapRecord> {

    private final ColumnConverter converter;

    public BpSwapRecordRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link BpSwapRecord} stored in the database.
     */
    @Override
    public BpSwapRecord apply(Row row, String prefix) {
        BpSwapRecord entity = new BpSwapRecord();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setOldBat(converter.fromRow(row, prefix + "_old_bat", String.class));
        entity.setNewBat(converter.fromRow(row, prefix + "_new_bat", String.class));
        entity.setOldCab(converter.fromRow(row, prefix + "_old_cab", String.class));
        entity.setNewCab(converter.fromRow(row, prefix + "_new_cab", String.class));
        entity.setBss(converter.fromRow(row, prefix + "_bss", String.class));
        entity.setUser(converter.fromRow(row, prefix + "_user", Long.class));
        entity.setState(converter.fromRow(row, prefix + "_state", Integer.class));
        entity.setError(converter.fromRow(row, prefix + "_error", Integer.class));
        return entity;
    }
}
