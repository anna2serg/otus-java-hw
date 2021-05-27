package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {

    private Map<K, V> cache = new WeakHashMap<>();
    private List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach( listenerWeakReference -> {
            HwListener<K, V> listener = listenerWeakReference.get();
            if (Objects.nonNull(listener)) {
                listener.notify(key, value, HwCacheAction.ADD);
            }
        });
    }

    @Override
    public void remove(K key) {
        var value = cache.get(key);
        cache.remove(key);
        listeners.forEach( listenerWeakReference -> {
            HwListener<K, V> listener = listenerWeakReference.get();
            if (Objects.nonNull(listener)) {
                listener.notify(key, value,HwCacheAction.REMOVE);
            }
        });
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(new WeakReference<>(listener));
    }
}
