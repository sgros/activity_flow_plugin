// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db;

public interface SupportSQLiteQuery
{
    void bindTo(final SupportSQLiteProgram p0);
    
    String getSql();
}
