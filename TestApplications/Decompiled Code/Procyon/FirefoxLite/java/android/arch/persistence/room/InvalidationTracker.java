// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room;

import java.lang.ref.WeakReference;
import java.util.Collections;
import android.support.v4.util.ArraySet;
import java.util.Set;
import android.arch.core.executor.ArchTaskExecutor;
import java.util.Arrays;
import java.util.Locale;
import java.util.Iterator;
import android.arch.persistence.db.SupportSQLiteDatabase;
import java.util.concurrent.locks.Lock;
import java.util.Map;
import android.util.Log;
import android.database.sqlite.SQLiteException;
import android.database.Cursor;
import android.support.v4.util.ArrayMap;
import java.util.concurrent.atomic.AtomicBoolean;
import android.arch.core.internal.SafeIterableMap;
import android.arch.persistence.db.SupportSQLiteStatement;

public class InvalidationTracker
{
    private static final String[] TRIGGERS;
    private volatile SupportSQLiteStatement mCleanupStatement;
    private final RoomDatabase mDatabase;
    private volatile boolean mInitialized;
    private long mMaxVersion;
    private ObservedTableTracker mObservedTableTracker;
    final SafeIterableMap<Observer, ObserverWrapper> mObserverMap;
    AtomicBoolean mPendingRefresh;
    private Object[] mQueryArgs;
    Runnable mRefreshRunnable;
    ArrayMap<String, Integer> mTableIdLookup;
    private String[] mTableNames;
    long[] mTableVersions;
    
    static {
        TRIGGERS = new String[] { "UPDATE", "DELETE", "INSERT" };
    }
    
    public InvalidationTracker(final RoomDatabase mDatabase, final String... array) {
        this.mQueryArgs = new Object[1];
        this.mMaxVersion = 0L;
        int i = 0;
        this.mPendingRefresh = new AtomicBoolean(false);
        this.mInitialized = false;
        this.mObserverMap = new SafeIterableMap<Observer, ObserverWrapper>();
        this.mRefreshRunnable = new Runnable() {
            private boolean checkUpdatedTable() {
                final Cursor query = InvalidationTracker.this.mDatabase.query("SELECT * FROM room_table_modification_log WHERE version  > ? ORDER BY version ASC;", InvalidationTracker.this.mQueryArgs);
                boolean b = false;
                try {
                    while (query.moveToNext()) {
                        final long long1 = query.getLong(0);
                        InvalidationTracker.this.mTableVersions[query.getInt(1)] = long1;
                        InvalidationTracker.this.mMaxVersion = long1;
                        b = true;
                    }
                    return b;
                }
                finally {
                    query.close();
                }
            }
            
            @Override
            public void run() {
                final Lock closeLock = InvalidationTracker.this.mDatabase.getCloseLock();
                final int n = 0;
                int n2 = 0;
                Label_0231: {
                    Object o;
                    try {
                        try {
                            closeLock.lock();
                            n2 = (InvalidationTracker.this.ensureInitialization() ? 1 : 0);
                            if (n2 == 0) {
                                closeLock.unlock();
                                return;
                            }
                            n2 = (InvalidationTracker.this.mPendingRefresh.compareAndSet(true, false) ? 1 : 0);
                            if (n2 == 0) {
                                closeLock.unlock();
                                return;
                            }
                            n2 = (InvalidationTracker.this.mDatabase.inTransaction() ? 1 : 0);
                            if (n2 != 0) {
                                closeLock.unlock();
                                return;
                            }
                            InvalidationTracker.this.mCleanupStatement.executeUpdateDelete();
                            InvalidationTracker.this.mQueryArgs[0] = InvalidationTracker.this.mMaxVersion;
                            if (InvalidationTracker.this.mDatabase.mWriteAheadLoggingEnabled) {
                                final SupportSQLiteDatabase writableDatabase = InvalidationTracker.this.mDatabase.getOpenHelper().getWritableDatabase();
                                try {
                                    writableDatabase.beginTransaction();
                                    n2 = (this.checkUpdatedTable() ? 1 : 0);
                                    try {
                                        writableDatabase.setTransactionSuccessful();
                                        try {
                                            writableDatabase.endTransaction();
                                        }
                                        catch (IllegalStateException | SQLiteException ex) {}
                                    }
                                    finally {}
                                }
                                finally {
                                    n2 = n;
                                }
                                try {
                                    writableDatabase.endTransaction();
                                    throw;
                                }
                                catch (IllegalStateException | SQLiteException ex2) {}
                            }
                            n2 = (this.checkUpdatedTable() ? 1 : 0);
                            break Label_0231;
                        }
                        finally {}
                    }
                    catch (IllegalStateException | SQLiteException ex3) {
                        final Object o2;
                        o = o2;
                        n2 = 0;
                    }
                    Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", (Throwable)o);
                }
                closeLock.unlock();
                if (n2 != 0) {
                    synchronized (InvalidationTracker.this.mObserverMap) {
                        final Iterator<Map.Entry<Observer, ObserverWrapper>> iterator = InvalidationTracker.this.mObserverMap.iterator();
                        while (iterator.hasNext()) {
                            ((Map.Entry<Observer, ObserverWrapper>)iterator.next()).getValue().checkForInvalidation(InvalidationTracker.this.mTableVersions);
                        }
                    }
                }
                return;
                closeLock.unlock();
            }
        };
        this.mDatabase = mDatabase;
        this.mObservedTableTracker = new ObservedTableTracker(array.length);
        this.mTableIdLookup = new ArrayMap<String, Integer>();
        final int length = array.length;
        this.mTableNames = new String[length];
        while (i < length) {
            final String lowerCase = array[i].toLowerCase(Locale.US);
            this.mTableIdLookup.put(lowerCase, i);
            this.mTableNames[i] = lowerCase;
            ++i;
        }
        Arrays.fill(this.mTableVersions = new long[array.length], 0L);
    }
    
    private static void appendTriggerName(final StringBuilder sb, final String str, final String str2) {
        sb.append("`");
        sb.append("room_table_modification_trigger_");
        sb.append(str);
        sb.append("_");
        sb.append(str2);
        sb.append("`");
    }
    
    private boolean ensureInitialization() {
        if (!this.mDatabase.isOpen()) {
            return false;
        }
        if (!this.mInitialized) {
            this.mDatabase.getOpenHelper().getWritableDatabase();
        }
        if (!this.mInitialized) {
            Log.e("ROOM", "database is not initialized even though it is open");
            return false;
        }
        return true;
    }
    
    private void startTrackingTable(final SupportSQLiteDatabase supportSQLiteDatabase, final int i) {
        final String str = this.mTableNames[i];
        final StringBuilder sb = new StringBuilder();
        for (final String str2 : InvalidationTracker.TRIGGERS) {
            sb.setLength(0);
            sb.append("CREATE TEMP TRIGGER IF NOT EXISTS ");
            appendTriggerName(sb, str, str2);
            sb.append(" AFTER ");
            sb.append(str2);
            sb.append(" ON `");
            sb.append(str);
            sb.append("` BEGIN INSERT OR REPLACE INTO ");
            sb.append("room_table_modification_log");
            sb.append(" VALUES(null, ");
            sb.append(i);
            sb.append("); END");
            supportSQLiteDatabase.execSQL(sb.toString());
        }
    }
    
    private void stopTrackingTable(final SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        final String s = this.mTableNames[i];
        final StringBuilder sb = new StringBuilder();
        final String[] triggers = InvalidationTracker.TRIGGERS;
        int length;
        String s2;
        for (length = triggers.length, i = 0; i < length; ++i) {
            s2 = triggers[i];
            sb.setLength(0);
            sb.append("DROP TRIGGER IF EXISTS ");
            appendTriggerName(sb, s, s2);
            supportSQLiteDatabase.execSQL(sb.toString());
        }
    }
    
    public void addObserver(final Observer observer) {
        final String[] mTables = observer.mTables;
        final int[] array = new int[mTables.length];
        final int length = mTables.length;
        final long[] array2 = new long[mTables.length];
        for (int i = 0; i < length; ++i) {
            final Integer n = this.mTableIdLookup.get(mTables[i].toLowerCase(Locale.US));
            if (n == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("There is no table with name ");
                sb.append(mTables[i]);
                throw new IllegalArgumentException(sb.toString());
            }
            array[i] = n;
            array2[i] = this.mMaxVersion;
        }
        final ObserverWrapper observerWrapper = new ObserverWrapper(observer, array, mTables, array2);
        synchronized (this.mObserverMap) {
            final ObserverWrapper observerWrapper2 = this.mObserverMap.putIfAbsent(observer, observerWrapper);
            // monitorexit(this.mObserverMap)
            if (observerWrapper2 == null && this.mObservedTableTracker.onAdded(array)) {
                this.syncTriggers();
            }
        }
    }
    
    public void addWeakObserver(final Observer observer) {
        this.addObserver((Observer)new WeakObserver(this, observer));
    }
    
    void internalInit(final SupportSQLiteDatabase supportSQLiteDatabase) {
        synchronized (this) {
            if (this.mInitialized) {
                Log.e("ROOM", "Invalidation tracker is initialized twice :/.");
                return;
            }
            supportSQLiteDatabase.beginTransaction();
            try {
                supportSQLiteDatabase.execSQL("PRAGMA temp_store = MEMORY;");
                supportSQLiteDatabase.execSQL("PRAGMA recursive_triggers='ON';");
                supportSQLiteDatabase.execSQL("CREATE TEMP TABLE room_table_modification_log(version INTEGER PRIMARY KEY AUTOINCREMENT, table_id INTEGER)");
                supportSQLiteDatabase.setTransactionSuccessful();
                supportSQLiteDatabase.endTransaction();
                this.syncTriggers(supportSQLiteDatabase);
                this.mCleanupStatement = supportSQLiteDatabase.compileStatement("DELETE FROM room_table_modification_log WHERE version NOT IN( SELECT MAX(version) FROM room_table_modification_log GROUP BY table_id)");
                this.mInitialized = true;
            }
            finally {
                supportSQLiteDatabase.endTransaction();
            }
        }
    }
    
    public void refreshVersionsAsync() {
        if (this.mPendingRefresh.compareAndSet(false, true)) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(this.mRefreshRunnable);
        }
    }
    
    public void removeObserver(final Observer observer) {
        synchronized (this.mObserverMap) {
            final ObserverWrapper observerWrapper = this.mObserverMap.remove(observer);
            // monitorexit(this.mObserverMap)
            if (observerWrapper != null && this.mObservedTableTracker.onRemoved(observerWrapper.mTableIds)) {
                this.syncTriggers();
            }
        }
    }
    
    void syncTriggers() {
        if (!this.mDatabase.isOpen()) {
            return;
        }
        this.syncTriggers(this.mDatabase.getOpenHelper().getWritableDatabase());
    }
    
    void syncTriggers(final SupportSQLiteDatabase supportSQLiteDatabase) {
        if (supportSQLiteDatabase.inTransaction()) {
            return;
        }
        Label_0010: {
            break Label_0010;
            try {
                while (true) {
                    final Lock closeLock = this.mDatabase.getCloseLock();
                    closeLock.lock();
                    try {
                        final int[] tablesToSync = this.mObservedTableTracker.getTablesToSync();
                        if (tablesToSync == null) {
                            return;
                        }
                        final int length = tablesToSync.length;
                        try {
                            supportSQLiteDatabase.beginTransaction();
                            for (int i = 0; i < length; ++i) {
                                switch (tablesToSync[i]) {
                                    case 2: {
                                        this.stopTrackingTable(supportSQLiteDatabase, i);
                                        break;
                                    }
                                    case 1: {
                                        this.startTrackingTable(supportSQLiteDatabase, i);
                                        break;
                                    }
                                }
                            }
                            supportSQLiteDatabase.setTransactionSuccessful();
                            supportSQLiteDatabase.endTransaction();
                            this.mObservedTableTracker.onSyncCompleted();
                        }
                        finally {
                            supportSQLiteDatabase.endTransaction();
                        }
                    }
                    finally {
                        closeLock.unlock();
                    }
                }
            }
            catch (IllegalStateException | SQLiteException ex) {
                final Throwable t;
                Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", t);
            }
        }
    }
    
    static class ObservedTableTracker
    {
        boolean mNeedsSync;
        boolean mPendingSync;
        final long[] mTableObservers;
        final int[] mTriggerStateChanges;
        final boolean[] mTriggerStates;
        
        ObservedTableTracker(final int n) {
            this.mTableObservers = new long[n];
            this.mTriggerStates = new boolean[n];
            this.mTriggerStateChanges = new int[n];
            Arrays.fill(this.mTableObservers, 0L);
            Arrays.fill(this.mTriggerStates, false);
        }
        
        int[] getTablesToSync() {
            synchronized (this) {
                if (this.mNeedsSync && !this.mPendingSync) {
                    final int length = this.mTableObservers.length;
                    int n = 0;
                    while (true) {
                        int n2 = 1;
                        if (n >= length) {
                            break;
                        }
                        final boolean b = this.mTableObservers[n] > 0L;
                        if (b != this.mTriggerStates[n]) {
                            final int[] mTriggerStateChanges = this.mTriggerStateChanges;
                            if (!b) {
                                n2 = 2;
                            }
                            mTriggerStateChanges[n] = n2;
                        }
                        else {
                            this.mTriggerStateChanges[n] = 0;
                        }
                        this.mTriggerStates[n] = b;
                        ++n;
                    }
                    this.mPendingSync = true;
                    this.mNeedsSync = false;
                    return this.mTriggerStateChanges;
                }
                return null;
            }
        }
        
        boolean onAdded(final int... array) {
            synchronized (this) {
                final int length = array.length;
                int i = 0;
                boolean b = false;
                while (i < length) {
                    final int n = array[i];
                    final long n2 = this.mTableObservers[n];
                    this.mTableObservers[n] = 1L + n2;
                    if (n2 == 0L) {
                        this.mNeedsSync = true;
                        b = true;
                    }
                    ++i;
                }
                return b;
            }
        }
        
        boolean onRemoved(final int... array) {
            synchronized (this) {
                final int length = array.length;
                int i = 0;
                boolean b = false;
                while (i < length) {
                    final int n = array[i];
                    final long n2 = this.mTableObservers[n];
                    this.mTableObservers[n] = n2 - 1L;
                    if (n2 == 1L) {
                        this.mNeedsSync = true;
                        b = true;
                    }
                    ++i;
                }
                return b;
            }
        }
        
        void onSyncCompleted() {
            synchronized (this) {
                this.mPendingSync = false;
            }
        }
    }
    
    public abstract static class Observer
    {
        final String[] mTables;
        
        protected Observer(final String s, final String... original) {
            (this.mTables = Arrays.copyOf(original, original.length + 1))[original.length] = s;
        }
        
        public Observer(final String[] original) {
            this.mTables = Arrays.copyOf(original, original.length);
        }
        
        public abstract void onInvalidated(final Set<String> p0);
    }
    
    static class ObserverWrapper
    {
        final Observer mObserver;
        private final Set<String> mSingleTableSet;
        final int[] mTableIds;
        private final String[] mTableNames;
        private final long[] mVersions;
        
        ObserverWrapper(final Observer mObserver, final int[] mTableIds, final String[] mTableNames, final long[] mVersions) {
            this.mObserver = mObserver;
            this.mTableIds = mTableIds;
            this.mTableNames = mTableNames;
            this.mVersions = mVersions;
            if (mTableIds.length == 1) {
                final ArraySet<String> s = new ArraySet<String>();
                s.add(this.mTableNames[0]);
                this.mSingleTableSet = (Set<String>)Collections.unmodifiableSet((Set<?>)s);
            }
            else {
                this.mSingleTableSet = null;
            }
        }
        
        void checkForInvalidation(final long[] array) {
            final int length = this.mTableIds.length;
            Set<String> set = null;
            Set<String> mSingleTableSet;
            for (int i = 0; i < length; ++i, set = mSingleTableSet) {
                final long n = array[this.mTableIds[i]];
                mSingleTableSet = set;
                if (this.mVersions[i] < n) {
                    this.mVersions[i] = n;
                    if (length == 1) {
                        mSingleTableSet = this.mSingleTableSet;
                    }
                    else {
                        if ((mSingleTableSet = set) == null) {
                            mSingleTableSet = new ArraySet<String>(length);
                        }
                        mSingleTableSet.add(this.mTableNames[i]);
                    }
                }
            }
            if (set != null) {
                this.mObserver.onInvalidated(set);
            }
        }
    }
    
    static class WeakObserver extends Observer
    {
        final WeakReference<Observer> mDelegateRef;
        final InvalidationTracker mTracker;
        
        WeakObserver(final InvalidationTracker mTracker, final Observer referent) {
            super(referent.mTables);
            this.mTracker = mTracker;
            this.mDelegateRef = new WeakReference<Observer>(referent);
        }
        
        @Override
        public void onInvalidated(final Set<String> set) {
            final Observer observer = this.mDelegateRef.get();
            if (observer == null) {
                this.mTracker.removeObserver((Observer)this);
            }
            else {
                observer.onInvalidated(set);
            }
        }
    }
}
