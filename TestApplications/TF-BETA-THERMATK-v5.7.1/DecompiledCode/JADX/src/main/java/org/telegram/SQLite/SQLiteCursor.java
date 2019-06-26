package org.telegram.SQLite;

import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.NativeByteBuffer;

public class SQLiteCursor {
    public static final int FIELD_TYPE_BYTEARRAY = 4;
    public static final int FIELD_TYPE_FLOAT = 2;
    public static final int FIELD_TYPE_INT = 1;
    public static final int FIELD_TYPE_NULL = 5;
    public static final int FIELD_TYPE_STRING = 3;
    private boolean inRow = false;
    private SQLitePreparedStatement preparedStatement;

    public native byte[] columnByteArrayValue(long j, int i);

    public native long columnByteBufferValue(long j, int i);

    public native int columnCount(long j);

    public native double columnDoubleValue(long j, int i);

    public native int columnIntValue(long j, int i);

    public native int columnIsNull(long j, int i);

    public native long columnLongValue(long j, int i);

    public native String columnStringValue(long j, int i);

    public native int columnType(long j, int i);

    public SQLiteCursor(SQLitePreparedStatement sQLitePreparedStatement) {
        this.preparedStatement = sQLitePreparedStatement;
    }

    public boolean isNull(int i) throws SQLiteException {
        checkRow();
        return columnIsNull(this.preparedStatement.getStatementHandle(), i) == 1;
    }

    public SQLitePreparedStatement getPreparedStatement() {
        return this.preparedStatement;
    }

    public int intValue(int i) throws SQLiteException {
        checkRow();
        return columnIntValue(this.preparedStatement.getStatementHandle(), i);
    }

    public double doubleValue(int i) throws SQLiteException {
        checkRow();
        return columnDoubleValue(this.preparedStatement.getStatementHandle(), i);
    }

    public long longValue(int i) throws SQLiteException {
        checkRow();
        return columnLongValue(this.preparedStatement.getStatementHandle(), i);
    }

    public String stringValue(int i) throws SQLiteException {
        checkRow();
        return columnStringValue(this.preparedStatement.getStatementHandle(), i);
    }

    public byte[] byteArrayValue(int i) throws SQLiteException {
        checkRow();
        return columnByteArrayValue(this.preparedStatement.getStatementHandle(), i);
    }

    public NativeByteBuffer byteBufferValue(int i) throws SQLiteException {
        checkRow();
        long columnByteBufferValue = columnByteBufferValue(this.preparedStatement.getStatementHandle(), i);
        return columnByteBufferValue != 0 ? NativeByteBuffer.wrap(columnByteBufferValue) : null;
    }

    public int getTypeOf(int i) throws SQLiteException {
        checkRow();
        return columnType(this.preparedStatement.getStatementHandle(), i);
    }

    public boolean next() throws SQLiteException {
        SQLitePreparedStatement sQLitePreparedStatement = this.preparedStatement;
        int step = sQLitePreparedStatement.step(sQLitePreparedStatement.getStatementHandle());
        if (step == -1) {
            int i = 6;
            while (true) {
                int i2 = i - 1;
                if (i == 0) {
                    break;
                }
                try {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.m27d("sqlite busy, waiting...");
                    }
                    Thread.sleep(500);
                    step = this.preparedStatement.step();
                    if (step == 0) {
                        break;
                    }
                    i = i2;
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
            }
            if (step == -1) {
                throw new SQLiteException("sqlite busy");
            }
        }
        this.inRow = step == 0;
        return this.inRow;
    }

    public long getStatementHandle() {
        return this.preparedStatement.getStatementHandle();
    }

    public int getColumnCount() {
        return columnCount(this.preparedStatement.getStatementHandle());
    }

    public void dispose() {
        this.preparedStatement.dispose();
    }

    /* Access modifiers changed, original: 0000 */
    public void checkRow() throws SQLiteException {
        if (!this.inRow) {
            throw new SQLiteException("You must call next before");
        }
    }
}