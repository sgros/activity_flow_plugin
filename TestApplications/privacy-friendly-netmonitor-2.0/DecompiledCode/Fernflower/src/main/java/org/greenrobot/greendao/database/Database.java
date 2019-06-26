package org.greenrobot.greendao.database;

import android.database.Cursor;
import android.database.SQLException;

public interface Database {
   void beginTransaction();

   void close();

   DatabaseStatement compileStatement(String var1);

   void endTransaction();

   void execSQL(String var1) throws SQLException;

   void execSQL(String var1, Object[] var2) throws SQLException;

   Object getRawDatabase();

   boolean inTransaction();

   boolean isDbLockedByCurrentThread();

   Cursor rawQuery(String var1, String[] var2);

   void setTransactionSuccessful();
}
