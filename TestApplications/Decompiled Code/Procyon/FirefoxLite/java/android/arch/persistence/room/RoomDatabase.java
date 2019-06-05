// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room;

import java.util.Collections;
import android.util.Log;
import android.support.v4.util.SparseArrayCompat;
import android.annotation.SuppressLint;
import android.support.v4.app.ActivityManagerCompat;
import android.app.ActivityManager;
import java.util.Iterator;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import java.util.HashSet;
import android.arch.persistence.room.migration.Migration;
import java.util.Set;
import android.content.Context;
import java.util.ArrayList;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.database.Cursor;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.Build$VERSION;
import java.util.concurrent.locks.Lock;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.core.executor.ArchTaskExecutor;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteDatabase;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;

public abstract class RoomDatabase
{
    private boolean mAllowMainThreadQueries;
    protected List<Callback> mCallbacks;
    private final ReentrantLock mCloseLock;
    protected volatile SupportSQLiteDatabase mDatabase;
    private final InvalidationTracker mInvalidationTracker;
    private SupportSQLiteOpenHelper mOpenHelper;
    boolean mWriteAheadLoggingEnabled;
    
    public RoomDatabase() {
        this.mCloseLock = new ReentrantLock();
        this.mInvalidationTracker = this.createInvalidationTracker();
    }
    
    public void assertNotMainThread() {
        if (this.mAllowMainThreadQueries) {
            return;
        }
        if (!ArchTaskExecutor.getInstance().isMainThread()) {
            return;
        }
        throw new IllegalStateException("Cannot access database on the main thread since it may potentially lock the UI for a long period of time.");
    }
    
    public void beginTransaction() {
        this.assertNotMainThread();
        final SupportSQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase();
        this.mInvalidationTracker.syncTriggers(writableDatabase);
        writableDatabase.beginTransaction();
    }
    
    public SupportSQLiteStatement compileStatement(final String s) {
        this.assertNotMainThread();
        return this.mOpenHelper.getWritableDatabase().compileStatement(s);
    }
    
    protected abstract InvalidationTracker createInvalidationTracker();
    
    protected abstract SupportSQLiteOpenHelper createOpenHelper(final DatabaseConfiguration p0);
    
    public void endTransaction() {
        this.mOpenHelper.getWritableDatabase().endTransaction();
        if (!this.inTransaction()) {
            this.mInvalidationTracker.refreshVersionsAsync();
        }
    }
    
    Lock getCloseLock() {
        return this.mCloseLock;
    }
    
    public InvalidationTracker getInvalidationTracker() {
        return this.mInvalidationTracker;
    }
    
    public SupportSQLiteOpenHelper getOpenHelper() {
        return this.mOpenHelper;
    }
    
    public boolean inTransaction() {
        return this.mOpenHelper.getWritableDatabase().inTransaction();
    }
    
    public void init(final DatabaseConfiguration databaseConfiguration) {
        this.mOpenHelper = this.createOpenHelper(databaseConfiguration);
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = false;
        final boolean b2 = false;
        if (sdk_INT >= 16) {
            b = b2;
            if (databaseConfiguration.journalMode == JournalMode.WRITE_AHEAD_LOGGING) {
                b = true;
            }
            this.mOpenHelper.setWriteAheadLoggingEnabled(b);
        }
        this.mCallbacks = databaseConfiguration.callbacks;
        this.mAllowMainThreadQueries = databaseConfiguration.allowMainThreadQueries;
        this.mWriteAheadLoggingEnabled = b;
    }
    
    protected void internalInitInvalidationTracker(final SupportSQLiteDatabase supportSQLiteDatabase) {
        this.mInvalidationTracker.internalInit(supportSQLiteDatabase);
    }
    
    public boolean isOpen() {
        final SupportSQLiteDatabase mDatabase = this.mDatabase;
        return mDatabase != null && mDatabase.isOpen();
    }
    
    public Cursor query(final SupportSQLiteQuery supportSQLiteQuery) {
        this.assertNotMainThread();
        return this.mOpenHelper.getWritableDatabase().query(supportSQLiteQuery);
    }
    
    public Cursor query(final String s, final Object[] array) {
        return this.mOpenHelper.getWritableDatabase().query(new SimpleSQLiteQuery(s, array));
    }
    
    public void setTransactionSuccessful() {
        this.mOpenHelper.getWritableDatabase().setTransactionSuccessful();
    }
    
    public static class Builder<T extends RoomDatabase>
    {
        private boolean mAllowMainThreadQueries;
        private ArrayList<Callback> mCallbacks;
        private final Context mContext;
        private final Class<T> mDatabaseClass;
        private SupportSQLiteOpenHelper.Factory mFactory;
        private JournalMode mJournalMode;
        private final MigrationContainer mMigrationContainer;
        private Set<Integer> mMigrationStartAndEndVersions;
        private Set<Integer> mMigrationsNotRequiredFrom;
        private final String mName;
        private boolean mRequireMigration;
        
        Builder(final Context mContext, final Class<T> mDatabaseClass, final String mName) {
            this.mContext = mContext;
            this.mDatabaseClass = mDatabaseClass;
            this.mName = mName;
            this.mJournalMode = JournalMode.AUTOMATIC;
            this.mRequireMigration = true;
            this.mMigrationContainer = new MigrationContainer();
        }
        
        public Builder<T> addCallback(final Callback e) {
            if (this.mCallbacks == null) {
                this.mCallbacks = new ArrayList<Callback>();
            }
            this.mCallbacks.add(e);
            return this;
        }
        
        public Builder<T> addMigrations(final Migration... array) {
            if (this.mMigrationStartAndEndVersions == null) {
                this.mMigrationStartAndEndVersions = new HashSet<Integer>();
            }
            for (final Migration migration : array) {
                this.mMigrationStartAndEndVersions.add(migration.startVersion);
                this.mMigrationStartAndEndVersions.add(migration.endVersion);
            }
            this.mMigrationContainer.addMigrations(array);
            return this;
        }
        
        public Builder<T> allowMainThreadQueries() {
            this.mAllowMainThreadQueries = true;
            return this;
        }
        
        public T build() {
            if (this.mContext == null) {
                throw new IllegalArgumentException("Cannot provide null context for the database.");
            }
            if (this.mDatabaseClass != null) {
                if (this.mMigrationStartAndEndVersions != null && this.mMigrationsNotRequiredFrom != null) {
                    for (final Integer obj : this.mMigrationStartAndEndVersions) {
                        if (!this.mMigrationsNotRequiredFrom.contains(obj)) {
                            continue;
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Inconsistency detected. A Migration was supplied to addMigration(Migration... migrations) that has a start or end version equal to a start version supplied to fallbackToDestructiveMigrationFrom(int... startVersions). Start version: ");
                        sb.append(obj);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
                if (this.mFactory == null) {
                    this.mFactory = new FrameworkSQLiteOpenHelperFactory();
                }
                final DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(this.mContext, this.mName, this.mFactory, this.mMigrationContainer, this.mCallbacks, this.mAllowMainThreadQueries, this.mJournalMode.resolve(this.mContext), this.mRequireMigration, this.mMigrationsNotRequiredFrom);
                final RoomDatabase roomDatabase = Room.getGeneratedImplementation(this.mDatabaseClass, "_Impl");
                roomDatabase.init(databaseConfiguration);
                return (T)roomDatabase;
            }
            throw new IllegalArgumentException("Must provide an abstract class that extends RoomDatabase");
        }
        
        public Builder<T> fallbackToDestructiveMigration() {
            this.mRequireMigration = false;
            return this;
        }
    }
    
    public abstract static class Callback
    {
        public void onCreate(final SupportSQLiteDatabase supportSQLiteDatabase) {
        }
        
        public void onOpen(final SupportSQLiteDatabase supportSQLiteDatabase) {
        }
    }
    
    public enum JournalMode
    {
        AUTOMATIC, 
        TRUNCATE, 
        WRITE_AHEAD_LOGGING;
        
        @SuppressLint({ "NewApi" })
        JournalMode resolve(final Context context) {
            if (this != JournalMode.AUTOMATIC) {
                return this;
            }
            if (Build$VERSION.SDK_INT >= 16) {
                final ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
                if (activityManager != null && !ActivityManagerCompat.isLowRamDevice(activityManager)) {
                    return JournalMode.WRITE_AHEAD_LOGGING;
                }
            }
            return JournalMode.TRUNCATE;
        }
    }
    
    public static class MigrationContainer
    {
        private SparseArrayCompat<SparseArrayCompat<Migration>> mMigrations;
        
        public MigrationContainer() {
            this.mMigrations = new SparseArrayCompat<SparseArrayCompat<Migration>>();
        }
        
        private void addMigration(final Migration obj) {
            final int startVersion = obj.startVersion;
            final int endVersion = obj.endVersion;
            SparseArrayCompat<Migration> sparseArrayCompat;
            if ((sparseArrayCompat = this.mMigrations.get(startVersion)) == null) {
                sparseArrayCompat = new SparseArrayCompat<Migration>();
                this.mMigrations.put(startVersion, sparseArrayCompat);
            }
            final Migration obj2 = sparseArrayCompat.get(endVersion);
            if (obj2 != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Overriding migration ");
                sb.append(obj2);
                sb.append(" with ");
                sb.append(obj);
                Log.w("ROOM", sb.toString());
            }
            sparseArrayCompat.append(endVersion, obj);
        }
        
        private List<Migration> findUpMigrationPath(final List<Migration> list, final boolean b, int n, final int n2) {
            int n3;
            int n4;
            if (b) {
                n3 = -1;
                n4 = n;
            }
            else {
                n3 = 1;
                n4 = n;
            }
            int i;
            do {
                if (b) {
                    if (n4 >= n2) {
                        return list;
                    }
                }
                else if (n4 <= n2) {
                    return list;
                }
                final SparseArrayCompat<Migration> sparseArrayCompat = this.mMigrations.get(n4);
                if (sparseArrayCompat == null) {
                    return null;
                }
                int size = sparseArrayCompat.size();
                final int n5 = 0;
                if (b) {
                    n = size - 1;
                    size = -1;
                }
                else {
                    n = 0;
                }
                int key;
                while (true) {
                    i = n5;
                    key = n4;
                    if (n == size) {
                        break;
                    }
                    key = sparseArrayCompat.keyAt(n);
                    if (b ? (key <= n2 && key > n4) : (key >= n2 && key < n4)) {
                        list.add(sparseArrayCompat.valueAt(n));
                        i = 1;
                        break;
                    }
                    n += n3;
                }
                n4 = key;
            } while (i != 0);
            return null;
        }
        
        public void addMigrations(final Migration... array) {
            for (int length = array.length, i = 0; i < length; ++i) {
                this.addMigration(array[i]);
            }
        }
        
        public List<Migration> findMigrationPath(final int n, final int n2) {
            if (n == n2) {
                return Collections.emptyList();
            }
            return this.findUpMigrationPath(new ArrayList<Migration>(), n2 > n, n, n2);
        }
    }
}
