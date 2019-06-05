package org.mozilla.focus.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ScreenshotDatabaseHelper {
   private static ScreenshotDatabaseHelper sInstacne;
   private final ScreenshotDatabaseHelper.OpenHelper mOpenHelper;

   private ScreenshotDatabaseHelper(Context var1) {
      this.mOpenHelper = new ScreenshotDatabaseHelper.OpenHelper(var1, "screenshot.db", (CursorFactory)null, 1);
   }

   public static ScreenshotDatabaseHelper getsInstacne(Context var0) {
      synchronized(ScreenshotDatabaseHelper.class){}

      ScreenshotDatabaseHelper var4;
      try {
         if (sInstacne == null) {
            ScreenshotDatabaseHelper var1 = new ScreenshotDatabaseHelper(var0);
            sInstacne = var1;
         }

         var4 = sInstacne;
      } finally {
         ;
      }

      return var4;
   }

   public SQLiteDatabase getReadableDatabase() {
      return this.mOpenHelper.getReadableDatabase();
   }

   public SQLiteDatabase getWritableDatabase() {
      return this.mOpenHelper.getWritableDatabase();
   }

   private static final class OpenHelper extends SQLiteOpenHelper {
      public OpenHelper(Context var1, String var2, CursorFactory var3, int var4) {
         super(var1, var2, var3, var4);
      }

      public void onCreate(SQLiteDatabase var1) {
         var1.execSQL("DROP TABLE IF EXISTS screenshot");
         var1.execSQL("CREATE TABLE IF NOT EXISTS screenshot (_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,url TEXT NOT NULL,timestamp INTEGER NOT NULL,image_uri TEXT NOT NULL);");
         var1.execSQL("DROP TRIGGER IF EXISTS screenshot_inserted;");
         var1.execSQL("CREATE TRIGGER IF NOT EXISTS screenshot_inserted    AFTER INSERT ON screenshot WHEN (SELECT count() FROM screenshot) > 2000 BEGIN    DELETE FROM screenshot     WHERE _id = (SELECT _id FROM screenshot ORDER BY timestamp LIMIT 1); END");
      }

      public void onUpgrade(SQLiteDatabase var1, int var2, int var3) {
      }
   }
}
