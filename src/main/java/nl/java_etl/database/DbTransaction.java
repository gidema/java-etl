package nl.java_etl.database;

import java.sql.Connection;

import nl.java_etl.EtlTransaction;

public interface DbTransaction extends EtlTransaction {

    Connection getConnection();
}
