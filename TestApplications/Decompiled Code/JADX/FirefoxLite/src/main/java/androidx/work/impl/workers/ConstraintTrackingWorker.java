package androidx.work.impl.workers;

import android.content.Context;
import android.text.TextUtils;
import androidx.work.ListenableWorker;
import androidx.work.ListenableWorker.Result;
import androidx.work.Logger;
import androidx.work.WorkerParameters;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.futures.SettableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import java.util.List;

public class ConstraintTrackingWorker extends ListenableWorker implements WorkConstraintsCallback {
    private static final String TAG = Logger.tagWithPrefix("ConstraintTrkngWrkr");
    volatile boolean mAreConstraintsUnmet = false;
    private ListenableWorker mDelegate;
    SettableFuture<Result> mFuture = SettableFuture.create();
    final Object mLock = new Object();
    private WorkerParameters mWorkerParameters;

    /* renamed from: androidx.work.impl.workers.ConstraintTrackingWorker$1 */
    class C02881 implements Runnable {
        C02881() {
        }

        public void run() {
            ConstraintTrackingWorker.this.setupAndRunConstraintTrackingWork();
        }
    }

    public void onAllConstraintsMet(List<String> list) {
    }

    public ConstraintTrackingWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
        this.mWorkerParameters = workerParameters;
    }

    public ListenableFuture<Result> startWork() {
        getBackgroundExecutor().execute(new C02881());
        return this.mFuture;
    }

    /* Access modifiers changed, original: 0000 */
    public void setupAndRunConstraintTrackingWork() {
        String string = getInputData().getString("androidx.work.impl.workers.ConstraintTrackingWorker.ARGUMENT_CLASS_NAME");
        if (TextUtils.isEmpty(string)) {
            Logger.get().error(TAG, "No worker to delegate to.", new Throwable[0]);
            setFutureFailed();
            return;
        }
        this.mDelegate = getWorkerFactory().createWorkerWithDefaultFallback(getApplicationContext(), string, this.mWorkerParameters);
        if (this.mDelegate == null) {
            Logger.get().debug(TAG, "No worker to delegate to.", new Throwable[0]);
            setFutureFailed();
            return;
        }
        WorkSpec workSpec = getWorkDatabase().workSpecDao().getWorkSpec(getId().toString());
        if (workSpec == null) {
            setFutureFailed();
            return;
        }
        WorkConstraintsTracker workConstraintsTracker = new WorkConstraintsTracker(getApplicationContext(), this);
        workConstraintsTracker.replace(Collections.singletonList(workSpec));
        if (workConstraintsTracker.areAllConstraintsMet(getId().toString())) {
            Logger.get().debug(TAG, String.format("Constraints met for delegate %s", new Object[]{string}), new Throwable[0]);
            try {
                final ListenableFuture startWork = this.mDelegate.startWork();
                startWork.addListener(new Runnable() {
                    public void run() {
                        synchronized (ConstraintTrackingWorker.this.mLock) {
                            if (ConstraintTrackingWorker.this.mAreConstraintsUnmet) {
                                ConstraintTrackingWorker.this.setFutureRetry();
                            } else {
                                ConstraintTrackingWorker.this.mFuture.setFuture(startWork);
                            }
                        }
                    }
                }, getBackgroundExecutor());
            } catch (Throwable th) {
                Logger.get().debug(TAG, String.format("Delegated worker %s threw exception in startWork.", new Object[]{string}), th);
                synchronized (this.mLock) {
                    if (this.mAreConstraintsUnmet) {
                        Logger.get().debug(TAG, "Constraints were unmet, Retrying.", new Throwable[0]);
                        setFutureRetry();
                    } else {
                        setFutureFailed();
                    }
                }
            }
        } else {
            Logger.get().debug(TAG, String.format("Constraints not met for delegate %s. Requesting retry.", new Object[]{string}), new Throwable[0]);
            setFutureRetry();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setFutureFailed() {
        this.mFuture.set(Result.failure());
    }

    /* Access modifiers changed, original: 0000 */
    public void setFutureRetry() {
        this.mFuture.set(Result.retry());
    }

    public void onStopped() {
        super.onStopped();
        if (this.mDelegate != null) {
            this.mDelegate.stop();
        }
    }

    public WorkDatabase getWorkDatabase() {
        return WorkManagerImpl.getInstance().getWorkDatabase();
    }

    public void onAllConstraintsNotMet(List<String> list) {
        Logger.get().debug(TAG, String.format("Constraints changed for %s", new Object[]{list}), new Throwable[0]);
        synchronized (this.mLock) {
            this.mAreConstraintsUnmet = true;
        }
    }
}
