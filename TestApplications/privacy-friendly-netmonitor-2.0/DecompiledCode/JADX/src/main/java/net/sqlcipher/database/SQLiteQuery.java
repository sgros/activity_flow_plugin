package net.sqlcipher.database;

import android.os.SystemClock;
import android.util.Log;
import net.sqlcipher.CursorWindow;

public class SQLiteQuery extends SQLiteProgram {
    private static final String TAG = "Cursor";
    private String[] mBindArgs;
    private Object[] mObjectBindArgs;
    private int mOffsetIndex;

    private final native int native_column_count();

    private final native String native_column_name(int i);

    private final native int native_fill_window(CursorWindow cursorWindow, int i, int i2, int i3, int i4);

    SQLiteQuery(SQLiteDatabase sQLiteDatabase, String str, int i, String[] strArr) {
        super(sQLiteDatabase, str);
        this.mOffsetIndex = i;
        this.mBindArgs = strArr;
    }

    SQLiteQuery(SQLiteDatabase sQLiteDatabase, String str, int i, Object[] objArr) {
        super(sQLiteDatabase, str);
        this.mOffsetIndex = i;
        this.mObjectBindArgs = objArr;
        this.mBindArgs = new String[(this.mObjectBindArgs != null ? this.mObjectBindArgs.length : 0)];
    }

    /* Access modifiers changed, original: 0000 */
    public int fillWindow(CursorWindow cursorWindow, int i, int i2) {
        SystemClock.uptimeMillis();
        this.mDatabase.lock();
        try {
            acquireReference();
            cursorWindow.acquireReference();
            i = native_fill_window(cursorWindow, cursorWindow.getStartPosition(), this.mOffsetIndex, i, i2);
            if (SQLiteDebug.DEBUG_SQL_STATEMENTS) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("fillWindow(): ");
                stringBuilder.append(this.mSql);
                Log.d(str, stringBuilder.toString());
            }
            cursorWindow.releaseReference();
        } catch (IllegalStateException unused) {
            i = 0;
            cursorWindow.releaseReference();
        } catch (SQLiteDatabaseCorruptException e) {
            this.mDatabase.onCorruption();
            throw e;
        } catch (Throwable th) {
            releaseReference();
            this.mDatabase.unlock();
        }
        releaseReference();
        this.mDatabase.unlock();
        return i;
    }

    /* Access modifiers changed, original: 0000 */
    public int columnCountLocked() {
        acquireReference();
        try {
            int native_column_count = native_column_count();
            return native_column_count;
        } finally {
            releaseReference();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public String columnNameLocked(int i) {
        acquireReference();
        try {
            String native_column_name = native_column_name(i);
            return native_column_name;
        } finally {
            releaseReference();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SQLiteQuery: ");
        stringBuilder.append(this.mSql);
        return stringBuilder.toString();
    }

    /* Access modifiers changed, original: 0000 */
    public void requery() {
        if (this.mBindArgs != null) {
            int length = this.mBindArgs.length;
            int i = 0;
            try {
                if (this.mObjectBindArgs != null) {
                    bindArguments(this.mObjectBindArgs);
                    return;
                }
                int i2 = 0;
                while (i2 < length) {
                    int i3 = i2 + 1;
                    super.bindString(i3, this.mBindArgs[i2]);
                    i2 = i3;
                }
            } catch (SQLiteMisuseException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("mSql ");
                stringBuilder.append(this.mSql);
                StringBuilder stringBuilder2 = new StringBuilder(stringBuilder.toString());
                while (i < length) {
                    stringBuilder2.append(" ");
                    stringBuilder2.append(this.mBindArgs[i]);
                    i++;
                }
                stringBuilder2.append(" ");
                throw new IllegalStateException(stringBuilder2.toString(), e);
            }
        }
    }

    public void bindNull(int i) {
        this.mBindArgs[i - 1] = null;
        if (!this.mClosed) {
            super.bindNull(i);
        }
    }

    public void bindLong(int i, long j) {
        this.mBindArgs[i - 1] = Long.toString(j);
        if (!this.mClosed) {
            super.bindLong(i, j);
        }
    }

    public void bindDouble(int i, double d) {
        this.mBindArgs[i - 1] = Double.toString(d);
        if (!this.mClosed) {
            super.bindDouble(i, d);
        }
    }

    public void bindString(int i, String str) {
        this.mBindArgs[i - 1] = str;
        if (!this.mClosed) {
            super.bindString(i, str);
        }
    }

    public void bindArguments(Object[] objArr) {
        if (objArr != null && objArr.length > 0) {
            for (int i = 0; i < objArr.length; i++) {
                Object obj = objArr[i];
                if (obj == null) {
                    bindNull(i + 1);
                } else if (obj instanceof Double) {
                    bindDouble(i + 1, ((Double) obj).doubleValue());
                } else if (obj instanceof Float) {
                    bindDouble(i + 1, Double.valueOf((double) ((Number) obj).floatValue()).doubleValue());
                } else if (obj instanceof Long) {
                    bindLong(i + 1, ((Long) obj).longValue());
                } else if (obj instanceof Integer) {
                    bindLong(i + 1, Long.valueOf((long) ((Number) obj).intValue()).longValue());
                } else if (obj instanceof Boolean) {
                    bindLong(i + 1, ((Boolean) obj).booleanValue() ? 1 : 0);
                } else if (obj instanceof byte[]) {
                    bindBlob(i + 1, (byte[]) obj);
                } else {
                    bindString(i + 1, obj.toString());
                }
            }
        }
    }
}
