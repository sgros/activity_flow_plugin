package net.sqlcipher.database;

import android.util.Log;
import java.util.Map;

public abstract class SQLiteProgram extends SQLiteClosable {
   private static final String TAG = "SQLiteProgram";
   boolean mClosed = false;
   private SQLiteCompiledSql mCompiledSql;
   @Deprecated
   protected SQLiteDatabase mDatabase;
   final String mSql;
   @Deprecated
   protected long nHandle = 0L;
   @Deprecated
   protected long nStatement = 0L;

   SQLiteProgram(SQLiteDatabase var1, String var2) {
      this.mDatabase = var1;
      this.mSql = var2.trim();
      var1.acquireReference();
      var1.addSQLiteClosable(this);
      this.nHandle = var1.mNativeHandle;
      String var3;
      if (this.mSql.length() >= 6) {
         var3 = this.mSql.substring(0, 6);
      } else {
         var3 = this.mSql;
      }

      if (!var3.equalsIgnoreCase("INSERT") && !var3.equalsIgnoreCase("UPDATE") && !var3.equalsIgnoreCase("REPLAC") && !var3.equalsIgnoreCase("DELETE") && !var3.equalsIgnoreCase("SELECT")) {
         this.mCompiledSql = new SQLiteCompiledSql(var1, var2);
         this.nStatement = this.mCompiledSql.nStatement;
      } else {
         this.mCompiledSql = var1.getCompiledStatementForSql(var2);
         StringBuilder var6;
         if (this.mCompiledSql == null) {
            this.mCompiledSql = new SQLiteCompiledSql(var1, var2);
            this.mCompiledSql.acquire();
            var1.addToCompiledQueries(var2, this.mCompiledSql);
            if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
               var6 = new StringBuilder();
               var6.append("Created DbObj (id#");
               var6.append(this.mCompiledSql.nStatement);
               var6.append(") for sql: ");
               var6.append(var2);
               Log.v("SQLiteProgram", var6.toString());
            }
         } else if (!this.mCompiledSql.acquire()) {
            long var4 = this.mCompiledSql.nStatement;
            this.mCompiledSql = new SQLiteCompiledSql(var1, var2);
            if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
               var6 = new StringBuilder();
               var6.append("** possible bug ** Created NEW DbObj (id#");
               var6.append(this.mCompiledSql.nStatement);
               var6.append(") because the previously created DbObj (id#");
               var6.append(var4);
               var6.append(") was not released for sql:");
               var6.append(var2);
               Log.v("SQLiteProgram", var6.toString());
            }
         }

         this.nStatement = this.mCompiledSql.nStatement;
      }
   }

   private final native void native_clear_bindings();

   private void releaseCompiledSqlIfNotInCache() {
      if (this.mCompiledSql != null) {
         Map var1 = this.mDatabase.mCompiledQueries;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label215: {
            label207: {
               try {
                  if (!this.mDatabase.mCompiledQueries.containsValue(this.mCompiledSql)) {
                     this.mCompiledSql.releaseSqlStatement();
                     this.mCompiledSql = null;
                     this.nStatement = 0L;
                     break label207;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label215;
               }

               try {
                  this.mCompiledSql.release();
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label215;
               }
            }

            label198:
            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label198;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void bindBlob(int var1, byte[] var2) {
      StringBuilder var5;
      if (var2 == null) {
         var5 = new StringBuilder();
         var5.append("the bind value at index ");
         var5.append(var1);
         var5.append(" is null");
         throw new IllegalArgumentException(var5.toString());
      } else if (this.mClosed) {
         throw new IllegalStateException("program already closed");
      } else if (!this.mDatabase.isOpen()) {
         var5 = new StringBuilder();
         var5.append("database ");
         var5.append(this.mDatabase.getPath());
         var5.append(" already closed");
         throw new IllegalStateException(var5.toString());
      } else {
         this.acquireReference();

         try {
            this.native_bind_blob(var1, var2);
         } finally {
            this.releaseReference();
         }

      }
   }

   public void bindDouble(int var1, double var2) {
      if (this.mClosed) {
         throw new IllegalStateException("program already closed");
      } else if (!this.mDatabase.isOpen()) {
         StringBuilder var4 = new StringBuilder();
         var4.append("database ");
         var4.append(this.mDatabase.getPath());
         var4.append(" already closed");
         throw new IllegalStateException(var4.toString());
      } else {
         this.acquireReference();

         try {
            this.native_bind_double(var1, var2);
         } finally {
            this.releaseReference();
         }

      }
   }

   public void bindLong(int var1, long var2) {
      if (this.mClosed) {
         throw new IllegalStateException("program already closed");
      } else if (!this.mDatabase.isOpen()) {
         StringBuilder var4 = new StringBuilder();
         var4.append("database ");
         var4.append(this.mDatabase.getPath());
         var4.append(" already closed");
         throw new IllegalStateException(var4.toString());
      } else {
         this.acquireReference();

         try {
            this.native_bind_long(var1, var2);
         } finally {
            this.releaseReference();
         }

      }
   }

   public void bindNull(int var1) {
      if (this.mClosed) {
         throw new IllegalStateException("program already closed");
      } else if (!this.mDatabase.isOpen()) {
         StringBuilder var2 = new StringBuilder();
         var2.append("database ");
         var2.append(this.mDatabase.getPath());
         var2.append(" already closed");
         throw new IllegalStateException(var2.toString());
      } else {
         this.acquireReference();

         try {
            this.native_bind_null(var1);
         } finally {
            this.releaseReference();
         }

      }
   }

   public void bindString(int var1, String var2) {
      StringBuilder var5;
      if (var2 == null) {
         var5 = new StringBuilder();
         var5.append("the bind value at index ");
         var5.append(var1);
         var5.append(" is null");
         throw new IllegalArgumentException(var5.toString());
      } else if (this.mClosed) {
         throw new IllegalStateException("program already closed");
      } else if (!this.mDatabase.isOpen()) {
         var5 = new StringBuilder();
         var5.append("database ");
         var5.append(this.mDatabase.getPath());
         var5.append(" already closed");
         throw new IllegalStateException(var5.toString());
      } else {
         this.acquireReference();

         try {
            this.native_bind_string(var1, var2);
         } finally {
            this.releaseReference();
         }

      }
   }

   public void clearBindings() {
      if (this.mClosed) {
         throw new IllegalStateException("program already closed");
      } else if (!this.mDatabase.isOpen()) {
         StringBuilder var1 = new StringBuilder();
         var1.append("database ");
         var1.append(this.mDatabase.getPath());
         var1.append(" already closed");
         throw new IllegalStateException(var1.toString());
      } else {
         this.acquireReference();

         try {
            this.native_clear_bindings();
         } finally {
            this.releaseReference();
         }

      }
   }

   public void close() {
      if (!this.mClosed) {
         if (this.mDatabase.isOpen()) {
            this.mDatabase.lock();

            try {
               this.releaseReference();
            } finally {
               this.mDatabase.unlock();
            }

            this.mClosed = true;
         }
      }
   }

   @Deprecated
   protected void compile(String var1, boolean var2) {
   }

   String getSqlString() {
      return this.mSql;
   }

   public final long getUniqueId() {
      return this.nStatement;
   }

   protected final native void native_bind_blob(int var1, byte[] var2);

   protected final native void native_bind_double(int var1, double var2);

   protected final native void native_bind_long(int var1, long var2);

   protected final native void native_bind_null(int var1);

   protected final native void native_bind_string(int var1, String var2);

   @Deprecated
   protected final native void native_compile(String var1);

   @Deprecated
   protected final native void native_finalize();

   protected void onAllReferencesReleased() {
      this.releaseCompiledSqlIfNotInCache();
      this.mDatabase.releaseReference();
      this.mDatabase.removeSQLiteClosable(this);
   }

   protected void onAllReferencesReleasedFromContainer() {
      this.releaseCompiledSqlIfNotInCache();
      this.mDatabase.releaseReference();
   }
}
