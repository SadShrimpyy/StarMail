package me.sword7.starmail.util.storage;

import me.sword7.starmail.util.Scheduler;

import java.util.Map;

public abstract class SingleFileStorage<T, V extends ICopyable<V>> extends Storage<T, V> {

    public void fetchAllSync(CallbackQuery<Map<T, V>> callback) {
        ioOperation(() -> {
            Map<T, V> map = fetchAll();
            callback.onQueryDone(map);
        });
    }

    public void fetchAllAsync(final CallbackQuery<Map<T, V>> callback) {
        ioOperationAsync(() -> {
            Map<T, V> map = fetchAll();
            Scheduler.run(() -> {
                callback.onQueryDone(map);
            });
        });
    }

    protected abstract Map<T, V> fetchAll();

}
