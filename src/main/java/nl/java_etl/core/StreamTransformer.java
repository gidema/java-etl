package nl.java_etl.core;

public interface StreamTransformer<T, U> extends StreamConsumer<T>, StreamProducer<U> {
    //
}
