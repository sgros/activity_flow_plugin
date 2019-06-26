// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

public interface SQLiteDatabaseHook
{
    void postKey(final SQLiteDatabase p0);
    
    void preKey(final SQLiteDatabase p0);
}
