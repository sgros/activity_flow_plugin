// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.SQLite;

import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import org.telegram.tgnet.NativeByteBuffer;

public class SQLiteCursor
{
    public static final int FIELD_TYPE_BYTEARRAY = 4;
    public static final int FIELD_TYPE_FLOAT = 2;
    public static final int FIELD_TYPE_INT = 1;
    public static final int FIELD_TYPE_NULL = 5;
    public static final int FIELD_TYPE_STRING = 3;
    private boolean inRow;
    private SQLitePreparedStatement preparedStatement;
    
    public SQLiteCursor(final SQLitePreparedStatement preparedStatement) {
        this.inRow = false;
        this.preparedStatement = preparedStatement;
    }
    
    public byte[] byteArrayValue(final int n) throws SQLiteException {
        this.checkRow();
        return this.columnByteArrayValue(this.preparedStatement.getStatementHandle(), n);
    }
    
    public NativeByteBuffer byteBufferValue(final int n) throws SQLiteException {
        this.checkRow();
        final long columnByteBufferValue = this.columnByteBufferValue(this.preparedStatement.getStatementHandle(), n);
        if (columnByteBufferValue != 0L) {
            return NativeByteBuffer.wrap(columnByteBufferValue);
        }
        return null;
    }
    
    void checkRow() throws SQLiteException {
        if (this.inRow) {
            return;
        }
        throw new SQLiteException("You must call next before");
    }
    
    native byte[] columnByteArrayValue(final long p0, final int p1);
    
    native long columnByteBufferValue(final long p0, final int p1);
    
    native int columnCount(final long p0);
    
    native double columnDoubleValue(final long p0, final int p1);
    
    native int columnIntValue(final long p0, final int p1);
    
    native int columnIsNull(final long p0, final int p1);
    
    native long columnLongValue(final long p0, final int p1);
    
    native String columnStringValue(final long p0, final int p1);
    
    native int columnType(final long p0, final int p1);
    
    public void dispose() {
        this.preparedStatement.dispose();
    }
    
    public double doubleValue(final int n) throws SQLiteException {
        this.checkRow();
        return this.columnDoubleValue(this.preparedStatement.getStatementHandle(), n);
    }
    
    public int getColumnCount() {
        return this.columnCount(this.preparedStatement.getStatementHandle());
    }
    
    public SQLitePreparedStatement getPreparedStatement() {
        return this.preparedStatement;
    }
    
    public long getStatementHandle() {
        return this.preparedStatement.getStatementHandle();
    }
    
    public int getTypeOf(final int n) throws SQLiteException {
        this.checkRow();
        return this.columnType(this.preparedStatement.getStatementHandle(), n);
    }
    
    public int intValue(final int n) throws SQLiteException {
        this.checkRow();
        return this.columnIntValue(this.preparedStatement.getStatementHandle(), n);
    }
    
    public boolean isNull(int columnIsNull) throws SQLiteException {
        this.checkRow();
        columnIsNull = this.columnIsNull(this.preparedStatement.getStatementHandle(), columnIsNull);
        boolean b = true;
        if (columnIsNull != 1) {
            b = false;
        }
        return b;
    }
    
    public long longValue(final int n) throws SQLiteException {
        this.checkRow();
        return this.columnLongValue(this.preparedStatement.getStatementHandle(), n);
    }
    
    public boolean next() throws SQLiteException {
        final SQLitePreparedStatement preparedStatement = this.preparedStatement;
        int n2;
        int n = n2 = preparedStatement.step(preparedStatement.getStatementHandle());
        if (n == -1) {
            int n3 = 6;
            while (true) {
                n2 = n;
                if (n3 == 0) {
                    break;
                }
                try {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("sqlite busy, waiting...");
                    }
                    Thread.sleep(500L);
                    n2 = this.preparedStatement.step();
                    if ((n = n2) == 0) {
                        break;
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                --n3;
            }
            if (n2 == -1) {
                throw new SQLiteException("sqlite busy");
            }
        }
        return this.inRow = (n2 == 0);
    }
    
    public String stringValue(final int n) throws SQLiteException {
        this.checkRow();
        return this.columnStringValue(this.preparedStatement.getStatementHandle(), n);
    }
}
