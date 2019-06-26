package org.greenrobot.greendao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public abstract class DatabaseOpenHelper extends SQLiteOpenHelper {
   private final Context context;
   private DatabaseOpenHelper.EncryptedHelper encryptedHelper;
   private boolean loadSQLCipherNativeLibs;
   private final String name;
   private final int version;

   public DatabaseOpenHelper(Context var1, String var2, int var3) {
      this(var1, var2, (CursorFactory)null, var3);
   }

   public DatabaseOpenHelper(Context var1, String var2, CursorFactory var3, int var4) {
      super(var1, var2, var3, var4);
      this.loadSQLCipherNativeLibs = true;
      this.context = var1;
      this.name = var2;
      this.version = var4;
   }

   private DatabaseOpenHelper.EncryptedHelper checkEncryptedHelper() {
      if (this.encryptedHelper == null) {
         this.encryptedHelper = new DatabaseOpenHelper.EncryptedHelper(this.context, this.name, this.version, this.loadSQLCipherNativeLibs);
      }

      return this.encryptedHelper;
   }

   public Database getEncryptedReadableDb(String var1) {
      DatabaseOpenHelper.EncryptedHelper var2 = this.checkEncryptedHelper();
      return var2.wrap(var2.getReadableDatabase(var1));
   }

   public Database getEncryptedReadableDb(char[] var1) {
      DatabaseOpenHelper.EncryptedHelper var2 = this.checkEncryptedHelper();
      return var2.wrap(var2.getReadableDatabase(var1));
   }

   public Database getEncryptedWritableDb(String var1) {
      DatabaseOpenHelper.EncryptedHelper var2 = this.checkEncryptedHelper();
      return var2.wrap(var2.getWritableDatabase(var1));
   }

   public Database getEncryptedWritableDb(char[] var1) {
      DatabaseOpenHelper.EncryptedHelper var2 = this.checkEncryptedHelper();
      return var2.wrap(var2.getWritableDatabase(var1));
   }

   public Database getReadableDb() {
      return this.wrap(this.getReadableDatabase());
   }

   public Database getWritableDb() {
      return this.wrap(this.getWritableDatabase());
   }

   public void onCreate(SQLiteDatabase var1) {
      this.onCreate(this.wrap(var1));
   }

   public void onCreate(Database var1) {
   }

   public void onOpen(SQLiteDatabase var1) {
      this.onOpen(this.wrap(var1));
   }

   public void onOpen(Database var1) {
   }

   public void onUpgrade(SQLiteDatabase var1, int var2, int var3) {
      this.onUpgrade(this.wrap(var1), var2, var3);
   }

   public void onUpgrade(Database var1, int var2, int var3) {
   }

   public void setLoadSQLCipherNativeLibs(boolean var1) {
      this.loadSQLCipherNativeLibs = var1;
   }

   protected Database wrap(SQLiteDatabase var1) {
      return new StandardDatabase(var1);
   }

   private class EncryptedHelper extends net.sqlcipher.database.SQLiteOpenHelper {
      public EncryptedHelper(Context var2, String var3, int var4, boolean var5) {
         super(var2, var3, (net.sqlcipher.database.SQLiteDatabase.CursorFactory)null, var4);
         if (var5) {
            net.sqlcipher.database.SQLiteDatabase.loadLibs(var2);
         }

      }

      public void onCreate(net.sqlcipher.database.SQLiteDatabase var1) {
         DatabaseOpenHelper.this.onCreate(this.wrap(var1));
      }

      public void onOpen(net.sqlcipher.database.SQLiteDatabase var1) {
         DatabaseOpenHelper.this.onOpen(this.wrap(var1));
      }

      public void onUpgrade(net.sqlcipher.database.SQLiteDatabase var1, int var2, int var3) {
         DatabaseOpenHelper.this.onUpgrade(this.wrap(var1), var2, var3);
      }

      protected Database wrap(net.sqlcipher.database.SQLiteDatabase var1) {
         return new EncryptedDatabase(var1);
      }
   }
}
