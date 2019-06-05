// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.WorkDatabase;
import androidx.work.WorkInfo;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;

public class StopWorkRunnable implements Runnable
{
    private static final String TAG;
    private WorkManagerImpl mWorkManagerImpl;
    private String mWorkSpecId;
    
    static {
        TAG = Logger.tagWithPrefix("StopWorkRunnable");
    }
    
    public StopWorkRunnable(final WorkManagerImpl mWorkManagerImpl, final String mWorkSpecId) {
        this.mWorkManagerImpl = mWorkManagerImpl;
        this.mWorkSpecId = mWorkSpecId;
    }
    
    @Override
    public void run() {
        final WorkDatabase workDatabase = this.mWorkManagerImpl.getWorkDatabase();
        final WorkSpecDao workSpecDao = workDatabase.workSpecDao();
        workDatabase.beginTransaction();
        try {
            if (workSpecDao.getState(this.mWorkSpecId) == WorkInfo.State.RUNNING) {
                workSpecDao.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
            }
            Logger.get().debug(StopWorkRunnable.TAG, String.format("StopWorkRunnable for %s; Processor.stopWork = %s", this.mWorkSpecId, this.mWorkManagerImpl.getProcessor().stopWork(this.mWorkSpecId)), new Throwable[0]);
            workDatabase.setTransactionSuccessful();
        }
        finally {
            workDatabase.endTransaction();
        }
    }
}
