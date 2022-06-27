package com.sonpj.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BssSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("address", table, columnPrefix + "_address"));
        columns.add(Column.aliased("serial_number", table, columnPrefix + "_serial_number"));
        columns.add(Column.aliased("hw_version", table, columnPrefix + "_hw_version"));
        columns.add(Column.aliased("sw_version", table, columnPrefix + "_sw_version"));
        columns.add(Column.aliased("manufacture_date", table, columnPrefix + "_manufacture_date"));
        columns.add(Column.aliased("lon", table, columnPrefix + "_lon"));
        columns.add(Column.aliased("lat", table, columnPrefix + "_lat"));
        columns.add(Column.aliased("type_code", table, columnPrefix + "_type_code"));
        columns.add(Column.aliased("cab_num", table, columnPrefix + "_cab_num"));
        columns.add(Column.aliased("cab_empty_num", table, columnPrefix + "_cab_empty_num"));
        columns.add(Column.aliased("bp_ready_num", table, columnPrefix + "_bp_ready_num"));
        columns.add(Column.aliased("swap_bp_no", table, columnPrefix + "_swap_bp_no"));

        return columns;
    }
}
