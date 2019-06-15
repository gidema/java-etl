package nl.java_etl.core.impl;

import java.util.ArrayList;
import java.util.List;

import nl.java_etl.core.StreamConsumer;

public class MultiTargetStreamConsumer implements StreamConsumer<Object> {
    private final List<StreamConsumer<Object>> targets;

    @SuppressWarnings("unchecked")
    public MultiTargetStreamConsumer(List<StreamConsumer<?>> targets) {
        super();
        this.targets = new ArrayList<>(targets.size());
        targets.forEach(t -> this.targets.add((StreamConsumer<Object>) t));
    }

    @Override
    public void accept(Object t) {
        targets.forEach(target-> target.accept(t));
    }
}
