package nl.java_etl.database;

import java.util.List;

public interface SqlDML {
    public void appendCatalogSeparator(StringBuilder sb);
    public void appendIdentifier(StringBuilder sb, String identifier);
    public default void appendColumn(StringBuilder sb, String column) {
        appendIdentifier(sb, column);
    }

    public default void appendSchema(StringBuilder sb, String schema) {
        appendIdentifier(sb, schema);
    }

    public default void appendTable(StringBuilder sb, String table) {
        appendIdentifier(sb, table);
    }

    public default void appendTable(StringBuilder sb, String schema, String table) {
        if (schema != null) {
            appendSchema(sb, schema);
            appendCatalogSeparator(sb);
        }
        appendTable(sb, table);
    }

    public void append(StringBuilder sb, String s);

    public void insertIntoPst(StringBuilder sb, String table, List<String> columns);

    public void insertIntoPst(StringBuilder sb, String schema, String table, List<String> columns);
}
