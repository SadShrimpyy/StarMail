package me.sword7.starmail.util.storage;

import me.sword7.starmail.util.Scheduler;

public abstract class MultiFileStorage<T, V extends ICopyable<V>> extends Storage<T, V> {

    public void fetchSync(T key, CallbackQuery<V> callback) {
        ioOperation(() -> {
            V value = fetch(key);
            callback.onQueryDone(value);
        });
    }

    public void fetchAsync(final T key, final CallbackQuery<V> callback) {
        ioOperationAsync(() -> {
            V value = fetch(key);
            Scheduler.run(() -> {
                callback.onQueryDone(value);
            });
        });
    }

    protected abstract V fetch(T key);

}
