package androidx.work.impl.model;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;

public class WorkNameDao_Impl implements WorkNameDao {
   private final RoomDatabase __db;
   private final EntityInsertionAdapter __insertionAdapterOfWorkName;

   public WorkNameDao_Impl(RoomDatabase var1) {
      this.__db = var1;
      this.__insertionAdapterOfWorkName = new EntityInsertionAdapter(var1) {
         public void bind(SupportSQLiteStatement var1, WorkName var2) {
            if (var2.name == null) {
               var1.bindNull(1);
            } else {
               var1.bindString(1, var2.name);
            }

            if (var2.workSpecId == null) {
               var1.bindNull(2);
            } else {
               var1.bindString(2, var2.workSpecId);
            }

         }

         public String createQuery() {
            return "INSERT OR IGNORE INTO `WorkName`(`name`,`work_spec_id`) VALUES (?,?)";
         }
      };
   }

   public void insert(WorkName var1) {
      this.__db.beginTransaction();

      try {
         this.__insertionAdapterOfWorkName.insert((Object)var1);
         this.__db.setTransactionSuccessful();
      } finally {
         this.__db.endTransaction();
      }

   }
}
