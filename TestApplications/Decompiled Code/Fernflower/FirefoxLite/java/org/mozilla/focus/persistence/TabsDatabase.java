package org.mozilla.focus.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

public abstract class TabsDatabase extends RoomDatabase {
   static final Migration MIGRATION_1_2 = new Migration(1, 2) {
      public void migrate(SupportSQLiteDatabase var1) {
         var1.execSQL("ALTER TABLE tabs RENAME TO tabs_old");
         var1.execSQL("CREATE TABLE IF NOT EXISTS tabs (tab_id TEXT NOT NULL, tab_parent_id TEXT, tab_title TEXT, tab_url TEXT, PRIMARY KEY(tab_id))");
         var1.execSQL("INSERT INTO tabs (tab_id, tab_parent_id, tab_title, tab_url) SELECT tab_id, tab_parent_id, tab_title, tab_url FROM tabs_old");
         var1.execSQL("DROP TABLE tabs_old");
      }
   };
   private static volatile TabsDatabase instance;

   public static TabsDatabase getInstance(Context var0) {
      if (instance == null) {
         synchronized(TabsDatabase.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (instance == null) {
                  instance = (TabsDatabase)Room.databaseBuilder(var0.getApplicationContext(), TabsDatabase.class, "tabs.db").addMigrations(MIGRATION_1_2).build();
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

   public abstract TabDao tabDao();
}
