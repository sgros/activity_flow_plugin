// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.async;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.AbstractDao;

public class AsyncOperation
{
    public static final int FLAG_MERGE_TX = 1;
    public static final int FLAG_STOP_QUEUE_ON_EXCEPTION = 2;
    public static final int FLAG_TRACK_CREATOR_STACKTRACE = 4;
    private volatile boolean completed;
    final Exception creatorStacktrace;
    final AbstractDao<Object, Object> dao;
    private final Database database;
    final int flags;
    volatile int mergedOperationsCount;
    final Object parameter;
    volatile Object result;
    int sequenceNumber;
    volatile Throwable throwable;
    volatile long timeCompleted;
    volatile long timeStarted;
    final OperationType type;
    
    AsyncOperation(final OperationType type, final AbstractDao<?, ?> dao, final Database database, final Object parameter, final int flags) {
        this.type = type;
        this.flags = flags;
        this.dao = (AbstractDao<Object, Object>)dao;
        this.database = database;
        this.parameter = parameter;
        Exception creatorStacktrace;
        if ((flags & 0x4) != 0x0) {
            creatorStacktrace = new Exception("AsyncOperation was created here");
        }
        else {
            creatorStacktrace = null;
        }
        this.creatorStacktrace = creatorStacktrace;
    }
    
    public Exception getCreatorStacktrace() {
        return this.creatorStacktrace;
    }
    
    Database getDatabase() {
        Database database;
        if (this.database != null) {
            database = this.database;
        }
        else {
            database = this.dao.getDatabase();
        }
        return database;
    }
    
    public long getDuration() {
        if (this.timeCompleted == 0L) {
            throw new DaoException("This operation did not yet complete");
        }
        return this.timeCompleted - this.timeStarted;
    }
    
    public int getMergedOperationsCount() {
        return this.mergedOperationsCount;
    }
    
    public Object getParameter() {
        return this.parameter;
    }
    
    public Object getResult() {
        synchronized (this) {
            if (!this.completed) {
                this.waitForCompletion();
            }
            if (this.throwable != null) {
                throw new AsyncDaoException(this, this.throwable);
            }
            return this.result;
        }
    }
    
    public int getSequenceNumber() {
        return this.sequenceNumber;
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    public long getTimeCompleted() {
        return this.timeCompleted;
    }
    
    public long getTimeStarted() {
        return this.timeStarted;
    }
    
    public OperationType getType() {
        return this.type;
    }
    
    public boolean isCompleted() {
        return this.completed;
    }
    
    public boolean isCompletedSucessfully() {
        return this.completed && this.throwable == null;
    }
    
    public boolean isFailed() {
        return this.throwable != null;
    }
    
    public boolean isMergeTx() {
        final int flags = this.flags;
        boolean b = true;
        if ((flags & 0x1) == 0x0) {
            b = false;
        }
        return b;
    }
    
    boolean isMergeableWith(final AsyncOperation asyncOperation) {
        return asyncOperation != null && this.isMergeTx() && asyncOperation.isMergeTx() && this.getDatabase() == asyncOperation.getDatabase();
    }
    
    void reset() {
        this.timeStarted = 0L;
        this.timeCompleted = 0L;
        this.completed = false;
        this.throwable = null;
        this.result = null;
        this.mergedOperationsCount = 0;
    }
    
    void setCompleted() {
        synchronized (this) {
            this.completed = true;
            this.notifyAll();
        }
    }
    
    public void setThrowable(final Throwable throwable) {
        this.throwable = throwable;
    }
    
    public Object waitForCompletion() {
        synchronized (this) {
            while (!this.completed) {
                try {
                    this.wait();
                    continue;
                }
                catch (InterruptedException ex) {
                    throw new DaoException("Interrupted while waiting for operation to complete", ex);
                }
                break;
            }
            return this.result;
        }
    }
    
    public boolean waitForCompletion(final int n) {
        synchronized (this) {
            if (!this.completed) {
                final long n2 = n;
                try {
                    this.wait(n2);
                }
                catch (InterruptedException ex) {
                    throw new DaoException("Interrupted while waiting for operation to complete", ex);
                }
            }
            return this.completed;
        }
    }
    
    public enum OperationType
    {
        Count, 
        Delete, 
        DeleteAll, 
        DeleteByKey, 
        DeleteInTxArray, 
        DeleteInTxIterable, 
        Insert, 
        InsertInTxArray, 
        InsertInTxIterable, 
        InsertOrReplace, 
        InsertOrReplaceInTxArray, 
        InsertOrReplaceInTxIterable, 
        Load, 
        LoadAll, 
        QueryList, 
        QueryUnique, 
        Refresh, 
        TransactionCallable, 
        TransactionRunnable, 
        Update, 
        UpdateInTxArray, 
        UpdateInTxIterable;
    }
}
