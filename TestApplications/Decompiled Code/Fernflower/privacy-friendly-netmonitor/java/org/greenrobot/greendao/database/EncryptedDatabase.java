package org.greenrobot.greendao.database;

import android.database.Cursor;
import android.database.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

public class EncryptedDatabase implements Database {
   private final SQLiteDatabase delegate;

   public EncryptedDatabase(SQLiteDatabase var1) {
      this.delegate = var1;
   }

   public void beginTransaction() {
      this.delegate.beginTransaction();
   }

   public void close() {
      this.delegate.close();
   }

   public DatabaseStatement compileStatement(String var1) {
      return new EncryptedDatabaseStatement(this.delegate.compileStatement(var1));
   }

   public void endTransaction() {
      this.delegate.endTransaction();
   }

   public void execSQL(String var1) throws SQLException {
      this.delegate.execSQL(var1);
   }

   public void execSQL(String var1, Object[] var2) throws SQLException {
      this.delegate.execSQL(var1, var2);
   }

   public Object getRawDatabase() {
      return this.delegate;
   }

   public SQLiteDatabase getSQLiteDatabase() {
      return this.delegate;
   }

   public boolean inTransaction() {
      return this.delegate.inTransaction();
   }

   public boolean isDbLockedByCurrentThread() {
      return this.delegate.isDbLockedByCurrentThread();
   }

   public Cursor rawQuery(String var1, String[] var2) {
      return this.delegate.rawQuery(var1, var2);
   }

   public void setTransactionSuccessful() {
      this.delegate.setTransactionSuccessful();
   }
}
