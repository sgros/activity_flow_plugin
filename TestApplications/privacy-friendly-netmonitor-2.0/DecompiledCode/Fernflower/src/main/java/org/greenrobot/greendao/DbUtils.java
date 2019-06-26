package org.greenrobot.greendao;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.greenrobot.greendao.database.Database;

public class DbUtils {
   public static int copyAllBytes(InputStream var0, OutputStream var1) throws IOException {
      byte[] var2 = new byte[4096];
      int var3 = 0;

      while(true) {
         int var4 = var0.read(var2);
         if (var4 == -1) {
            return var3;
         }

         var1.write(var2, 0, var4);
         var3 += var4;
      }
   }

   public static int executeSqlScript(Context var0, Database var1, String var2) throws IOException {
      return executeSqlScript(var0, var1, var2, true);
   }

   public static int executeSqlScript(Context var0, Database var1, String var2, boolean var3) throws IOException {
      String[] var5 = (new String(readAsset(var0, var2), "UTF-8")).split(";(\\s)*[\n\r]");
      int var4;
      if (var3) {
         var4 = executeSqlStatementsInTx(var1, var5);
      } else {
         var4 = executeSqlStatements(var1, var5);
      }

      StringBuilder var6 = new StringBuilder();
      var6.append("Executed ");
      var6.append(var4);
      var6.append(" statements from SQL script '");
      var6.append(var2);
      var6.append("'");
      DaoLog.i(var6.toString());
      return var4;
   }

   public static int executeSqlStatements(Database var0, String[] var1) {
      int var2 = 0;
      int var3 = var1.length;

      int var4;
      int var6;
      for(var4 = 0; var2 < var3; var4 = var6) {
         String var5 = var1[var2].trim();
         var6 = var4;
         if (var5.length() > 0) {
            var0.execSQL(var5);
            var6 = var4 + 1;
         }

         ++var2;
      }

      return var4;
   }

   public static int executeSqlStatementsInTx(Database var0, String[] var1) {
      var0.beginTransaction();

      int var2;
      try {
         var2 = executeSqlStatements(var0, var1);
         var0.setTransactionSuccessful();
      } finally {
         var0.endTransaction();
      }

      return var2;
   }

   public static void logTableDump(SQLiteDatabase var0, String var1) {
      Cursor var4 = var0.query(var1, (String[])null, (String)null, (String[])null, (String)null, (String)null, (String)null);

      try {
         DaoLog.d(DatabaseUtils.dumpCursorToString(var4));
      } finally {
         var4.close();
      }

   }

   public static byte[] readAllBytes(InputStream var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      copyAllBytes(var0, var1);
      return var1.toByteArray();
   }

   public static byte[] readAsset(Context var0, String var1) throws IOException {
      InputStream var4 = var0.getResources().getAssets().open(var1);

      byte[] var5;
      try {
         var5 = readAllBytes(var4);
      } finally {
         var4.close();
      }

      return var5;
   }

   public static void vacuum(Database var0) {
      var0.execSQL("VACUUM");
   }
}
