package com.sonpj.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BatteryStateSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("serial_number", table, columnPrefix + "_serial_number"));
        columns.add(Column.aliased("vol", table, columnPrefix + "_vol"));
        columns.add(Column.aliased("cur", table, columnPrefix + "_cur"));
        columns.add(Column.aliased("soc", table, columnPrefix + "_soc"));
        columns.add(Column.aliased("soh", table, columnPrefix + "_soh"));
        columns.add(Column.aliased("state", table, columnPrefix + "_state"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));

        return columns;
    }
}
