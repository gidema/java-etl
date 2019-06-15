package nl.java_etl.core;

public interface ProducingStreamGenerator<T> extends StreamGenerator, StreamProducer<T> {

    public StreamProducer<T> generate();
}
