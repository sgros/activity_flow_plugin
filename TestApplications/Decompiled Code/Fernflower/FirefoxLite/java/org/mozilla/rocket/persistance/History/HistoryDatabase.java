package org.mozilla.rocket.persistance.History;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

public abstract class HistoryDatabase extends RoomDatabase {
   private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
      public void migrate(SupportSQLiteDatabase var1) {
         var1.beginTransaction();

         try {
            var1.execSQL("CREATE TABLE IF NOT EXISTS browsing_history_legacy (_id INTEGER PRIMARY KEY NOT NULL,url TEXT NOT NULL,fav_icon BLOB);");
            var1.execSQL("INSERT INTO browsing_history_legacy (_id, fav_icon, url) SELECT _id, fav_icon, url FROM browsing_history ORDER BY last_view_timestamp DESC LIMIT 50");
            var1.execSQL("INSERT OR REPLACE INTO browsing_history_legacy (_id, fav_icon, url) SELECT _id, fav_icon, url FROM browsing_history WHERE view_count > 6 ORDER BY view_count LIMIT 16");
            var1.execSQL("CREATE TABLE IF NOT EXISTS browsing_history_new (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,title TEXT,url TEXT NOT NULL,view_count INTEGER NOT NULL DEFAULT 1,last_view_timestamp INTEGER NOT NULL,fav_icon_uri TEXT);");
            var1.execSQL("INSERT INTO browsing_history_new (_id, title, url, view_count, last_view_timestamp) SELECT _id, title, url, view_count, last_view_timestamp FROM browsing_history");
            var1.execSQL("DROP TABLE browsing_history");
            var1.execSQL("ALTER TABLE browsing_history_new RENAME TO browsing_history");
            var1.setTransactionSuccessful();
         } finally {
            var1.endTransaction();
         }

      }
   };
   private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
      public void migrate(SupportSQLiteDatabase var1) {
         var1.execSQL("CREATE INDEX IF NOT EXISTS index_browsing_history_view_count ON browsing_history(view_count)");
      }
   };
   private static volatile HistoryDatabase instance;

   public static HistoryDatabase getInstance(Context var0) {
      if (instance == null) {
         synchronized(HistoryDatabase.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (instance == null) {
                  instance = (HistoryDatabase)Room.databaseBuilder(var0.getApplicationContext(), HistoryDatabase.class, "history.db").addMigrations(MIGRATION_1_2, MIGRATION_2_3).build();
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return instance;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var13 = var10000;

            try {
               throw var13;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               continue;
            }
         }
      } else {
         return instance;
      }
   }
}
