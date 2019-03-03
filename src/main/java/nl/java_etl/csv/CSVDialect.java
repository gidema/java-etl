package nl.java_etl.csv;

/**
 * CSV Dialect defines a simple format to describe the various dialects of CSV files in a language agnostic manner.
 *
 * The interface is kept as close as possible to the definition provided by
 * frictionlessdata.
 * @see {@link https://frictionlessdata.io/specs/csv-dialect/}
 *
 * @author Gertjan Idema
 *
 */
public interface CSVDialect {
    final static char[] DEFAULT_LINE_TERMINATOR = new char[] {'\r', '\n'};

    /**
     * Does this delimited document have a header line?
     * @return true if a header line is present, false otherwise
     */
    public default boolean hasHeader() { return true;}

    /**
     * Is the header case-sensitive?
     *
     * @return true if the header is case sensitive, false if it is not,
     *  or if hasHeader returns null
     */
    public default boolean isCaseSensitiveHeader() { return false;}

    /**
     * Get the QuoteStrategy. This can be one of: NEVER, ALWAYS, WHEN_NEEDED.
     * WHEN_NEEDED means only delimits a field that contains 1 or more delimiter and/or
     * separator characters.
     *
     * @return
     */
    public QuoteStrategy getDelimitStrategy();

    /**
     * Get the quoting character. Defaults to '"'
     * <br/> Default: " (Double quote)
     * @return The delimiter character or '\0' for no delimiter
     */
    public default char getQouteChar() { return '"';}

    /**
     * Controls the handling of quotes inside fields. If true, two consecutive quotes should be interpreted as one.
     * <br/>Default: true
     * @return true if quote character inside fields should be duplicated.
     */
    public default boolean isDoubleQuote() { return true; }
    /**
     * Get the delimiter character between fields.
     * <br/>Default: , (Comma)
     * @return The delimiter character
     */
    public default char getDelimiter() { return ',';}

    /**
     * Get the escape character.
     * <br/>Default: '\0' (Not set)
     * @return The escape character or '\0' for no escape character
     */
    public default char getEscapeChar() { return '\0';}

    /**
     * Check if this syntax supports escaping of special characters in fields.
     * @return
     */
    public boolean isEscaped();

    /**
     * Get the line terminator character(s)
     * <br/> Default: \r\n (Carriage return + Line feed)
     * @return The line terminator character(s)
     */
    public default char[] getLineTerminator() { return DEFAULT_LINE_TERMINATOR;}
}
