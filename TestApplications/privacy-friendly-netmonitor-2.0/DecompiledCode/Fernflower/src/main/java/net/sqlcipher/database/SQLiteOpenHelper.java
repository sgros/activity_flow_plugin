package net.sqlcipher.database;

import android.content.Context;
import net.sqlcipher.DatabaseErrorHandler;
import net.sqlcipher.DefaultDatabaseErrorHandler;

public abstract class SQLiteOpenHelper {
   private static final String TAG = "SQLiteOpenHelper";
   private final Context mContext;
   private SQLiteDatabase mDatabase;
   private final DatabaseErrorHandler mErrorHandler;
   private final SQLiteDatabase.CursorFactory mFactory;
   private final SQLiteDatabaseHook mHook;
   private boolean mIsInitializing;
   private final String mName;
   private final int mNewVersion;

   public SQLiteOpenHelper(Context var1, String var2, SQLiteDatabase.CursorFactory var3, int var4) {
      this(var1, var2, var3, var4, (SQLiteDatabaseHook)null, new DefaultDatabaseErrorHandler());
   }

   public SQLiteOpenHelper(Context var1, String var2, SQLiteDatabase.CursorFactory var3, int var4, SQLiteDatabaseHook var5) {
      this(var1, var2, var3, var4, var5, new DefaultDatabaseErrorHandler());
   }

   public SQLiteOpenHelper(Context var1, String var2, SQLiteDatabase.CursorFactory var3, int var4, SQLiteDatabaseHook var5, DatabaseErrorHandler var6) {
      this.mDatabase = null;
      this.mIsInitializing = false;
      if (var4 < 1) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Version must be >= 1, was ");
         var7.append(var4);
         throw new IllegalArgumentException(var7.toString());
      } else if (var6 == null) {
         throw new IllegalArgumentException("DatabaseErrorHandler param value can't be null.");
      } else {
         this.mContext = var1;
         this.mName = var2;
         this.mFactory = var3;
         this.mNewVersion = var4;
         this.mHook = var5;
         this.mErrorHandler = var6;
      }
   }

   public void close() {
      synchronized(this){}

      try {
         if (this.mIsInitializing) {
            IllegalStateException var1 = new IllegalStateException("Closed during initialization");
            throw var1;
         }

         if (this.mDatabase != null && this.mDatabase.isOpen()) {
            this.mDatabase.close();
            this.mDatabase = null;
         }
      } finally {
         ;
      }

   }

   public SQLiteDatabase getReadableDatabase(String var1) {
      Throwable var10000;
      label61: {
         synchronized(this){}
         boolean var10001;
         char[] var8;
         if (var1 == null) {
            var8 = null;
         } else {
            try {
               var8 = var1.toCharArray();
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label61;
            }
         }

         label57:
         try {
            SQLiteDatabase var10 = this.getReadableDatabase(var8);
            return var10;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label57;
         }
      }

      Throwable var9 = var10000;
      throw var9;
   }

   public SQLiteDatabase getReadableDatabase(char[] param1) {
      // $FF: Couldn't be decompiled
   }

   public SQLiteDatabase getWritableDatabase(String var1) {
      Throwable var10000;
      label61: {
         synchronized(this){}
         boolean var10001;
         char[] var8;
         if (var1 == null) {
            var8 = null;
         } else {
            try {
               var8 = var1.toCharArray();
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label61;
            }
         }

         label57:
         try {
            SQLiteDatabase var10 = this.getWritableDatabase(var8);
            return var10;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label57;
         }
      }

      Throwable var9 = var10000;
      throw var9;
   }

   public SQLiteDatabase getWritableDatabase(char[] param1) {
      // $FF: Couldn't be decompiled
   }

   public abstract void onCreate(SQLiteDatabase var1);

   public void onOpen(SQLiteDatabase var1) {
   }

   public abstract void onUpgrade(SQLiteDatabase var1, int var2, int var3);
}
