package nl.java_etl.csv.impl;

import nl.java_etl.csv.CSVDialect;
import nl.java_etl.csv.QuoteStrategy;

public class BasicCSVDialect implements CSVDialect {
    private final boolean hasHeader;
    private final QuoteStrategy quoteStrategy;
    private final char quoteChar;
    private final char delimiter;
    private final char escape;
    private final char[] linefeed;

    public BasicCSVDialect(boolean hasHeader, Character quoteChar,
            QuoteStrategy quoteStrategy, Character delimiter, Character escape, String linefeed) {
        super();
        this.hasHeader = hasHeader;
        this.quoteChar = quoteChar == null ? '\0' : quoteChar;
        this.quoteStrategy = quoteStrategy;
        this.delimiter = delimiter;
        this.escape = escape == null ? '\0' : escape;
        this.linefeed = linefeed.toCharArray();
    }

    @Override
    public boolean hasHeader() {
        return hasHeader;
    }

    @Override
    public QuoteStrategy getDelimitStrategy() {
        return quoteStrategy;
    }

    @Override
    public char getQouteChar() {
        return quoteChar;
    }

    @Override
    public char getDelimiter() {
        return delimiter;
    }

    @Override
    public char getEscapeChar() {
        return escape;
    }

    @Override
    public boolean isEscaped() {
        return escape != '\0';
    }

    @Override
    public char[] getLineTerminator() {
        return linefeed;
    }
}
