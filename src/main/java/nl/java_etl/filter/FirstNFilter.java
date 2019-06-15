package nl.java_etl.filter;


import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamFilter;

public class FirstNFilter<T> implements StreamFilter<T> {
    private final long headCount;
    private long processed = 0;
    private StreamConsumer<T> target;

    public FirstNFilter(long headCount) {
        super();
        this.headCount = headCount;
    }

    @Override
    public void accept(T t) {
        if (processed < headCount) {
            processed++;
            target.accept(t);
        }
    }

    @Override
    public void setTarget(StreamConsumer<T> target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setGenericTarget(StreamConsumer<?> target) {
        this.target = (StreamConsumer<T>) target;
    }
}
