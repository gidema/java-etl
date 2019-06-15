package nl.java_etl.binding;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PojoBindingBuilder<A,B> {
    public List<TypeBinding<A, B>> propertyBindings = new LinkedList<>();

    public <T> PojoBindingBuilder<A, B> bind(Function<A, T> getter,
            BiConsumer<B, T> setter, T defaultValue) {
        propertyBindings.add(new PropertyBinding<>(getter, setter, defaultValue));
        return this;
    }

    public <T> PojoBindingBuilder<A, B> bind(Function<A, T> getter,
            BiConsumer<B, T> setter) {
        return bind(getter, setter, null);
    }

    public PojoBinding<A, B> build() {
        return new PojoBinding<>(propertyBindings);
    }

    public Function<A, B> asFunction(Supplier<B> factory) {
        return new MappingFunction(factory, build());
    }

    public class MappingFunction implements Function<A, B> {
        private final Supplier<B> supplier;
        private final PojoBinding<A, B> binding;

        MappingFunction(Supplier<B> supplier, PojoBinding<A, B> binding) {
            super();
            this.supplier = supplier;
            this.binding = binding;
        }

        @Override
        public B apply(A a) {
            B b = supplier.get();
            binding.map(a, b);
            return b;
        }
    }
}