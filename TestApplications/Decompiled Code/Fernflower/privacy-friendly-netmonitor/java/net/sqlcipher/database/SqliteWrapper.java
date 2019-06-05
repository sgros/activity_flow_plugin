package net.sqlcipher.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import net.sqlcipher.Cursor;

public final class SqliteWrapper {
   private static final String SQLITE_EXCEPTION_DETAIL_MESSAGE = "unable to open database file";
   private static final String TAG = "SqliteWrapper";

   private SqliteWrapper() {
   }

   public static void checkSQLiteException(Context var0, SQLiteException var1) {
      if (isLowMemory(var1)) {
         Toast.makeText(var0, var1.getMessage(), 0).show();
      } else {
         throw var1;
      }
   }

   public static int delete(Context var0, ContentResolver var1, Uri var2, String var3, String[] var4) {
      try {
         int var5 = var1.delete(var2, var3, var4);
         return var5;
      } catch (SQLiteException var6) {
         Log.e("SqliteWrapper", "Catch a SQLiteException when delete: ", var6);
         checkSQLiteException(var0, var6);
         return -1;
      }
   }

   public static Uri insert(Context var0, ContentResolver var1, Uri var2, ContentValues var3) {
      try {
         Uri var5 = var1.insert(var2, var3);
         return var5;
      } catch (SQLiteException var4) {
         Log.e("SqliteWrapper", "Catch a SQLiteException when insert: ", var4);
         checkSQLiteException(var0, var4);
         return null;
      }
   }

   private static boolean isLowMemory(SQLiteException var0) {
      return var0.getMessage().equals("unable to open database file");
   }

   public static Cursor query(Context var0, ContentResolver var1, Uri var2, String[] var3, String var4, String[] var5, String var6) {
      try {
         Cursor var8 = (Cursor)var1.query(var2, var3, var4, var5, var6);
         return var8;
      } catch (SQLiteException var7) {
         Log.e("SqliteWrapper", "Catch a SQLiteException when query: ", var7);
         checkSQLiteException(var0, var7);
         return null;
      }
   }

   public static boolean requery(Context var0, android.database.Cursor var1) {
      try {
         boolean var2 = var1.requery();
         return var2;
      } catch (SQLiteException var3) {
         Log.e("SqliteWrapper", "Catch a SQLiteException when requery: ", var3);
         checkSQLiteException(var0, var3);
         return false;
      }
   }

   public static int update(Context var0, ContentResolver var1, Uri var2, ContentValues var3, String var4, String[] var5) {
      try {
         int var6 = var1.update(var2, var3, var4, var5);
         return var6;
      } catch (SQLiteException var7) {
         Log.e("SqliteWrapper", "Catch a SQLiteException when update: ", var7);
         checkSQLiteException(var0, var7);
         return -1;
      }
   }
}
