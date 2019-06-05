// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

public interface SQLiteTransactionListener
{
    void onBegin();
    
    void onCommit();
    
    void onRollback();
}
