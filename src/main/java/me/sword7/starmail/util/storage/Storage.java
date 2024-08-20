package me.sword7.starmail.util.storage;

import me.sword7.starmail.util.Scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class Storage<T, V extends ICopyable<V>> {

    protected void ioOperationAsync(Runnable r) {
        Scheduler.runAsync(() -> {
            ioOperation(r);
        });
    }

    protected synchronized void ioOperation(Runnable r) {
        r.run();
    }

    public void storeAsync(final Map<T, V> data, CallbackUpdate callback) {

        final Map<T, V> dataCopy = new HashMap<>();
        for (Map.Entry<T, V> entry : data.entrySet()) {
            V value = entry.getValue();
            V vCopy = value != null ? value.copy() : null;
            dataCopy.put(entry.getKey(), vCopy);
        }
        ioOperationAsync(() -> {
            store(dataCopy);
            dataCopy.clear();
            Scheduler.run(() -> {
                callback.onUpdateDone();
            });
        });
    }

    public void storeSync(Map<T, V> data) {
        ioOperation(() -> {
            store(data);
        });
    }

    protected abstract void store(Map<T, V> data);

    protected void specialFetchAsync(Supplier<V> supplier, CallbackQuery<V> callback) {
        ioOperationAsync(() -> {
            final V value = supplier.get();
            Scheduler.run(() -> {
                callback.onQueryDone(value);
            });
        });
    }

}
