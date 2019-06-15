package nl.java_etl.binding;

import nl.java_etl.core.StringArray;

public interface SATypeBinding<B> {
    void map(StringArray sa, B b);
}
