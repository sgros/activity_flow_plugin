// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

public abstract class SQLiteClosable
{
    private Object mLock;
    private int mReferenceCount;
    
    public SQLiteClosable() {
        this.mReferenceCount = 1;
        this.mLock = new Object();
    }
    
    private String getObjInfo() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" (");
        if (this instanceof SQLiteDatabase) {
            sb.append("database = ");
            sb.append(((SQLiteDatabase)this).getPath());
        }
        else if (this instanceof SQLiteProgram || this instanceof SQLiteStatement || this instanceof SQLiteQuery) {
            sb.append("mSql = ");
            sb.append(((SQLiteProgram)this).mSql);
        }
        sb.append(") ");
        return sb.toString();
    }
    
    public void acquireReference() {
        synchronized (this.mLock) {
            if (this.mReferenceCount <= 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("attempt to re-open an already-closed object: ");
                sb.append(this.getObjInfo());
                throw new IllegalStateException(sb.toString());
            }
            ++this.mReferenceCount;
        }
    }
    
    protected abstract void onAllReferencesReleased();
    
    protected void onAllReferencesReleasedFromContainer() {
    }
    
    public void releaseReference() {
        synchronized (this.mLock) {
            --this.mReferenceCount;
            if (this.mReferenceCount == 0) {
                this.onAllReferencesReleased();
            }
        }
    }
    
    public void releaseReferenceFromContainer() {
        synchronized (this.mLock) {
            --this.mReferenceCount;
            if (this.mReferenceCount == 0) {
                this.onAllReferencesReleasedFromContainer();
            }
        }
    }
}
