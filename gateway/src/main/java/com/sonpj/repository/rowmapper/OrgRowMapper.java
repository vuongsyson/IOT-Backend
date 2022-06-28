package com.sonpj.repository.rowmapper;

import com.sonpj.domain.Org;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Org}, with proper type conversions.
 */
@Service
public class OrgRowMapper implements BiFunction<Row, String, Org> {

    private final ColumnConverter converter;

    public OrgRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Org} stored in the database.
     */
    @Override
    public Org apply(Row row, String prefix) {
        Org entity = new Org();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setOrgId(converter.fromRow(row, prefix + "_org_id", Long.class));
        return entity;
    }
}
