package nl.java_etl.csv;

import java.io.IOException;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StringArray;

public interface DelimitedTextWriter extends StreamConsumer<StringArray> {
    void flush() throws IOException;
}
