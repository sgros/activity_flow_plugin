package org.telegram.SQLite;

import java.nio.ByteBuffer;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.NativeByteBuffer;

public class SQLitePreparedStatement {
   private boolean finalizeAfterQuery;
   private boolean isFinalized = false;
   private long sqliteStatementHandle;

   public SQLitePreparedStatement(SQLiteDatabase var1, String var2, boolean var3) throws SQLiteException {
      this.finalizeAfterQuery = var3;
      this.sqliteStatementHandle = this.prepare(var1.getSQLiteHandle(), var2);
   }

   public void bindByteBuffer(int var1, ByteBuffer var2) throws SQLiteException {
      this.bindByteBuffer(this.sqliteStatementHandle, var1, var2, var2.limit());
   }

   public void bindByteBuffer(int var1, NativeByteBuffer var2) throws SQLiteException {
      this.bindByteBuffer(this.sqliteStatementHandle, var1, var2.buffer, var2.limit());
   }

   native void bindByteBuffer(long var1, int var3, ByteBuffer var4, int var5) throws SQLiteException;

   public void bindDouble(int var1, double var2) throws SQLiteException {
      this.bindDouble(this.sqliteStatementHandle, var1, var2);
   }

   native void bindDouble(long var1, int var3, double var4) throws SQLiteException;

   native void bindInt(long var1, int var3, int var4) throws SQLiteException;

   public void bindInteger(int var1, int var2) throws SQLiteException {
      this.bindInt(this.sqliteStatementHandle, var1, var2);
   }

   public void bindLong(int var1, long var2) throws SQLiteException {
      this.bindLong(this.sqliteStatementHandle, var1, var2);
   }

   native void bindLong(long var1, int var3, long var4) throws SQLiteException;

   public void bindNull(int var1) throws SQLiteException {
      this.bindNull(this.sqliteStatementHandle, var1);
   }

   native void bindNull(long var1, int var3) throws SQLiteException;

   public void bindString(int var1, String var2) throws SQLiteException {
      this.bindString(this.sqliteStatementHandle, var1, var2);
   }

   native void bindString(long var1, int var3, String var4) throws SQLiteException;

   void checkFinalized() throws SQLiteException {
      if (this.isFinalized) {
         throw new SQLiteException("Prepared query finalized");
      }
   }

   public void dispose() {
      if (this.finalizeAfterQuery) {
         this.finalizeQuery();
      }

   }

   native void finalize(long var1) throws SQLiteException;

   public void finalizeQuery() {
      if (!this.isFinalized) {
         try {
            this.isFinalized = true;
            this.finalize(this.sqliteStatementHandle);
         } catch (SQLiteException var2) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e(var2.getMessage(), var2);
            }
         }

      }
   }

   public long getStatementHandle() {
      return this.sqliteStatementHandle;
   }

   native long prepare(long var1, String var3) throws SQLiteException;

   public SQLiteCursor query(Object[] var1) throws SQLiteException {
      if (var1 != null) {
         this.checkFinalized();
         this.reset(this.sqliteStatementHandle);
         int var2 = 0;

         for(int var3 = 1; var2 < var1.length; ++var2) {
            Object var4 = var1[var2];
            if (var4 == null) {
               this.bindNull(this.sqliteStatementHandle, var3);
            } else if (var4 instanceof Integer) {
               this.bindInt(this.sqliteStatementHandle, var3, (Integer)var4);
            } else if (var4 instanceof Double) {
               this.bindDouble(this.sqliteStatementHandle, var3, (Double)var4);
            } else if (var4 instanceof String) {
               this.bindString(this.sqliteStatementHandle, var3, (String)var4);
            } else {
               if (!(var4 instanceof Long)) {
                  throw new IllegalArgumentException();
               }

               this.bindLong(this.sqliteStatementHandle, var3, (Long)var4);
            }

            ++var3;
         }

         return new SQLiteCursor(this);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void requery() throws SQLiteException {
      this.checkFinalized();
      this.reset(this.sqliteStatementHandle);
   }

   native void reset(long var1) throws SQLiteException;

   public int step() throws SQLiteException {
      return this.step(this.sqliteStatementHandle);
   }

   native int step(long var1) throws SQLiteException;

   public SQLitePreparedStatement stepThis() throws SQLiteException {
      this.step(this.sqliteStatementHandle);
      return this;
   }
}
