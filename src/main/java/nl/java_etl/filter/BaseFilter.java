package nl.java_etl.filter;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamFilter;

/**
 * Default implementation of StreamFilter to be used as a template for other filters.
 * Just pass through all messages untouched.
 * Prevent target from being changed once the stream has started.
 *
 * @author Gertjan Idema
 *
 */
public class BaseFilter<T> implements StreamFilter<T> {
    private StreamConsumer<T> target;
    private boolean started = false;

    public BaseFilter() {
        //
    }

    public BaseFilter(StreamConsumer<T> target) {
        super();
        this.target = target;
    }

    @Override
    public void onStart() {
        if (target == null) {
            throw new UnsupportedOperationException("No target consumer has been configured");
        }
        started = true;
        target.onStart();
    }

    @Override
    public void onComplete() {
        if (target == null) {
            throw new UnsupportedOperationException("No target consumer has been configured");
        }
        target.onComplete();
    }

    @Override
    public void accept(T t) {
        if (target == null) {
            throw new UnsupportedOperationException("No target consumer has been configured");
        }
        target.accept(t);
    }

    @Override
    public void setTarget(StreamConsumer<T> target) {
        if (started) throw new UnsupportedOperationException(
                "The target can not be changed because the stream has already been started.");
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setGenericTarget(StreamConsumer<?> target) {
        if (started) throw new UnsupportedOperationException(
                "The target can not be changed because the stream has already been started.");
        this.target = (StreamConsumer<T>) target;
    }
}
