// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.database;

import android.database.sqlite.SQLiteStatement;

public class StandardDatabaseStatement implements DatabaseStatement
{
    private final SQLiteStatement delegate;
    
    public StandardDatabaseStatement(final SQLiteStatement delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void bindBlob(final int n, final byte[] array) {
        this.delegate.bindBlob(n, array);
    }
    
    @Override
    public void bindDouble(final int n, final double n2) {
        this.delegate.bindDouble(n, n2);
    }
    
    @Override
    public void bindLong(final int n, final long n2) {
        this.delegate.bindLong(n, n2);
    }
    
    @Override
    public void bindNull(final int n) {
        this.delegate.bindNull(n);
    }
    
    @Override
    public void bindString(final int n, final String s) {
        this.delegate.bindString(n, s);
    }
    
    @Override
    public void clearBindings() {
        this.delegate.clearBindings();
    }
    
    @Override
    public void close() {
        this.delegate.close();
    }
    
    @Override
    public void execute() {
        this.delegate.execute();
    }
    
    @Override
    public long executeInsert() {
        return this.delegate.executeInsert();
    }
    
    @Override
    public Object getRawStatement() {
        return this.delegate;
    }
    
    @Override
    public long simpleQueryForLong() {
        return this.delegate.simpleQueryForLong();
    }
}
