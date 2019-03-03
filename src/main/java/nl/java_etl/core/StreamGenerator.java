package nl.java_etl.core;

import java.io.IOException;

public interface StreamGenerator<T> extends StreamProducer<T> {
    public void run() throws IOException;
}
