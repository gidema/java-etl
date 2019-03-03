package nl.java_etl.csv.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamGenerator;
import nl.java_etl.core.StringArray;
import nl.java_etl.csv.DelimitedDocumentFormat;

public class DelimitedTextFileReader implements StreamGenerator<StringArray> {
    private final File file;
    private InputStream is;
    private Reader reader;
    private final DelimitedDocumentFormat syntax;
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
    public void setTarget(StreamConsumer<StringArray> target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setGenericTarget(StreamConsumer<?> target) {
        this.target = (StreamConsumer<StringArray>) target;
    }

    @Override
    public void run() throws IOException {
        if (file != null) {
            this.is = new FileInputStream(file);
        }
        if (is != null) {
            this.reader = new BufferedReader(new InputStreamReader(is));
        }
        DelimitedTextReader textReader = new DelimitedTextReader(reader, syntax);
        textReader.setTarget(target);
        textReader.run();
    }
}
