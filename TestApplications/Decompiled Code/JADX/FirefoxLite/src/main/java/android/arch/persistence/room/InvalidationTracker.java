package android.arch.persistence.room;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;
import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.p001v4.util.ArrayMap;
import android.support.p001v4.util.ArraySet;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

public class InvalidationTracker {
    private static final String[] TRIGGERS = new String[]{"UPDATE", "DELETE", "INSERT"};
    private volatile SupportSQLiteStatement mCleanupStatement;
    private final RoomDatabase mDatabase;
    private volatile boolean mInitialized;
    private long mMaxVersion = 0;
    private ObservedTableTracker mObservedTableTracker;
    final SafeIterableMap<Observer, ObserverWrapper> mObserverMap;
    AtomicBoolean mPendingRefresh;
    private Object[] mQueryArgs = new Object[1];
    Runnable mRefreshRunnable;
    ArrayMap<String, Integer> mTableIdLookup;
    private String[] mTableNames;
    long[] mTableVersions;

    /* renamed from: android.arch.persistence.room.InvalidationTracker$1 */
    class C00151 implements Runnable {
        C00151() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:45:0x009f  */
        public void run() {
            /*
            r6 = this;
            r0 = android.arch.persistence.room.InvalidationTracker.this;
            r0 = r0.mDatabase;
            r0 = r0.getCloseLock();
            r1 = 0;
            r0.lock();	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.ensureInitialization();	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            if (r2 != 0) goto L_0x001a;
        L_0x0016:
            r0.unlock();
            return;
        L_0x001a:
            r2 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.mPendingRefresh;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r3 = 1;
            r2 = r2.compareAndSet(r3, r1);	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            if (r2 != 0) goto L_0x0029;
        L_0x0025:
            r0.unlock();
            return;
        L_0x0029:
            r2 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.mDatabase;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.inTransaction();	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            if (r2 == 0) goto L_0x0039;
        L_0x0035:
            r0.unlock();
            return;
        L_0x0039:
            r2 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.mCleanupStatement;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2.executeUpdateDelete();	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.mQueryArgs;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r3 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r3 = r3.mMaxVersion;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r3 = java.lang.Long.valueOf(r3);	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2[r1] = r3;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.mDatabase;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.mWriteAheadLoggingEnabled;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            if (r2 == 0) goto L_0x008a;
        L_0x005e:
            r2 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.mDatabase;	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.getOpenHelper();	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2 = r2.getWritableDatabase();	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            r2.beginTransaction();	 Catch:{ all -> 0x0082 }
            r3 = r6.checkUpdatedTable();	 Catch:{ all -> 0x0082 }
            r2.setTransactionSuccessful();	 Catch:{ all -> 0x007d }
            r2.endTransaction();	 Catch:{ SQLiteException | IllegalStateException -> 0x007a, SQLiteException | IllegalStateException -> 0x007a }
            goto L_0x009a;
        L_0x007a:
            r1 = move-exception;
            r2 = r1;
            goto L_0x0093;
        L_0x007d:
            r1 = move-exception;
            r5 = r3;
            r3 = r1;
            r1 = r5;
            goto L_0x0083;
        L_0x0082:
            r3 = move-exception;
        L_0x0083:
            r2.endTransaction();	 Catch:{ SQLiteException | IllegalStateException -> 0x0087, SQLiteException | IllegalStateException -> 0x0087 }
            throw r3;	 Catch:{ SQLiteException | IllegalStateException -> 0x0087, SQLiteException | IllegalStateException -> 0x0087 }
        L_0x0087:
            r2 = move-exception;
            r3 = r1;
            goto L_0x0093;
        L_0x008a:
            r3 = r6.checkUpdatedTable();	 Catch:{ SQLiteException | IllegalStateException -> 0x0091, SQLiteException | IllegalStateException -> 0x0091 }
            goto L_0x009a;
        L_0x008f:
            r1 = move-exception;
            goto L_0x00cc;
        L_0x0091:
            r2 = move-exception;
            r3 = 0;
        L_0x0093:
            r1 = "ROOM";
            r4 = "Cannot run invalidation tracker. Is the db closed?";
            android.util.Log.e(r1, r4, r2);	 Catch:{ all -> 0x008f }
        L_0x009a:
            r0.unlock();
            if (r3 == 0) goto L_0x00cb;
        L_0x009f:
            r0 = android.arch.persistence.room.InvalidationTracker.this;
            r0 = r0.mObserverMap;
            monitor-enter(r0);
            r1 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ all -> 0x00c8 }
            r1 = r1.mObserverMap;	 Catch:{ all -> 0x00c8 }
            r1 = r1.iterator();	 Catch:{ all -> 0x00c8 }
        L_0x00ac:
            r2 = r1.hasNext();	 Catch:{ all -> 0x00c8 }
            if (r2 == 0) goto L_0x00c6;
        L_0x00b2:
            r2 = r1.next();	 Catch:{ all -> 0x00c8 }
            r2 = (java.util.Map.Entry) r2;	 Catch:{ all -> 0x00c8 }
            r2 = r2.getValue();	 Catch:{ all -> 0x00c8 }
            r2 = (android.arch.persistence.room.InvalidationTracker.ObserverWrapper) r2;	 Catch:{ all -> 0x00c8 }
            r3 = android.arch.persistence.room.InvalidationTracker.this;	 Catch:{ all -> 0x00c8 }
            r3 = r3.mTableVersions;	 Catch:{ all -> 0x00c8 }
            r2.checkForInvalidation(r3);	 Catch:{ all -> 0x00c8 }
            goto L_0x00ac;
        L_0x00c6:
            monitor-exit(r0);	 Catch:{ all -> 0x00c8 }
            goto L_0x00cb;
        L_0x00c8:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x00c8 }
            throw r1;
        L_0x00cb:
            return;
        L_0x00cc:
            r0.unlock();
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.arch.persistence.room.InvalidationTracker$C00151.run():void");
        }

        private boolean checkUpdatedTable() {
            Cursor query = InvalidationTracker.this.mDatabase.query("SELECT * FROM room_table_modification_log WHERE version  > ? ORDER BY version ASC;", InvalidationTracker.this.mQueryArgs);
            boolean z = false;
            while (query.moveToNext()) {
                try {
                    long j = query.getLong(0);
                    InvalidationTracker.this.mTableVersions[query.getInt(1)] = j;
                    InvalidationTracker.this.mMaxVersion = j;
                    z = true;
                } finally {
                    query.close();
                }
            }
            return z;
        }
    }

    static class ObservedTableTracker {
        boolean mNeedsSync;
        boolean mPendingSync;
        final long[] mTableObservers;
        final int[] mTriggerStateChanges;
        final boolean[] mTriggerStates;

        ObservedTableTracker(int i) {
            this.mTableObservers = new long[i];
            this.mTriggerStates = new boolean[i];
            this.mTriggerStateChanges = new int[i];
            Arrays.fill(this.mTableObservers, 0);
            Arrays.fill(this.mTriggerStates, false);
        }

        /* Access modifiers changed, original: varargs */
        public boolean onAdded(int... iArr) {
            boolean z;
            synchronized (this) {
                z = false;
                for (int i : iArr) {
                    long j = this.mTableObservers[i];
                    this.mTableObservers[i] = 1 + j;
                    if (j == 0) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        /* Access modifiers changed, original: varargs */
        public boolean onRemoved(int... iArr) {
            boolean z;
            synchronized (this) {
                z = false;
                for (int i : iArr) {
                    long j = this.mTableObservers[i];
                    this.mTableObservers[i] = j - 1;
                    if (j == 1) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        /* Access modifiers changed, original: 0000 */
        public int[] getTablesToSync() {
            synchronized (this) {
                if (this.mNeedsSync) {
                    if (!this.mPendingSync) {
                        int length = this.mTableObservers.length;
                        int i = 0;
                        while (true) {
                            boolean z = true;
                            if (i < length) {
                                boolean z2 = this.mTableObservers[i] > 0;
                                if (z2 != this.mTriggerStates[i]) {
                                    int[] iArr = this.mTriggerStateChanges;
                                    if (!z2) {
                                        z = true;
                                    }
                                    iArr[i] = z;
                                } else {
                                    this.mTriggerStateChanges[i] = 0;
                                }
                                this.mTriggerStates[i] = z2;
                                i++;
                            } else {
                                this.mPendingSync = true;
                                this.mNeedsSync = false;
                                int[] iArr2 = this.mTriggerStateChanges;
                                return iArr2;
                            }
                        }
                    }
                }
                return null;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void onSyncCompleted() {
            synchronized (this) {
                this.mPendingSync = false;
            }
        }
    }

    public static abstract class Observer {
        final String[] mTables;

        public abstract void onInvalidated(Set<String> set);

        protected Observer(String str, String... strArr) {
            this.mTables = (String[]) Arrays.copyOf(strArr, strArr.length + 1);
            this.mTables[strArr.length] = str;
        }

        public Observer(String[] strArr) {
            this.mTables = (String[]) Arrays.copyOf(strArr, strArr.length);
        }
    }

    static class ObserverWrapper {
        final Observer mObserver;
        private final Set<String> mSingleTableSet;
        final int[] mTableIds;
        private final String[] mTableNames;
        private final long[] mVersions;

        ObserverWrapper(Observer observer, int[] iArr, String[] strArr, long[] jArr) {
            this.mObserver = observer;
            this.mTableIds = iArr;
            this.mTableNames = strArr;
            this.mVersions = jArr;
            if (iArr.length == 1) {
                ArraySet arraySet = new ArraySet();
                arraySet.add(this.mTableNames[0]);
                this.mSingleTableSet = Collections.unmodifiableSet(arraySet);
                return;
            }
            this.mSingleTableSet = null;
        }

        /* Access modifiers changed, original: 0000 */
        public void checkForInvalidation(long[] jArr) {
            int length = this.mTableIds.length;
            Set set = null;
            for (int i = 0; i < length; i++) {
                long j = jArr[this.mTableIds[i]];
                if (this.mVersions[i] < j) {
                    this.mVersions[i] = j;
                    if (length == 1) {
                        set = this.mSingleTableSet;
                    } else {
                        if (set == null) {
                            set = new ArraySet(length);
                        }
                        set.add(this.mTableNames[i]);
                    }
                }
            }
            if (set != null) {
                this.mObserver.onInvalidated(set);
            }
        }
    }

    static class WeakObserver extends Observer {
        final WeakReference<Observer> mDelegateRef;
        final InvalidationTracker mTracker;

        WeakObserver(InvalidationTracker invalidationTracker, Observer observer) {
            super(observer.mTables);
            this.mTracker = invalidationTracker;
            this.mDelegateRef = new WeakReference(observer);
        }

        public void onInvalidated(Set<String> set) {
            Observer observer = (Observer) this.mDelegateRef.get();
            if (observer == null) {
                this.mTracker.removeObserver(this);
            } else {
                observer.onInvalidated(set);
            }
        }
    }

    public InvalidationTracker(RoomDatabase roomDatabase, String... strArr) {
        int i = 0;
        this.mPendingRefresh = new AtomicBoolean(false);
        this.mInitialized = false;
        this.mObserverMap = new SafeIterableMap();
        this.mRefreshRunnable = new C00151();
        this.mDatabase = roomDatabase;
        this.mObservedTableTracker = new ObservedTableTracker(strArr.length);
        this.mTableIdLookup = new ArrayMap();
        int length = strArr.length;
        this.mTableNames = new String[length];
        while (i < length) {
            String toLowerCase = strArr[i].toLowerCase(Locale.US);
            this.mTableIdLookup.put(toLowerCase, Integer.valueOf(i));
            this.mTableNames[i] = toLowerCase;
            i++;
        }
        this.mTableVersions = new long[strArr.length];
        Arrays.fill(this.mTableVersions, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public void internalInit(SupportSQLiteDatabase supportSQLiteDatabase) {
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
                syncTriggers(supportSQLiteDatabase);
                this.mCleanupStatement = supportSQLiteDatabase.compileStatement("DELETE FROM room_table_modification_log WHERE version NOT IN( SELECT MAX(version) FROM room_table_modification_log GROUP BY table_id)");
                this.mInitialized = true;
            } finally {
                supportSQLiteDatabase.endTransaction();
            }
        }
    }

    private static void appendTriggerName(StringBuilder stringBuilder, String str, String str2) {
        stringBuilder.append("`");
        stringBuilder.append("room_table_modification_trigger_");
        stringBuilder.append(str);
        stringBuilder.append("_");
        stringBuilder.append(str2);
        stringBuilder.append("`");
    }

    private void stopTrackingTable(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        String str = this.mTableNames[i];
        StringBuilder stringBuilder = new StringBuilder();
        for (String str2 : TRIGGERS) {
            stringBuilder.setLength(0);
            stringBuilder.append("DROP TRIGGER IF EXISTS ");
            appendTriggerName(stringBuilder, str, str2);
            supportSQLiteDatabase.execSQL(stringBuilder.toString());
        }
    }

    private void startTrackingTable(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        String str = this.mTableNames[i];
        StringBuilder stringBuilder = new StringBuilder();
        for (String str2 : TRIGGERS) {
            stringBuilder.setLength(0);
            stringBuilder.append("CREATE TEMP TRIGGER IF NOT EXISTS ");
            appendTriggerName(stringBuilder, str, str2);
            stringBuilder.append(" AFTER ");
            stringBuilder.append(str2);
            stringBuilder.append(" ON `");
            stringBuilder.append(str);
            stringBuilder.append("` BEGIN INSERT OR REPLACE INTO ");
            stringBuilder.append("room_table_modification_log");
            stringBuilder.append(" VALUES(null, ");
            stringBuilder.append(i);
            stringBuilder.append("); END");
            supportSQLiteDatabase.execSQL(stringBuilder.toString());
        }
    }

    public void addObserver(Observer observer) {
        ObserverWrapper observerWrapper;
        String[] strArr = observer.mTables;
        int[] iArr = new int[strArr.length];
        int length = strArr.length;
        long[] jArr = new long[strArr.length];
        int i = 0;
        while (i < length) {
            Integer num = (Integer) this.mTableIdLookup.get(strArr[i].toLowerCase(Locale.US));
            if (num != null) {
                iArr[i] = num.intValue();
                jArr[i] = this.mMaxVersion;
                i++;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("There is no table with name ");
                stringBuilder.append(strArr[i]);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        ObserverWrapper observerWrapper2 = new ObserverWrapper(observer, iArr, strArr, jArr);
        synchronized (this.mObserverMap) {
            observerWrapper = (ObserverWrapper) this.mObserverMap.putIfAbsent(observer, observerWrapper2);
        }
        if (observerWrapper == null && this.mObservedTableTracker.onAdded(iArr)) {
            syncTriggers();
        }
    }

    public void addWeakObserver(Observer observer) {
        addObserver(new WeakObserver(this, observer));
    }

    public void removeObserver(Observer observer) {
        ObserverWrapper observerWrapper;
        synchronized (this.mObserverMap) {
            observerWrapper = (ObserverWrapper) this.mObserverMap.remove(observer);
        }
        if (observerWrapper != null && this.mObservedTableTracker.onRemoved(observerWrapper.mTableIds)) {
            syncTriggers();
        }
    }

    private boolean ensureInitialization() {
        if (!this.mDatabase.isOpen()) {
            return false;
        }
        if (!this.mInitialized) {
            this.mDatabase.getOpenHelper().getWritableDatabase();
        }
        if (this.mInitialized) {
            return true;
        }
        Log.e("ROOM", "database is not initialized even though it is open");
        return false;
    }

    public void refreshVersionsAsync() {
        if (this.mPendingRefresh.compareAndSet(false, true)) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(this.mRefreshRunnable);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void syncTriggers(SupportSQLiteDatabase supportSQLiteDatabase) {
        if (!supportSQLiteDatabase.inTransaction()) {
            while (true) {
                try {
                    Lock closeLock = this.mDatabase.getCloseLock();
                    closeLock.lock();
                    try {
                        int[] tablesToSync = this.mObservedTableTracker.getTablesToSync();
                        if (tablesToSync == null) {
                            closeLock.unlock();
                            return;
                        }
                        int length = tablesToSync.length;
                        supportSQLiteDatabase.beginTransaction();
                        for (int i = 0; i < length; i++) {
                            switch (tablesToSync[i]) {
                                case 1:
                                    startTrackingTable(supportSQLiteDatabase, i);
                                    break;
                                case 2:
                                    stopTrackingTable(supportSQLiteDatabase, i);
                                    break;
                                default:
                                    break;
                            }
                        }
                        supportSQLiteDatabase.setTransactionSuccessful();
                        supportSQLiteDatabase.endTransaction();
                        this.mObservedTableTracker.onSyncCompleted();
                        closeLock.unlock();
                    } catch (Throwable th) {
                        closeLock.unlock();
                    }
                } catch (SQLiteException | IllegalStateException e) {
                    Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", e);
                    return;
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void syncTriggers() {
        if (this.mDatabase.isOpen()) {
            syncTriggers(this.mDatabase.getOpenHelper().getWritableDatabase());
        }
    }
}
