package com.sonpj.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BpSwapRecordSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("old_bat", table, columnPrefix + "_old_bat"));
        columns.add(Column.aliased("new_bat", table, columnPrefix + "_new_bat"));
        columns.add(Column.aliased("old_cab", table, columnPrefix + "_old_cab"));
        columns.add(Column.aliased("new_cab", table, columnPrefix + "_new_cab"));
        columns.add(Column.aliased("bss", table, columnPrefix + "_bss"));
        columns.add(Column.aliased("user", table, columnPrefix + "_user"));
        columns.add(Column.aliased("state", table, columnPrefix + "_state"));
        columns.add(Column.aliased("error", table, columnPrefix + "_error"));

        return columns;
    }
}
