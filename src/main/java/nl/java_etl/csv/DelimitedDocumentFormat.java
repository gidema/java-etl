package nl.java_etl.csv;

import java.util.Arrays;
import java.util.List;

public interface DelimitedDocumentFormat {
    public CSVDialect getSyntax();
    public boolean hasHeader();
    public List<String> getColumnNames();

    public class Impl implements DelimitedDocumentFormat {
        private final CSVDialect syntax;
        private final boolean hasHeader;
        private final List<String> columnNames;

        public Impl(CSVDialect syntax, boolean hasHeader,
                List<String> columnNames) {
            super();
            this.syntax = syntax;
            this.hasHeader = hasHeader;
            this.columnNames = columnNames;
        }

        @Override
        public CSVDialect getSyntax() {
            return syntax;
        }

        @Override
        public boolean hasHeader() {
            return hasHeader;
        }

        @Override
        public List<String> getColumnNames() {
            return columnNames;
        }
    }

    public static DelimitedDocumentFormat of(CSVDialect syntax, String... columnNames) {
        return new DelimitedDocumentFormat.Impl(syntax, true, Arrays.asList(columnNames));
    }

    public static DelimitedDocumentFormat of(CSVDialect syntax, List<String> columnNames) {
        return new DelimitedDocumentFormat.Impl(syntax, true, columnNames);
    }
}
