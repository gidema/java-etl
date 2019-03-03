package nl.java_etl.csv.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamGenerator;
import nl.java_etl.core.StringArray;
import nl.java_etl.csv.DelimitedDocumentFormat;
import nl.java_etl.csv.DelimitedLineReader;
import nl.java_etl.csv.CSVDialect;

public class DelimitedTextReader implements StreamGenerator<StringArray> {
    private BufferedReader reader;
    private final DelimitedLineReader delimitedLineReader;
    private final CSVDialect syntax;
    private final List<String> header;
    private StreamConsumer<StringArray> consumer;

    public DelimitedTextReader(Reader reader, CSVDialect syntax, List<String> header) {
        if (reader instanceof BufferedReader) {
            this.reader = (BufferedReader) reader;
        }
        else {
            this.reader = new BufferedReader(reader);
        }
        this.delimitedLineReader = new DelimitedLineReaderImpl(syntax, header.size());
        this.syntax = syntax;
        this.header = header;
    }

    public DelimitedTextReader(Reader reader, CSVDialect syntax, int columnCount) {
        if (reader instanceof BufferedReader) {
            this.reader = (BufferedReader) reader;
        }
        else {
            this.reader = new BufferedReader(reader);
        }
        this.delimitedLineReader = new DelimitedLineReaderImpl(syntax, columnCount);
        this.syntax = syntax;
        this.header = null;
    }

    public DelimitedTextReader(Reader reader, DelimitedDocumentFormat format) {
        this(reader, format.getSyntax(), format.getColumnNames());
    }

    @Override
    public void setTarget(StreamConsumer<StringArray> consumer) {
        this.consumer = consumer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setGenericTarget(StreamConsumer<?> consumer) {
        this.consumer = (StreamConsumer<StringArray>) consumer;
    }

    @Override
    public void run() throws IOException {
        if (syntax.hasHeader()) {
            List<String> headerData = delimitedLineReader.readHeader(reader);
            if (!headerData.equals(header)) {
                String msg = "The file header doesn't match the expected columns";
                IOException e = new IOException(msg);
                consumer.onError(e);
                throw e;
            }
        }
        consumer.onStart();
        StringArray data = delimitedLineReader.readLine(reader);
        while (data != StringArray.EMPTY) {
            consumer.accept(data);
            data = delimitedLineReader.readLine(reader);
        }
        consumer.onComplete();
    }
}
