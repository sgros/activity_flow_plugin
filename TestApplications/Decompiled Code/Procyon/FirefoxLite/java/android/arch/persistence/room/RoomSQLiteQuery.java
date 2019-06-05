// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteProgram;

public class RoomSQLiteQuery implements SupportSQLiteProgram, SupportSQLiteQuery
{
    static final TreeMap<Integer, RoomSQLiteQuery> sQueryPool;
    int mArgCount;
    private final int[] mBindingTypes;
    final byte[][] mBlobBindings;
    final int mCapacity;
    final double[] mDoubleBindings;
    final long[] mLongBindings;
    private volatile String mQuery;
    final String[] mStringBindings;
    
    static {
        sQueryPool = new TreeMap<Integer, RoomSQLiteQuery>();
    }
    
    private RoomSQLiteQuery(int mCapacity) {
        this.mCapacity = mCapacity;
        ++mCapacity;
        this.mBindingTypes = new int[mCapacity];
        this.mLongBindings = new long[mCapacity];
        this.mDoubleBindings = new double[mCapacity];
        this.mStringBindings = new String[mCapacity];
        this.mBlobBindings = new byte[mCapacity][];
    }
    
    public static RoomSQLiteQuery acquire(final String s, final int i) {
        Object sQueryPool = RoomSQLiteQuery.sQueryPool;
        synchronized (sQueryPool) {
            final Map.Entry<Integer, RoomSQLiteQuery> ceilingEntry = RoomSQLiteQuery.sQueryPool.ceilingEntry(i);
            if (ceilingEntry != null) {
                RoomSQLiteQuery.sQueryPool.remove(ceilingEntry.getKey());
                final RoomSQLiteQuery roomSQLiteQuery = ceilingEntry.getValue();
                roomSQLiteQuery.init(s, i);
                return roomSQLiteQuery;
            }
            // monitorexit(sQueryPool)
            sQueryPool = new RoomSQLiteQuery(i);
            ((RoomSQLiteQuery)sQueryPool).init(s, i);
            return (RoomSQLiteQuery)sQueryPool;
        }
    }
    
    private static void prunePoolLocked() {
        if (RoomSQLiteQuery.sQueryPool.size() > 15) {
            int i = RoomSQLiteQuery.sQueryPool.size() - 10;
            final Iterator<Integer> iterator = RoomSQLiteQuery.sQueryPool.descendingKeySet().iterator();
            while (i > 0) {
                iterator.next();
                iterator.remove();
                --i;
            }
        }
    }
    
    @Override
    public void bindBlob(final int n, final byte[] array) {
        this.mBindingTypes[n] = 5;
        this.mBlobBindings[n] = array;
    }
    
    @Override
    public void bindDouble(final int n, final double n2) {
        this.mBindingTypes[n] = 3;
        this.mDoubleBindings[n] = n2;
    }
    
    @Override
    public void bindLong(final int n, final long n2) {
        this.mBindingTypes[n] = 2;
        this.mLongBindings[n] = n2;
    }
    
    @Override
    public void bindNull(final int n) {
        this.mBindingTypes[n] = 1;
    }
    
    @Override
    public void bindString(final int n, final String s) {
        this.mBindingTypes[n] = 4;
        this.mStringBindings[n] = s;
    }
    
    @Override
    public void bindTo(final SupportSQLiteProgram supportSQLiteProgram) {
        for (int i = 1; i <= this.mArgCount; ++i) {
            switch (this.mBindingTypes[i]) {
                case 5: {
                    supportSQLiteProgram.bindBlob(i, this.mBlobBindings[i]);
                    break;
                }
                case 4: {
                    supportSQLiteProgram.bindString(i, this.mStringBindings[i]);
                    break;
                }
                case 3: {
                    supportSQLiteProgram.bindDouble(i, this.mDoubleBindings[i]);
                    break;
                }
                case 2: {
                    supportSQLiteProgram.bindLong(i, this.mLongBindings[i]);
                    break;
                }
                case 1: {
                    supportSQLiteProgram.bindNull(i);
                    break;
                }
            }
        }
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String getSql() {
        return this.mQuery;
    }
    
    void init(final String mQuery, final int mArgCount) {
        this.mQuery = mQuery;
        this.mArgCount = mArgCount;
    }
    
    public void release() {
        synchronized (RoomSQLiteQuery.sQueryPool) {
            RoomSQLiteQuery.sQueryPool.put(this.mCapacity, this);
            prunePoolLocked();
        }
    }
}
