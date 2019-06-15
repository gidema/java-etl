package nl.java_etl.core;

import nl.java_etl.EtlTransaction;

public interface Transactional {
    public EtlTransaction getTransaction();
}
