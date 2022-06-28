package com.sonpj.repository.rowmapper;

import com.sonpj.domain.Vehicle;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Vehicle}, with proper type conversions.
 */
@Service
public class VehicleRowMapper implements BiFunction<Row, String, Vehicle> {

    private final ColumnConverter converter;

    public VehicleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Vehicle} stored in the database.
     */
    @Override
    public Vehicle apply(Row row, String prefix) {
        Vehicle entity = new Vehicle();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setClearance(converter.fromRow(row, prefix + "_clearance", Integer.class));
        entity.setMaxPower(converter.fromRow(row, prefix + "_max_power", Integer.class));
        entity.setMaxSpeed(converter.fromRow(row, prefix + "_max_speed", Integer.class));
        entity.setMaxLoad(converter.fromRow(row, prefix + "_max_load", Integer.class));
        entity.setWeightTotal(converter.fromRow(row, prefix + "_weight_total", Integer.class));
        entity.setMaxDistance(converter.fromRow(row, prefix + "_max_distance", Integer.class));
        entity.setWheelBase(converter.fromRow(row, prefix + "_wheel_base", Integer.class));
        entity.setHwVersion(converter.fromRow(row, prefix + "_hw_version", Integer.class));
        entity.setSwVersion(converter.fromRow(row, prefix + "_sw_version", Integer.class));
        entity.setSerialNumber(converter.fromRow(row, prefix + "_serial_number", String.class));
        entity.setManufactureDate(converter.fromRow(row, prefix + "_manufacture_date", String.class));
        entity.setLotNumber(converter.fromRow(row, prefix + "_lot_number", Integer.class));
        entity.setColor(converter.fromRow(row, prefix + "_color", String.class));
        entity.setVehicleType(converter.fromRow(row, prefix + "_vehicle_type", String.class));
        entity.setUsed(converter.fromRow(row, prefix + "_used", Boolean.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
