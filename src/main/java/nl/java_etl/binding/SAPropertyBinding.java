package nl.java_etl.binding;

import java.util.function.BiConsumer;
import java.util.function.Function;

import nl.java_etl.core.StringArray;

public class SAPropertyBinding<B, T> implements SATypeBinding<B> {
    private final int index;
    private final BiConsumer<B, T> setter;
    private final Function<String, T> transformer;
    private final T defaultValue;

    public SAPropertyBinding(int index, Function<String, T> transformer, BiConsumer<B, T> setter) {
        this(index, transformer, setter, null);
    }

    public SAPropertyBinding(int index, Function<String, T> transformer, BiConsumer<B, T> setter,
            T defaultValue) {
        super();
        this.index = index;
        this.transformer = transformer;
        this.setter = setter;
        this.defaultValue = defaultValue;
    }

    @Override
    public void map(StringArray sa, B b) {
        T value = transformer.apply(sa.get(index));
        setter.accept(b, value == null ? defaultValue : value);
    }
}
