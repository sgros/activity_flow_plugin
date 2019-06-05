package android.arch.persistence.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;

public interface SupportSQLiteOpenHelper {
   SupportSQLiteDatabase getReadableDatabase();

   SupportSQLiteDatabase getWritableDatabase();

   void setWriteAheadLoggingEnabled(boolean var1);

   public abstract static class Callback {
      public final int version;

      public Callback(int var1) {
         this.version = var1;
      }

      private void deleteDatabaseFile(String var1) {
         if (!var1.equalsIgnoreCase(":memory:") && var1.trim().length() != 0) {
            StringBuilder var2 = new StringBuilder();
            var2.append("deleting the database file: ");
            var2.append(var1);
            Log.w("SupportSQLite", var2.toString());

            Exception var10000;
            Exception var6;
            label38: {
               boolean var10001;
               File var7;
               try {
                  if (VERSION.SDK_INT >= 16) {
                     var7 = new File(var1);
                     SQLiteDatabase.deleteDatabase(var7);
                     return;
                  }
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label38;
               }

               try {
                  var7 = new File(var1);
                  if (!var7.delete()) {
                     var2 = new StringBuilder();
                     var2.append("Could not delete the database file ");
                     var2.append(var1);
                     Log.e("SupportSQLite", var2.toString());
                  }

                  return;
               } catch (Exception var4) {
                  var6 = var4;

                  try {
                     Log.e("SupportSQLite", "error while deleting corrupted database file", var6);
                     return;
                  } catch (Exception var3) {
                     var10000 = var3;
                     var10001 = false;
                  }
               }
            }

            var6 = var10000;
            Log.w("SupportSQLite", "delete failed: ", var6);
         }
      }

      public void onConfigure(SupportSQLiteDatabase var1) {
      }

      public void onCorruption(SupportSQLiteDatabase param1) {
         // $FF: Couldn't be decompiled
      }

      public abstract void onCreate(SupportSQLiteDatabase var1);

      public void onDowngrade(SupportSQLiteDatabase var1, int var2, int var3) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Can't downgrade database from version ");
         var4.append(var2);
         var4.append(" to ");
         var4.append(var3);
         throw new SQLiteException(var4.toString());
      }

      public void onOpen(SupportSQLiteDatabase var1) {
      }

      public abstract void onUpgrade(SupportSQLiteDatabase var1, int var2, int var3);
   }

   public static class Configuration {
      public final SupportSQLiteOpenHelper.Callback callback;
      public final Context context;
      public final String name;

      Configuration(Context var1, String var2, SupportSQLiteOpenHelper.Callback var3) {
         this.context = var1;
         this.name = var2;
         this.callback = var3;
      }

      public static SupportSQLiteOpenHelper.Configuration.Builder builder(Context var0) {
         return new SupportSQLiteOpenHelper.Configuration.Builder(var0);
      }

      public static class Builder {
         SupportSQLiteOpenHelper.Callback mCallback;
         Context mContext;
         String mName;

         Builder(Context var1) {
            this.mContext = var1;
         }

         public SupportSQLiteOpenHelper.Configuration build() {
            if (this.mCallback != null) {
               if (this.mContext != null) {
                  return new SupportSQLiteOpenHelper.Configuration(this.mContext, this.mName, this.mCallback);
               } else {
                  throw new IllegalArgumentException("Must set a non-null context to create the configuration.");
               }
            } else {
               throw new IllegalArgumentException("Must set a callback to create the configuration.");
            }
         }

         public SupportSQLiteOpenHelper.Configuration.Builder callback(SupportSQLiteOpenHelper.Callback var1) {
            this.mCallback = var1;
            return this;
         }

         public SupportSQLiteOpenHelper.Configuration.Builder name(String var1) {
            this.mName = var1;
            return this;
         }
      }
   }

   public interface Factory {
      SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration var1);
   }
}
