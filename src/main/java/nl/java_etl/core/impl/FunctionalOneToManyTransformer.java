package nl.java_etl.core.impl;

import java.util.function.Function;
import java.util.stream.Stream;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamTransformer;

public class FunctionalOneToManyTransformer<T, U>
implements StreamTransformer<T, U> {
    private StreamConsumer<U> target;
    private final Function<T, Stream<U>> transformation;
    private boolean started = false;

    public FunctionalOneToManyTransformer(Function<T, Stream<U>> transformation) {
        super();
        this.transformation = transformation;
    }

    @Override
    public void onStart() {
        started = true;
        target.onStart();
    }

    @Override
    public void onComplete() {
        target.onComplete();
    }

    @Override
    public void onError(Throwable error) {
        target.onError(error);
    }

    @Override
    public void accept(T t) {
        transformation.apply(t).forEach(target);
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
