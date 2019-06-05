package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteStatement;

public abstract class EntityDeletionOrUpdateAdapter extends SharedSQLiteStatement {
   public EntityDeletionOrUpdateAdapter(RoomDatabase var1) {
      super(var1);
   }

   protected abstract void bind(SupportSQLiteStatement var1, Object var2);

   protected abstract String createQuery();

   public final int handle(Object var1) {
      SupportSQLiteStatement var2 = this.acquire();

      int var3;
      try {
         this.bind(var2, var1);
         var3 = var2.executeUpdateDelete();
      } finally {
         this.release(var2);
      }

      return var3;
   }
}
