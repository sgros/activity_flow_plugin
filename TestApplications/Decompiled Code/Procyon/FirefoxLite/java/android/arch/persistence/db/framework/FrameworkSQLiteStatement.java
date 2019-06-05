// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db.framework;

import android.database.sqlite.SQLiteProgram;
import android.database.sqlite.SQLiteStatement;
import android.arch.persistence.db.SupportSQLiteStatement;

class FrameworkSQLiteStatement extends FrameworkSQLiteProgram implements SupportSQLiteStatement
{
    private final SQLiteStatement mDelegate;
    
    FrameworkSQLiteStatement(final SQLiteStatement mDelegate) {
        super((SQLiteProgram)mDelegate);
        this.mDelegate = mDelegate;
    }
    
    @Override
    public long executeInsert() {
        return this.mDelegate.executeInsert();
    }
    
    @Override
    public int executeUpdateDelete() {
        return this.mDelegate.executeUpdateDelete();
    }
}
