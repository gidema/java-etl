package nl.java_etl.core;

import java.util.Map;
import java.util.Map.Entry;

public class CompoundType {
    private final String[] propertyNames;
    private final Class<?>[] propertyTypes;

    public CompoundType(Map<String, Class<?>> typeMap) {
        propertyNames = new String[typeMap.size()];
        propertyTypes = new Class<?>[typeMap.size()];
        int i = 0;
        for (Entry<String, Class<?>> entry: typeMap.entrySet()) {
            propertyNames[i] = entry.getKey();
            propertyTypes[i] = entry.getValue();
            i++;
        }
    }

    public int getSize() {
        return propertyNames.length;
    }

    public CompoundObject newObject() {
        return new CompoundObject(this);
    }
}
