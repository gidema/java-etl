package nl.java_etl.core;

import nl.java_etl.core.impl.PipeLine;

public interface StreamProducer<T> {
    /**
     * @param pipeLine
     */
    public default void setPipeLine(PipeLine pipeLine) {
        return;
    }
    public void setTarget(StreamConsumer<T> consumer);
    public void setGenericTarget(StreamConsumer<?> consumer);
}
