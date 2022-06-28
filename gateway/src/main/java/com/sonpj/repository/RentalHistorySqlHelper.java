package com.sonpj.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RentalHistorySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        columns.add(Column.aliased("battery_id", table, columnPrefix + "_battery_id"));
        columns.add(Column.aliased("time_start", table, columnPrefix + "_time_start"));
        columns.add(Column.aliased("time_end", table, columnPrefix + "_time_end"));

        return columns;
    }
}
