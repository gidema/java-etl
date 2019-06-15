package nl.java_etl.database.impl;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import nl.java_etl.EtlTransaction;
import nl.java_etl.core.impl.PipeLine;
import nl.java_etl.database.DbTableWriter;
import nl.java_etl.database.DbTransaction;
import nl.java_etl.database.StatementWriter;

public class BasicTableWriter<T> implements DbTableWriter<T> {
    private PipeLine pipeLine;
    private final DbTransaction dbTransaction;
    private final String sqlInsert;
    private PreparedStatement pst;
    private final StatementWriter<T> statementWriter;
    private final int batchSize = 1000;
    private int rowCount = 0;

    public BasicTableWriter(DbTransaction dbTransaction, String sqlInsert,
            StatementWriter<T> statementWriter) {
        super();
        this.dbTransaction = dbTransaction;
        this.sqlInsert = sqlInsert;
        this.statementWriter = statementWriter;
    }

    @Override
    public void setPipeLine(PipeLine pipeLine) {
        this.pipeLine = pipeLine;
    }

    @Override
    public void onStart() {
        try {
            @SuppressWarnings("resource")
            Connection conn = dbTransaction.getConnection();
            pst = conn.prepareStatement(sqlInsert);
            rowCount = 0;
        } catch (SQLException e) {
            pipeLine.abort(e.toString());
        }
    }

    @Override
    public void onComplete() {
        try {
            if (rowCount > 0) {
                pst.executeBatch();
            }
        } catch (SQLException e) {
            pipeLine.abort(e.toString());
        }
    }

    @Override
    public void onAbort() {
        dbTransaction.onRollBack();
    }

    @Override
    public void accept(T t) {
        try {
            statementWriter.write(pst, t);
            pst.addBatch();
            rowCount++;
            if (rowCount >= batchSize) {
                rowCount = 0;
                pst.executeBatch();
            }
        } catch (BatchUpdateException e) {
            pipeLine.abort(e.toString() + ":" + e.getNextException());

        } catch (SQLException e) {
            pipeLine.abort(e.toString());
        }
    }

    @Override
    public EtlTransaction getTransaction() {
        return dbTransaction;
    }
}
