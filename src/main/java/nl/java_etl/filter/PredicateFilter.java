package nl.java_etl.filter;

import java.util.function.Predicate;

public class PredicateFilter<T> extends BaseFilter<T> {
    private final Predicate<T> predicate;

    public PredicateFilter(Predicate<T> predicate) {
        super();
        this.predicate = predicate;
    }

    @Override
    public void accept(T t) {
        if (predicate.test(t)) {
            super.accept(t);
        }
    }
}
