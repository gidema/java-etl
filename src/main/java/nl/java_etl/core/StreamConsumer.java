package nl.java_etl.core;

import java.util.function.Consumer;

import nl.java_etl.core.impl.PipeLine;

public interface StreamConsumer<T> extends Consumer<T> {
    public static StreamConsumer<?> Empty = new EmptyConsumer<>();

    /**
     * Set the pipeline in which this StreamConsumer is included.
     * Access to the pipeline is used to report errors while processing the stream.
     *
     * @param pipeLine
     */
    public default void setPipeLine(PipeLine pipeLine) {
        return;
    }

    /**
     * Perform tasks at the start of the stream processing. Any assigned resources here should
     * be released in the onComplete and onAbort method.
     */
    public default void onStart() {
        return;
    }

    /**
     * Perform any necessary closing and/or clean-up task at completion of the stream.
     * Release all resources assigned in the onStart method.
     */
    public default void onComplete() {
        return;
    }

    /**
     * Perform any necessary cleanup tasks when the stream is aborted.
     * Release all resources assigned in the onStart method.
     */
    public default void onAbort() {
        return;
    }

    @SuppressWarnings("unchecked")
    public static <T1> StreamConsumer<T1> of(
            StreamConsumer<T1> consumer) {
        return consumer==null ? (StreamConsumer<T1>) Empty : consumer;
    }

    @SuppressWarnings("unchecked")
    public static <T1> StreamConsumer<T1> empty() {
        return (StreamConsumer<T1>) Empty;
    }

    public static class EmptyConsumer<T> implements StreamConsumer<T> {

        @Override
        public void accept(T t) {
            return;
        }
    }
}
