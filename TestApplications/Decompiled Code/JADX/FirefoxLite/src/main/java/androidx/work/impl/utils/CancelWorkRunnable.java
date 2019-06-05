package androidx.work.impl.utils;

import androidx.work.Operation;
import androidx.work.Operation.State.FAILURE;
import androidx.work.WorkInfo.State;
import androidx.work.impl.OperationImpl;
import androidx.work.impl.Scheduler;
import androidx.work.impl.Schedulers;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.WorkSpecDao;

public abstract class CancelWorkRunnable implements Runnable {
    private final OperationImpl mOperation = new OperationImpl();

    public abstract void runInternal();

    public Operation getOperation() {
        return this.mOperation;
    }

    public void run() {
        try {
            runInternal();
            this.mOperation.setState(Operation.SUCCESS);
        } catch (Throwable th) {
            this.mOperation.setState(new FAILURE(th));
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void cancel(WorkManagerImpl workManagerImpl, String str) {
        recursivelyCancelWorkAndDependents(workManagerImpl.getWorkDatabase(), str);
        workManagerImpl.getProcessor().stopAndCancelWork(str);
        for (Scheduler cancel : workManagerImpl.getSchedulers()) {
            cancel.cancel(str);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void reschedulePendingWorkers(WorkManagerImpl workManagerImpl) {
        Schedulers.schedule(workManagerImpl.getConfiguration(), workManagerImpl.getWorkDatabase(), workManagerImpl.getSchedulers());
    }

    private void recursivelyCancelWorkAndDependents(WorkDatabase workDatabase, String str) {
        WorkSpecDao workSpecDao = workDatabase.workSpecDao();
        for (String recursivelyCancelWorkAndDependents : workDatabase.dependencyDao().getDependentWorkIds(str)) {
            recursivelyCancelWorkAndDependents(workDatabase, recursivelyCancelWorkAndDependents);
        }
        State state = workSpecDao.getState(str);
        if (state != State.SUCCEEDED && state != State.FAILED) {
            workSpecDao.setState(State.CANCELLED, str);
        }
    }

    public static CancelWorkRunnable forTag(final String str, final WorkManagerImpl workManagerImpl) {
        return new CancelWorkRunnable() {
            /* Access modifiers changed, original: 0000 */
            public void runInternal() {
                WorkDatabase workDatabase = workManagerImpl.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    for (String cancel : workDatabase.workSpecDao().getUnfinishedWorkWithTag(str)) {
                        cancel(workManagerImpl, cancel);
                    }
                    workDatabase.setTransactionSuccessful();
                    reschedulePendingWorkers(workManagerImpl);
                } finally {
                    workDatabase.endTransaction();
                }
            }
        };
    }

    public static CancelWorkRunnable forName(final String str, final WorkManagerImpl workManagerImpl, final boolean z) {
        return new CancelWorkRunnable() {
            /* Access modifiers changed, original: 0000 */
            public void runInternal() {
                WorkDatabase workDatabase = workManagerImpl.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    for (String cancel : workDatabase.workSpecDao().getUnfinishedWorkWithName(str)) {
                        cancel(workManagerImpl, cancel);
                    }
                    workDatabase.setTransactionSuccessful();
                    if (z) {
                        reschedulePendingWorkers(workManagerImpl);
                    }
                } finally {
                    workDatabase.endTransaction();
                }
            }
        };
    }
}
