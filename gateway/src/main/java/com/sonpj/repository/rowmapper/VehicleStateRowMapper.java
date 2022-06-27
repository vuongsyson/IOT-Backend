package com.sonpj.repository.rowmapper;

import com.sonpj.domain.VehicleState;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link VehicleState}, with proper type conversions.
 */
@Service
public class VehicleStateRowMapper implements BiFunction<Row, String, VehicleState> {

    private final ColumnConverter converter;

    public VehicleStateRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link VehicleState} stored in the database.
     */
    @Override
    public VehicleState apply(Row row, String prefix) {
        VehicleState entity = new VehicleState();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSpeed(converter.fromRow(row, prefix + "_speed", Integer.class));
        entity.setLat(converter.fromRow(row, prefix + "_lat", Double.class));
        entity.setLon(converter.fromRow(row, prefix + "_lon", Double.class));
        entity.setError(converter.fromRow(row, prefix + "_error", Integer.class));
        entity.setSerialNumber(converter.fromRow(row, prefix + "_serial_number", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", String.class));
        entity.setOdo(converter.fromRow(row, prefix + "_odo", Double.class));
        entity.setPower(converter.fromRow(row, prefix + "_power", Double.class));
        entity.setThrottle(converter.fromRow(row, prefix + "_throttle", Double.class));
        entity.setTime(converter.fromRow(row, prefix + "_time", Instant.class));
        return entity;
    }
}
