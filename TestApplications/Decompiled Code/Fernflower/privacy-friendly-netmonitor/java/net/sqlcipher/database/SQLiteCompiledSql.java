package net.sqlcipher.database;

import android.util.Log;

class SQLiteCompiledSql {
   private static final String TAG = "SQLiteCompiledSql";
   SQLiteDatabase mDatabase;
   private boolean mInUse = false;
   private String mSqlStmt = null;
   private Throwable mStackTrace = null;
   long nHandle = 0L;
   long nStatement = 0L;

   SQLiteCompiledSql(SQLiteDatabase var1, String var2) {
      if (!var1.isOpen()) {
         StringBuilder var3 = new StringBuilder();
         var3.append("database ");
         var3.append(var1.getPath());
         var3.append(" already closed");
         throw new IllegalStateException(var3.toString());
      } else {
         this.mDatabase = var1;
         this.mSqlStmt = var2;
         this.mStackTrace = (new DatabaseObjectNotClosedException()).fillInStackTrace();
         this.nHandle = var1.mNativeHandle;
         this.compile(var2, true);
      }
   }

   private void compile(String var1, boolean var2) {
      if (!this.mDatabase.isOpen()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("database ");
         var5.append(this.mDatabase.getPath());
         var5.append(" already closed");
         throw new IllegalStateException(var5.toString());
      } else {
         if (var2) {
            this.mDatabase.lock();

            try {
               this.native_compile(var1);
            } finally {
               this.mDatabase.unlock();
            }
         }

      }
   }

   private final native void native_compile(String var1);

   private final native void native_finalize();

   boolean acquire() {
      synchronized(this){}

      Throwable var10000;
      label90: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.mInUse;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label90;
         }

         if (var1) {
            return false;
         }

         label81:
         try {
            this.mInUse = true;
            if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
               StringBuilder var9 = new StringBuilder();
               var9.append("Acquired DbObj (id#");
               var9.append(this.nStatement);
               var9.append(") from DB cache");
               Log.v("SQLiteCompiledSql", var9.toString());
            }

            return true;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label81;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   protected void finalize() throws Throwable {
      label213: {
         Throwable var10000;
         label216: {
            long var1;
            boolean var10001;
            try {
               var1 = this.nStatement;
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label216;
            }

            if (var1 == 0L) {
               super.finalize();
               return;
            }

            StringBuilder var3;
            try {
               if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
                  var3 = new StringBuilder();
                  var3.append("** warning ** Finalized DbObj (id#");
                  var3.append(this.nStatement);
                  var3.append(")");
                  Log.v("SQLiteCompiledSql", var3.toString());
               }
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label216;
            }

            int var4;
            String var5;
            try {
               var4 = this.mSqlStmt.length();
               var3 = new StringBuilder();
               var3.append("Releasing statement in a finalizer. Please ensure that you explicitly call close() on your cursor: ");
               var5 = this.mSqlStmt;
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label216;
            }

            int var6 = var4;
            if (var4 > 100) {
               var6 = 100;
            }

            label201:
            try {
               var3.append(var5.substring(0, var6));
               Log.w("SQLiteCompiledSql", var3.toString(), this.mStackTrace);
               this.releaseSqlStatement();
               break label213;
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label201;
            }
         }

         Throwable var27 = var10000;
         super.finalize();
         throw var27;
      }

      super.finalize();
   }

   void release() {
      synchronized(this){}

      try {
         if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
            StringBuilder var1 = new StringBuilder();
            var1.append("Released DbObj (id#");
            var1.append(this.nStatement);
            var1.append(") back to DB cache");
            Log.v("SQLiteCompiledSql", var1.toString());
         }

         this.mInUse = false;
      } finally {
         ;
      }

   }

   void releaseSqlStatement() {
      if (this.nStatement != 0L) {
         if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
            StringBuilder var1 = new StringBuilder();
            var1.append("closed and deallocated DbObj (id#");
            var1.append(this.nStatement);
            var1.append(")");
            Log.v("SQLiteCompiledSql", var1.toString());
         }

         try {
            this.mDatabase.lock();
            this.native_finalize();
            this.nStatement = 0L;
         } finally {
            this.mDatabase.unlock();
         }
      }

   }
}
