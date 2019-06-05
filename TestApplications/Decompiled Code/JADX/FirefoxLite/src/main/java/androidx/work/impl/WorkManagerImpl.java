package androidx.work.impl;

import android.content.BroadcastReceiver.PendingResult;
import android.content.Context;
import android.os.Build.VERSION;
import androidx.work.C0267R;
import androidx.work.Configuration;
import androidx.work.Logger;
import androidx.work.Logger.LogcatLogger;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.WorkerParameters.RuntimeExtras;
import androidx.work.impl.background.greedy.GreedyScheduler;
import androidx.work.impl.background.systemjob.SystemJobScheduler;
import androidx.work.impl.utils.CancelWorkRunnable;
import androidx.work.impl.utils.ForceStopRunnable;
import androidx.work.impl.utils.Preferences;
import androidx.work.impl.utils.StartWorkRunnable;
import androidx.work.impl.utils.StatusRunnable;
import androidx.work.impl.utils.StopWorkRunnable;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Arrays;
import java.util.List;

public class WorkManagerImpl extends WorkManager {
    private static WorkManagerImpl sDefaultInstance;
    private static WorkManagerImpl sDelegatedInstance;
    private static final Object sLock = new Object();
    private Configuration mConfiguration;
    private Context mContext;
    private boolean mForceStopRunnableCompleted;
    private final WorkManagerLiveDataTracker mLiveDataTracker;
    private Preferences mPreferences;
    private Processor mProcessor;
    private PendingResult mRescheduleReceiverResult;
    private List<Scheduler> mSchedulers;
    private WorkDatabase mWorkDatabase;
    private TaskExecutor mWorkTaskExecutor;

    public static WorkManagerImpl getInstance() {
        synchronized (sLock) {
            WorkManagerImpl workManagerImpl;
            if (sDelegatedInstance != null) {
                workManagerImpl = sDelegatedInstance;
                return workManagerImpl;
            }
            workManagerImpl = sDefaultInstance;
            return workManagerImpl;
        }
    }

    public static void initialize(Context context, Configuration configuration) {
        synchronized (sLock) {
            if (sDelegatedInstance != null) {
                if (sDefaultInstance != null) {
                    throw new IllegalStateException("WorkManager is already initialized.  Did you try to initialize it manually without disabling WorkManagerInitializer? See WorkManager#initialize(Context, Configuration) or the class levelJavadoc for more information.");
                }
            }
            if (sDelegatedInstance == null) {
                context = context.getApplicationContext();
                if (sDefaultInstance == null) {
                    sDefaultInstance = new WorkManagerImpl(context, configuration, new WorkManagerTaskExecutor());
                }
                sDelegatedInstance = sDefaultInstance;
            }
        }
    }

    public WorkManagerImpl(Context context, Configuration configuration, TaskExecutor taskExecutor) {
        this(context, configuration, taskExecutor, context.getResources().getBoolean(C0267R.bool.workmanager_test_configuration));
    }

    public WorkManagerImpl(Context context, Configuration configuration, TaskExecutor taskExecutor, boolean z) {
        this.mLiveDataTracker = new WorkManagerLiveDataTracker();
        Context applicationContext = context.getApplicationContext();
        WorkDatabase create = WorkDatabase.create(applicationContext, z);
        Logger.setLogger(new LogcatLogger(configuration.getMinimumLoggingLevel()));
        Context context2 = context;
        Configuration configuration2 = configuration;
        TaskExecutor taskExecutor2 = taskExecutor;
        WorkDatabase workDatabase = create;
        List createSchedulers = createSchedulers(applicationContext);
        internalInit(context2, configuration2, taskExecutor2, workDatabase, createSchedulers, new Processor(context2, configuration2, taskExecutor2, workDatabase, createSchedulers));
    }

    public Context getApplicationContext() {
        return this.mContext;
    }

    public WorkDatabase getWorkDatabase() {
        return this.mWorkDatabase;
    }

    public Configuration getConfiguration() {
        return this.mConfiguration;
    }

    public List<Scheduler> getSchedulers() {
        return this.mSchedulers;
    }

    public Processor getProcessor() {
        return this.mProcessor;
    }

    public TaskExecutor getWorkTaskExecutor() {
        return this.mWorkTaskExecutor;
    }

    public Preferences getPreferences() {
        return this.mPreferences;
    }

    public Operation enqueue(List<? extends WorkRequest> list) {
        if (!list.isEmpty()) {
            return new WorkContinuationImpl(this, list).enqueue();
        }
        throw new IllegalArgumentException("enqueue needs at least one WorkRequest.");
    }

    public Operation cancelAllWorkByTag(String str) {
        CancelWorkRunnable forTag = CancelWorkRunnable.forTag(str, this);
        this.mWorkTaskExecutor.executeOnBackgroundThread(forTag);
        return forTag.getOperation();
    }

    public ListenableFuture<List<WorkInfo>> getWorkInfosByTag(String str) {
        StatusRunnable forTag = StatusRunnable.forTag(this, str);
        this.mWorkTaskExecutor.getBackgroundExecutor().execute(forTag);
        return forTag.getFuture();
    }

    public void startWork(String str) {
        startWork(str, null);
    }

    public void startWork(String str, RuntimeExtras runtimeExtras) {
        this.mWorkTaskExecutor.executeOnBackgroundThread(new StartWorkRunnable(this, str, runtimeExtras));
    }

    public void stopWork(String str) {
        this.mWorkTaskExecutor.executeOnBackgroundThread(new StopWorkRunnable(this, str));
    }

    public void rescheduleEligibleWork() {
        if (VERSION.SDK_INT >= 23) {
            SystemJobScheduler.jobSchedulerCancelAll(getApplicationContext());
        }
        getWorkDatabase().workSpecDao().resetScheduledState();
        Schedulers.schedule(getConfiguration(), getWorkDatabase(), getSchedulers());
    }

    public void onForceStopRunnableCompleted() {
        synchronized (sLock) {
            this.mForceStopRunnableCompleted = true;
            if (this.mRescheduleReceiverResult != null) {
                this.mRescheduleReceiverResult.finish();
                this.mRescheduleReceiverResult = null;
            }
        }
    }

    public void setReschedulePendingResult(PendingResult pendingResult) {
        synchronized (sLock) {
            this.mRescheduleReceiverResult = pendingResult;
            if (this.mForceStopRunnableCompleted) {
                this.mRescheduleReceiverResult.finish();
                this.mRescheduleReceiverResult = null;
            }
        }
    }

    private void internalInit(Context context, Configuration configuration, TaskExecutor taskExecutor, WorkDatabase workDatabase, List<Scheduler> list, Processor processor) {
        context = context.getApplicationContext();
        this.mContext = context;
        this.mConfiguration = configuration;
        this.mWorkTaskExecutor = taskExecutor;
        this.mWorkDatabase = workDatabase;
        this.mSchedulers = list;
        this.mProcessor = processor;
        this.mPreferences = new Preferences(this.mContext);
        this.mForceStopRunnableCompleted = false;
        this.mWorkTaskExecutor.executeOnBackgroundThread(new ForceStopRunnable(context, this));
    }

    public List<Scheduler> createSchedulers(Context context) {
        return Arrays.asList(new Scheduler[]{Schedulers.createBestAvailableBackgroundScheduler(context, this), new GreedyScheduler(context, this)});
    }
}
