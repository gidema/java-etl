package nl.java_etl.binding;

import java.util.ArrayList;
import java.util.List;

public class PojoBinding<A, B> {
    private final List<TypeBinding<A, B>> propertyBindings;

    public PojoBinding(List<TypeBinding<A, B>> propertyBindings) {
        super();
        this.propertyBindings = new ArrayList<>(propertyBindings);
    }

    public void map(A a, B b) {
        propertyBindings.forEach(p -> p.map(a, b));
    }
}
