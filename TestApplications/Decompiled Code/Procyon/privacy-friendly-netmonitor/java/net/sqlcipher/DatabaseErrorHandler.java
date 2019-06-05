// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import net.sqlcipher.database.SQLiteDatabase;

public interface DatabaseErrorHandler
{
    void onCorruption(final SQLiteDatabase p0);
}
