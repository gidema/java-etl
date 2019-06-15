package nl.java_etl.csv.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import nl.java_etl.core.ProducingStreamGenerator;
import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamProducer;
import nl.java_etl.core.StringArray;
import nl.java_etl.core.impl.PipeLine;
import nl.java_etl.csv.DelimitedDocumentFormat;

public class DelimitedTextFileReader implements ProducingStreamGenerator<StringArray> {
    private PipeLine pipeLine;
    private final File file;
    private InputStream is;
    private Reader reader;
    private final DelimitedDocumentFormat syntax;
    private DelimitedTextReader textReader;
    private StreamConsumer<StringArray> target;

    public DelimitedTextFileReader(String fileName, DelimitedDocumentFormat syntax) {
        this(new File(fileName), syntax);
    }

    public DelimitedTextFileReader(File file, DelimitedDocumentFormat syntax) {
        this.file = file;
        this.syntax = syntax;
        this.is = null;
        this.reader = null;
    }

    public DelimitedTextFileReader(InputStream is, DelimitedDocumentFormat syntax) {
        this.file = null;
        this.syntax = syntax;
        this.is = is;
        this.reader = null;
    }

    public DelimitedTextFileReader(Reader reader, DelimitedDocumentFormat syntax) {
        this.file = null;
        this.syntax = syntax;
        this.is = null;
        this.reader = reader;
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

    @Override
    public void setTarget(StreamConsumer<StringArray> target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setGenericTarget(StreamConsumer<?> target) {
        this.target = (StreamConsumer<StringArray>) target;
    }

    @Override
    public boolean tryAdvance() {
        return textReader.tryAdvance();
    }

    @Override
    public void onStart() {
        if (file != null) {
            try {
                this.is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                pipeLine.abort("The input file could not be found: %s", file.toString());
                return;
            }
        }
        if (is != null) {
            this.reader = new BufferedReader(new InputStreamReader(is));
        }
        textReader = new DelimitedTextReader(reader, syntax);
        textReader.setTarget(target);
        textReader.setPipeLine(pipeLine);
        textReader.onStart();
    }
}
