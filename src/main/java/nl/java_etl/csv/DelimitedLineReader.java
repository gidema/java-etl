package nl.java_etl.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import nl.java_etl.core.StringArray;

public interface DelimitedLineReader {
    StringArray readLine(BufferedReader reader) throws IOException;

    /**
     * Read the header line of the delimited file. All column headers are read regardless the expected number of columns.
     *
     * @param reader
     * @return
     * @throws IOException
     */
    List<String> readHeader(BufferedReader reader) throws IOException;
}
