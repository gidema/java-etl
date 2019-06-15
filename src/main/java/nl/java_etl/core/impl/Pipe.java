package nl.java_etl.core.impl;

import java.util.List;

import nl.java_etl.EtlTransaction;
import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamProducer;
import nl.java_etl.core.StreamTransformer;
import nl.java_etl.core.Transactional;

public class Pipe {
    final PipeLine pipeLine;
    final StreamProducer<?> producer;
    final List<StreamTransformer<?, ?>> transformers;
    final List<StreamConsumer<?>> targets;
    private boolean aborted;
    private String abortReason;

    public Pipe(PipeLine pipeLine, StreamProducer<?> producer,
            List<StreamTransformer<?, ?>> transformers,
            List<StreamConsumer<?>> targets) {
        super();
        this.pipeLine = pipeLine;
        this.producer = producer;
        this.transformers = transformers;
        this.targets = targets;
        producer.setPipeLine(pipeLine);
        transformers.forEach(t -> t.setPipeLine(pipeLine));
        targets.forEach(t -> t.setPipeLine(pipeLine));
        StreamProducer<?> p = producer;
        for (StreamTransformer<?, ?> transformer : transformers) {
            p.setGenericTarget(transformer);
            p = transformer;
        }
        if (targets.size() == 1) {
            p.setGenericTarget(targets.get(0));
        }
        else {
            StreamConsumer<?> consumer = new MultiTargetStreamConsumer(targets);
            p.setGenericTarget(consumer);
        }
        targets.forEach(target-> {
            if (target instanceof Transactional) {
                EtlTransaction transaction = ((Transactional)target).getTransaction();
                if (transaction != null) {
                    pipeLine.addTransaction(transaction);
                }
            }
        });
    }

    public boolean isAborted() {
        return aborted;
    }

    public String getAbortReason() {
        return abortReason;
    }

    /**
     * Abort the pipeline with a specified reason.
     *
     * @param reason
     */
    public void abort(String reason) {
        this.aborted = true;
        this.abortReason = reason;
    }

    /**
     * Convenience method to abort with a formatted message
     *
     * @param message
     * @param args
     */
    public void abort(String message, Object... args) {
        abort(String.format(message, args));
    }
}
