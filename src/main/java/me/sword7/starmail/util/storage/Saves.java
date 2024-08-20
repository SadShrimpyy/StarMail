package me.sword7.starmail.util.storage;

import me.sword7.starmail.util.BulkTask;

import java.util.HashMap;
import java.util.Map;

public class Saves<T, V extends ICopyable<V>> extends BulkTask {

    private Map<T, V> unsaved = new HashMap<>();
    private Map<T, V> beingSaved = new HashMap<>();
    private Storage<T, V> storage;
    private Runnable onSaveFinalized = () -> {
    };

    public Saves(Storage<T, V> storage) {
        super(6, 9);
        this.storage = storage;
    }

    public void setOnSaveFinalized(Runnable onSaveFinalized) {
        if (onSaveFinalized != null) {
            this.onSaveFinalized = onSaveFinalized;
        } else {
            this.onSaveFinalized = () -> {
            };
        }
    }

    public void add(T key, V value) {
        unsaved.put(key, value);
    }

    public void commit() {
        onExtend();
    }

    public void commit(T key, V value) {
        add(key, value);
        commit();
    }

    @Override
    protected void runTask() {
        beingSaved.clear();
        beingSaved.putAll(unsaved);
        storage.storeAsync(unsaved, () -> {
            beingSaved.clear();
            onSaveFinalized.run();
        });
        unsaved.clear();
    }

    public void shutdown() {
        cancel();
        unsaved.putAll(beingSaved);
        beingSaved.clear();
        storage.storeSync(unsaved);
        unsaved.clear();
    }

    public boolean containsKey(T key) {
        if (unsaved.containsKey(key)) {
            return true;
        } else if (beingSaved.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }

    public V get(T key) {
        if (unsaved.containsKey(key)) {
            return unsaved.get(key);
        } else if (beingSaved.containsKey(key)) {
            return beingSaved.get(key);
        } else {
            return null;
        }
    }

}
