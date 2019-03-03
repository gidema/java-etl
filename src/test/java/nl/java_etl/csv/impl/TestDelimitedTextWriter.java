package nl.java_etl.csv.impl;

import static nl.java_etl.csv.QuoteStrategy.WHEN_REQUIRED;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Collections;

import org.junit.Test;

import nl.java_etl.csv.CSVDialect;

public class TestDelimitedTextWriter {
    private final static CSVDialect SYNTAX1 = new BasicCSVDialect(true, '"',
            WHEN_REQUIRED, ',', '\\', "\n");

    @SuppressWarnings("static-method")
    @Test
    public void testWriteValue1() throws Exception {
        testWriteValue(SYNTAX1, "Te\\st", "Te\\\\st");
    }

    @SuppressWarnings("static-method")
    @Test
    public void testWriteValue2() throws Exception {
        testWriteValue(SYNTAX1, "Te\"st", "\"Te\\\"st\"");
    }

    private static void testWriteValue(CSVDialect syntax, String value, String expected) throws Exception {
        try (
                StringWriter stringWriter = new StringWriter();
                ) {
            DelimitedTextWriterImpl writer = new DelimitedTextWriterImpl(stringWriter, syntax, Collections.emptyList());
            Method method = writer.getClass().getDeclaredMethod("writeValue", String.class);
            method.setAccessible(true);
            method.invoke(writer, value);
            writer.flush();
            stringWriter.close();
            assertEquals(expected, stringWriter.toString());
        }
    }
}
