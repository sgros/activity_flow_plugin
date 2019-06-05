package android.arch.persistence.db;

import java.io.Closeable;

public interface SupportSQLiteProgram extends Closeable {
   void bindBlob(int var1, byte[] var2);

   void bindDouble(int var1, double var2);

   void bindLong(int var1, long var2);

   void bindNull(int var1);

   void bindString(int var1, String var2);
}
