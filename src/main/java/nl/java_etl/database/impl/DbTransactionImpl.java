package nl.java_etl.database.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

import nl.java_etl.database.DbTransaction;

public class DbTransactionImpl implements DbTransaction {
    private final BasicDataSource dataSource;
    private Connection conn;

    public DbTransactionImpl(Properties prop) {
        try {
            Class.forName(prop.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.dataSource = new BasicDataSource();
        dataSource.setUrl(prop.getProperty("jdbc.driver"));
        dataSource.setUrl(prop.getProperty("jdbc.url"));
        dataSource.setUsername(prop.getProperty("jdbc.username"));
        dataSource.setPassword(prop.getProperty("jdbc.password"));
        dataSource.setValidationQuery("SELECT 1;");
    }

    @Override
    public Connection getConnection() {
        return conn;
    }

    @Override
    public void onStart() {
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
        try {
            if (conn != null) {
                conn.commit();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onRollBack() {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
