// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db;

import android.database.Cursor;
import android.content.ContentValues;
import android.util.Pair;
import java.util.List;
import android.database.SQLException;
import java.io.Closeable;

public interface SupportSQLiteDatabase extends Closeable
{
    void beginTransaction();
    
    SupportSQLiteStatement compileStatement(final String p0);
    
    int delete(final String p0, final String p1, final Object[] p2);
    
    void endTransaction();
    
    void execSQL(final String p0) throws SQLException;
    
    List<Pair<String, String>> getAttachedDbs();
    
    String getPath();
    
    boolean inTransaction();
    
    long insert(final String p0, final int p1, final ContentValues p2) throws SQLException;
    
    boolean isOpen();
    
    Cursor query(final SupportSQLiteQuery p0);
    
    Cursor query(final String p0);
    
    void setTransactionSuccessful();
    
    int update(final String p0, final int p1, final ContentValues p2, final String p3, final Object[] p4);
}
