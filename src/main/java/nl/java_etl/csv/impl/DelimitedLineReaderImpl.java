package nl.java_etl.csv.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.java_etl.core.StringArray;
import nl.java_etl.csv.CSVDialect;
import nl.java_etl.csv.DelimitedLineReader;

public class DelimitedLineReaderImpl implements DelimitedLineReader {
    private final static int EOF = -1;
    private final char[] lf;
    private final char delimiter;
    private final char quoteChar;
    private final char escape;
    private final int nColumns; // -1 if unknown
    private final StringBuilder sb = new StringBuilder(100);

    public DelimitedLineReaderImpl(CSVDialect syntax, int nColumns) {
        super();
        this.lf = syntax.getLineTerminator();
        this.delimiter = syntax.getDelimiter();
        this.quoteChar = syntax.getQouteChar();
        this.escape = syntax.getEscapeChar();
        this.nColumns = nColumns;
    }

    /**
     * Read the header line of the delimited file. All column headers are read
     * regardless the expected number of columns.
     *
     * @param reader
     * @return
     * @throws IOException
     */
    @Override
    public List<String> readHeader(BufferedReader reader) throws IOException {
        if (eof(reader))
            throw new IOException("The input document is empty");
        if (matchEol(reader))
            throw new IOException("The header line of the document is empty");
        List<String> header = new ArrayList<>(nColumns);
        do {
            String value = readValue(reader);
            header.add(value);
        }
        while (!matchEol(reader) && match(reader, delimiter));
        return header;
    }

    @Override
    public StringArray readLine(BufferedReader reader) throws IOException {
        if (matchEol(reader))
            return StringArray.EMPTY;
        StringArray array = new StringArray.Impl(nColumns);
        int i = 0;
        while (!matchEol(reader) && i < nColumns) {
            if (i > 0) match(reader, delimiter);
            String value = readValue(reader);
            array.set(i, value);
            i++;
        }
        if (matchEol(reader) && i < nColumns && nColumns != -1) {
            String msg = String.format(
                    "Missing data. %d columns were expected "
                            + "but the row contains only %d values.",
                            nColumns, i);
            throw new IOException(msg);
        }
        if (i > nColumns && nColumns != -1) {
            String msg = String.format("Extra data. %d columns were expected "
                    + "but the row contains extra values.", nColumns);
            throw new IOException(msg);
        }
        // Remove trailing spaces and the end-of-line character(s)
        skipSpaces(reader);
        matchEol(reader);
        return array;
    }

    protected String readValue(BufferedReader reader) throws IOException {
        skipSpaces(reader);
        if (peekEol(reader)) {
            return "";
        }
        boolean delimited = match(reader, quoteChar);
        if (delimited) {
            return readDelimitedValue(reader);
        }
        return readUndelimitedValue(reader);
    }

    protected String readDelimitedValue(BufferedReader reader)
            throws IOException {
        sb.setLength(0);
        boolean ready = false;
        while (!ready) {
            if (match(reader, escape)) {
                if (escape == quoteChar) {
                    if (match(reader, quoteChar)) {
                        sb.append(quoteChar);
                    } else {
                        ready = true;
                    }
                } else {
                    sb.appendCodePoint(reader.read());
                }
                continue;
            }
            if (match(reader, quoteChar)) {
                skipSpaces(reader);
                ready = peek(reader, delimiter) || peekEol(reader);
            }
            else {
                sb.appendCodePoint(reader.read());
            }
        }
        return sb.toString();
    }

    protected String readUndelimitedValue(BufferedReader reader)
            throws IOException {
        skipSpaces(reader);
        sb.setLength(0);
        boolean ready = false;
        while (!ready) {
            if (peekEol(reader)) {
                ready = true;
                break;
            }
            if (match(reader, escape)) {
                sb.appendCodePoint(reader.read());
                continue;
            }
            if (peek(reader, delimiter)) {
                ready = true;
                continue;
            }
            sb.appendCodePoint(reader.read());
        }
        return sb.toString().trim();
    }

    private void skipSpaces(BufferedReader reader) throws IOException {
        if (delimiter != ' ') {
            reader.mark(1);
            while (reader.read() == ' ') {
                reader.mark(1);
            }
            reader.reset();
        }
    }

    private boolean matchEol(BufferedReader reader) throws IOException {
        // eof implies eol
        if (eof(reader))
            return true;
        return match(reader, lf, true);
    }

    private boolean peekEol(BufferedReader reader) throws IOException {
        // eof implies eol
        if (eof(reader))
            return true;
        return match(reader, lf, false);
    }

    private static boolean match(BufferedReader reader, int c)
            throws IOException {
        reader.mark(1);
        if (reader.read() == c)
            return true;
        reader.reset();
        return false;
    }

    /**
     * Check if the next character to be read matches the specified character, but don't remove it.
     *
     * @param reader
     * @param c
     * @return
     * @throws IOException
     */
    private static boolean peek(BufferedReader reader, char c) throws IOException {
        reader.mark(1);
        boolean found = (reader.read() == c);
        reader.reset();
        return found;
    }

    private static boolean match(BufferedReader reader, char[] chars, boolean skip)
            throws IOException {
        reader.mark(chars.length);
        int i = 0;
        boolean ready = false;
        while (!ready) {
            int c = reader.read();
            if (c == chars[i++] && i == chars.length) {
                if (!skip) reader.reset();
                return true;
            }
            if (i == chars.length) {
                reader.reset();
                return false;
            }
        }
        reader.reset();
        return false;
    }

    private static boolean eof(BufferedReader reader) throws IOException {
        return match(reader, EOF);
    }
}
