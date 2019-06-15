package nl.java_etl.binding;

public interface TypeBinding<A, B> {
    void map(A a, B b);
}
