// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import java.util.ArrayList;
import android.util.Log;

public final class SQLiteDebug
{
    public static final boolean DEBUG_ACTIVE_CURSOR_FINALIZATION;
    public static final boolean DEBUG_LOCK_TIME_TRACKING;
    public static final boolean DEBUG_LOCK_TIME_TRACKING_STACK_TRACE;
    public static final boolean DEBUG_SQL_CACHE;
    public static final boolean DEBUG_SQL_STATEMENTS;
    public static final boolean DEBUG_SQL_TIME;
    private static int sNumActiveCursorsFinalized;
    
    static {
        DEBUG_SQL_STATEMENTS = Log.isLoggable("SQLiteStatements", 2);
        DEBUG_SQL_TIME = Log.isLoggable("SQLiteTime", 2);
        DEBUG_SQL_CACHE = Log.isLoggable("SQLiteCompiledSql", 2);
        DEBUG_ACTIVE_CURSOR_FINALIZATION = Log.isLoggable("SQLiteCursorClosing", 2);
        DEBUG_LOCK_TIME_TRACKING = Log.isLoggable("SQLiteLockTime", 2);
        DEBUG_LOCK_TIME_TRACKING_STACK_TRACE = Log.isLoggable("SQLiteLockStackTrace", 2);
    }
    
    public static PagerStats getDatabaseInfo() {
        final PagerStats pagerStats = new PagerStats();
        getPagerStats(pagerStats);
        pagerStats.dbStats = SQLiteDatabase.getDbStats();
        return pagerStats;
    }
    
    public static native long getHeapAllocatedSize();
    
    public static native void getHeapDirtyPages(final int[] p0);
    
    public static native long getHeapFreeSize();
    
    public static native long getHeapSize();
    
    public static int getNumActiveCursorsFinalized() {
        return SQLiteDebug.sNumActiveCursorsFinalized;
    }
    
    public static native void getPagerStats(final PagerStats p0);
    
    static void notifyActiveCursorFinalized() {
        synchronized (SQLiteDebug.class) {
            ++SQLiteDebug.sNumActiveCursorsFinalized;
        }
    }
    
    public static class DbStats
    {
        public String dbName;
        public long dbSize;
        public int lookaside;
        public long pageSize;
        
        public DbStats(final String dbName, final long n, final long pageSize, final int lookaside) {
            this.dbName = dbName;
            this.pageSize = pageSize;
            this.dbSize = n * pageSize / 1024L;
            this.lookaside = lookaside;
        }
    }
    
    public static class PagerStats
    {
        @Deprecated
        public long databaseBytes;
        public ArrayList<DbStats> dbStats;
        public int largestMemAlloc;
        public int memoryUsed;
        @Deprecated
        public int numPagers;
        public int pageCacheOverflo;
        @Deprecated
        public long referencedBytes;
        @Deprecated
        public long totalBytes;
    }
}
