package net.sqlcipher.database;

import android.util.Log;
import java.util.ArrayList;

public final class SQLiteDebug {
   public static final boolean DEBUG_ACTIVE_CURSOR_FINALIZATION = Log.isLoggable("SQLiteCursorClosing", 2);
   public static final boolean DEBUG_LOCK_TIME_TRACKING = Log.isLoggable("SQLiteLockTime", 2);
   public static final boolean DEBUG_LOCK_TIME_TRACKING_STACK_TRACE = Log.isLoggable("SQLiteLockStackTrace", 2);
   public static final boolean DEBUG_SQL_CACHE = Log.isLoggable("SQLiteCompiledSql", 2);
   public static final boolean DEBUG_SQL_STATEMENTS = Log.isLoggable("SQLiteStatements", 2);
   public static final boolean DEBUG_SQL_TIME = Log.isLoggable("SQLiteTime", 2);
   private static int sNumActiveCursorsFinalized;

   public static SQLiteDebug.PagerStats getDatabaseInfo() {
      SQLiteDebug.PagerStats var0 = new SQLiteDebug.PagerStats();
      getPagerStats(var0);
      var0.dbStats = SQLiteDatabase.getDbStats();
      return var0;
   }

   public static native long getHeapAllocatedSize();

   public static native void getHeapDirtyPages(int[] var0);

   public static native long getHeapFreeSize();

   public static native long getHeapSize();

   public static int getNumActiveCursorsFinalized() {
      return sNumActiveCursorsFinalized;
   }

   public static native void getPagerStats(SQLiteDebug.PagerStats var0);

   static void notifyActiveCursorFinalized() {
      synchronized(SQLiteDebug.class){}

      try {
         ++sNumActiveCursorsFinalized;
      } finally {
         ;
      }

   }

   public static class DbStats {
      public String dbName;
      public long dbSize;
      public int lookaside;
      public long pageSize;

      public DbStats(String var1, long var2, long var4, int var6) {
         this.dbName = var1;
         this.pageSize = var4;
         this.dbSize = var2 * var4 / 1024L;
         this.lookaside = var6;
      }
   }

   public static class PagerStats {
      @Deprecated
      public long databaseBytes;
      public ArrayList dbStats;
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
