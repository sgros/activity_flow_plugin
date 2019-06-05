// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.database;

public interface DatabaseStatement
{
    void bindBlob(final int p0, final byte[] p1);
    
    void bindDouble(final int p0, final double p1);
    
    void bindLong(final int p0, final long p1);
    
    void bindNull(final int p0);
    
    void bindString(final int p0, final String p1);
    
    void clearBindings();
    
    void close();
    
    void execute();
    
    long executeInsert();
    
    Object getRawStatement();
    
    long simpleQueryForLong();
}
