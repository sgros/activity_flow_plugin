// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import androidx.work.impl.utils.StopWorkRunnable;
import androidx.work.impl.utils.StartWorkRunnable;
import androidx.work.WorkerParameters;
import androidx.work.impl.background.systemjob.SystemJobScheduler;
import android.os.Build$VERSION;
import androidx.work.impl.utils.StatusRunnable;
import androidx.work.WorkInfo;
import com.google.common.util.concurrent.ListenableFuture;
import androidx.work.WorkRequest;
import java.util.Arrays;
import androidx.work.impl.background.greedy.GreedyScheduler;
import androidx.work.impl.utils.CancelWorkRunnable;
import androidx.work.Operation;
import androidx.work.impl.utils.ForceStopRunnable;
import androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor;
import androidx.work.Logger;
import androidx.work.R;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.List;
import android.content.BroadcastReceiver$PendingResult;
import androidx.work.impl.utils.Preferences;
import android.content.Context;
import androidx.work.Configuration;
import androidx.work.WorkManager;

public class WorkManagerImpl extends WorkManager
{
    private static WorkManagerImpl sDefaultInstance;
    private static WorkManagerImpl sDelegatedInstance;
    private static final Object sLock;
    private Configuration mConfiguration;
    private Context mContext;
    private boolean mForceStopRunnableCompleted;
    private final WorkManagerLiveDataTracker mLiveDataTracker;
    private Preferences mPreferences;
    private Processor mProcessor;
    private BroadcastReceiver$PendingResult mRescheduleReceiverResult;
    private List<Scheduler> mSchedulers;
    private WorkDatabase mWorkDatabase;
    private TaskExecutor mWorkTaskExecutor;
    
    static {
        sLock = new Object();
    }
    
    public WorkManagerImpl(final Context context, final Configuration configuration, final TaskExecutor taskExecutor) {
        this(context, configuration, taskExecutor, context.getResources().getBoolean(R.bool.workmanager_test_configuration));
    }
    
    public WorkManagerImpl(final Context context, final Configuration configuration, final TaskExecutor taskExecutor, final boolean b) {
        this.mLiveDataTracker = new WorkManagerLiveDataTracker();
        final Context applicationContext = context.getApplicationContext();
        final WorkDatabase create = WorkDatabase.create(applicationContext, b);
        Logger.setLogger(new Logger.LogcatLogger(configuration.getMinimumLoggingLevel()));
        final List<Scheduler> schedulers = this.createSchedulers(applicationContext);
        this.internalInit(context, configuration, taskExecutor, create, schedulers, new Processor(context, configuration, taskExecutor, create, schedulers));
    }
    
    public static WorkManagerImpl getInstance() {
        synchronized (WorkManagerImpl.sLock) {
            if (WorkManagerImpl.sDelegatedInstance != null) {
                return WorkManagerImpl.sDelegatedInstance;
            }
            return WorkManagerImpl.sDefaultInstance;
        }
    }
    
    public static void initialize(final Context context, final Configuration configuration) {
        synchronized (WorkManagerImpl.sLock) {
            if (WorkManagerImpl.sDelegatedInstance != null && WorkManagerImpl.sDefaultInstance != null) {
                throw new IllegalStateException("WorkManager is already initialized.  Did you try to initialize it manually without disabling WorkManagerInitializer? See WorkManager#initialize(Context, Configuration) or the class levelJavadoc for more information.");
            }
            if (WorkManagerImpl.sDelegatedInstance == null) {
                final Context applicationContext = context.getApplicationContext();
                if (WorkManagerImpl.sDefaultInstance == null) {
                    WorkManagerImpl.sDefaultInstance = new WorkManagerImpl(applicationContext, configuration, new WorkManagerTaskExecutor());
                }
                WorkManagerImpl.sDelegatedInstance = WorkManagerImpl.sDefaultInstance;
            }
        }
    }
    
    private void internalInit(Context applicationContext, final Configuration mConfiguration, final TaskExecutor mWorkTaskExecutor, final WorkDatabase mWorkDatabase, final List<Scheduler> mSchedulers, final Processor mProcessor) {
        applicationContext = applicationContext.getApplicationContext();
        this.mContext = applicationContext;
        this.mConfiguration = mConfiguration;
        this.mWorkTaskExecutor = mWorkTaskExecutor;
        this.mWorkDatabase = mWorkDatabase;
        this.mSchedulers = mSchedulers;
        this.mProcessor = mProcessor;
        this.mPreferences = new Preferences(this.mContext);
        this.mForceStopRunnableCompleted = false;
        this.mWorkTaskExecutor.executeOnBackgroundThread(new ForceStopRunnable(applicationContext, this));
    }
    
    @Override
    public Operation cancelAllWorkByTag(final String s) {
        final CancelWorkRunnable forTag = CancelWorkRunnable.forTag(s, this);
        this.mWorkTaskExecutor.executeOnBackgroundThread(forTag);
        return forTag.getOperation();
    }
    
    public List<Scheduler> createSchedulers(final Context context) {
        return Arrays.asList(Schedulers.createBestAvailableBackgroundScheduler(context, this), new GreedyScheduler(context, this));
    }
    
    @Override
    public Operation enqueue(final List<? extends WorkRequest> list) {
        if (!list.isEmpty()) {
            return new WorkContinuationImpl(this, list).enqueue();
        }
        throw new IllegalArgumentException("enqueue needs at least one WorkRequest.");
    }
    
    public Context getApplicationContext() {
        return this.mContext;
    }
    
    public Configuration getConfiguration() {
        return this.mConfiguration;
    }
    
    public Preferences getPreferences() {
        return this.mPreferences;
    }
    
    public Processor getProcessor() {
        return this.mProcessor;
    }
    
    public List<Scheduler> getSchedulers() {
        return this.mSchedulers;
    }
    
    public WorkDatabase getWorkDatabase() {
        return this.mWorkDatabase;
    }
    
    @Override
    public ListenableFuture<List<WorkInfo>> getWorkInfosByTag(final String s) {
        final StatusRunnable<List<WorkInfo>> forTag = StatusRunnable.forTag(this, s);
        this.mWorkTaskExecutor.getBackgroundExecutor().execute(forTag);
        return forTag.getFuture();
    }
    
    public TaskExecutor getWorkTaskExecutor() {
        return this.mWorkTaskExecutor;
    }
    
    public void onForceStopRunnableCompleted() {
        synchronized (WorkManagerImpl.sLock) {
            this.mForceStopRunnableCompleted = true;
            if (this.mRescheduleReceiverResult != null) {
                this.mRescheduleReceiverResult.finish();
                this.mRescheduleReceiverResult = null;
            }
        }
    }
    
    public void rescheduleEligibleWork() {
        if (Build$VERSION.SDK_INT >= 23) {
            SystemJobScheduler.jobSchedulerCancelAll(this.getApplicationContext());
        }
        this.getWorkDatabase().workSpecDao().resetScheduledState();
        Schedulers.schedule(this.getConfiguration(), this.getWorkDatabase(), this.getSchedulers());
    }
    
    public void setReschedulePendingResult(final BroadcastReceiver$PendingResult mRescheduleReceiverResult) {
        synchronized (WorkManagerImpl.sLock) {
            this.mRescheduleReceiverResult = mRescheduleReceiverResult;
            if (this.mForceStopRunnableCompleted) {
                this.mRescheduleReceiverResult.finish();
                this.mRescheduleReceiverResult = null;
            }
        }
    }
    
    public void startWork(final String s) {
        this.startWork(s, null);
    }
    
    public void startWork(final String s, final WorkerParameters.RuntimeExtras runtimeExtras) {
        this.mWorkTaskExecutor.executeOnBackgroundThread(new StartWorkRunnable(this, s, runtimeExtras));
    }
    
    public void stopWork(final String s) {
        this.mWorkTaskExecutor.executeOnBackgroundThread(new StopWorkRunnable(this, s));
    }
}
