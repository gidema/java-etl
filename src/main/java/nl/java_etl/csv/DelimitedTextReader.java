package nl.java_etl.csv;

import java.io.IOException;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StringArray;

public interface DelimitedTextReader {

    void read(StreamConsumer<StringArray> consumer) throws IOException;

}
