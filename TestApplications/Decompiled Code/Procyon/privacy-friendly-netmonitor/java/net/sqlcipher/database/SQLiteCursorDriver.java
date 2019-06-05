// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import android.database.Cursor;

public interface SQLiteCursorDriver
{
    void cursorClosed();
    
    void cursorDeactivated();
    
    void cursorRequeried(final Cursor p0);
    
    net.sqlcipher.Cursor query(final SQLiteDatabase.CursorFactory p0, final String[] p1);
    
    void setBindArguments(final String[] p0);
}
