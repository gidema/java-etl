package nl.java_etl.core.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamGenerator;
import nl.java_etl.core.StreamProducer;
import nl.java_etl.core.StreamTransformer;
import nl.java_etl.filter.PredicateFilter;

public class PipeLine {
    StreamGenerator<?> generator;
    final List<StreamTransformer<?, ?>> transformers = new LinkedList<>();

    private PipeLine() {
    }

    public static <T> Stage<T> from(StreamGenerator<T> generator) {
        return new PipeLine().start(generator);
    }

    public <T> Stage<T> start(StreamGenerator<?> generator1) {
        this.generator = generator1;
        return new Stage<>();
    }

    public void run() throws IOException {
        this.generator.run();
    }

    public class Stage<T> {
        public <U> Stage<U> transform(StreamTransformer<T, U> transformer) {
            transformers.add(transformer);
            return new Stage<>();
        }

        public <U> Stage<U> transform(Function<T, U> transformation) {
            transformers.add(new FunctionalStreamTransformer<>(transformation));
            return new Stage<>();
        }

        public <U> Stage<U> transformToMany(Function<T, Stream<U>> transformation) {
            transformers.add(new FunctionalOneToManyTransformer<>(transformation));
            return new Stage<>();
        }

        public Stage<T> filter(Predicate<T> predicate) {
            transformers.add(new PredicateFilter<>(predicate));
            return new Stage<>();
        }

        public PipeLine target(StreamConsumer<T> consumer) {
            StreamProducer<?> producer = generator;
            for (StreamTransformer<?, ?> transformer : transformers) {
                producer.setGenericTarget(transformer);
                producer = transformer;
            }
            producer.setGenericTarget(consumer);
            return PipeLine.this;
        }
    }
}
