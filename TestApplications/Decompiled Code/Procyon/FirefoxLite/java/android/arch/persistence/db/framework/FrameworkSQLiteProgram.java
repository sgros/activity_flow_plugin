// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db.framework;

import android.database.sqlite.SQLiteProgram;
import android.arch.persistence.db.SupportSQLiteProgram;

class FrameworkSQLiteProgram implements SupportSQLiteProgram
{
    private final SQLiteProgram mDelegate;
    
    FrameworkSQLiteProgram(final SQLiteProgram mDelegate) {
        this.mDelegate = mDelegate;
    }
    
    @Override
    public void bindBlob(final int n, final byte[] array) {
        this.mDelegate.bindBlob(n, array);
    }
    
    @Override
    public void bindDouble(final int n, final double n2) {
        this.mDelegate.bindDouble(n, n2);
    }
    
    @Override
    public void bindLong(final int n, final long n2) {
        this.mDelegate.bindLong(n, n2);
    }
    
    @Override
    public void bindNull(final int n) {
        this.mDelegate.bindNull(n);
    }
    
    @Override
    public void bindString(final int n, final String s) {
        this.mDelegate.bindString(n, s);
    }
    
    @Override
    public void close() {
        this.mDelegate.close();
    }
}
