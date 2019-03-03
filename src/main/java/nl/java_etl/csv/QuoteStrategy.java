package nl.java_etl.csv;

/**
 * Strategy for quoting delimited values in CVS files.
 *
 * @author Gertjan Idema
 *
 */
public enum QuoteStrategy {
    NEVER, // Never quote a delimited value
    WHEN_REQUIRED, // Quote a delimited value if it contains delimiter characters or line terminators
    ALWAYS // Always quote a delimited value
}
