package p009se.krka.kahlua.p010vm;

import java.lang.ref.WeakReference;
import locus.api.objects.geocaching.GeocachingData;
import p009se.krka.kahlua.stdlib.BaseLib;

/* renamed from: se.krka.kahlua.vm.LuaTableImpl */
public final class LuaTableImpl implements LuaTable {
    private static final int[] log_2 = new int[]{0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};
    private int freeIndex = 1;
    private Object keyIndexCacheKey;
    private int keyIndexCacheValue = -1;
    private Object[] keys = new Object[1];
    private LuaTable metatable;
    private int[] next = new int[1];
    private Object[] values = new Object[1];
    private boolean weakKeys;
    private boolean weakValues;

    private static int luaO_log2(int x) {
        int l = -1;
        while (x >= 256) {
            l += 8;
            x >>= 8;
        }
        return log_2[x] + l;
    }

    private static int neededBits(int x) {
        return LuaTableImpl.luaO_log2(x) + 1;
    }

    private static int nearestPowerOfTwo(int x) {
        return 1 << LuaTableImpl.luaO_log2(x);
    }

    private int getMP(Object key) {
        return LuaTableImpl.luaHashcode(key) & (this.keys.length - 1);
    }

    private final Object unref(Object o) {
        return !canBeWeakObject(o) ? o : ((WeakReference) o).get();
    }

    private final Object ref(Object o) {
        return !canBeWeakObject(o) ? o : new WeakReference(o);
    }

    private boolean canBeWeakObject(Object o) {
        return (o == null || (o instanceof String) || (o instanceof Double) || (o instanceof Boolean)) ? false : true;
    }

    private final Object __getKey(int index) {
        Object key = this.keys[index];
        if (this.weakKeys) {
            return unref(key);
        }
        return key;
    }

    private final void __setKey(int index, Object key) {
        if (this.weakKeys) {
            key = ref(key);
        }
        this.keys[index] = key;
    }

    private final Object __getValue(int index) {
        Object value = this.values[index];
        if (this.weakValues) {
            return unref(value);
        }
        return value;
    }

    private final void __setValue(int index, Object value) {
        if (this.weakValues) {
            value = ref(value);
        }
        this.values[index] = value;
    }

    private final int hash_primitiveFindKey(Object key, int index) {
        Object currentKey = __getKey(index);
        if (currentKey == null) {
            return -1;
        }
        if (key instanceof Double) {
            double dkey = LuaState.fromDouble(key);
            while (true) {
                if ((currentKey instanceof Double) && dkey == LuaState.fromDouble(currentKey)) {
                    return index;
                }
                index = this.next[index];
                if (index == -1) {
                    return -1;
                }
                currentKey = __getKey(index);
            }
        } else if (key instanceof String) {
            while (!key.equals(currentKey)) {
                index = this.next[index];
                if (index == -1) {
                    return -1;
                }
                currentKey = __getKey(index);
            }
            return index;
        } else {
            while (key != currentKey) {
                index = this.next[index];
                if (index == -1) {
                    return -1;
                }
                currentKey = __getKey(index);
            }
            return index;
        }
    }

    private final int hash_primitiveNewKey(Object key, int mp) {
        this.keyIndexCacheKey = null;
        this.keyIndexCacheValue = -1;
        Object key2 = __getKey(mp);
        if (key2 == null) {
            __setKey(mp, key);
            this.next[mp] = -1;
            return mp;
        }
        int i;
        do {
            try {
                i = this.freeIndex - 1;
                this.freeIndex = i;
            } catch (ArrayIndexOutOfBoundsException e) {
                hash_rehash(key);
                return -1;
            }
        } while (__getKey(i) != null);
        int mp2 = getMP(key2);
        if (mp2 == mp) {
            __setKey(this.freeIndex, key);
            this.next[this.freeIndex] = this.next[mp];
            this.next[mp] = this.freeIndex;
            return this.freeIndex;
        }
        this.keys[this.freeIndex] = this.keys[mp];
        this.values[this.freeIndex] = this.values[mp];
        this.next[this.freeIndex] = this.next[mp];
        __setKey(mp, key);
        this.next[mp] = -1;
        int prev = mp2;
        while (true) {
            int tmp = this.next[prev];
            if (tmp == mp) {
                this.next[prev] = this.freeIndex;
                return mp;
            }
            prev = tmp;
        }
    }

    private void hash_rehash(Object newKey) {
        boolean oldWeakKeys = this.weakKeys;
        boolean oldWeakValues = this.weakValues;
        updateWeakSettings(false, false);
        Object[] oldKeys = this.keys;
        Object[] oldValues = this.values;
        int hashLength = oldKeys.length;
        int usedTotal = 1;
        int i = hashLength - 1;
        while (i >= 0) {
            if (!(this.keys[i] == null || this.values[i] == null)) {
                usedTotal++;
            }
            i--;
        }
        int hashCapacity = LuaTableImpl.nearestPowerOfTwo(usedTotal) * 2;
        if (hashCapacity < 2) {
            hashCapacity = 2;
        }
        this.keys = new Object[hashCapacity];
        this.values = new Object[hashCapacity];
        this.next = new int[hashCapacity];
        this.freeIndex = hashCapacity;
        for (i = hashLength - 1; i >= 0; i--) {
            Object key = oldKeys[i];
            if (key != null) {
                Object value = oldValues[i];
                if (value != null) {
                    rawset(key, value);
                }
            }
        }
        updateWeakSettings(oldWeakKeys, oldWeakValues);
    }

    public final void rawset(Object key, Object value) {
        LuaTableImpl.checkKey(key);
        rawsetHash(key, value);
    }

    private void rawsetHash(Object key, Object value) {
        int index = getHashIndex(key);
        if (index < 0) {
            index = hash_primitiveNewKey(key, getMP(key));
            if (index < 0) {
                rawset(key, value);
                return;
            }
        }
        __setValue(index, value);
    }

    public Object rawget(int index) {
        return rawgetHash(LuaState.toDouble((long) index));
    }

    public void rawset(int index, Object value) {
        rawsetHash(LuaState.toDouble((long) index), value);
    }

    public final Object rawget(Object key) {
        LuaTableImpl.checkKey(key);
        if (key instanceof Double) {
            BaseLib.luaAssert(!((Double) key).isNaN(), "table index is NaN");
        }
        return rawgetHash(key);
    }

    private Object rawgetHash(Object key) {
        int index = getHashIndex(key);
        if (index >= 0) {
            return __getValue(index);
        }
        return null;
    }

    private int getHashIndex(Object key) {
        if (key == this.keyIndexCacheKey) {
            return this.keyIndexCacheValue;
        }
        int index = hash_primitiveFindKey(key, getMP(key));
        if (this.weakKeys) {
            return index;
        }
        this.keyIndexCacheKey = key;
        this.keyIndexCacheValue = index;
        return index;
    }

    public static void checkKey(Object key) {
        BaseLib.luaAssert(key != null, "table index is nil");
    }

    private Object nextHash(Object key) {
        int index = 0;
        if (key != null) {
            index = getHashIndex(key) + 1;
            if (index <= 0) {
                BaseLib.fail("invalid key to 'next'");
                return null;
            }
        }
        while (index != this.keys.length) {
            Object next = __getKey(index);
            if (next != null && __getValue(index) != null) {
                return next;
            }
            index++;
        }
        return null;
    }

    public final Object next(Object key) {
        return nextHash(key);
    }

    public final int len() {
        int high = this.keys.length * 2;
        int low = 0;
        while (low < high) {
            int middle = ((high + low) + 1) >> 1;
            if (rawget(middle) == null) {
                high = middle - 1;
            } else {
                low = middle;
            }
        }
        while (rawget(low + 1) != null) {
            low++;
        }
        return low;
    }

    public static int luaHashcode(Object a) {
        if (a instanceof Double) {
            long l = Double.doubleToLongBits(((Double) a).doubleValue()) & Long.MAX_VALUE;
            return (int) ((l >>> 32) ^ l);
        } else if (a instanceof String) {
            return a.hashCode();
        } else {
            return System.identityHashCode(a);
        }
    }

    private void updateWeakSettings(boolean k, boolean v) {
        this.keyIndexCacheKey = null;
        this.keyIndexCacheValue = -1;
        if (k != this.weakKeys) {
            fixWeakRefs(this.keys, k);
            this.weakKeys = k;
        }
        if (v != this.weakValues) {
            fixWeakRefs(this.values, v);
            this.weakValues = v;
        }
    }

    private void fixWeakRefs(Object[] entries, boolean weak) {
        for (int i = entries.length - 1; i >= 0; i--) {
            Object o = entries[i];
            if (weak) {
                o = ref(o);
            } else {
                o = unref(o);
            }
            entries[i] = o;
        }
    }

    public LuaTable getMetatable() {
        return this.metatable;
    }

    public void setMetatable(LuaTable metatable) {
        this.metatable = metatable;
        boolean weakKeys = false;
        boolean weakValues = false;
        if (metatable != null) {
            String modeObj = metatable.rawget(BaseLib.MODE_KEY);
            if (modeObj != null && (modeObj instanceof String)) {
                String mode = modeObj;
                if (mode.indexOf(GeocachingData.CACHE_SOURCE_OPENCACHING_RO) >= 0) {
                    weakKeys = true;
                } else {
                    weakKeys = false;
                }
                weakValues = mode.indexOf(118) >= 0;
            }
        }
        updateWeakSettings(weakKeys, weakValues);
    }
}
