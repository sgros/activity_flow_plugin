package org.mozilla.focus.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DownloadInfoDbHelper {
   private static DownloadInfoDbHelper sInstance;
   private final DownloadInfoDbHelper.OpenHelper mOpenHelper;

   private DownloadInfoDbHelper(Context var1) {
      this.mOpenHelper = new DownloadInfoDbHelper.OpenHelper(var1, "DownloadInfo.db", (CursorFactory)null, this.getDatabaseVersion());
   }

   private int getDatabaseVersion() {
      return 2;
   }

   public static DownloadInfoDbHelper getsInstance(Context var0) {
      synchronized(DownloadInfoDbHelper.class){}

      DownloadInfoDbHelper var4;
      try {
         if (sInstance == null) {
            DownloadInfoDbHelper var1 = new DownloadInfoDbHelper(var0);
            sInstance = var1;
         }

         var4 = sInstance;
      } finally {
         ;
      }

      return var4;
   }

   public SQLiteDatabase getReadableDB() {
      return this.mOpenHelper.getReadableDatabase();
   }

   public SQLiteDatabase getWritableDB() {
      return this.mOpenHelper.getWritableDatabase();
   }

   private static final class OpenHelper extends SQLiteOpenHelper {
      public OpenHelper(Context var1, String var2, CursorFactory var3, int var4) {
         super(var1, var2, var3, var4);
      }

      public void onCreate(SQLiteDatabase var1) {
         var1.execSQL("CREATE TABLE download_info(_id INTEGER PRIMARY KEY AUTOINCREMENT,download_id INTEGER,file_path TEXT,status INTEGER,is_read INTEGER DEFAULT 0)");
      }

      public void onUpgrade(SQLiteDatabase var1, int var2, int var3) {
         if (var2 < 2) {
            var1.execSQL("ALTER TABLE download_info ADD status INTEGER;");
            StringBuilder var4 = new StringBuilder();
            var4.append("UPDATE download_info SET status = ");
            var4.append(String.valueOf(8));
            var4.append(";");
            var1.execSQL(var4.toString());
            var1.execSQL("ALTER TABLE download_info ADD is_read INTEGER DEFAULT 0;");
            var1.execSQL("UPDATE download_info SET is_read = 1;");
         }

      }
   }
}
