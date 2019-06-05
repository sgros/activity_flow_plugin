// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import java.util.concurrent.ExecutionException;
import com.google.common.util.concurrent.ListenableFuture;
import androidx.work.WorkerParameters;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import androidx.work.Logger;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.List;
import java.util.Map;
import androidx.work.Configuration;
import java.util.Set;
import android.content.Context;

public class Processor implements ExecutionListener
{
    private static final String TAG;
    private Context mAppContext;
    private Set<String> mCancelledIds;
    private Configuration mConfiguration;
    private Map<String, WorkerWrapper> mEnqueuedWorkMap;
    private final Object mLock;
    private final List<ExecutionListener> mOuterListeners;
    private List<Scheduler> mSchedulers;
    private WorkDatabase mWorkDatabase;
    private TaskExecutor mWorkTaskExecutor;
    
    static {
        TAG = Logger.tagWithPrefix("Processor");
    }
    
    public Processor(final Context mAppContext, final Configuration mConfiguration, final TaskExecutor mWorkTaskExecutor, final WorkDatabase mWorkDatabase, final List<Scheduler> mSchedulers) {
        this.mAppContext = mAppContext;
        this.mConfiguration = mConfiguration;
        this.mWorkTaskExecutor = mWorkTaskExecutor;
        this.mWorkDatabase = mWorkDatabase;
        this.mEnqueuedWorkMap = new HashMap<String, WorkerWrapper>();
        this.mSchedulers = mSchedulers;
        this.mCancelledIds = new HashSet<String>();
        this.mOuterListeners = new ArrayList<ExecutionListener>();
        this.mLock = new Object();
    }
    
    public void addExecutionListener(final ExecutionListener executionListener) {
        synchronized (this.mLock) {
            this.mOuterListeners.add(executionListener);
        }
    }
    
    public boolean isCancelled(final String s) {
        synchronized (this.mLock) {
            return this.mCancelledIds.contains(s);
        }
    }
    
    public boolean isEnqueued(final String s) {
        synchronized (this.mLock) {
            return this.mEnqueuedWorkMap.containsKey(s);
        }
    }
    
    @Override
    public void onExecuted(final String s, final boolean b) {
        synchronized (this.mLock) {
            this.mEnqueuedWorkMap.remove(s);
            Logger.get().debug(Processor.TAG, String.format("%s %s executed; reschedule = %s", this.getClass().getSimpleName(), s, b), new Throwable[0]);
            final Iterator<ExecutionListener> iterator = this.mOuterListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onExecuted(s, b);
            }
        }
    }
    
    public void removeExecutionListener(final ExecutionListener executionListener) {
        synchronized (this.mLock) {
            this.mOuterListeners.remove(executionListener);
        }
    }
    
    public boolean startWork(final String s) {
        return this.startWork(s, null);
    }
    
    public boolean startWork(final String s, final WorkerParameters.RuntimeExtras runtimeExtras) {
        synchronized (this.mLock) {
            if (this.mEnqueuedWorkMap.containsKey(s)) {
                Logger.get().debug(Processor.TAG, String.format("Work %s is already enqueued for processing", s), new Throwable[0]);
                return false;
            }
            final WorkerWrapper build = new WorkerWrapper.Builder(this.mAppContext, this.mConfiguration, this.mWorkTaskExecutor, this.mWorkDatabase, s).withSchedulers(this.mSchedulers).withRuntimeExtras(runtimeExtras).build();
            final ListenableFuture<Boolean> future = build.getFuture();
            future.addListener(new FutureListener(this, s, future), this.mWorkTaskExecutor.getMainThreadExecutor());
            this.mEnqueuedWorkMap.put(s, build);
            // monitorexit(this.mLock)
            this.mWorkTaskExecutor.getBackgroundExecutor().execute(build);
            Logger.get().debug(Processor.TAG, String.format("%s: processing %s", this.getClass().getSimpleName(), s), new Throwable[0]);
            return true;
        }
    }
    
    public boolean stopAndCancelWork(final String s) {
        synchronized (this.mLock) {
            Logger.get().debug(Processor.TAG, String.format("Processor cancelling %s", s), new Throwable[0]);
            this.mCancelledIds.add(s);
            final WorkerWrapper workerWrapper = this.mEnqueuedWorkMap.remove(s);
            if (workerWrapper != null) {
                workerWrapper.interrupt(true);
                Logger.get().debug(Processor.TAG, String.format("WorkerWrapper cancelled for %s", s), new Throwable[0]);
                return true;
            }
            Logger.get().debug(Processor.TAG, String.format("WorkerWrapper could not be found for %s", s), new Throwable[0]);
            return false;
        }
    }
    
    public boolean stopWork(final String s) {
        synchronized (this.mLock) {
            Logger.get().debug(Processor.TAG, String.format("Processor stopping %s", s), new Throwable[0]);
            final WorkerWrapper workerWrapper = this.mEnqueuedWorkMap.remove(s);
            if (workerWrapper != null) {
                workerWrapper.interrupt(false);
                Logger.get().debug(Processor.TAG, String.format("WorkerWrapper stopped for %s", s), new Throwable[0]);
                return true;
            }
            Logger.get().debug(Processor.TAG, String.format("WorkerWrapper could not be found for %s", s), new Throwable[0]);
            return false;
        }
    }
    
    private static class FutureListener implements Runnable
    {
        private ExecutionListener mExecutionListener;
        private ListenableFuture<Boolean> mFuture;
        private String mWorkSpecId;
        
        FutureListener(final ExecutionListener mExecutionListener, final String mWorkSpecId, final ListenableFuture<Boolean> mFuture) {
            this.mExecutionListener = mExecutionListener;
            this.mWorkSpecId = mWorkSpecId;
            this.mFuture = mFuture;
        }
        
        @Override
        public void run() {
            boolean booleanValue;
            try {
                booleanValue = this.mFuture.get();
            }
            catch (InterruptedException | ExecutionException ex) {
                booleanValue = true;
            }
            this.mExecutionListener.onExecuted(this.mWorkSpecId, booleanValue);
        }
    }
}
