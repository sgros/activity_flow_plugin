// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StandardDatabase implements Database
{
    private final SQLiteDatabase delegate;
    
    public StandardDatabase(final SQLiteDatabase delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void beginTransaction() {
        this.delegate.beginTransaction();
    }
    
    @Override
    public void close() {
        this.delegate.close();
    }
    
    @Override
    public DatabaseStatement compileStatement(final String s) {
        return new StandardDatabaseStatement(this.delegate.compileStatement(s));
    }
    
    @Override
    public void endTransaction() {
        this.delegate.endTransaction();
    }
    
    @Override
    public void execSQL(final String s) throws SQLException {
        this.delegate.execSQL(s);
    }
    
    @Override
    public void execSQL(final String s, final Object[] array) throws SQLException {
        this.delegate.execSQL(s, array);
    }
    
    @Override
    public Object getRawDatabase() {
        return this.delegate;
    }
    
    public SQLiteDatabase getSQLiteDatabase() {
        return this.delegate;
    }
    
    @Override
    public boolean inTransaction() {
        return this.delegate.inTransaction();
    }
    
    @Override
    public boolean isDbLockedByCurrentThread() {
        return this.delegate.isDbLockedByCurrentThread();
    }
    
    @Override
    public Cursor rawQuery(final String s, final String[] array) {
        return this.delegate.rawQuery(s, array);
    }
    
    @Override
    public void setTransactionSuccessful() {
        this.delegate.setTransactionSuccessful();
    }
}
