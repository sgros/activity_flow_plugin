package net.sqlcipher.database;

import android.database.Cursor;

public interface SQLiteCursorDriver {
   void cursorClosed();

   void cursorDeactivated();

   void cursorRequeried(Cursor var1);

   net.sqlcipher.Cursor query(SQLiteDatabase.CursorFactory var1, String[] var2);

   void setBindArguments(String[] var1);
}
