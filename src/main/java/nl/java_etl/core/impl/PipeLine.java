package nl.java_etl.core.impl;

import java.util.Set;

import nl.java_etl.EtlTransaction;
import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamGenerator;

public class PipeLine implements Runnable {
    final Set<EtlTransaction> transactions;
    final StreamGenerator generator;
    final Set<StreamConsumer<?>> targets;
    private boolean aborted;
    private String abortReason;

    public PipeLine(StreamGenerator generator,
            Set<EtlTransaction> transactions,
            Set<StreamConsumer<?>> targets) {
        super();
        this.transactions = transactions;
        this.generator = generator;
        this.targets = targets;
    }

    @Override
    public void run() {
        transactions.forEach(EtlTransaction::onStart);
        generator.onStart();
        targets.forEach(StreamConsumer::onStart);
        while (!aborted && generator.tryAdvance()) {
            // Just keep on going
        }
        targets.forEach(t -> t.onComplete());
        transactions.forEach(t -> t.onComplete());
        if (aborted) {
            // TODO Report issues
        }
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

    public void addTransaction(EtlTransaction transaction) {
        transactions.add(transaction);
    }
}
