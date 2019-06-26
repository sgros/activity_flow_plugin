// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.core.util;

import java.util.Map;
import java.util.LinkedHashMap;

public class LRUCache<K, V> extends LinkedHashMap<K, V>
{
    private static final float LOAD_FACTOR = 0.6f;
    private static final long serialVersionUID = 1L;
    private final int capacity;
    
    public LRUCache(final int capacity) {
        super(calculateInitialCapacity(capacity), 0.6f, true);
        this.capacity = capacity;
    }
    
    private static int calculateInitialCapacity(final int i) {
        if (i < 0) {
            throw new IllegalArgumentException("capacity must not be negative: " + i);
        }
        return (int)(i / 0.6f) + 2;
    }
    
    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> entry) {
        return this.size() > this.capacity;
    }
}
