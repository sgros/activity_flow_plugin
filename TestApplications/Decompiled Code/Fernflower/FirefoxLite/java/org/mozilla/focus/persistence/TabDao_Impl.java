package org.mozilla.focus.persistence;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class TabDao_Impl extends TabDao {
   private final RoomDatabase __db;
   private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTabEntity;
   private final EntityInsertionAdapter __insertionAdapterOfTabEntity;
   private final SharedSQLiteStatement __preparedStmtOfDeleteAllTabs;

   public TabDao_Impl(RoomDatabase var1) {
      this.__db = var1;
      this.__insertionAdapterOfTabEntity = new EntityInsertionAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, TabEntity var2) {
            if (var2.getId() == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.getId());
            }

            if (var2.getParentId() == null) {
               var1.bindNull(2);
            } else {
               var1.bindString(2, var2.getParentId());
            }

            if (var2.getTitle() == null) {
               var1.bindNull(3);
            } else {
               var1.bindString(3, var2.getTitle());
            }

            if (var2.getUrl() == null) {
               var1.bindNull(4);
            } else {
               var1.bindString(4, var2.getUrl());
            }

         }

         public String createQuery() {
            return "INSERT OR REPLACE INTO `tabs`(`tab_id`,`tab_parent_id`,`tab_title`,`tab_url`) VALUES (?,?,?,?)";
         }
      };
      this.__deletionAdapterOfTabEntity = new EntityDeletionOrUpdateAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, TabEntity var2) {
            if (var2.getId() == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.getId());
            }

         }

         public String createQuery() {
            return "DELETE FROM `tabs` WHERE `tab_id` = ?";
         }
      };
      this.__preparedStmtOfDeleteAllTabs = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "DELETE FROM tabs";
         }
      };
   }

   public void deleteAllTabs() {
      SupportSQLiteStatement var1 = this.__preparedStmtOfDeleteAllTabs.acquire();
      this.__db.beginTransaction();

      try {
         var1.executeUpdateDelete();
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
         this.__preparedStmtOfDeleteAllTabs.release(var1);
      }

   }

   public void deleteAllTabsAndInsertTabsInTransaction(TabEntity... var1) {
      this.__db.beginTransaction();

      try {
         super.deleteAllTabsAndInsertTabsInTransaction(var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }

   public List getTabs() {
      RoomSQLiteQuery var1 = RoomSQLiteQuery.acquire("SELECT * FROM tabs", 0);
      Cursor var2 = this.__db.query(var1);

      ArrayList var7;
      label78: {
         Throwable var10000;
         label77: {
            int var3;
            int var4;
            int var5;
            int var6;
            boolean var10001;
            try {
               var3 = var2.getColumnIndexOrThrow("tab_id");
               var4 = var2.getColumnIndexOrThrow("tab_parent_id");
               var5 = var2.getColumnIndexOrThrow("tab_title");
               var6 = var2.getColumnIndexOrThrow("tab_url");
               var7 = new ArrayList(var2.getCount());
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label77;
            }

            while(true) {
               try {
                  if (!var2.moveToNext()) {
                     break label78;
                  }

                  String var8 = var2.getString(var3);
                  String var9 = var2.getString(var4);
                  String var10 = var2.getString(var5);
                  String var19 = var2.getString(var6);
                  TabEntity var12 = new TabEntity(var8, var9, var10, var19);
                  var7.add(var12);
               } catch (Throwable var17) {
                  var10000 = var17;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var11 = var10000;
         var2.close();
         var1.release();
         throw var11;
      }

      var2.close();
      var1.release();
      return var7;
   }

   public void insertTabs(TabEntity... var1) {
      this.__db.beginTransaction();

      try {
         this.__insertionAdapterOfTabEntity.insert((Object[])var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }
}
