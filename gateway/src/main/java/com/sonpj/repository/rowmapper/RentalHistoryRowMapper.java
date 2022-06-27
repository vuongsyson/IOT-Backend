package com.sonpj.repository.rowmapper;

import com.sonpj.domain.RentalHistory;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RentalHistory}, with proper type conversions.
 */
@Service
public class RentalHistoryRowMapper implements BiFunction<Row, String, RentalHistory> {

    private final ColumnConverter converter;

    public RentalHistoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RentalHistory} stored in the database.
     */
    @Override
    public RentalHistory apply(Row row, String prefix) {
        RentalHistory entity = new RentalHistory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setBatteryId(converter.fromRow(row, prefix + "_battery_id", Long.class));
        entity.setTimeStart(converter.fromRow(row, prefix + "_time_start", Instant.class));
        entity.setTimeEnd(converter.fromRow(row, prefix + "_time_end", Instant.class));
        return entity;
    }
}
