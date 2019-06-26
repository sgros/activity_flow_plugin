// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.SQLite;

import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import org.telegram.tgnet.NativeByteBuffer;
import java.nio.ByteBuffer;

public class SQLitePreparedStatement
{
    private boolean finalizeAfterQuery;
    private boolean isFinalized;
    private long sqliteStatementHandle;
    
    public SQLitePreparedStatement(final SQLiteDatabase sqLiteDatabase, final String s, final boolean finalizeAfterQuery) throws SQLiteException {
        this.isFinalized = false;
        this.finalizeAfterQuery = finalizeAfterQuery;
        this.sqliteStatementHandle = this.prepare(sqLiteDatabase.getSQLiteHandle(), s);
    }
    
    public void bindByteBuffer(final int n, final ByteBuffer byteBuffer) throws SQLiteException {
        this.bindByteBuffer(this.sqliteStatementHandle, n, byteBuffer, byteBuffer.limit());
    }
    
    public void bindByteBuffer(final int n, final NativeByteBuffer nativeByteBuffer) throws SQLiteException {
        this.bindByteBuffer(this.sqliteStatementHandle, n, nativeByteBuffer.buffer, nativeByteBuffer.limit());
    }
    
    native void bindByteBuffer(final long p0, final int p1, final ByteBuffer p2, final int p3) throws SQLiteException;
    
    public void bindDouble(final int n, final double n2) throws SQLiteException {
        this.bindDouble(this.sqliteStatementHandle, n, n2);
    }
    
    native void bindDouble(final long p0, final int p1, final double p2) throws SQLiteException;
    
    native void bindInt(final long p0, final int p1, final int p2) throws SQLiteException;
    
    public void bindInteger(final int n, final int n2) throws SQLiteException {
        this.bindInt(this.sqliteStatementHandle, n, n2);
    }
    
    public void bindLong(final int n, final long n2) throws SQLiteException {
        this.bindLong(this.sqliteStatementHandle, n, n2);
    }
    
    native void bindLong(final long p0, final int p1, final long p2) throws SQLiteException;
    
    public void bindNull(final int n) throws SQLiteException {
        this.bindNull(this.sqliteStatementHandle, n);
    }
    
    native void bindNull(final long p0, final int p1) throws SQLiteException;
    
    public void bindString(final int n, final String s) throws SQLiteException {
        this.bindString(this.sqliteStatementHandle, n, s);
    }
    
    native void bindString(final long p0, final int p1, final String p2) throws SQLiteException;
    
    void checkFinalized() throws SQLiteException {
        if (!this.isFinalized) {
            return;
        }
        throw new SQLiteException("Prepared query finalized");
    }
    
    public void dispose() {
        if (this.finalizeAfterQuery) {
            this.finalizeQuery();
        }
    }
    
    native void finalize(final long p0) throws SQLiteException;
    
    public void finalizeQuery() {
        if (this.isFinalized) {
            return;
        }
        try {
            this.isFinalized = true;
            this.finalize(this.sqliteStatementHandle);
        }
        catch (SQLiteException ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e(ex.getMessage(), ex);
            }
        }
    }
    
    public long getStatementHandle() {
        return this.sqliteStatementHandle;
    }
    
    native long prepare(final long p0, final String p1) throws SQLiteException;
    
    public SQLiteCursor query(final Object[] array) throws SQLiteException {
        if (array != null) {
            this.checkFinalized();
            this.reset(this.sqliteStatementHandle);
            int i = 0;
            int n = 1;
            while (i < array.length) {
                final Object o = array[i];
                if (o == null) {
                    this.bindNull(this.sqliteStatementHandle, n);
                }
                else if (o instanceof Integer) {
                    this.bindInt(this.sqliteStatementHandle, n, (int)o);
                }
                else if (o instanceof Double) {
                    this.bindDouble(this.sqliteStatementHandle, n, (double)o);
                }
                else if (o instanceof String) {
                    this.bindString(this.sqliteStatementHandle, n, (String)o);
                }
                else {
                    if (!(o instanceof Long)) {
                        throw new IllegalArgumentException();
                    }
                    this.bindLong(this.sqliteStatementHandle, n, (long)o);
                }
                ++n;
                ++i;
            }
            return new SQLiteCursor(this);
        }
        throw new IllegalArgumentException();
    }
    
    public void requery() throws SQLiteException {
        this.checkFinalized();
        this.reset(this.sqliteStatementHandle);
    }
    
    native void reset(final long p0) throws SQLiteException;
    
    public int step() throws SQLiteException {
        return this.step(this.sqliteStatementHandle);
    }
    
    native int step(final long p0) throws SQLiteException;
    
    public SQLitePreparedStatement stepThis() throws SQLiteException {
        this.step(this.sqliteStatementHandle);
        return this;
    }
}
