package nl.java_etl.database.impl;

import java.util.List;

import nl.java_etl.database.SqlDML;

public class SqlBuilder {
    private final SqlDML dml;
    private StringBuilder sb;

    public SqlBuilder(SqlDML dml) {
        super();
        this.dml = dml;
    }

    public SqlBuilder catalogSeparator() {
        dml.appendCatalogSeparator(sb);
        return this;
    }

    public SqlBuilder identifier(String identifier) {
        dml.appendIdentifier(sb, identifier);
        return this;
    }

    public SqlBuilder append(String s) {
        sb.append(s);
        return this;
    }

    public SqlBuilder insertIntoPst(String table, List<String> columns) {
        dml.insertIntoPst(sb, null, table, columns);
        return this;
    }

    public SqlBuilder insertIntoPst(String schema, String table,
            List<String> columns) {
        dml.insertIntoPst(sb, schema, table, columns);
        return this;
    }

    public String build() {
        return sb.toString();
    }
}
