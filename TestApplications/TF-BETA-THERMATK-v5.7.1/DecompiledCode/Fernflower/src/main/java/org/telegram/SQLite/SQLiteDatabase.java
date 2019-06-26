package org.telegram.SQLite;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

public class SQLiteDatabase {
   private boolean inTransaction;
   private boolean isOpen;
   private final long sqliteHandle;

   public SQLiteDatabase(String var1) throws SQLiteException {
      this.sqliteHandle = this.opendb(var1, ApplicationLoader.getFilesDirFixed().getPath());
      this.isOpen = true;
   }

   public void beginTransaction() throws SQLiteException {
      if (!this.inTransaction) {
         this.inTransaction = true;
         this.beginTransaction(this.sqliteHandle);
      } else {
         throw new SQLiteException("database already in transaction");
      }
   }

   native void beginTransaction(long var1);

   void checkOpened() throws SQLiteException {
      if (!this.isOpen) {
         throw new SQLiteException("Database closed");
      }
   }

   public void close() {
      if (this.isOpen) {
         try {
            this.commitTransaction();
            this.closedb(this.sqliteHandle);
         } catch (SQLiteException var2) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e(var2.getMessage(), var2);
            }
         }

         this.isOpen = false;
      }

   }

   native void closedb(long var1) throws SQLiteException;

   public void commitTransaction() {
      if (this.inTransaction) {
         this.inTransaction = false;
         this.commitTransaction(this.sqliteHandle);
      }
   }

   native void commitTransaction(long var1);

   public SQLitePreparedStatement executeFast(String var1) throws SQLiteException {
      return new SQLitePreparedStatement(this, var1, true);
   }

   public Integer executeInt(String var1, Object... var2) throws SQLiteException {
      this.checkOpened();
      SQLiteCursor var11 = this.queryFinalized(var1, var2);

      Throwable var10000;
      label78: {
         boolean var10001;
         boolean var3;
         try {
            var3 = var11.next();
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label78;
         }

         if (!var3) {
            var11.dispose();
            return null;
         }

         int var4;
         try {
            var4 = var11.intValue(0);
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label78;
         }

         var11.dispose();
         return var4;
      }

      Throwable var12 = var10000;
      var11.dispose();
      throw var12;
   }

   public void explainQuery(String var1, Object... var2) throws SQLiteException {
      this.checkOpened();
      StringBuilder var3 = new StringBuilder();
      var3.append("EXPLAIN QUERY PLAN ");
      var3.append(var1);
      SQLiteCursor var6 = (new SQLitePreparedStatement(this, var3.toString(), true)).query(var2);

      while(var6.next()) {
         int var4 = var6.getColumnCount();
         var3 = new StringBuilder();

         for(int var5 = 0; var5 < var4; ++var5) {
            var3.append(var6.stringValue(var5));
            var3.append(", ");
         }

         StringBuilder var7 = new StringBuilder();
         var7.append("EXPLAIN QUERY PLAN ");
         var7.append(var3.toString());
         FileLog.d(var7.toString());
      }

      var6.dispose();
   }

   public void finalize() throws Throwable {
      super.finalize();
      this.close();
   }

   public long getSQLiteHandle() {
      return this.sqliteHandle;
   }

   native long opendb(String var1, String var2) throws SQLiteException;

   public SQLiteCursor queryFinalized(String var1, Object... var2) throws SQLiteException {
      this.checkOpened();
      return (new SQLitePreparedStatement(this, var1, true)).query(var2);
   }

   public boolean tableExists(String var1) throws SQLiteException {
      this.checkOpened();
      boolean var2 = true;
      if (this.executeInt("SELECT rowid FROM sqlite_master WHERE type='table' AND name=?;", var1) == null) {
         var2 = false;
      }

      return var2;
   }
}
