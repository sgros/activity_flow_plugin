// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db;

public interface SupportSQLiteStatement extends SupportSQLiteProgram
{
    long executeInsert();
    
    int executeUpdateDelete();
}
