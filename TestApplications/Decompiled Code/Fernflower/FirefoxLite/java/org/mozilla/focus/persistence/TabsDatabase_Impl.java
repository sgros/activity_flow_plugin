package org.mozilla.focus.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.util.TableInfo;
import java.util.HashMap;
import java.util.HashSet;

public class TabsDatabase_Impl extends TabsDatabase {
   private volatile TabDao _tabDao;

   protected InvalidationTracker createInvalidationTracker() {
      return new InvalidationTracker(this, new String[]{"tabs"});
   }

   protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration var1) {
      RoomOpenHelper var2 = new RoomOpenHelper(var1, new RoomOpenHelper.Delegate(2) {
         public void createAllTables(SupportSQLiteDatabase var1) {
            var1.execSQL("CREATE TABLE IF NOT EXISTS `tabs` (`tab_id` TEXT NOT NULL, `tab_parent_id` TEXT, `tab_title` TEXT, `tab_url` TEXT, PRIMARY KEY(`tab_id`))");
            var1.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            var1.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"e4cc9f6316824138aa0032ce908db7d6\")");
         }

         public void dropAllTables(SupportSQLiteDatabase var1) {
            var1.execSQL("DROP TABLE IF EXISTS `tabs`");
         }

         protected void onCreate(SupportSQLiteDatabase var1) {
            if (TabsDatabase_Impl.this.mCallbacks != null) {
               int var2 = 0;

               for(int var3 = TabsDatabase_Impl.this.mCallbacks.size(); var2 < var3; ++var2) {
                  ((RoomDatabase.Callback)TabsDatabase_Impl.this.mCallbacks.get(var2)).onCreate(var1);
               }
            }

         }

         public void onOpen(SupportSQLiteDatabase var1) {
            TabsDatabase_Impl.this.mDatabase = var1;
            TabsDatabase_Impl.this.internalInitInvalidationTracker(var1);
            if (TabsDatabase_Impl.this.mCallbacks != null) {
               int var2 = 0;

               for(int var3 = TabsDatabase_Impl.this.mCallbacks.size(); var2 < var3; ++var2) {
                  ((RoomDatabase.Callback)TabsDatabase_Impl.this.mCallbacks.get(var2)).onOpen(var1);
               }
            }

         }

         protected void validateMigration(SupportSQLiteDatabase var1) {
            HashMap var2 = new HashMap(4);
            var2.put("tab_id", new TableInfo.Column("tab_id", "TEXT", true, 1));
            var2.put("tab_parent_id", new TableInfo.Column("tab_parent_id", "TEXT", false, 0));
            var2.put("tab_title", new TableInfo.Column("tab_title", "TEXT", false, 0));
            var2.put("tab_url", new TableInfo.Column("tab_url", "TEXT", false, 0));
            TableInfo var5 = new TableInfo("tabs", var2, new HashSet(0), new HashSet(0));
            TableInfo var3 = TableInfo.read(var1, "tabs");
            if (!var5.equals(var3)) {
               StringBuilder var4 = new StringBuilder();
               var4.append("Migration didn't properly handle tabs(org.mozilla.focus.persistence.TabEntity).\n Expected:\n");
               var4.append(var5);
               var4.append("\n Found:\n");
               var4.append(var3);
               throw new IllegalStateException(var4.toString());
            }
         }
      }, "e4cc9f6316824138aa0032ce908db7d6", "34f484a157ca50a1d8f3c2529bd492dc");
      SupportSQLiteOpenHelper.Configuration var3 = SupportSQLiteOpenHelper.Configuration.builder(var1.context).name(var1.name).callback(var2).build();
      return var1.sqliteOpenHelperFactory.create(var3);
   }

   public TabDao tabDao() {
      if (this._tabDao != null) {
         return this._tabDao;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               if (this._tabDao == null) {
                  TabDao_Impl var1 = new TabDao_Impl(this);
                  this._tabDao = var1;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label136;
            }

            label133:
            try {
               TabDao var15 = this._tabDao;
               return var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label133;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
