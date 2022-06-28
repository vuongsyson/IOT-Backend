package com.sonpj.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CabinetSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("bss_id", table, columnPrefix + "_bss_id"));
        columns.add(Column.aliased("bp_id", table, columnPrefix + "_bp_id"));
        columns.add(Column.aliased("bp_ready", table, columnPrefix + "_bp_ready"));
        columns.add(Column.aliased("swap_no", table, columnPrefix + "_swap_no"));
        columns.add(Column.aliased("state_code", table, columnPrefix + "_state_code"));

        return columns;
    }
}
