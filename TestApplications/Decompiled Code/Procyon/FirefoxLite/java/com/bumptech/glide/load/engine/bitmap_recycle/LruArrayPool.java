// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.TreeMap;
import android.util.Log;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.Map;

public final class LruArrayPool implements ArrayPool
{
    private final Map<Class<?>, ArrayAdapterInterface<?>> adapters;
    private int currentSize;
    private final GroupedLinkedMap<Key, Object> groupedMap;
    private final KeyPool keyPool;
    private final int maxSize;
    private final Map<Class<?>, NavigableMap<Integer, Integer>> sortedSizes;
    
    public LruArrayPool() {
        this.groupedMap = new GroupedLinkedMap<Key, Object>();
        this.keyPool = new KeyPool();
        this.sortedSizes = new HashMap<Class<?>, NavigableMap<Integer, Integer>>();
        this.adapters = new HashMap<Class<?>, ArrayAdapterInterface<?>>();
        this.maxSize = 4194304;
    }
    
    public LruArrayPool(final int maxSize) {
        this.groupedMap = new GroupedLinkedMap<Key, Object>();
        this.keyPool = new KeyPool();
        this.sortedSizes = new HashMap<Class<?>, NavigableMap<Integer, Integer>>();
        this.adapters = new HashMap<Class<?>, ArrayAdapterInterface<?>>();
        this.maxSize = maxSize;
    }
    
    private void decrementArrayOfSize(final int n, final Class<?> clazz) {
        final NavigableMap<Integer, Integer> sizesForAdapter = this.getSizesForAdapter(clazz);
        final Integer n2 = sizesForAdapter.get(n);
        if (n2 != null) {
            if (n2 == 1) {
                sizesForAdapter.remove(n);
            }
            else {
                sizesForAdapter.put(n, n2 - 1);
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Tried to decrement empty size, size: ");
        sb.append(n);
        sb.append(", this: ");
        sb.append(this);
        throw new NullPointerException(sb.toString());
    }
    
    private void evict() {
        this.evictToSize(this.maxSize);
    }
    
    private void evictToSize(final int n) {
        while (this.currentSize > n) {
            final Object removeLast = this.groupedMap.removeLast();
            Preconditions.checkNotNull(removeLast);
            final ArrayAdapterInterface<Object> adapterFromObject = this.getAdapterFromObject(removeLast);
            this.currentSize -= adapterFromObject.getArrayLength(removeLast) * adapterFromObject.getElementSizeInBytes();
            this.decrementArrayOfSize(adapterFromObject.getArrayLength(removeLast), removeLast.getClass());
            if (Log.isLoggable(adapterFromObject.getTag(), 2)) {
                final String tag = adapterFromObject.getTag();
                final StringBuilder sb = new StringBuilder();
                sb.append("evicted: ");
                sb.append(adapterFromObject.getArrayLength(removeLast));
                Log.v(tag, sb.toString());
            }
        }
    }
    
    private <T> ArrayAdapterInterface<T> getAdapterFromObject(final T t) {
        return this.getAdapterFromType(t.getClass());
    }
    
    private <T> ArrayAdapterInterface<T> getAdapterFromType(final Class<T> clazz) {
        ArrayAdapterInterface<?> arrayAdapterInterface;
        if ((arrayAdapterInterface = this.adapters.get(clazz)) == null) {
            if (clazz.equals(int[].class)) {
                arrayAdapterInterface = new IntegerArrayAdapter();
            }
            else {
                if (!clazz.equals(byte[].class)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("No array pool found for: ");
                    sb.append(clazz.getSimpleName());
                    throw new IllegalArgumentException(sb.toString());
                }
                arrayAdapterInterface = new ByteArrayAdapter();
            }
            this.adapters.put(clazz, arrayAdapterInterface);
        }
        return (ArrayAdapterInterface<T>)arrayAdapterInterface;
    }
    
    private <T> T getArrayForKey(final Key key) {
        return (T)this.groupedMap.get(key);
    }
    
    private NavigableMap<Integer, Integer> getSizesForAdapter(final Class<?> clazz) {
        NavigableMap<Integer, Integer> navigableMap;
        if ((navigableMap = this.sortedSizes.get(clazz)) == null) {
            navigableMap = new TreeMap<Integer, Integer>();
            this.sortedSizes.put(clazz, navigableMap);
        }
        return navigableMap;
    }
    
    private boolean isNoMoreThanHalfFull() {
        return this.currentSize == 0 || this.maxSize / this.currentSize >= 2;
    }
    
    private boolean isSmallEnoughForReuse(final int n) {
        return n <= this.maxSize / 2;
    }
    
    private boolean mayFillRequest(final int n, final Integer n2) {
        return n2 != null && (this.isNoMoreThanHalfFull() || n2 <= n * 8);
    }
    
    @Override
    public void clearMemory() {
        synchronized (this) {
            this.evictToSize(0);
        }
    }
    
    @Override
    public <T> T get(final int n, final Class<T> clazz) {
        final ArrayAdapterInterface<T> adapterFromType = this.getAdapterFromType(clazz);
        synchronized (this) {
            final Integer n2 = this.getSizesForAdapter(clazz).ceilingKey(n);
            Key key;
            if (this.mayFillRequest(n, n2)) {
                key = this.keyPool.get(n2, clazz);
            }
            else {
                key = this.keyPool.get(n, clazz);
            }
            final Object arrayForKey = this.getArrayForKey(key);
            if (arrayForKey != null) {
                this.currentSize -= adapterFromType.getArrayLength((T)arrayForKey) * adapterFromType.getElementSizeInBytes();
                this.decrementArrayOfSize(adapterFromType.getArrayLength((T)arrayForKey), clazz);
            }
            // monitorexit(this)
            T array;
            if ((array = (T)arrayForKey) == null) {
                if (Log.isLoggable(adapterFromType.getTag(), 2)) {
                    final String tag = adapterFromType.getTag();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Allocated ");
                    sb.append(n);
                    sb.append(" bytes");
                    Log.v(tag, sb.toString());
                }
                array = adapterFromType.newArray(n);
            }
            return array;
        }
    }
    
    @Override
    public <T> void put(final T t, final Class<T> clazz) {
        synchronized (this) {
            final ArrayAdapterInterface<T> adapterFromType = this.getAdapterFromType(clazz);
            final int arrayLength = adapterFromType.getArrayLength(t);
            final int n = adapterFromType.getElementSizeInBytes() * arrayLength;
            if (!this.isSmallEnoughForReuse(n)) {
                return;
            }
            final Key value = this.keyPool.get(arrayLength, clazz);
            this.groupedMap.put(value, t);
            final NavigableMap<Integer, Integer> sizesForAdapter = this.getSizesForAdapter(clazz);
            final Integer n2 = sizesForAdapter.get(value.size);
            final int size = value.size;
            int i = 1;
            if (n2 != null) {
                i = 1 + n2;
            }
            sizesForAdapter.put(size, i);
            this.currentSize += n;
            this.evict();
        }
    }
    
    @Override
    public void trimMemory(final int n) {
        // monitorenter(this)
        Label_0042: {
            Label_0019: {
                if (n >= 40) {
                    Label_0038: {
                        try {
                            this.clearMemory();
                            break Label_0042;
                        }
                        finally {
                            break Label_0038;
                        }
                        break Label_0019;
                    }
                }
                // monitorexit(this)
            }
            if (n >= 20) {
                this.evictToSize(this.maxSize / 2);
            }
        }
    }
    // monitorexit(this)
    
    private static final class Key implements Poolable
    {
        private Class<?> arrayClass;
        private final KeyPool pool;
        int size;
        
        Key(final KeyPool pool) {
            this.pool = pool;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof Key;
            final boolean b2 = false;
            if (b) {
                final Key key = (Key)o;
                boolean b3 = b2;
                if (this.size == key.size) {
                    b3 = b2;
                    if (this.arrayClass == key.arrayClass) {
                        b3 = true;
                    }
                }
                return b3;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            final int size = this.size;
            int hashCode;
            if (this.arrayClass != null) {
                hashCode = this.arrayClass.hashCode();
            }
            else {
                hashCode = 0;
            }
            return size * 31 + hashCode;
        }
        
        void init(final int size, final Class<?> arrayClass) {
            this.size = size;
            this.arrayClass = arrayClass;
        }
        
        @Override
        public void offer() {
            this.pool.offer(this);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Key{size=");
            sb.append(this.size);
            sb.append("array=");
            sb.append(this.arrayClass);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static final class KeyPool extends BaseKeyPool<Key>
    {
        KeyPool() {
        }
        
        @Override
        protected Key create() {
            return new Key(this);
        }
        
        Key get(final int n, final Class<?> clazz) {
            final Key key = this.get();
            key.init(n, clazz);
            return key;
        }
    }
}
