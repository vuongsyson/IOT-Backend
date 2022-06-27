package com.sonpj.repository.rowmapper;

import com.sonpj.domain.Bss;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Bss}, with proper type conversions.
 */
@Service
public class BssRowMapper implements BiFunction<Row, String, Bss> {

    private final ColumnConverter converter;

    public BssRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Bss} stored in the database.
     */
    @Override
    public Bss apply(Row row, String prefix) {
        Bss entity = new Bss();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setSerialNumber(converter.fromRow(row, prefix + "_serial_number", String.class));
        entity.setHwVersion(converter.fromRow(row, prefix + "_hw_version", Integer.class));
        entity.setSwVersion(converter.fromRow(row, prefix + "_sw_version", Integer.class));
        entity.setManufactureDate(converter.fromRow(row, prefix + "_manufacture_date", String.class));
        entity.setLon(converter.fromRow(row, prefix + "_lon", Double.class));
        entity.setLat(converter.fromRow(row, prefix + "_lat", Double.class));
        entity.setTypeCode(converter.fromRow(row, prefix + "_type_code", Integer.class));
        entity.setCabNum(converter.fromRow(row, prefix + "_cab_num", Integer.class));
        entity.setCabEmptyNum(converter.fromRow(row, prefix + "_cab_empty_num", Integer.class));
        entity.setBpReadyNum(converter.fromRow(row, prefix + "_bp_ready_num", Integer.class));
        entity.setSwapBpNo(converter.fromRow(row, prefix + "_swap_bp_no", Long.class));
        return entity;
    }
}
