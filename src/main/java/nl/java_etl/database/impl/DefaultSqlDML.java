package nl.java_etl.database.impl;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import nl.java_etl.database.SqlDML;

public class DefaultSqlDML implements SqlDML {
    private String catalogSeparator;
    private String identifierQuote;

    public DefaultSqlDML(DatabaseMetaData metadata) {
        super();
        try {
            this.catalogSeparator = metadata.getCatalogSeparator();
            this.identifierQuote = metadata.getIdentifierQuoteString();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void appendCatalogSeparator(StringBuilder sb) {
        sb.append(catalogSeparator);
    }

    @Override
    public void appendIdentifier(StringBuilder sb, String identifier) {
        sb.append(identifierQuote)
        .append(identifier)
        .append(identifierQuote);
    }

    @Override
    public void append(StringBuilder sb, String s) {
        sb.append(s);
    }

    @Override
    public void insertIntoPst(StringBuilder sb, String table, List<String> columns) {
        insertIntoPst(sb, null, table, columns);
    }

    @Override
    public void insertIntoPst(StringBuilder sb, String schema, String table,
            List<String> columns) {
        sb.append("INSERT INTO ");
        appendTable(sb, schema, table);
        if (schema != null) {
            sb.append("\"").append(schema).append("\".");
        }
        sb.append("\"").append(table).append("\"(");
        sb.append(String.join(", ", columns));
        sb.append(") VALUES (");
        String.join(", ", Collections.nCopies(columns.size(), "?"));
        sb.append(");");
    }
}
