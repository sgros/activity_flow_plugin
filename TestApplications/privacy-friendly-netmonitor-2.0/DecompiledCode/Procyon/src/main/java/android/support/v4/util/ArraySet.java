// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.util;

import java.lang.reflect.Array;
import android.support.annotation.RestrictTo;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

public final class ArraySet<E> implements Collection<E>, Set<E>
{
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final int[] INT;
    private static final Object[] OBJECT;
    private static final String TAG = "ArraySet";
    static Object[] sBaseCache;
    static int sBaseCacheSize;
    static Object[] sTwiceBaseCache;
    static int sTwiceBaseCacheSize;
    Object[] mArray;
    MapCollections<E, E> mCollections;
    int[] mHashes;
    final boolean mIdentityHashCode;
    int mSize;
    
    static {
        INT = new int[0];
        OBJECT = new Object[0];
    }
    
    public ArraySet() {
        this(0, false);
    }
    
    public ArraySet(final int n) {
        this(n, false);
    }
    
    public ArraySet(final int n, final boolean mIdentityHashCode) {
        this.mIdentityHashCode = mIdentityHashCode;
        if (n == 0) {
            this.mHashes = ArraySet.INT;
            this.mArray = ArraySet.OBJECT;
        }
        else {
            this.allocArrays(n);
        }
        this.mSize = 0;
    }
    
    public ArraySet(final ArraySet<E> set) {
        this();
        if (set != null) {
            this.addAll((ArraySet<? extends E>)set);
        }
    }
    
    public ArraySet(final Collection<E> collection) {
        this();
        if (collection != null) {
            this.addAll((Collection<? extends E>)collection);
        }
    }
    
    private void allocArrays(final int n) {
        Label_0149: {
            if (n == 8) {
                synchronized (ArraySet.class) {
                    if (ArraySet.sTwiceBaseCache != null) {
                        final Object[] sTwiceBaseCache = ArraySet.sTwiceBaseCache;
                        this.mArray = sTwiceBaseCache;
                        ArraySet.sTwiceBaseCache = (Object[])sTwiceBaseCache[0];
                        this.mHashes = (int[])sTwiceBaseCache[1];
                        sTwiceBaseCache[0] = (sTwiceBaseCache[1] = null);
                        --ArraySet.sTwiceBaseCacheSize;
                        return;
                    }
                    break Label_0149;
                }
            }
            if (n == 4) {
                synchronized (ArraySet.class) {
                    if (ArraySet.sBaseCache != null) {
                        final Object[] sBaseCache = ArraySet.sBaseCache;
                        this.mArray = sBaseCache;
                        ArraySet.sBaseCache = (Object[])sBaseCache[0];
                        this.mHashes = (int[])sBaseCache[1];
                        sBaseCache[0] = (sBaseCache[1] = null);
                        --ArraySet.sBaseCacheSize;
                        return;
                    }
                }
            }
        }
        this.mHashes = new int[n];
        this.mArray = new Object[n];
    }
    
    private static void freeArrays(final int[] array, final Object[] array2, int i) {
        if (array.length == 8) {
            synchronized (ArraySet.class) {
                if (ArraySet.sTwiceBaseCacheSize < 10) {
                    array2[0] = ArraySet.sTwiceBaseCache;
                    array2[1] = array;
                    --i;
                    while (i >= 2) {
                        array2[i] = null;
                        --i;
                    }
                    ArraySet.sTwiceBaseCache = array2;
                    ++ArraySet.sTwiceBaseCacheSize;
                }
                return;
            }
        }
        if (array.length == 4) {
            synchronized (ArraySet.class) {
                if (ArraySet.sBaseCacheSize < 10) {
                    array2[0] = ArraySet.sBaseCache;
                    array2[1] = array;
                    --i;
                    while (i >= 2) {
                        array2[i] = null;
                        --i;
                    }
                    ArraySet.sBaseCache = array2;
                    ++ArraySet.sBaseCacheSize;
                }
            }
        }
    }
    
    private MapCollections<E, E> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<E, E>() {
                @Override
                protected void colClear() {
                    ArraySet.this.clear();
                }
                
                @Override
                protected Object colGetEntry(final int n, final int n2) {
                    return ArraySet.this.mArray[n];
                }
                
                @Override
                protected Map<E, E> colGetMap() {
                    throw new UnsupportedOperationException("not a map");
                }
                
                @Override
                protected int colGetSize() {
                    return ArraySet.this.mSize;
                }
                
                @Override
                protected int colIndexOfKey(final Object o) {
                    return ArraySet.this.indexOf(o);
                }
                
                @Override
                protected int colIndexOfValue(final Object o) {
                    return ArraySet.this.indexOf(o);
                }
                
                @Override
                protected void colPut(final E e, final E e2) {
                    ArraySet.this.add(e);
                }
                
                @Override
                protected void colRemoveAt(final int n) {
                    ArraySet.this.removeAt(n);
                }
                
                @Override
                protected E colSetValue(final int n, final E e) {
                    throw new UnsupportedOperationException("not a map");
                }
            };
        }
        return this.mCollections;
    }
    
    private int indexOf(final Object o, final int n) {
        final int mSize = this.mSize;
        if (mSize == 0) {
            return -1;
        }
        int binarySearch = ContainerHelpers.binarySearch(this.mHashes, mSize, n);
        if (binarySearch < 0) {
            return binarySearch;
        }
        if (o.equals(this.mArray[binarySearch])) {
            return binarySearch;
        }
        int n2;
        for (n2 = binarySearch + 1; n2 < mSize && this.mHashes[n2] == n; ++n2) {
            if (o.equals(this.mArray[n2])) {
                return n2;
            }
        }
        --binarySearch;
        while (binarySearch >= 0 && this.mHashes[binarySearch] == n) {
            if (o.equals(this.mArray[binarySearch])) {
                return binarySearch;
            }
            --binarySearch;
        }
        return ~n2;
    }
    
    private int indexOfNull() {
        final int mSize = this.mSize;
        if (mSize == 0) {
            return -1;
        }
        final int binarySearch = ContainerHelpers.binarySearch(this.mHashes, mSize, 0);
        if (binarySearch < 0) {
            return binarySearch;
        }
        if (this.mArray[binarySearch] == null) {
            return binarySearch;
        }
        int n;
        for (n = binarySearch + 1; n < mSize && this.mHashes[n] == 0; ++n) {
            if (this.mArray[n] == null) {
                return n;
            }
        }
        for (int n2 = binarySearch - 1; n2 >= 0 && this.mHashes[n2] == 0; --n2) {
            if (this.mArray[n2] == null) {
                return n2;
            }
        }
        return ~n;
    }
    
    @Override
    public boolean add(final E e) {
        int n;
        int n2;
        if (e == null) {
            n = this.indexOfNull();
            n2 = 0;
        }
        else {
            int n3;
            if (this.mIdentityHashCode) {
                n3 = System.identityHashCode(e);
            }
            else {
                n3 = e.hashCode();
            }
            n = this.indexOf(e, n3);
            n2 = n3;
        }
        if (n >= 0) {
            return false;
        }
        final int n4 = ~n;
        if (this.mSize >= this.mHashes.length) {
            final int mSize = this.mSize;
            int n5 = 4;
            if (mSize >= 8) {
                n5 = (this.mSize >> 1) + this.mSize;
            }
            else if (this.mSize >= 4) {
                n5 = 8;
            }
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            this.allocArrays(n5);
            if (this.mHashes.length > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, mHashes.length);
                System.arraycopy(mArray, 0, this.mArray, 0, mArray.length);
            }
            freeArrays(mHashes, mArray, this.mSize);
        }
        if (n4 < this.mSize) {
            final int[] mHashes2 = this.mHashes;
            final int[] mHashes3 = this.mHashes;
            final int n6 = n4 + 1;
            System.arraycopy(mHashes2, n4, mHashes3, n6, this.mSize - n4);
            System.arraycopy(this.mArray, n4, this.mArray, n6, this.mSize - n4);
        }
        this.mHashes[n4] = n2;
        this.mArray[n4] = e;
        ++this.mSize;
        return true;
    }
    
    public void addAll(final ArraySet<? extends E> set) {
        final int mSize = set.mSize;
        this.ensureCapacity(this.mSize + mSize);
        final int mSize2 = this.mSize;
        int i = 0;
        if (mSize2 == 0) {
            if (mSize > 0) {
                System.arraycopy(set.mHashes, 0, this.mHashes, 0, mSize);
                System.arraycopy(set.mArray, 0, this.mArray, 0, mSize);
                this.mSize = mSize;
            }
        }
        else {
            while (i < mSize) {
                this.add(set.valueAt(i));
                ++i;
            }
        }
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        this.ensureCapacity(this.mSize + collection.size());
        final Iterator<? extends E> iterator = collection.iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            b |= this.add(iterator.next());
        }
        return b;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void append(final E e) {
        final int mSize = this.mSize;
        int n;
        if (e == null) {
            n = 0;
        }
        else if (this.mIdentityHashCode) {
            n = System.identityHashCode(e);
        }
        else {
            n = e.hashCode();
        }
        if (mSize >= this.mHashes.length) {
            throw new IllegalStateException("Array is full");
        }
        if (mSize > 0 && this.mHashes[mSize - 1] > n) {
            this.add(e);
            return;
        }
        this.mSize = mSize + 1;
        this.mHashes[mSize] = n;
        this.mArray[mSize] = e;
    }
    
    @Override
    public void clear() {
        if (this.mSize != 0) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ArraySet.INT;
            this.mArray = ArraySet.OBJECT;
            this.mSize = 0;
        }
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.indexOf(o) >= 0;
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!this.contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    public void ensureCapacity(final int n) {
        if (this.mHashes.length < n) {
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            this.allocArrays(n);
            if (this.mSize > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, this.mSize);
                System.arraycopy(mArray, 0, this.mArray, 0, this.mSize);
            }
            freeArrays(mHashes, mArray, this.mSize);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Set) {
            final Set set = (Set)o;
            if (this.size() != set.size()) {
                return false;
            }
            int i = 0;
            try {
                while (i < this.mSize) {
                    if (!set.contains(this.valueAt(i))) {
                        return false;
                    }
                    ++i;
                }
                return true;
            }
            catch (ClassCastException ex) {
                return false;
            }
            catch (NullPointerException ex2) {
                return false;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int[] mHashes = this.mHashes;
        final int mSize = this.mSize;
        int i = 0;
        int n = 0;
        while (i < mSize) {
            n += mHashes[i];
            ++i;
        }
        return n;
    }
    
    public int indexOf(final Object o) {
        int n;
        if (o == null) {
            n = this.indexOfNull();
        }
        else {
            int n2;
            if (this.mIdentityHashCode) {
                n2 = System.identityHashCode(o);
            }
            else {
                n2 = o.hashCode();
            }
            n = this.indexOf(o, n2);
        }
        return n;
    }
    
    @Override
    public boolean isEmpty() {
        return this.mSize <= 0;
    }
    
    @Override
    public Iterator<E> iterator() {
        return this.getCollection().getKeySet().iterator();
    }
    
    @Override
    public boolean remove(final Object o) {
        final int index = this.indexOf(o);
        if (index >= 0) {
            this.removeAt(index);
            return true;
        }
        return false;
    }
    
    public boolean removeAll(final ArraySet<? extends E> set) {
        final int mSize = set.mSize;
        final int mSize2 = this.mSize;
        boolean b = false;
        for (int i = 0; i < mSize; ++i) {
            this.remove(set.valueAt(i));
        }
        if (mSize2 != this.mSize) {
            b = true;
        }
        return b;
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        final Iterator<?> iterator = collection.iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            b |= this.remove(iterator.next());
        }
        return b;
    }
    
    public E removeAt(final int n) {
        final Object o = this.mArray[n];
        if (this.mSize <= 1) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ArraySet.INT;
            this.mArray = ArraySet.OBJECT;
            this.mSize = 0;
        }
        else {
            final int[] mHashes = this.mHashes;
            int n2 = 8;
            if (mHashes.length > 8 && this.mSize < this.mHashes.length / 3) {
                if (this.mSize > 8) {
                    n2 = (this.mSize >> 1) + this.mSize;
                }
                final int[] mHashes2 = this.mHashes;
                final Object[] mArray = this.mArray;
                this.allocArrays(n2);
                --this.mSize;
                if (n > 0) {
                    System.arraycopy(mHashes2, 0, this.mHashes, 0, n);
                    System.arraycopy(mArray, 0, this.mArray, 0, n);
                }
                if (n < this.mSize) {
                    final int n3 = n + 1;
                    System.arraycopy(mHashes2, n3, this.mHashes, n, this.mSize - n);
                    System.arraycopy(mArray, n3, this.mArray, n, this.mSize - n);
                }
            }
            else {
                --this.mSize;
                if (n < this.mSize) {
                    final int[] mHashes3 = this.mHashes;
                    final int n4 = n + 1;
                    System.arraycopy(mHashes3, n4, this.mHashes, n, this.mSize - n);
                    System.arraycopy(this.mArray, n4, this.mArray, n, this.mSize - n);
                }
                this.mArray[this.mSize] = null;
            }
        }
        return (E)o;
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        int i = this.mSize - 1;
        boolean b = false;
        while (i >= 0) {
            if (!collection.contains(this.mArray[i])) {
                this.removeAt(i);
                b = true;
            }
            --i;
        }
        return b;
    }
    
    @Override
    public int size() {
        return this.mSize;
    }
    
    @Override
    public Object[] toArray() {
        final Object[] array = new Object[this.mSize];
        System.arraycopy(this.mArray, 0, array, 0, this.mSize);
        return array;
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        Object[] array2 = array;
        if (array.length < this.mSize) {
            array2 = (Object[])Array.newInstance(array.getClass().getComponentType(), this.mSize);
        }
        System.arraycopy(this.mArray, 0, array2, 0, this.mSize);
        if (array2.length > this.mSize) {
            array2[this.mSize] = null;
        }
        return (T[])array2;
    }
    
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(this.mSize * 14);
        sb.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            final E value = this.valueAt(i);
            if (value != this) {
                sb.append(value);
            }
            else {
                sb.append("(this Set)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    public E valueAt(final int n) {
        return (E)this.mArray[n];
    }
}
