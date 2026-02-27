package org.lexingtonchristian.ftc.snapshot;

public interface Device<T> {

    String getName();
    T getValue();

    default Snapshot<T> takeSnapshot() {
        return new Snapshot<>(getName(), getValue());
    }

    class Snapshot<T> {

        public final String name;
        public final T value;

        public Snapshot(String name, T value) {
            this.name = name;
            this.value = value;
        }

    }

}
