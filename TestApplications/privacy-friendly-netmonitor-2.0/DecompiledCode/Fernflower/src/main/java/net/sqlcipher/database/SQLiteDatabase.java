package net.sqlcipher.database;

import android.content.ContentValues;
import android.content.Context;
import android.os.Debug;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import net.sqlcipher.CrossProcessCursorWrapper;
import net.sqlcipher.Cursor;
import net.sqlcipher.CursorWrapper;
import net.sqlcipher.DatabaseErrorHandler;
import net.sqlcipher.SQLException;

public class SQLiteDatabase extends SQLiteClosable {
   private static final String COMMIT_SQL = "COMMIT;";
   public static final int CONFLICT_ABORT = 2;
   public static final int CONFLICT_FAIL = 3;
   public static final int CONFLICT_IGNORE = 4;
   public static final int CONFLICT_NONE = 0;
   public static final int CONFLICT_REPLACE = 5;
   public static final int CONFLICT_ROLLBACK = 1;
   private static final String[] CONFLICT_VALUES = new String[]{"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
   public static final int CREATE_IF_NECESSARY = 268435456;
   private static final Pattern EMAIL_IN_DB_PATTERN = Pattern.compile("[\\w\\.\\-]+@[\\w\\.\\-]+");
   private static final int EVENT_DB_CORRUPT = 75004;
   private static final int EVENT_DB_OPERATION = 52000;
   static final String GET_LOCK_LOG_PREFIX = "GETLOCK:";
   private static final String KEY_ENCODING = "UTF-8";
   private static final int LOCK_ACQUIRED_WARNING_THREAD_TIME_IN_MS = 100;
   private static final int LOCK_ACQUIRED_WARNING_TIME_IN_MS = 300;
   private static final int LOCK_ACQUIRED_WARNING_TIME_IN_MS_ALWAYS_PRINT = 2000;
   private static final int LOCK_WARNING_WINDOW_IN_MS = 20000;
   private static final String LOG_SLOW_QUERIES_PROPERTY = "db.log.slow_query_threshold";
   public static final int MAX_SQL_CACHE_SIZE = 250;
   private static final int MAX_WARNINGS_ON_CACHESIZE_CONDITION = 1;
   public static final String MEMORY = ":memory:";
   public static final int NO_LOCALIZED_COLLATORS = 16;
   public static final int OPEN_READONLY = 1;
   public static final int OPEN_READWRITE = 0;
   private static final int OPEN_READ_MASK = 1;
   private static final int QUERY_LOG_SQL_LENGTH = 64;
   private static final int SLEEP_AFTER_YIELD_QUANTUM = 1000;
   public static final String SQLCIPHER_ANDROID_VERSION = "3.5.7";
   public static final int SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000;
   private static final String TAG = "Database";
   private static WeakHashMap sActiveDatabases = new WeakHashMap();
   private static int sQueryLogTimeInMillis;
   private int mCacheFullWarnings;
   Map mCompiledQueries;
   private final DatabaseErrorHandler mErrorHandler;
   private SQLiteDatabase.CursorFactory mFactory;
   private int mFlags;
   private boolean mInnerTransactionIsSuccessful;
   private long mLastLockMessageTime;
   private String mLastSqlStatement;
   private final ReentrantLock mLock;
   private long mLockAcquiredThreadTime;
   private long mLockAcquiredWallTime;
   private boolean mLockingEnabled;
   private int mMaxSqlCacheSize;
   long mNativeHandle;
   private int mNumCacheHits;
   private int mNumCacheMisses;
   private String mPath;
   private String mPathForLogs;
   private WeakHashMap mPrograms;
   private final int mSlowQueryThreshold;
   private Throwable mStackTrace;
   private final Map mSyncUpdateInfo;
   int mTempTableSequence;
   private String mTimeClosed;
   private String mTimeOpened;
   private boolean mTransactionIsSuccessful;
   private SQLiteTransactionListener mTransactionListener;

   private SQLiteDatabase(String var1, SQLiteDatabase.CursorFactory var2, int var3, DatabaseErrorHandler var4) {
      this.mLock = new ReentrantLock(true);
      this.mLockAcquiredWallTime = 0L;
      this.mLockAcquiredThreadTime = 0L;
      this.mLastLockMessageTime = 0L;
      this.mLastSqlStatement = null;
      this.mNativeHandle = 0L;
      this.mTempTableSequence = 0;
      this.mPathForLogs = null;
      this.mCompiledQueries = new HashMap();
      this.mMaxSqlCacheSize = 250;
      this.mTimeOpened = null;
      this.mTimeClosed = null;
      this.mStackTrace = null;
      this.mLockingEnabled = true;
      this.mSyncUpdateInfo = new HashMap();
      if (var1 == null) {
         throw new IllegalArgumentException("path should not be null");
      } else {
         this.mFlags = var3;
         this.mPath = var1;
         this.mSlowQueryThreshold = -1;
         this.mStackTrace = (new DatabaseObjectNotClosedException()).fillInStackTrace();
         this.mFactory = var2;
         this.mPrograms = new WeakHashMap();
         this.mErrorHandler = var4;
      }
   }

   public SQLiteDatabase(String var1, char[] var2, SQLiteDatabase.CursorFactory var3, int var4) {
      this(var1, var3, var4, (DatabaseErrorHandler)null);
      this.openDatabaseInternal(var2, (SQLiteDatabaseHook)null);
   }

   public SQLiteDatabase(String var1, char[] var2, SQLiteDatabase.CursorFactory var3, int var4, SQLiteDatabaseHook var5) {
      this(var1, var3, var4, (DatabaseErrorHandler)null);
      this.openDatabaseInternal(var2, var5);
   }

   private void checkLockHoldTime() {
      long var1 = SystemClock.elapsedRealtime();
      long var3 = var1 - this.mLockAcquiredWallTime;
      if (var3 >= 2000L || Log.isLoggable("Database", 2) || var1 - this.mLastLockMessageTime >= 20000L) {
         if (var3 > 300L) {
            int var5 = (int)((Debug.threadCpuTimeNanos() - this.mLockAcquiredThreadTime) / 1000000L);
            if (var5 > 100 || var3 > 2000L) {
               this.mLastLockMessageTime = var1;
               StringBuilder var6 = new StringBuilder();
               var6.append("lock held on ");
               var6.append(this.mPath);
               var6.append(" for ");
               var6.append(var3);
               var6.append("ms. Thread time was ");
               var6.append(var5);
               var6.append("ms");
               String var7 = var6.toString();
               if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING_STACK_TRACE) {
                  Log.d("Database", var7, new Exception());
               } else {
                  Log.d("Database", var7);
               }
            }
         }

      }
   }

   private void closeClosable() {
      this.deallocCachedSqlStatements();
      Iterator var1 = this.mPrograms.entrySet().iterator();

      while(var1.hasNext()) {
         SQLiteClosable var2 = (SQLiteClosable)((Entry)var1.next()).getKey();
         if (var2 != null) {
            var2.onAllReferencesReleasedFromContainer();
         }
      }

   }

   private boolean containsNull(char[] var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 != null) {
         var3 = var2;
         if (var1.length > 0) {
            int var4 = var1.length;
            int var5 = 0;

            while(true) {
               var3 = var2;
               if (var5 >= var4) {
                  break;
               }

               if (var1[var5] == 0) {
                  var3 = true;
                  break;
               }

               ++var5;
            }
         }
      }

      return var3;
   }

   public static SQLiteDatabase create(SQLiteDatabase.CursorFactory var0, String var1) {
      char[] var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = var1.toCharArray();
      }

      return openDatabase(":memory:", var2, var0, 268435456);
   }

   public static SQLiteDatabase create(SQLiteDatabase.CursorFactory var0, char[] var1) {
      return openDatabase(":memory:", var1, var0, 268435456);
   }

   private native void dbclose();

   private native void dbopen(String var1, int var2);

   private void deallocCachedSqlStatements() {
      Map var1 = this.mCompiledQueries;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var2;
         try {
            var2 = this.mCompiledQueries.values().iterator();
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  ((SQLiteCompiledSql)var2.next()).releaseSqlStatement();
                  continue;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            try {
               this.mCompiledQueries.clear();
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private native void enableSqlProfiling(String var1);

   private native void enableSqlTracing(String var1);

   public static String findEditTable(String var0) {
      if (TextUtils.isEmpty(var0)) {
         throw new IllegalStateException("Invalid tables");
      } else {
         int var1 = var0.indexOf(32);
         int var2 = var0.indexOf(44);
         if (var1 <= 0 || var1 >= var2 && var2 >= 0) {
            return var2 <= 0 || var2 >= var1 && var1 >= 0 ? var0 : var0.substring(0, var2);
         } else {
            return var0.substring(0, var1);
         }
      }
   }

   private static ArrayList getActiveDatabases() {
      // $FF: Couldn't be decompiled
   }

   private static ArrayList getAttachedDbs(SQLiteDatabase var0) {
      if (!var0.isOpen()) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         Cursor var2 = var0.rawQuery("pragma database_list;", (String[])null);

         while(var2.moveToNext()) {
            var1.add(new Pair(var2.getString(1), var2.getString(2)));
         }

         var2.close();
         return var1;
      }
   }

   private byte[] getBytes(char[] var1) {
      if (var1 != null && var1.length != 0) {
         CharBuffer var3 = CharBuffer.wrap(var1);
         ByteBuffer var2 = Charset.forName("UTF-8").encode(var3);
         byte[] var4 = new byte[var2.limit()];
         var2.get(var4);
         return var4;
      } else {
         return null;
      }
   }

   static ArrayList getDbStats() {
      ArrayList var0 = new ArrayList();
      Iterator var1 = getActiveDatabases().iterator();

      while(true) {
         SQLiteDatabase var2;
         int var3;
         String var4;
         int var5;
         String var6;
         ArrayList var7;
         do {
            do {
               do {
                  if (!var1.hasNext()) {
                     return var0;
                  }

                  var2 = (SQLiteDatabase)var1.next();
               } while(var2 == null);
            } while(!var2.isOpen());

            var3 = var2.native_getDbLookaside();
            var4 = var2.getPath();
            var5 = var4.lastIndexOf("/");
            if (var5 != -1) {
               ++var5;
            } else {
               var5 = 0;
            }

            var6 = var4.substring(var5);
            var7 = getAttachedDbs(var2);
         } while(var7 == null);

         for(var5 = 0; var5 < var7.size(); ++var5) {
            Pair var8 = (Pair)var7.get(var5);
            StringBuilder var12 = new StringBuilder();
            var12.append((String)var8.first);
            var12.append(".page_count;");
            long var9 = getPragmaVal(var2, var12.toString());
            if (var5 == 0) {
               var4 = var6;
            } else {
               var12 = new StringBuilder();
               var12.append("  (attached) ");
               var12.append((String)var8.first);
               String var11 = var12.toString();
               var4 = var11;
               if (((String)var8.second).trim().length() > 0) {
                  var3 = ((String)var8.second).lastIndexOf("/");
                  var12 = new StringBuilder();
                  var12.append(var11);
                  var12.append(" : ");
                  var11 = (String)var8.second;
                  if (var3 != -1) {
                     ++var3;
                  } else {
                     var3 = 0;
                  }

                  var12.append(var11.substring(var3));
                  var4 = var12.toString();
               }

               var3 = 0;
            }

            if (var9 > 0L) {
               var0.add(new SQLiteDebug.DbStats(var4, var9, var2.getPageSize(), var3));
            }
         }
      }
   }

   private String getPathForLogs() {
      if (this.mPathForLogs != null) {
         return this.mPathForLogs;
      } else if (this.mPath == null) {
         return null;
      } else {
         if (this.mPath.indexOf(64) == -1) {
            this.mPathForLogs = this.mPath;
         } else {
            this.mPathForLogs = EMAIL_IN_DB_PATTERN.matcher(this.mPath).replaceAll("XX@YY");
         }

         return this.mPathForLogs;
      }
   }

   private static long getPragmaVal(SQLiteDatabase var0, String var1) {
      if (!var0.isOpen()) {
         return 0L;
      } else {
         Object var2 = null;
         boolean var11 = false;

         SQLiteStatement var3;
         try {
            var11 = true;
            StringBuilder var4 = new StringBuilder();
            var4.append("PRAGMA ");
            var4.append(var1);
            var3 = new SQLiteStatement(var0, var4.toString());
            var11 = false;
         } finally {
            if (var11) {
               if (var2 != null) {
                  ((SQLiteStatement)var2).close();
               }

            }
         }

         long var5;
         try {
            var5 = var3.simpleQueryForLong();
         } finally {
            ;
         }

         if (var3 != null) {
            var3.close();
         }

         return var5;
      }
   }

   private String getTime() {
      return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ", Locale.US)).format(System.currentTimeMillis());
   }

   private native void key(byte[] var1) throws SQLException;

   private void keyDatabase(SQLiteDatabaseHook var1, Runnable var2) {
      if (var1 != null) {
         var1.preKey(this);
      }

      if (var2 != null) {
         var2.run();
      }

      if (var1 != null) {
         var1.postKey(this);
      }

      if (SQLiteDebug.DEBUG_SQL_CACHE) {
         this.mTimeOpened = this.getTime();
      }

      RuntimeException var10000;
      label41: {
         boolean var10001;
         Cursor var5;
         try {
            var5 = this.rawQuery("select count(*) from sqlite_master;", new String[0]);
         } catch (RuntimeException var4) {
            var10000 = var4;
            var10001 = false;
            break label41;
         }

         if (var5 == null) {
            return;
         }

         try {
            var5.moveToFirst();
            var5.getInt(0);
            var5.close();
            return;
         } catch (RuntimeException var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      RuntimeException var6 = var10000;
      Log.e("Database", var6.getMessage(), var6);
      throw var6;
   }

   private native void key_mutf8(char[] var1) throws SQLException;

   private static void loadICUData(Context param0, File param1) {
      // $FF: Couldn't be decompiled
   }

   public static void loadLibs(Context var0) {
      synchronized(SQLiteDatabase.class){}

      try {
         loadLibs(var0, var0.getFilesDir());
      } finally {
         ;
      }

   }

   public static void loadLibs(Context var0, File var1) {
      synchronized(SQLiteDatabase.class){}

      try {
         SQLiteDatabase.LibraryLoader var2 = new SQLiteDatabase.LibraryLoader() {
            public void loadLibraries(String... var1) {
               int var2 = 0;

               for(int var3 = var1.length; var2 < var3; ++var2) {
                  System.loadLibrary(var1[var2]);
               }

            }
         };
         loadLibs(var0, var1, var2);
      } finally {
         ;
      }

   }

   public static void loadLibs(Context var0, File var1, SQLiteDatabase.LibraryLoader var2) {
      synchronized(SQLiteDatabase.class){}

      try {
         var2.loadLibraries("sqlcipher");
      } finally {
         ;
      }

   }

   public static void loadLibs(Context var0, SQLiteDatabase.LibraryLoader var1) {
      synchronized(SQLiteDatabase.class){}

      try {
         loadLibs(var0, var0.getFilesDir(), var1);
      } finally {
         ;
      }

   }

   private void lockForced() {
      this.mLock.lock();
      if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
         this.mLockAcquiredWallTime = SystemClock.elapsedRealtime();
         this.mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
      }

   }

   private void markTableSyncable(String param1, String param2, String param3, String param4) {
      // $FF: Couldn't be decompiled
   }

   private native int native_getDbLookaside();

   private native void native_key(char[] var1) throws SQLException;

   private native void native_rawExecSQL(String var1);

   private native void native_rekey(String var1) throws SQLException;

   private native int native_status(int var1, boolean var2);

   public static SQLiteDatabase openDatabase(String var0, String var1, SQLiteDatabase.CursorFactory var2, int var3) {
      return openDatabase(var0, (String)var1, var2, var3, (SQLiteDatabaseHook)null);
   }

   public static SQLiteDatabase openDatabase(String var0, String var1, SQLiteDatabase.CursorFactory var2, int var3, SQLiteDatabaseHook var4) {
      return openDatabase(var0, (String)var1, var2, var3, var4, (DatabaseErrorHandler)null);
   }

   public static SQLiteDatabase openDatabase(String var0, String var1, SQLiteDatabase.CursorFactory var2, int var3, SQLiteDatabaseHook var4, DatabaseErrorHandler var5) {
      char[] var6;
      if (var1 == null) {
         var6 = null;
      } else {
         var6 = var1.toCharArray();
      }

      return openDatabase(var0, var6, var2, var3, var4, var5);
   }

   public static SQLiteDatabase openDatabase(String var0, char[] var1, SQLiteDatabase.CursorFactory var2, int var3) {
      return openDatabase(var0, (char[])var1, var2, var3, (SQLiteDatabaseHook)null, (DatabaseErrorHandler)null);
   }

   public static SQLiteDatabase openDatabase(String var0, char[] var1, SQLiteDatabase.CursorFactory var2, int var3, SQLiteDatabaseHook var4) {
      return openDatabase(var0, (char[])var1, var2, var3, var4, (DatabaseErrorHandler)null);
   }

   public static SQLiteDatabase openDatabase(String param0, char[] param1, SQLiteDatabase.CursorFactory param2, int param3, SQLiteDatabaseHook param4, DatabaseErrorHandler param5) {
      // $FF: Couldn't be decompiled
   }

   private void openDatabaseInternal(final char[] var1, SQLiteDatabaseHook var2) {
      final byte[] var3 = this.getBytes(var1);
      this.dbopen(this.mPath, this.mFlags);
      byte var4 = 0;
      byte var5 = 0;
      int var6 = 0;

      int var34;
      byte var37;
      label445: {
         Throwable var10000;
         label446: {
            Runnable var36;
            RuntimeException var7;
            boolean var10001;
            try {
               try {
                  var36 = new Runnable() {
                     public void run() {
                        if (var3 != null && var3.length > 0) {
                           SQLiteDatabase.this.key(var3);
                        }

                     }
                  };
                  this.keyDatabase(var2, var36);
                  break label445;
               } catch (RuntimeException var31) {
                  var7 = var31;
               }
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label446;
            }

            label452: {
               try {
                  if (!this.containsNull(var1)) {
                     break label452;
                  }

                  var36 = new Runnable() {
                     public void run() {
                        if (var1 != null) {
                           SQLiteDatabase.this.key_mutf8(var1);
                        }

                     }
                  };
                  this.keyDatabase(var2, var36);
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label446;
               }

               if (var3 != null) {
                  try {
                     if (var3.length > 0) {
                        this.rekey(var3);
                     }
                  } catch (Throwable var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label446;
                  }
               }

               if (var3 != null && var3.length > 0) {
                  int var35 = var3.length;

                  for(var6 = var4; var6 < var35; ++var6) {
                     var37 = var3[var6];
                  }
               }

               return;
            }

            label423:
            try {
               throw var7;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               break label423;
            }
         }

         Throwable var33 = var10000;
         this.dbclose();
         if (SQLiteDebug.DEBUG_SQL_CACHE) {
            this.mTimeClosed = this.getTime();
         }

         if (var3 != null && var3.length > 0) {
            var34 = var3.length;

            for(var6 = var5; var6 < var34; ++var6) {
               var37 = var3[var6];
            }
         }

         throw var33;
      }

      if (var3 != null && var3.length > 0) {
         for(var34 = var3.length; var6 < var34; ++var6) {
            var37 = var3[var6];
         }
      }

   }

   public static SQLiteDatabase openOrCreateDatabase(File var0, String var1, SQLiteDatabase.CursorFactory var2) {
      return openOrCreateDatabase((File)var0, (String)var1, var2, (SQLiteDatabaseHook)null);
   }

   public static SQLiteDatabase openOrCreateDatabase(File var0, String var1, SQLiteDatabase.CursorFactory var2, SQLiteDatabaseHook var3) {
      return openOrCreateDatabase((File)var0, (String)var1, var2, var3, (DatabaseErrorHandler)null);
   }

   public static SQLiteDatabase openOrCreateDatabase(File var0, String var1, SQLiteDatabase.CursorFactory var2, SQLiteDatabaseHook var3, DatabaseErrorHandler var4) {
      String var5;
      if (var0 == null) {
         var5 = null;
      } else {
         var5 = var0.getPath();
      }

      return openOrCreateDatabase(var5, var1, var2, var3, var4);
   }

   public static SQLiteDatabase openOrCreateDatabase(String var0, String var1, SQLiteDatabase.CursorFactory var2) {
      return openDatabase(var0, (String)var1, var2, 268435456, (SQLiteDatabaseHook)null);
   }

   public static SQLiteDatabase openOrCreateDatabase(String var0, String var1, SQLiteDatabase.CursorFactory var2, SQLiteDatabaseHook var3) {
      return openDatabase(var0, var1, var2, 268435456, var3);
   }

   public static SQLiteDatabase openOrCreateDatabase(String var0, String var1, SQLiteDatabase.CursorFactory var2, SQLiteDatabaseHook var3, DatabaseErrorHandler var4) {
      char[] var5;
      if (var1 == null) {
         var5 = null;
      } else {
         var5 = var1.toCharArray();
      }

      return openDatabase(var0, var5, var2, 268435456, var3, var4);
   }

   public static SQLiteDatabase openOrCreateDatabase(String var0, char[] var1, SQLiteDatabase.CursorFactory var2) {
      return openDatabase(var0, (char[])var1, var2, 268435456, (SQLiteDatabaseHook)null);
   }

   public static SQLiteDatabase openOrCreateDatabase(String var0, char[] var1, SQLiteDatabase.CursorFactory var2, SQLiteDatabaseHook var3) {
      return openDatabase(var0, var1, var2, 268435456, var3);
   }

   public static SQLiteDatabase openOrCreateDatabase(String var0, char[] var1, SQLiteDatabase.CursorFactory var2, SQLiteDatabaseHook var3, DatabaseErrorHandler var4) {
      return openDatabase(var0, var1, var2, 268435456, var3, var4);
   }

   private native void rekey(byte[] var1) throws SQLException;

   public static native int releaseMemory();

   public static native void setICURoot(String var0);

   private void unlockForced() {
      if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
         this.checkLockHoldTime();
      }

      this.mLock.unlock();
   }

   private boolean yieldIfContendedHelper(boolean var1, long var2) {
      if (this.mLock.getQueueLength() == 0) {
         this.mLockAcquiredWallTime = SystemClock.elapsedRealtime();
         this.mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
         return false;
      } else {
         this.setTransactionSuccessful();
         SQLiteTransactionListener var4 = this.mTransactionListener;
         this.endTransaction();
         if (var1 && this.isDbLockedByCurrentThread()) {
            throw new IllegalStateException("Db locked more than once. yielfIfContended cannot yield");
         } else {
            if (var2 > 0L) {
               while(var2 > 0L) {
                  long var5;
                  if (var2 < 1000L) {
                     var5 = var2;
                  } else {
                     var5 = 1000L;
                  }

                  try {
                     Thread.sleep(var5);
                  } catch (InterruptedException var8) {
                     Thread.interrupted();
                  }

                  if (this.mLock.getQueueLength() == 0) {
                     break;
                  }

                  var2 -= 1000L;
               }
            }

            this.beginTransactionWithListener(var4);
            return true;
         }
      }
   }

   void addSQLiteClosable(SQLiteClosable var1) {
      this.lock();

      try {
         this.mPrograms.put(var1, (Object)null);
      } finally {
         this.unlock();
      }

   }

   void addToCompiledQueries(String var1, SQLiteCompiledSql var2) {
      StringBuilder var48;
      if (this.mMaxSqlCacheSize == 0) {
         if (SQLiteDebug.DEBUG_SQL_CACHE) {
            var48 = new StringBuilder();
            var48.append("|NOT adding_sql_to_cache|");
            var48.append(this.getPath());
            var48.append("|");
            var48.append(var1);
            Log.v("Database", var48.toString());
         }

      } else {
         Map var3 = this.mCompiledQueries;
         synchronized(var3){}

         Throwable var10000;
         boolean var10001;
         label438: {
            try {
               if ((SQLiteCompiledSql)this.mCompiledQueries.get(var1) != null) {
                  return;
               }
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label438;
            }

            label428: {
               label427: {
                  int var4;
                  try {
                     if (this.mCompiledQueries.size() != this.mMaxSqlCacheSize) {
                        break label427;
                     }

                     var4 = this.mCacheFullWarnings + 1;
                     this.mCacheFullWarnings = var4;
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label438;
                  }

                  if (var4 == 1) {
                     try {
                        var48 = new StringBuilder();
                        var48.append("Reached MAX size for compiled-sql statement cache for database ");
                        var48.append(this.getPath());
                        var48.append("; i.e., NO space for this sql statement in cache: ");
                        var48.append(var1);
                        var48.append(". Please change your sql statements to use '?' for ");
                        var48.append("bindargs, instead of using actual values");
                        Log.w("Database", var48.toString());
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label438;
                     }
                  }
                  break label428;
               }

               try {
                  this.mCompiledQueries.put(var1, var2);
                  if (SQLiteDebug.DEBUG_SQL_CACHE) {
                     var48 = new StringBuilder();
                     var48.append("|adding_sql_to_cache|");
                     var48.append(this.getPath());
                     var48.append("|");
                     var48.append(this.mCompiledQueries.size());
                     var48.append("|");
                     var48.append(var1);
                     Log.v("Database", var48.toString());
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label438;
               }
            }

            label416:
            try {
               return;
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label416;
            }
         }

         while(true) {
            Throwable var47 = var10000;

            try {
               throw var47;
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void beginTransaction() {
      this.beginTransactionWithListener((SQLiteTransactionListener)null);
   }

   public void beginTransactionWithListener(SQLiteTransactionListener param1) {
      // $FF: Couldn't be decompiled
   }

   public void changePassword(String var1) throws SQLiteException {
      if (!this.isOpen()) {
         throw new SQLiteException("database not open");
      } else {
         if (var1 != null) {
            byte[] var5 = this.getBytes(var1.toCharArray());
            this.rekey(var5);
            int var2 = var5.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               byte var10000 = var5[var3];
            }
         }

      }
   }

   public void changePassword(char[] var1) throws SQLiteException {
      if (!this.isOpen()) {
         throw new SQLiteException("database not open");
      } else {
         if (var1 != null) {
            byte[] var5 = this.getBytes(var1);
            this.rekey(var5);
            int var2 = var5.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               byte var10000 = var5[var3];
            }
         }

      }
   }

   public void close() {
      if (this.isOpen()) {
         this.lock();

         try {
            this.closeClosable();
            this.onAllReferencesReleased();
         } finally {
            this.unlock();
         }

      }
   }

   public SQLiteStatement compileStatement(String var1) throws SQLException {
      this.lock();
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         SQLiteStatement var4;
         try {
            var4 = new SQLiteStatement(this, var1);
         } finally {
            this.unlock();
         }

         return var4;
      }
   }

   public int delete(String param1, String param2, String[] param3) {
      // $FF: Couldn't be decompiled
   }

   public void endTransaction() {
      // $FF: Couldn't be decompiled
   }

   public void execSQL(String var1) throws SQLException {
      SystemClock.uptimeMillis();
      this.lock();
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         try {
            this.native_execSQL(var1);
         } catch (SQLiteDatabaseCorruptException var4) {
            this.onCorruption();
            throw var4;
         } finally {
            this.unlock();
         }

      }
   }

   public void execSQL(String param1, Object[] param2) throws SQLException {
      // $FF: Couldn't be decompiled
   }

   protected void finalize() {
      if (this.isOpen()) {
         StringBuilder var1 = new StringBuilder();
         var1.append("close() was never explicitly called on database '");
         var1.append(this.mPath);
         var1.append("' ");
         Log.e("Database", var1.toString(), this.mStackTrace);
         this.closeClosable();
         this.onAllReferencesReleased();
      }

   }

   SQLiteCompiledSql getCompiledStatementForSql(String var1) {
      Map var2 = this.mCompiledQueries;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label381: {
         label376: {
            try {
               if (this.mMaxSqlCacheSize != 0) {
                  break label376;
               }

               if (SQLiteDebug.DEBUG_SQL_CACHE) {
                  StringBuilder var35 = new StringBuilder();
                  var35.append("|cache NOT found|");
                  var35.append(this.getPath());
                  Log.v("Database", var35.toString());
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label381;
            }

            try {
               return null;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label381;
            }
         }

         SQLiteCompiledSql var3;
         try {
            var3 = (SQLiteCompiledSql)this.mCompiledQueries.get(var1);
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label381;
         }

         boolean var4;
         if (var3 != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         try {
            ;
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label381;
         }

         if (var4) {
            ++this.mNumCacheHits;
         } else {
            ++this.mNumCacheMisses;
         }

         if (SQLiteDebug.DEBUG_SQL_CACHE) {
            StringBuilder var37 = new StringBuilder();
            var37.append("|cache_stats|");
            var37.append(this.getPath());
            var37.append("|");
            var37.append(this.mCompiledQueries.size());
            var37.append("|");
            var37.append(this.mNumCacheHits);
            var37.append("|");
            var37.append(this.mNumCacheMisses);
            var37.append("|");
            var37.append(var4);
            var37.append("|");
            var37.append(this.mTimeOpened);
            var37.append("|");
            var37.append(this.mTimeClosed);
            var37.append("|");
            var37.append(var1);
            Log.v("Database", var37.toString());
         }

         return var3;
      }

      while(true) {
         Throwable var36 = var10000;

         try {
            throw var36;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public int getMaxSqlCacheSize() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.mMaxSqlCacheSize;
      } finally {
         ;
      }

      return var1;
   }

   public long getMaximumSize() {
      this.lock();
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         Object var1 = null;
         boolean var12 = false;

         SQLiteStatement var2;
         try {
            var12 = true;
            var2 = new SQLiteStatement(this, "PRAGMA max_page_count;");
            var12 = false;
         } finally {
            if (var12) {
               if (var1 != null) {
                  ((SQLiteStatement)var1).close();
               }

               this.unlock();
            }
         }

         long var3;
         long var5;
         try {
            var3 = var2.simpleQueryForLong();
            var5 = this.getPageSize();
         } finally {
            ;
         }

         if (var2 != null) {
            var2.close();
         }

         this.unlock();
         return var3 * var5;
      }
   }

   public long getPageSize() {
      this.lock();
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         Object var1 = null;
         boolean var10 = false;

         SQLiteStatement var2;
         try {
            var10 = true;
            var2 = new SQLiteStatement(this, "PRAGMA page_size;");
            var10 = false;
         } finally {
            if (var10) {
               if (var1 != null) {
                  ((SQLiteStatement)var1).close();
               }

               this.unlock();
            }
         }

         long var3;
         try {
            var3 = var2.simpleQueryForLong();
         } finally {
            ;
         }

         if (var2 != null) {
            var2.close();
         }

         this.unlock();
         return var3;
      }
   }

   public final String getPath() {
      return this.mPath;
   }

   public Map getSyncedTables() {
      Map var1 = this.mSyncUpdateInfo;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label221: {
         HashMap var2;
         Iterator var3;
         try {
            var2 = new HashMap();
            var3 = this.mSyncUpdateInfo.keySet().iterator();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label221;
         }

         while(true) {
            try {
               while(var3.hasNext()) {
                  String var4 = (String)var3.next();
                  SQLiteDatabase.SyncUpdateInfo var5 = (SQLiteDatabase.SyncUpdateInfo)this.mSyncUpdateInfo.get(var4);
                  if (var5.deletedTable != null) {
                     var2.put(var4, var5.deletedTable);
                  }
               }
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break;
            }

            try {
               return var2;
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var26 = var10000;

         try {
            throw var26;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   public int getVersion() {
      this.lock();
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         Object var1 = null;
         boolean var11 = false;

         SQLiteStatement var2;
         try {
            var11 = true;
            var2 = new SQLiteStatement(this, "PRAGMA user_version;");
            var11 = false;
         } finally {
            if (var11) {
               if (var1 != null) {
                  ((SQLiteStatement)var1).close();
               }

               this.unlock();
            }
         }

         long var3;
         try {
            var3 = var2.simpleQueryForLong();
         } finally {
            ;
         }

         int var5 = (int)var3;
         if (var2 != null) {
            var2.close();
         }

         this.unlock();
         return var5;
      }
   }

   public boolean inTransaction() {
      boolean var1;
      if (this.mLock.getHoldCount() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public long insert(String var1, String var2, ContentValues var3) {
      try {
         long var4 = this.insertWithOnConflict(var1, var2, var3, 0);
         return var4;
      } catch (SQLException var6) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Error inserting <redacted values> into ");
         var7.append(var1);
         Log.e("Database", var7.toString(), var6);
         return -1L;
      }
   }

   public long insertOrThrow(String var1, String var2, ContentValues var3) throws SQLException {
      return this.insertWithOnConflict(var1, var2, var3, 0);
   }

   public long insertWithOnConflict(String param1, String param2, ContentValues param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   public boolean isDbLockedByCurrentThread() {
      return this.mLock.isHeldByCurrentThread();
   }

   public boolean isDbLockedByOtherThreads() {
      boolean var1;
      if (!this.mLock.isHeldByCurrentThread() && this.mLock.isLocked()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isInCompiledSqlCache(String param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean isOpen() {
      boolean var1;
      if (this.mNativeHandle != 0L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isReadOnly() {
      int var1 = this.mFlags;
      boolean var2 = true;
      if ((var1 & 1) != 1) {
         var2 = false;
      }

      return var2;
   }

   native int lastChangeCount();

   native long lastInsertRow();

   void lock() {
      if (this.mLockingEnabled) {
         this.mLock.lock();
         if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
            this.mLockAcquiredWallTime = SystemClock.elapsedRealtime();
            this.mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
         }

      }
   }

   public void markTableSyncable(String var1, String var2) {
      if (!this.isOpen()) {
         throw new SQLiteException("database not open");
      } else {
         this.markTableSyncable(var1, "_id", var1, var2);
      }
   }

   public void markTableSyncable(String var1, String var2, String var3) {
      if (!this.isOpen()) {
         throw new SQLiteException("database not open");
      } else {
         this.markTableSyncable(var1, var2, var3, (String)null);
      }
   }

   native void native_execSQL(String var1) throws SQLException;

   native void native_setLocale(String var1, int var2);

   public boolean needUpgrade(int var1) {
      boolean var2;
      if (var1 > this.getVersion()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   protected void onAllReferencesReleased() {
      // $FF: Couldn't be decompiled
   }

   void onCorruption() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Calling error handler for corrupt database (detected) ");
      var1.append(this.mPath);
      Log.e("Database", var1.toString());
      this.mErrorHandler.onCorruption(this);
   }

   public void purgeFromCompiledSqlCache(String param1) {
      // $FF: Couldn't be decompiled
   }

   public Cursor query(String var1, String[] var2, String var3, String[] var4, String var5, String var6, String var7) {
      return this.query(false, var1, var2, var3, var4, var5, var6, var7, (String)null);
   }

   public Cursor query(String var1, String[] var2, String var3, String[] var4, String var5, String var6, String var7, String var8) {
      return this.query(false, var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public Cursor query(boolean var1, String var2, String[] var3, String var4, String[] var5, String var6, String var7, String var8, String var9) {
      return this.queryWithFactory((SQLiteDatabase.CursorFactory)null, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public Cursor queryWithFactory(SQLiteDatabase.CursorFactory var1, boolean var2, String var3, String[] var4, String var5, String[] var6, String var7, String var8, String var9, String var10) {
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         return this.rawQueryWithFactory(var1, SQLiteQueryBuilder.buildQueryString(var2, var3, var4, var5, var7, var8, var9, var10), var6, findEditTable(var3));
      }
   }

   public void rawExecSQL(String var1) {
      SystemClock.uptimeMillis();
      this.lock();
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         try {
            this.native_rawExecSQL(var1);
         } catch (SQLiteDatabaseCorruptException var4) {
            this.onCorruption();
            throw var4;
         } finally {
            this.unlock();
         }

      }
   }

   public Cursor rawQuery(String var1, Object[] var2) {
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         long var3 = 0L;
         int var5 = this.mSlowQueryThreshold;
         int var6 = -1;
         if (var5 != -1) {
            var3 = System.currentTimeMillis();
         }

         SQLiteDirectCursorDriver var11 = new SQLiteDirectCursorDriver(this, var1, (String)null);
         boolean var9 = false;

         StringBuilder var7;
         Cursor var12;
         try {
            var9 = true;
            var12 = var11.query(this.mFactory, var2);
            var9 = false;
         } finally {
            if (var9) {
               if (this.mSlowQueryThreshold != -1) {
                  var3 = System.currentTimeMillis() - var3;
                  if (var3 >= (long)this.mSlowQueryThreshold) {
                     var7 = new StringBuilder();
                     var7.append("query (");
                     var7.append(var3);
                     var7.append(" ms): ");
                     var7.append(var11.toString());
                     var7.append(", args are <redacted>, count is ");
                     var7.append(-1);
                     Log.v("Database", var7.toString());
                  }
               }

            }
         }

         if (this.mSlowQueryThreshold != -1) {
            if (var12 != null) {
               var6 = var12.getCount();
            }

            var3 = System.currentTimeMillis() - var3;
            if (var3 >= (long)this.mSlowQueryThreshold) {
               var7 = new StringBuilder();
               var7.append("query (");
               var7.append(var3);
               var7.append(" ms): ");
               var7.append(var11.toString());
               var7.append(", args are <redacted>, count is ");
               var7.append(var6);
               Log.v("Database", var7.toString());
            }
         }

         return new CrossProcessCursorWrapper(var12);
      }
   }

   public Cursor rawQuery(String var1, String[] var2) {
      return this.rawQueryWithFactory((SQLiteDatabase.CursorFactory)null, var1, var2, (String)null);
   }

   public Cursor rawQuery(String var1, String[] var2, int var3, int var4) {
      CursorWrapper var5 = (CursorWrapper)this.rawQueryWithFactory((SQLiteDatabase.CursorFactory)null, var1, var2, (String)null);
      ((SQLiteCursor)var5.getWrappedCursor()).setLoadStyle(var3, var4);
      return var5;
   }

   public Cursor rawQueryWithFactory(SQLiteDatabase.CursorFactory var1, String var2, String[] var3, String var4) {
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         long var5 = 0L;
         int var7 = this.mSlowQueryThreshold;
         int var8 = -1;
         if (var7 != -1) {
            var5 = System.currentTimeMillis();
         }

         SQLiteDirectCursorDriver var16;
         Cursor var19;
         label139: {
            Throwable var10000;
            label132: {
               var16 = new SQLiteDirectCursorDriver(this, var2, var4);
               boolean var10001;
               if (var1 == null) {
                  try {
                     var1 = this.mFactory;
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label132;
                  }
               }

               label128:
               try {
                  var19 = var16.query(var1, var3);
                  break label139;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label128;
               }
            }

            Throwable var15 = var10000;
            if (this.mSlowQueryThreshold != -1) {
               var5 = System.currentTimeMillis() - var5;
               if (var5 >= (long)this.mSlowQueryThreshold) {
                  StringBuilder var18 = new StringBuilder();
                  var18.append("query (");
                  var18.append(var5);
                  var18.append(" ms): ");
                  var18.append(var16.toString());
                  var18.append(", args are <redacted>, count is ");
                  var18.append(-1);
                  Log.v("Database", var18.toString());
               }
            }

            throw var15;
         }

         if (this.mSlowQueryThreshold != -1) {
            if (var19 != null) {
               var8 = var19.getCount();
            }

            var5 = System.currentTimeMillis() - var5;
            if (var5 >= (long)this.mSlowQueryThreshold) {
               StringBuilder var17 = new StringBuilder();
               var17.append("query (");
               var17.append(var5);
               var17.append(" ms): ");
               var17.append(var16.toString());
               var17.append(", args are <redacted>, count is ");
               var17.append(var8);
               Log.v("Database", var17.toString());
            }
         }

         return new CrossProcessCursorWrapper(var19);
      }
   }

   void removeSQLiteClosable(SQLiteClosable var1) {
      this.lock();

      try {
         this.mPrograms.remove(var1);
      } finally {
         this.unlock();
      }

   }

   public long replace(String var1, String var2, ContentValues var3) {
      try {
         long var4 = this.insertWithOnConflict(var1, var2, var3, 5);
         return var4;
      } catch (SQLException var6) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Error inserting <redacted values> into ");
         var7.append(var1);
         Log.e("Database", var7.toString(), var6);
         return -1L;
      }
   }

   public long replaceOrThrow(String var1, String var2, ContentValues var3) throws SQLException {
      return this.insertWithOnConflict(var1, var2, var3, 5);
   }

   public void resetCompiledSqlCache() {
      // $FF: Couldn't be decompiled
   }

   void rowUpdated(String param1, long param2) {
      // $FF: Couldn't be decompiled
   }

   public void setLocale(Locale var1) {
      this.lock();

      try {
         this.native_setLocale(var1.toString(), this.mFlags);
      } finally {
         this.unlock();
      }

   }

   public void setLockingEnabled(boolean var1) {
      this.mLockingEnabled = var1;
   }

   public void setMaxSqlCacheSize(int var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      IllegalStateException var2;
      if (var1 <= 250 && var1 >= 0) {
         label129: {
            try {
               if (var1 < this.mMaxSqlCacheSize) {
                  var2 = new IllegalStateException("cannot set cacheSize to a value less than the value set with previous setMaxSqlCacheSize() call.");
                  throw var2;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label129;
            }

            try {
               this.mMaxSqlCacheSize = var1;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label129;
            }

            return;
         }
      } else {
         label117:
         try {
            var2 = new IllegalStateException("expected value between 0 and 250");
            throw var2;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label117;
         }
      }

      Throwable var15 = var10000;
      throw var15;
   }

   public long setMaximumSize(long var1) {
      this.lock();
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else {
         Object var3 = null;

         long var4;
         SQLiteStatement var10;
         label190: {
            Throwable var10000;
            label193: {
               long var6;
               boolean var10001;
               try {
                  var4 = this.getPageSize();
                  var6 = var1 / var4;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label193;
               }

               long var8 = var6;
               if (var1 % var4 != 0L) {
                  var8 = var6 + 1L;
               }

               label179:
               try {
                  StringBuilder var24 = new StringBuilder();
                  var24.append("PRAGMA max_page_count = ");
                  var24.append(var8);
                  var10 = new SQLiteStatement(this, var24.toString());
                  break label190;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label179;
               }
            }

            Throwable var11 = var10000;
            if (var3 != null) {
               ((SQLiteStatement)var3).close();
            }

            this.unlock();
            throw var11;
         }

         try {
            var1 = var10.simpleQueryForLong();
         } finally {
            ;
         }

         if (var10 != null) {
            var10.close();
         }

         this.unlock();
         return var1 * var4;
      }
   }

   public void setPageSize(long var1) {
      StringBuilder var3 = new StringBuilder();
      var3.append("PRAGMA page_size = ");
      var3.append(var1);
      this.execSQL(var3.toString());
   }

   public void setTransactionSuccessful() {
      if (!this.isOpen()) {
         throw new IllegalStateException("database not open");
      } else if (!this.mLock.isHeldByCurrentThread()) {
         throw new IllegalStateException("no transaction pending");
      } else if (this.mInnerTransactionIsSuccessful) {
         throw new IllegalStateException("setTransactionSuccessful may only be called once per call to beginTransaction");
      } else {
         this.mInnerTransactionIsSuccessful = true;
      }
   }

   public void setVersion(int var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("PRAGMA user_version = ");
      var2.append(var1);
      this.execSQL(var2.toString());
   }

   public int status(int var1, boolean var2) {
      return this.native_status(var1, var2);
   }

   void unlock() {
      if (this.mLockingEnabled) {
         if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING && this.mLock.getHoldCount() == 1) {
            this.checkLockHoldTime();
         }

         this.mLock.unlock();
      }
   }

   public int update(String var1, ContentValues var2, String var3, String[] var4) {
      return this.updateWithOnConflict(var1, var2, var3, var4, 0);
   }

   public int updateWithOnConflict(String param1, ContentValues param2, String param3, String[] param4, int param5) {
      // $FF: Couldn't be decompiled
   }

   @Deprecated
   public boolean yieldIfContended() {
      return !this.isOpen() ? false : this.yieldIfContendedHelper(false, -1L);
   }

   public boolean yieldIfContendedSafely() {
      return !this.isOpen() ? false : this.yieldIfContendedHelper(true, -1L);
   }

   public boolean yieldIfContendedSafely(long var1) {
      return !this.isOpen() ? false : this.yieldIfContendedHelper(true, var1);
   }

   public interface CursorFactory {
      Cursor newCursor(SQLiteDatabase var1, SQLiteCursorDriver var2, String var3, SQLiteQuery var4);
   }

   public interface LibraryLoader {
      void loadLibraries(String... var1);
   }

   private static class SyncUpdateInfo {
      String deletedTable;
      String foreignKey;
      String masterTable;

      SyncUpdateInfo(String var1, String var2, String var3) {
         this.masterTable = var1;
         this.deletedTable = var2;
         this.foreignKey = var3;
      }
   }
}
