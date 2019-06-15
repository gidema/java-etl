package nl.java_etl.dsl;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import nl.java_etl.core.StreamTransformer;

public interface PipeBuilder<T> {
    public <U> PipeBuilder<U> transform(StreamTransformer<T, U> transformer);

    public <U> PipeBuilder<U> transform(Function<T, U> transformation);

    public <U> PipeBuilder<U> transformToMany(Function<T, Stream<U>> transformation);

    public PipeBuilder<T> filter(Predicate<T> predicate);
}
