package nl.java_etl.database.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.java_etl.database.SqlDML;
import nl.java_etl.database.StatementWriter;
import nl.java_etl.database.impl.SqlInsertBuilder.ColumnBinding;

public class SqlInsert<T> implements StatementWriter<T> {
    private final String schema;
    private final String table;
    private final List<ColumnBinding<T>> bindings;
    private final List<String> columns;

    public SqlInsert(String schema, String table, List<ColumnBinding<T>> bindings) {
        this.schema = schema;
        this.table = table;
        this.bindings = new ArrayList<>(bindings);
        this.columns = bindings.stream().map(ColumnBinding::getColumnName).collect(Collectors.toList());
    }

    @Override
    public void write(PreparedStatement pst, T data) {
        for (int i=0; i<bindings.size(); i++) {
            try {
                pst.setObject(i+1, bindings.get(i).getGetter().apply(data));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public String getDML(SqlDML dml) {
        return new SqlBuilder(dml).insertIntoPst(schema, table, columns).build();
    }
}
