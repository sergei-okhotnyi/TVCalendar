package dev.okhotny.TVCalendar;

public class FinalWrapper<T> {
    public T value;

    public FinalWrapper() {
    }

    public FinalWrapper(T item) {
        value = item;
    }
}
