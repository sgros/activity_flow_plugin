// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemjob;

import java.util.Arrays;
import androidx.work.WorkerParameters;
import android.os.Build$VERSION;
import android.text.TextUtils;
import android.app.Application;
import java.util.HashMap;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;
import android.app.job.JobParameters;
import java.util.Map;
import androidx.work.impl.ExecutionListener;
import android.app.job.JobService;

public class SystemJobService extends JobService implements ExecutionListener
{
    private static final String TAG;
    private final Map<String, JobParameters> mJobParameters;
    private WorkManagerImpl mWorkManagerImpl;
    
    static {
        TAG = Logger.tagWithPrefix("SystemJobService");
    }
    
    public SystemJobService() {
        this.mJobParameters = new HashMap<String, JobParameters>();
    }
    
    public void onCreate() {
        super.onCreate();
        this.mWorkManagerImpl = WorkManagerImpl.getInstance();
        if (this.mWorkManagerImpl == null) {
            if (!Application.class.equals(this.getApplication().getClass())) {
                throw new IllegalStateException("WorkManager needs to be initialized via a ContentProvider#onCreate() or an Application#onCreate().");
            }
            Logger.get().warning(SystemJobService.TAG, "Could not find WorkManager instance; this may be because an auto-backup is in progress. Ignoring JobScheduler commands for now. Please make sure that you are initializing WorkManager if you have manually disabled WorkManagerInitializer.", new Throwable[0]);
        }
        else {
            this.mWorkManagerImpl.getProcessor().addExecutionListener(this);
        }
    }
    
    public void onDestroy() {
        super.onDestroy();
        if (this.mWorkManagerImpl != null) {
            this.mWorkManagerImpl.getProcessor().removeExecutionListener(this);
        }
    }
    
    public void onExecuted(final String s, final boolean b) {
        Logger.get().debug(SystemJobService.TAG, String.format("%s executed on JobScheduler", s), new Throwable[0]);
        synchronized (this.mJobParameters) {
            final JobParameters jobParameters = this.mJobParameters.remove(s);
            // monitorexit(this.mJobParameters)
            if (jobParameters != null) {
                this.jobFinished(jobParameters, b);
            }
        }
    }
    
    public boolean onStartJob(final JobParameters jobParameters) {
        if (this.mWorkManagerImpl == null) {
            Logger.get().debug(SystemJobService.TAG, "WorkManager is not initialized; requesting retry.", new Throwable[0]);
            this.jobFinished(jobParameters, true);
            return false;
        }
        final String string = jobParameters.getExtras().getString("EXTRA_WORK_SPEC_ID");
        if (TextUtils.isEmpty((CharSequence)string)) {
            Logger.get().error(SystemJobService.TAG, "WorkSpec id not found!", new Throwable[0]);
            return false;
        }
        Object mJobParameters = this.mJobParameters;
        synchronized (mJobParameters) {
            if (this.mJobParameters.containsKey(string)) {
                Logger.get().debug(SystemJobService.TAG, String.format("Job is already being executed by SystemJobService: %s", string), new Throwable[0]);
                return false;
            }
            Logger.get().debug(SystemJobService.TAG, String.format("onStartJob for %s", string), new Throwable[0]);
            this.mJobParameters.put(string, jobParameters);
            // monitorexit(mJobParameters)
            mJobParameters = null;
            if (Build$VERSION.SDK_INT >= 24) {
                final WorkerParameters.RuntimeExtras runtimeExtras = new WorkerParameters.RuntimeExtras();
                if (jobParameters.getTriggeredContentUris() != null) {
                    runtimeExtras.triggeredContentUris = Arrays.asList(jobParameters.getTriggeredContentUris());
                }
                if (jobParameters.getTriggeredContentAuthorities() != null) {
                    runtimeExtras.triggeredContentAuthorities = Arrays.asList(jobParameters.getTriggeredContentAuthorities());
                }
                mJobParameters = runtimeExtras;
                if (Build$VERSION.SDK_INT >= 28) {
                    runtimeExtras.network = jobParameters.getNetwork();
                    mJobParameters = runtimeExtras;
                }
            }
            this.mWorkManagerImpl.startWork(string, (WorkerParameters.RuntimeExtras)mJobParameters);
            return true;
        }
    }
    
    public boolean onStopJob(final JobParameters jobParameters) {
        if (this.mWorkManagerImpl == null) {
            Logger.get().debug(SystemJobService.TAG, "WorkManager is not initialized; requesting retry.", new Throwable[0]);
            return true;
        }
        final String string = jobParameters.getExtras().getString("EXTRA_WORK_SPEC_ID");
        if (TextUtils.isEmpty((CharSequence)string)) {
            Logger.get().error(SystemJobService.TAG, "WorkSpec id not found!", new Throwable[0]);
            return false;
        }
        Logger.get().debug(SystemJobService.TAG, String.format("onStopJob for %s", string), new Throwable[0]);
        synchronized (this.mJobParameters) {
            this.mJobParameters.remove(string);
            // monitorexit(this.mJobParameters)
            this.mWorkManagerImpl.stopWork(string);
            return this.mWorkManagerImpl.getProcessor().isCancelled(string) ^ true;
        }
    }
}
