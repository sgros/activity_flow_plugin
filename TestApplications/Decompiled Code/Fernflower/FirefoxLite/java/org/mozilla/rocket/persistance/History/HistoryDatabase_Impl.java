package org.mozilla.rocket.persistance.History;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.util.TableInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class HistoryDatabase_Impl extends HistoryDatabase {
   protected InvalidationTracker createInvalidationTracker() {
      return new InvalidationTracker(this, new String[]{"browsing_history"});
   }

   protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration var1) {
      RoomOpenHelper var2 = new RoomOpenHelper(var1, new RoomOpenHelper.Delegate(3) {
         public void createAllTables(SupportSQLiteDatabase var1) {
            var1.execSQL("CREATE TABLE IF NOT EXISTS `browsing_history` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `url` TEXT NOT NULL, `view_count` INTEGER NOT NULL, `last_view_timestamp` INTEGER NOT NULL, `fav_icon_uri` TEXT)");
            var1.execSQL("CREATE  INDEX `index_browsing_history_view_count` ON `browsing_history` (`view_count`)");
            var1.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            var1.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"ed1350002e11ab61b6b9f2aab52e8ce4\")");
         }

         public void dropAllTables(SupportSQLiteDatabase var1) {
            var1.execSQL("DROP TABLE IF EXISTS `browsing_history`");
         }

         protected void onCreate(SupportSQLiteDatabase var1) {
            if (HistoryDatabase_Impl.this.mCallbacks != null) {
               int var2 = 0;

               for(int var3 = HistoryDatabase_Impl.this.mCallbacks.size(); var2 < var3; ++var2) {
                  ((RoomDatabase.Callback)HistoryDatabase_Impl.this.mCallbacks.get(var2)).onCreate(var1);
               }
            }

         }

         public void onOpen(SupportSQLiteDatabase var1) {
            HistoryDatabase_Impl.this.mDatabase = var1;
            HistoryDatabase_Impl.this.internalInitInvalidationTracker(var1);
            if (HistoryDatabase_Impl.this.mCallbacks != null) {
               int var2 = 0;

               for(int var3 = HistoryDatabase_Impl.this.mCallbacks.size(); var2 < var3; ++var2) {
                  ((RoomDatabase.Callback)HistoryDatabase_Impl.this.mCallbacks.get(var2)).onOpen(var1);
               }
            }

         }

         protected void validateMigration(SupportSQLiteDatabase var1) {
            HashMap var2 = new HashMap(6);
            var2.put("_id", new TableInfo.Column("_id", "INTEGER", true, 1));
            var2.put("title", new TableInfo.Column("title", "TEXT", false, 0));
            var2.put("url", new TableInfo.Column("url", "TEXT", true, 0));
            var2.put("view_count", new TableInfo.Column("view_count", "INTEGER", true, 0));
            var2.put("last_view_timestamp", new TableInfo.Column("last_view_timestamp", "INTEGER", true, 0));
            var2.put("fav_icon_uri", new TableInfo.Column("fav_icon_uri", "TEXT", false, 0));
            HashSet var3 = new HashSet(0);
            HashSet var4 = new HashSet(1);
            var4.add(new TableInfo.Index("index_browsing_history_view_count", false, Arrays.asList("view_count")));
            TableInfo var7 = new TableInfo("browsing_history", var2, var3, var4);
            TableInfo var5 = TableInfo.read(var1, "browsing_history");
            if (!var7.equals(var5)) {
               StringBuilder var6 = new StringBuilder();
               var6.append("Migration didn't properly handle browsing_history(org.mozilla.focus.history.model.Site).\n Expected:\n");
               var6.append(var7);
               var6.append("\n Found:\n");
               var6.append(var5);
               throw new IllegalStateException(var6.toString());
            }
         }
      }, "ed1350002e11ab61b6b9f2aab52e8ce4", "187c585e7ba70c63c7fcbeb3f99508f2");
      SupportSQLiteOpenHelper.Configuration var3 = SupportSQLiteOpenHelper.Configuration.builder(var1.context).name(var1.name).callback(var2).build();
      return var1.sqliteOpenHelperFactory.create(var3);
   }
}
