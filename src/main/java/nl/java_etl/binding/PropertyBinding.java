package nl.java_etl.binding;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PropertyBinding<A, B, T> implements TypeBinding<A, B> {
    private final Function<A, T> getter;
    private final BiConsumer<B, T> setter;
    private final T defaultValue;

    public PropertyBinding(Function<A, T> getter, BiConsumer<B, T> setter) {
        this(getter, setter, null);
    }

    public PropertyBinding(Function<A, T> getter, BiConsumer<B, T> setter,
            T defaultValue) {
        super();
        this.getter = getter;
        this.setter = setter;
        this.defaultValue = defaultValue;
    }

    @Override
    public void map(A a, B b) {
        T value = getter.apply(a);
        setter.accept(b, value == null ? defaultValue : value);
    }
}
