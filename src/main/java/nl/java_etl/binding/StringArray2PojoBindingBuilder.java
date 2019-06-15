package nl.java_etl.binding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import nl.java_etl.core.StringArray;

public class StringArray2PojoBindingBuilder<B> {
    public ArrayList<String> columnNames;
    public List<SATypeBinding<B>> propertyBindings = new LinkedList<>();

    public StringArray2PojoBindingBuilder(List<String> columnNames) {
        super();
        this.columnNames = new ArrayList<>(columnNames);
    }

    public StringArray2PojoBindingBuilder<B> bind(String key,
            BiConsumer<B, String> setter) {
        return bind(key, setter, null);
    }

    public StringArray2PojoBindingBuilder<B> bind(String key,
            BiConsumer<B, String> setter, String defaultValue) {
        int index = columnNames.indexOf(key);
        if (index >= 0) {
            propertyBindings.add(new SAPropertyBinding<>(index, String::toString, setter, defaultValue));
        }
        return this;
    }

    public StringArray2PojoBindingBuilder<B> bindInteger(String key,
            BiConsumer<B, Integer> setter) {
        return bind(key, setter, (Integer)null);
    }

    public StringArray2PojoBindingBuilder<B> bind(String key,
            BiConsumer<B, Integer> setter, Integer defaultValue) {
        int index = columnNames.indexOf(key);
        if (index >= 0) {
            propertyBindings.add(new SAPropertyBinding<>(index, Integer::valueOf, setter, defaultValue));
        }
        return this;
    }

    public StringArray2PojoBindingBuilder<B> bindLong(String key,
            BiConsumer<B, Long> setter) {
        return bind(key, setter, null);
    }

    public StringArray2PojoBindingBuilder<B> bind(String key,
            BiConsumer<B, Long> setter, Long defaultValue) {
        int index = columnNames.indexOf(key);
        if (index >= 0) {
            propertyBindings.add(new SAPropertyBinding<>(index, Long::valueOf, setter, defaultValue));
        }
        return this;
    }

    public StringArray2PojoBinding<B> build() {
        return new StringArray2PojoBinding<>(propertyBindings);
    }

    public Function<StringArray, B> asFunction(Supplier<B> factory) {
        return new MappingFunction(factory, build());
    }

    public class MappingFunction implements Function<StringArray, B> {
        private final Supplier<B> supplier;
        private final StringArray2PojoBinding<B> binding;

        MappingFunction(Supplier<B> supplier, StringArray2PojoBinding<B> binding) {
            super();
            this.supplier = supplier;
            this.binding = binding;
        }

        @Override
        public B apply(StringArray sa) {
            B b = supplier.get();
            binding.map(sa, b);
            return b;
        }
    }
}