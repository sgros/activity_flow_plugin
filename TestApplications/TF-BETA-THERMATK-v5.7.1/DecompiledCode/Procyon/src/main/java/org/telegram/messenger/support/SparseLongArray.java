// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.support;

public class SparseLongArray implements Cloneable
{
    private int[] mKeys;
    private int mSize;
    private long[] mValues;
    
    public SparseLongArray() {
        this(10);
    }
    
    public SparseLongArray(int idealLongArraySize) {
        idealLongArraySize = ArrayUtils.idealLongArraySize(idealLongArraySize);
        this.mKeys = new int[idealLongArraySize];
        this.mValues = new long[idealLongArraySize];
        this.mSize = 0;
    }
    
    private static int binarySearch(final int[] array, int n, int n2, final long n3) {
        final int n4 = n2 + n;
        n2 = n - 1;
        n = n4;
        while (n - n2 > 1) {
            final int n5 = (n + n2) / 2;
            if (array[n5] < n3) {
                n2 = n5;
            }
            else {
                n = n5;
            }
        }
        if (n == n4) {
            return ~n4;
        }
        if (array[n] == n3) {
            return n;
        }
        return ~n;
    }
    
    private void growKeyAndValueArrays(int idealLongArraySize) {
        idealLongArraySize = ArrayUtils.idealLongArraySize(idealLongArraySize);
        final int[] mKeys = new int[idealLongArraySize];
        final long[] mValues = new long[idealLongArraySize];
        final int[] mKeys2 = this.mKeys;
        System.arraycopy(mKeys2, 0, mKeys, 0, mKeys2.length);
        final long[] mValues2 = this.mValues;
        System.arraycopy(mValues2, 0, mValues, 0, mValues2.length);
        this.mKeys = mKeys;
        this.mValues = mValues;
    }
    
    public void append(final int n, final long n2) {
        final int mSize = this.mSize;
        if (mSize != 0 && n <= this.mKeys[mSize - 1]) {
            this.put(n, n2);
            return;
        }
        final int mSize2 = this.mSize;
        if (mSize2 >= this.mKeys.length) {
            this.growKeyAndValueArrays(mSize2 + 1);
        }
        this.mKeys[mSize2] = n;
        this.mValues[mSize2] = n2;
        this.mSize = mSize2 + 1;
    }
    
    public void clear() {
        this.mSize = 0;
    }
    
    public SparseLongArray clone() {
        SparseLongArray sparseLongArray;
        try {
            final SparseLongArray sparseLongArray2;
            sparseLongArray = (sparseLongArray2 = (SparseLongArray)super.clone());
            final SparseLongArray sparseLongArray3 = this;
            final int[] array = sparseLongArray3.mKeys;
            final int[] array2 = array.clone();
            final int[] array3 = array2;
            sparseLongArray2.mKeys = array3;
            final SparseLongArray sparseLongArray4 = sparseLongArray;
            final SparseLongArray sparseLongArray5 = this;
            final long[] array4 = sparseLongArray5.mValues;
            final long[] array5 = array4.clone();
            final long[] array6 = array5;
            sparseLongArray4.mValues = array6;
            return sparseLongArray;
        }
        catch (CloneNotSupportedException sparseLongArray) {
            sparseLongArray = null;
        }
        try {
            final SparseLongArray sparseLongArray2 = sparseLongArray;
            final SparseLongArray sparseLongArray3 = this;
            final int[] array = sparseLongArray3.mKeys;
            final int[] array2 = array.clone();
            final int[] array3 = array2;
            sparseLongArray2.mKeys = array3;
            final SparseLongArray sparseLongArray4 = sparseLongArray;
            final SparseLongArray sparseLongArray5 = this;
            final long[] array4 = sparseLongArray5.mValues;
            final long[] array5 = array4.clone();
            final long[] array6 = array5;
            sparseLongArray4.mValues = array6;
            return sparseLongArray;
        }
        catch (CloneNotSupportedException ex) {
            return sparseLongArray;
        }
    }
    
    public void delete(int binarySearch) {
        binarySearch = binarySearch(this.mKeys, 0, this.mSize, binarySearch);
        if (binarySearch >= 0) {
            this.removeAt(binarySearch);
        }
    }
    
    public long get(final int n) {
        return this.get(n, 0L);
    }
    
    public long get(int binarySearch, final long n) {
        binarySearch = binarySearch(this.mKeys, 0, this.mSize, binarySearch);
        if (binarySearch < 0) {
            return n;
        }
        return this.mValues[binarySearch];
    }
    
    public int indexOfKey(final int n) {
        return binarySearch(this.mKeys, 0, this.mSize, n);
    }
    
    public int indexOfValue(final long n) {
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mValues[i] == n) {
                return i;
            }
        }
        return -1;
    }
    
    public int keyAt(final int n) {
        return this.mKeys[n];
    }
    
    public void put(final int n, final long n2) {
        final int binarySearch = binarySearch(this.mKeys, 0, this.mSize, n);
        if (binarySearch >= 0) {
            this.mValues[binarySearch] = n2;
        }
        else {
            final int n3 = ~binarySearch;
            final int mSize = this.mSize;
            if (mSize >= this.mKeys.length) {
                this.growKeyAndValueArrays(mSize + 1);
            }
            final int mSize2 = this.mSize;
            if (mSize2 - n3 != 0) {
                final int[] mKeys = this.mKeys;
                final int n4 = n3 + 1;
                System.arraycopy(mKeys, n3, mKeys, n4, mSize2 - n3);
                final long[] mValues = this.mValues;
                System.arraycopy(mValues, n3, mValues, n4, this.mSize - n3);
            }
            this.mKeys[n3] = n;
            this.mValues[n3] = n2;
            ++this.mSize;
        }
    }
    
    public void removeAt(final int n) {
        final int[] mKeys = this.mKeys;
        final int n2 = n + 1;
        System.arraycopy(mKeys, n2, mKeys, n, this.mSize - n2);
        final long[] mValues = this.mValues;
        System.arraycopy(mValues, n2, mValues, n, this.mSize - n2);
        --this.mSize;
    }
    
    public int size() {
        return this.mSize;
    }
    
    public long valueAt(final int n) {
        return this.mValues[n];
    }
}
