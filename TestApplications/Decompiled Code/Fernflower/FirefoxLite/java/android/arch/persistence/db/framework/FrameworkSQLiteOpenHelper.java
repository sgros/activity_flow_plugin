package android.arch.persistence.db.framework;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

class FrameworkSQLiteOpenHelper implements SupportSQLiteOpenHelper {
   private final FrameworkSQLiteOpenHelper.OpenHelper mDelegate;

   FrameworkSQLiteOpenHelper(Context var1, String var2, SupportSQLiteOpenHelper.Callback var3) {
      this.mDelegate = this.createDelegate(var1, var2, var3);
   }

   private FrameworkSQLiteOpenHelper.OpenHelper createDelegate(Context var1, String var2, SupportSQLiteOpenHelper.Callback var3) {
      return new FrameworkSQLiteOpenHelper.OpenHelper(var1, var2, new FrameworkSQLiteDatabase[1], var3);
   }

   public SupportSQLiteDatabase getReadableDatabase() {
      return this.mDelegate.getReadableSupportDatabase();
   }

   public SupportSQLiteDatabase getWritableDatabase() {
      return this.mDelegate.getWritableSupportDatabase();
   }

   public void setWriteAheadLoggingEnabled(boolean var1) {
      this.mDelegate.setWriteAheadLoggingEnabled(var1);
   }

   static class OpenHelper extends SQLiteOpenHelper {
      final SupportSQLiteOpenHelper.Callback mCallback;
      final FrameworkSQLiteDatabase[] mDbRef;
      private boolean mMigrated;

      OpenHelper(Context var1, String var2, final FrameworkSQLiteDatabase[] var3, final SupportSQLiteOpenHelper.Callback var4) {
         super(var1, var2, (CursorFactory)null, var4.version, new DatabaseErrorHandler() {
            public void onCorruption(SQLiteDatabase var1) {
               FrameworkSQLiteDatabase var2 = var3[0];
               if (var2 != null) {
                  var4.onCorruption(var2);
               }

            }
         });
         this.mCallback = var4;
         this.mDbRef = var3;
      }

      public void close() {
         synchronized(this){}

         try {
            super.close();
            this.mDbRef[0] = null;
         } finally {
            ;
         }

      }

      SupportSQLiteDatabase getReadableSupportDatabase() {
         synchronized(this){}

         SupportSQLiteDatabase var4;
         try {
            this.mMigrated = false;
            SQLiteDatabase var1 = super.getReadableDatabase();
            if (!this.mMigrated) {
               FrameworkSQLiteDatabase var5 = this.getWrappedDb(var1);
               return var5;
            }

            this.close();
            var4 = this.getReadableSupportDatabase();
         } finally {
            ;
         }

         return var4;
      }

      FrameworkSQLiteDatabase getWrappedDb(SQLiteDatabase var1) {
         if (this.mDbRef[0] == null) {
            FrameworkSQLiteDatabase var2 = new FrameworkSQLiteDatabase(var1);
            this.mDbRef[0] = var2;
         }

         return this.mDbRef[0];
      }

      SupportSQLiteDatabase getWritableSupportDatabase() {
         synchronized(this){}

         FrameworkSQLiteDatabase var4;
         try {
            this.mMigrated = false;
            SQLiteDatabase var1 = super.getWritableDatabase();
            if (this.mMigrated) {
               this.close();
               SupportSQLiteDatabase var5 = this.getWritableSupportDatabase();
               return var5;
            }

            var4 = this.getWrappedDb(var1);
         } finally {
            ;
         }

         return var4;
      }

      public void onConfigure(SQLiteDatabase var1) {
         this.mCallback.onConfigure(this.getWrappedDb(var1));
      }

      public void onCreate(SQLiteDatabase var1) {
         this.mCallback.onCreate(this.getWrappedDb(var1));
      }

      public void onDowngrade(SQLiteDatabase var1, int var2, int var3) {
         this.mMigrated = true;
         this.mCallback.onDowngrade(this.getWrappedDb(var1), var2, var3);
      }

      public void onOpen(SQLiteDatabase var1) {
         if (!this.mMigrated) {
            this.mCallback.onOpen(this.getWrappedDb(var1));
         }

      }

      public void onUpgrade(SQLiteDatabase var1, int var2, int var3) {
         this.mMigrated = true;
         this.mCallback.onUpgrade(this.getWrappedDb(var1), var2, var3);
      }
   }
}
