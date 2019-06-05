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

public class BookmarksDatabase_Impl extends BookmarksDatabase {
   private volatile BookmarkDao _bookmarkDao;

   public BookmarkDao bookmarkDao() {
      if (this._bookmarkDao != null) {
         return this._bookmarkDao;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               if (this._bookmarkDao == null) {
                  BookmarkDao_Impl var1 = new BookmarkDao_Impl(this);
                  this._bookmarkDao = var1;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label136;
            }

            label133:
            try {
               BookmarkDao var15 = this._bookmarkDao;
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

   protected InvalidationTracker createInvalidationTracker() {
      return new InvalidationTracker(this, new String[]{"bookmarks"});
   }

   protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration var1) {
      RoomOpenHelper var2 = new RoomOpenHelper(var1, new RoomOpenHelper.Delegate(1) {
         public void createAllTables(SupportSQLiteDatabase var1) {
            var1.execSQL("CREATE TABLE IF NOT EXISTS `bookmarks` (`id` TEXT NOT NULL, `title` TEXT, `url` TEXT, PRIMARY KEY(`id`))");
            var1.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            var1.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"d9d8ebc3eb829f5e9f3e096acd2ce08a\")");
         }

         public void dropAllTables(SupportSQLiteDatabase var1) {
            var1.execSQL("DROP TABLE IF EXISTS `bookmarks`");
         }

         protected void onCreate(SupportSQLiteDatabase var1) {
            if (BookmarksDatabase_Impl.this.mCallbacks != null) {
               int var2 = 0;

               for(int var3 = BookmarksDatabase_Impl.this.mCallbacks.size(); var2 < var3; ++var2) {
                  ((RoomDatabase.Callback)BookmarksDatabase_Impl.this.mCallbacks.get(var2)).onCreate(var1);
               }
            }

         }

         public void onOpen(SupportSQLiteDatabase var1) {
            BookmarksDatabase_Impl.this.mDatabase = var1;
            BookmarksDatabase_Impl.this.internalInitInvalidationTracker(var1);
            if (BookmarksDatabase_Impl.this.mCallbacks != null) {
               int var2 = 0;

               for(int var3 = BookmarksDatabase_Impl.this.mCallbacks.size(); var2 < var3; ++var2) {
                  ((RoomDatabase.Callback)BookmarksDatabase_Impl.this.mCallbacks.get(var2)).onOpen(var1);
               }
            }

         }

         protected void validateMigration(SupportSQLiteDatabase var1) {
            HashMap var2 = new HashMap(3);
            var2.put("id", new TableInfo.Column("id", "TEXT", true, 1));
            var2.put("title", new TableInfo.Column("title", "TEXT", false, 0));
            var2.put("url", new TableInfo.Column("url", "TEXT", false, 0));
            TableInfo var5 = new TableInfo("bookmarks", var2, new HashSet(0), new HashSet(0));
            TableInfo var4 = TableInfo.read(var1, "bookmarks");
            if (!var5.equals(var4)) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Migration didn't properly handle bookmarks(org.mozilla.focus.persistence.BookmarkModel).\n Expected:\n");
               var3.append(var5);
               var3.append("\n Found:\n");
               var3.append(var4);
               throw new IllegalStateException(var3.toString());
            }
         }
      }, "d9d8ebc3eb829f5e9f3e096acd2ce08a", "30da370378aadd165e18a3e0c60327a5");
      SupportSQLiteOpenHelper.Configuration var3 = SupportSQLiteOpenHelper.Configuration.builder(var1.context).name(var1.name).callback(var2).build();
      return var1.sqliteOpenHelperFactory.create(var3);
   }
}
