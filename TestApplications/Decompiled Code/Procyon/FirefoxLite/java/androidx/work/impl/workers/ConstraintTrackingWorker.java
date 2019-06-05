// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.workers;

import androidx.work.impl.model.WorkSpec;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import android.text.TextUtils;
import java.util.List;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.WorkDatabase;
import android.content.Context;
import androidx.work.Logger;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.ListenableWorker;

public class ConstraintTrackingWorker extends ListenableWorker implements WorkConstraintsCallback
{
    private static final String TAG;
    volatile boolean mAreConstraintsUnmet;
    private ListenableWorker mDelegate;
    SettableFuture<Result> mFuture;
    final Object mLock;
    private WorkerParameters mWorkerParameters;
    
    static {
        TAG = Logger.tagWithPrefix("ConstraintTrkngWrkr");
    }
    
    public ConstraintTrackingWorker(final Context context, final WorkerParameters mWorkerParameters) {
        super(context, mWorkerParameters);
        this.mWorkerParameters = mWorkerParameters;
        this.mLock = new Object();
        this.mAreConstraintsUnmet = false;
        this.mFuture = SettableFuture.create();
    }
    
    public WorkDatabase getWorkDatabase() {
        return WorkManagerImpl.getInstance().getWorkDatabase();
    }
    
    @Override
    public void onAllConstraintsMet(final List<String> list) {
    }
    
    @Override
    public void onAllConstraintsNotMet(final List<String> list) {
        Logger.get().debug(ConstraintTrackingWorker.TAG, String.format("Constraints changed for %s", list), new Throwable[0]);
        synchronized (this.mLock) {
            this.mAreConstraintsUnmet = true;
        }
    }
    
    @Override
    public void onStopped() {
        super.onStopped();
        if (this.mDelegate != null) {
            this.mDelegate.stop();
        }
    }
    
    void setFutureFailed() {
        this.mFuture.set(Result.failure());
    }
    
    void setFutureRetry() {
        this.mFuture.set(Result.retry());
    }
    
    void setupAndRunConstraintTrackingWork() {
        final String string = this.getInputData().getString("androidx.work.impl.workers.ConstraintTrackingWorker.ARGUMENT_CLASS_NAME");
        if (TextUtils.isEmpty((CharSequence)string)) {
            Logger.get().error(ConstraintTrackingWorker.TAG, "No worker to delegate to.", new Throwable[0]);
            this.setFutureFailed();
            return;
        }
        this.mDelegate = this.getWorkerFactory().createWorkerWithDefaultFallback(this.getApplicationContext(), string, this.mWorkerParameters);
        if (this.mDelegate == null) {
            Logger.get().debug(ConstraintTrackingWorker.TAG, "No worker to delegate to.", new Throwable[0]);
            this.setFutureFailed();
            return;
        }
        final WorkSpec workSpec = this.getWorkDatabase().workSpecDao().getWorkSpec(this.getId().toString());
        if (workSpec == null) {
            this.setFutureFailed();
            return;
        }
        final WorkConstraintsTracker workConstraintsTracker = new WorkConstraintsTracker(this.getApplicationContext(), this);
        workConstraintsTracker.replace(Collections.singletonList(workSpec));
        if (workConstraintsTracker.areAllConstraintsMet(this.getId().toString())) {
            Logger.get().debug(ConstraintTrackingWorker.TAG, String.format("Constraints met for delegate %s", string), new Throwable[0]);
            try {
                final ListenableFuture<Result> startWork = this.mDelegate.startWork();
                startWork.addListener(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (ConstraintTrackingWorker.this.mLock) {
                            if (ConstraintTrackingWorker.this.mAreConstraintsUnmet) {
                                ConstraintTrackingWorker.this.setFutureRetry();
                            }
                            else {
                                ConstraintTrackingWorker.this.mFuture.setFuture(startWork);
                            }
                        }
                    }
                }, this.getBackgroundExecutor());
                return;
            }
            catch (Throwable t) {
                Logger.get().debug(ConstraintTrackingWorker.TAG, String.format("Delegated worker %s threw exception in startWork.", string), t);
                synchronized (this.mLock) {
                    if (this.mAreConstraintsUnmet) {
                        Logger.get().debug(ConstraintTrackingWorker.TAG, "Constraints were unmet, Retrying.", new Throwable[0]);
                        this.setFutureRetry();
                    }
                    else {
                        this.setFutureFailed();
                    }
                }
            }
        }
        Logger.get().debug(ConstraintTrackingWorker.TAG, String.format("Constraints not met for delegate %s. Requesting retry.", string), new Throwable[0]);
        this.setFutureRetry();
    }
    
    @Override
    public ListenableFuture<Result> startWork() {
        this.getBackgroundExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ConstraintTrackingWorker.this.setupAndRunConstraintTrackingWork();
            }
        });
        return this.mFuture;
    }
}
