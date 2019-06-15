package nl.java_etl.core.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nl.java_etl.core.StreamConsumer;

public class ListCollector<T> implements StreamConsumer<T> {
    private List<T> result;

    @Override
    public void onStart() {
        this.result = new LinkedList<>();
    }

    @Override
    public void accept(T t) {
        result.add(t);
    }

    @Override
    public void onAbort() {
        this.result = Collections.emptyList();
    }

    public List<T> getResult() {
        return result;
    }
}
