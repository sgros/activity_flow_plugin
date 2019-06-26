package org.greenrobot.greendao.database;

public interface DatabaseStatement {
   void bindBlob(int var1, byte[] var2);

   void bindDouble(int var1, double var2);

   void bindLong(int var1, long var2);

   void bindNull(int var1);

   void bindString(int var1, String var2);

   void clearBindings();

   void close();

   void execute();

   long executeInsert();

   Object getRawStatement();

   long simpleQueryForLong();
}
