package nl.java_etl.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface StringArray {
    public static StringArray EMPTY = new Empty();

    public int getSize();
    public List<String> asList();
    public String get(int index) throws IndexOutOfBoundsException;

    public class Impl implements StringArray {
        final String[] array;

        public Impl(int size) {
            super();
            this.array = new String[size];
        }

        @Override
        public int getSize() {
            return array.length;
        }

        @Override
        public String get(int index) throws IndexOutOfBoundsException {
            return array[index];
        }

        @Override
        public void set(int i, String value) {
            array[i] = value;
        }

        @Override
        public List<String> asList() {
            return Arrays.asList(array);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || !(obj instanceof StringArray)) return false;
            return Arrays.equals(array, ((Impl)obj).array);
        }
    }

    public class Empty implements StringArray {
        Empty() {
        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public String get(int index) throws IndexOutOfBoundsException {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void set(int i, String value) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public List<String> asList() {
            return Collections.emptyList();
        }
    }

    public void set(int i, String value);
}
