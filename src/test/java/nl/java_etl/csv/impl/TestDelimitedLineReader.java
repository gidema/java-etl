package nl.java_etl.csv.impl;

import static nl.java_etl.csv.QuoteStrategy.ALWAYS;
import static nl.java_etl.csv.QuoteStrategy.NEVER;
import static nl.java_etl.csv.QuoteStrategy.WHEN_REQUIRED;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import nl.java_etl.core.StringArray;
import nl.java_etl.csv.CSVDialect;

public class TestDelimitedLineReader {
    @SuppressWarnings("static-method")
    @Test
    public void testReadDelimitedValue() throws IOException {
        CSVDialect syntax = new BasicCSVDialect(true, '"',
                WHEN_REQUIRED, ',', '\\', "\n");
        BufferedReader reader = new BufferedReader(new StringReader("\"test\""));
        DelimitedLineReaderImpl lineReader = new DelimitedLineReaderImpl(syntax, 1);
        String value = lineReader.readValue(reader);
        assertEquals("test", value);
    }

    @SuppressWarnings("static-method")
    @Test
    public void testReadDelimitedValueWithEscape() throws IOException {
        CSVDialect syntax = new BasicCSVDialect(true, '\'',
                WHEN_REQUIRED, ',', '\\', "\n");
        BufferedReader reader = new BufferedReader(new StringReader("'te\\'st'"));
        //  ,te\\\"st, \"te\nst\"
        DelimitedLineReaderImpl lineReader = new DelimitedLineReaderImpl(syntax, 1);
        String value = lineReader.readValue(reader);
        assertEquals("te'st", value);
    }

    @SuppressWarnings("static-method")
    @Test
    public void testReadDelimitedValueWithNewLine() throws IOException {
        CSVDialect syntax = new BasicCSVDialect(true, '\'',
                WHEN_REQUIRED, ',', '\\', "\n");
        BufferedReader reader = new BufferedReader(new StringReader("'te\nst'"));
        DelimitedLineReaderImpl lineReader = new DelimitedLineReaderImpl(syntax, 1);
        String value = lineReader.readValue(reader);
        assertEquals("te\nst", value);
    }

    @SuppressWarnings("static-method")
    @Test
    public void testReadUnDelimitedValue() throws IOException {
        CSVDialect syntax = new BasicCSVDialect(true, null,
                NEVER, ',', '\\', "\n");
        BufferedReader reader = new BufferedReader(new StringReader("test"));
        DelimitedLineReaderImpl lineReader = new DelimitedLineReaderImpl(syntax, 1);
        String value = lineReader.readValue(reader);
        assertEquals("test", value);
    }

    @SuppressWarnings("static-method")
    @Test
    public void testReadHeader() throws IOException {
        CSVDialect syntax = new BasicCSVDialect(true, '"',
                WHEN_REQUIRED, ',', '\\', "\n");
        BufferedReader reader = new BufferedReader(new StringReader("foo, bar, fooBar"));
        DelimitedLineReaderImpl lineReader = new DelimitedLineReaderImpl(syntax, 3);
        List<String> value = lineReader.readHeader(reader);
        assertEquals(3, value.size());
        assertEquals("bar", value.get(1));
        assertEquals("fooBar", value.get(2));
    }

    @SuppressWarnings("static-method")
    @Test
    public void testReadDelimitedLine() throws IOException {
        CSVDialect syntax = new BasicCSVDialect(true, '"',
                WHEN_REQUIRED, ',', '\\', "\n");
        BufferedReader reader = new BufferedReader(new StringReader("foo,bar,,"));
        DelimitedLineReaderImpl lineReader = new DelimitedLineReaderImpl(syntax, 4);
        StringArray value = lineReader.readLine(reader);
        assertEquals(4, value.getSize());
        assertEquals("bar", value.get(1));
        assertEquals("", value.get(2));
        assertEquals("", value.get(3));
    }

    @SuppressWarnings("static-method")
    @Test
    public void testReadQoutedLine() throws IOException {
        CSVDialect syntax = new BasicCSVDialect(true, '"',
                ALWAYS, ',', '\\', "\n");
        BufferedReader reader = new BufferedReader(new StringReader("\"foo\",\"bar\",\"\",\"\""));
        DelimitedLineReaderImpl lineReader = new DelimitedLineReaderImpl(syntax, 4);
        StringArray value = lineReader.readLine(reader);
        assertEquals(4, value.getSize());
        assertEquals("bar", value.get(1));
        assertEquals("", value.get(2));
        assertEquals("", value.get(3));
    }


}
