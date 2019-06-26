package net.sqlcipher.database;

public interface SQLiteDatabaseHook {
   void postKey(SQLiteDatabase var1);

   void preKey(SQLiteDatabase var1);
}
