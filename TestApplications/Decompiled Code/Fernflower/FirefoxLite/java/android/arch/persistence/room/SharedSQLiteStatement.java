package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteStatement;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SharedSQLiteStatement {
   private final RoomDatabase mDatabase;
   private final AtomicBoolean mLock = new AtomicBoolean(false);
   private volatile SupportSQLiteStatement mStmt;

   public SharedSQLiteStatement(RoomDatabase var1) {
      this.mDatabase = var1;
   }

   private SupportSQLiteStatement createNewStatement() {
      String var1 = this.createQuery();
      return this.mDatabase.compileStatement(var1);
   }

   private SupportSQLiteStatement getStmt(boolean var1) {
      SupportSQLiteStatement var2;
      if (var1) {
         if (this.mStmt == null) {
            this.mStmt = this.createNewStatement();
         }

         var2 = this.mStmt;
      } else {
         var2 = this.createNewStatement();
      }

      return var2;
   }

   public SupportSQLiteStatement acquire() {
      this.assertNotMainThread();
      return this.getStmt(this.mLock.compareAndSet(false, true));
   }

   protected void assertNotMainThread() {
      this.mDatabase.assertNotMainThread();
   }

   protected abstract String createQuery();

   public void release(SupportSQLiteStatement var1) {
      if (var1 == this.mStmt) {
         this.mLock.set(false);
      }

   }
}
