package com.sonpj.repository.rowmapper;

import com.sonpj.domain.BatteryState;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link BatteryState}, with proper type conversions.
 */
@Service
public class BatteryStateRowMapper implements BiFunction<Row, String, BatteryState> {

    private final ColumnConverter converter;

    public BatteryStateRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link BatteryState} stored in the database.
     */
    @Override
    public BatteryState apply(Row row, String prefix) {
        BatteryState entity = new BatteryState();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSerialNumber(converter.fromRow(row, prefix + "_serial_number", String.class));
        entity.setVol(converter.fromRow(row, prefix + "_vol", Integer.class));
        entity.setCur(converter.fromRow(row, prefix + "_cur", Integer.class));
        entity.setSoc(converter.fromRow(row, prefix + "_soc", Integer.class));
        entity.setSoh(converter.fromRow(row, prefix + "_soh", Integer.class));
        entity.setState(converter.fromRow(row, prefix + "_state", Integer.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", Integer.class));
        return entity;
    }
}
