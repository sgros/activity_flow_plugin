// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import android.os.SystemClock;

public class SQLiteStatement extends SQLiteProgram
{
    SQLiteStatement(final SQLiteDatabase sqLiteDatabase, final String s) {
        super(sqLiteDatabase, s);
    }
    
    private final native long native_1x1_long();
    
    private final native String native_1x1_string();
    
    private final native void native_execute();
    
    public void execute() {
        if (!this.mDatabase.isOpen()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("database ");
            sb.append(this.mDatabase.getPath());
            sb.append(" already closed");
            throw new IllegalStateException(sb.toString());
        }
        SystemClock.uptimeMillis();
        this.mDatabase.lock();
        this.acquireReference();
        try {
            this.native_execute();
        }
        finally {
            this.releaseReference();
            this.mDatabase.unlock();
        }
    }
    
    public long executeInsert() {
        if (!this.mDatabase.isOpen()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("database ");
            sb.append(this.mDatabase.getPath());
            sb.append(" already closed");
            throw new IllegalStateException(sb.toString());
        }
        SystemClock.uptimeMillis();
        this.mDatabase.lock();
        this.acquireReference();
        try {
            this.native_execute();
            long lastInsertRow;
            if (this.mDatabase.lastChangeCount() > 0) {
                lastInsertRow = this.mDatabase.lastInsertRow();
            }
            else {
                lastInsertRow = -1L;
            }
            return lastInsertRow;
        }
        finally {
            this.releaseReference();
            this.mDatabase.unlock();
        }
    }
    
    public int executeUpdateDelete() {
        if (!this.mDatabase.isOpen()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("database ");
            sb.append(this.mDatabase.getPath());
            sb.append(" already closed");
            throw new IllegalStateException(sb.toString());
        }
        SystemClock.uptimeMillis();
        this.mDatabase.lock();
        this.acquireReference();
        try {
            this.native_execute();
            return this.mDatabase.lastChangeCount();
        }
        finally {
            this.releaseReference();
            this.mDatabase.unlock();
        }
    }
    
    public long simpleQueryForLong() {
        if (!this.mDatabase.isOpen()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("database ");
            sb.append(this.mDatabase.getPath());
            sb.append(" already closed");
            throw new IllegalStateException(sb.toString());
        }
        SystemClock.uptimeMillis();
        this.mDatabase.lock();
        this.acquireReference();
        try {
            return this.native_1x1_long();
        }
        finally {
            this.releaseReference();
            this.mDatabase.unlock();
        }
    }
    
    public String simpleQueryForString() {
        if (!this.mDatabase.isOpen()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("database ");
            sb.append(this.mDatabase.getPath());
            sb.append(" already closed");
            throw new IllegalStateException(sb.toString());
        }
        SystemClock.uptimeMillis();
        this.mDatabase.lock();
        this.acquireReference();
        try {
            return this.native_1x1_string();
        }
        finally {
            this.releaseReference();
            this.mDatabase.unlock();
        }
    }
}
