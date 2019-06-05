package androidx.work.impl.model;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class WorkTagDao_Impl implements WorkTagDao {
   private final RoomDatabase __db;
   private final EntityInsertionAdapter __insertionAdapterOfWorkTag;

   public WorkTagDao_Impl(RoomDatabase var1) {
      this.__db = var1;
      this.__insertionAdapterOfWorkTag = new EntityInsertionAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, WorkTag var2) {
            if (var2.tag == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.tag);
            }

            if (var2.workSpecId == null) {
               var1.bindNull(2);
            } else {
               var1.bindString(2, var2.workSpecId);
            }

         }

         public String createQuery() {
            return "INSERT OR IGNORE INTO `WorkTag`(`tag`,`work_spec_id`) VALUES (?,?)";
         }
      };
   }

   public List getTagsForWorkSpecId(String var1) {
      RoomSQLiteQuery var2 = RoomSQLiteQuery.acquire("SELECT DISTINCT tag FROM worktag WHERE work_spec_id=?", 1);
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

   public void insert(WorkTag var1) {
      this.__db.beginTransaction();

      try {
         this.__insertionAdapterOfWorkTag.insert((Object)var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }
}
