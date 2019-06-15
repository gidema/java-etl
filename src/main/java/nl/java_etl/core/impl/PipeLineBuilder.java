package nl.java_etl.core.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nl.java_etl.EtlTransaction;
import nl.java_etl.core.ProducingStreamGenerator;
import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamGenerator;
import nl.java_etl.core.StreamProducer;
import nl.java_etl.core.StreamTransformer;
import nl.java_etl.core.impl.PipeBuilder.Stage;

public class PipeLineBuilder {
    private final List<PipeBuilder> pipeBuilders = new LinkedList<>();
    Set<EtlTransaction> transactions = new HashSet<>();
    StreamGenerator generator;
    final List<StreamTransformer<?, ?>> transformers = new LinkedList<>();
    final Set<StreamConsumer<?>> targets = new HashSet<>();

    public PipeLineBuilder(StreamGenerator generator) {
        this.generator = generator;
    }

    public <T> Stage<T> addPipe(Class<T> clazz) {
        return PipeBuilder.from(this, generator.generate(clazz));
    }

    public PipeLineBuilder addPipe(PipeBuilder pipeBuilder) {
        pipeBuilders.add(pipeBuilder);
        return this;
    }

    public static <T> Stage<T> from(ProducingStreamGenerator<T> generator1) {
        return PipeBuilder.from(new PipeLineBuilder(generator1), generator1.generate());
    }

    public static PipeLineBuilder from(StreamGenerator generator1) {
        return new PipeLineBuilder(generator1);
    }

    //    public <T> Stage<T> start(StreamProducer<T> producer) {
    //        PipeBuilder pipeBuilder = PipeBuilder.from(producer);
    //
    //    }

    //    public class Stage<T> {
    //        public <U> Stage<U> transform(StreamTransformer<T, U> transformer) {
    //            transformers.add(transformer);
    //            return new Stage<>();
    //        }
    //
    //        public <U> Stage<U> transform(Function<T, U> transformation) {
    //            transformers.add(new FunctionalStreamTransformer<>(transformation));
    //            return new Stage<>();
    //        }
    //
    //        public <U> Stage<U> transformToMany(Function<T, Stream<U>> transformation) {
    //            transformers.add(new FunctionalOneToManyTransformer<>(transformation));
    //            return new Stage<>();
    //        }
    //
    //        public Stage<T> filter(Predicate<T> predicate) {
    //            transformers.add(new PredicateFilter<>(predicate));
    //            return this;
    //        }
    //
    //        @SuppressWarnings("unchecked")
    //        public Target<T> target(StreamConsumer<T> consumer) {
    //            PipeLineBuilder.this.targets.add((StreamConsumer<Object>) consumer);
    //            return new Target<>();
    //        }
    //    }

    //    public class Target<T> {
    //        @SuppressWarnings("unchecked")
    //        public Target<T> target(StreamConsumer<T> consumer) {
    //            PipeLineBuilder.this.targets.add((StreamConsumer<Object>) consumer);
    //            return this;
    //        }

    public PipeLine build() {
        PipeLine pipeLine = new PipeLine(generator, transactions, targets);
        pipeBuilders.forEach(pb -> {
            Pipe pipe = pb.build(pipeLine);
        });
        return pipeLine;
    }

    @SuppressWarnings("static-method")
    public void handleException(Exception e) {
        e.printStackTrace();
    }

    public static class TypedPipeLineBuilder<T> {
        StreamProducer<T> producer;

        public TypedPipeLineBuilder(StreamProducer<T> producer) {
            super();
            this.producer = producer;
        }
    }
}
