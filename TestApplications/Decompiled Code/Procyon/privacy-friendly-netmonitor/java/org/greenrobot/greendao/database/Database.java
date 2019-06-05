// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.database;

import android.database.Cursor;
import android.database.SQLException;

public interface Database
{
    void beginTransaction();
    
    void close();
    
    DatabaseStatement compileStatement(final String p0);
    
    void endTransaction();
    
    void execSQL(final String p0) throws SQLException;
    
    void execSQL(final String p0, final Object[] p1) throws SQLException;
    
    Object getRawDatabase();
    
    boolean inTransaction();
    
    boolean isDbLockedByCurrentThread();
    
    Cursor rawQuery(final String p0, final String[] p1);
    
    void setTransactionSuccessful();
}
