// 
// Decompiled by Procyon v0.5.34
// 

package androidx.collection;

public class LongSparseArray<E> implements Cloneable
{
    private static final Object DELETED;
    private boolean mGarbage;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;
    
    static {
        DELETED = new Object();
    }
    
    public LongSparseArray() {
        this(10);
    }
    
    public LongSparseArray(int idealLongArraySize) {
        this.mGarbage = false;
        if (idealLongArraySize == 0) {
            this.mKeys = ContainerHelpers.EMPTY_LONGS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        }
        else {
            idealLongArraySize = ContainerHelpers.idealLongArraySize(idealLongArraySize);
            this.mKeys = new long[idealLongArraySize];
            this.mValues = new Object[idealLongArraySize];
        }
        this.mSize = 0;
    }
    
    private void gc() {
        final int mSize = this.mSize;
        final long[] mKeys = this.mKeys;
        final Object[] mValues = this.mValues;
        int i = 0;
        int mSize2 = 0;
        while (i < mSize) {
            final Object o = mValues[i];
            int n = mSize2;
            if (o != LongSparseArray.DELETED) {
                if (i != mSize2) {
                    mKeys[mSize2] = mKeys[i];
                    mValues[mSize2] = o;
                    mValues[i] = null;
                }
                n = mSize2 + 1;
            }
            ++i;
            mSize2 = n;
        }
        this.mGarbage = false;
        this.mSize = mSize2;
    }
    
    public void append(final long n, final E e) {
        final int mSize = this.mSize;
        if (mSize != 0 && n <= this.mKeys[mSize - 1]) {
            this.put(n, e);
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
        }
        final int mSize2 = this.mSize;
        if (mSize2 >= this.mKeys.length) {
            final int idealLongArraySize = ContainerHelpers.idealLongArraySize(mSize2 + 1);
            final long[] mKeys = new long[idealLongArraySize];
            final Object[] mValues = new Object[idealLongArraySize];
            final long[] mKeys2 = this.mKeys;
            System.arraycopy(mKeys2, 0, mKeys, 0, mKeys2.length);
            final Object[] mValues2 = this.mValues;
            System.arraycopy(mValues2, 0, mValues, 0, mValues2.length);
            this.mKeys = mKeys;
            this.mValues = mValues;
        }
        this.mKeys[mSize2] = n;
        this.mValues[mSize2] = e;
        this.mSize = mSize2 + 1;
    }
    
    public void clear() {
        final int mSize = this.mSize;
        final Object[] mValues = this.mValues;
        for (int i = 0; i < mSize; ++i) {
            mValues[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }
    
    public LongSparseArray<E> clone() {
        try {
            final LongSparseArray longSparseArray = (LongSparseArray)super.clone();
            longSparseArray.mKeys = this.mKeys.clone();
            longSparseArray.mValues = this.mValues.clone();
            return longSparseArray;
        }
        catch (CloneNotSupportedException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    public boolean containsKey(final long n) {
        return this.indexOfKey(n) >= 0;
    }
    
    public void delete(final long n) {
        final int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (binarySearch >= 0) {
            final Object[] mValues = this.mValues;
            final Object o = mValues[binarySearch];
            final Object deleted = LongSparseArray.DELETED;
            if (o != deleted) {
                mValues[binarySearch] = deleted;
                this.mGarbage = true;
            }
        }
    }
    
    public E get(final long n) {
        return this.get(n, null);
    }
    
    public E get(final long n, final E e) {
        final int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (binarySearch >= 0) {
            final Object[] mValues = this.mValues;
            if (mValues[binarySearch] != LongSparseArray.DELETED) {
                return (E)mValues[binarySearch];
            }
        }
        return e;
    }
    
    public int indexOfKey(final long n) {
        if (this.mGarbage) {
            this.gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
    }
    
    public long keyAt(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[n];
    }
    
    public void put(final long n, final E e) {
        final int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (binarySearch >= 0) {
            this.mValues[binarySearch] = e;
        }
        else {
            final int n2 = ~binarySearch;
            if (n2 < this.mSize) {
                final Object[] mValues = this.mValues;
                if (mValues[n2] == LongSparseArray.DELETED) {
                    this.mKeys[n2] = n;
                    mValues[n2] = e;
                    return;
                }
            }
            int n3 = n2;
            if (this.mGarbage) {
                n3 = n2;
                if (this.mSize >= this.mKeys.length) {
                    this.gc();
                    n3 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
                }
            }
            final int mSize = this.mSize;
            if (mSize >= this.mKeys.length) {
                final int idealLongArraySize = ContainerHelpers.idealLongArraySize(mSize + 1);
                final long[] mKeys = new long[idealLongArraySize];
                final Object[] mValues2 = new Object[idealLongArraySize];
                final long[] mKeys2 = this.mKeys;
                System.arraycopy(mKeys2, 0, mKeys, 0, mKeys2.length);
                final Object[] mValues3 = this.mValues;
                System.arraycopy(mValues3, 0, mValues2, 0, mValues3.length);
                this.mKeys = mKeys;
                this.mValues = mValues2;
            }
            final int mSize2 = this.mSize;
            if (mSize2 - n3 != 0) {
                final long[] mKeys3 = this.mKeys;
                final int n4 = n3 + 1;
                System.arraycopy(mKeys3, n3, mKeys3, n4, mSize2 - n3);
                final Object[] mValues4 = this.mValues;
                System.arraycopy(mValues4, n3, mValues4, n4, this.mSize - n3);
            }
            this.mKeys[n3] = n;
            this.mValues[n3] = e;
            ++this.mSize;
        }
    }
    
    public void removeAt(final int n) {
        final Object[] mValues = this.mValues;
        final Object o = mValues[n];
        final Object deleted = LongSparseArray.DELETED;
        if (o != deleted) {
            mValues[n] = deleted;
            this.mGarbage = true;
        }
    }
    
    public int size() {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mSize;
    }
    
    @Override
    public String toString() {
        if (this.size() <= 0) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.keyAt(i));
            sb.append('=');
            final E value = this.valueAt(i);
            if (value != this) {
                sb.append(value);
            }
            else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    public E valueAt(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return (E)this.mValues[n];
    }
}
