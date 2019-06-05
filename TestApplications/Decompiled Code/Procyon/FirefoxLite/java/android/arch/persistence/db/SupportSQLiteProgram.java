// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db;

import java.io.Closeable;

public interface SupportSQLiteProgram extends Closeable
{
    void bindBlob(final int p0, final byte[] p1);
    
    void bindDouble(final int p0, final double p1);
    
    void bindLong(final int p0, final long p1);
    
    void bindNull(final int p0);
    
    void bindString(final int p0, final String p1);
}
