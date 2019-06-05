// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import android.util.Log;
import android.os.SystemClock;
import net.sqlcipher.CursorWindow;

public class SQLiteQuery extends SQLiteProgram
{
    private static final String TAG = "Cursor";
    private String[] mBindArgs;
    private Object[] mObjectBindArgs;
    private int mOffsetIndex;
    
    SQLiteQuery(final SQLiteDatabase sqLiteDatabase, final String s, int length, final Object[] mObjectBindArgs) {
        super(sqLiteDatabase, s);
        this.mOffsetIndex = length;
        this.mObjectBindArgs = mObjectBindArgs;
        if (this.mObjectBindArgs != null) {
            length = this.mObjectBindArgs.length;
        }
        else {
            length = 0;
        }
        this.mBindArgs = new String[length];
    }
    
    SQLiteQuery(final SQLiteDatabase sqLiteDatabase, final String s, final int mOffsetIndex, final String[] mBindArgs) {
        super(sqLiteDatabase, s);
        this.mOffsetIndex = mOffsetIndex;
        this.mBindArgs = mBindArgs;
    }
    
    private final native int native_column_count();
    
    private final native String native_column_name(final int p0);
    
    private final native int native_fill_window(final CursorWindow p0, final int p1, final int p2, final int p3, final int p4);
    
    public void bindArguments(final Object[] array) {
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; ++i) {
                final Object o = array[i];
                if (o == null) {
                    this.bindNull(i + 1);
                }
                else if (o instanceof Double) {
                    this.bindDouble(i + 1, (double)o);
                }
                else if (o instanceof Float) {
                    this.bindDouble(i + 1, ((Double)o).floatValue());
                }
                else if (o instanceof Long) {
                    this.bindLong(i + 1, (long)o);
                }
                else if (o instanceof Integer) {
                    this.bindLong(i + 1, ((Long)o).intValue());
                }
                else if (o instanceof Boolean) {
                    long n;
                    if (o) {
                        n = 1L;
                    }
                    else {
                        n = 0L;
                    }
                    this.bindLong(i + 1, n);
                }
                else if (o instanceof byte[]) {
                    this.bindBlob(i + 1, (byte[])o);
                }
                else {
                    this.bindString(i + 1, o.toString());
                }
            }
        }
    }
    
    @Override
    public void bindDouble(final int n, final double d) {
        this.mBindArgs[n - 1] = Double.toString(d);
        if (!this.mClosed) {
            super.bindDouble(n, d);
        }
    }
    
    @Override
    public void bindLong(final int n, final long i) {
        this.mBindArgs[n - 1] = Long.toString(i);
        if (!this.mClosed) {
            super.bindLong(n, i);
        }
    }
    
    @Override
    public void bindNull(final int n) {
        this.mBindArgs[n - 1] = null;
        if (!this.mClosed) {
            super.bindNull(n);
        }
    }
    
    @Override
    public void bindString(final int n, final String s) {
        this.mBindArgs[n - 1] = s;
        if (!this.mClosed) {
            super.bindString(n, s);
        }
    }
    
    int columnCountLocked() {
        this.acquireReference();
        try {
            return this.native_column_count();
        }
        finally {
            this.releaseReference();
        }
    }
    
    String columnNameLocked(final int n) {
        this.acquireReference();
        try {
            return this.native_column_name(n);
        }
        finally {
            this.releaseReference();
        }
    }
    
    int fillWindow(final CursorWindow cursorWindow, int native_fill_window, final int n) {
        SystemClock.uptimeMillis();
        this.mDatabase.lock();
        try {
            this.acquireReference();
            try {
                try {
                    cursorWindow.acquireReference();
                    native_fill_window = this.native_fill_window(cursorWindow, cursorWindow.getStartPosition(), this.mOffsetIndex, native_fill_window, n);
                    if (SQLiteDebug.DEBUG_SQL_STATEMENTS) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("fillWindow(): ");
                        sb.append(this.mSql);
                        Log.d("Cursor", sb.toString());
                    }
                    cursorWindow.releaseReference();
                    return native_fill_window;
                }
                finally {}
            }
            catch (SQLiteDatabaseCorruptException ex) {
                this.mDatabase.onCorruption();
                throw ex;
            }
            catch (IllegalStateException ex2) {
                native_fill_window = 0;
                cursorWindow.releaseReference();
            }
        }
        finally {
            this.releaseReference();
            this.mDatabase.unlock();
        }
    }
    
    void requery() {
        if (this.mBindArgs != null) {
            final int length = this.mBindArgs.length;
            final int n = 0;
            try {
                if (this.mObjectBindArgs != null) {
                    this.bindArguments(this.mObjectBindArgs);
                }
                else {
                    int n2;
                    for (int i = 0; i < length; i = n2) {
                        n2 = i + 1;
                        super.bindString(n2, this.mBindArgs[i]);
                    }
                }
            }
            catch (SQLiteMisuseException cause) {
                final StringBuilder sb = new StringBuilder();
                sb.append("mSql ");
                sb.append(this.mSql);
                final StringBuilder sb2 = new StringBuilder(sb.toString());
                for (int j = n; j < length; ++j) {
                    sb2.append(" ");
                    sb2.append(this.mBindArgs[j]);
                }
                sb2.append(" ");
                throw new IllegalStateException(sb2.toString(), cause);
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SQLiteQuery: ");
        sb.append(this.mSql);
        return sb.toString();
    }
}
