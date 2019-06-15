package nl.java_etl.core.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamProducer;
import nl.java_etl.core.StreamTransformer;
import nl.java_etl.filter.PredicateFilter;

public class PipeBuilder {
    final PipeLineBuilder pipeLineBuilder;
    final StreamProducer<?> producer;
    final List<StreamTransformer<?, ?>> transformers = new LinkedList<>();
    final List<StreamConsumer<?>> targets = new LinkedList<>();

    public <T> PipeBuilder(PipeLineBuilder pipeLineBuilder, StreamProducer<T> producer) {
        this.pipeLineBuilder = pipeLineBuilder;
        this.producer = producer;
    }

    public static <T> Stage<T> from(PipeLineBuilder pipeLineBuilder, StreamProducer<T> producer) {
        return new PipeBuilder(pipeLineBuilder, producer).start();
    }

    public <T> Stage<T> start() {
        return new Stage<>();
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
            return this;
        }

        public PipeLineBuilder target(StreamConsumer<T> consumer) {
            PipeBuilder pb = PipeBuilder.this;
            pb.targets.add(consumer);
            pb.pipeLineBuilder.targets.add(consumer);
            return pb.pipeLineBuilder.addPipe(pb);
        }

        @SuppressWarnings("unchecked")
        public PipeLineBuilder target(StreamConsumer<T> ... consumers) {
            for(StreamConsumer<T> consumer : consumers) {
                PipeBuilder.this.targets.add(consumer);
            }
            return PipeBuilder.this.pipeLineBuilder;
        }
    }

    //    public class Target<T> {
    //        @SuppressWarnings("unchecked")
    //        public Target<T> target(StreamConsumer<T> consumer) {
    //            PipeBuilder.this.targets.add((StreamConsumer<Object>) consumer);
    //            return this;
    //        }
    //
    //        public PipeBuilder build() {
    //            return PipeBuilder.this;
    //        }
    //    }
    //
    public Pipe build(PipeLine pipeLine) {
        return new Pipe(pipeLine, producer, transformers, targets);
    }

    @SuppressWarnings("static-method")
    public void handleException(Exception e) {
        e.printStackTrace();
    }
}
