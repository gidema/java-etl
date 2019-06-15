package nl.java_etl.csv.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import nl.java_etl.core.ProducingStreamGenerator;
import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamProducer;
import nl.java_etl.core.StringArray;
import nl.java_etl.core.impl.PipeLine;
import nl.java_etl.csv.CSVDialect;
import nl.java_etl.csv.DelimitedDocumentFormat;
import nl.java_etl.csv.DelimitedLineReader;

public class DelimitedTextReader implements ProducingStreamGenerator<StringArray> {
    private PipeLine pipeLine;
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

    @Override
    public StreamProducer<StringArray> generate() {
        return generate(StringArray.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> StreamProducer<T> generate(Class<T> clazz) {
        return (StreamProducer<T>) this;
    }

    @Override
    public void setPipeLine(PipeLine pipeLine) {
        this.pipeLine = pipeLine;
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
    public void onStart() {
        if (syntax.hasHeader()) {
            try {
                List<String> headerData = delimitedLineReader.readHeader(reader);
                if (!headerData.equals(header)) {
                    String msg = "The file header doesn't match the expected columns";
                    pipeLine.abort(msg);
                    return;
                }
            } catch (IOException e) {
                pipeLine.abort(e.toString());
                return;
            }
        }
    }

    @Override
    public boolean tryAdvance() {
        try {
            StringArray sa = delimitedLineReader.readLine(reader);
            if (sa == null) return false;
            consumer.accept(sa);
        } catch (IOException e) {
            pipeLine.abort(e.toString());
            throw new RuntimeException(e);
        }
        return true;
    }
}
