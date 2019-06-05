// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.vm;

import java.lang.ref.WeakReference;
import se.krka.kahlua.stdlib.BaseLib;

public final class LuaTableImpl implements LuaTable
{
    private static final int[] log_2;
    private int freeIndex;
    private Object keyIndexCacheKey;
    private int keyIndexCacheValue;
    private Object[] keys;
    private LuaTable metatable;
    private int[] next;
    private Object[] values;
    private boolean weakKeys;
    private boolean weakValues;
    
    static {
        log_2 = new int[] { 0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
    }
    
    public LuaTableImpl() {
        this.keyIndexCacheValue = -1;
        this.keys = new Object[1];
        this.values = new Object[1];
        this.next = new int[1];
        this.freeIndex = 1;
    }
    
    private final Object __getKey(final int n) {
        Object unref = this.keys[n];
        if (this.weakKeys) {
            unref = this.unref(unref);
        }
        return unref;
    }
    
    private final Object __getValue(final int n) {
        Object unref = this.values[n];
        if (this.weakValues) {
            unref = this.unref(unref);
        }
        return unref;
    }
    
    private final void __setKey(final int n, final Object o) {
        Object ref = o;
        if (this.weakKeys) {
            ref = this.ref(o);
        }
        this.keys[n] = ref;
    }
    
    private final void __setValue(final int n, final Object o) {
        Object ref = o;
        if (this.weakValues) {
            ref = this.ref(o);
        }
        this.values[n] = ref;
    }
    
    private boolean canBeWeakObject(final Object o) {
        return o != null && !(o instanceof String) && !(o instanceof Double) && !(o instanceof Boolean);
    }
    
    public static void checkKey(final Object o) {
        BaseLib.luaAssert(o != null, "table index is nil");
    }
    
    private void fixWeakRefs(final Object[] array, final boolean b) {
        for (int i = array.length - 1; i >= 0; --i) {
            final Object o = array[i];
            Object o2;
            if (b) {
                o2 = this.ref(o);
            }
            else {
                o2 = this.unref(o);
            }
            array[i] = o2;
        }
    }
    
    private int getHashIndex(final Object keyIndexCacheKey) {
        int n;
        if (keyIndexCacheKey == this.keyIndexCacheKey) {
            n = this.keyIndexCacheValue;
        }
        else {
            final int keyIndexCacheValue = n = this.hash_primitiveFindKey(keyIndexCacheKey, this.getMP(keyIndexCacheKey));
            if (!this.weakKeys) {
                this.keyIndexCacheKey = keyIndexCacheKey;
                this.keyIndexCacheValue = keyIndexCacheValue;
                n = keyIndexCacheValue;
            }
        }
        return n;
    }
    
    private int getMP(final Object o) {
        return luaHashcode(o) & this.keys.length - 1;
    }
    
    private final int hash_primitiveFindKey(final Object o, int n) {
        final int n2 = -1;
        Object obj = this.__getKey(n);
        if (obj == null) {
            n = n2;
        }
        else if (o instanceof Double) {
            int n3;
            for (double fromDouble = LuaState.fromDouble(o); !(obj instanceof Double) || fromDouble != LuaState.fromDouble(obj); obj = this.__getKey(n3), n = n3) {
                n3 = this.next[n];
                n = n2;
                if (n3 == -1) {
                    break;
                }
            }
        }
        else {
            Object _getKey = obj;
            int n4 = n;
            if (o instanceof String) {
                while (!o.equals(obj)) {
                    final int n5 = this.next[n];
                    n = n2;
                    if (n5 == -1) {
                        break;
                    }
                    obj = this.__getKey(n5);
                    n = n5;
                }
            }
            else {
                while (o != _getKey) {
                    n4 = this.next[n4];
                    if (n4 == -1) {
                        n = n2;
                        return n;
                    }
                    _getKey = this.__getKey(n4);
                }
                n = n4;
            }
        }
        return n;
    }
    
    private final int hash_primitiveNewKey(final Object o, int freeIndex) {
        this.keyIndexCacheKey = null;
        this.keyIndexCacheValue = -1;
        final Object _getKey = this.__getKey(freeIndex);
        if (_getKey == null) {
            this.__setKey(freeIndex, o);
            this.next[freeIndex] = -1;
        }
        else {
            int mp = 0;
            Label_0128: {
                Label_0035: {
                    break Label_0035;
                    try {
                        int freeIndex2;
                        do {
                            freeIndex2 = this.freeIndex - 1;
                            this.freeIndex = freeIndex2;
                        } while (this.__getKey(freeIndex2) != null);
                        mp = this.getMP(_getKey);
                        if (mp != freeIndex) {
                            break Label_0128;
                        }
                        this.__setKey(this.freeIndex, o);
                        this.next[this.freeIndex] = this.next[freeIndex];
                        this.next[freeIndex] = this.freeIndex;
                        freeIndex = this.freeIndex;
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        this.hash_rehash(o);
                        freeIndex = -1;
                    }
                }
                return freeIndex;
            }
            this.keys[this.freeIndex] = this.keys[freeIndex];
            this.values[this.freeIndex] = this.values[freeIndex];
            this.next[this.freeIndex] = this.next[freeIndex];
            this.__setKey(freeIndex, o);
            this.next[freeIndex] = -1;
            while (true) {
                final int n = this.next[mp];
                if (n == freeIndex) {
                    break;
                }
                mp = n;
            }
            this.next[mp] = this.freeIndex;
        }
        return freeIndex;
    }
    
    private void hash_rehash(Object o) {
        final boolean weakKeys = this.weakKeys;
        final boolean weakValues = this.weakValues;
        this.updateWeakSettings(false, false);
        final Object[] keys = this.keys;
        final Object[] values = this.values;
        final int length = keys.length;
        int n = 1;
        int n2;
        for (int i = length - 1; i >= 0; --i, n = n2) {
            n2 = n;
            if (this.keys[i] != null) {
                n2 = n;
                if (this.values[i] != null) {
                    n2 = n + 1;
                }
            }
        }
        int freeIndex;
        if ((freeIndex = nearestPowerOfTwo(n) * 2) < 2) {
            freeIndex = 2;
        }
        this.keys = new Object[freeIndex];
        this.values = new Object[freeIndex];
        this.next = new int[freeIndex];
        this.freeIndex = freeIndex;
        for (int j = length - 1; j >= 0; --j) {
            final Object o2 = keys[j];
            if (o2 != null) {
                o = values[j];
                if (o != null) {
                    this.rawset(o2, o);
                }
            }
        }
        this.updateWeakSettings(weakKeys, weakValues);
    }
    
    public static int luaHashcode(final Object o) {
        int n2;
        if (o instanceof Double) {
            final long n = Double.doubleToLongBits((double)o) & Long.MAX_VALUE;
            n2 = (int)(n >>> 32 ^ n);
        }
        else if (o instanceof String) {
            n2 = o.hashCode();
        }
        else {
            n2 = System.identityHashCode(o);
        }
        return n2;
    }
    
    private static int luaO_log2(int i) {
        int n = -1;
        while (i >= 256) {
            n += 8;
            i >>= 8;
        }
        return LuaTableImpl.log_2[i] + n;
    }
    
    private static int nearestPowerOfTwo(final int n) {
        return 1 << luaO_log2(n);
    }
    
    private static int neededBits(final int n) {
        return luaO_log2(n) + 1;
    }
    
    private Object nextHash(Object _getKey) {
        int i = 0;
        if (_getKey != null && (i = this.getHashIndex(_getKey) + 1) <= 0) {
            BaseLib.fail("invalid key to 'next'");
            _getKey = null;
        }
        else {
            while (i != this.keys.length) {
                _getKey = this.__getKey(i);
                if (_getKey != null && this.__getValue(i) != null) {
                    return _getKey;
                }
                ++i;
            }
            _getKey = null;
        }
        return _getKey;
    }
    
    private Object rawgetHash(Object _getValue) {
        final int hashIndex = this.getHashIndex(_getValue);
        if (hashIndex >= 0) {
            _getValue = this.__getValue(hashIndex);
        }
        else {
            _getValue = null;
        }
        return _getValue;
    }
    
    private void rawsetHash(final Object o, final Object o2) {
        int n;
        if ((n = this.getHashIndex(o)) < 0 && (n = this.hash_primitiveNewKey(o, this.getMP(o))) < 0) {
            this.rawset(o, o2);
        }
        else {
            this.__setValue(n, o2);
        }
    }
    
    private final Object ref(Object referent) {
        if (this.canBeWeakObject(referent)) {
            referent = new WeakReference(referent);
        }
        return referent;
    }
    
    private final Object unref(Object value) {
        if (this.canBeWeakObject(value)) {
            value = ((WeakReference)value).get();
        }
        return value;
    }
    
    private void updateWeakSettings(final boolean weakKeys, final boolean weakValues) {
        this.keyIndexCacheKey = null;
        this.keyIndexCacheValue = -1;
        if (weakKeys != this.weakKeys) {
            this.fixWeakRefs(this.keys, weakKeys);
            this.weakKeys = weakKeys;
        }
        if (weakValues != this.weakValues) {
            this.fixWeakRefs(this.values, weakValues);
            this.weakValues = weakValues;
        }
    }
    
    @Override
    public LuaTable getMetatable() {
        return this.metatable;
    }
    
    @Override
    public final int len() {
        int n = this.keys.length * 2;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n2;
            if (n2 >= n) {
                break;
            }
            final int n4 = n + n2 + 1 >> 1;
            if (this.rawget(n4) == null) {
                n = n4 - 1;
            }
            else {
                n2 = n4;
            }
        }
        while (this.rawget(n3 + 1) != null) {
            ++n3;
        }
        return n3;
    }
    
    @Override
    public final Object next(final Object o) {
        return this.nextHash(o);
    }
    
    public Object rawget(final int n) {
        return this.rawgetHash(LuaState.toDouble(n));
    }
    
    @Override
    public final Object rawget(final Object o) {
        checkKey(o);
        if (o instanceof Double) {
            BaseLib.luaAssert(!((Double)o).isNaN(), "table index is NaN");
        }
        return this.rawgetHash(o);
    }
    
    public void rawset(final int n, final Object o) {
        this.rawsetHash(LuaState.toDouble(n), o);
    }
    
    @Override
    public final void rawset(final Object o, final Object o2) {
        checkKey(o);
        this.rawsetHash(o, o2);
    }
    
    @Override
    public void setMetatable(final LuaTable metatable) {
        this.metatable = metatable;
        final boolean b = false;
        final boolean b2 = false;
        boolean b3 = b;
        boolean b4 = b2;
        if (metatable != null) {
            final Object rawget = metatable.rawget(BaseLib.MODE_KEY);
            b3 = b;
            b4 = b2;
            if (rawget != null) {
                b3 = b;
                b4 = b2;
                if (rawget instanceof String) {
                    final String s = (String)rawget;
                    b3 = (s.indexOf(107) >= 0);
                    b4 = (s.indexOf(118) >= 0);
                }
            }
        }
        this.updateWeakSettings(b3, b4);
    }
}
