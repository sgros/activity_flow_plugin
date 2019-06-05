package org.mapsforge.core.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final float LOAD_FACTOR = 0.6f;
    private static final long serialVersionUID = 1;
    private final int capacity;

    private static int calculateInitialCapacity(int capacity) {
        if (capacity >= 0) {
            return ((int) (((float) capacity) / LOAD_FACTOR)) + 2;
        }
        throw new IllegalArgumentException("capacity must not be negative: " + capacity);
    }

    public LRUCache(int capacity) {
        super(calculateInitialCapacity(capacity), LOAD_FACTOR, true);
        this.capacity = capacity;
    }

    /* Access modifiers changed, original: protected */
    public boolean removeEldestEntry(Entry<K, V> entry) {
        return size() > this.capacity;
    }
}
