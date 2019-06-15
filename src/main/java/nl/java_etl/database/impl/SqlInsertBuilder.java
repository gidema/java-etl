package nl.java_etl.database.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class SqlInsertBuilder<T> {
    private final String schema;
    private final String tableName;
    private final List<ColumnBinding<T>> bindings = new LinkedList<>();

    public SqlInsertBuilder(String schema, String tableName) {
        super();
        this.schema = schema;
        this.tableName = tableName;
    }

    public SqlInsertBuilder<T> bind(String columnName, Function<T, ?> getter) {
        bindings.add(new ColumnBinding<>(columnName, getter));
        return this;
    }

    public SqlInsert<T> build() {
        return new SqlInsert<>(schema, tableName, bindings);
    }

    public static class ColumnBinding<T> {
        private final String columnName;
        private final Function<T, ?> getter;

        public ColumnBinding(String columnName, Function<T, ?> getter) {
            super();
            this.columnName = columnName;
            this.getter = getter;
        }

        public String getColumnName() {
            return columnName;
        }

        public Function<T, ?> getGetter() {
            return getter;
        }
    }
}
