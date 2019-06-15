package nl.java_etl.core;

public interface StreamGenerator {

    default void onStart() {
        return;
    }

    /**
     * Check if anymore input elements are available.
     *
     * @return true if more elements are available; false otherwise
     */
    public boolean tryAdvance();

    public <T> StreamProducer<T> generate(Class<T> clazz);


    /**
     * Process the next element.
     */
    //    public void next();
}
