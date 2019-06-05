package android.arch.persistence.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import java.io.Closeable;
import java.util.List;

public interface SupportSQLiteDatabase extends Closeable {
   void beginTransaction();

   SupportSQLiteStatement compileStatement(String var1);

   int delete(String var1, String var2, Object[] var3);

   void endTransaction();

   void execSQL(String var1) throws SQLException;

   List getAttachedDbs();

   String getPath();

   boolean inTransaction();

   long insert(String var1, int var2, ContentValues var3) throws SQLException;

   boolean isOpen();

   Cursor query(SupportSQLiteQuery var1);

   Cursor query(String var1);

   void setTransactionSuccessful();

   int update(String var1, int var2, ContentValues var3, String var4, Object[] var5);
}
