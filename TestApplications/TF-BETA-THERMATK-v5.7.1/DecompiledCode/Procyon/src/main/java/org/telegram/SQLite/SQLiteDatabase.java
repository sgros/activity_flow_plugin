// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.SQLite;

import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ApplicationLoader;

public class SQLiteDatabase
{
    private boolean inTransaction;
    private boolean isOpen;
    private final long sqliteHandle;
    
    public SQLiteDatabase(final String s) throws SQLiteException {
        this.sqliteHandle = this.opendb(s, ApplicationLoader.getFilesDirFixed().getPath());
        this.isOpen = true;
    }
    
    public void beginTransaction() throws SQLiteException {
        if (!this.inTransaction) {
            this.inTransaction = true;
            this.beginTransaction(this.sqliteHandle);
            return;
        }
        throw new SQLiteException("database already in transaction");
    }
    
    native void beginTransaction(final long p0);
    
    void checkOpened() throws SQLiteException {
        if (this.isOpen) {
            return;
        }
        throw new SQLiteException("Database closed");
    }
    
    public void close() {
        if (this.isOpen) {
            try {
                this.commitTransaction();
                this.closedb(this.sqliteHandle);
            }
            catch (SQLiteException ex) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e(ex.getMessage(), ex);
                }
            }
            this.isOpen = false;
        }
    }
    
    native void closedb(final long p0) throws SQLiteException;
    
    public void commitTransaction() {
        if (!this.inTransaction) {
            return;
        }
        this.inTransaction = false;
        this.commitTransaction(this.sqliteHandle);
    }
    
    native void commitTransaction(final long p0);
    
    public SQLitePreparedStatement executeFast(final String s) throws SQLiteException {
        return new SQLitePreparedStatement(this, s, true);
    }
    
    public Integer executeInt(String queryFinalized, final Object... array) throws SQLiteException {
        this.checkOpened();
        queryFinalized = (String)this.queryFinalized(queryFinalized, array);
        try {
            if (!((SQLiteCursor)queryFinalized).next()) {
                return null;
            }
            final int intValue = ((SQLiteCursor)queryFinalized).intValue(0);
            ((SQLiteCursor)queryFinalized).dispose();
            return intValue;
        }
        finally {
            ((SQLiteCursor)queryFinalized).dispose();
        }
    }
    
    public void explainQuery(final String str, final Object... array) throws SQLiteException {
        this.checkOpened();
        final StringBuilder sb = new StringBuilder();
        sb.append("EXPLAIN QUERY PLAN ");
        sb.append(str);
        final SQLiteCursor query = new SQLitePreparedStatement(this, sb.toString(), true).query(array);
        while (query.next()) {
            final int columnCount = query.getColumnCount();
            final StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < columnCount; ++i) {
                sb2.append(query.stringValue(i));
                sb2.append(", ");
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("EXPLAIN QUERY PLAN ");
            sb3.append(sb2.toString());
            FileLog.d(sb3.toString());
        }
        query.dispose();
    }
    
    public void finalize() throws Throwable {
        super.finalize();
        this.close();
    }
    
    public long getSQLiteHandle() {
        return this.sqliteHandle;
    }
    
    native long opendb(final String p0, final String p1) throws SQLiteException;
    
    public SQLiteCursor queryFinalized(final String s, final Object... array) throws SQLiteException {
        this.checkOpened();
        return new SQLitePreparedStatement(this, s, true).query(array);
    }
    
    public boolean tableExists(final String s) throws SQLiteException {
        this.checkOpened();
        boolean b = true;
        if (this.executeInt("SELECT rowid FROM sqlite_master WHERE type='table' AND name=?;", s) == null) {
            b = false;
        }
        return b;
    }
}
