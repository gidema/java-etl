package nl.java_etl.csv.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import nl.java_etl.core.StringArray;
import nl.java_etl.core.impl.ListAggregator;
import nl.java_etl.core.impl.PipeLine;
import nl.java_etl.csv.CSVDialect;
import nl.java_etl.csv.QuoteStrategy;

public class TestDelimitedTextReader {
    @Test
    public void test1() {
        CSVDialect syntax = new BasicCSVDialect(true, '"',
                QuoteStrategy.WHEN_REQUIRED, ',', '\\', "\n");
        List<String> header = Arrays.asList("route_id", "agency_id", "route_short_name",
                "route_long_name", "route_desc", "route_type", "route_color",
                "route_text_color", "route_url");
        try (
                InputStream is = getClass().getResourceAsStream("routes.txt");
                Reader reader = new InputStreamReader(is);
                ) {
            DelimitedTextReader textReader = new DelimitedTextReader(reader, syntax, header);
            ListAggregator<StringArray> consumer = new ListAggregator<>();
            PipeLine.from(textReader).target(consumer);
            textReader.run();
            List<StringArray> list = consumer.getResult();
            assertEquals(23, list.size());
            List<String> row3 = Arrays.asList("52921", "CXX", "60", "Delft Tanthof - Nootdorp",
                    "", "3", "", "","http://www.connexxion.nl/dienstregeling/lijn?ID=W060");
            assertEquals(row3, list.get(3).asList());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        CSVDialect syntax = new BasicCSVDialect(true, '"',
                QuoteStrategy.ALWAYS, ',', '\\', "\n");
        List<String> header = Arrays.asList("route_id", "agency_id", "route_short_name",
                "route_long_name", "route_desc", "route_type", "route_color",
                "route_text_color", "route_url");
        try (
                InputStream is = getClass().getResourceAsStream("quotedroutes.txt");
                Reader reader = new InputStreamReader(is);
                ) {
            DelimitedTextReader textReader = new DelimitedTextReader(reader, syntax, header);
            ListAggregator<StringArray> consumer = new ListAggregator<>();
            PipeLine.from(textReader).target(consumer);
            textReader.run();
            List<StringArray> list = consumer.getResult();
            assertEquals(2, list.size());
            List<String> row1 = Arrays.asList("52936","CXX","37","Den Haag - Delft - Delfgauw","","3","","","http://www.connexxion.nl/dienstregeling/lijn?ID=W037");
            assertEquals(row1, list.get(0).asList());
            assertEquals("Centrum West - Noordhove v.v.", list.get(1).get(3));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
