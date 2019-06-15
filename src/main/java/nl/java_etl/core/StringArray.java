package nl.java_etl.core;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface StringArray {
    public static StringArray EMPTY = new Empty();

    public int getSize();
    public List<String> asList();
    public String get(int index) throws IndexOutOfBoundsException;
    public Integer getInteger(int i);
    public Long getLong(int i);
    public Integer getInteger(int i, Integer ifNull);
    public Double getDouble(int i, Double ifNull);
    public Long getLong(int i, Long ifNull);
    public Boolean getBoolean(int i, String falseString, String trueString);
    public Boolean getBoolean(int i, String falseString, String trueString, Boolean ifNull);
    public LocalTime getLocalTime(int i);

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

        public void set(int i, String value) {
            array[i] = value;
        }

        @Override
        public Integer getInteger(int i) {
            return Integer.parseInt(get(i));
        }

        @Override
        public Long getLong(int i) {
            return Long.parseLong(get(i));
        }

        @Override
        public Integer getInteger(int i, Integer ifNull) {
            String s = get(i);
            return (s == null || s.isEmpty()) ? ifNull : Integer.valueOf(s);
        }

        @Override
        public Long getLong(int i, Long ifNull) {
            String s = get(i);
            return (s == null || s.isEmpty()) ? ifNull : Long.valueOf(s);
        }

        @Override
        public Boolean getBoolean(int i, String falseString,
                String trueString) {
            String s = get(i);
            if (falseString.equals(s)) return false;
            if (trueString.equals(s)) return true;
            throw new IllegalArgumentException();
        }

        @Override
        public Boolean getBoolean(int i, String falseString,
                String trueString, Boolean ifNull) {
            String s = get(i);
            if (s == null || s.isEmpty()) return ifNull;
            if (falseString.equals(s)) return false;
            if (trueString.equals(s)) return true;
            throw new IllegalArgumentException();
        }

        @Override
        public Double getDouble(int i, Double ifNull) {
            String s = get(i);
            return s == null ? ifNull :Double.parseDouble(s);
        }

        @Override
        public LocalTime getLocalTime(int i) {
            return LocalTime.parse(get(i));
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
        public Integer getInteger(int i) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public Long getLong(int i) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public Boolean getBoolean(int i, String falseString,
                String trueString) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public Integer getInteger(int i, Integer ifNull) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public Long getLong(int i, Long ifNull) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public Boolean getBoolean(int i, String falseString, String trueString, Boolean ifNull) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public List<String> asList() {
            return Collections.emptyList();
        }

        @Override
        public Double getDouble(int i, Double ifNull) {
            return null;
        }

        @Override
        public LocalTime getLocalTime(int i) {
            return null;
        }
    }
}
