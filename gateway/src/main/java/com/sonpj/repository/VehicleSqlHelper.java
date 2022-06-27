package com.sonpj.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class VehicleSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("clearance", table, columnPrefix + "_clearance"));
        columns.add(Column.aliased("max_power", table, columnPrefix + "_max_power"));
        columns.add(Column.aliased("max_speed", table, columnPrefix + "_max_speed"));
        columns.add(Column.aliased("max_load", table, columnPrefix + "_max_load"));
        columns.add(Column.aliased("weight_total", table, columnPrefix + "_weight_total"));
        columns.add(Column.aliased("max_distance", table, columnPrefix + "_max_distance"));
        columns.add(Column.aliased("wheel_base", table, columnPrefix + "_wheel_base"));
        columns.add(Column.aliased("hw_version", table, columnPrefix + "_hw_version"));
        columns.add(Column.aliased("sw_version", table, columnPrefix + "_sw_version"));
        columns.add(Column.aliased("serial_number", table, columnPrefix + "_serial_number"));
        columns.add(Column.aliased("manufacture_date", table, columnPrefix + "_manufacture_date"));
        columns.add(Column.aliased("lot_number", table, columnPrefix + "_lot_number"));
        columns.add(Column.aliased("color", table, columnPrefix + "_color"));
        columns.add(Column.aliased("vehicle_type", table, columnPrefix + "_vehicle_type"));
        columns.add(Column.aliased("used", table, columnPrefix + "_used"));
        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));

        return columns;
    }
}
