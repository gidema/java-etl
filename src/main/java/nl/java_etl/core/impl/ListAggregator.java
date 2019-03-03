package nl.java_etl.core.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nl.java_etl.core.StreamConsumer;

public class ListAggregator<T> implements StreamConsumer<T> {
    private List<T> result;

    @Override
    public void accept(T t) {
        result.add(t);
    }

    @Override
    public void onStart() {
        this.result = new LinkedList<>();
    }

    @Override
    public void onComplete() {
        //
    }

    @Override
    public void onError(Throwable error) {
        this.result = Collections.emptyList();
    }

    public List<T> getResult() {
        return result;
    }
}
