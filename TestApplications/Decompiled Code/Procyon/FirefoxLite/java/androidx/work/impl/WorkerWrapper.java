// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import android.annotation.SuppressLint;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.UUID;
import java.util.Collection;
import androidx.work.Data;
import java.util.ArrayList;
import androidx.work.InputMerger;
import androidx.work.impl.utils.PackageManagerHelper;
import androidx.work.impl.background.systemalarm.RescheduleReceiver;
import android.os.Build$VERSION;
import androidx.work.WorkInfo;
import java.util.Iterator;
import androidx.work.Logger;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import androidx.work.impl.model.WorkTagDao;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkSpec;
import java.util.List;
import androidx.work.WorkerParameters;
import androidx.work.ListenableWorker;
import com.google.common.util.concurrent.ListenableFuture;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.model.DependencyDao;
import androidx.work.Configuration;
import android.content.Context;

public class WorkerWrapper implements Runnable
{
    static final String TAG;
    private Context mAppContext;
    private Configuration mConfiguration;
    private DependencyDao mDependencyDao;
    private SettableFuture<Boolean> mFuture;
    ListenableFuture<ListenableWorker.Result> mInnerFuture;
    private volatile boolean mInterrupted;
    ListenableWorker.Result mResult;
    private WorkerParameters.RuntimeExtras mRuntimeExtras;
    private List<Scheduler> mSchedulers;
    private List<String> mTags;
    private WorkDatabase mWorkDatabase;
    private String mWorkDescription;
    WorkSpec mWorkSpec;
    private WorkSpecDao mWorkSpecDao;
    private String mWorkSpecId;
    private WorkTagDao mWorkTagDao;
    private TaskExecutor mWorkTaskExecutor;
    ListenableWorker mWorker;
    
    static {
        TAG = Logger.tagWithPrefix("WorkerWrapper");
    }
    
    WorkerWrapper(final Builder builder) {
        this.mResult = ListenableWorker.Result.failure();
        this.mFuture = SettableFuture.create();
        this.mInnerFuture = null;
        this.mAppContext = builder.mAppContext;
        this.mWorkTaskExecutor = builder.mWorkTaskExecutor;
        this.mWorkSpecId = builder.mWorkSpecId;
        this.mSchedulers = builder.mSchedulers;
        this.mRuntimeExtras = builder.mRuntimeExtras;
        this.mWorker = builder.mWorker;
        this.mConfiguration = builder.mConfiguration;
        this.mWorkDatabase = builder.mWorkDatabase;
        this.mWorkSpecDao = this.mWorkDatabase.workSpecDao();
        this.mDependencyDao = this.mWorkDatabase.dependencyDao();
        this.mWorkTagDao = this.mWorkDatabase.workTagDao();
    }
    
    private void assertBackgroundExecutorThread() {
        if (this.mWorkTaskExecutor.getBackgroundExecutorThread() == Thread.currentThread()) {
            return;
        }
        throw new IllegalStateException("Needs to be executed on the Background executor thread.");
    }
    
    private String createWorkDescription(final List<String> list) {
        final StringBuilder sb = new StringBuilder("Work [ id=");
        sb.append(this.mWorkSpecId);
        sb.append(", tags={ ");
        final Iterator<String> iterator = list.iterator();
        int n = 1;
        while (iterator.hasNext()) {
            final String str = iterator.next();
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(", ");
            }
            sb.append(str);
        }
        sb.append(" } ]");
        return sb.toString();
    }
    
    private void handleResult(final ListenableWorker.Result result) {
        if (result instanceof ListenableWorker.Result.Success) {
            Logger.get().info(WorkerWrapper.TAG, String.format("Worker result SUCCESS for %s", this.mWorkDescription), new Throwable[0]);
            if (this.mWorkSpec.isPeriodic()) {
                this.resetPeriodicAndResolve();
            }
            else {
                this.setSucceededAndResolve();
            }
        }
        else if (result instanceof ListenableWorker.Result.Retry) {
            Logger.get().info(WorkerWrapper.TAG, String.format("Worker result RETRY for %s", this.mWorkDescription), new Throwable[0]);
            this.rescheduleAndResolve();
        }
        else {
            Logger.get().info(WorkerWrapper.TAG, String.format("Worker result FAILURE for %s", this.mWorkDescription), new Throwable[0]);
            if (this.mWorkSpec.isPeriodic()) {
                this.resetPeriodicAndResolve();
            }
            else {
                this.setFailedAndResolve();
            }
        }
    }
    
    private void recursivelyFailWorkAndDependents(final String s) {
        final Iterator<String> iterator = this.mDependencyDao.getDependentWorkIds(s).iterator();
        while (iterator.hasNext()) {
            this.recursivelyFailWorkAndDependents(iterator.next());
        }
        if (this.mWorkSpecDao.getState(s) != WorkInfo.State.CANCELLED) {
            this.mWorkSpecDao.setState(WorkInfo.State.FAILED, s);
        }
    }
    
    private void rescheduleAndResolve() {
        this.mWorkDatabase.beginTransaction();
        try {
            this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
            this.mWorkSpecDao.setPeriodStartTime(this.mWorkSpecId, System.currentTimeMillis());
            if (Build$VERSION.SDK_INT < 23) {
                this.mWorkSpecDao.markWorkSpecScheduled(this.mWorkSpecId, -1L);
            }
            this.mWorkDatabase.setTransactionSuccessful();
        }
        finally {
            this.mWorkDatabase.endTransaction();
            this.resolve(true);
        }
    }
    
    private void resetPeriodicAndResolve() {
        this.mWorkDatabase.beginTransaction();
        try {
            this.mWorkSpecDao.setPeriodStartTime(this.mWorkSpecId, System.currentTimeMillis());
            this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
            this.mWorkSpecDao.resetWorkSpecRunAttemptCount(this.mWorkSpecId);
            if (Build$VERSION.SDK_INT < 23) {
                this.mWorkSpecDao.markWorkSpecScheduled(this.mWorkSpecId, -1L);
            }
            this.mWorkDatabase.setTransactionSuccessful();
        }
        finally {
            this.mWorkDatabase.endTransaction();
            this.resolve(false);
        }
    }
    
    private void resolve(final boolean b) {
        try {
            this.mWorkDatabase.beginTransaction();
            final List<String> allUnfinishedWork = this.mWorkDatabase.workSpecDao().getAllUnfinishedWork();
            if (allUnfinishedWork == null || allUnfinishedWork.isEmpty()) {
                PackageManagerHelper.setComponentEnabled(this.mAppContext, RescheduleReceiver.class, false);
            }
            this.mWorkDatabase.setTransactionSuccessful();
            this.mWorkDatabase.endTransaction();
            this.mFuture.set(b);
        }
        finally {
            this.mWorkDatabase.endTransaction();
        }
    }
    
    private void resolveIncorrectStatus() {
        final WorkInfo.State state = this.mWorkSpecDao.getState(this.mWorkSpecId);
        if (state == WorkInfo.State.RUNNING) {
            Logger.get().debug(WorkerWrapper.TAG, String.format("Status for %s is RUNNING;not doing any work and rescheduling for later execution", this.mWorkSpecId), new Throwable[0]);
            this.resolve(true);
        }
        else {
            Logger.get().debug(WorkerWrapper.TAG, String.format("Status for %s is %s; not doing any work", this.mWorkSpecId, state), new Throwable[0]);
            this.resolve(false);
        }
    }
    
    private void runWorker() {
        if (this.tryCheckForInterruptionAndResolve()) {
            return;
        }
        this.mWorkDatabase.beginTransaction();
        try {
            this.mWorkSpec = this.mWorkSpecDao.getWorkSpec(this.mWorkSpecId);
            if (this.mWorkSpec == null) {
                Logger.get().error(WorkerWrapper.TAG, String.format("Didn't find WorkSpec for id %s", this.mWorkSpecId), new Throwable[0]);
                this.resolve(false);
                return;
            }
            if (this.mWorkSpec.state != WorkInfo.State.ENQUEUED) {
                this.resolveIncorrectStatus();
                this.mWorkDatabase.setTransactionSuccessful();
                Logger.get().debug(WorkerWrapper.TAG, String.format("%s is not in ENQUEUED state. Nothing more to do.", this.mWorkSpec.workerClassName), new Throwable[0]);
                return;
            }
            if (this.mWorkSpec.isPeriodic() || this.mWorkSpec.isBackedOff()) {
                final long currentTimeMillis = System.currentTimeMillis();
                if ((Build$VERSION.SDK_INT >= 23 || this.mWorkSpec.intervalDuration == this.mWorkSpec.flexDuration || this.mWorkSpec.periodStartTime != 0L) && currentTimeMillis < this.mWorkSpec.calculateNextRunTime()) {
                    Logger.get().debug(WorkerWrapper.TAG, String.format("Delaying execution for %s because it is being executed before schedule.", this.mWorkSpec.workerClassName), new Throwable[0]);
                    this.resolve(true);
                    return;
                }
            }
            this.mWorkDatabase.setTransactionSuccessful();
            this.mWorkDatabase.endTransaction();
            Data data;
            if (this.mWorkSpec.isPeriodic()) {
                data = this.mWorkSpec.input;
            }
            else {
                final InputMerger fromClassName = InputMerger.fromClassName(this.mWorkSpec.inputMergerClassName);
                if (fromClassName == null) {
                    Logger.get().error(WorkerWrapper.TAG, String.format("Could not create Input Merger %s", this.mWorkSpec.inputMergerClassName), new Throwable[0]);
                    this.setFailedAndResolve();
                    return;
                }
                final ArrayList<Data> list = new ArrayList<Data>();
                list.add(this.mWorkSpec.input);
                list.addAll((Collection<?>)this.mWorkSpecDao.getInputsFromPrerequisites(this.mWorkSpecId));
                data = fromClassName.merge(list);
            }
            final WorkerParameters workerParameters = new WorkerParameters(UUID.fromString(this.mWorkSpecId), data, this.mTags, this.mRuntimeExtras, this.mWorkSpec.runAttemptCount, this.mConfiguration.getExecutor(), this.mWorkTaskExecutor, this.mConfiguration.getWorkerFactory());
            if (this.mWorker == null) {
                this.mWorker = this.mConfiguration.getWorkerFactory().createWorkerWithDefaultFallback(this.mAppContext, this.mWorkSpec.workerClassName, workerParameters);
            }
            if (this.mWorker == null) {
                Logger.get().error(WorkerWrapper.TAG, String.format("Could not create Worker %s", this.mWorkSpec.workerClassName), new Throwable[0]);
                this.setFailedAndResolve();
                return;
            }
            if (this.mWorker.isUsed()) {
                Logger.get().error(WorkerWrapper.TAG, String.format("Received an already-used Worker %s; WorkerFactory should return new instances", this.mWorkSpec.workerClassName), new Throwable[0]);
                this.setFailedAndResolve();
                return;
            }
            this.mWorker.setUsed();
            if (this.trySetRunning()) {
                if (this.tryCheckForInterruptionAndResolve()) {
                    return;
                }
                final SettableFuture<Object> create = SettableFuture.create();
                this.mWorkTaskExecutor.getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Logger.get().debug(WorkerWrapper.TAG, String.format("Starting work for %s", WorkerWrapper.this.mWorkSpec.workerClassName), new Throwable[0]);
                            WorkerWrapper.this.mInnerFuture = WorkerWrapper.this.mWorker.startWork();
                            create.setFuture(WorkerWrapper.this.mInnerFuture);
                        }
                        catch (Throwable exception) {
                            create.setException(exception);
                        }
                    }
                });
                create.addListener(new Runnable() {
                    final /* synthetic */ String val$workDescription = WorkerWrapper.this.mWorkDescription;
                    
                    @SuppressLint({ "SyntheticAccessor" })
                    @Override
                    public void run() {
                        try {
                            try {
                                final ListenableWorker.Result mResult = (ListenableWorker.Result)create.get();
                                if (mResult == null) {
                                    Logger.get().error(WorkerWrapper.TAG, String.format("%s returned a null result. Treating it as a failure.", WorkerWrapper.this.mWorkSpec.workerClassName), new Throwable[0]);
                                }
                                Logger.get().debug(WorkerWrapper.TAG, String.format("%s returned a %s result.", WorkerWrapper.this.mWorkSpec.workerClassName, mResult), new Throwable[0]);
                                WorkerWrapper.this.mResult = mResult;
                            }
                            finally {}
                        }
                        catch (InterruptedException | ExecutionException ex2) {
                            final Object o;
                            Logger.get().error(WorkerWrapper.TAG, String.format("%s failed because it threw an exception/error", this.val$workDescription), (Throwable)o);
                        }
                        catch (CancellationException ex) {
                            Logger.get().info(WorkerWrapper.TAG, String.format("%s was cancelled", this.val$workDescription), ex);
                        }
                        WorkerWrapper.this.onWorkFinished();
                        return;
                        WorkerWrapper.this.onWorkFinished();
                    }
                }, this.mWorkTaskExecutor.getBackgroundExecutor());
            }
            else {
                this.resolveIncorrectStatus();
            }
        }
        finally {
            this.mWorkDatabase.endTransaction();
        }
    }
    
    private void setFailedAndResolve() {
        this.mWorkDatabase.beginTransaction();
        try {
            this.recursivelyFailWorkAndDependents(this.mWorkSpecId);
            this.mWorkSpecDao.setOutput(this.mWorkSpecId, ((ListenableWorker.Result.Failure)this.mResult).getOutputData());
            this.mWorkDatabase.setTransactionSuccessful();
        }
        finally {
            this.mWorkDatabase.endTransaction();
            this.resolve(false);
        }
    }
    
    private void setSucceededAndResolve() {
        this.mWorkDatabase.beginTransaction();
        try {
            this.mWorkSpecDao.setState(WorkInfo.State.SUCCEEDED, this.mWorkSpecId);
            this.mWorkSpecDao.setOutput(this.mWorkSpecId, ((ListenableWorker.Result.Success)this.mResult).getOutputData());
            final long currentTimeMillis = System.currentTimeMillis();
            for (final String s : this.mDependencyDao.getDependentWorkIds(this.mWorkSpecId)) {
                if (this.mWorkSpecDao.getState(s) == WorkInfo.State.BLOCKED && this.mDependencyDao.hasCompletedAllPrerequisites(s)) {
                    Logger.get().info(WorkerWrapper.TAG, String.format("Setting status to enqueued for %s", s), new Throwable[0]);
                    this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, s);
                    this.mWorkSpecDao.setPeriodStartTime(s, currentTimeMillis);
                }
            }
            this.mWorkDatabase.setTransactionSuccessful();
        }
        finally {
            this.mWorkDatabase.endTransaction();
            this.resolve(false);
        }
    }
    
    private boolean tryCheckForInterruptionAndResolve() {
        if (this.mInterrupted) {
            Logger.get().debug(WorkerWrapper.TAG, String.format("Work interrupted for %s", this.mWorkDescription), new Throwable[0]);
            final WorkInfo.State state = this.mWorkSpecDao.getState(this.mWorkSpecId);
            if (state == null) {
                this.resolve(false);
            }
            else {
                this.resolve(state.isFinished() ^ true);
            }
            return true;
        }
        return false;
    }
    
    private boolean trySetRunning() {
        this.mWorkDatabase.beginTransaction();
        try {
            final WorkInfo.State state = this.mWorkSpecDao.getState(this.mWorkSpecId);
            final WorkInfo.State enqueued = WorkInfo.State.ENQUEUED;
            boolean b = true;
            if (state == enqueued) {
                this.mWorkSpecDao.setState(WorkInfo.State.RUNNING, this.mWorkSpecId);
                this.mWorkSpecDao.incrementWorkSpecRunAttemptCount(this.mWorkSpecId);
            }
            else {
                b = false;
            }
            this.mWorkDatabase.setTransactionSuccessful();
            return b;
        }
        finally {
            this.mWorkDatabase.endTransaction();
        }
    }
    
    public ListenableFuture<Boolean> getFuture() {
        return this.mFuture;
    }
    
    public void interrupt(final boolean b) {
        this.mInterrupted = true;
        this.tryCheckForInterruptionAndResolve();
        if (this.mInnerFuture != null) {
            this.mInnerFuture.cancel(true);
        }
        if (this.mWorker != null) {
            this.mWorker.stop();
        }
    }
    
    void onWorkFinished() {
        this.assertBackgroundExecutorThread();
        final boolean tryCheckForInterruptionAndResolve = this.tryCheckForInterruptionAndResolve();
        int finished = 0;
        final boolean b = false;
        if (!tryCheckForInterruptionAndResolve) {
            try {
                this.mWorkDatabase.beginTransaction();
                final WorkInfo.State state = this.mWorkSpecDao.getState(this.mWorkSpecId);
                if (state == null) {
                    this.resolve(false);
                    finished = 1;
                }
                else if (state == WorkInfo.State.RUNNING) {
                    this.handleResult(this.mResult);
                    finished = (this.mWorkSpecDao.getState(this.mWorkSpecId).isFinished() ? 1 : 0);
                }
                else {
                    finished = (b ? 1 : 0);
                    if (!state.isFinished()) {
                        this.rescheduleAndResolve();
                        finished = (b ? 1 : 0);
                    }
                }
                this.mWorkDatabase.setTransactionSuccessful();
            }
            finally {
                this.mWorkDatabase.endTransaction();
            }
        }
        if (this.mSchedulers != null) {
            if (finished != 0) {
                final Iterator<Scheduler> iterator = this.mSchedulers.iterator();
                while (iterator.hasNext()) {
                    iterator.next().cancel(this.mWorkSpecId);
                }
            }
            Schedulers.schedule(this.mConfiguration, this.mWorkDatabase, this.mSchedulers);
        }
    }
    
    @Override
    public void run() {
        this.mTags = this.mWorkTagDao.getTagsForWorkSpecId(this.mWorkSpecId);
        this.mWorkDescription = this.createWorkDescription(this.mTags);
        this.runWorker();
    }
    
    public static class Builder
    {
        Context mAppContext;
        Configuration mConfiguration;
        WorkerParameters.RuntimeExtras mRuntimeExtras;
        List<Scheduler> mSchedulers;
        WorkDatabase mWorkDatabase;
        String mWorkSpecId;
        TaskExecutor mWorkTaskExecutor;
        ListenableWorker mWorker;
        
        public Builder(final Context context, final Configuration mConfiguration, final TaskExecutor mWorkTaskExecutor, final WorkDatabase mWorkDatabase, final String mWorkSpecId) {
            this.mRuntimeExtras = new WorkerParameters.RuntimeExtras();
            this.mAppContext = context.getApplicationContext();
            this.mWorkTaskExecutor = mWorkTaskExecutor;
            this.mConfiguration = mConfiguration;
            this.mWorkDatabase = mWorkDatabase;
            this.mWorkSpecId = mWorkSpecId;
        }
        
        public WorkerWrapper build() {
            return new WorkerWrapper(this);
        }
        
        public Builder withRuntimeExtras(final WorkerParameters.RuntimeExtras mRuntimeExtras) {
            if (mRuntimeExtras != null) {
                this.mRuntimeExtras = mRuntimeExtras;
            }
            return this;
        }
        
        public Builder withSchedulers(final List<Scheduler> mSchedulers) {
            this.mSchedulers = mSchedulers;
            return this;
        }
    }
}
