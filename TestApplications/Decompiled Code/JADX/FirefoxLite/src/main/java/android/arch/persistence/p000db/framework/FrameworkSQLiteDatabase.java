package android.arch.persistence.p000db.framework;

import android.arch.persistence.p000db.SimpleSQLiteQuery;
import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteQuery;
import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.text.TextUtils;
import android.util.Pair;
import java.io.IOException;
import java.util.List;

/* renamed from: android.arch.persistence.db.framework.FrameworkSQLiteDatabase */
class FrameworkSQLiteDatabase implements SupportSQLiteDatabase {
    private static final String[] CONFLICT_VALUES = new String[]{"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final SQLiteDatabase mDelegate;

    FrameworkSQLiteDatabase(SQLiteDatabase sQLiteDatabase) {
        this.mDelegate = sQLiteDatabase;
    }

    public SupportSQLiteStatement compileStatement(String str) {
        return new FrameworkSQLiteStatement(this.mDelegate.compileStatement(str));
    }

    public void beginTransaction() {
        this.mDelegate.beginTransaction();
    }

    public void endTransaction() {
        this.mDelegate.endTransaction();
    }

    public void setTransactionSuccessful() {
        this.mDelegate.setTransactionSuccessful();
    }

    public boolean inTransaction() {
        return this.mDelegate.inTransaction();
    }

    public Cursor query(String str) {
        return query(new SimpleSQLiteQuery(str));
    }

    public Cursor query(final SupportSQLiteQuery supportSQLiteQuery) {
        return this.mDelegate.rawQueryWithFactory(new CursorFactory() {
            public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
                supportSQLiteQuery.bindTo(new FrameworkSQLiteProgram(sQLiteQuery));
                return new SQLiteCursor(sQLiteCursorDriver, str, sQLiteQuery);
            }
        }, supportSQLiteQuery.getSql(), EMPTY_STRING_ARRAY, null);
    }

    public long insert(String str, int i, ContentValues contentValues) throws SQLException {
        return this.mDelegate.insertWithOnConflict(str, null, contentValues, i);
    }

    public int delete(String str, String str2, Object[] objArr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM ");
        stringBuilder.append(str);
        if (TextUtils.isEmpty(str2)) {
            str = "";
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(" WHERE ");
            stringBuilder2.append(str2);
            str = stringBuilder2.toString();
        }
        stringBuilder.append(str);
        SupportSQLiteStatement compileStatement = compileStatement(stringBuilder.toString());
        SimpleSQLiteQuery.bind(compileStatement, objArr);
        return compileStatement.executeUpdateDelete();
    }

    public int update(String str, int i, ContentValues contentValues, String str2, Object[] objArr) {
        if (contentValues == null || contentValues.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }
        StringBuilder stringBuilder = new StringBuilder(120);
        stringBuilder.append("UPDATE ");
        stringBuilder.append(CONFLICT_VALUES[i]);
        stringBuilder.append(str);
        stringBuilder.append(" SET ");
        int size = contentValues.size();
        if (objArr == null) {
            i = size;
        } else {
            i = objArr.length + size;
        }
        Object[] objArr2 = new Object[i];
        int i2 = 0;
        for (String str3 : contentValues.keySet()) {
            stringBuilder.append(i2 > 0 ? "," : "");
            stringBuilder.append(str3);
            int i3 = i2 + 1;
            objArr2[i2] = contentValues.get(str3);
            stringBuilder.append("=?");
            i2 = i3;
        }
        if (objArr != null) {
            for (int i4 = size; i4 < i; i4++) {
                objArr2[i4] = objArr[i4 - size];
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            stringBuilder.append(" WHERE ");
            stringBuilder.append(str2);
        }
        SupportSQLiteStatement compileStatement = compileStatement(stringBuilder.toString());
        SimpleSQLiteQuery.bind(compileStatement, objArr2);
        return compileStatement.executeUpdateDelete();
    }

    public void execSQL(String str) throws SQLException {
        this.mDelegate.execSQL(str);
    }

    public boolean isOpen() {
        return this.mDelegate.isOpen();
    }

    public String getPath() {
        return this.mDelegate.getPath();
    }

    public List<Pair<String, String>> getAttachedDbs() {
        return this.mDelegate.getAttachedDbs();
    }

    public void close() throws IOException {
        this.mDelegate.close();
    }
}
