// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteStatement;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SharedSQLiteStatement
{
    private final RoomDatabase mDatabase;
    private final AtomicBoolean mLock;
    private volatile SupportSQLiteStatement mStmt;
    
    public SharedSQLiteStatement(final RoomDatabase mDatabase) {
        this.mLock = new AtomicBoolean(false);
        this.mDatabase = mDatabase;
    }
    
    private SupportSQLiteStatement createNewStatement() {
        return this.mDatabase.compileStatement(this.createQuery());
    }
    
    private SupportSQLiteStatement getStmt(final boolean b) {
        SupportSQLiteStatement supportSQLiteStatement;
        if (b) {
            if (this.mStmt == null) {
                this.mStmt = this.createNewStatement();
            }
            supportSQLiteStatement = this.mStmt;
        }
        else {
            supportSQLiteStatement = this.createNewStatement();
        }
        return supportSQLiteStatement;
    }
    
    public SupportSQLiteStatement acquire() {
        this.assertNotMainThread();
        return this.getStmt(this.mLock.compareAndSet(false, true));
    }
    
    protected void assertNotMainThread() {
        this.mDatabase.assertNotMainThread();
    }
    
    protected abstract String createQuery();
    
    public void release(final SupportSQLiteStatement supportSQLiteStatement) {
        if (supportSQLiteStatement == this.mStmt) {
            this.mLock.set(false);
        }
    }
}
