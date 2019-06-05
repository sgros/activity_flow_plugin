package androidx.work.impl.model;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;

public class SystemIdInfoDao_Impl implements SystemIdInfoDao {
   private final RoomDatabase __db;
   private final EntityInsertionAdapter __insertionAdapterOfSystemIdInfo;
   private final SharedSQLiteStatement __preparedStmtOfRemoveSystemIdInfo;

   public SystemIdInfoDao_Impl(RoomDatabase var1) {
      this.__db = var1;
      this.__insertionAdapterOfSystemIdInfo = new EntityInsertionAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, SystemIdInfo var2) {
            if (var2.workSpecId == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.workSpecId);
            }

            var1.bindLong(2, (long)var2.systemId);
         }

         public String createQuery() {
            return "INSERT OR REPLACE INTO `SystemIdInfo`(`work_spec_id`,`system_id`) VALUES (?,?)";
         }
      };
      this.__preparedStmtOfRemoveSystemIdInfo = new SharedSQLiteStatement(var1) {
         public String createQuery() {
            return "DELETE FROM SystemIdInfo where work_spec_id=?";
         }
      };
   }

   public SystemIdInfo getSystemIdInfo(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT * FROM SystemIdInfo WHERE work_spec_id=?", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      Cursor var3 = this.__db.query(var2);
      boolean var7 = false;

      SystemIdInfo var9;
      label47: {
         try {
            var7 = true;
            int var4 = var3.getColumnIndexOrThrow("work_spec_id");
            int var5 = var3.getColumnIndexOrThrow("system_id");
            if (var3.moveToFirst()) {
               var9 = new SystemIdInfo(var3.getString(var4), var3.getInt(var5));
               var7 = false;
               break label47;
            }

            var7 = false;
         } finally {
            if (var7) {
               var3.close();
               var2.release();
            }
         }

         var9 = null;
      }

      var3.close();
      var2.release();
      return var9;
   }

   public void insertSystemIdInfo(SystemIdInfo var1) {
      this.__db.beginTransaction();

      try {
         this.__insertionAdapterOfSystemIdInfo.insert((Object)var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }

   public void removeSystemIdInfo(String var1) {
      SupportSQLiteStatement var2;
      label100: {
         Throwable var10000;
         label99: {
            var2 = this.__preparedStmtOfRemoveSystemIdInfo.acquire();
            this.__db.beginTransaction();
            boolean var10001;
            if (var1 == null) {
               try {
                  var2.bindNull(1);
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label99;
               }
            } else {
               try {
                  var2.bindString(1, var1);
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label99;
               }
            }

            label93:
            try {
               var2.executeUpdateDelete();
               this.__db.setTransactionSuccessful();
               break label100;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label93;
            }
         }

         Throwable var15 = var10000;
         this.__db.endTransaction();
         this.__preparedStmtOfRemoveSystemIdInfo.release(var2);
         throw var15;
      }

      this.__db.endTransaction();
      this.__preparedStmtOfRemoveSystemIdInfo.release(var2);
   }
}
