package nl.java_etl.database;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface StatementWriter<T> {
    public void write(PreparedStatement pst, T data);
}
