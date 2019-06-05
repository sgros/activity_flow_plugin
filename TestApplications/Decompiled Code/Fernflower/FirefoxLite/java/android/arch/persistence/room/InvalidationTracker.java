package android.arch.persistence.room;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArraySet;
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
   private long mMaxVersion = 0L;
   private InvalidationTracker.ObservedTableTracker mObservedTableTracker;
   final SafeIterableMap mObserverMap;
   AtomicBoolean mPendingRefresh;
   private Object[] mQueryArgs = new Object[1];
   Runnable mRefreshRunnable;
   ArrayMap mTableIdLookup;
   private String[] mTableNames;
   long[] mTableVersions;

   public InvalidationTracker(RoomDatabase var1, String... var2) {
      int var3 = 0;
      this.mPendingRefresh = new AtomicBoolean(false);
      this.mInitialized = false;
      this.mObserverMap = new SafeIterableMap();
      this.mRefreshRunnable = new Runnable() {
         private boolean checkUpdatedTable() {
            Cursor var1 = InvalidationTracker.this.mDatabase.query("SELECT * FROM room_table_modification_log WHERE version  > ? ORDER BY version ASC;", InvalidationTracker.this.mQueryArgs);
            boolean var2 = false;

            while(true) {
               boolean var8 = false;

               try {
                  var8 = true;
                  if (!var1.moveToNext()) {
                     var8 = false;
                     break;
                  }

                  long var3 = var1.getLong(0);
                  int var5 = var1.getInt(1);
                  InvalidationTracker.this.mTableVersions[var5] = var3;
                  InvalidationTracker.this.mMaxVersion = var3;
                  var8 = false;
               } finally {
                  if (var8) {
                     var1.close();
                  }
               }

               var2 = true;
            }

            var1.close();
            return var2;
         }

         public void run() {
            // $FF: Couldn't be decompiled
         }
      };
      this.mDatabase = var1;
      this.mObservedTableTracker = new InvalidationTracker.ObservedTableTracker(var2.length);
      this.mTableIdLookup = new ArrayMap();
      int var4 = var2.length;

      for(this.mTableNames = new String[var4]; var3 < var4; ++var3) {
         String var5 = var2[var3].toLowerCase(Locale.US);
         this.mTableIdLookup.put(var5, var3);
         this.mTableNames[var3] = var5;
      }

      this.mTableVersions = new long[var2.length];
      Arrays.fill(this.mTableVersions, 0L);
   }

   // $FF: synthetic method
   static boolean access$100(InvalidationTracker var0) {
      return var0.ensureInitialization();
   }

   // $FF: synthetic method
   static SupportSQLiteStatement access$200(InvalidationTracker var0) {
      return var0.mCleanupStatement;
   }

   // $FF: synthetic method
   static long access$400(InvalidationTracker var0) {
      return var0.mMaxVersion;
   }

   private static void appendTriggerName(StringBuilder var0, String var1, String var2) {
      var0.append("`");
      var0.append("room_table_modification_trigger_");
      var0.append(var1);
      var0.append("_");
      var0.append(var2);
      var0.append("`");
   }

   private boolean ensureInitialization() {
      if (!this.mDatabase.isOpen()) {
         return false;
      } else {
         if (!this.mInitialized) {
            this.mDatabase.getOpenHelper().getWritableDatabase();
         }

         if (!this.mInitialized) {
            Log.e("ROOM", "database is not initialized even though it is open");
            return false;
         } else {
            return true;
         }
      }
   }

   private void startTrackingTable(SupportSQLiteDatabase var1, int var2) {
      String var3 = this.mTableNames[var2];
      StringBuilder var4 = new StringBuilder();
      String[] var5 = TRIGGERS;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         var4.setLength(0);
         var4.append("CREATE TEMP TRIGGER IF NOT EXISTS ");
         appendTriggerName(var4, var3, var8);
         var4.append(" AFTER ");
         var4.append(var8);
         var4.append(" ON `");
         var4.append(var3);
         var4.append("` BEGIN INSERT OR REPLACE INTO ");
         var4.append("room_table_modification_log");
         var4.append(" VALUES(null, ");
         var4.append(var2);
         var4.append("); END");
         var1.execSQL(var4.toString());
      }

   }

   private void stopTrackingTable(SupportSQLiteDatabase var1, int var2) {
      String var3 = this.mTableNames[var2];
      StringBuilder var4 = new StringBuilder();
      String[] var5 = TRIGGERS;
      int var6 = var5.length;

      for(var2 = 0; var2 < var6; ++var2) {
         String var7 = var5[var2];
         var4.setLength(0);
         var4.append("DROP TRIGGER IF EXISTS ");
         appendTriggerName(var4, var3, var7);
         var1.execSQL(var4.toString());
      }

   }

   public void addObserver(InvalidationTracker.Observer param1) {
      // $FF: Couldn't be decompiled
   }

   public void addWeakObserver(InvalidationTracker.Observer var1) {
      this.addObserver(new InvalidationTracker.WeakObserver(this, var1));
   }

   void internalInit(SupportSQLiteDatabase var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label322: {
         try {
            if (this.mInitialized) {
               Log.e("ROOM", "Invalidation tracker is initialized twice :/.");
               return;
            }
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label322;
         }

         try {
            var1.beginTransaction();
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            break label322;
         }

         try {
            var1.execSQL("PRAGMA temp_store = MEMORY;");
            var1.execSQL("PRAGMA recursive_triggers='ON';");
            var1.execSQL("CREATE TEMP TABLE room_table_modification_log(version INTEGER PRIMARY KEY AUTOINCREMENT, table_id INTEGER)");
            var1.setTransactionSuccessful();
         } finally {
            try {
               var1.endTransaction();
            } catch (Throwable var38) {
               var10000 = var38;
               var10001 = false;
               break label322;
            }
         }

         this.syncTriggers(var1);
         this.mCleanupStatement = var1.compileStatement("DELETE FROM room_table_modification_log WHERE version NOT IN( SELECT MAX(version) FROM room_table_modification_log GROUP BY table_id)");
         this.mInitialized = true;
         return;
      }

      while(true) {
         Throwable var42 = var10000;

         try {
            throw var42;
         } catch (Throwable var37) {
            var10000 = var37;
            var10001 = false;
            continue;
         }
      }
   }

   public void refreshVersionsAsync() {
      if (this.mPendingRefresh.compareAndSet(false, true)) {
         ArchTaskExecutor.getInstance().executeOnDiskIO(this.mRefreshRunnable);
      }

   }

   public void removeObserver(InvalidationTracker.Observer param1) {
      // $FF: Couldn't be decompiled
   }

   void syncTriggers() {
      if (this.mDatabase.isOpen()) {
         this.syncTriggers(this.mDatabase.getOpenHelper().getWritableDatabase());
      }
   }

   void syncTriggers(SupportSQLiteDatabase var1) {
      if (!var1.inTransaction()) {
         IllegalStateException var10000;
         while(true) {
            Lock var2;
            boolean var10001;
            try {
               var2 = this.mDatabase.getCloseLock();
               var2.lock();
            } catch (SQLiteException | IllegalStateException var113) {
               var10000 = var113;
               var10001 = false;
               break;
            }

            Throwable var117;
            label877: {
               int[] var3;
               try {
                  var3 = this.mObservedTableTracker.getTablesToSync();
               } catch (Throwable var112) {
                  var117 = var112;
                  var10001 = false;
                  break label877;
               }

               if (var3 == null) {
                  try {
                     var2.unlock();
                     return;
                  } catch (SQLiteException | IllegalStateException var104) {
                     var10000 = var104;
                     var10001 = false;
                     break;
                  }
               }

               int var4;
               try {
                  var4 = var3.length;
               } catch (Throwable var111) {
                  var117 = var111;
                  var10001 = false;
                  break label877;
               }

               label878: {
                  label879: {
                     try {
                        var1.beginTransaction();
                     } catch (Throwable var110) {
                        var117 = var110;
                        var10001 = false;
                        break label879;
                     }

                     int var5 = 0;

                     label853:
                     while(true) {
                        if (var5 >= var4) {
                           try {
                              var1.setTransactionSuccessful();
                              break label878;
                           } catch (Throwable var107) {
                              var117 = var107;
                              var10001 = false;
                              break;
                           }
                        }

                        switch(var3[var5]) {
                        case 1:
                           try {
                              this.startTrackingTable(var1, var5);
                              break;
                           } catch (Throwable var109) {
                              var117 = var109;
                              var10001 = false;
                              break label853;
                           }
                        case 2:
                           try {
                              this.stopTrackingTable(var1, var5);
                           } catch (Throwable var108) {
                              var117 = var108;
                              var10001 = false;
                              break label853;
                           }
                        }

                        ++var5;
                     }
                  }

                  Throwable var116 = var117;

                  try {
                     var1.endTransaction();
                     throw var116;
                  } catch (Throwable var103) {
                     var117 = var103;
                     var10001 = false;
                     break label877;
                  }
               }

               try {
                  var1.endTransaction();
                  this.mObservedTableTracker.onSyncCompleted();
               } catch (Throwable var106) {
                  var117 = var106;
                  var10001 = false;
                  break label877;
               }

               try {
                  var2.unlock();
                  continue;
               } catch (SQLiteException | IllegalStateException var105) {
                  var10000 = var105;
                  var10001 = false;
                  break;
               }
            }

            Throwable var114 = var117;

            try {
               var2.unlock();
               throw var114;
            } catch (SQLiteException | IllegalStateException var102) {
               var10000 = var102;
               var10001 = false;
               break;
            }
         }

         IllegalStateException var115 = var10000;
         Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", var115);
      }
   }

   static class ObservedTableTracker {
      boolean mNeedsSync;
      boolean mPendingSync;
      final long[] mTableObservers;
      final int[] mTriggerStateChanges;
      final boolean[] mTriggerStates;

      ObservedTableTracker(int var1) {
         this.mTableObservers = new long[var1];
         this.mTriggerStates = new boolean[var1];
         this.mTriggerStateChanges = new int[var1];
         Arrays.fill(this.mTableObservers, 0L);
         Arrays.fill(this.mTriggerStates, false);
      }

      int[] getTablesToSync() {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label923: {
            label926: {
               try {
                  if (this.mNeedsSync && !this.mPendingSync) {
                     break label926;
                  }
               } catch (Throwable var95) {
                  var10000 = var95;
                  var10001 = false;
                  break label923;
               }

               try {
                  return null;
               } catch (Throwable var94) {
                  var10000 = var94;
                  var10001 = false;
                  break label923;
               }
            }

            int var1;
            try {
               var1 = this.mTableObservers.length;
            } catch (Throwable var93) {
               var10000 = var93;
               var10001 = false;
               break label923;
            }

            int var2 = 0;

            while(true) {
               byte var3 = 1;
               int[] var5;
               if (var2 >= var1) {
                  try {
                     this.mPendingSync = true;
                     this.mNeedsSync = false;
                     var5 = this.mTriggerStateChanges;
                     return var5;
                  } catch (Throwable var88) {
                     var10000 = var88;
                     var10001 = false;
                     break;
                  }
               }

               boolean var4;
               label891: {
                  label890: {
                     try {
                        if (this.mTableObservers[var2] > 0L) {
                           break label890;
                        }
                     } catch (Throwable var91) {
                        var10000 = var91;
                        var10001 = false;
                        break;
                     }

                     var4 = false;
                     break label891;
                  }

                  var4 = true;
               }

               label900: {
                  label925: {
                     try {
                        if (var4 == this.mTriggerStates[var2]) {
                           break label925;
                        }

                        var5 = this.mTriggerStateChanges;
                     } catch (Throwable var92) {
                        var10000 = var92;
                        var10001 = false;
                        break;
                     }

                     if (!var4) {
                        var3 = 2;
                     }

                     var5[var2] = var3;
                     break label900;
                  }

                  try {
                     this.mTriggerStateChanges[var2] = 0;
                  } catch (Throwable var90) {
                     var10000 = var90;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  this.mTriggerStates[var2] = var4;
               } catch (Throwable var89) {
                  var10000 = var89;
                  var10001 = false;
                  break;
               }

               ++var2;
            }
         }

         while(true) {
            Throwable var96 = var10000;

            try {
               throw var96;
            } catch (Throwable var87) {
               var10000 = var87;
               var10001 = false;
               continue;
            }
         }
      }

      boolean onAdded(int... var1) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label318: {
            int var2;
            try {
               var2 = var1.length;
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label318;
            }

            int var3 = 0;

            boolean var4;
            for(var4 = false; var3 < var2; ++var3) {
               int var5 = var1[var3];

               int var6;
               try {
                  var6 = this.mTableObservers[var5]++;
               } catch (Throwable var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label318;
               }

               if (var6 == 0L) {
                  try {
                     this.mNeedsSync = true;
                  } catch (Throwable var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label318;
                  }

                  var4 = true;
               }
            }

            label297:
            try {
               return var4;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label297;
            }
         }

         while(true) {
            Throwable var38 = var10000;

            try {
               throw var38;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               continue;
            }
         }
      }

      boolean onRemoved(int... var1) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label318: {
            int var2;
            try {
               var2 = var1.length;
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label318;
            }

            int var3 = 0;

            boolean var4;
            for(var4 = false; var3 < var2; ++var3) {
               int var5 = var1[var3];

               int var6;
               try {
                  var6 = this.mTableObservers[var5]--;
               } catch (Throwable var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label318;
               }

               if (var6 == 1L) {
                  try {
                     this.mNeedsSync = true;
                  } catch (Throwable var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label318;
                  }

                  var4 = true;
               }
            }

            label297:
            try {
               return var4;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label297;
            }
         }

         while(true) {
            Throwable var38 = var10000;

            try {
               throw var38;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               continue;
            }
         }
      }

      void onSyncCompleted() {
         // $FF: Couldn't be decompiled
      }
   }

   public abstract static class Observer {
      final String[] mTables;

      protected Observer(String var1, String... var2) {
         this.mTables = (String[])Arrays.copyOf(var2, var2.length + 1);
         this.mTables[var2.length] = var1;
      }

      public Observer(String[] var1) {
         this.mTables = (String[])Arrays.copyOf(var1, var1.length);
      }

      public abstract void onInvalidated(Set var1);
   }

   static class ObserverWrapper {
      final InvalidationTracker.Observer mObserver;
      private final Set mSingleTableSet;
      final int[] mTableIds;
      private final String[] mTableNames;
      private final long[] mVersions;

      ObserverWrapper(InvalidationTracker.Observer var1, int[] var2, String[] var3, long[] var4) {
         this.mObserver = var1;
         this.mTableIds = var2;
         this.mTableNames = var3;
         this.mVersions = var4;
         if (var2.length == 1) {
            ArraySet var5 = new ArraySet();
            var5.add(this.mTableNames[0]);
            this.mSingleTableSet = Collections.unmodifiableSet(var5);
         } else {
            this.mSingleTableSet = null;
         }

      }

      void checkForInvalidation(long[] var1) {
         int var2 = this.mTableIds.length;
         Object var3 = null;

         Object var7;
         for(int var4 = 0; var4 < var2; var3 = var7) {
            long var5 = var1[this.mTableIds[var4]];
            var7 = var3;
            if (this.mVersions[var4] < var5) {
               this.mVersions[var4] = var5;
               if (var2 == 1) {
                  var7 = this.mSingleTableSet;
               } else {
                  var7 = var3;
                  if (var3 == null) {
                     var7 = new ArraySet(var2);
                  }

                  ((Set)var7).add(this.mTableNames[var4]);
               }
            }

            ++var4;
         }

         if (var3 != null) {
            this.mObserver.onInvalidated((Set)var3);
         }

      }
   }

   static class WeakObserver extends InvalidationTracker.Observer {
      final WeakReference mDelegateRef;
      final InvalidationTracker mTracker;

      WeakObserver(InvalidationTracker var1, InvalidationTracker.Observer var2) {
         super(var2.mTables);
         this.mTracker = var1;
         this.mDelegateRef = new WeakReference(var2);
      }

      public void onInvalidated(Set var1) {
         InvalidationTracker.Observer var2 = (InvalidationTracker.Observer)this.mDelegateRef.get();
         if (var2 == null) {
            this.mTracker.removeObserver(this);
         } else {
            var2.onInvalidated(var1);
         }

      }
   }
}
