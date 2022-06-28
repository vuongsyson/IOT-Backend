package com.sonpj.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BatterySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("serial_no", table, columnPrefix + "_serial_no"));
        columns.add(Column.aliased("hw_version", table, columnPrefix + "_hw_version"));
        columns.add(Column.aliased("sw_version", table, columnPrefix + "_sw_version"));
        columns.add(Column.aliased("manufacture_date", table, columnPrefix + "_manufacture_date"));
        columns.add(Column.aliased("capacity", table, columnPrefix + "_capacity"));
        columns.add(Column.aliased("max_charge", table, columnPrefix + "_max_charge"));
        columns.add(Column.aliased("max_discarge", table, columnPrefix + "_max_discarge"));
        columns.add(Column.aliased("max_vol", table, columnPrefix + "_max_vol"));
        columns.add(Column.aliased("min_vol", table, columnPrefix + "_min_vol"));
        columns.add(Column.aliased("used", table, columnPrefix + "_used"));
        columns.add(Column.aliased("soc", table, columnPrefix + "_soc"));
        columns.add(Column.aliased("soh", table, columnPrefix + "_soh"));
        columns.add(Column.aliased("temp", table, columnPrefix + "_temp"));
        columns.add(Column.aliased("owner_id", table, columnPrefix + "_owner_id"));
        columns.add(Column.aliased("renter_id", table, columnPrefix + "_renter_id"));
        columns.add(Column.aliased("cycle_count", table, columnPrefix + "_cycle_count"));

        columns.add(Column.aliased("rental_history_id", table, columnPrefix + "_rental_history_id"));
        return columns;
    }
}
