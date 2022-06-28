package com.sonpj.repository.rowmapper;

import com.sonpj.domain.SosRequest;
import com.sonpj.domain.enumeration.SosState;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SosRequest}, with proper type conversions.
 */
@Service
public class SosRequestRowMapper implements BiFunction<Row, String, SosRequest> {

    private final ColumnConverter converter;

    public SosRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SosRequest} stored in the database.
     */
    @Override
    public SosRequest apply(Row row, String prefix) {
        SosRequest entity = new SosRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setDeviceSerialNumber(converter.fromRow(row, prefix + "_device_serial_number", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setImage(converter.fromRow(row, prefix + "_image", String.class));
        entity.setState(converter.fromRow(row, prefix + "_state", SosState.class));
        entity.setRating(converter.fromRow(row, prefix + "_rating", Integer.class));
        entity.setDone(converter.fromRow(row, prefix + "_done", Boolean.class));
        entity.setDoneTime(converter.fromRow(row, prefix + "_done_time", Instant.class));
        return entity;
    }
}
