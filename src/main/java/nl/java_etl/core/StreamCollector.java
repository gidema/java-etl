package nl.java_etl.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public interface StreamCollector<T, C extends Collection<T>> extends StreamConsumer<T> {
    public C getCollection();

    public class List_<T> implements StreamCollector<T, List<T>> {
        List<T> list;

        @Override
        public void onStart() {
            if (list == null) {
                this.list = new LinkedList<>();
            }
        }

        @Override
        public void onComplete() {
            //
        }

        @Override
        public void onError(Throwable error) {
            this.list = Collections.emptyList();
        }

        @Override
        public void accept(T t) {
            list.add(t);
        }

        @Override
        public List<T> getCollection() {
            return list;
        }
    }

    public class Set_<T> implements StreamCollector<T, Set<T>> {
        Set<T> set;

        @Override
        public void onStart() {
            if (set == null) {
                this.set = new HashSet<>();
            }
        }

        @Override
        public void onComplete() {
            //
        }

        @Override
        public void onError(Throwable error) {
            this.set = Collections.emptySet();
        }

        @Override
        public void accept(T t) {
            set.add(t);
        }

        @Override
        public Set<T> getCollection() {
            return set;
        }
    }
}
