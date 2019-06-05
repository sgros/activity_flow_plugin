package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteStatement;

public abstract class EntityInsertionAdapter extends SharedSQLiteStatement {
   public EntityInsertionAdapter(RoomDatabase var1) {
      super(var1);
   }

   protected abstract void bind(SupportSQLiteStatement var1, Object var2);

   public final void insert(Object var1) {
      SupportSQLiteStatement var2 = this.acquire();

      try {
         this.bind(var2, var1);
         var2.executeInsert();
      } finally {
         this.release(var2);
      }

   }

   public final void insert(Object[] var1) {
      SupportSQLiteStatement var2 = this.acquire();

      Throwable var10000;
      label92: {
         boolean var10001;
         int var3;
         try {
            var3 = var1.length;
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label92;
         }

         int var4 = 0;

         while(true) {
            if (var4 >= var3) {
               this.release(var2);
               return;
            }

            try {
               this.bind(var2, var1[var4]);
               var2.executeInsert();
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break;
            }

            ++var4;
         }
      }

      Throwable var11 = var10000;
      this.release(var2);
      throw var11;
   }
}
