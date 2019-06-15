package nl.java_etl;

public interface EtlTransaction {
    void onStart();

    void onComplete();

    void onRollBack();
}
