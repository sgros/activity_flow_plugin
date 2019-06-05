package android.arch.persistence.db.framework;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.database.sqlite.SQLiteStatement;

class FrameworkSQLiteStatement extends FrameworkSQLiteProgram implements SupportSQLiteStatement {
   private final SQLiteStatement mDelegate;

   FrameworkSQLiteStatement(SQLiteStatement var1) {
      super(var1);
      this.mDelegate = var1;
   }

   public long executeInsert() {
      return this.mDelegate.executeInsert();
   }

   public int executeUpdateDelete() {
      return this.mDelegate.executeUpdateDelete();
   }
}
