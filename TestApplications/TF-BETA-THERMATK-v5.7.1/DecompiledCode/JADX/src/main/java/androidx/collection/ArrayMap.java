package androidx.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
    MapCollections<K, V> mCollections;

    /* renamed from: androidx.collection.ArrayMap$1 */
    class C33011 extends MapCollections<K, V> {
        C33011() {
        }

        /* Access modifiers changed, original: protected */
        public int colGetSize() {
            return ArrayMap.this.mSize;
        }

        /* Access modifiers changed, original: protected */
        public Object colGetEntry(int i, int i2) {
            return ArrayMap.this.mArray[(i << 1) + i2];
        }

        /* Access modifiers changed, original: protected */
        public int colIndexOfKey(Object obj) {
            return ArrayMap.this.indexOfKey(obj);
        }

        /* Access modifiers changed, original: protected */
        public int colIndexOfValue(Object obj) {
            return ArrayMap.this.indexOfValue(obj);
        }

        /* Access modifiers changed, original: protected */
        public Map<K, V> colGetMap() {
            return ArrayMap.this;
        }

        /* Access modifiers changed, original: protected */
        public void colPut(K k, V v) {
            ArrayMap.this.put(k, v);
        }

        /* Access modifiers changed, original: protected */
        public V colSetValue(int i, V v) {
            return ArrayMap.this.setValueAt(i, v);
        }

        /* Access modifiers changed, original: protected */
        public void colRemoveAt(int i) {
            ArrayMap.this.removeAt(i);
        }

        /* Access modifiers changed, original: protected */
        public void colClear() {
            ArrayMap.this.clear();
        }
    }

    public ArrayMap(int i) {
        super(i);
    }

    private MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new C33011();
        }
        return this.mCollections;
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        ensureCapacity(this.mSize + map.size());
        for (Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public Set<Entry<K, V>> entrySet() {
        return getCollection().getEntrySet();
    }

    public Set<K> keySet() {
        return getCollection().getKeySet();
    }

    public Collection<V> values() {
        return getCollection().getValues();
    }
}
