package me.sword7.starmail.util.storage;

public interface CallbackQuery<T> {

    void onQueryDone(T t);

}
