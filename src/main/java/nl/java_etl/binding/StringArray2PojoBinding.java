package nl.java_etl.binding;

import java.util.ArrayList;
import java.util.List;

import nl.java_etl.core.StringArray;

public class StringArray2PojoBinding<B> {
    private final List<SATypeBinding<B>> propertyBindings;

    public StringArray2PojoBinding(List<SATypeBinding<B>> propertyBindings) {
        super();
        this.propertyBindings = new ArrayList<>(propertyBindings);
    }

    public void map(StringArray sa, B b) {
        propertyBindings.forEach(p -> p.map(sa, b));
    }
}
