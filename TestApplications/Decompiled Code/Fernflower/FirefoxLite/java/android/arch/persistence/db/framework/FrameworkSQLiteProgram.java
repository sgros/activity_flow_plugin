package android.arch.persistence.db.framework;

import android.arch.persistence.db.SupportSQLiteProgram;
import android.database.sqlite.SQLiteProgram;

class FrameworkSQLiteProgram implements SupportSQLiteProgram {
   private final SQLiteProgram mDelegate;

   FrameworkSQLiteProgram(SQLiteProgram var1) {
      this.mDelegate = var1;
   }

   public void bindBlob(int var1, byte[] var2) {
      this.mDelegate.bindBlob(var1, var2);
   }

   public void bindDouble(int var1, double var2) {
      this.mDelegate.bindDouble(var1, var2);
   }

   public void bindLong(int var1, long var2) {
      this.mDelegate.bindLong(var1, var2);
   }

   public void bindNull(int var1) {
      this.mDelegate.bindNull(var1);
   }

   public void bindString(int var1, String var2) {
      this.mDelegate.bindString(var1, var2);
   }

   public void close() {
      this.mDelegate.close();
   }
}
