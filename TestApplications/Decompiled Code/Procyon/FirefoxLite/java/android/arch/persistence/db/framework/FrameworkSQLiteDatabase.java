// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db.framework;

import java.util.Iterator;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteProgram;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.database.Cursor;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.content.ContentValues;
import android.util.Pair;
import java.util.List;
import android.database.SQLException;
import android.arch.persistence.db.SupportSQLiteProgram;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.text.TextUtils;
import android.arch.persistence.db.SupportSQLiteStatement;
import java.io.IOException;
import android.database.sqlite.SQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteDatabase;

class FrameworkSQLiteDatabase implements SupportSQLiteDatabase
{
    private static final String[] CONFLICT_VALUES;
    private static final String[] EMPTY_STRING_ARRAY;
    private final SQLiteDatabase mDelegate;
    
    static {
        CONFLICT_VALUES = new String[] { "", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE " };
        EMPTY_STRING_ARRAY = new String[0];
    }
    
    FrameworkSQLiteDatabase(final SQLiteDatabase mDelegate) {
        this.mDelegate = mDelegate;
    }
    
    @Override
    public void beginTransaction() {
        this.mDelegate.beginTransaction();
    }
    
    @Override
    public void close() throws IOException {
        this.mDelegate.close();
    }
    
    @Override
    public SupportSQLiteStatement compileStatement(final String s) {
        return new FrameworkSQLiteStatement(this.mDelegate.compileStatement(s));
    }
    
    @Override
    public int delete(String string, final String str, final Object[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(string);
        if (TextUtils.isEmpty((CharSequence)str)) {
            string = "";
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(" WHERE ");
            sb2.append(str);
            string = sb2.toString();
        }
        sb.append(string);
        final SupportSQLiteStatement compileStatement = this.compileStatement(sb.toString());
        SimpleSQLiteQuery.bind(compileStatement, array);
        return compileStatement.executeUpdateDelete();
    }
    
    @Override
    public void endTransaction() {
        this.mDelegate.endTransaction();
    }
    
    @Override
    public void execSQL(final String s) throws SQLException {
        this.mDelegate.execSQL(s);
    }
    
    @Override
    public List<Pair<String, String>> getAttachedDbs() {
        return (List<Pair<String, String>>)this.mDelegate.getAttachedDbs();
    }
    
    @Override
    public String getPath() {
        return this.mDelegate.getPath();
    }
    
    @Override
    public boolean inTransaction() {
        return this.mDelegate.inTransaction();
    }
    
    @Override
    public long insert(final String s, final int n, final ContentValues contentValues) throws SQLException {
        return this.mDelegate.insertWithOnConflict(s, (String)null, contentValues, n);
    }
    
    @Override
    public boolean isOpen() {
        return this.mDelegate.isOpen();
    }
    
    @Override
    public Cursor query(final SupportSQLiteQuery supportSQLiteQuery) {
        return this.mDelegate.rawQueryWithFactory((SQLiteDatabase$CursorFactory)new SQLiteDatabase$CursorFactory() {
            public Cursor newCursor(final SQLiteDatabase sqLiteDatabase, final SQLiteCursorDriver sqLiteCursorDriver, final String s, final SQLiteQuery sqLiteQuery) {
                supportSQLiteQuery.bindTo(new FrameworkSQLiteProgram((SQLiteProgram)sqLiteQuery));
                return (Cursor)new SQLiteCursor(sqLiteCursorDriver, s, sqLiteQuery);
            }
        }, supportSQLiteQuery.getSql(), FrameworkSQLiteDatabase.EMPTY_STRING_ARRAY, (String)null);
    }
    
    @Override
    public Cursor query(final String s) {
        return this.query(new SimpleSQLiteQuery(s));
    }
    
    @Override
    public void setTransactionSuccessful() {
        this.mDelegate.setTransactionSuccessful();
    }
    
    @Override
    public int update(String s, int size, final ContentValues contentValues, final String str, final Object[] array) {
        if (contentValues != null && contentValues.size() != 0) {
            final StringBuilder sb = new StringBuilder(120);
            sb.append("UPDATE ");
            sb.append(FrameworkSQLiteDatabase.CONFLICT_VALUES[size]);
            sb.append(s);
            sb.append(" SET ");
            size = contentValues.size();
            int n;
            if (array == null) {
                n = size;
            }
            else {
                n = array.length + size;
            }
            final Object[] array2 = new Object[n];
            int n2 = 0;
            for (final String str2 : contentValues.keySet()) {
                if (n2 > 0) {
                    s = ",";
                }
                else {
                    s = "";
                }
                sb.append(s);
                sb.append(str2);
                array2[n2] = contentValues.get(str2);
                sb.append("=?");
                ++n2;
            }
            if (array != null) {
                for (int i = size; i < n; ++i) {
                    array2[i] = array[i - size];
                }
            }
            if (!TextUtils.isEmpty((CharSequence)str)) {
                sb.append(" WHERE ");
                sb.append(str);
            }
            final SupportSQLiteStatement compileStatement = this.compileStatement(sb.toString());
            SimpleSQLiteQuery.bind(compileStatement, array2);
            return compileStatement.executeUpdateDelete();
        }
        throw new IllegalArgumentException("Empty values");
    }
}
