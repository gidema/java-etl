package nl.java_etl.csv.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestDelimitedLineReader.class,
    TestDelimitedTextReader.class,
    TestDelimitedTextWriter.class
})
public class TestSuite {
    //
}
