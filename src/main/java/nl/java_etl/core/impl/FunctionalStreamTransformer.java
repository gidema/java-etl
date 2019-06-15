package nl.java_etl.core.impl;

import java.util.function.Function;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamTransformer;

public class FunctionalStreamTransformer<T, U>
implements StreamTransformer<T, U> {
    private StreamConsumer<U> target;
    private final Function<T, U> transformation;
    private boolean started = false;

    public FunctionalStreamTransformer(Function<T, U> transformation) {
        super();
        this.transformation = transformation;
    }

    @Override
    public void onStart() {
        started = true;
    }

    @Override
    public void accept(T t) {
        target.accept(transformation.apply(t));
    }

    @Override
    public void setTarget(StreamConsumer<U> target) {
        if (started)
            throw new UnsupportedOperationException(
                    "Cannot set the target, because the stream "
                            + "has already been started");
        this.target = target;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setGenericTarget(StreamConsumer<?> target) {
        if (started)
            throw new UnsupportedOperationException(
                    "Cannot set the target, because the stream "
                            + "has already been started");
        this.target = (StreamConsumer<U>) target;
    }

    public StreamConsumer<U> getTarget() {
        return target;
    }
}
