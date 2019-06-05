package android.arch.persistence.room;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.arch.core.executor.ArchTaskExecutor;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class RoomDatabase {
   private boolean mAllowMainThreadQueries;
   protected List mCallbacks;
   private final ReentrantLock mCloseLock = new ReentrantLock();
   protected volatile SupportSQLiteDatabase mDatabase;
   private final InvalidationTracker mInvalidationTracker = this.createInvalidationTracker();
   private SupportSQLiteOpenHelper mOpenHelper;
   boolean mWriteAheadLoggingEnabled;

   public void assertNotMainThread() {
      if (!this.mAllowMainThreadQueries) {
         if (ArchTaskExecutor.getInstance().isMainThread()) {
            throw new IllegalStateException("Cannot access database on the main thread since it may potentially lock the UI for a long period of time.");
         }
      }
   }

   public void beginTransaction() {
      this.assertNotMainThread();
      SupportSQLiteDatabase var1 = this.mOpenHelper.getWritableDatabase();
      this.mInvalidationTracker.syncTriggers(var1);
      var1.beginTransaction();
   }

   public SupportSQLiteStatement compileStatement(String var1) {
      this.assertNotMainThread();
      return this.mOpenHelper.getWritableDatabase().compileStatement(var1);
   }

   protected abstract InvalidationTracker createInvalidationTracker();

   protected abstract SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration var1);

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

   public void init(DatabaseConfiguration var1) {
      this.mOpenHelper = this.createOpenHelper(var1);
      int var2 = VERSION.SDK_INT;
      boolean var3 = false;
      boolean var4 = false;
      if (var2 >= 16) {
         var3 = var4;
         if (var1.journalMode == RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING) {
            var3 = true;
         }

         this.mOpenHelper.setWriteAheadLoggingEnabled(var3);
      }

      this.mCallbacks = var1.callbacks;
      this.mAllowMainThreadQueries = var1.allowMainThreadQueries;
      this.mWriteAheadLoggingEnabled = var3;
   }

   protected void internalInitInvalidationTracker(SupportSQLiteDatabase var1) {
      this.mInvalidationTracker.internalInit(var1);
   }

   public boolean isOpen() {
      SupportSQLiteDatabase var1 = this.mDatabase;
      boolean var2;
      if (var1 != null && var1.isOpen()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Cursor query(SupportSQLiteQuery var1) {
      this.assertNotMainThread();
      return this.mOpenHelper.getWritableDatabase().query(var1);
   }

   public Cursor query(String var1, Object[] var2) {
      return this.mOpenHelper.getWritableDatabase().query((SupportSQLiteQuery)(new SimpleSQLiteQuery(var1, var2)));
   }

   public void setTransactionSuccessful() {
      this.mOpenHelper.getWritableDatabase().setTransactionSuccessful();
   }

   public static class Builder {
      private boolean mAllowMainThreadQueries;
      private ArrayList mCallbacks;
      private final Context mContext;
      private final Class mDatabaseClass;
      private SupportSQLiteOpenHelper.Factory mFactory;
      private RoomDatabase.JournalMode mJournalMode;
      private final RoomDatabase.MigrationContainer mMigrationContainer;
      private Set mMigrationStartAndEndVersions;
      private Set mMigrationsNotRequiredFrom;
      private final String mName;
      private boolean mRequireMigration;

      Builder(Context var1, Class var2, String var3) {
         this.mContext = var1;
         this.mDatabaseClass = var2;
         this.mName = var3;
         this.mJournalMode = RoomDatabase.JournalMode.AUTOMATIC;
         this.mRequireMigration = true;
         this.mMigrationContainer = new RoomDatabase.MigrationContainer();
      }

      public RoomDatabase.Builder addCallback(RoomDatabase.Callback var1) {
         if (this.mCallbacks == null) {
            this.mCallbacks = new ArrayList();
         }

         this.mCallbacks.add(var1);
         return this;
      }

      public RoomDatabase.Builder addMigrations(Migration... var1) {
         if (this.mMigrationStartAndEndVersions == null) {
            this.mMigrationStartAndEndVersions = new HashSet();
         }

         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Migration var4 = var1[var3];
            this.mMigrationStartAndEndVersions.add(var4.startVersion);
            this.mMigrationStartAndEndVersions.add(var4.endVersion);
         }

         this.mMigrationContainer.addMigrations(var1);
         return this;
      }

      public RoomDatabase.Builder allowMainThreadQueries() {
         this.mAllowMainThreadQueries = true;
         return this;
      }

      public RoomDatabase build() {
         if (this.mContext != null) {
            if (this.mDatabaseClass == null) {
               throw new IllegalArgumentException("Must provide an abstract class that extends RoomDatabase");
            } else {
               if (this.mMigrationStartAndEndVersions != null && this.mMigrationsNotRequiredFrom != null) {
                  Iterator var1 = this.mMigrationStartAndEndVersions.iterator();

                  while(var1.hasNext()) {
                     Integer var2 = (Integer)var1.next();
                     if (this.mMigrationsNotRequiredFrom.contains(var2)) {
                        StringBuilder var3 = new StringBuilder();
                        var3.append("Inconsistency detected. A Migration was supplied to addMigration(Migration... migrations) that has a start or end version equal to a start version supplied to fallbackToDestructiveMigrationFrom(int... startVersions). Start version: ");
                        var3.append(var2);
                        throw new IllegalArgumentException(var3.toString());
                     }
                  }
               }

               if (this.mFactory == null) {
                  this.mFactory = new FrameworkSQLiteOpenHelperFactory();
               }

               DatabaseConfiguration var5 = new DatabaseConfiguration(this.mContext, this.mName, this.mFactory, this.mMigrationContainer, this.mCallbacks, this.mAllowMainThreadQueries, this.mJournalMode.resolve(this.mContext), this.mRequireMigration, this.mMigrationsNotRequiredFrom);
               RoomDatabase var4 = (RoomDatabase)Room.getGeneratedImplementation(this.mDatabaseClass, "_Impl");
               var4.init(var5);
               return var4;
            }
         } else {
            throw new IllegalArgumentException("Cannot provide null context for the database.");
         }
      }

      public RoomDatabase.Builder fallbackToDestructiveMigration() {
         this.mRequireMigration = false;
         return this;
      }
   }

   public abstract static class Callback {
      public void onCreate(SupportSQLiteDatabase var1) {
      }

      public void onOpen(SupportSQLiteDatabase var1) {
      }
   }

   public static enum JournalMode {
      AUTOMATIC,
      TRUNCATE,
      WRITE_AHEAD_LOGGING;

      @SuppressLint({"NewApi"})
      RoomDatabase.JournalMode resolve(Context var1) {
         if (this != AUTOMATIC) {
            return this;
         } else {
            if (VERSION.SDK_INT >= 16) {
               ActivityManager var2 = (ActivityManager)var1.getSystemService("activity");
               if (var2 != null && !ActivityManagerCompat.isLowRamDevice(var2)) {
                  return WRITE_AHEAD_LOGGING;
               }
            }

            return TRUNCATE;
         }
      }
   }

   public static class MigrationContainer {
      private SparseArrayCompat mMigrations = new SparseArrayCompat();

      private void addMigration(Migration var1) {
         int var2 = var1.startVersion;
         int var3 = var1.endVersion;
         SparseArrayCompat var4 = (SparseArrayCompat)this.mMigrations.get(var2);
         SparseArrayCompat var5 = var4;
         if (var4 == null) {
            var5 = new SparseArrayCompat();
            this.mMigrations.put(var2, var5);
         }

         Migration var6 = (Migration)var5.get(var3);
         if (var6 != null) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Overriding migration ");
            var7.append(var6);
            var7.append(" with ");
            var7.append(var1);
            Log.w("ROOM", var7.toString());
         }

         var5.append(var3, var1);
      }

      private List findUpMigrationPath(List var1, boolean var2, int var3, int var4) {
         byte var5;
         int var6;
         if (var2) {
            var5 = -1;
            var6 = var3;
         } else {
            var5 = 1;
            var6 = var3;
         }

         while(true) {
            if (var2) {
               if (var6 >= var4) {
                  break;
               }
            } else if (var6 <= var4) {
               break;
            }

            SparseArrayCompat var7 = (SparseArrayCompat)this.mMigrations.get(var6);
            if (var7 == null) {
               return null;
            }

            int var8 = var7.size();
            boolean var9 = false;
            if (var2) {
               var3 = var8 - 1;
               var8 = -1;
            } else {
               var3 = 0;
            }

            boolean var10;
            int var11;
            while(true) {
               var10 = var9;
               var11 = var6;
               if (var3 == var8) {
                  break;
               }

               label53: {
                  label52: {
                     var11 = var7.keyAt(var3);
                     if (var2) {
                        if (var11 <= var4 && var11 > var6) {
                           break label52;
                        }
                     } else if (var11 >= var4 && var11 < var6) {
                        break label52;
                     }

                     var10 = false;
                     break label53;
                  }

                  var10 = true;
               }

               if (var10) {
                  var1.add(var7.valueAt(var3));
                  var10 = true;
                  break;
               }

               var3 += var5;
            }

            var6 = var11;
            if (!var10) {
               return null;
            }
         }

         return var1;
      }

      public void addMigrations(Migration... var1) {
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            this.addMigration(var1[var3]);
         }

      }

      public List findMigrationPath(int var1, int var2) {
         if (var1 == var2) {
            return Collections.emptyList();
         } else {
            boolean var3;
            if (var2 > var1) {
               var3 = true;
            } else {
               var3 = false;
            }

            return this.findUpMigrationPath(new ArrayList(), var3, var1, var2);
         }
      }
   }
}
