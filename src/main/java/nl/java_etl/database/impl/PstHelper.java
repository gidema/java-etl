package nl.java_etl.database.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;

public class PstHelper {
    public static void setLong(PreparedStatement pst, int index, Long value) throws SQLException {
        if (value == null) {
            pst.setNull(index, Types.NUMERIC);
        }
        else {
            pst.setLong(index, value);
        }
    }

    public static void setInt(PreparedStatement pst, int index, Integer value) throws SQLException {
        if (value == null) {
            pst.setNull(index, Types.INTEGER);
        }
        else {
            pst.setInt(index, value);
        }
    }

    public static void setString(PreparedStatement pst, int index, String value) throws SQLException {
        if (value == null) {
            pst.setNull(index, Types.VARCHAR);
        }
        else {
            pst.setString(index, value);
        }
    }

    public static void setDouble(PreparedStatement pst, int index, Double value) throws SQLException {
        if (value == null) {
            pst.setNull(index, Types.DOUBLE);
        }
        else {
            pst.setDouble(index, value);
        }
    }

    public static void setLocalTime(PreparedStatement pst, int index, LocalTime value) throws SQLException {
        if (value == null) {
            pst.setNull(index, Types.TIME);
        }
        else {
            pst.setTime(index, Time.valueOf(value));
        }
    }
}
