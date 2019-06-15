package nl.java_etl.core;

public class CompoundObject {
    private final CompoundType type;
    private final Object[] data;

    public CompoundObject(CompoundType type) {
        this.type = type;
        this.data = new Object[type.getSize()];
    }

    public CompoundType getType() {
        return type;
    }

    public Object[] getData() {
        return data;
    }
}
