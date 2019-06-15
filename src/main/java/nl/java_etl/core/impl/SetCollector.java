package nl.java_etl.core.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nl.java_etl.core.StreamConsumer;

public class SetCollector<T> implements StreamConsumer<T> {
    private Set<T> result;

    @Override
    public void onStart() {
        this.result = new HashSet<>();
    }

    @Override
    public void accept(T t) {
        result.add(t);
    }

    @Override
    public void onAbort() {
        this.result = Collections.emptySet();
    }

    public Set<T> getResult() {
        return result;
    }
}
