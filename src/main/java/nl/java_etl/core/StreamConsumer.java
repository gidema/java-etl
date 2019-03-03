package nl.java_etl.core;

import java.util.function.Consumer;

public interface StreamConsumer<T> extends Consumer<T> {
    public static StreamConsumer<?> Empty = new EmptyConsumer<>();

    public void onStart();
    public void onComplete();
    public void onError(Throwable error);

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
        public void onStart() {
            return;
        }

        @Override
        public void accept(T t) {
            return;
        }

        @Override
        public void onComplete() {
            return;
        }

        @Override
        public void onError(Throwable error) {
            return;
        }
    }
}
