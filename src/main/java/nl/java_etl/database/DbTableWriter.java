package nl.java_etl.database;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.Transactional;

public interface DbTableWriter<T> extends StreamConsumer<T>, Transactional {
    //
}
