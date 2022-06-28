package com.sonpj.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class VehicleStateSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("speed", table, columnPrefix + "_speed"));
        columns.add(Column.aliased("lat", table, columnPrefix + "_lat"));
        columns.add(Column.aliased("lon", table, columnPrefix + "_lon"));
        columns.add(Column.aliased("error", table, columnPrefix + "_error"));
        columns.add(Column.aliased("serial_number", table, columnPrefix + "_serial_number"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("odo", table, columnPrefix + "_odo"));
        columns.add(Column.aliased("power", table, columnPrefix + "_power"));
        columns.add(Column.aliased("throttle", table, columnPrefix + "_throttle"));
        columns.add(Column.aliased("time", table, columnPrefix + "_time"));

        return columns;
    }
}
