// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import android.util.Log;

class SQLiteCompiledSql
{
    private static final String TAG = "SQLiteCompiledSql";
    SQLiteDatabase mDatabase;
    private boolean mInUse;
    private String mSqlStmt;
    private Throwable mStackTrace;
    long nHandle;
    long nStatement;
    
    SQLiteCompiledSql(final SQLiteDatabase mDatabase, final String mSqlStmt) {
        this.nHandle = 0L;
        this.nStatement = 0L;
        this.mSqlStmt = null;
        this.mStackTrace = null;
        this.mInUse = false;
        if (!mDatabase.isOpen()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("database ");
            sb.append(mDatabase.getPath());
            sb.append(" already closed");
            throw new IllegalStateException(sb.toString());
        }
        this.mDatabase = mDatabase;
        this.mSqlStmt = mSqlStmt;
        this.mStackTrace = new DatabaseObjectNotClosedException().fillInStackTrace();
        this.nHandle = mDatabase.mNativeHandle;
        this.compile(mSqlStmt, true);
    }
    
    private void compile(final String s, final boolean b) {
        if (!this.mDatabase.isOpen()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("database ");
            sb.append(this.mDatabase.getPath());
            sb.append(" already closed");
            throw new IllegalStateException(sb.toString());
        }
        if (b) {
            this.mDatabase.lock();
            try {
                this.native_compile(s);
            }
            finally {
                this.mDatabase.unlock();
            }
        }
    }
    
    private final native void native_compile(final String p0);
    
    private final native void native_finalize();
    
    boolean acquire() {
        synchronized (this) {
            if (this.mInUse) {
                return false;
            }
            this.mInUse = true;
            if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Acquired DbObj (id#");
                sb.append(this.nStatement);
                sb.append(") from DB cache");
                Log.v("SQLiteCompiledSql", sb.toString());
            }
            return true;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            if (this.nStatement == 0L) {
                return;
            }
            if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
                final StringBuilder sb = new StringBuilder();
                sb.append("** warning ** Finalized DbObj (id#");
                sb.append(this.nStatement);
                sb.append(")");
                Log.v("SQLiteCompiledSql", sb.toString());
            }
            final int length = this.mSqlStmt.length();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Releasing statement in a finalizer. Please ensure that you explicitly call close() on your cursor: ");
            final String mSqlStmt = this.mSqlStmt;
            int endIndex;
            if ((endIndex = length) > 100) {
                endIndex = 100;
            }
            sb2.append(mSqlStmt.substring(0, endIndex));
            Log.w("SQLiteCompiledSql", sb2.toString(), this.mStackTrace);
            this.releaseSqlStatement();
        }
        finally {
            super.finalize();
        }
    }
    
    void release() {
        synchronized (this) {
            if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Released DbObj (id#");
                sb.append(this.nStatement);
                sb.append(") back to DB cache");
                Log.v("SQLiteCompiledSql", sb.toString());
            }
            this.mInUse = false;
        }
    }
    
    void releaseSqlStatement() {
        if (this.nStatement != 0L) {
            if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
                final StringBuilder sb = new StringBuilder();
                sb.append("closed and deallocated DbObj (id#");
                sb.append(this.nStatement);
                sb.append(")");
                Log.v("SQLiteCompiledSql", sb.toString());
            }
            try {
                this.mDatabase.lock();
                this.native_finalize();
                this.nStatement = 0L;
            }
            finally {
                this.mDatabase.unlock();
            }
        }
    }
}
