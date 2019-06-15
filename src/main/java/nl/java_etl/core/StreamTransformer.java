package nl.java_etl.core;

import nl.java_etl.core.impl.PipeLine;

public interface StreamTransformer<T, U> extends StreamConsumer<T>, StreamProducer<U> {

    @Override
    default void setPipeLine(PipeLine pipeLine) {
        return;
    }
}
