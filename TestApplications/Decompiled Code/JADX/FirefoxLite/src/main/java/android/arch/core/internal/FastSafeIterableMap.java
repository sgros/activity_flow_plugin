package android.arch.core.internal;

import java.util.HashMap;
import java.util.Map.Entry;

public class FastSafeIterableMap<K, V> extends SafeIterableMap<K, V> {
    private HashMap<K, Entry<K, V>> mHashMap = new HashMap();

    /* Access modifiers changed, original: protected */
    public Entry<K, V> get(K k) {
        return (Entry) this.mHashMap.get(k);
    }

    public V putIfAbsent(K k, V v) {
        Entry entry = get(k);
        if (entry != null) {
            return entry.mValue;
        }
        this.mHashMap.put(k, put(k, v));
        return null;
    }

    public V remove(K k) {
        Object remove = super.remove(k);
        this.mHashMap.remove(k);
        return remove;
    }

    public boolean contains(K k) {
        return this.mHashMap.containsKey(k);
    }

    public Entry<K, V> ceil(K k) {
        return contains(k) ? ((Entry) this.mHashMap.get(k)).mPrevious : null;
    }
}
