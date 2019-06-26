// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import net.sqlcipher.Cursor;

public class SQLiteDirectCursorDriver implements SQLiteCursorDriver
{
    private Cursor mCursor;
    private SQLiteDatabase mDatabase;
    private String mEditTable;
    private SQLiteQuery mQuery;
    private String mSql;
    
    public SQLiteDirectCursorDriver(final SQLiteDatabase mDatabase, final String mSql, final String mEditTable) {
        this.mDatabase = mDatabase;
        this.mEditTable = mEditTable;
        this.mSql = mSql;
    }
    
    @Override
    public void cursorClosed() {
        this.mCursor = null;
    }
    
    @Override
    public void cursorDeactivated() {
    }
    
    @Override
    public void cursorRequeried(final android.database.Cursor cursor) {
    }
    
    public Cursor query(final SQLiteDatabase.CursorFactory cursorFactory, final Object[] array) {
        SQLiteQuery sqLiteQuery;
        final SQLiteQuery mQuery = sqLiteQuery = new SQLiteQuery(this.mDatabase, this.mSql, 0, array);
        try {
            mQuery.bindArguments(array);
            if (cursorFactory == null) {
                sqLiteQuery = mQuery;
                sqLiteQuery = mQuery;
                final SQLiteCursor mCursor = new SQLiteCursor(this.mDatabase, this, this.mEditTable, mQuery);
                sqLiteQuery = mQuery;
                this.mCursor = mCursor;
            }
            else {
                sqLiteQuery = mQuery;
                this.mCursor = cursorFactory.newCursor(this.mDatabase, this, this.mEditTable, mQuery);
            }
            sqLiteQuery = mQuery;
            this.mQuery = mQuery;
            sqLiteQuery = null;
            return this.mCursor;
        }
        finally {
            if (sqLiteQuery != null) {
                sqLiteQuery.close();
            }
        }
    }
    
    @Override
    public Cursor query(final SQLiteDatabase.CursorFactory cursorFactory, final String[] array) {
        final SQLiteDatabase mDatabase = this.mDatabase;
        Object mSql = this.mSql;
        int i = 0;
        final SQLiteQuery mQuery = new SQLiteQuery(mDatabase, (String)mSql, 0, array);
        Label_0044: {
            if (array == null) {
                final int length = 0;
                break Label_0044;
            }
            mSql = mQuery;
            try {
                final int length = array.length;
                while (i < length) {
                    final int n = i + 1;
                    mSql = mQuery;
                    mQuery.bindString(n, array[i]);
                    i = n;
                }
                if (cursorFactory == null) {
                    mSql = mQuery;
                    mSql = mQuery;
                    final SQLiteCursor mCursor = new SQLiteCursor(this.mDatabase, this, this.mEditTable, mQuery);
                    mSql = mQuery;
                    this.mCursor = mCursor;
                }
                else {
                    mSql = mQuery;
                    this.mCursor = cursorFactory.newCursor(this.mDatabase, this, this.mEditTable, mQuery);
                }
                mSql = mQuery;
                this.mQuery = mQuery;
                mSql = null;
                return this.mCursor;
            }
            finally {
                if (mSql != null) {
                    ((SQLiteProgram)mSql).close();
                }
            }
        }
    }
    
    @Override
    public void setBindArguments(final String[] array) {
        int n;
        for (int i = 0; i < array.length; i = n) {
            final SQLiteQuery mQuery = this.mQuery;
            n = i + 1;
            mQuery.bindString(n, array[i]);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SQLiteDirectCursorDriver: ");
        sb.append(this.mSql);
        return sb.toString();
    }
}
