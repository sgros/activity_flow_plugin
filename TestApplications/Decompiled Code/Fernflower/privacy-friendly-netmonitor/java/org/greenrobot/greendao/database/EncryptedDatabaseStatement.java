package org.greenrobot.greendao.database;

import net.sqlcipher.database.SQLiteStatement;

public class EncryptedDatabaseStatement implements DatabaseStatement {
   private final SQLiteStatement delegate;

   public EncryptedDatabaseStatement(SQLiteStatement var1) {
      this.delegate = var1;
   }

   public void bindBlob(int var1, byte[] var2) {
      this.delegate.bindBlob(var1, var2);
   }

   public void bindDouble(int var1, double var2) {
      this.delegate.bindDouble(var1, var2);
   }

   public void bindLong(int var1, long var2) {
      this.delegate.bindLong(var1, var2);
   }

   public void bindNull(int var1) {
      this.delegate.bindNull(var1);
   }

   public void bindString(int var1, String var2) {
      this.delegate.bindString(var1, var2);
   }

   public void clearBindings() {
      this.delegate.clearBindings();
   }

   public void close() {
      this.delegate.close();
   }

   public void execute() {
      this.delegate.execute();
   }

   public long executeInsert() {
      return this.delegate.executeInsert();
   }

   public Object getRawStatement() {
      return this.delegate;
   }

   public long simpleQueryForLong() {
      return this.delegate.simpleQueryForLong();
   }
}
