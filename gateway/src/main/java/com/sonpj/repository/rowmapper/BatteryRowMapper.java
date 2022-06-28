package com.sonpj.repository.rowmapper;

import com.sonpj.domain.Battery;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Battery}, with proper type conversions.
 */
@Service
public class BatteryRowMapper implements BiFunction<Row, String, Battery> {

    private final ColumnConverter converter;

    public BatteryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Battery} stored in the database.
     */
    @Override
    public Battery apply(Row row, String prefix) {
        Battery entity = new Battery();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSerialNo(converter.fromRow(row, prefix + "_serial_no", String.class));
        entity.setHwVersion(converter.fromRow(row, prefix + "_hw_version", Integer.class));
        entity.setSwVersion(converter.fromRow(row, prefix + "_sw_version", Integer.class));
        entity.setManufactureDate(converter.fromRow(row, prefix + "_manufacture_date", String.class));
        entity.setCapacity(converter.fromRow(row, prefix + "_capacity", Integer.class));
        entity.setMaxCharge(converter.fromRow(row, prefix + "_max_charge", Integer.class));
        entity.setMaxDiscarge(converter.fromRow(row, prefix + "_max_discarge", Integer.class));
        entity.setMaxVol(converter.fromRow(row, prefix + "_max_vol", Integer.class));
        entity.setMinVol(converter.fromRow(row, prefix + "_min_vol", Integer.class));
        entity.setUsed(converter.fromRow(row, prefix + "_used", Boolean.class));
        entity.setSoc(converter.fromRow(row, prefix + "_soc", Integer.class));
        entity.setSoh(converter.fromRow(row, prefix + "_soh", Integer.class));
        entity.setTemp(converter.fromRow(row, prefix + "_temp", Integer.class));
        entity.setOwnerId(converter.fromRow(row, prefix + "_owner_id", Long.class));
        entity.setRenterId(converter.fromRow(row, prefix + "_renter_id", Long.class));
        entity.setCycleCount(converter.fromRow(row, prefix + "_cycle_count", Long.class));
        entity.setRentalHistoryId(converter.fromRow(row, prefix + "_rental_history_id", Long.class));
        return entity;
    }
}
