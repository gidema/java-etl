package nl.java_etl.csv.impl;

import static nl.java_etl.csv.QuoteStrategy.ALWAYS;
import static nl.java_etl.csv.QuoteStrategy.NEVER;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import nl.java_etl.core.StringArray;
import nl.java_etl.core.impl.PipeLine;
import nl.java_etl.csv.CSVDialect;
import nl.java_etl.csv.DelimitedTextWriter;
import nl.java_etl.csv.QuoteStrategy;

public class DelimitedTextWriterImpl implements DelimitedTextWriter {
    private PipeLine pipeLine;
    private final File file;
    private final CSVDialect syntax;
    private final char delimiter;
    private final char separator;
    private final char escape;
    private final QuoteStrategy quoteStrategy;
    private final boolean isEscaped;
    private final String newLine;
    private final List<String> header;

    private BufferedWriter writer;

    public DelimitedTextWriterImpl(File file, CSVDialect syntax,
            List<String> header) {
        this(file, null, syntax, header);
    }

    public DelimitedTextWriterImpl(String filePath, CSVDialect syntax,
            List<String> header) {
        this(new File(filePath), null, syntax, header);
    }

    public DelimitedTextWriterImpl(Writer writer, CSVDialect syntax,
            List<String> header) {
        this(null, writer, syntax, header);
    }

    private DelimitedTextWriterImpl(File file, Writer writer,
            CSVDialect syntax, List<String> header) {
        if (writer != null) {
            if (writer instanceof BufferedWriter) {
                this.writer = (BufferedWriter) writer;
            } else {
                this.writer = new BufferedWriter(writer);
            }
        }
        this.file = file;
        this.syntax = syntax;
        this.quoteStrategy = syntax.getDelimitStrategy();
        this.delimiter = syntax.getQouteChar();
        this.separator = syntax.getDelimiter();
        this.escape = syntax.getEscapeChar();
        this.isEscaped = syntax.isEscaped();
        this.newLine = new String(syntax.getLineTerminator());
        this.header = new ArrayList<>(header);
    }

    @Override
    public void setPipeLine(PipeLine pipeLine) {
        this.pipeLine = pipeLine;
    }

    @Override
    public void accept(StringArray row) {
        try {
            for (int i = 0; i < row.getSize(); i++) {
                if (i > 0) {
                    writer.append(syntax.getDelimiter());
                }
                String value = row.get(i);
                if (value != null) writeValue(value);
            }
            writer.append(newLine);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStart() {
        if (writer == null) {
            try {
                writer = new BufferedWriter(new FileWriter(file));
            } catch (IOException e) {
                pipeLine.abort("While trying to start writing to '%s', the following exception was thrown: %s",
                        file.toString(), e.toString());
                return;
            }
        }
        try {
            if (syntax.hasHeader()) {
                writeHeader();
            }
        } catch (IOException e) {
            pipeLine.abort("While trying to write the header line to '%s', the following exception was thrown: %s",
                    file.toString(), e.toString());
            return;
        }
    }

    private void writeHeader() throws IOException {
        for (int i = 0; i < header.size(); i++) {
            if (i > 0) {
                writer.append(syntax.getDelimiter());
            }
            writeValue(header.get(i));
        }
        writer.write(newLine);
    }

    private void writeValue(String value) throws IOException {
        StringBuilder sb = new StringBuilder(value.length() * 2);
        boolean needsDelimiting = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            // Duplicate any escape in the input
            if (isEscaped && c == escape) {
                sb.append(escape).append(escape);
            } else if (c == delimiter && quoteStrategy != NEVER) {
                needsDelimiting = true;
                sb.append(escape).append(delimiter);
            } else if (c == separator) {
                if (quoteStrategy == NEVER) {
                    if (!isEscaped) {
                        throw new IllegalArgumentException(
                                "One or more fields contain a separator character, "
                                        + "but the strategy doesn't support delimiting of fields or "
                                        + "escaping of special characters");
                    }
                    sb.append(escape).append(separator);
                } else {
                    sb.append(separator);
                    needsDelimiting = true;
                }
            } else if (c == '\n' || c == '\r') {
                if (quoteStrategy == NEVER) {
                    throw new IllegalArgumentException(
                            "One or more fields contain a linefeed and/or carriage return character "
                                    + "but the strategy doesn't support delimiting.");
                }
                needsDelimiting = true;
            } else {
                sb.append(c);
            }
        }
        if (needsDelimiting || quoteStrategy == ALWAYS) {
            writer.append(delimiter);
        }
        writer.append(sb);
        if (needsDelimiting || quoteStrategy == ALWAYS) {
            writer.append(delimiter);
        }
    }

    @Override
    public void onComplete() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAbort() {
        try {
            if (writer != null) {
                writer.close();
            }
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }
}
