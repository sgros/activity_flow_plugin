// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import androidx.work.impl.Schedulers;
import androidx.work.Operation;
import androidx.work.impl.Scheduler;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.WorkInfo;
import java.util.Iterator;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.OperationImpl;

public abstract class CancelWorkRunnable implements Runnable
{
    private final OperationImpl mOperation;
    
    public CancelWorkRunnable() {
        this.mOperation = new OperationImpl();
    }
    
    public static CancelWorkRunnable forName(final String s, final WorkManagerImpl workManagerImpl, final boolean b) {
        return new CancelWorkRunnable() {
            @Override
            void runInternal() {
                final WorkDatabase workDatabase = workManagerImpl.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    final Iterator<String> iterator = workDatabase.workSpecDao().getUnfinishedWorkWithName(s).iterator();
                    while (iterator.hasNext()) {
                        this.cancel(workManagerImpl, iterator.next());
                    }
                    workDatabase.setTransactionSuccessful();
                    workDatabase.endTransaction();
                    if (b) {
                        this.reschedulePendingWorkers(workManagerImpl);
                    }
                }
                finally {
                    workDatabase.endTransaction();
                }
            }
        };
    }
    
    public static CancelWorkRunnable forTag(final String s, final WorkManagerImpl workManagerImpl) {
        return new CancelWorkRunnable() {
            @Override
            void runInternal() {
                final WorkDatabase workDatabase = workManagerImpl.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    final Iterator<String> iterator = workDatabase.workSpecDao().getUnfinishedWorkWithTag(s).iterator();
                    while (iterator.hasNext()) {
                        this.cancel(workManagerImpl, iterator.next());
                    }
                    workDatabase.setTransactionSuccessful();
                    workDatabase.endTransaction();
                    this.reschedulePendingWorkers(workManagerImpl);
                }
                finally {
                    workDatabase.endTransaction();
                }
            }
        };
    }
    
    private void recursivelyCancelWorkAndDependents(final WorkDatabase workDatabase, final String s) {
        final WorkSpecDao workSpecDao = workDatabase.workSpecDao();
        final Iterator<String> iterator = workDatabase.dependencyDao().getDependentWorkIds(s).iterator();
        while (iterator.hasNext()) {
            this.recursivelyCancelWorkAndDependents(workDatabase, iterator.next());
        }
        final WorkInfo.State state = workSpecDao.getState(s);
        if (state != WorkInfo.State.SUCCEEDED && state != WorkInfo.State.FAILED) {
            workSpecDao.setState(WorkInfo.State.CANCELLED, s);
        }
    }
    
    void cancel(final WorkManagerImpl workManagerImpl, final String s) {
        this.recursivelyCancelWorkAndDependents(workManagerImpl.getWorkDatabase(), s);
        workManagerImpl.getProcessor().stopAndCancelWork(s);
        final Iterator<Scheduler> iterator = workManagerImpl.getSchedulers().iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel(s);
        }
    }
    
    public Operation getOperation() {
        return this.mOperation;
    }
    
    void reschedulePendingWorkers(final WorkManagerImpl workManagerImpl) {
        Schedulers.schedule(workManagerImpl.getConfiguration(), workManagerImpl.getWorkDatabase(), workManagerImpl.getSchedulers());
    }
    
    @Override
    public void run() {
        try {
            this.runInternal();
            this.mOperation.setState((Operation.State)Operation.SUCCESS);
        }
        catch (Throwable t) {
            this.mOperation.setState((Operation.State)new Operation.State.FAILURE(t));
        }
    }
    
    abstract void runInternal();
}
