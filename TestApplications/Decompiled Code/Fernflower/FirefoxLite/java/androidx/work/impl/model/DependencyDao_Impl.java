package androidx.work.impl.model;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class DependencyDao_Impl implements DependencyDao {
   private final RoomDatabase __db;
   private final EntityInsertionAdapter __insertionAdapterOfDependency;

   public DependencyDao_Impl(RoomDatabase var1) {
      this.__db = var1;
      this.__insertionAdapterOfDependency = new EntityInsertionAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, Dependency var2) {
            if (var2.workSpecId == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.workSpecId);
            }

            if (var2.prerequisiteId == null) {
               var1.bindNull(2);
            } else {
               var1.bindString(2, var2.prerequisiteId);
            }

         }

         public String createQuery() {
            return "INSERT OR IGNORE INTO `Dependency`(`work_spec_id`,`prerequisite_id`) VALUES (?,?)";
         }
      };
   }

   public List getDependentWorkIds(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT work_spec_id FROM dependency WHERE prerequisite_id=?", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      Cursor var10 = this.__db.query(var2);

      ArrayList var3;
      label93: {
         Throwable var10000;
         label92: {
            boolean var10001;
            try {
               var3 = new ArrayList(var10.getCount());
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label92;
            }

            while(true) {
               try {
                  if (!var10.moveToNext()) {
                     break label93;
                  }

                  var3.add(var10.getString(0));
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var11 = var10000;
         var10.close();
         var2.release();
         throw var11;
      }

      var10.close();
      var2.release();
      return var3;
   }

   public boolean hasCompletedAllPrerequisites(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT COUNT(*)=0 FROM dependency WHERE work_spec_id=? AND prerequisite_id IN (SELECT id FROM workspec WHERE state!=2)", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      Cursor var3 = this.__db.query(var2);

      boolean var6;
      label101: {
         boolean var5;
         int var7;
         label100: {
            Throwable var10000;
            label105: {
               boolean var4;
               boolean var10001;
               try {
                  var4 = var3.moveToFirst();
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label105;
               }

               var5 = false;
               var6 = var5;
               if (!var4) {
                  break label101;
               }

               label95:
               try {
                  var7 = var3.getInt(0);
                  break label100;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label95;
               }
            }

            Throwable var14 = var10000;
            var3.close();
            var2.release();
            throw var14;
         }

         var6 = var5;
         if (var7 != 0) {
            var6 = true;
         }
      }

      var3.close();
      var2.release();
      return var6;
   }

   public boolean hasDependents(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT COUNT(*)>0 FROM dependency WHERE prerequisite_id=?", 1);
      if (var1 == null) {
         var2.bindNull(1);
      } else {
         var2.bindString(1, var1);
      }

      Cursor var14 = this.__db.query(var2);

      boolean var5;
      label101: {
         boolean var4;
         int var6;
         label100: {
            Throwable var10000;
            label105: {
               boolean var3;
               boolean var10001;
               try {
                  var3 = var14.moveToFirst();
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label105;
               }

               var4 = false;
               var5 = var4;
               if (!var3) {
                  break label101;
               }

               label95:
               try {
                  var6 = var14.getInt(0);
                  break label100;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label95;
               }
            }

            Throwable var7 = var10000;
            var14.close();
            var2.release();
            throw var7;
         }

         var5 = var4;
         if (var6 != 0) {
            var5 = true;
         }
      }

      var14.close();
      var2.release();
      return var5;
   }

   public void insertDependency(Dependency var1) {
      this.__db.beginTransaction();

      try {
         this.__insertionAdapterOfDependency.insert((Object)var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }
}
