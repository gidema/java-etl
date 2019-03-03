package nl.java_etl.core;

public interface StreamProducer<T> {
    public void setTarget(StreamConsumer<T> consumer);
    public void setGenericTarget(StreamConsumer<?> consumer);
}
